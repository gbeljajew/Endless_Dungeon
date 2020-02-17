/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import java.awt.Graphics2D;

/**
 *
 * @autor gbeljajew
 */
public class MunitionItem implements Item
{
    private int count = 5;
    
    public int getCount()
    {
        return this.count;
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        // TODO
    }
    
}
