/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.util.drawable;

import bin.game.GameConstants;

/**
 *
 * @author gbeljajew
 */
public class CameraField 
{
    private final int minX, minY, maxX, maxY;

    public CameraField(int minX, int minY, int maxX, int maxY, int screenWidth, int screenHeight)
    {
        maxX -= screenWidth;
        maxY -= screenHeight;
        
        if(minX > maxX)
        {
            minX = maxX = (minX + maxX) / 2;
        }
        
        if(minY > maxY)
        {
            minY = maxY = (minY + maxY) / 2;
        }
        
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        
        System.out.println(this);
    }

    public int getMinX()
    {
        return minX;
    }

    public int getMinY()
    {
        return minY;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public int getMaxY()
    {
        return maxY;
    }

    @Override
    public String toString()
    {
        return "[(" + minX + "," + minY + ")(" + maxX + "," + maxY + ")]";
    }
    
    
}
