/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.dungeon.shop;

import bin.game.GameLocale;
import bin.game.resources.GraphicResources;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class Church implements Shop
{

    private final ShopPanel shopPanel = new ShopPanel();

    @Override
    public JPanel getShopPanel()
    {
        return this.shopPanel;
    }

    @Override
    public String getName()
    {
        return GameLocale.getString("core.shop.name.church");
    }

    @Override
    public void refresh()
    {
    }

    @Override
    public Image getShopOwner()
    {
        return GraphicResources.getMiskImage("core.shop.owner.church");
    }

    @Override
    public void beforeShop()
    {
        System.out.println("before shop church");
    }

    @Override
    public void afterShop()
    {
        
    }
    
    class ShopPanel extends JPanel
    {

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            
            g.drawString("Church", 20, 20);
            
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        
    }

}
