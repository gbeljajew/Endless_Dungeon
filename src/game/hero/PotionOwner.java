/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.items.Potion;

/**
 *
 * @autor gbeljajew
 */
public interface PotionOwner
{
    public int getPotionAmount(Potion potion);
    public void usePotion(Potion potion);
    public void addPotion(Potion potion);
}
