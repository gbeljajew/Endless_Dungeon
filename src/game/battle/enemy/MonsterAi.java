/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import game.hero.skill.ActiveSkillCore;

/**
 *
 * @autor gbeljajew
 */
public interface MonsterAi
{
    public Contrahent getBattleCore();
    
    public ActiveSkillCore decide(Contrahent hero);
}
