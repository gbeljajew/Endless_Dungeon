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
public class JewelryItem extends GoldItem
{
    // 
    
    private int rarity;
    
    public JewelryItem()
    {
        // set rarity, returned summ and image is based on rarity.
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        // TODO
    }

    @Override
    public int getSumm()
    {
        return super.getSumm(); // TODO 
    }
    
}
