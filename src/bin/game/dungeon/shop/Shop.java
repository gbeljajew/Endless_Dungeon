/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.dungeon.shop;

import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public interface Shop
{
    void done();
    
    JPanel getShopPanel();
    
    String getName();
    
    void refresh();
}
