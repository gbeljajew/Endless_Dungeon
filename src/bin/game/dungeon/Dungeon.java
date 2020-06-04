/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.dungeon;

import bin.game.Game;
import bin.game.GameConstants;
import static bin.game.GameConstants.TILE_HEIGHT;
import static bin.game.GameConstants.TILE_WIDTH;
import bin.game.dungeon.cristall.Cristall;
import bin.game.dungeon.cristall.CristallFactory;
import bin.game.hero.Hero;
import bin.game.hero.HeroFigure;
import bin.game.resources.GameResources;
import bin.game.util.Direction;
import bin.game.util.Utils;
import bin.game.util.container.Coordinates2D;
import bin.game.util.container.CoordinatesContainer;
import bin.game.util.container.CoordinatesSet;
import bin.game.util.drawable.Animation;
import bin.game.util.drawable.CameraField;
import bin.game.util.drawable.Drawable;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import bin.game.dungeon.cristall.Touchable;
import bin.game.util.OneWaySwitchable;

/**
 *
 * @author gbeljajew
 */
public class Dungeon implements Room
{
    private int level = 1;
    private FloorType floorType = GameResources.getRandomFloorType();
    
    private Floor floor;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Dungeon(int level)
    {
        this.level = level - 1;
        
        this.nextFloor();
    }

    

    @Override
    public boolean isPassable(int x, int y)
    {
        return this.floor.getCurrentRoom().isPassable(x, y);
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        this.floor.getCurrentRoom().draw(g, kameraOffsetX, kameraOffsetY);
    }
    
    void nextFloor()
    {
        this.level++;
        this.floorType = GameResources.getRandomFloorType();
        this.floor = new Floor(level, this);
        
        // TODO new floorMap and new room.
    }

    @Override
    public void update()
    {
        this.floor.getCurrentRoom().update();
    }

    public int getCurrentFloor()
    {
        return this.level;
    }

    @Override
    public CameraField getCameraField()
    {
        return this.floor.getCurrentRoom().getCameraField();
    }

    @Override
    public void touch(int x, int y)
    {
        this.floor.getCurrentRoom().touch(x, y);
    }
    
}
// TODO implement methods
class DungeonRoom implements Room
{

    static Room createRoom(FloorType floorType, Direction exitDirection, Direction entryDirection,Floor floor)
    {
        return new DungeonRoom(floorType, exitDirection, entryDirection, floor);
    }
    
    private final CoordinatesContainer<Tile> map = new CoordinatesSet<>();
    private final CoordinatesContainer<Cristall> cristalls = new CoordinatesSet<>();
    
    private CameraField cameraField;
    private final FloorType floorType;
    private final Floor floor;
    private final int width;
    private final int height;
    
    private SwichableTile exit;
    private Animation animation;
    private Direction exitDirection;
    
    public DungeonRoom(FloorType floorType, Direction exitDirection, Direction entryDirection, Floor floor)
    {
        this.floorType = floorType;
        this.floor = floor;
        this.exitDirection = exitDirection;
        
        this.width = Utils.generateRandomInt(GameConstants.MIN_ROOM_SIZE, GameConstants.MAX_ROOM_SIZE);
        this.height = Utils.generateRandomInt(GameConstants.MIN_ROOM_SIZE, GameConstants.MAX_ROOM_SIZE);
        
        this.calculateNewHeroPositionAndPlaceHero(entryDirection);
        
        this.generateMap(exitDirection);
        
        // entry animation
        this.animation = RoomSwitchAnimationFactory.getEntryAnimation(entryDirection);
    }
    
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
        //System.out.println("draw " + kameraOffsetX + " " + kameraOffsetY);
        
        for (Tile tile : map)
        {
            tile.draw(g, kameraOffsetX, kameraOffsetY);
        }
        
        // TODO make sure the hero is under cristalls that are more south to him
        // and over cristalls, thet are north to him.
        Game.getHero().draw(g, kameraOffsetX, kameraOffsetY);
        
        for (Cristall cristall : cristalls)
        {
            cristall.draw(g, kameraOffsetX, kameraOffsetY);
        }
        
        if(this.animation != null)
            this.animation.draw(g, 0, 0);
    }

    @Override
    public void update()
    {
        // TODO implement.
        
        if(this.animation != null)
        {
            if(animation.isDone())
                this.animation = null;
            else
                return;
        }
        
        
        Game.getHero().update();
    }
    
    @Override
    public CameraField getCameraField()
    {
        if(this.cameraField == null)
        {
            int minX = this.map.getMinX() * GameConstants.TILE_WIDTH;
            int maxX = (this.map.getMaxX() + 1) * GameConstants.TILE_WIDTH;
            int minY = this.map.getMinY() * GameConstants.TILE_HEIGHT;
            int maxY = (this.map.getMaxY() + 1) * GameConstants.TILE_HEIGHT;
            
            this.cameraField = new CameraField(minX, minY, maxX, maxY, 
                    GameConstants.MAP_SCREEN_WIDTH, 
                    GameConstants.MAP_SCREEN_HEIGHT);
        }
        
        //System.out.println(cameraField);
        
        return this.cameraField;
    }

    @Override
    public void touch(int x, int y)
    {
        if(this.exit.wasSwiched() && this.exit.getX() == x && y == this.exit.getY())
        {
            
            // TODO exit animation
            this.animation = RoomSwitchAnimationFactory.getExitAnimation(this.exitDirection, floor);
            return;
        }
        
        
        Touchable cristall = this.cristalls.get(x, y);
        
        if(cristall != null)
            this.animation = cristall.touch();
    }

    

    private void generateMap(Direction exitDirection)
    {
        // TODO implement
        
        int heroX = Game.getHero().getMapX();
        int heroY = Game.getHero().getMapY();
        int exitY;
        int exitX;
        
        // calculate exit tile
        
        switch(exitDirection)
        {
            case NORTH:
                exitY = 0;
                exitX = Utils.generateRandomInt(1, this.width - 2);
                break;
            case EAST:
                exitX = this.width - 1;
                exitY = Utils.generateRandomInt(1, this.height - 2);
                break;
            case SOUTH:
                exitY = this.height - 1;
                exitX = Utils.generateRandomInt(1, this.width - 2);
                break;
            case WEST:
                exitX = 0;
                exitY = Utils.generateRandomInt(1, this.height - 2);
                break;
            case NONE:
                exitX = Utils.generateRandomInt(1, this.width - 2);
                exitY = Utils.generateRandomInt(1, this.height - 2);
                break;
            default:
                throw new AssertionError(exitDirection.name());
        }
        
        int keyX, keyY;
        
        do
        {
            keyX = Utils.generateRandomInt(1, this.width - 2);
            keyY = Utils.generateRandomInt(1, this.height - 2);
        }
        // this is to prevent, that player stumbles into key cristall and the next step falls into exit.
        // with this player needs at least one turn to find key and after that at least one turn to reach exit.
        while(keyX == heroX || keyX == exitX || keyY == heroY || keyY == exitY);
        
        Image normal, alternate;
        boolean passable = false;
        
        if(exitDirection == Direction.NONE)
        {
            normal = this.floorType.getFloorTileSprite();
            alternate = this.floorType.getStairsTileSprite();
            passable = true;
        }
        else
        {
            normal = this.floorType.getWallTileSprite();
            alternate = this.floorType.getFloorTileSprite();
        }
        
        //System.out.println("(" + exitX + ":" + exitY + ")");
        
        this.exit = new SwichableTile(normal, passable, alternate, true, exitX, exitY);
        
        this.map.add(exit);
        
        
        
        
        for (int mx = 0; mx < this.width; mx++)
        {
            for (int my = 0; my < this.height; my++)
            {
                Tile t;
                
                if(mx == 0 || my == 0 || mx == this.width - 1 || my == this.height - 1)
                {
                    t = new DefTile(this.floorType.getWallTileSprite(), null, false, mx, my);
                }
                else
                {
                    t = new DefTile(this.floorType.getFloorTileSprite(), this.floorType.getClutterOverlaySprite(), true, mx, my);
                }
                
                this.map.add(t);
            }
        }

        // TODO cristalls
        Cristall keyCristall = CristallFactory.getKeyCristall(exit, keyX, keyY);
        
        this.cristalls.add(keyCristall);

    }

    private void calculateNewHeroPositionAndPlaceHero(Direction entryDirection)
    {
        int x = Game.getHero().getMapX();
        int y = Game.getHero().getMapY();
        
        switch(entryDirection)
        {
            case NORTH:
                y = this.height - 2;
                if(x > this.width - 2 || x < 1)
                    x = Utils.generateRandomInt(1, this.width - 2);
                break;
            case EAST:
                x = 1;
                if(y > this.height - 2 || y < 1)
                    y = Utils.generateRandomInt(1, this.height - 2);
                break;
            case SOUTH:
                y = 1;
                if(x > this.width - 2 || x < 1)
                    x = Utils.generateRandomInt(1, this.width - 2);
                break;
            case WEST:
                x = this.width - 2;
                if(y > this.height - 2 || y < 1)
                    y = Utils.generateRandomInt(1, this.height - 2);
                break;
            case NONE:
                if(x > this.width - 2 || x < 1)
                    x = Utils.generateRandomInt(1, this.width - 2);
                if(y > this.height - 2 || y < 1)
                    y = Utils.generateRandomInt(1, this.height - 2);
                break;
            default:
                throw new AssertionError(entryDirection.name());
            
        }
        
        Game.getHero().setPosition(x, y);
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
    protected boolean passable;
    
    protected int x, y;

    public DefTile(Image tile, Image overlay, boolean passable, int x, int y)
    {
        if(tile == null)
            throw new IllegalArgumentException();
        
        this.tile = tile;
        this.overlay = overlay;
        this.passable = passable;
        this.x = x;
        this.y = y;
    }
    
    

    @Override
    public boolean isPassable()
    {
        return passable;
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

class SwichableTile extends DefTile implements OneWaySwitchable
{
    private final Image altTile;
    
    private final boolean passableAfterUnlock;
    private boolean wasSwiched = false;

    public SwichableTile(Image tile, boolean passable, Image altTile, boolean passableAfterUnlock, int x, int y)
    {
        super(tile, null, passable, x, y);
        this.altTile = altTile;
        this.passableAfterUnlock = passableAfterUnlock;
    }
    
    @Override
    public void doSwitch()
    {
        this.tile = altTile;
        this.passable = passableAfterUnlock;
        this.wasSwiched = true;
    }
    
    boolean wasSwiched()
    {
        return this.wasSwiched;
    }
}

class Floor
{

    private final List<RoomNode> rooms;
    private Room currentRoom;
    private int roomNumber = 0;

    private final FloorType floorType;

    public FloorType getFloorType()
    {
        return floorType;
    }

    private final Dungeon dungeon;

    Floor(int currentFloor, Dungeon dungeon)
    {
        this.dungeon = dungeon;

        rooms = RoomNode.createFloorMap(GameConstants.getMinRoomsOnFloor(),
                GameConstants.getMaxRoomsOnFloor());

        this.floorType = GameResources.getRandomFloorType();

        this.currentRoom = DungeonRoom.createRoom(floorType, rooms.get(0).getExitDirection(), Direction.NONE, this);

    }

    Room getCurrentRoom()
    {
        return this.currentRoom;
    }

    void nextRoom()
    {
        // hero position in next room.
        Hero h = Game.getHero();
        int x = h.getMapX();
        int y = h.getMapY();

        Direction direction = rooms.get(roomNumber).getExitDirection();

        this.roomNumber++;
        
        if(this.roomNumber >= rooms.size())
        {
            this.dungeon.nextFloor();
            return;
        }
        
        this.currentRoom = DungeonRoom.createRoom(floorType, rooms.get(roomNumber).getExitDirection(), direction, this);
    }

    void nextFloor()
    {
        this.dungeon.nextFloor();
    }
}

/**
 * needed to create a believable chain of rooms
 */
class RoomNode implements Coordinates2D
{

    private final int roomPositionX;
    private final int roomPositionY;
    private Direction exit = Direction.NONE;

    private RoomNode(int positionX, int positionY)
    {
        this.roomPositionX = positionX;
        this.roomPositionY = positionY;
    }

    /**
     * creates a "map" of rooms. there is no sidetracking and no backtracking.
     * all rooms can be accessed in a sequence, one after another. this method
     * creates a 1 dimensional list of rooms with exit directions so this list
     * can be mapped on 2d map without overlapping.
     *
     * @param min minimal number of rooms
     * @param max maximal number of rooms
     */
    static List<RoomNode> createFloorMap(int min, int max)
    {

        if (min < 1 || max < min)
        {
            throw new IllegalArgumentException("wrong value of min or max. min=" + min + " max=" + max);
        }

        int number = min + (int) ((max - min + 1) * Math.random());

        List<RoomNode> result = new ArrayList<>();
        CoordinatesSet<RoomNode> rooms = new CoordinatesSet<>();

        do
        {
            result.clear();
            rooms.clear();

            RoomNode current = new RoomNode(0, 0);
            result.add(current);
            rooms.add(current);

            do
            {
                List<Direction> directions = current.findFreeAdjustedCell(rooms);
                
                if (directions.isEmpty())
                {
                    break;
                }

                Direction dirNew = directions.get((int) (Math.random() * directions.size()));
                current.exit = dirNew;

                current = new RoomNode(current.roomPositionX + dirNew.dx,
                        current.roomPositionY + dirNew.dy);
                
                result.add(current);
                rooms.add(current);
            } while (result.size() < number);

        } while (result.size() < min);
        // there is chance, that we run into a dead end. 
        // this one is to make sure we have at least a minimum of rooms.

        //result.get(result.size() - 1).exit = Direction.NONE;

        return result;

    }

    

    Direction getExitDirection()
    {
        return exit;
    }

    @Override
    public int getX()
    {
        return this.roomPositionX;
    }

    @Override
    public int getY()
    {
        return roomPositionY;
    }

    private List<Direction> findFreeAdjustedCell(CoordinatesSet<RoomNode> rooms)
    {
        Direction[] dir = new Direction[]
        {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
        };

        ArrayList<Direction> directions = new ArrayList<>();

        directions.addAll(Arrays.asList(dir));

        // WEST
        if (rooms.get(roomPositionX - 1, roomPositionY) != null)
        {
            directions.remove(Direction.WEST);
        }

        // EAST
        if (rooms.get(roomPositionX + 1, roomPositionY) != null)
        {
            directions.remove(Direction.EAST);
        }

        // NORD
        if (rooms.get(roomPositionX, roomPositionY - 1) != null)
        {
            directions.remove(Direction.NORTH);
        }

        // SOUTH
        if (rooms.get(roomPositionX, roomPositionY + 1) != null)
        {
            directions.remove(Direction.SOUTH);
        }

        return directions;
    }

}