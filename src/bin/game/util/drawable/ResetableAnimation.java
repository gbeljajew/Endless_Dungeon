/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.drawable;

/**
 *
 * @author geljajew
 */
public interface ResetableAnimation extends Animation, Cloneable
{
    /**
     * reset everything so it can be started again.
     */
    public void reset();
    
    /**
     * set position relative to some anker object.
     * 
     * @param x
     * @param y 
     */
    public void setPosition(int x, int y);
    
    public ResetableAnimation cloneYourself();
    
    
}
