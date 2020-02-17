/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import game.Game;
import java.awt.Graphics2D;

/**
 *
 * @autor gbeljajew
 */
public class GoldItem implements Item
{

    private final int summ;

    public GoldItem()
    {
        int min = Game.getCurrentDungeonLevel() * 5;
        int dMax = Game.getCurrentDungeonLevel() * 10;
        
        this.summ = min + (int)(Math.random() * dMax);
        
    }
    
    public int getSumm()
    {
        return summ;
    }
    
    
    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        // TODO 
    }
    
}
