/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import java.awt.Image;

/**
 *
 * @author gbeljajew
 */
public interface PotionPouch
{
    /**
     * 
     * @return number of potions owned
     */
    public int getCount();
    
    public void drink();
    
    public Image getImage();
}
