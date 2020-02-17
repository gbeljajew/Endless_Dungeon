/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

import game.GameConstants;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @autor gbeljajew
 */
public class AnimatedSizeDrawable implements SizedDrawable
{
    private final Image[] images;
    private int count, step, width, heigth;
    private int delay;

    public AnimatedSizeDrawable(Image[] images, int delay)
    {
        if(images == null)
            throw new NullPointerException();
        
        if(images.length == 0)
            throw new ArrayIndexOutOfBoundsException();
        
        this.images = images;
        this.delay = delay;
        
        this.width = images[0].getWidth(null);
        this.heigth = images[0].getHeight(null);
        
        
    }

    public AnimatedSizeDrawable(Image[] images)
    {
        this(images, GameConstants.STANDARD_ANIMATION_DELAY);
    }
    
    
    
    
    
    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.heigth;
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        count++;
        
        if(count % this.delay == 0)
            step = (step + 1) % this.images.length;
        
        int dx = this.images[step].getWidth(null) / 2;
        int dy = this.images[step].getHeight(null) / 2;
        
        g.drawImage(this.images[step], kameraOffsetX - dx, kameraOffsetY - dy, null);
    }
    
}
