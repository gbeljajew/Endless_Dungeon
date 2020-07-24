/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.dungeon.cristall;

import bin.game.GameConstants;
import static bin.game.GameConstants.TILE_HEIGHT;
import static bin.game.GameConstants.TILE_WIDTH;
import bin.game.resources.GraphicResources;
import bin.game.util.OneWaySwitchable;
import bin.game.util.drawable.Animation;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author gbeljajew
 */
public class CristallFactory
{

    public static Cristall getKeyCristall(OneWaySwitchable exitTile, int cristallX, int cristallY)
    {
        return new CristallDefImpl(
                () ->
                {
                    exitTile.doSwitch();

                    System.out.println("open exit");

                    // TODO return animation
                    return null;
                }, 
        cristallX, 
        cristallY);
    }
}

class CristallDefImpl implements Cristall
{

    private final static int CRISTALL_SHIFT_X = 16;
    private final static int CRISTALL_SHIFT_Y = 54;

    private final Image[] SPRITES = GraphicResources.getMiskTileSet("core.cristall");
    private final CristallContent content;
    private final int x, y;

    private boolean touched = false;
    private int step = 0;
    private int tick = 0;

    public CristallDefImpl(CristallContent content, int x, int y)
    {
        this.content = content;
        this.x = x;
        this.y = y;
    }

    @Override
    public Animation touch()
    {
        if (this.touched == true)
        {
            return null;
        }

        touched = true;

        return this.content.touch();
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY)
    {
        if(this.touched)
            return;
        
        tick = (tick + 1) % GameConstants.STANDARD_ANIMATION_DELAY;

        if (tick == 0)
        {
            step = (step + 1) % SPRITES.length;
        }
        
        int xPos = TILE_WIDTH * x - cameraOffsetX + TILE_WIDTH / 2 - CRISTALL_SHIFT_X;
        int yPos = TILE_HEIGHT * y - cameraOffsetY + TILE_HEIGHT / 2 - CRISTALL_SHIFT_Y;
        
        g.drawImage(this.SPRITES[step], xPos, yPos, null);

    }

    @Override
    public int getPicksX()
    {
        return TILE_WIDTH * x + TILE_WIDTH / 2;
    }

    @Override
    public int getPicksY()
    {
        return TILE_HEIGHT * y + TILE_HEIGHT / 2;
    }

}
