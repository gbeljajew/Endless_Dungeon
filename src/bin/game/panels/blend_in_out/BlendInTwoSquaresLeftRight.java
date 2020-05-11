/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels.blend_in_out;

import bin.game.GameConstants;
import bin.game.util.drawable.Animation;
import java.awt.Graphics2D;

/**
 *
 * @author gbeljajew
 */
public class BlendInTwoSquaresLeftRight implements Animation
{
    private int xPos = 0;
    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        xPos += 10;
        
        g.fillRect(0, 0, GameConstants.SCREEN_WIDTH - xPos, 
                GameConstants.SCREEN_HEIGHT / 2);
        
        g.fillRect(xPos,  
                GameConstants.SCREEN_HEIGHT / 2, 
                GameConstants.SCREEN_WIDTH - xPos, 
                GameConstants.SCREEN_HEIGHT / 2);
    }

    @Override
    public boolean isDone()
    {
        return this.xPos >= GameConstants.SCREEN_WIDTH;
    }

}
