/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.drawable;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * minimalistic implementation. it will go trough an array of images and display
 * them.
 *
 * @author gbeljajew
 */
public class DefaultResetableAnimation implements ResetableAnimation
{

    private final int speed;
    private int counter = 0;
    private int step = 0;
    private final Image[] images;
    
    private int posX, posY;

    public DefaultResetableAnimation(Image[] images, int speed)
    {
        this.speed = speed;
        this.images = images;
    }

    @Override
    public ResetableAnimation cloneYourself()
    {
        return new DefaultResetableAnimation(images, speed);
    }
    
    

    @Override
    public void reset()
    {
        counter = 0;
        step = 0;
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        counter++;
        step = counter / speed;

        if (step >= images.length)
        {
            return;
        }

        Image image = this.images[step];

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        g.drawImage(image, x + this.posX - width / 2, y + this.posY - height / 2, null);

    }

    @Override
    public boolean isDone()
    {
        return this.step >= this.images.length;
    }

    
    
    @Override
    public void setPosition(int x, int y)
    {
        this.posX = x;
        this.posY = y;
    }
}