/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.battle.BattleLog;
import game.battle.enemy.Contrahent;
import game.hero.StatOwner;
import game.hero.StatsEnum;
import game.util.Animations;
import game.util.drawable.Animation;
import game.util.drawable.ResetableAnimation;
import game.util.drawable.TwinAnimation;






class Fireball extends AbstractActiveSkill
{
    private final ResetableAnimation animation;
    public Fireball()
    {
        this.animation = Animations.getAnimation("#FIREBALL");
        this.color = "red";
        this.key ="#FIREBALL";
        this.manaConsumption0 = 5;
        this.manaConsumptionLvl = 2;
        this.nameKey = "skill.active.fireball.name";
        this.type = SkillType.MAGIC;
    }
    
    

    @Override
    public Animation use(Contrahent me, Contrahent them)
    {
        this.animation.reset();
        this.animation.setPosition(them.getHitPosition().x, them.getHitPosition().y);
        
        int manaUsage = this.getManaConsumption();
        
        BattleLog.log( me.getName() + " cast " + this.getName());
        
        manaUsage = me.getRealManaUsage(manaUsage);
        
        SkillSuccess sks = me.tryPerformMagic(3 + this.getLevel(), manaUsage);
        
        if(sks == SkillSuccess.FAIL)
        {
            BattleLog.log("cast failed, " + manaUsage + " was consumed.");
            
            ResetableAnimation fail = Animations.getAnimation("CAST_FAIL");
            me.consumeMana(manaUsage);
            fail.reset();
            fail.setPosition(me.getHitPosition().x, me.getHitPosition().y);
            
            return fail;
        }
        
        sks  = them.tryAttack(SkillType.MAGIC, me, 1, AttackProperty.FIRE);
        
        
        int damage = this.calculateDamage(me);
        
        
        sks.reactAttack(damage, me, them, animation, type, AttackProperty.FIRE, AttackProperty.MAGICAL);
        
        me.consumeMana(manaUsage);

        return this.animation;
    }

    private int calculateDamage(Contrahent me)
    {
        double res = me.calculateMagicDamage();
        
        res = (int)(res * (1.5 + 0.2 * this.lvl));
        
        return (int)res;
    }
    
}

class Leach extends AbstractActiveSkill
{

    private final TwinAnimation animation;

    public Leach()
    {
        this.animation = new TwinAnimation(
                Animations.getAnimation("#HEAL"), 
                Animations.getAnimation("#BITE"), 
                TwinAnimation.DelayType.STARTER_AFTER);
        
        this.color = "red";
        this.coolDown = 2;
        this.key = "#LEECH";
        this.manaConsumption0 = 0;
        this.manaConsumptionLvl = 1;
        this.nameKey = "";
    }
    
    
    
    @Override
    public Animation use(Contrahent me, Contrahent them)
    {
        this.animation.reset();
        this.animation.setStarterPosition(me.getHitPosition().x, me.getHitPosition().y);
        this.animation.setRecieverPosition(them.getHitPosition().x, them.getHitPosition().y);
        
        
        SkillSuccess tryAttack = them.tryAttack(SkillType.MELE, me, 1, AttackProperty.BITE,AttackProperty.MAGICAL);
        
        int damage = (int)(me.calculateMeleDamage() *2 / 3 + me.calculateMagicDamage() / 2);
        
        int healing = (int)(damage * (0.5 + 0.01 * lvl));
        
        tryAttack.reactAttack(damage, me, them, animation.getRecieverAnimation(), type, AttackProperty.BITE,AttackProperty.MAGICAL);
        
        tryAttack.reactHealing(healing, me, animation.getStarterAnimation(), AttackProperty.MAGICAL);
        
        
        return this.animation;
    }
    
}

