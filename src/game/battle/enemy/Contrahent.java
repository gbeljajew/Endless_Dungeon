/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import game.battle.BattleLogItem;
import game.battle.attack.Attack;
import game.battle.attack.AttackResult;
import game.battle.attack.Damage;
import game.battle.attack.StatusAligment;
import game.battle.attack.StatusAligments;
import game.hero.BarInfo;
import game.hero.PotionOwner;
import game.hero.SkillOwner;
import game.hero.StatOwner;
import game.hero.skill.ActiveSkillCore;
import java.awt.Point;
import java.util.List;

/**
 *
 * @autor gbeljajew
 */
public interface Contrahent extends StatOwner, BattleLogItem, SkillOwner, PotionOwner
{

    

    /**
     * call this method to get position of animation target.
     *
     * @return position of center of this figure on BattleScreen.
     */
    public Point getHitPosition();

    /**
     * call this method to get position of animation target.
     *
     * @return position outside of this figure on BattleScreen.
     */
    public Point getMissPosition();

    /**
     * set Position on BattleScreen.
     *
     * @param p
     */
    public void setHitPosition(Point p);

    public void setMissPosition(Point p);

    /**
     * try and see if attack hits, is blocked or is critical.
     * @param skillType
     * @param attacker
     * @param critChanceModifer
     * @param attackPropertys
     * @return 
     */
    @Deprecated
    public ActiveSkillCore.SkillSuccess tryAttack(ActiveSkillCore.SkillType skillType, Contrahent attacker, double critChanceModifer, ActiveSkillCore.AttackProperty... attackPropertys);

    /**
     * do some damage to contrahent. make sure you allready confirmed, that
     * attack not failed and not missed
     *
     * @param damage
     * @param attackProperty
     */
    @Deprecated
    public void trapDamage(int damage, ActiveSkillCore.AttackProperty... attackProperty);

    /**
     * for logging condition of enemy/hero called after enemy/hero was attacked
     */
    public void logCondition();

    public ActiveSkillCore.SkillSuccess tryPerformMagic(int complexity, int mana);
    
    public boolean tryCast(int complexity, int mana);
    
    public boolean tryMele(int complexity);

    public int getRealManaUsage(int manaUsage);

    public void consumeMana(int manaUsage);

    public double calculateCritDamage(double damage);

    public double calculateSuperCritDamage(double damage);

    public double calculateMeleDamage();

    public double calculateMagicDamage();

    public void heal(int heal);

    public ActiveSkillCore.SkillSuccess tryCastBuff(int complexity, int manaUsage);

    public int getHP();

    public int getMaxHP();

    public int getMP();

    public int getMaxMP();

    /**
     *
     * @return amount of exp given for killing it.
     */
    public int getExpForKilling();
    public int getGoldForKilling();
    
    public double calculateBlockDamage(double damage);

    public void removeRandomDebuff();

    public void addExtraHp(int extra);

    public void addExtraMp(int extra);

    /**
     * turn passed. it is time to reduce Buffs and StatsAligements.
     * regenerate HP and mana and do some more stuff
     */
    public void turnPassed();
    
    public BarInfo getHpBar();
    public BarInfo getMpBar();
    public BarInfo getTimeBar();
    
    public void update();
    public boolean isReady();
    public void doneWithTurn();

    public boolean isAlive();
    
    public AttackResult tryAttack(Attack attack);
    
    /**
     * how well you can dodge attacks.
     * @return 
     */
    public double getDodgeValue();
    
    /**
     * how well you are guarded against crits
     * @param critChance crit chance of enemy against a normal target.
     * @return 
     */
    public double recalculateCritChance(double critChance);
    
    public void damage(Damage damage);
    
    public double getCritModifier();
    
    public boolean canGetCrit();
    
    public int getStatAligementLvl(StatusAligments key);

    public List<StatusAligment> getStatAligements();

    public double calkulateToHit();
}
