/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util;

/**
 * 
 * @author gbeljajew
 */
public interface OneWaySwitchable
{
    /**
     * switch state of this object to other position.
     * there is no way to return to previous state 
     * and second and after use of this method should do nothing.
     */
    void doSwitch();
    
}
