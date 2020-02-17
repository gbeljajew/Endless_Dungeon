/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game.GameConstants;
import game.battle.BattleLog;
import game.battle.attack.Attack;
import game.battle.attack.AttackResult;
import game.battle.attack.Damage;
import game.battle.attack.ElementeEnum;
import game.battle.attack.StatusAligment;
import game.battle.attack.StatusAligments;
import game.hero.BarInfo;
import game.hero.HeroClass;
import game.hero.KeyInt;
import game.hero.Stat;
import game.hero.StatsEnum;
import game.hero.skill.ActiveSkillCore;
import game.hero.skill.PassiveSkill;
import game.hero.skill.SkillFactory;
import game.items.Potion;
import game.util.Amount;
import game.util.ResourcesContainer;


public class BasicContrahent implements Contrahent
{
    private Point hitPosition, missPosition;

    protected int hp, mp, extraHP, extraMP;
    protected int lvl;
    private volatile int time;
    
    protected String name;
    protected String color;
    
    protected Map<StatsEnum, Stat> stats = new HashMap<>();
    protected Stat[] statsArr;
    protected Map<String, PassiveSkill> passiveSkills = new HashMap<>();
    protected Map<String, ActiveSkillCore> activeSkills = new HashMap<>();
    protected Map<Potion, Amount> potions = new HashMap<>();
    protected Map<Resistable, Resistence> resistances = new HashMap<>();
    protected Map<StatusAligments, StatusAligment> statAligements = new HashMap<>();
    private int updateCount;
    

    private final BarInfo healthBarInfo = new BarInfo()
    {
        @Override
        public Color getBarColor()
        {
            Color res = Color.GREEN;

            double percent = 100.0 * getHP() / getMaxHP();

            if (percent < 90)
            {
                res = Color.YELLOW;
            }

            if (percent < 50)
            {
                res = Color.RED;
            }

            return res;
        }

        @Override
        public Image getIcon()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_HP_BAR);
        }

        @Override
        public int getMaxValue()
        {
            return getMaxHP();
        }

        @Override
        public int getValue()
        {
            return getHP();
        }
    };

    private final BarInfo manaBarInfo = new BarInfo()
    {
        @Override
        public Color getBarColor()
        {
            return Color.CYAN;
        }

        @Override
        public Image getIcon()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_MP_BAR);
        }

        @Override
        public int getMaxValue()
        {
            return getMaxMP();
        }

        @Override
        public int getValue()
        {
            return getMP();
        }
    };
    
    
    
    
    
    private final BarInfo timeBarInfo = new BarInfo()
    {
        
        @Override
        public Color getBarColor()
        {
            Color res = Color.MAGENTA;

            return res;
        }

        @Override
        public Image getIcon()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_CLOCK_ICON);
        }

        @Override
        public int getMaxValue()
        {
            return 1000;
        }

        @Override
        public int getValue()
        {
            return BasicContrahent.this.time;
        }
    };

    public BasicContrahent()
    {
    }

    public BasicContrahent(HeroClass heroClass)
    {
        
        statsArr = heroClass.getStartStats();
        for (Stat stat : statsArr)
        {
            this.stats.put(stat.getNameKey(), stat);
        }
        
        List<KeyInt> startSkills = heroClass.getStartSkills();
        
        for (KeyInt startSkill : startSkills)
        {
            String key = startSkill.key;

            if(key.startsWith("#"))
            {
                ActiveSkillCore skill = SkillFactory.getActiveSkill(key);
                skill.setLevel(startSkill.value);
                this.activeSkills.put(key, skill);
            }
            else
            {
                PassiveSkill skill = new PassiveSkill(key, startSkill.value);
                this.passiveSkills.put(key, skill);
            }
        }
        
        for (Potion p : Potion.values())
        {
            this.potions.put(p, new Amount(0));
        }
        
        for (KeyInt startingPotion : heroClass.getStartingPotions())
        {
            Amount amount = this.potions.get(Potion.valueOf(startingPotion.key));
            amount.setAmount(startingPotion.value);
        }
        
        
        
        
    }
    
    @Override
    public void addExtraHp(int extra)
    {
        if(extra < 0)
            return;
        
        this.extraHP += extra;
    }

    @Override
    public void addExtraMp(int extra)
    {
        if(extra < 0)
            return;
        
        this.extraMP += extra;
    }

    @Override
    public void addPotion(Potion potion)
    {
        Amount amount = this.potions.get(potion);
        amount.add(1);
        
        int max = potion.getMaxAmount() + this.getSkillLevel("BIG_POCKETS");
        
        if(amount.getAmount() > max)
            this.usePotion(potion);
        
    }

    @Override
    public void addSkill(String key)
    {
        if (key.startsWith("#")) // active skills
        {
            ActiveSkillCore skill = this.activeSkills.get(key);

            if (skill == null)
            {
                skill = SkillFactory.getActiveSkill(key);
                this.activeSkills.put(key, skill);
            }

            skill.levelSkill();
        } else // passive skill
        {
            PassiveSkill skill = this.passiveSkills.get(key);

            if (skill == null)
            {
                skill = new PassiveSkill(key);
                this.passiveSkills.put(key, skill);
            } else
            {
                skill.levelSkill();
            }
        }

    }

    public void aplyStatsAligment(Damage damage)
    {
        for (Map.Entry<StatusAligments, Double> entry : damage.statusAligmentsSet())
        {
            double buildUp = this.resist(entry.getKey(), entry.getValue());
            
            StatusAligment sa = this.statAligements.get(entry.getKey());
            
            if(sa == null)
            {
                sa = new StatusAligment(entry.getKey());
                this.statAligements.put(entry.getKey(), sa);
            }
            
            sa.buildUp(buildUp);
        }
    }

    @Override
    public double calculateBlockDamage(double damage)
    {
        damage = damage - this.getStat(StatsEnum.ARM).getValue();
        
        if(damage < 1)
            damage = 1;
        
        return damage;
    }

    @Override
    public double calculateCritDamage(double damage)
    {
        double res = damage * 1.5;
        
        int critDamageLevel = this.getSkillLevel("CRIT_DAMAGE");
        if(critDamageLevel > 0)
            res += res * critDamageLevel / 10; // +10% for CRIT_DAMAGE lvl
        
        
        return res;
    }

    @Override
    public double calculateMagicDamage()
    {
        int res = this.getStat(StatsEnum.MAG).getValue();
        
        // TODO better magic weapon

        return res;
    }

    @Override
    public double calculateMeleDamage()
    {
        int res = this.getStat(StatsEnum.STR).getValue();
        
        // TODO better weapon
        
        return res;
        
    }

    @Override
    public double calculateSuperCritDamage(double damage)
    {
        double res = damage * 2;
        
       int critDamageLevel = this.getSkillLevel("CRIT_DAMAGE");
        if(critDamageLevel > 0)
            res += res * critDamageLevel / 10; // +10% for CRIT_DAMAGE lvl
        
        return res;
    }

    @Override
    public boolean canGetCrit()
    {
        return true;
    }

    @Override
    public void consumeMana(int manaUsage)
    {
        if(manaUsage > this.mp)
        {
            this.hp -= manaUsage - this.mp; // exceed mana usage is substracted from HP
            this.mp = 0;
        }
        else
        {
            this.mp -= manaUsage;
        }
        
        int manaPool = this.getSkillLevel("MANA_POOL"); // TODO skill
        
        if(manaPool * 2 > Math.random() * 100)
            this.addExtraMp(1 + manaUsage / 10);
        
    }

    @Override
    public void damage(Damage damage)
    {
        
        double summDamage = 0;
        
        for (Map.Entry<ElementeEnum, Double> entry : damage.damageSet())
        {
            summDamage += this.resist(entry.getKey(), entry.getValue());
        }
        
        if(summDamage == 0)
            return;
        
        if(summDamage > 1)
        {
            summDamage -= (this.getStat( StatsEnum.ARM ).getValue() + this.getSkillLevel( "BETTER_ARMOR") );
            
            if(summDamage < 1)
                summDamage = 1;
        }
        
        if(damage.canBeBlocked())
        {
            if(this.tryBlock())
            {
                summDamage = this.calculateBlockDamage(summDamage);
            }
        }else 
        
        this.hp -= (int)summDamage;
        
        if(summDamage > 0.1)
        {
            BattleLog.log(this.getName() + " was damaged by " + (int)summDamage);
        }
        else if(summDamage < -0.1)
        {
            BattleLog.log(this.getName() + " was healed by " + (int)summDamage);
        }
        
        
    }

    private void damage(StatusAligments sa, int severity)
    {
        if(this.getStatAligementLvl(sa) > 0)
        {
            double damage = this.getStatAligementLvl(StatusAligments.POISON) * 
                    (1 + severity + this.getMaxHP() * severity / 100.0);
            
            damage = this.resist(sa, damage);
            
            
            if(damage > 0)
            {
                // TODO BattleLog.log
                this.hp -= damage;
            }
            
        }
    }

    @Override
    public void doneWithTurn()
    {
        this.time = 0;
    }

    @Override
    public List<ActiveSkillCore> getActiveSkills()
    {
        List<ActiveSkillCore> res = new ArrayList<>();
        
        res.addAll(this.activeSkills.values());
        
        return res;
    }

    @Override
    public double getCritModifier()
    {
        double result = 1;
        
        result /= (1 + this.getSkillLevel( "CRIT_GUARD") * 0.1);
        
        
        
        return result;
    }

    

    @Override
    public double getDodgeValue()
    {
        int res = this.getStat(StatsEnum.SPD).getValue();
        
        res += this.getSkillLevel("DODGE") * 2;
        
        boolean hidden = this.getStatAligementLvl(StatusAligments.HIDDEN) > 0;
        boolean stoped = this.getStatAligementLvl(StatusAligments.STOP) > 0;
        boolean slowed = this.getStatAligementLvl(StatusAligments.SLOW) > 0;
        
        if(hidden)
        {
            res *= 1 + this.getStatAligementLvl(StatusAligments.HIDDEN);
            
            if(stoped || slowed)
            {
                int d = res / 10;
                
                res -= d * (this.getStatAligementLvl(StatusAligments.STOP) +
                            this.getStatAligementLvl(StatusAligments.SLOW));
                
            }
        }
        else
        {
            if(slowed)
                res /= 1 + this.getStatAligementLvl(StatusAligments.SLOW);
            
            if(stoped)
                res = 0;
        }
        
        if(this.getStatAligementLvl(StatusAligments.MARKED) > 0)
        {
            res /= 1 + this.getStatAligementLvl(StatusAligments.MARKED);
        }
        
        if(res < 0)
            res = 0;
        
        return res;
    }

    @Override
    public int getExpForKilling()
    {
        return 0;
    }

    @Override
    public int getGoldForKilling()
    {
        return 0;
    }

    @Override
    public Point getHitPosition()
    {
        return this.hitPosition;
    }

    @Override
    public int getHP()
    {
        return this.hp;
    }

    @Override
    public BarInfo getHpBar()
    {
        return this.healthBarInfo;
    }

    @Override
    public int getMaxHP()
    {
        int res = this.getStat(StatsEnum.VIT).getValue() * 
                (10 + this.getSkillLevel("MEATSHIELD"));
        
        // TODO here more skills
        
        return res + this.extraHP;
    }

    @Override
    public int getMaxMP()
    {
        int res = this.getStat(StatsEnum.MAG).getValue() * 
                (10 + this.getSkillLevel("MANA_POOL"));
        
        // TODO more stuff
        
        return res + this.extraMP;
    }

    @Override
    public Point getMissPosition()
    {
        return this.missPosition;
    }

    

    @Override
    public int getMP()
    {
        return this.mp;
    }

    @Override
    public BarInfo getMpBar()
    {
        return this.manaBarInfo;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    
    @Override
    public String getNameColor()
    {
        return this.color;
    }

    @Override
    public List<String> getOwnedSkills()
    {
        List<String> res =new ArrayList<>();
        
        Set<String> keySet = this.activeSkills.keySet();
        res.addAll(keySet);
        
        keySet = this.passiveSkills.keySet();
        res.addAll(keySet);
        
        return res;
    }

    @Override
    public int getPotionAmount(Potion potion)
    {
        Amount amount = this.potions.get(potion);
        
        if(amount == null)
            return 0;
        
        return amount.getAmount();
    }

    @Override
    public int getRealManaUsage(int manaUsage)
    {
        // TODO 
        return manaUsage;
    }

    private Resistence getResistance( Resistable key )
    {
        
        Resistence res = this.resistances.get( key );
        
        if(res == null)
        {
            res = new Resistence();
            this.resistances.put( key, res );
        }
        
        return res;
        
    }

    @Override
    public int getSkillLevel(String key)
    {
        if(key.startsWith("#")) // active skills
        {
            ActiveSkillCore skill = this.activeSkills.get(key);
            
            if(skill == null)
            {
                return 0;
            }
            
            return skill.getLevel();
        }
        else // passive skill
        {
            PassiveSkill skill = this.passiveSkills.get(key);
            
            if(skill == null)
            {
                return 0;
            }
            
            return skill.getLevel();
        }
    }

    @Override
    public List<ActiveSkillCore> getSkills(ActiveSkillCore.SkillType skillType)
    {
        List<ActiveSkillCore> res = new ArrayList<>();
        
        for (ActiveSkillCore value : this.activeSkills.values())
        {
            if(value.getType() == skillType)
                res.add(value);
        }
        
        return res;
    }

    @Override
    public Stat getStat(StatsEnum key)
    {
        return this.stats.get(key);
    }

    @Override
    public int getStatAligementLvl(StatusAligments key)
    {
        StatusAligment sa = this.statAligements.get(key);
        
        if(sa == null)
            return 0;
        
        return sa.getLevel();
    }

    @Override
    public List<StatusAligment> getStatAligements()
    {
        return new ArrayList<StatusAligment>(this.statAligements.values());
    }

    
    public Stat[] getStats()
    {
        return this.statsArr;
    }
    
    @Override
    public BarInfo getTimeBar()
    {
        return this.timeBarInfo;
    }
    
    
    @Override
    public void heal(int heal)
    {
        this.hp += heal;
        
        if(this.hp > this.getMaxHP())
            this.hp = this.getMaxHP();
    }

    @Override
    public boolean isAlive()
    {
        return this.hp > 0;
    }
    
    @Override
    public boolean isReady()
    {
        return this.time >= 1000;
    }
    
    @Override
    @Deprecated
    public void logCondition()
    {
        // TODO decent battle log
        
        BattleLog.log(this.getName() + " Condition: " + this.hp + "/" + this.getMaxHP());
    }

    @Override
    public double recalculateCritChance(double critChance)
    {
        //TODO skill "CRIT_GUARD", stat "
        
        return critChance; // (1 + this.getSkillLevel("CRIT_GUARD"));
    }

    @Override
    public void removeRandomDebuff()
    {
        // TODO
    }

    protected double resist(Resistable element, double amount)
    {
        
        Resistence res = this.getResistance( element );
        
        return res.resist(amount);
    }

    @Override
    public void setHitPosition(Point p)
    {
        this.hitPosition = p;
    }

    @Override
    public void setMissPosition(Point p)
    {
        this.missPosition = p;
    }

    @Override
    @Deprecated
    public void trapDamage(int damage, ActiveSkillCore.AttackProperty... attackProperty)
    {
        damage = damage - this.getStat(StatsEnum.ARM).getValue(); // TODO better armor skill
        if(damage < 1)
            damage = 1;
        
        // TODO resists comes here
        
        this.hp -= damage;
        BattleLog.log(this.name + " was damaged by " + damage);
    }

    @Override
    public ActiveSkillCore.SkillSuccess tryAttack(
            ActiveSkillCore.SkillType skillType, Contrahent attacker, double critChanceModifer, ActiveSkillCore.AttackProperty... attackPropertys)
    {
        ActiveSkillCore.SkillSuccess res;
        
        List<ActiveSkillCore.AttackProperty> ap = new ArrayList<>(Arrays.asList(attackPropertys));
        
        int mySpeed = this.getStat(StatsEnum.SPD).getValue();
        int attakerSpeed = attacker.getStat(StatsEnum.SPD).getValue();
        
        if(mySpeed >= attakerSpeed)
            mySpeed *= 2;
        else
            attakerSpeed *= 2;
        
        if(ap.contains(ActiveSkillCore.AttackProperty.AOE))
            mySpeed /= 2;
        
        if(ap.contains(ActiveSkillCore.AttackProperty.HOMING))
            mySpeed /= 2;
        
        if(attakerSpeed >= Math.random() * (mySpeed + attakerSpeed))
            res = ActiveSkillCore.SkillSuccess.HIT;
        else
            return ActiveSkillCore.SkillSuccess.MISS;
        
        
        int block = this.getSkillLevel("BLOCK") * 5;
        
        if(block >= Math.random() * 100)
            return ActiveSkillCore.SkillSuccess.BLOCK;
        
        int critChance = 1; // TODO build in a crit skill
        
        
        if((Math.random() * 100) <= critChance * critChanceModifer ) // CRIT!
        {
            res = ActiveSkillCore.SkillSuccess.CRIT;
            critChance += 0; // TODO build in a supercrit addition skill
            
            if((Math.random() * 100) <= critChance  * critChanceModifer ) // SUPERCRIT!!!
            {
                res = ActiveSkillCore.SkillSuccess.SUPERCRIT;
            }
            
        }
        
        return res;
    }

    @Override
    public AttackResult tryAttack(Attack attack)
    {
        
        
        if(attack.canMiss())
        {
            double toHit = attack.getToHit();
            double dodge = this.getDodgeValue();

            if(toHit < (Math.random() * (toHit + dodge)))   // MISS
            {
                return attack.miss();
            }
        }
        
        if(attack.canCrit() && this.canGetCrit())
        {
            
            double critChance = attack.getCritChance() * this.getCritModifier();
            
            if(critChance > Math.random() * 100 || attack.allwaysCrit())
            {
                if(critChance > Math.random() * 100)
                    return attack.supercrit();
                else
                    return attack.crit();
            }
            
        }
        
        return attack.hit();
    }

    private boolean tryBlock()
    {
        int block = this.getSkillLevel("BLOCK") * 5;
        
        return block >= Math.random() * 100;
    }

    @Override
    public boolean tryCast(int complexity, int mana)
    {
        if(this.mp < mana)
            complexity *= 2;
        
        complexity /= 1 + this.getStatAligementLvl(StatusAligments.FRESH);
        
        // TODO here all dbuffs and such
        
        int magic = this.getStat(StatsEnum.MAG).getValue() + this.getSkillLevel("MAGIC_SKILL");
        
        if(magic > complexity * 2)
            return true;
        
        if(magic > complexity)
            magic *= 1 + ((double)magic / complexity);
        
        
        return magic > Math.random() * (magic + complexity);
        
    }

    @Override
    public ActiveSkillCore.SkillSuccess tryCastBuff(int complexity, int manaUsage)
    {
        if(this.mp < manaUsage)
            complexity *= 2;
        
        // TODO here all dbuffs and such
        
        int magic = this.getStat(StatsEnum.MAG).getValue();
        
        if(magic > complexity)
            magic *= 1 + ((double)magic / complexity);
        
        if(magic > Math.random() * (magic + complexity))
            return ActiveSkillCore.SkillSuccess.HIT;
        
        return ActiveSkillCore.SkillSuccess.FAIL;
    }

    @Override
    public boolean tryMele(int complexity)
    {
        complexity /= 1 + this.getStatAligementLvl(StatusAligments.FRESH);
        
        int mele = this.getStat(StatsEnum.SPD).getValue() + this.getSkillLevel("WEAPON_SKILL");
        
        
        if(mele > complexity * 2)
            return true;
        
        return mele > Math.random() * (mele + complexity);
    }

    @Override
    public ActiveSkillCore.SkillSuccess tryPerformMagic(int complexity, int mana)
    {
        if(this.mp < mana)
            complexity *= 2;
        
        // TODO here all dbuffs and such
        
        int magic = this.getStat(StatsEnum.MAG).getValue();
        
        if(magic > complexity)
            magic *= 1 + ((double)magic / complexity);
        
        if(magic > Math.random() * (magic + complexity))
            return ActiveSkillCore.SkillSuccess.HIT;
        
        return ActiveSkillCore.SkillSuccess.FAIL;
        
    }

    @Override
    public void turnPassed()
    {
        for (StatsEnum value : StatsEnum.values())
        {
            this.getStat(value).timePassed();
        }
        
        int manaFlow = this.getSkillLevel("MANA_FLOW");
        if(manaFlow > 0)
        {
            this.mp += manaFlow;
            if(this.mp > this.getMaxMP())
            {
                this.mp = this.getMaxMP();
            }
        }
        
        manaFlow = this.getStatAligementLvl(StatusAligments.MP_REGEN);
        if(manaFlow > 0)
        {
            double regMP = 1 + this.getMaxHP() / 100.0 * manaFlow;
            
            this.mp += regMP;
            if(this.mp > this.getMaxMP())
            {
                this.mp = this.getMaxMP();
            }
        }
        
        int regen = this.getSkillLevel("REGENERATION");
        if(regen > 0)
        {
            this.hp += regen;
            if(this.hp > this.getMaxHP())
            {
                this.hp = this.getMaxHP();
            }
        }
        
        regen = this.getStatAligementLvl(StatusAligments.REGEN);
        if(regen > 0)
        {
            double regHP = 1 + this.getMaxHP() / 100.0 * regen;
            
            this.hp += regHP;
            if(this.hp > this.getMaxHP())
            {
                this.hp = this.getMaxHP();
            }
        }
        
        this.damage(StatusAligments.POISON, 1);
        this.damage(StatusAligments.BLEED, 1);
        this.damage(StatusAligments.BURN, 3);
        
        
        for (Map.Entry<StatusAligments, StatusAligment> entry : this.statAligements.entrySet())
        {
            StatusAligments key = entry.getKey();
            StatusAligment value = entry.getValue();
            
            
            if(!key.shouldUpdateInUpdate())
            {
                value.reduce( this.getResistance(key));
            }
        }
        
        // TODO bad status timePassed()
        
    }

    @Override
    public void update()
    {
        this.updateCount ++;
        int dTime = this.getStat(StatsEnum.SPD).getValue() + this.getSkillLevel("QUICK");
        
        
        // this happents about as often, as this one does attack.
        int mTime = 1000 / dTime;
        if(this.updateCount % mTime == 0)
        {
            for( Map.Entry<StatusAligments, StatusAligment> entry : this.statAligements.entrySet() )
            {
                StatusAligments key = entry.getKey();
                StatusAligment value = entry.getValue();

                if(key.shouldUpdateInUpdate())
                    value.reduce( this.getResistance(key));
            }
            this.updateCount = 0;
        }
        
        if(this.getStatAligementLvl(StatusAligments.STOP) == 0)
        {
            this.time += dTime /(1 + this.getStatAligementLvl(StatusAligments.SLOW));
        }
    }

    protected void useBoostPotion()
    {
        int boost = 5 + this.getSkillLevel("POTION_EFFECTIVENESS");
        
        for (StatsEnum value : StatsEnum.values())
        {
            this.getStat(value).boost(boost);
        }
    }

    protected void useHpPotion()
    {
        Amount amount = this.potions.get(Potion.HP_POTION);
        
        if (amount.getAmount() <= 0)
        {
            return;
        }
        
        amount.substract(1);
        
        this.hp +=this.getMaxHP() * (0.2 + 0.05 * this.getSkillLevel("POTION_EFFECTIVENESS"));    // TODO skill adjustments
        

        if (Math.random() * 100 <= 2 * this.getSkillLevel("MEATSHIELD"))
        {
            this.extraHP++; 
        }
        if (this.hp > this.getMaxHP())
        {
            this.extraHP += 1 + this.getMaxHP() / (100 - 2 * this.getSkillLevel("MEATSHIELD")); // TODO grow need ajusting
            this.hp = this.getMaxHP();
        }
    }

    protected void useMpPotion()
    {
        Amount amount = this.potions.get(Potion.MP_POTION);
        
        if (amount.getAmount() <= 0)
        {
            return;
        }
        
        amount.substract(1);
        
        this.mp +=this.getMaxMP() * (0.2 + 
                0.05 * this.getSkillLevel("POTION_EFFECTIVENESS") + 
                0.01 * this.getSkillLevel("MANA_FLOW"));    // TODO skill adjustments
        

        if (Math.random() * 100 <= 2 * this.getSkillLevel("MANA_POOL"))
        {
            this.extraMP++; 
        }
        if (this.mp > this.getMaxMP())
        {
            this.extraMP += 1 + this.getMaxMP() / (100 - 2 * this.getSkillLevel("MANA_POOL")); // TODO grow need ajusting
            this.mp = this.getMaxMP();
        }
    }

    @Override
    public void usePotion(Potion potion)
    {
        if(this.getPotionAmount(potion) <= 0)
            return;
        
        switch(potion)
        {
            case HP_POTION:
                this.useHpPotion();
                break;
            case MP_POTION:
                this.useMpPotion();
                break;
            case SPEED_POTION:
                this.useSpeedPotion();
                break;
            case BOOST_POTION:
                this.useBoostPotion();
                break;
            default:
                throw new AssertionError(potion.name());
            
        }
    }

    protected void useSpeedPotion()
    {
        Stat speed = this.getStat(StatsEnum.SPD);
        
        int boost = 10 + this.getSkillLevel("POTION_EFFECTIVENESS");
        
        speed.boost(boost);
    }

    @Override
    public double calkulateToHit()
    {
        double res = this.getStat(StatsEnum.SPD).getValue();
        
        res += this.getSkillLevel("PRECISION") * 2;
        
        res += this.getStatAligementLvl(StatusAligments.EYE_OF_MIND) * 5;
        
        res /= 1 + this.getStatAligementLvl(StatusAligments.BLIND);
        
        return res;
    }
    
    
    
    
}



