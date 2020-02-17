/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.attack;

import java.awt.Graphics2D;

import game.battle.BattleLog;
import game.battle.enemy.Contrahent;
import game.util.drawable.Animation;

/**
 *
 * @author gbeljajew
 */
public class AttackResult implements Animation
{
    private final Animation animation;
    private final Damage damage;
    private final Contrahent target;
    
    // TODO something better
    private final String text;
    
    private boolean isDone = false;
    
    public AttackResult(Animation animation, Damage damage, Contrahent target, String text)
    {
        super();
        this.animation = animation;
        this.damage = damage;
        this.target = target;
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        if(this.animation != null)
            this.animation.draw(g, x, y);
    }

    @Override
    public boolean isDone()
    {
        if(this.animation == null)
        {
            this.commit();
            return true;
        }
        
        if(this.animation.isDone() && this.isDone == false)
        {
            this.commit();
            return true;
        }
        
        return this.isDone;
        
    }

    
    private void commit()
    {
        if(this.damage != null && this.target != null)
            this.target.damage(damage);
        
        if(this.text != null)
            BattleLog.log(text);
        
        this.isDone = true;
    }

    
}


