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
 * them. this one will repeat
 *
 * @author gbeljajew
 */
public class RepetableResetableAnimation implements ResetableAnimation
{

    private final int delay, repeatingTimes;
    private int counter = 0;
    private int step = 0;
    private int times = 0;
    private final Image[] images;

    private int posX, posY;
    
    public RepetableResetableAnimation(Image[] images, int speed, int repeatingTimes)
    {
        this.delay = speed;
        this.images = images;
        this.repeatingTimes = repeatingTimes;
    }

    @Override
    public void reset()
    {
        counter = 0;
        step = 0;
        times = 0;
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        counter++;
        step = counter / delay;

        if (step >= images.length)
        {
            counter = 0;
            step = 0;
            times ++;
        }

        Image image = this.images[step];

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        g.drawImage(image, x + this.posX - width / 2, y + this.posY - height / 2, null);

    }

    @Override
    public boolean isDone()
    {
        return this.times >= this.repeatingTimes;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone(); 
    }

    @Override
    public void setPosition(int x, int y)
    {
        this.posX = x;
        this.posY = y;
    }

    @Override
    public ResetableAnimation cloneYourself()
    {
        return new RepetableResetableAnimation(images, step, repeatingTimes);
    }
    
    
}