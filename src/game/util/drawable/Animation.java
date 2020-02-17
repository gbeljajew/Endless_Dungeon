
package game.util.drawable;

import java.awt.Graphics2D;

/**
 * this is a overlay shown over map, when event happens.
 * animation only. action happens somewhere else.
 * 
 * @author gbeljajew
 */
public interface Animation
{
    /**
     * drawing and counting frames happens here.
     * @param g
     * @param x coordinates of center of animation
     * @param y 
     */
    public void draw(Graphics2D g, int x, int y);
    
    /**
     * 
     * @return if animation completely shown and can de disposed
     */
    public boolean isDone();
    
}
