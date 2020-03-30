/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.hero;


import bin.game.GameConstants;
import bin.game.resources.GraphicResources;
import java.awt.Image;

/**
 *
 * @autor gbeljajew
 */
public enum StatsEnum
{
    /** Phisical attack */
    STR("stat.str", "Sword icon"), 
    /** magic attack */
    MAG("stat.mag", "wand icon"),
    /** Armor */
    ARM("stat.arm", "shild icon"),
    /** Vitality */
    VIT("stat.vit", "HP heart"),
    /** Speed */
    SPD("stat.spd", "speed icon")
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
        return GraphicResources.getMiskImage(this.statIconKey);
    }
    
    
}
