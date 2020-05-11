/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.dungeon.shop;

import bin.game.Game;
import bin.game.panels.InnScreen;
import bin.game.panels.ScreenEnum;
import bin.game.panels.ShopScreen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class ShopFactory 
{
    private final static Map<ScreenEnum, JPanel> SHOP_PANELS = new HashMap<>();
    
    private final static Map<ScreenEnum, Shop> SHOPS = new HashMap<>();
    
    public static Map<ScreenEnum, JPanel> getShopScreens()
    {
        return SHOP_PANELS;
    }
    
    
    public static void init()
    {
        
        add(new AlchemyShop(), ScreenEnum.ALCHEMIST);
        
        add(new BlackSmith(), ScreenEnum.SMITH);
        
        add(new SkillShop(),ScreenEnum.SKILL_SHOP);
        
        add(new Trainer(), ScreenEnum.TRAINER);
        
        add(new Church(), ScreenEnum.CHURCH);
        
        // INN needs more buttons thats why it uses a diferent screen class
        INN inn = new INN();
        SHOPS.put(ScreenEnum.INN, inn);
        SHOP_PANELS.put(ScreenEnum.INN, new InnScreen(inn));
        
    }

    private static void add(Shop shop, ScreenEnum screen)
    {
        SHOPS.put(screen, shop);
        SHOP_PANELS.put(screen, new ShopScreen(shop));
    }
    
    public static void refreshShops()
    {
        if(Game.getHero() != null)
        {
            for (Map.Entry<ScreenEnum, Shop> entry : SHOPS.entrySet())
            {
                Shop s = entry.getValue();
                s.refresh();
            }
        }
    }
    
    public static void beforeShop(ScreenEnum screen)
    {
        Shop s = SHOPS.get(screen);
        
        if(s != null)
            s.beforeShop();
    }
}
