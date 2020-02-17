/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import game.GameConstants;
import game.GameLocale;
import game.util.ResourcesContainer;
import game.panels.shop.ShopItem;
import game.util.container.RandomSet;
import game.util.container.RandomizerWrapper;
import game.util.drawable.SizedDrawable;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @autor gbeljajew
 */
public enum Potion implements Item, ShopItem, SizedDrawable
{
    // TODO adjust prices 
    
    HP_POTION (
            ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_HP_POTION),
            "potion.hp.", 100, 5),
    MP_POTION(
            ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_MP_POTION),
            "potion.mp.", 150, 5),
    SPEED_POTION(
            ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_GREEN_POTION),
            "potion.speed.", 200, 4),
    BOOST_POTION (
            ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_GOLD_POTION),
            "potion.boost.", 500, 3);
    
    
    private static final RandomSet<RandomizerWrapper<Potion>> POTIONS = new RandomSet<>();
    
    
    static
    {
        POTIONS.add(new RandomizerWrapper<>(HP_POTION, 5));
        POTIONS.add(new RandomizerWrapper<>(MP_POTION, 3));
        POTIONS.add(new RandomizerWrapper<>(SPEED_POTION, 1));
        POTIONS.add(new RandomizerWrapper<>(BOOST_POTION, 0.1));
    }
    
    public static Potion getRandomPotion()
    {
        return POTIONS.get().getItem();
    }
    
    private final Image image;
    private final String name;
    private final String description;
    private final int price;
    private final int maxAmount;
    

    Potion(Image image, String key, int price, int maxAmount)
    {
        this.image = image;
        this.name = GameLocale.getString(key + "name");
        this.description = GameLocale.getString(key + "description");
        this.price = price;
        this.maxAmount = maxAmount;
    }
    
    
    
    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        int width = this.image.getWidth(null);
        int high = this.image.getHeight(null);
        g.drawImage(image, kameraOffsetX, kameraOffsetY, null);
    }

    @Override
    public SizedDrawable getIcon()
    {
        return this;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    @Override
    public int detPrice()
    {
        return this.price;
    }

    @Override
    public int getWidth()
    {
        return this.image.getWidth(null);
    }

    @Override
    public int getHeight()
    {
        return this.image.getHeight(null);
    }

    @Override
    public String getKey()
    {
        return this.toString();
    }
    
    public int getMaxAmount()
    {
        return this.maxAmount;
    }
}



