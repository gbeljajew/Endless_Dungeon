/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.dungeon.shop;

import bin.game.GameLocale;
import bin.game.resources.GraphicResources;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class SkillShop implements Shop
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
        return GameLocale.getString("core.shop.name.skills");
    }

    @Override
    public void refresh()
    {
    }

    @Override
    public Image getShopOwner()
    {
        return GraphicResources.getMiskImage("core.shop.owner.skill");
    }

    @Override
    public void beforeShop()
    {
    }

    @Override
    public void afterShop()
    {
    }
    
    class ShopPanel extends JPanel
    {
        
    }

}
