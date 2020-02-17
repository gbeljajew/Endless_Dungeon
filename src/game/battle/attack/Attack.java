/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.attack;

/**
 *
 * @author gbeljajew
 */
public interface Attack
{

    public AttackResult hit();
    public AttackResult miss();
    public AttackResult crit();
    public AttackResult supercrit();
    public AttackResult reflected();

    public double getToHit();

    public double getCritChance();
    
    public boolean canMiss();

    public boolean canBeBlocked();

    public boolean canCrit();

    public boolean allwaysCrit();
    
}
