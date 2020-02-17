/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @autor gbeljajew
 */
public class SimpleSizedDrawable implements SizedDrawable
{
    private final Image image;
    private final int width, height, offsetX, offsetY; 

    /**
     * creates simple implementation of SizedDrawable without offsets.
     * @param image 
     */
    public SimpleSizedDrawable(Image image)
    {
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.offsetX = 0;
        this.offsetY = 0;
    }

    /**
     * creates simple implementation of SizedDrawable with offsets.
     * @param image
     * @param offsetX
     * @param offsetY 
     */
    public SimpleSizedDrawable(Image image, int offsetX, int offsetY)
    {
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        g.drawImage(image, this.offsetX + kameraOffsetX, this.offsetY + kameraOffsetY, null);
    }
    
    
    
}
