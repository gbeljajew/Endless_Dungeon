/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels;

import bin.game.Game;
import bin.game.GameConstants;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author gbeljajew
 */
public class ScreenControl 
{
    private static MainFrame mainFrame;
    
    public static void init()
    {
        mainFrame = new MainFrame();
        
        Timer timer = new Timer(true);
        
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                Game.update();
                mainFrame.repaint();
            }
        }, 10, GameConstants.TIMER_PERIOD);
    }
    
    public static void switchScreen(ScreenEnum screen)
    {
        mainFrame.switchScreen(screen);
    }

}


