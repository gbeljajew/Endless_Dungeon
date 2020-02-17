/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.GameConstants;
import game.util.ResourcesContainer;
import game.battle.BattleLog;
import game.battle.enemy.Contrahent;
import game.util.Animations;
import game.util.drawable.Animation;
import game.util.drawable.RepeatableFortAndBackAnimation;
import game.util.drawable.ResetableAnimation;

class Slash extends AbstractActiveSkill
{

    private final ResetableAnimation animation;

    public Slash()
    {

        this.animation = Animations.getAnimation( "#SLASH" );
        this.color = "black";
        this.coolDown = 0;
        this.key = "#SLASH";
        this.nameKey = "skill.active.slash.name";
        this.type = SkillType.MELE;

    }

    @Override
    public Animation use( Contrahent me, Contrahent them )
    {
        this.animation.reset();
        this.animation.setPosition( them.getHitPosition().x, them.getHitPosition().y );

        BattleLog.log( me.getName() + " attacks " + them.getName() + " with " + this.getName() );
        SkillSuccess sks = them.tryAttack(SkillType.MELE, me, 1 );

        int damage = (int)me.calculateMeleDamage();
        damage = ( int )( damage * ( 1 + 0.1 * this.lvl ) );

        sks.reactAttack( damage, me, them, animation, type, AttackProperty.PHISICAL );

        return this.animation;
    }

}

class Bite extends AbstractActiveSkill
{

    private final RepeatableFortAndBackAnimation animation;

    public Bite()
    {
        animation = new RepeatableFortAndBackAnimation(
                ResourcesContainer.getMiskAnimationSprites( GameConstants.ANIMATION_KEY_BITE ),
                GameConstants.STANDARD_ANIMATION_DELAY, 1 );

        this.color = "red";
        this.key = "#BITE";
        this.nameKey = "skill.active.bite.name";
        this.type = SkillType.MELE;
    }

    @Override
    public Animation use( Contrahent me, Contrahent them )
    {
        this.animation.reset();
        this.animation.setPosition( them.getHitPosition().x, them.getHitPosition().y );

        BattleLog.log( me.getName() + " attacks " + them.getName() + " with " + this.getName() );
        SkillSuccess sks = them.tryAttack(SkillType.MELE, me, 1 );

        int damage = (int)me.calculateMeleDamage();
        damage = ( int )( damage * ( 1 + 0.1 * this.lvl ) );

        sks.reactAttack( damage, me, them, animation, type, AttackProperty.BITE, AttackProperty.PHISICAL );

        return this.animation;
    }

}
