/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.util.drawable;

import bin.game.Game;
import bin.game.GameConstants;

/**
 * contains and controls camera offsets. 
 * this data represents coordinates of top left pixel of underlying map, that will be drawn. 
 * @author gbeljajew
 */
public class Camera 
{
    private int cameraOffsetX;
    private int cameraOffsetY;

    
    public int getCameraOffsetX()
    {
        return cameraOffsetX;
    }

    public int getCameraOffsetY()
    {
        return cameraOffsetY;
    }

    /**
     * update position, so it is always over hero.
     * @param x
     * @param y 
     */
    public void updatePosition(int x, int y)
    {
        CameraField cameraField = Game.getCurrentRoom().getCameraField();
        
        
        
        this.cameraOffsetX = x - GameConstants.MAP_SCREEN_WIDTH / 2;
        this.cameraOffsetY = y - GameConstants.MAP_SCREEN_HEIGHT / 2;
        
        if(this.cameraOffsetX < cameraField.getMinX())
            this.cameraOffsetX = cameraField.getMinX();
        
        if(this.cameraOffsetX > cameraField.getMaxX())
            this.cameraOffsetX = cameraField.getMaxX();
        
        if(this.cameraOffsetY < cameraField.getMinY())
            this.cameraOffsetY = cameraField.getMinY();
        
        if(this.cameraOffsetY > cameraField.getMaxY())
            this.cameraOffsetY = cameraField.getMaxY();
        
    }
    
    
}
