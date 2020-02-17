/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.attack;

import game.battle.enemy.Resistence;
import java.awt.Image;

/**
 *
 * @author gbeljajew
 */
public class StatusAligment implements Comparable<StatusAligment>
{
    private final StatusAligments sa;
    private double buildUp;

    public StatusAligment(StatusAligments sa)
    {
        this.sa = sa;
    }
    
    public void buildUp( double amount)
    {
        if(this.buildUp < 0)
            this.buildUp = amount;
        else
            this.buildUp += amount;
    }
    
    public void reduce(Resistence res)
    {
        if(this.buildUp <= 0)
            return;
        
        double amount = res.getReduce();
        
        this.buildUp -= amount;
        
        if(this.buildUp < 0)
            this.buildUp = 0;
    }
    
    public Image getIcon()
    {
        return this.sa.getImage(this.buildUp);
    }
    
    public int getLevel()
    {
        return this.sa.getLevel(buildUp);
    }

    @Override
    public int compareTo(StatusAligment t)
    {
        
        if(this.getLevel() > t.getLevel())
            return 1;
        
        if(this.getLevel() < t.getLevel())
            return -1;
        
        if(this.buildUp > t.buildUp)
            return 1;
        
        if(this.buildUp < t.buildUp)
            return -1;
        
        return this.sa.getName().compareTo(t.sa.getName());
    }
    
}
