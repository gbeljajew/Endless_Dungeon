/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.drawable;

import java.awt.Graphics2D;

/**
 *
 * @author gbeljajew
 */
public interface Drawable
{

    /**
     * do your drawing yourself. <br>
     * you should know yourslf where you are and where you should draw yourself.
     *
     * @param g
     * @param cameraOffsetX x position on screen of something, which is used for
     * you to calculate your position on screen.
     * @param cameraOffsetY y position on screen of something, which is used for
     * you to calculate your position on screen.
     */
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY);

}
