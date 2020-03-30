
package bin.game.util;

/**
 *
 * @autor gbeljajew
 */
public class KeyInt
{

    public final String key;
    public final int value;

    public KeyInt(String key, int level)
    {
        this.key = key;
        this.value = level;
    }

    @Override
    public String toString()
    {
        return "KeyInt{" + "key=" + key + ", level=" + value + '}';
    }
}
