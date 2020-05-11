/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.dungeon.shop;

import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public interface Shop
{
    JPanel getShopPanel();
    
    String getName();
    
    Image getShopOwner();
    
    void refresh();
    
    void beforeShop();
    
    void afterShop();
}
