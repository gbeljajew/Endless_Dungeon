/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import game.hero.skill.ActiveSkillCore;
import game.util.container.RandomKeyImpl;
import game.util.container.RandomSet;
import game.util.container.RandomizerWrapper;
import java.util.List;


public class MonsterAiDefaultImpl implements MonsterAi
{
    
    private final Contrahent battleCore;
    private final RandomSet<RandomizerWrapper<ActiveSkillCore>> randomiser;

    public MonsterAiDefaultImpl(Contrahent battleCore, Monster monster)
    {
        this.battleCore = battleCore;
        List<RandomKeyImpl> skillAiList = monster.getSkillAiList();
        List<ActiveSkillCore> activeSkills = battleCore.getActiveSkills();
        
        this.randomiser = new RandomSet<>();
        
        for (RandomKeyImpl defaultRandomKey : skillAiList)
        {
            for (ActiveSkillCore activeSkill : activeSkills)
            {
                if(activeSkill.getKey().equals(defaultRandomKey.getKey()))
                    this.randomiser.add(new RandomizerWrapper<>(activeSkill, defaultRandomKey.getChance()));
            }
        }
        
    }

    
    
    @Override
    public Contrahent getBattleCore()
    {
        return this.battleCore;
    }

    @Override
    public ActiveSkillCore decide(Contrahent hero)
    {
        ActiveSkillCore res = null;

        if (randomiser.size() > 0)
        {

            int count = 0;

            do
            {
                res = this.randomiser.get().getItem();
                count++;
            } while (!res.canBeUsed() && count < 5);

            if (!res.canBeUsed())
            {
                res = null;
            }
        }
        
        this.battleCore.doneWithTurn();
        
        return res;

    }
    
}
