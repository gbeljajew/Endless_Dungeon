
package bin.game.util.logger;

/**
 *
 * @autor gbeljajew
 */
public interface LogListener
{
    /**
     * called every time a log is written
     * 
     * @param txt  text of log
     */
    public void talk(String txt);
}
