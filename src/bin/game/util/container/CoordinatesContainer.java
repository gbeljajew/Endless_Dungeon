/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.container;

import java.util.Iterator;

/**
 * container for items, that have x and y coordinates.
 * @author gbeljajew
 * @param <T>
 */
public interface CoordinatesContainer<T extends Coordinates2D> extends Iterable<T>
{

    /**
     * put an item, which has coordinates into this container.
     * 
     * @param item
     * @return true if item was added.<br>
     *         false if there is already an item on those coordinates.
     */
    boolean add(T item);

    /**
     * remove item from container
     * @param item
     * @return true if item was there.
     */
    boolean remove(T item);
    
    /**
     * remove item on coordinates x,y.
     * @param x
     * @param y
     * @return true if it was present.
     */
    boolean remove(final int x, final int y);
    
    /**
     * empty container.
     */
    void clear();

    /**
     * get item at this coordinates.
     * @param x
     * @param y
     * @return 
     */
    T get(final int x, final int y);
    
    /**
     * get item on the same position as in parameter.
     * @param c2d
     * @return 
     */
    default T get(final Coordinates2D c2d)
    {
        return get(c2d.getX(), c2d.getY());
    }

    @Override
    Iterator<T> iterator();

    /**
     * 
     * @return number of items in container.
     */
    int size();
    
    int getMinX();
    
    int getMaxX();
    
    int getMinY();
    
    int getMaxY();
}
