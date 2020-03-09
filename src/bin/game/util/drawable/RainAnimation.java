/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.drawable;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @autor gbeljajew
 */
public class RainAnimation implements RepeatableAnimation
{
    private final Image[] images;
    private final List<RainDrop> drops = new ArrayList<>();
    
    private int x, y, count = 0, 
            /** number of drops planed */
            numDrops, 
            /** number of frame changes between drops */
            dropsDelay, 
            /** number of frame changes since the last drop was added */
            dropsWait;
    private int width, heigth;
    private RepeatingType repeater = RepeatingType.CICLING;
    private final int fallingSpeed, delay;

    public RainAnimation(Image[] images, int fallingSpeed, int dropDelay, int delay, RepeatingType repeater)
    {
        this.dropsDelay = dropDelay;
        this.images = images;
        this.fallingSpeed = fallingSpeed;
        this.delay = delay;
        this.repeater = repeater;
    }

    public RainAnimation(Image[] images, int fallingSpeed, int dropDelay, int delay)
    {
        this.dropsDelay = 2;
        this.images = images;
        this.fallingSpeed = fallingSpeed;
        this.delay = delay;
    }
    
    
    
    
    private void addDrop()
    {
        if(this.drops.size() >= this.numDrops)
            return;
        
        int minX = this.x - this.width / 2;
        
        int dropX = (int)(minX + Math.random() * this.width);
        int dropY = this.y - this.heigth / 2;
        
        this.drops.add(new RainDrop(dropX, dropY, this.fallingSpeed, this.repeater));
        
    }
    
    public void setDimension(int width, int heigth)
    {
        this.width = width;
        this.heigth = heigth;
    }
    
    @Override
    public void setRepeats(int repeats)
    {
        this.numDrops = repeats;
    }

    @Override
    public void setRepeater(RepeatingType repeatingType)
    {
        this.repeater = repeatingType;
    }

    @Override
    public void reset()
    {
        this.count = 0;
        this.drops.clear();
        this.dropsWait = 0;
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
        
        return new RainAnimation(images, fallingSpeed, dropsDelay, delay, repeater);
        
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        count ++;
        
        if(count % delay == 0)
            this.step();
        
        for (RainDrop drop : drops)
        {
            if(!drop.isDone())
                drop.draw(g, x, y);
        }
    }

    @Override
    public boolean isDone()
    {
        
        for (RainDrop drop : drops)
        {
            if(!drop.isDone())
            {
                return false;
            }
        }
        
        return this.drops.size() >= this.numDrops;
    }

    private int getBottom()
    {
        return this.y + this.heigth / 2;
    }

    private void step()
    {
        for (RainDrop drop : this.drops)
        {
            drop.nextStep();
        }
        
        this.dropsWait ++;
        
        if(this.dropsWait >= dropsDelay)
        {
            this.addDrop();
            this.dropsWait = 0;
        }
        
    }
    
    
    
    private class RainDrop implements Animation
    {

        private final int x;
        private int y;
        private int step = 0;
        private final int fallingSpeed;
        private final RepeatingType.Repeater repeater;

        public RainDrop(int x, int y, int fallingSpeed, RepeatableAnimation.RepeatingType rt)
        {
            this.x = x;
            this.y = y;
            this.fallingSpeed = fallingSpeed;
            this.repeater = rt.getRepeater(RainAnimation.this.images.length);
        }
        
        public void nextStep()
        {
            this.y += this.fallingSpeed;
            this.step = this.repeater.next();
        }
                

        @Override
        public void draw(Graphics2D g, int kamX, int kamY)
        {
            Image img = RainAnimation.this.images[this.step];
            int dx = img.getWidth(null) / 2 * -1;
            int dy = img.getHeight(null) / 2 * -1;
            
            g.drawImage(img, this.x + dx, this.y + dy, null);
        }

        @Override
        public boolean isDone()
        {
            return this.y > RainAnimation.this.getBottom();
        }
        
    }
}
