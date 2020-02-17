/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.Game;
import game.GameConstants;
import game.battle.BattleLog;
import game.battle.enemy.Contrahent;
import game.util.Animations;
import game.util.drawable.Animation;
import game.util.drawable.RainAnimation;
import game.util.drawable.ResetableAnimation;


class Heal extends AbstractActiveSkill
{
    private final ResetableAnimation animation;
    public Heal()
    {
        this.animation = Animations.getAnimation("#HEAL");
        this.color = "blue";
        this.coolDown = 3;
        this.key = "#HEAL";
        this.manaConsumption0 = 8;
        this.manaConsumptionLvl = 2;
        this.nameKey = "skill.active.heal.name";
        this.type = SkillType.SUPPORT;
    }
    
    

    @Override
    public Animation use(Contrahent me, Contrahent them)
    {
        
        System.out.println("HEAL");
        
        this.animation.reset();
        this.animation.setPosition(me.getHitPosition().x, me.getHitPosition().y);
        
        int manaUsage = this.getManaConsumption();
        
        BattleLog.log( me.getName() + " cast " + this.getName());
        
        manaUsage = me.getRealManaUsage(manaUsage);
        
        SkillSuccess sks = me.tryCastBuff(3 + this.getLevel(), manaUsage);
        
        if(sks == SkillSuccess.FAIL)
        {
            BattleLog.log("cast failed, " + manaUsage + " was consumed.");
            
            ResetableAnimation fail = Animations.getAnimation("CAST_FAIL");
            me.consumeMana(manaUsage);
            fail.reset();
            fail.setPosition(me.getHitPosition().x, me.getHitPosition().y);
            
            return fail;
        }
        
        RainAnimation ra = (RainAnimation)this.animation;
        ra.setRepeats(this.lvl + 5);
        ra.setDimension(GameConstants.HERO_SPRITE_WIDTH * 2, GameConstants.HERO_SPRITE_HEIGTH );
        
        int heal = this.calculateHealAmount(me);
        
        sks.reactHealing(heal, me, animation, AttackProperty.HEAL);
        
        return this.animation;
    }

    private int calculateHealAmount(Contrahent me)
    {
        int res = (int)me.calculateMagicDamage();
        
        res = (int)(res * (2 + 0.2 * this.getLevel()));
        
        return res;
    }
    
}


