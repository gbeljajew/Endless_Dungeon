/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.dungeon;

import game.Game;
import game.GameConstants;
import game.GameResources;
import game.hero.HeroFigure;
import game.util.container.Coordinates2D;
import game.util.container.CoordinatesSet;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gbeljajew
 */
public class Dungeon
{

    public void drawCurrentRoom(Graphics2D grphcs, int x, int y)
    {
        this.floor.getCurrentRoom().draw(grphcs, x, y);
    }

    public void update()
    {
        this.floor.getCurrentRoom().update();
    }

    // ---------------------------------------------------------
    private Floor floor;
    private int currentFloor = 1;

    

    public Dungeon(int floor)
    {
        this.floor = new Floor(currentFloor, this);
        this.currentFloor = floor;
    }

    void nextFloor()
    {
        currentFloor++;
        this.floor = new Floor(this.currentFloor, this);
    }

    public int getLevel()
    {
        return this.currentFloor;
    }

    public FloorType getFloorType()
    {
        return this.floor.getFloorType();
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

        rooms = RoomNode.createFloorMap(GameConstants.MIN_ROOMS_ON_FLOOR_1
                + currentFloor / GameConstants.MIN_ROOMS_GROW_EVERY_NUMBER_FLOORS,
                GameConstants.MAX_ROOMS_ON_FLOOR_1
                + currentFloor / GameConstants.MAX_ROOMS_GROW_EVERY_NUMBER_FLOORS);

        this.floorType = GameResources.getRandomFloorType();

        this.currentRoom = Room.createRoom(floorType, rooms.get(0).getExitDirection(), this);

    }

    Room getCurrentRoom()
    {
        return this.currentRoom;
    }

    void nextRoom()
    {
        // hero position in next room.
        HeroFigure figure = Game.getHero().getFigure();
        int x = figure.getX();
        int y = figure.getY();

        Direction direction = rooms.get(roomNumber).getExitDirection();

        switch (direction)
        {
            case NORTH:
                figure.setStartPosition(x, 10);
                break;
            case EAST:
                figure.setStartPosition(1, y);
                break;
            case SOUTH:
                figure.setStartPosition(x, 1);
                break;
            case WEST:
                figure.setStartPosition(10, y);
                break;
            default:
                throw new AssertionError(direction.name());
        }

        this.roomNumber++;
        this.currentRoom = Room.createRoom(floorType, rooms.get(roomNumber).getExitDirection(), this);
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
