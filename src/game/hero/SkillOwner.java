/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.hero.skill.ActiveSkillCore;
import java.util.List;

/**
 *
 * @autor gbeljajew
 */
public interface SkillOwner
{
    public void addSkill(String key);
    
    public int getSkillLevel(String key);
    
    /**
     * 
     * @return keys for owned skills
     */
    public List<String> getOwnedSkills();
    
    public List<ActiveSkillCore> getActiveSkills();
    public List<ActiveSkillCore> getSkills(ActiveSkillCore.SkillType skillType);
    
}
