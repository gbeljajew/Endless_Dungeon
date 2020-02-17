/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.dungeon;


import game.Game;
import game.GameConstants;
import game.hero.HeroFigure;
import game.util.container.Coordinates2D;
import game.util.container.CoordinatesSet;
import game.util.drawable.Drawable;
import java.awt.Graphics2D;
import java.awt.Image;
import game.util.drawable.Animation;

/**
 *
 * @autor gbeljajew
 */
class Room implements Drawable
{
    private boolean keyFound = false;
    private final CoordinatesSet<Tile> tiles = new CoordinatesSet<>();
    private final CoordinatesSet<Cristall> cristalls = new CoordinatesSet<>();
    private final Floor floor;
    private final int exitX, exitY;
    private final Direction exitDirection;
    private Animation animatedEvent = null;

    Room(Floor floor, int exitX, int exitY, Direction exitDirection)
    {
        this.floor = floor;
        this.exitX = exitX;
        this.exitY = exitY;
        this.exitDirection = exitDirection;
    }
    

    
    
    

    static Room createRoom(FloorType floorType, Direction exitDirection, Floor floor)
    {
        
        System.out.println("exitDirection " + exitDirection);
        
        
        int exitX, exitY;
        Image baseImage;
        Image alternateImage;
        switch(exitDirection)
        {
            case NORTH:
                exitY = 0;
                exitX = (int)(Math.random() * 10 + 1);
                baseImage = floorType.getWallTile();
                alternateImage = floorType.getFloorTile();
                break;
            case EAST:
                exitY = (int)(Math.random() * 10 + 1);
                exitX = 11;
                baseImage = floorType.getWallTile();
                alternateImage = floorType.getFloorTile();
                break;
            case SOUTH:
                exitY = 11;
                exitX = (int)(Math.random() * 10 + 1);
                baseImage = floorType.getWallTile();
                alternateImage = floorType.getFloorTile();
                break;
            case WEST:
                exitY = (int)(Math.random() * 10 + 1);
                exitX = 0;
                baseImage = floorType.getWallTile();
                alternateImage = floorType.getFloorTile();
                break;
            case NONE:
                exitY = (int)(Math.random() * 10 + 1);
                exitX = (int)(Math.random() * 10 + 1);
                baseImage = floorType.getFloorTile();
                alternateImage = floorType.getStairsImage();
                break;
            default:
                throw new AssertionError(exitDirection.name());
            
        }
        
        
        
        Room room = new Room(floor, exitX, exitY, exitDirection);
        
        
        // special tile, that will transform into door or stairs when key is found
        Tile special = new SwitchableTile(baseImage, null, exitX, exitY, alternateImage, room);
        // CoordinatesSet will prevent other tile to be put in the same spot as allready existing one.
        room.tiles.add(special);
        
        for(int x = 0; x < 12; x++)
            for(int y = 0; y < 12; y++)
            {
                Image base, cluter;
                
                if(x == 0 || x == 11 || y == 0 || y == 11  )
                {
                    base = floorType.getWallTile();
                    cluter = null;
                }
                else
                {
                    base = floorType.getFloorTile();
                    cluter = floorType.getClutterImage();
                }
                
                Tile t = new Tile(base, cluter, x, y);
                
                room.tiles.add(t);
                
            }
        
        // ----- Cristall -------
        
        Cristall.fillWithCristalls(room, room.cristalls, floorType, exitX, exitY);
        
        
        
        
        
        return room;
    }
    
    
    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        for (Tile tile : tiles)
        {
            tile.draw(g, kameraOffsetX, kameraOffsetY);
        }
        
        for (Cristall cristall : cristalls)
        {
            cristall.draw(g, kameraOffsetX, kameraOffsetY);
        }
        
        Game.getHero().getFigure().draw(g, kameraOffsetX, kameraOffsetY);
        
        if(this.animatedEvent != null)
        {
            this.animatedEvent.draw(g, exitX + GameConstants.TILE_WIDTH * 6, 
                    exitY + GameConstants.TILE_HEIGHT * 6);
        }
    }

    public boolean isKeyFound()
    {
        return keyFound;
    }
    
    void foundKey()
    {
        this.keyFound = true;
        System.out.println("Key Found.");
    }

    void update()
    {
        HeroFigure figure = Game.getHero().getFigure();
        
        // ----- found exit ---------------------------------------
        
        if(figure.getX() == this.exitX && figure.getY() == this.exitY)
        {
            // TODO room exit animation start here.
            
            
            if(this.exitDirection == Direction.NONE)
                this.floor.nextFloor();
            else
                this.floor.nextRoom();
            return;
        }
        
        
        // ----- touch cristall -----------------------------------
        Cristall cristall = this.cristalls.get(figure.getX(), figure.getY());
        
        if(cristall != null)
        {
            this.animatedEvent = cristall.touch();
            
        }
        
        if(this.animatedEvent != null)
        {
            if(this.animatedEvent.isDone())
                this.animatedEvent = null;
        }
        
        // ----- Allow Go Direction -------------------------------
        
        if(figure.getX() == 1)
            figure.allowGoingWest(false);
        else
            figure.allowGoingWest(true);
        
        if(figure.getX() == 10)
            figure.allowGoingEast(false);
        else
            figure.allowGoingEast(true);
        
        if(figure.getY() == 1)
            figure.allowGoingNorth(false);
        else
            figure.allowGoingNorth(true);
        
        if(figure.getY() == 10)
            figure.allowGoingSouth(false);
        else
            figure.allowGoingSouth(true);
          
        // TODO allow to go into opened path.
        
        if(this.keyFound)
            if( figure.getX() == this.exitX)
            {
                if(this.exitDirection == Direction.NORTH)
                    figure.allowGoingNorth(true);
                else if(this.exitDirection == Direction.SOUTH)
                    figure.allowGoingSouth(true);
            }
            else if(figure.getY() == this.exitY)
            {
                if(this.exitDirection == Direction.EAST)
                    figure.allowGoingEast(true);
                else if(this.exitDirection == Direction.WEST)
                    figure.allowGoingWest(true);
            }
        
        
        //figure.update();
    }
}

class Tile implements Drawable, Coordinates2D
{

    /** base image of tile */
    private final Image base;
    /** Image of some clutter drawn over tile */
    private final Image cluter;
    /** x position of this tile */
    protected final int x;
    /** y position of this tile */
    protected final int y;
    /** x position of this tile in pixel with kamera offset = 0 */
    protected final int drawX;
    /** y position of this tile in pixel with kamera offset = 0 */
    protected final int drawY;
    
    public Tile(Image base, Image cluter, int x, int y)
    {
        if(base == null)
            throw new NullPointerException("base image may not be null");
        
        this.base = base;
        this.cluter = cluter;
        
        this.x = x;
        this.y = y;
        
        drawX = x * GameConstants.TILE_WIDTH;
        drawY = y * GameConstants.TILE_HEIGHT;
    }
    
    

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        
        
        g.drawImage(this.base, this.drawX + kameraOffsetX, this.drawY + kameraOffsetY, null);
        
        if(this.cluter != null)
            g.drawImage(this.cluter, this.drawX + kameraOffsetX, this.drawY + kameraOffsetY, null);
        
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
    }
}

class SwitchableTile extends Tile
{

    private final Image alternate;
    private final Room room;
    public SwitchableTile( Image base, Image cluter, int x, int y, Image alternate, Room room)
    {
        super(base, cluter, x, y);
        this.alternate = alternate;
        this.room = room;
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        if(!this.room.isKeyFound())
            super.draw(g, kameraOffsetX, kameraOffsetY); 
        else
            g.drawImage(this.alternate, this.drawX + kameraOffsetX, this.drawY + kameraOffsetY, null);
    }
    
    
}
