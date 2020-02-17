/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.battle.BattleLog;
import game.battle.enemy.Contrahent;
import game.util.Animations;
import game.util.drawable.Animation;
import game.util.drawable.ResetableAnimation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @autor gbeljajew
 */
public interface ActiveSkillCore extends SkillCore
{
    public enum SkillType
    {
        MELE, MAGIC, SUPPORT;
    }
    
    public enum SkillSuccess
    {
        HIT {
            @Override
            public ResetableAnimation reactAttack(int damage, Contrahent me, Contrahent them, ResetableAnimation animation, SkillType type, AttackProperty... attackPropertys)
            {
                animation.setPosition(them.getHitPosition().x, them.getHitPosition().y);
                them.trapDamage(damage, attackPropertys);
                
                return animation;
            }

            @Override
            public ResetableAnimation reactHealing(int healing, Contrahent me, ResetableAnimation animation, AttackProperty... attackProperty)
            {
                animation.setPosition(me.getHitPosition().x, me.getHitPosition().y);
                me.heal(healing);
                
                return animation;
            }
        },MISS {
            @Override
            public ResetableAnimation reactAttack(int damage,Contrahent me, Contrahent them, ResetableAnimation animation, SkillType type, AttackProperty... attackPropertys)
            {
                animation.setPosition(them.getMissPosition().x, them.getMissPosition().y);
                BattleLog.log("MISS");
                
                return animation;
            }

            @Override
            public ResetableAnimation reactHealing(int healing, Contrahent me, ResetableAnimation animation, AttackProperty... attackProperty)
            {
                animation.setPosition(me.getMissPosition().x, me.getMissPosition().y);
                BattleLog.log("MISS");
                
                return animation;
            }
        },FAIL {
            @Override
            public ResetableAnimation reactAttack(int damage,Contrahent me, Contrahent them, ResetableAnimation animation, SkillType type, AttackProperty... attackPropertys)
            {
                BattleLog.log("cast filed");
                
                ResetableAnimation battleAnimation = Animations.getAnimation("CAST_FAIL");
                battleAnimation.setPosition(me.getHitPosition().x, me.getHitPosition().y);
                return battleAnimation;
            }

            @Override
            public ResetableAnimation reactHealing(int healing, Contrahent me, ResetableAnimation animation, AttackProperty... attackProperty)
            {
                BattleLog.log("cast filed");
                
                ResetableAnimation battleAnimation = Animations.getAnimation("CAST_FAIL");
                battleAnimation.setPosition(me.getHitPosition().x, me.getHitPosition().y);
                return battleAnimation;
            }
        },CRIT {
            @Override
            public ResetableAnimation reactAttack(int damage,Contrahent me, Contrahent them, ResetableAnimation animation, SkillType type, AttackProperty... attackPropertys)
            {
                BattleLog.log("CRIT");
                animation.setPosition(them.getHitPosition().x, them.getHitPosition().y);
                damage = (int)me.calculateCritDamage(damage);
                
                List<AttackProperty> ap = new ArrayList(Arrays.asList(attackPropertys));
                ap.add(AttackProperty.CRIT);
                
                them.trapDamage(damage, ap.toArray(attackPropertys));
                return animation;
            }

            @Override
            public ResetableAnimation reactHealing(int healing, Contrahent me, ResetableAnimation animation, AttackProperty... attackProperty)
            {
                BattleLog.log("CRIT");
                animation.setPosition(me.getHitPosition().x, me.getHitPosition().y);
                healing = (int)me.calculateCritDamage(healing);
                
                List<AttackProperty> ap = new ArrayList(Arrays.asList(attackProperty));
                ap.add(AttackProperty.CRIT);
                
                me.heal(healing);
                return animation;
            }
        },
        SUPERCRIT{
            @Override
            public ResetableAnimation reactAttack(int damage,Contrahent me, Contrahent them, ResetableAnimation animation, SkillType type, AttackProperty... attackPropertys)
            {
                BattleLog.log("SUPERCRIT");
                animation.setPosition(them.getHitPosition().x, them.getHitPosition().y);
                damage = (int)me.calculateSuperCritDamage(damage);
                
                List<AttackProperty> ap = new ArrayList(Arrays.asList(attackPropertys));
                ap.add(AttackProperty.SUPERCRIT);
                
                them.trapDamage(damage, ap.toArray(attackPropertys));
                return animation;
            }

            @Override
            public ResetableAnimation reactHealing(int healing, Contrahent me, ResetableAnimation animation, AttackProperty... attackProperty)
            {
                BattleLog.log("SUPERCRIT");
                animation.setPosition(me.getHitPosition().x, me.getHitPosition().y);
                healing = (int)me.calculateSuperCritDamage(healing);
                
                List<AttackProperty> ap = new ArrayList(Arrays.asList(attackProperty));
                ap.add(AttackProperty.SUPERCRIT);
                
                me.heal(healing);
                return animation;
            }
        },
        BLOCK{
            @Override
            public ResetableAnimation reactAttack(int damage,Contrahent me, Contrahent them, ResetableAnimation animation, SkillType type, AttackProperty... attackPropertys)
            {
                BattleLog.log("BLOCK");
                animation.setPosition(them.getHitPosition().x, them.getHitPosition().y);
                damage = (int)them.calculateBlockDamage(damage);
                them.trapDamage(damage, attackPropertys);
                return animation;
            }

            @Override
            public ResetableAnimation reactHealing(int healing, Contrahent me, ResetableAnimation animation, AttackProperty... attackProperty)
            {
                return animation;
            }
        },
        
        ;
        
        public abstract ResetableAnimation reactAttack(int damage,
                Contrahent me, Contrahent them, 
                ResetableAnimation animation, 
                SkillType type, 
                AttackProperty... attackPropertys); 
        
        public abstract ResetableAnimation reactHealing(int healing, Contrahent me, 
                ResetableAnimation animation, AttackProperty... attackProperty);
    }
    
    public enum AttackProperty
    {
        PHISICAL, MAGICAL, FIRE, ICE, ARMOR_PIERCING, CRIT, SUPERCRIT, HEAL, BITE, BLOCK,
        AOE, HOMING;
    }
    
    
    
    /**
     * 
     * @return how much mana is konsumed for using this skill
     */
    public int getManaConsumption();
    
    /**
     * 
     * @return type of this skill
     */
    public SkillType getType();
    
    /**
     * 
     * @param me
     * @param them
     * @return 
     */
    public Animation use(Contrahent me, Contrahent them);
    
    public int getLeftCoolDown();
    public int getCoolDown();
    
    /**
     * is called every turn. for Skills it reduces cooldowt by 1
     */
    public void timePassed();
    
    public int getLevel();
    
    public boolean levelSkill();
    
    /**
     * call before every battle to reset cooldowns
     */
    public void resetBeforBattle();
    
    public boolean canBeUsed();
    
    public void setLevel(int lvl);
    
}

