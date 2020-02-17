/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

/**
 *
 * @author gbeljajew
 */
public class ExtraPoints
{
    private int points = 0;
    private int pointsUsed = 0;
    
    public int getPoints()
    {
        return this.points;
    }
    
    public int getPointsLeft()
    {
        return this.points - this.pointsUsed;
    }
    
    public void add(int p)
    {
        this.points += p;
    }
    
    public boolean use()
    {
        if(this.points > this.pointsUsed)
        {
            this.pointsUsed++;
            return true;
        }
        return false;
    }

    public void confirmUsage()
    {
        this.points -= this.pointsUsed;
        this.pointsUsed = 0;
    }
    
    void resetUsage()
    {
        this.pointsUsed = 0;
    }
}
