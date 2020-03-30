/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * interface for wraper around NPC sprite matrix.<br>
 * classes, that implement this interface are just containers, that will give
 * you the right sprite based on actions you called on this container.<br>
 * expected, that there are sprites for 4 directions with a few step phases for
 * each dirrection.<br>
 * we do not expect any "special moves" expect walking.
 *
 * @author gbeljajew
 */
public interface NPC_Sprite
{

    /**
     *
     * @return current sprite. no changes.
     */
    Image getSprite();

    /**
     * draw hero with feet at position x,y. <br>this class is intended only to
     * hold images as a part of other class, which knows its position and can
     * calkulate where this sprite should be drawn.
     *
     * @param g2d
     * @param x
     * @param y
     */
    void draw(Graphics2D g2d, int x, int y);

    //----- Steps -------------------------------------------------
    /**
     * switch to next step phase and return image for it.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite step();

    /**
     * switch to standing step phase.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite step0();

    //----- Turning -------------------------------------------------
    /**
     * turn counter clock direction.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite turnLeft();

    /**
     * turn in clock direction.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite turnRight();

    /**
     * turn this NPC so it looks north.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite lookNorth();

    /**
     * turn this NPC so it looks west.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite lookWest();

    /**
     * turn this NPC so it looks east.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite lookEast();

    /**
     * turn this NPC so it looks south.
     *
     * @return this for chaining movements.
     */
    NPC_Sprite lookSouth();

}
