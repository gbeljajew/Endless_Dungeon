/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Animation which will go from beginn of an array to its end and then back to
 * beginn and that "repeats" times.
 *
 * @author gbeljajew
 */
public class RepeatableFortAndBackAnimation implements ResetableAnimation
{

    private final Image[] images;
    private final int delay;

    private int repeats;

    private int count = 0, direction = 1, step = 0, currentRepeat = 0;

    private int x, y;

    public RepeatableFortAndBackAnimation(Image[] images, int delay, int repeats)
    {
        this.images = images;
        this.delay = delay;
        this.repeats = repeats;
    }

    @Override
    public void reset()
    {
        this.count = 0;
        this.direction = 1;
        this.step = 0;
        currentRepeat = 0;
    }

    @Override
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public ResetableAnimation cloneYourself()
    {
        return new RepeatableFortAndBackAnimation(images, delay, repeats);
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        this.count++;

        if (this.count % this.delay == 0)
        {
            this.step += this.direction;

            if (this.step == this.images.length - 1)
            {
                this.direction = -1;
            }

            if (this.step == 0)
            {
                this.direction = 1;
                this.currentRepeat++;
            }
        }

        Image image = this.images[step];

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        g.drawImage(image, x + this.x - width / 2, y + this.y - height / 2, null);
    }

    @Override
    public boolean isDone()
    {
        return this.currentRepeat >= this.repeats;
    }

    public void setNumberOfRepeats(int repeats)
    {
        this.repeats = repeats;
    }

}
