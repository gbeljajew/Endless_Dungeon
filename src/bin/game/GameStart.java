/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game;

import bin.game.panels.ScreenControl;
import bin.game.resources.GameResources;
import bin.game.resources.GraphicResources;

/**
 *
 * @author gbeljajew
 */
public class GameStart 
{
    public static void main(String[] args)
    {
        GraphicResources.init();
        GameResources.init();
        
        ScreenControl.init();
    }
}
