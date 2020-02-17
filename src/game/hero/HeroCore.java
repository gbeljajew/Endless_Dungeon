/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.Game;
import game.GameConstants;
import game.util.ResourcesContainer;
import game.battle.enemy.BasicContrahent;
import game.hero.skill.ActiveSkillCore;
import game.items.MunitionItem;
import game.util.Animations;
import game.util.container.RandomContainer;
import game.util.container.RandomizerWrapper;
import game.util.drawable.Animation;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @autor gbeljajew
 */
public class HeroCore extends BasicContrahent
{

    private int munition;
    private int exp;

    private final double statPerLvl;
    private final double bonusPoints;
    private final int expPerLvl;
    protected final ExtraPoints extraPoints = new ExtraPoints();
    private final HeroClass heroClass;
    private double statPoints = 0;
    private double statBonus = 0;
    
    
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public HeroCore(HeroClass heroClass)
    {
        super(heroClass);
        this.heroClass = heroClass;
        
        this.statPerLvl = heroClass.getStatPerLevel();
        this.bonusPoints = heroClass.getBonusPerLevel();
        this.expPerLvl = heroClass.getExpPerLvl();
        
        this.hp = this.getMaxHP();
        this.mp = this.getMaxMP();
        this.munition = heroClass.getMunition();
        this.lvl = 1;
        
    }

    boolean tryEvadeTrap(int dificulty, ActiveSkillCore.AttackProperty... attackPropertys)
    {
        List<ActiveSkillCore.AttackProperty> ap = new ArrayList(Arrays.asList(attackPropertys));
        
        int chance = this.getStat(StatsEnum.SPD).getValue() + 5 * this.getSkillLevel("DODGE");
        int rnd = chance + 50 + dificulty;
        
        if(ap.contains(ActiveSkillCore.AttackProperty.AOE))
            chance /= 2;
        
        if(ap.contains(ActiveSkillCore.AttackProperty.HOMING))
            chance /= 4;
        
        return chance >= Math.random() * rnd;
    }

    boolean tryBlockTrap(int dificulty, ActiveSkillCore.AttackProperty[] attackPropertys)
    {
        List<ActiveSkillCore.AttackProperty> ap = new ArrayList(Arrays.asList(attackPropertys));
        
        int chance = this.getStat(StatsEnum.SPD).getValue() + 5 * this.getSkillLevel("BLOCK");
        int rnd = chance + 50 + dificulty;
        
        if(ap.contains(ActiveSkillCore.AttackProperty.ARMOR_PIERCING))
            chance /= 4;
        
        return chance >= Math.random() * rnd;
    }

    int getMunitionAmount()
    {
        return this.munition;
    }

    BarInfo getExpBar()
    {
        return this.expBarInfo;
    }

    int getLevel()
    {
        return this.lvl;
    }

    public Animation addExp(int xp)
    {
        this.exp += xp;
        
        boolean hasLeveld = false;
        
        while(this.exp >= this.getExpForNextLevel())
        {
            hasLeveld = true;
            
            this.exp -= this.getExpForNextLevel();
            this.lvl++;
            
            this.collectPointsForLevelup();
        }
        
        if (this.extraPoints.getPoints() > 0) // for the case you get some extra points from somewhere
        {
            JOptionPane.showMessageDialog(null, new LevelUpDialogPanel(this.getStats(), extraPoints),
                    "LevelUP",
                    JOptionPane.PLAIN_MESSAGE);
            for (Stat stat : statsArr)
            {
                stat.useIncrement();
            }

            this.extraPoints.confirmUsage();

            this.hp = this.getMaxHP();
            this.mp = this.getMaxMP();
        }
        
        
        
        if(hasLeveld && 
                Game.getOption(GameConstants.OPTION_KEY_LEVELUP).equals(
                        GameConstants.OPTION_VALUE_LEVELUP_FULL_AUTO))
        {
            return Animations.getAnimation("LEVEL_UP"); // for full auto we need at least show player, that he leveled up
        }
        
        // TODO 
        return null;
    }

    protected void stashMunition(MunitionItem munitionItem)
    {
        this.stashMunition(munitionItem.getCount() + this.getSkillLevel("MUNITION_FINDER"));
    }
    
    protected void stashMunition(int amount)
    {
        int max = 10 + 2 * this.getSkillLevel("BIG_POCKETS");
        
        // TODO add here AMMO_POUCH and ARROW_POUCH.
        
        this.munition += amount;
        if(this.munition > max)
            this.munition = max;
        
    }
    
    private final BarInfo expBarInfo = new BarInfo()
    {
        @Override
        public int getValue()
        {
            return HeroCore.this.exp;
        }

        @Override
        public int getMaxValue()
        {
            return HeroCore.this.getExpForNextLevel();
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

    private int getExpForNextLevel()
    {
        return this.lvl * this.expPerLvl;
    }

    private void collectPointsForLevelup()
    {
        RandomContainer<RandomizerWrapper<StatsEnum>> statRandomiser = this.heroClass.getStatsDistributor();

        this.statPoints += this.statPerLvl;
        int lvlPoints = (int)this.statPoints; // points distributed randomly
        this.statPoints -= lvlPoints;
        
        this.statBonus += this.bonusPoints;
        int bonus = (int)statBonus;  // points distributed manualy
        this.statBonus -= bonus;

        // TODO number of statpoints different based on skills and classes
        // TODO extra points from skills add here.
        switch (Game.getOption(GameConstants.OPTION_KEY_LEVELUP))
        {
            case GameConstants.OPTION_VALUE_LEVELUP_FULL_AUTO:    // all random
                lvlPoints += bonus;
                bonus = 0;
                break;
            case GameConstants.OPTION_VALUE_LEVELUP_MANUAL:         // all manual
                bonus += lvlPoints;
                lvlPoints = 0;
                break;
            default:
        }

        for (int i = 0; i < lvlPoints; i++)
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
    
    public int useMunition(int amount)
    {
        if(amount > this.munition)
            amount = munition;
        
        munition -= amount;
        
        return amount;
    }

    void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean canGetCrit()
    {
        return this.heroClass.canGetCrit();
    }

    @Override
    public double getCritModifier()
    {
        return this.heroClass.getCritModifer() * super.getCritModifier();
    }
    
    
}
