/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.container;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Container to hold unique (Based on Coordinates) items. every combination of
 * x and y can have only one item/tile... See it as a Map with two int keys. <br>
 * Make sure that coordinates are inmutable.
 *
 * @author gbeljajew
 * @param <T>
 */
public class CoordinatesSet<T extends Coordinates2D> implements CoordinateContainer<T>
{

    private final List<T> content = new ArrayList<>();

    private static final C2D_Comparator COMPARATOR = new C2D_Comparator();

    /**
     * put an item, which has coordinates into this container.
     * it will not replace item on the same coordinates.
     * @param item
     * @return true if item was added.<br>
     *         false if there is allready an item on those coordinates.
     */
    @Override
    public boolean add(T item)
    {
        T old = this.get(item.getX(), item.getY());

        if (old == null)
        {
            content.add(item);
            this.content.sort(COMPARATOR);
            return true;
        }

        return false;
    }

    @Override
    public T get(final int x, final int y)
    {
        int lower = 0, upper = this.content.size() - 1;

        T current;

        while (lower <= upper)
        {
            int position = lower + (upper - lower) / 2;
            current = this.content.get(position);
            if (current.getX() == x && current.getY() == y)
            {
                return current;
            } else if (COMPARATOR.compare(current, x, y) < 0)
            {
                lower = position + 1;
            } else
            {
                upper = position - 1;
            }

        }

        return null;
    }
    
    @Override
    public void clear()
    {
        this.content.clear();
    }
    
    /**
     * put an item into container and replace, if it exist.
     * @param item
     * @return true if there was somethingon those coordinates
     */
    public boolean addOrReplace(T item)
    {
        T oldItem = this.get(item.getX(), item.getY());
        boolean res = false;
        if(oldItem != null)
        {
            this.content.remove(oldItem);
            res = true;
        }
        
        this.content.add(item);
        this.content.sort(COMPARATOR);
        
        
        return res;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return this.content.iterator();
    }

    @Override
    public int size()
    {
        return this.content.size();
    }

    @Override
    public String toString()
    {

        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getCanonicalName());
        sb.append("{");

        for (T t : this.content)
        {
            sb.append(String.format("(%d,%d)", t.getX(), t.getY()));
        }

        sb.append("}");

        return sb.toString().replace(":}", "}");
    }

    @Override
    public boolean remove(T item)
    {
        return this.content.remove(item);
    }

    @Override
    public boolean remove(final int x, final int y)
    {
        T item = this.get(x, y);
        
        if(item == null)
            return false;
        
        return this.remove(item);
    }
    
    

}

class C2D_Comparator implements Comparator<Coordinates2D>
{

    @Override
    public int compare(Coordinates2D t, Coordinates2D t1)
    {
        return compare(t, t1.getX(), t1.getY());
    }

    public int compare(Coordinates2D c2d, int x, int y)
    {
        if (c2d.getX() < x)
        {
            return -1;
        } else if (c2d.getX() > x)
        {
            return 1;
        } else if (c2d.getY() < y)
        {
            return -1;
        } else if (c2d.getY() > y)
        {
            return 1;
        }

        return 0;
    }
}
