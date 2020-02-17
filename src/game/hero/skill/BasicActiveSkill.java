/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

/**
 *
 * @autor gbeljajew
 */
public class BasicActiveSkill implements Skill
{
    private int level;
    
    private final ActiveSkillCore type;

    public BasicActiveSkill(ActiveSkillCore type)
    {
        this.type = type;
        level = 1;
    }

    /**
     * 
     * @param type
     * @param level 
     */
    public BasicActiveSkill(ActiveSkillCore type, int level)
    {
        this.level = level;
        this.type = type;
    }
    
    @Override
    public int getLevel()
    {
        return this.level;
    }

    @Override
    public boolean isMaxLevel()
    {
        
        if(this.type.getMaxLvl() == -1) // -1 means no max level
            return false;
        
        return this.level >= this.type.getMaxLvl();
    }

    @Override
    public void levelSkill()
    {
        this.level++;   // one level per upgrade
    }

    public ActiveSkillCore getSkill()
    {
        return this.type;
    }
    
}
