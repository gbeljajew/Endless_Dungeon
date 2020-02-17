/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.panels.shop;

import game.Game;
import game.GameConstants;
import game.GameLocale;
import game.GameResources;
import game.util.drawable.SimpleSizedDrawable;
import game.util.drawable.SizedDrawable;
import game.util.ResourcesContainer;
import game.hero.skill.SkillFactory;
import game.items.Potion;
import game.util.container.RandomKey;
import game.util.container.RandomSet;
import game.util.container.Randomizable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gbeljajew
 */
public class ShopFactory
{

    private ShopFactory()
    {
    } // no constructor

    private final static RandomSet<ShopType> SHOPS = new RandomSet<>();

    static
    {
        // TODO 

        SHOPS.add(ShopType.POTION);
        SHOPS.add(ShopType.SKILL);
    }

    public static void showShop()
    {
    
        ShopPanel shopPanel = new ShopPanel(SHOPS.get().generateShop());
        shopPanel.setVisible(true);
    }
}

enum ShopType implements Randomizable
{
    POTION(1) {
        @Override
        Shop generateShop()
        {
            return new PotionShop();
        }
    }, SKILL(1) {
        @Override
        Shop generateShop()
        {
            return new SkillShop();
        }
    };

    private ShopType(double chance)
    {
        this.chance = chance;
    }

    
    
    private final double chance;
    
    abstract Shop generateShop();
    
    
    @Override
    public double getChance()
    {
        return chance;
    }
}

class PotionShop implements Shop
{

    private final SimpleSizedDrawable shopOwner = new SimpleSizedDrawable(
            ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_SHOP_PIXY));

    List<ShopItem> shopItems = new ArrayList<>();

    public PotionShop()
    {
        for (int i = 0; i < 5; i++)
        {
            this.shopItems.add(Potion.getRandomPotion());
        }
    }

    @Override
    public List<ShopItem> getShopItems()
    {
        return this.shopItems;
    }

    @Override
    public SizedDrawable getShopOwner()
    {
        return this.shopOwner;
    }

    @Override
    public String getShopName()
    {
        return GameLocale.getString("shop.name.potion");
    }

}

class SkillShop implements Shop
{
    
    private final SimpleSizedDrawable shopOwner = new SimpleSizedDrawable(
            ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_SHOP_SUCCUB));
    
    

    @Override
    public List<ShopItem> getShopItems()
    {
        Set<ShopItem> res = new HashSet<>();

        // 1.st position has chance to be a skill, hero knows
        if (Math.random() < 0.5)
        {
            res.add(this.getRandomHeroSkill());
            
        }
        else
        {
            res.add(this.getRandomSkillFromHeroClass());
        }
        
        // 2.nd position a random skill from hero class
        res.add(this.getRandomSkillFromHeroClass());
        
        // 3.rd position has a chance to get a skill from whole list of skills
        if (Math.random() < 0.5)
        {
            res.add(this.getRandomSkillFromWorld());
            
            System.out.println("All skills");
        }
        else
        {
            res.add(this.getRandomSkillFromHeroClass());
        }
        
        // have it to be exactly 3 items?
        while(res.size() < 3)
            res.add(this.getRandomSkillFromHeroClass());
        
        
        return new ArrayList<>(res);
    }

    @Override
    public SizedDrawable getShopOwner()
    {
        return this.shopOwner;
    }

    @Override
    public String getShopName()
    {
        return GameLocale.getString("shop.name.skill");
    }

    private ShopItem getRandomHeroSkill()
    {
        List<RandomKey> knownSkills = Game.getHero().getAllKnownSkills();

        if (knownSkills.isEmpty())
        {
            return this.getRandomSkillFromHeroClass();
        }

        RandomSet<RandomKey> set = new RandomSet<>();

        set.add(knownSkills);

        return SkillFactory.getShopItem(set.get().getKey());
    }

    private ShopItem getRandomSkillFromHeroClass()
    {
        List<RandomKey> classSkills = Game.getHero().getHeroClass().getClassSkills();

        if (classSkills.isEmpty())
        {
            return this.getRandomSkillFromWorld();
        }

        RandomSet<RandomKey> set = new RandomSet<>();

        set.add(classSkills);

        return SkillFactory.getShopItem(set.get().getKey());
    }

    private ShopItem getRandomSkillFromWorld()
    {
        List<RandomKey> skills = SkillFactory.getBuyableSkills();
        
        RandomSet<RandomKey> set = new RandomSet<>();

        set.add(skills);

        return SkillFactory.getShopItem(set.get().getKey()); 
    }

}
