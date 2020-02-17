/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import java.util.List;

import game.hero.Stat;
import game.hero.skill.ActiveSkillCore;
import game.hero.skill.PassiveSkill;

/**
 *
 * @autor gbeljajew
 */
public class BasicEnemy extends BasicContrahent 
{
    
    private final int goldForKilling, expForKilling;
    
    private final double critModifer;
    private final boolean canGetCrit;
    
    /**
     * 
     * @param stats
     * @param activeSkills
     * @param passiveSkills
     * @param name
     * @param lvl
     * @param expForKilling
     * @param goldForKilling 
     * @param critModifer 
     */
    public BasicEnemy(
            List<Stat> stats, 
            List<ActiveSkillCore> activeSkills, 
            List<PassiveSkill> passiveSkills, 
            String name,
            int lvl,
            int expForKilling,
            int goldForKilling,
            double critModifer, 
            boolean canGetCrit)
    {
        for (Stat stat : stats)
        {
            this.stats.put(stat.getNameKey(), stat);
        }
        
        for (PassiveSkill passiveSkill : passiveSkills)
        {
            this.passiveSkills.put(passiveSkill.getName(), passiveSkill);
        }
        
        for (ActiveSkillCore activeSkill : activeSkills)
        {
            this.activeSkills.put(activeSkill.getKey(), activeSkill);
        }
        
        this.goldForKilling = goldForKilling;
        this.expForKilling = expForKilling;
        this.lvl = lvl;
        this.name = name + " (" + lvl + ")";
        
        this.hp = this.getMaxHP();
        this.mp = this.getMaxMP();
        this.critModifer = critModifer;
        this.canGetCrit = canGetCrit;
    }
    
    
    

    

    @Override
    public int getExpForKilling()
    {
        return this.expForKilling;
    }

    @Override
    public int getGoldForKilling()
    {
        return this.goldForKilling;
    }

    @Override
    public double getCritModifier()
    {
        return this.critModifer * super.getCritModifier();
    }

    @Override
    public boolean canGetCrit()
    {
        return this.canGetCrit;
    }

    
    
    
    
}
