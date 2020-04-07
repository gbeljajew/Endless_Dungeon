/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.hero;

import bin.game.Game;
import static bin.game.GameConstants.TILE_HEIGHT;
import static bin.game.GameConstants.TILE_WIDTH;
import bin.game.Updatable;
import bin.game.util.Direction;
import bin.game.util.NPC_Sprite;
import bin.game.util.drawable.Drawable;
import java.awt.Graphics2D;

/**
 *
 * @author gbeljajew
 */
public class HeroFigure implements Drawable, Updatable
{
    private static final int STEP_LENGTH = 2;
    private static final int STEP_WAIT_TIME = 5;
    
    private static final int PADDING_NORTH = TILE_HEIGHT / 4;
    private static final int PADDING_EAST = (TILE_WIDTH * 3) / 4;
    private static final int PADDING_SOUTH = TILE_HEIGHT - 3;
    private static final int PADDING_WEST = TILE_WIDTH / 4;
            
    private final NPC_Sprite sprite;
    
    private int x, y;
    private Direction direction = Direction.NONE;
    private int step;

    HeroFigure(HeroClass heroClass)
    {
        this.sprite = heroClass.getHeroBody();
    }

    @Override
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY)
    {
        this.sprite.draw(g, x - cameraOffsetX, y - cameraOffsetY);
        
        //System.out.println("draw (" + x + "," + y + ")" );
    }

    @Override
    public void update()
    {
        // TODO implement
        // moving figure
        
        //System.out.println("(" + this.getMapX() + "," + this.getMapY() + ")");
        
        this.step++;
        
        if(this.step % STEP_WAIT_TIME == 0)
        {
            this.step = 0;
            this.sprite.step();
        }
        
        switch(this.direction)
        {
            case NORTH:
                this.sprite.lookNorth();
                this.tryGoNorth();
                break;
            case EAST:
                this.sprite.lookEast();
                this.tryGoEast();
                break;
            case SOUTH:
                this.sprite.lookSouth();
                this.tryGoSouth();
                break;
            case WEST:
                this.sprite.lookWest();
                this.tryGoWest();
                break;
            case NONE:
                this.sprite.step0();
                break;
            default:
                throw new AssertionError(this.direction.name());
        }
        
        Game.CAMERA.updatePosition(x, y);
    }

    void going(Direction direction)
    {
        this.direction = direction;
    }

    void stop()
    {
        this.direction = Direction.NONE;
    }

    private void tryGoNorth()
    {
        
        
        Boolean passable = Game.getCurrentRoom().isPassable(this.getMapX(), this.getMapY() - 1);
        
        if(!passable && y % TILE_HEIGHT < PADDING_NORTH)
        {
            this.y = this.getMapY() * TILE_HEIGHT + PADDING_NORTH;
        }
        else
        {
            this.y -= STEP_LENGTH;
        }
        
    }

    private void tryGoEast()
    {
        
        
        Boolean passable = Game.getCurrentRoom().isPassable(this.getMapX() + 1, this.getMapY());
        
        if(!passable && x % TILE_WIDTH > PADDING_EAST)
        {
            this.x = this.getMapX() * TILE_WIDTH + PADDING_EAST;
        }
        else
        {
            this.x += STEP_LENGTH;
        }
    }

    private void tryGoSouth()
    {
        
        
        Boolean passable = Game.getCurrentRoom().isPassable(this.getMapX(), this.getMapY() + 1);
        
        if(!passable && y % TILE_HEIGHT > PADDING_SOUTH)
        {
            this.y = this.getMapY() * TILE_HEIGHT + PADDING_SOUTH;
        }
        else
        {
            this.y += STEP_LENGTH;
        }
    }

    private void tryGoWest()
    {
        
        
        Boolean passable = Game.getCurrentRoom().isPassable(this.getMapX() - 1, this.getMapY());
        
        if(!passable && x % TILE_WIDTH < PADDING_WEST)
        {
            this.x = this.getMapX() * TILE_WIDTH + PADDING_WEST;
        }
        else
        {
            this.x -= STEP_LENGTH;
        }
    }

    public int getMapX()
    {
        return this.x / TILE_WIDTH;
    }

    public int getMapY()
    {
        return this.y / TILE_HEIGHT;
    }

    void setPosition(int x, int y)
    {
        this.x = x * TILE_WIDTH + TILE_WIDTH / 2;
        this.y = y * TILE_HEIGHT + TILE_HEIGHT / 2;
    }

    
}
