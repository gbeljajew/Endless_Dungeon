/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.GameConstants;
import game.util.ResourcesContainer;
import java.awt.Image;

/**
 *
 * @autor gbeljajew
 */
public enum StatsEnum
{
    /** Phisical attack */
    STR("stat.str", GameConstants.ICON_KEY_SWORD_ICON), 
    /** magic attack */
    MAG("stat.mag", GameConstants.ICON_KEY_WAND_ICON),
    /** Armor */
    ARM("stat.arm", GameConstants.ICON_KEY_SHILD_ICON),
    /** Vitality */
    VIT("stat.vit", GameConstants.ICON_KEY_HP_BAR),
    /** Speed */
    SPD("stat.spd", GameConstants.ICON_KEY_SPEED_ICON)
    ;
    
    public final String statNameKey;
    public final String statIconKey;

    private StatsEnum(String key, String statIconKey)
    {
        this.statNameKey = key;
        this.statIconKey = statIconKey;
    }

    Image getIcon()
    {
        return ResourcesContainer.getMiskImage(this.statIconKey);
    }
    
    
}
