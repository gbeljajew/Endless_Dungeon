/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gbeljajew
 * @param <T>
 */
public class RandomSet <T extends Randomizable> implements RandomContainer<T>
{

    private static final int MAX_LOOP = 100_000; // safety against endless loop
    
    private final List<T> items = new ArrayList<>();
    
    

    @Override
    public T get() throws ArrayIndexOutOfBoundsException
    {
        return choseRandom();
    }

    @Override
    public T[] getSet(int size) throws ArrayIndexOutOfBoundsException
    {
        if(size < 0 || size > this.items.size())
            throw new ArrayIndexOutOfBoundsException("illegal size " + size);
        
        Object[] res = new Object[size];
        
        // all items
        if(size == this.items.size())
        {
            
            for(int i = 0; i < res.length; i++)
                res[i] = this.items.get(i);
            return (T[])res;
        }
        
        Set<T> resultItems = new HashSet<>();
        
        int count = 0;
        do
        {
            resultItems.add(this.choseRandom());
            count++;
            
            if(count > RandomSet.MAX_LOOP)
                throw new IllegalStateException("it seems, that we have an endless loop");
        }while(resultItems.size() < size);
        
        int i = 0;
        for(T e: resultItems)
        {
            res[i] = e;
            i++;
        }
        
        return (T[])res;
    }
    
    @Override
    public T[] getList(int size) throws ArrayIndexOutOfBoundsException
    {
        if(size < 0 || size > this.items.size())
            throw new ArrayIndexOutOfBoundsException("illegal size " + size);
        
        Object[] res = new Object[size];
        
        for(int i = 0; i < res.length; i++)
        {
            res[i] = this.choseRandom();
        }
        
        return (T[])res;
    }

    @Override
    public T pullOut() 
    {
        
        if(this.size() == 0)
            return null;
        
        T item = choseRandom();
        
        this.items.remove(item);
        
        return item;
    }

    
    
    @Override
    public int size()
    {
        return this.items.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        
        return new Iterator<T>()
        {
            int current = 0;
            @Override
            public boolean hasNext()
            {
                return this.current < RandomSet.this.items.size();
            }

            @Override
            public T next()
            {
                T item = RandomSet.this.items.get(this.current);
                this.current++;
                
                return item;
                
            }
        };
    }

    private T choseRandom()
    {
        
        double summ = 0;
        
        for (T item : this.items)
        {
            summ += item.getChance();
        }
        
        double diceThrow = Math.random() * summ;
        
        double check = 0;
        for (T item : this.items)
        {
            check += item.getChance();
            
            if(check >= diceThrow)
                return item;
        }
        
        // i do not expect to get there, but to be sure
        return this.items.get((int)(Math.random() * this.items.size()));
    }

    @Override
    public boolean add(T item) throws NullPointerException, IllegalArgumentException
    {
        if(item == null)
            throw new NullPointerException("Item maybe not null");
        
        if(item.getChance() <= 0)
            return false;
        
        return this.items.add(item);
    }

    

}




