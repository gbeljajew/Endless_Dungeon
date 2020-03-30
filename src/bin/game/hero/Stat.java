package bin.game.hero;

import java.awt.Image;

/**
 *
 * @author gbeljajew
 */
public class Stat
{
    private final StatsEnum nameKey;
    
    private int value;
    private int increment = 0;
    private int boost = 0;

    public Stat(StatsEnum nameKey, int value)
    {
        this.nameKey = nameKey;
        this.value = value;
    }
    
    public void pp()
    {
        this.increment++;
    }
    
    public void useIncrement()
    {
        this.value += this.increment;
        this.increment = 0;
    }

    public StatsEnum getNameKey()
    {
        return nameKey;
    }

    public int getRealValue()
    {
        return value;
    }

    public int getIncrement()
    {
        return increment;
    }
    
    public int getValue()
    {
        return this.value + this.boost;
    }

    public Image getIcon()
    {
        return this.nameKey.getIcon();
    }
    
    public void boost(int boost)
    {
        this.boost += boost;
    }
    
    public void timePassed()
    {
        if(this.boost > 0)
            boost --;
        else if(this.boost < 0)
            boost ++;
    }

    @Override
    public String toString()
    {
        return "Stat: " + nameKey + " : " + value;
    }
    
    
    
}
