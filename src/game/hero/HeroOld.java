/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.hero.skill.Skill;
import game.hero.skill.PassiveSkillOld;
import game.hero.skill.BasicSkill;
import game.Game;
import game.GameConstants;
import game.util.ResourcesContainer;
import game.GameState;
import game.dungeon.Direction;
import game.hero.skill.BasicActiveSkill;
import game.hero.skill.BasicSkillShopItem;
import game.hero.skill.SkillCore;
import game.hero.skill.SkillFactory;
import game.items.ArtefactItem;
import game.items.GoldItem;
import game.items.Item;
import game.items.JewelryItem;
import game.items.MunitionItem;
import game.items.Potion;
import game.panels.shop.ShopItem;
import game.util.container.RandomContainer;
import game.util.container.RandomKey;
import game.util.container.RandomizerWrapper;
import game.util.logger.MyLogger;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import game.hero.skill.ActiveSkillCore;
import game.util.drawable.Animation;

/**
 *
 * @autor gbeljajew
 */
public class HeroOld implements StatOwner
{

    private final HeroFigure figure;
    protected final HeroClass heroClass;
    protected final ExtraPoints extraPoints = new ExtraPoints();

    protected final String name;
    protected long score = 0;
    protected long gold = 999990;

    protected int lvl = 1;
    protected int exp = 0;

    protected Map<StatsEnum, Stat> stats = new HashMap<>();
    protected Map<PassiveSkillOld, Skill> passiveSkills = new HashMap<>();
    protected Map<String, BasicActiveSkill> activeSkills = new HashMap<>();
    protected Map<String, BasicActiveSkill> meleSkills = new HashMap<>();
    protected Map<String, BasicActiveSkill> magicSkills = new HashMap<>();
    protected Map<String, BasicActiveSkill> supportSkills = new HashMap<>();
    protected Stat[] statsArr; // the same as stats, just sorted array

    protected int hpMax = 50;
    protected int hpBonus = 0;
    protected int hp = hpMax;
    protected int mpMax = 50;
    protected int mpBonus = 0;
    protected int mp = mpMax;

    protected int munition = 10;
    protected int hpPotions = 3;
    protected int mpPotions = 1;
    protected int speedPotion = 0;
    protected int boostPotion = 0;

    // ----- HP Bar -------------------------------------------------------
    private final BarInfo healthBarInfo = new BarInfo()
    {
        @Override
        public int getValue()
        {
            return HeroOld.this.hp;
        }

        @Override
        public int getMaxValue()
        {
            return HeroOld.this.getMaxHP();
        }

        @Override
        public Image getIcon()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_HP_BAR);
        }

        @Override
        public Color getBarColor()
        {
            Color res = Color.GREEN;

            double percent = 100.0 * hp / hpMax;

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
    };

    // ----- MP Bar -----------------------------------------------
    private final BarInfo manaBarInfo = new BarInfo()
    {
        @Override
        public int getValue()
        {
            return HeroOld.this.mp;
        }

        @Override
        public int getMaxValue()
        {
            return HeroOld.this.getMaxMP();
        }

        @Override
        public Image getIcon()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_MP_BAR);
        }

        @Override
        public Color getBarColor()
        {
            return Color.CYAN;
        }
    };
    
    // ----- EXP Bar ---------------------------------------------------
    private final BarInfo expBarInfo = new BarInfo()
    {
        @Override
        public int getValue()
        {
            return HeroOld.this.exp;
        }

        @Override
        public int getMaxValue()
        {
            return HeroOld.this.getExpForNextLevel();
        }

        @Override
        public Image getIcon()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_EXP_BAR);
        }

        @Override
        public Color getBarColor()
        {
            return Color.YELLOW;
        }
    };
    
    public BarInfo getExpBarInfo()
    {
        return this.expBarInfo;
    }
    
    // ----- HP Potion --------------------------------------------------
    private final PotionPouch hpPotionPouch = new PotionPouch()
    {
        @Override
        public int getCount()
        {
            return HeroOld.this.hpPotions;
        }

        @Override
        public void drink()
        {
            if (HeroOld.this.hpPotions <= 0)
            {
                return;
            }

            HeroOld.this.hp += HeroOld.this.hpMax / 3;    // TODO skill adjustments
            HeroOld.this.hpPotions--;

            if (Math.random() > 0.5)
            {
                HeroOld.this.hpBonus++;                // TODO not sure. adjust/change.
            }
            if (HeroOld.this.hp > HeroOld.this.getMaxHP())
            {
                HeroOld.this.hpBonus += 1 + HeroOld.this.getMaxHP() / 100; // TODO grow need ajusting
                HeroOld.this.hp = HeroOld.this.getMaxHP();
            }
        }

        @Override
        public Image getImage()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_HP_POTION);
        }
    };

    public PotionPouch getHPotionPouch()
    {
        return this.hpPotionPouch;
    }

    private final PotionPouch manaPotionPouch = new PotionPouch()
    {
        @Override
        public int getCount()
        {
            return HeroOld.this.mpPotions;
        }

        @Override
        public void drink()
        {
            if (HeroOld.this.mpPotions <= 0)
            {
                return;
            }

            HeroOld.this.mpPotions--;

            HeroOld.this.mp += HeroOld.this.getMaxMP() / 3;    // TODO skill adjustments

            if (Math.random() > 0.5)
            {
                HeroOld.this.mpBonus++;                    // TODO not sure. adjust/change.
            }
            if (HeroOld.this.mp > HeroOld.this.getMaxMP())
            {
                HeroOld.this.mpMax += 1 + HeroOld.this.getMaxMP() / 100;
                HeroOld.this.mp = HeroOld.this.getMaxMP();
            }

        }

        @Override
        public Image getImage()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_MP_POTION);
        }
    };

    public PotionPouch getManaPotionPouch()
    {
        return this.manaPotionPouch;
    }

    private PotionPouch sppedPotionPouch = new PotionPouch()
    {
        @Override
        public int getCount()
        {
            return HeroOld.this.speedPotion;
        }

        @Override
        public void drink()
        {
            if(HeroOld.this.speedPotion <= 0)
                return;
            
            HeroOld.this.speedPotion --;
            
            int time = 100 + HeroOld.this.getSkillLevel(PassiveSkillOld.MARATHON_RUNNER) * 10; // TODO:BALANCING
            
            HeroOld.this.figure.addSpeed(time);
        }

        @Override
        public Image getImage()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_GREEN_POTION);
        }
    };

    public PotionPouch getSpeedPotionPouch()
    {
        return this.sppedPotionPouch;
    }
    private final PotionPouch boostPotionPouch = new PotionPouch()
    {
        @Override
        public int getCount()
        {
            return HeroOld.this.boostPotion;
        }

        @Override
        public void drink()
        {
            // TODO create boost buff
        }

        @Override
        public Image getImage()
        {
            return ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_GOLD_POTION);
        }
    };

    public PotionPouch getBoostPotionPouch()
    {
        return this.boostPotionPouch;
    }
    
    private final PotionPouch munitionsPouch = new PotionPouch()
    {
        @Override
        public int getCount()
        {
            return HeroOld.this.munition;
        }

        @Override
        public void drink()
        {
            // will we use it outside battle?
        }

        @Override
        public Image getImage()
        {
            
            int skillLevel = HeroOld.this.getSkillLevel(PassiveSkillOld.MUNITION_QUALITY);
            
            Image[] throwingWeapons = ResourcesContainer.getMiskTileSet(GameConstants.TILESET_THROW_WEAPONS);
            
            int index = skillLevel / 5;
            
            if(index >= throwingWeapons.length)
                index = throwingWeapons.length - 1;
            
            
            return throwingWeapons[index];
        }
    };
    
    public PotionPouch getMunitionPouch()
    {
        return this.munitionsPouch;
    }

    public HeroOld(HeroClass heroClass, String name)
    {
        this.heroClass = heroClass;
        this.name = name;
        figure = new HeroFigure(heroClass);

        this.statsArr = heroClass.getStartStats();

        for (Stat stat : statsArr)
        {
            this.stats.put(stat.getNameKey(), stat);
        }

        
        
        
        // TODO:DEBUG
        
        this.passiveSkills.put(PassiveSkillOld.MUNITION_QUALITY,
                new BasicSkill(PassiveSkillOld.MUNITION_QUALITY, 7));
        
    }

    protected int getMaxHP()
    {
        return this.stats.get(StatsEnum.VIT).getValue() * 10 + this.hpBonus;
    }

    protected int getMaxMP()
    {
        return this.stats.get(StatsEnum.MAG).getValue() * 10 + this.mpBonus;
    }

    public String getName()
    {
        return name;
    }

    public HeroFigure getFigure()
    {
        return figure;
    }

    public void stop()
    {
        figure.stop();
    }

    public void going(Direction dir)
    {
        figure.going(dir);
    }

    public void update()
    {
        if (Game.getGameState() == GameState.MAP)
        {
            this.figure.update();
        }
    }

    public boolean isHeroAlive()
    {
        return this.hp > 0;
    }

    public BarInfo getHealthBarInfo()
    {
        return this.healthBarInfo;
    }

    public BarInfo getManaBarInfo()
    {
        return this.manaBarInfo;
    }

    public long getScore()
    {

        return this.score;
    }

    public void damage(int damage)
    {
        
        // TODO:DEBUG
        System.out.println("Damage: " + damage);
        
        
        int realDamage = damage - this.stats.get(StatsEnum.ARM).getValue();
        
        if(realDamage < 1)      // 1 is min.
            realDamage = 1;
        
        
        
        
        
        System.out.println("Was damaged by: " + realDamage);
        
        this.hp -= realDamage; // TODO chance to avoid and armor reduce
    }
    
    public boolean buy(ShopItem item, int price)
    {
        
        if(this.gold < price)
            return false;
        
        this.gold -= price;
        
        if(item instanceof Item)
            this.foundItem((Item)item);
        
        if(item instanceof PassiveSkillOld)
            this.foundSkill((PassiveSkillOld)item);
        
        if(item instanceof BasicSkillShopItem)
            this.foundSkill(item);
       
        
        return true;
    }
    

    
    
    public void foundSkill(ShopItem item)
    {
        String key = item.getKey();
        
        Skill skill = this.activeSkills.get(key);
        
        if(skill == null)
        {
            SkillCore skillCore = SkillFactory.getActiveSkill(key);
            
            if(skillCore == null)
                return;
            
            if(!(skillCore instanceof ActiveSkillCore))
            {
                MyLogger.error("expecting ActiveSkill found " + skillCore.getClass() + " key: " + key);
            }
            
            ActiveSkillCore activeSkill = (ActiveSkillCore)skillCore;
            
            BasicActiveSkill basicSkill = new BasicActiveSkill(activeSkill);
            
            this.activeSkills.put(key, (BasicActiveSkill)skill);
            
            switch(activeSkill.getType())
            {
                case MELE:
                    this.meleSkills.put(key, basicSkill);
                    break;
                case MAGIC:
                    this.magicSkills.put(key, basicSkill);
                    break;
                case SUPPORT:
                    this.supportSkills.put(key, basicSkill);
                    break;
                default:
                    throw new AssertionError(activeSkill.getType().name());
                
            }
        }
        else
        {
            skill.levelSkill();
        }
        
    }
    
    public void foundSkill(PassiveSkillOld skillType)
    {
        Skill skill = this.passiveSkills.get(skillType);
        
        if(skill == null)
        {
            skill = new BasicSkill(skillType);
            this.passiveSkills.put(skillType, skill);
        }
        else
        {
            skill.levelSkill();
        }
        
        
    }
    
    public void foundItem(Item item)
    {

        //TODO:DEBUG
        System.out.println("Found Item: " + item.getClass().getSimpleName());

        if (item instanceof Potion)
        {
            this.stashPotion((Potion) item);
        } else if (item instanceof JewelryItem)
        {
            this.stashJewelry((JewelryItem) item);
        } else if (item instanceof GoldItem)
        {
            this.stashGold((GoldItem) item);
        } else if (item instanceof ArtefactItem)
        {
            this.stashAtrefact((ArtefactItem) item);
        } else if (item instanceof MunitionItem)
        {
            this.stashMunition((MunitionItem) item);
        }

    }

    protected void stashAtrefact(ArtefactItem item)
    {
        // TODO
    }

    protected void stashMunition(MunitionItem item)
    {
        munition += item.getCount();
    }

    protected void stashGold(GoldItem item)
    {
        this.gold += item.getSumm();
        this.score += item.getSumm();
    }

    protected void stashJewelry(JewelryItem item)
    {
        this.gold += item.getSumm();
        this.score += item.getSumm();
    }

    protected void stashPotion(Potion potion)
    {
        int max;
        
        if (potion == Potion.HP_POTION)
        {
            System.out.println("HP Potion");
            max = 5 + this.getSkillLevel(PassiveSkillOld.BIG_POCKETS);

            this.hpPotions++;
            if (this.hpPotions > max)
            {
                this.hpPotionPouch.drink();
                System.out.println("HP Potion drunk");
            }
 
           
        } else if (potion == Potion.MP_POTION)
        {
            System.out.println("MP Potion");
            
            max = 3 + this.getSkillLevel(PassiveSkillOld.BIG_POCKETS);

            this.mpPotions++;

            if (this.mpPotions > max)
            {
                this.manaPotionPouch.drink();
                System.out.println("MP Potion drunk");
            }
            
           
        } else if (potion == Potion.SPEED_POTION)
        {
            System.out.println("Speed Potion");
            
            max = 2 + this.getSkillLevel(PassiveSkillOld.BIG_POCKETS);
            
            this.speedPotion++;
            
            if(this.speedPotion > max)
            {
                this.sppedPotionPouch.drink();
                System.out.println("Speed Potion drunk");
            }
            
        } else if (potion == Potion.BOOST_POTION)
        {
            System.out.println("Boost Potion");
            
            max = 1 + this.getSkillLevel(PassiveSkillOld.BIG_POCKETS);
            
            this.boostPotion++;
            
            if(this.boostPotion > max)
            {
                this.boostPotionPouch.drink();
                System.out.println("Boost Potion drunk");
            }
            
        }
    }

    public Animation levelUp(int xp)
    {
        this.exp += xp;

        if (this.exp >= this.getExpForNextLevel())
        {
            this.levelUp();

        }

        // TODO make animation for FULL_RANDOM levelup. other don't need it
        return null;
    }

    private void levelUp()
    {
        while (this.exp >= this.getExpForNextLevel())
        {
            this.exp -= this.getExpForNextLevel();
            this.lvl++;

            this.levelUpStatpoints();

        }

        if (this.extraPoints.getPoints() > 0) // for the case you get some extra points from somewhere
        {
            JOptionPane.showMessageDialog(null, new LevelUpDialogPanel(this.statsArr, extraPoints),
                    "LevelUP",
                    JOptionPane.PLAIN_MESSAGE);
        }

        this.extraPoints.confirmUsage();

        for (Stat stat : statsArr)
        {
            stat.useIncrement();
        }

        this.hp = this.getMaxHP();
        this.mp = this.getMaxMP();
        
        
        // TODO:DEBUG
        for (Stat stat : statsArr)
        {
            System.out.println(stat.getNameKey() + " " + stat.getRealValue());
        }
        
        
        
    }

    private int getExpForNextLevel()
    {
        return this.lvl * 10;      // TODO 
    }

    private void levelUpStatpoints()
    {
        RandomContainer<RandomizerWrapper<StatsEnum>> statRandomiser = this.heroClass.getStatsDistributor();

        int points = 4; // points distributed randomly
        int bonus = 1;  // points distributed manualy

        // TODO number of statpoints different based on skills and classes
        // TODO extra points from skills add here.
        switch (Game.getOption(GameConstants.OPTION_KEY_LEVELUP))
        {
            case GameConstants.OPTION_VALUE_LEVELUP_FULL_AUTO:    // all random
                points += bonus;
                bonus = 0;
                break;
            case GameConstants.OPTION_VALUE_LEVELUP_MANUAL:         // all manual
                bonus += points;
                points = 0;
                break;
            default:
        }

        for (int i = 0; i < points; i++)
        {
            StatsEnum statKey = statRandomiser.get().getItem();

            Stat statValue = this.stats.get(statKey);

            if (statValue != null)
            {
                statValue.pp();
            }
        }

        this.extraPoints.add(bonus); // TODO it is possible trough Skills, that there will be more o less than one extra point

    }

    public HeroClass getHeroClass()
    {
        return this.heroClass;
    }

    @Override
    public Stat getStat(StatsEnum key)
    {
        return this.stats.get(key);
    }

    public long getGoldAmount()
    {
        return this.gold;
    }

    public int getLvl()
    {
        return lvl;
    }

    public int getSkillLevel(PassiveSkillOld type)
    {
        Skill skill = this.passiveSkills.get(type);
        
        if(skill == null)
            return 0;
        
        return skill.getLevel();
        
    }
    
    public int getSkillLevel(String key)
    {
        Skill skill = null;

        try
        {
            if (key.startsWith("#"))
            {
                skill = this.activeSkills.get(key);
            } else
            {
                skill = this.passiveSkills.get(PassiveSkillOld.valueOf(key));
            }
        } catch (RuntimeException ex)
        {
            MyLogger.error("Wrong key found: " + key, ex);
        }

        if (skill == null)
        {
            return 0;
        }

        return skill.getLevel();
        
    }

    /**
     * 
     * @return all skills, activa and passive for Skill Shop.
     */
    public List<RandomKey> getAllKnownSkills()
    {
        
        List<RandomKey> res = new ArrayList<>();
        
        res.addAll(this.passiveSkills.keySet());
        
        for (String key : this.activeSkills.keySet())
        {
            res.add(SkillFactory.getChanceToGetSkill(key));
        }
        
        return res;
    }
    
    public List<RandomKey> getPassiveSkills()
    {
        return new ArrayList<>(this.passiveSkills.keySet());
    }
    
    public List<RandomKey> getMeleSkills()
    {
        List<RandomKey> res = new ArrayList<>();
        for (String key : this.meleSkills.keySet())
        {
            res.add(SkillFactory.getChanceToGetSkill(key));
        }
        return res;
    }
    
    public List<RandomKey> getMagicSkills()
    {
        List<RandomKey> res = new ArrayList<>();
        for (String key : this.magicSkills.keySet())
        {
            res.add(SkillFactory.getChanceToGetSkill(key));
        }
        return res;
    }
    
    public List<RandomKey> getSupportSkills()
    {
        List<RandomKey> res = new ArrayList<>();
        for (String key : this.supportSkills.keySet())
        {
            res.add(SkillFactory.getChanceToGetSkill(key));
        }
        return res;
    }
}
