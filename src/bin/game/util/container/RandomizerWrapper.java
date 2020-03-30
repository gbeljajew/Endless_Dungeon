package bin.game.util.container;

/**
 * a wrapper for stuff, that need some randonizing.
 * @autor gbeljajew
 * @param <T>
 */
public class RandomizerWrapper <T> implements Randomizable
{

    private final T item;
    private final double cahnce;

    public RandomizerWrapper(T item, double cahnce)
    {
        
        if(item == null)
            throw new NullPointerException("Item maybe not null");
        
        this.item = item;
        this.cahnce = cahnce;
    }
    
    
    @Override
    public double getChance()
    {
        return this.cahnce;
    }

    public T getItem()
    {
        return item;
    }
}
