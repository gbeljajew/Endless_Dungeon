package game.battle.attack;

import java.awt.Point;

import game.battle.enemy.Contrahent;
import game.util.drawable.ResetableAnimation;

public class AttackImpl implements Attack
{
    private final Contrahent attacker, target;
    private final Damage damage;
    private final ResetableAnimation animation;
    private boolean canCrit = true, 
                    canBeBlocked = true, 
                    aoe = false, 
                    alwaysCrit = false,
                    canMiss = true;
    
    private double homing = 0;

    public AttackImpl(Contrahent attacker, Contrahent target, Damage damage, ResetableAnimation animation)
    {
        super();
        this.attacker = attacker;
        this.target = target;
        this.damage = damage;
        this.animation = animation;
    }

    @Override
    public AttackResult hit()
    {
        Point pos = this.target.getHitPosition();
        
        this.animation.setPosition(pos.x, pos.y);
        
        return new AttackResult(animation, damage, target, "HIT");
    }

    @Override
    public AttackResult miss()
    {
        Point pos = this.target.getMissPosition();
        
        this.animation.setPosition(pos.x, pos.y);
        
        return new AttackResult(animation, null, target, "MISS");
    }

    @Override
    public AttackResult crit()
    {
        Point pos = this.target.getHitPosition();
        
        this.animation.setPosition(pos.x, pos.y);
        
        return new AttackResult(animation, this.damage.toCrit(attacker), target, "CRIT");
    }

    @Override
    public AttackResult supercrit()
    {
        Point pos = this.target.getHitPosition();
        
        this.animation.setPosition(pos.x, pos.y);
        
        return new AttackResult(animation, this.damage.toSuperCrit(attacker), target, "SUPERCRIT");
    }

    @Override
    public AttackResult reflected()
    {
        Point pos = this.attacker.getHitPosition();
        
        this.animation.setPosition(pos.x, pos.y);
        
        return new AttackResult(animation, damage, attacker, "REFLECTED");
       
    }

    

    @Override
    public double getToHit()
    {
        double toHit = this.attacker.calkulateToHit();
        
        if(this.aoe == true)
            toHit *= 2;
        
        toHit *= 1 + this.homing;
            
        return toHit;
    }

    @Override
    public double getCritChance()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    

    @Override
    public boolean canMiss()
    {
        return this.canMiss;
    }

    

    @Override
    public boolean canBeBlocked()
    {
        
        return this.canBeBlocked;
    }

    @Override
    public boolean canCrit()
    {
        
        return this.canCrit;
    }

    @Override
    public boolean allwaysCrit()
    {
        return this.alwaysCrit;
    }

    public void setCanCrit(boolean canCrit)
    {
        this.canCrit = canCrit;
    }

    public void setCanBeBlocked(boolean canBeBlocked)
    {
        this.canBeBlocked = canBeBlocked;
    }

    public void setAoe(boolean aoe)
    {
        this.aoe = aoe;
    }

    public void setAlwaysCrit(boolean alwaysCrit)
    {
        this.alwaysCrit = alwaysCrit;
    }

    public void setCanMiss(boolean canMiss)
    {
        this.canMiss = canMiss;
    }

    public void setHoming(double homing)
    {
        this.homing = homing;
    }
    
}
