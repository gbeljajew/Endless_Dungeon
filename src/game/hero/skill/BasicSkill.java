/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;


public class BasicSkill implements Skill
{
    private int level;
    
    private final SkillCore type;

    public BasicSkill(SkillCore type)
    {
        this.type = type;
        level = 1;
    }

    /**
     * 
     * @param type
     * @param level 
     */
    public BasicSkill(SkillCore type, int level)
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

    
    
}

