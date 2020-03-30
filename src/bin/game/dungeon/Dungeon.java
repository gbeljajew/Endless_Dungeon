/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.dungeon;

import bin.game.GameConstants;
import static bin.game.GameConstants.TILE_HEIGHT;
import static bin.game.GameConstants.TILE_WIDTH;
import bin.game.resources.GameResources;
import bin.game.util.container.Coordinates2D;
import bin.game.util.container.CoordinatesContainer;
import bin.game.util.container.CoordinatesSet;
import bin.game.util.drawable.Animation;
import bin.game.util.drawable.CameraField;
import bin.game.util.drawable.Drawable;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author gbeljajew
 */
public class Dungeon implements Room
{
    private int level = 1;
    private FloorType floorType = GameResources.getRandomFloorType();
    
    private DungeonRoom room;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Dungeon(int level)
    {
        this.level = level - 1;
        
        this.nextFloor();
    }

    

    @Override
    public boolean isPassable(int x, int y)
    {
        return this.room.isPassable(x, y);
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        this.room.draw(g, kameraOffsetX, kameraOffsetY);
    }
    
    void nextFloor()
    {
        this.level++;
        this.floorType = GameResources.getRandomFloorType();
        
        // TODO new floorMap and new room.
    }

    @Override
    public void update()
    {
        this.room.update();
    }

    public int getCurrentFloor()
    {
        return this.level;
    }

    @Override
    public CameraField getCameraField()
    {
        return this.room.getCameraField();
    }
    
}
// TODO implement methods
class DungeonRoom implements Room
{
    CoordinatesContainer<Tile> map = new CoordinatesSet<>();
    private CameraField cameraField;
    @Override
    public boolean isPassable(int x, int y)
    {
        Tile t = this.map.get(x, y);
        
        if(t == null)
            return false;
        
        return t.isPassable();
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        for (Tile tile : map)
        {
            tile.draw(g, kameraOffsetX, kameraOffsetY);
        }
    }

    @Override
    public void update()
    {
        // TODO implement.
    }
    
    @Override
    public CameraField getCameraField()
    {
        if(this.cameraField == null)
        {
            int minX = this.map.getMinX() * GameConstants.TILE_WIDTH;
            int maxX = (this.map.getMaxX() + 1) * GameConstants.TILE_WIDTH;
            int minY = this.map.getMinY() * GameConstants.TILE_HEIGHT;
            int maxY = (this.map.getMaxY() + 1) + GameConstants.TILE_HEIGHT;
            
            this.cameraField = new CameraField(minX, minY, maxX, maxY, 
                    GameConstants.MAP_SCREEN_WIDTH, 
                    GameConstants.MAP_SCREEN_HEIGHT);
        }
        
        return this.cameraField;
    }
    
}

interface Tile extends Drawable, Coordinates2D
{
    public boolean isPassable();
}

class DefTile implements Tile
{
    protected Image tile;
    protected Image overlay;
    protected boolean passabe;
    
    protected int x, y;

    public DefTile(Image tile, Image overlay, boolean passabe, int x, int y)
    {
        if(tile == null)
            throw new IllegalArgumentException();
        
        this.tile = tile;
        this.overlay = overlay;
        this.passabe = passabe;
        this.x = x;
        this.y = y;
    }
    
    

    @Override
    public boolean isPassable()
    {
        return passabe;
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        int xPos = TILE_WIDTH * x - kameraOffsetX;
        int yPos = TILE_HEIGHT * y - kameraOffsetY;
        
        g.drawImage(tile, xPos, yPos, null);
        
        if(this.overlay != null)
            g.drawImage(this.overlay, xPos, yPos, null);
        
        g.drawLine(xPos, yPos, xPos + TILE_WIDTH, yPos);
        g.drawLine(xPos, yPos, xPos, yPos + TILE_HEIGHT);
        g.drawLine(xPos, yPos + TILE_HEIGHT, xPos + TILE_WIDTH, yPos + TILE_HEIGHT);
        g.drawLine(xPos + TILE_WIDTH, yPos, xPos + TILE_WIDTH, yPos + TILE_HEIGHT);
        
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }
    
}

class SwichableTile extends DefTile
{
    private final Image altTile;
    private final Image altOverlay;
    
    private final boolean passableAfterUnlock;

    public SwichableTile(Image altTile, Image altOverlay, boolean passableAfterUnlock, Image tile, Image overlay, boolean passabe, int x, int y)
    {
        super(tile, overlay, passabe, x, y);
        this.altTile = altTile;
        this.altOverlay = altOverlay;
        this.passableAfterUnlock = passableAfterUnlock;
    }
    
    public void switchState()
    {
        this.tile = altTile;
        this.overlay = altOverlay;
        this.passabe = passableAfterUnlock;
    }
}