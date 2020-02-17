/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.panels.shop;

import game.util.drawable.Drawable;
import game.util.drawable.SizedDrawable;

/**
 *
 * @autor gbeljajew
 */
public interface ShopItem
{
    /**
     * items and skills are drawable so mostly it will be <code>return this;</code>
     * @return 
     */
    public SizedDrawable getIcon();
    
    /**
     * name of shop item
     * @return 
     */
    public String getName();
    
    /**
     * description of shop item
     * @return 
     */
    public String getDescription();
    
    /**
     * prise for item. <br>
     * skills should look in hero and cost more for every level allready posessed.
     * @return 
     */
    public int detPrice();
    
    /**
     * key for factory
     * @return 
     */
    public String getKey();
    
}
