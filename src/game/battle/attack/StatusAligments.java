/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.attack;

import game.GameLocale;
import game.battle.enemy.Resistable;
import java.awt.Image;
import static game.util.ResourcesContainer.getMiskTileSet;

/**
 * bad/good status aligments.
 * @author gbeljajew
 */
public enum StatusAligments implements Resistable
{
    /** slows the time gauge. wears off after time  */
    SLOW    ("stat.aligment.slow",true,true,100,4,5),               //
    /** stops the time gauge completely. wears off after time */
    STOP    ("stat.aligment.stop",true,true,100,6,7),               //
    /** damages you at start of your turn */
    POISON  ("stat.aligment.poison",true,false,100,0,1),            //
    /** damages you at start of your turn */
    BLEED   ("stat.aligment.bleed",true,false,100,2,3),             //
    /** it is easier to use complex attacks and magic */
    FRESH   ("stat.aligment.fresh",false,false,100,7,7),            //
    /** restores HP */
    REGEN   ("stat.aligment.regen",false,false,100,8,8),            //
    /** damages you at start of your turn */
    BURN    ("stat.aligment.burn",true,false,100,9,10),             //
    /** hides your presence so enemy will miss often */
    HIDDEN  ("stat.aligment.hidden",false,false,100,12,12),         //
    /** restores a little of mp */
    MP_REGEN("stat.aligment.mp_regen",false,false,100,13,13),       //
    /** enemy knows where you are hiding */
    MARKED  ("stat.aligment.marked",true,false,100,14,15),          //
    /** better chances to hit */
    EYE_OF_MIND("stat.aligment.eyeofmind",false,false,100,0,0),     //
    /** bad chances to hit target */
    BLIND   ("stat.aligment.blind",true,false,100,0,0),             //
    ;
    private final String nameKey;
    private final boolean bad;
    private final boolean updateInUpdate;
    /** how many points you need to collect to get first level in this aligment */
    private final int complexity;
    
    private final int iconIndex;
    private final int iconIndexDisabled;

    private StatusAligments(String nameKey, boolean bad, boolean updateInUpdate, int complexity, int iconIndex, int iconIndexDisabled)
    {
        this.nameKey = nameKey;
        this.bad = bad;
        this.updateInUpdate = updateInUpdate;
        this.complexity = complexity;
        this.iconIndex = iconIndex;
        this.iconIndexDisabled = iconIndexDisabled;
    }
    
    public String getName()
    {
        return GameLocale.getString(nameKey + ".name");
    }
    
    public String getDescription()
    {
        return GameLocale.getString(nameKey + ".description");
    }
    
    public boolean isBad()
    {
        return this.bad;
    }
    
    public boolean shouldUpdateInUpdate()
    {
        return this.updateInUpdate;
    }
    
    public int getLevel(double buildUp)
    {
        
        if(buildUp <= 0)
            return 0;
        
        int lvl = 0;
        
        while(buildUp > this.complexity * (lvl + 1))
        {
            lvl++;
            buildUp -= this.complexity * lvl;
        }
        
        if(!this.isBad())
            lvl++;
        
        return lvl;
    }
    
    public Image getImage()
    {
       return getMiskTileSet("StatusAligments")[this.iconIndex];
    }
    
    public Image getDisabledImage()
    {
        return getMiskTileSet("StatusAligments")[this.iconIndexDisabled];
    }
    
    public Image getImage(double buildUp)
    {
        if(buildUp <= 0)
            return null;
        
        int lvl = this.getLevel(buildUp);
        
        if(lvl > 0)
            return this.getImage();
            
        return this.getDisabledImage();
    }
}
