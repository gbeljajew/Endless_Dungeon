/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

import java.awt.Graphics2D;

/**
 * it draws two animations on two positions. there can be an delay between start
 * of animations or it can be set so one is waiting for other.
 * animations and delays are preset. positions must be set before every start.
 * @author gbeljajew
 */
public class TwinAnimation implements ResetableAnimation
{

   

    
    public enum DelayType
    {
        /** starter animation will wait {delay} ticks and then start. reciever animation starts immediately */
        STARTER_WAIT, 
        /** starter animation will start after reciever animation has ended. delay is ignored */
        STARTER_AFTER,
        /** starter animation will start after reciever animation has ended and delay has passed */
        STARTER_WAIT_AFTER,
        /** reciever animation will wait {delay} ticks and then start starter animation starts immediately */
        RECIEVER_WAIT,
        /** reciever animation will start after starter animation has ended. delay is ignored */
        RECIEVER_AFTER,
        /** reciever animation will start after starter animation has ended and delay has passed */
        RECIEVER_WAIT_AFTER;
    }
    
    private final ResetableAnimation starterAnimation;
    private final ResetableAnimation recieverAnimation;
    private final int delay;
    private final DelayType delayType;
    
    private int count = 0, countAfter = 0;
    private boolean starterMayShow = false, recieverMayShow = false, countAfterMayStart = false;
    
    

    /**
     * 
     * @param starterAnimation
     * @param recieverAnimation
     * @param delay
     * @param delayType 
     */
    public TwinAnimation(ResetableAnimation starterAnimation, ResetableAnimation recieverAnimation, int delay, DelayType delayType)
    {
        this.starterAnimation = starterAnimation;
        this.recieverAnimation = recieverAnimation;
        this.delay = delay;
        this.delayType = delayType;
    }
    
    /**
     * 
     * equals to this(starterAnimation, recieverAnimation, 0, delayType);
     * @param starterAnimation
     * @param recieverAnimation
     * @param delayType 
     */
    public TwinAnimation(ResetableAnimation starterAnimation, ResetableAnimation recieverAnimation, DelayType delayType)
    {
        this.starterAnimation = starterAnimation;
        this.recieverAnimation = recieverAnimation;
        this.delayType = delayType;
        this.delay = 0;
    }

    /**
     * both animation will start immediately
     * @param starterAnimation
     * @param recieverAnimation 
     */
    public TwinAnimation(ResetableAnimation starterAnimation, ResetableAnimation recieverAnimation)
    {
        this.starterAnimation = starterAnimation;
        this.recieverAnimation = recieverAnimation;
        this.delay = 0;
        this.delayType = DelayType.RECIEVER_WAIT;
    }
    
     @Override
    public ResetableAnimation cloneYourself()
    {
        return new TwinAnimation(
                starterAnimation.cloneYourself(), 
                recieverAnimation.cloneYourself(), 
                delay, delayType);
    }
    
    public void setStarterPosition(int x, int y)
    {
        this.starterAnimation.setPosition(x, y);
    }
    
    public void setRecieverPosition(int x, int y)
    {
        this.recieverAnimation.setPosition(x, y);
    }
    
    private void checkWhichToShow()
    {
        
        switch(this.delayType)
        {
            case STARTER_WAIT:
                
                if(this.count >= this.delay)
                    this.starterMayShow = true;
                
                break;
                
            case STARTER_AFTER:
                
                if(this.recieverAnimation.isDone())
                    this.starterMayShow = true;
                
                break;
                
            case STARTER_WAIT_AFTER:
                
                if(this.recieverAnimation.isDone())
                    this.countAfterMayStart = true;
                
                if(this.recieverAnimation.isDone() && this.countAfter >= this.delay)
                    this.starterMayShow = true;
                
                break;
                
            case RECIEVER_WAIT:
                
                if(this.count >= this.delay)
                    this.recieverMayShow = true;
                
                break;
                
            case RECIEVER_AFTER:
                
                if(this.starterAnimation.isDone())
                    this.recieverMayShow = true;
                
                break;
            case RECIEVER_WAIT_AFTER:
                
                if(this.starterAnimation.isDone())
                    this.countAfterMayStart = true;
                
                if(this.starterAnimation.isDone() && this.countAfter >= this.delay)
                    this.recieverMayShow = true;
                
                break;
            default:
                throw new AssertionError(this.delayType.name());
            
        }
        
        if(this.starterAnimation.isDone())
            this.starterMayShow = false;
        
        if(this.recieverAnimation.isDone())
            this.recieverMayShow = false;
    }
    
    private void resetFlags()
    {
        switch(this.delayType)
        {
            case STARTER_WAIT:
            case STARTER_AFTER:
            case STARTER_WAIT_AFTER:
                this.starterMayShow = false;
                this.recieverMayShow = true;
                break;
            case RECIEVER_WAIT:
            case RECIEVER_AFTER:
            case RECIEVER_WAIT_AFTER:
                this.starterMayShow = true;
                this.recieverMayShow = false;
                break;
            default:
                throw new AssertionError(this.delayType.name());
            
        }
        
        
        
    }

    public ResetableAnimation getStarterAnimation()
    {
        return starterAnimation;
    }

    public ResetableAnimation getRecieverAnimation()
    {
        return recieverAnimation;
    }
    

    @Override
    public void reset()
    {
        this.count = 0;
        this.countAfter = 0;
        
        this.recieverAnimation.reset();
        this.starterAnimation.reset();
        
        this.countAfterMayStart = false;
        this.resetFlags();
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        this.count++;
        
        if(this.countAfterMayStart)
            this.countAfter ++;
        
        this.checkWhichToShow();
        
        
        if(this.starterMayShow)
            this.starterAnimation.draw(g, x, y);
        
        if(this.recieverMayShow)
            this.recieverAnimation.draw(g, x, y);
            
    }

    @Override
    public boolean isDone()
    {
        // we wait for both animations to be ready
        return this.starterAnimation.isDone() && this.recieverAnimation.isDone();
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    @Override
    public void setPosition(int x, int y)
    {
        
    }
}
