/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.Game;
import game.GameConstants;
import game.GameLocale;
import game.battle.BattleLogItem;
import game.battle.enemy.Contrahent;
import game.util.drawable.ResetableAnimation;
import game.util.drawable.Animation;

/**
 *
 * @autor gbeljajew
 */
public abstract class AbstractActiveSkill implements ActiveSkillCore, BattleLogItem
{

    protected int 
            /** how much mana is used for this skill on level = 0  */
            manaConsumption0, 
            /** how much mana consumption grows per level */
            manaConsumptionLvl, 
            /** maximal level for this skill. 0 means no limit */ 
            maxLvl, 
            /** current level of this skill */
            lvl = 0;
    protected int 
            /** how many turns you have to wait untill you can use it again */
            coolDown, 
            /** how many turns left untill you can use this skill */
            currentCoolDown;
    protected SkillType type;
    protected String key;
    protected String nameKey;
    protected String color;
    
    

    @Override
    public int getManaConsumption()
    {
        return this.manaConsumption0 + this.manaConsumptionLvl * Game.getHero().getSkillLevel(this.getKey());
    }

    @Override
    public SkillType getType()
    {
        return this.type;
    }

    @Override
    public int getMaxLvl()
    {
        return this.maxLvl;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public int getLeftCoolDown()
    {
        return this.currentCoolDown;
    }

    @Override
    public int getCoolDown()
    {
        return this.coolDown;
    }

    @Override
    public void timePassed()
    {
        if(this.currentCoolDown > 0)
            this.currentCoolDown--;
    }

    @Override
    public String getName()
    {
        return GameLocale.getString(nameKey);
    }

    @Override
    public String getNameColor()
    {
        return this.color;
    }

    @Override
    public abstract Animation use(Contrahent me, Contrahent them); // other have to implement this one

    @Override
    public int getLevel()
    {
        return this.lvl;
    }

    @Override
    public boolean levelSkill()
    {
        if(this.maxLvl > 0 && this.lvl >= this.maxLvl)
            return false;
        
        this.lvl ++;
        
        return true;
    }

    @Override
    public void resetBeforBattle()
    {
        this.currentCoolDown = 0;
    }

    @Override
    public boolean canBeUsed()
    {
        return this.currentCoolDown <= 0;
    }

    @Override
    public void setLevel(int lvl)
    {
        this.lvl = lvl;
    }

    @Override
    public String toString()
    {
        return "ActiveSkill: " + this.key + " lvl " + this.lvl;
    }

    
    
}
