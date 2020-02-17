/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

/**
 *
 * @author gbeljajew
 */
public class Resistence
{
    private static final double GROW_RATE = 0.001;
    
    private double resistance;
    private final boolean mayGrow;
    private final double growChance;
    private final boolean immune;

    public Resistence(double resistance, boolean immune, boolean mayGrow, double growChance)
    {
        this.resistance = resistance;
        this.mayGrow = mayGrow;
        this.growChance = growChance;
        this.immune = immune;
    }

    public Resistence()
    {
        this.resistance = 0.0;
        this.mayGrow = true;
        this.growChance = 0.1;
        this.immune = false;
    }
    
    public double resist(double value)
    {
        double result = value - value * this.resistance;
        
        this.grow();
        
        return result;
    }

    public boolean isImmune()
    {
        return immune;
    }

    /**
     * 
     * @param buildUp
     * @return 
     */
    public double getReduce()
    {
        double res = 10;
        
        res *= 1 + this.resistance;
        
        if(res < 5)
            res = 5;
        
        this.grow();
        
        return res;
    }
    
    private void grow()
    {
        if(this.mayGrow && Math.random() < this.growChance)
            this.resistance += GROW_RATE;
    }
}
