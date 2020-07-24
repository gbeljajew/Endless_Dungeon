
package bin.game.util.container;

/**
 * Container, which will get you random stuff.
 * 
 * chance is how high is chance to get item. it is not a percent, it is relative.
 * With chance = 10 will be get about 10 times more often, than with chance = 1
 * 
 * @author gbeljajew
 * @param <T>
 */
public interface RandomContainer <T extends Randomizable> extends Iterable<T>
{
    /**
     * add one item.
     * @param item 
     * @return true if addition was successful.
     * @throws NullPointerException if item == null
     */
    public boolean add(T item) throws NullPointerException;
    
    
    /**
     * add items from a Collection
     * @param list 
     */
    default public void add(Iterable<T> list)
    {
        for (T item : list)
        {
            this.add(item);
        }
    }
    
    /**
     * add some items.
     * @param list 
     */
    default public void put(T... list)
    {
        for (T item : list)
        {
            this.add(item);
        }
    }
    
    /**
     * get one of contained items random.
     * item will stay in container.
     * @return random T object. may return null if there are no items to chose from.
     * @throws ArrayIndexOutOfBoundsException if you try to get an item when container is empty
     */
    public T get() throws ArrayIndexOutOfBoundsException;
    
    /**
     * get a set of items (unique in this array).
     * @param size of returned array.
     * @return 
     * @throws ArrayIndexOutOfBoundsException if size < 0 or size > this.size
     */
    public T[] getSet(int size) throws ArrayIndexOutOfBoundsException;
    
    /**
     * get a list of items (not unique)
     * @param size
     * @return
     * @throws ArrayIndexOutOfBoundsException if size < 0 
     */
    public T[] getList(int size)  throws ArrayIndexOutOfBoundsException;
    
    /**
     * remove one ramdom item and return it.
     * @return random item or null if container is empty;
     * 
     */
    public T pullOut();
    
    public int size();
}
