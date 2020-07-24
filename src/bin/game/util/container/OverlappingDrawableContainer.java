/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.util.container;

import bin.game.hero.Hero;
import bin.game.util.drawable.Drawable;
import bin.game.util.drawable.OverlappingDrawable;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * purpose for this container is to hold all drawable items, that might overlap 
 * and/or move and to draw those items in defined order so items, that are further 
 * are covered by items, that are near to player.
 * @author gbeljajew
 */
public class OverlappingDrawableContainer implements Drawable
{
    private final List<OverlappingDrawable> items = new ArrayList<>();
    
    private static final Comparator<OverlappingDrawable> COMPARATOR = new Comparator<OverlappingDrawable>()
    {
        @Override
        public int compare(OverlappingDrawable o1, OverlappingDrawable o2)
        {
            if(o1.getPicksY() < o2.getPicksY())
                return -1;
            
            if(o1.getPicksY() > o2.getPicksY())
                return 1;
            
            if(o1.getPicksX() < o2.getPicksX())
                return -1;
            
            if(o1.getPicksX() > o2.getPicksX())
                return 1;
            
            return 0;
        }
    };
    
    @Override
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY)
    {
        Collections.sort(items, COMPARATOR);
        
        for (OverlappingDrawable item : items)
        {
            item.draw(g, cameraOffsetX, cameraOffsetY);
        }
    }

    public void add(OverlappingDrawable item)
    {
        // TODO find a better way to make sure there is only 1 hero
        if(item instanceof Hero)
        {
            OverlappingDrawable t = null;
            
            for (OverlappingDrawable i : items)
            {
                if(i instanceof Hero)
                {
                    t = i;
                    break;
                }
            }
            
            if(t != null)
                this.remove(t);
        }
        
        if(!this.items.contains(item))
            this.items.add(item);
        
        
    }
    
    public void remove(OverlappingDrawable item)
    {
        this.items.remove(item);
    }
    
    
}
