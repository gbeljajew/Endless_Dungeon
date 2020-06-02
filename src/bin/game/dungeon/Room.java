
package bin.game.dungeon;

import bin.game.Updatable;
import bin.game.util.drawable.CameraField;
import bin.game.util.drawable.Drawable;

/**
 *
 * @author gbeljajew
 */
public interface Room extends Drawable, Updatable
{

    
    /**
     * 
     * @param x
     * @param y
     * @return true if it is possible to pass trough a tile with coordinates(x,y)
     */
    public boolean isPassable(int x, int y);

    public CameraField getCameraField();

    public void touch(int x, int y);
}
