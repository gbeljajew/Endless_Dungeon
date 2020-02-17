/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.Game;
import game.GameConstants;
import game.GameLocale;
import game.util.ResourcesContainer;
import game.panels.shop.ShopItem;
import game.util.container.RandomKey;
import game.util.drawable.SimpleSizedDrawable;
import game.util.drawable.SizedDrawable;

/**
 *
 * @autor gbeljajew
 */
public enum PassiveSkillOld implements SkillCore, ShopItem, RandomKey
{
    MUNITION_QUALITY(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.munition.quality", -1, 100,50,1),
    
    MUNITION_FINDER(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.munition.finder", -1, 100,50,1),
    
    BIG_POCKETS(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.big.pocket", -1, 100,50,1),
    
    POTION_EFFECTIVENESS(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.potion.effectiveness", -1, 100,50,1),
    
    MARATHON_RUNNER(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.marathon.runner", -1, 100,50,1),
    
    MULTITHROW(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.multithrow", -1, 100,50,1),
    
    MUNITION_RECOVERY(new SimpleSizedDrawable(
            ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
        "skill.munition.recovery", -1, 100,50,1),
    ;

    private PassiveSkillOld(SizedDrawable icon, String nameKey, int maxLvl, int price0, 
            int pricePerLvl, double chance)
    {
        this.icon = icon;
        this.nameKey = nameKey;
        this.maxLvl = maxLvl;
        this.price0 = price0;
        this.pricePerLvl = pricePerLvl;
        this.chance = chance;
    }
    
    private final SizedDrawable icon;
    private final String nameKey;
    private final int maxLvl;
    private final int price0;
    private final int pricePerLvl;
    private final double chance;
    
    
    
    @Override
    public String getName()
    {
        return GameLocale.getString(nameKey + ".name");
    }
    
    @Override
    public String getDescription()
    {
        return GameLocale.getString(nameKey + ".description");
    }
    
    @Override
    public int getMaxLvl()
    {
        return maxLvl;
    }
    

    @Override
    public SizedDrawable getIcon()
    {
        return this.icon;
    }

    @Override
    public int detPrice()
    {
        int skillLevel = Game.getHero().getSkillLevel(this.getKey());
        
        return this.price0 + this.pricePerLvl * skillLevel;
    }

    @Override
    public double getChance()
    {
        return this.chance;
    }

    @Override
    public String getKey()
    {
        return this.toString();
    }

    
}
