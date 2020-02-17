/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.dungeon.cristall_content;

import game.items.Item;
import java.awt.Graphics2D;
import game.util.drawable.Animation;

// TODO needs implementation

/**
 *
 * @autor gbeljajew
 */
public class TreasureEvent implements Animation
{

    private final Item item;

    public TreasureEvent(Item item)
    {
        this.item = item;
    }

    @Override
    public void draw(Graphics2D g, int x, int y)
    {
        // TODO animation
    }

    @Override
    public boolean isDone()
    {
    	// TODO 
        return true;
    }

}
