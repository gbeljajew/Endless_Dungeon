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
import bin.game.util.drawable.OverlappingDrawable;
import java.awt.Graphics2D;

/**
 *
 * @author gbeljajew
 */
public class HeroFigure implements OverlappingDrawable, Updatable
{
    private static final int STEP_LENGTH = 2;
    private static final int STEP_WAIT_TIME = 5;
    
    private static final int PADDING_NORTH = TILE_HEIGHT / 4;
    private static final int PADDING_EAST = (TILE_WIDTH * 3) / 4;
    private static final int PADDING_SOUTH = TILE_HEIGHT - 5;
    private static final int PADDING_SOUTH_FOR_SHIFT = (TILE_HEIGHT * 3) / 4;
    private static final int PADDING_WEST = TILE_WIDTH / 4;
            
    protected final NPC_Sprite sprite;
    
    private int x, y;
    protected Direction direction = Direction.NONE;
    private int step;
    private final int stepDelay;
    private final int stepLength;
    private boolean moving;

    HeroFigure(HeroClass heroClass)
    {
        this.sprite = heroClass.getHeroBody();
        
        // hero 
        this.stepDelay = STEP_WAIT_TIME;
        this.stepLength = STEP_LENGTH;
    }
    
    public HeroFigure(NPC_Sprite sprite, int stepDelay, int stepLength)
    {
        this.sprite = sprite;
        this.stepDelay = stepDelay;
        this.stepLength = stepLength;
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
        //System.out.println("(" + this.getMapX() + "," + this.getMapY() + ")");
        
        // if figure is not movable, then there is no need to update.
        if(!this.isMovable())
            return;
        
        this.step++;
        
        if(this.step % this.stepDelay == 0)
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
        
        
    }
    
    public boolean isMooving()
    {
        return this.moving;
    }
    
    
    /**
     * should be called only in Hero
     * this was taken out of "update" because only hero should update camera position.
     */
    public void updateCameraPosition()
    {
        Game.CAMERA.updatePosition(x, y);
    }

    public void going(Direction direction)
    {
        this.direction = direction;
    }

    protected void stop()
    {
        this.direction = Direction.NONE;
    }

    private void tryGoNorth()
    {
        // for cases, where npc or hero is to quick and jumps over tile border.
        if(!Game.getCurrentRoom().isPassable(this.getMapX(), this.getMapY()))
        {
            this.y = (this.getMapY() + 1) * TILE_HEIGHT + PADDING_NORTH;
            this.moving = false;
            return;
        }
        
        
        Boolean passable = this.checkPassable(this.getMapX(), this.getMapY() - 1);
        
        if(!passable && y % TILE_HEIGHT < TILE_HEIGHT / 2)
        {
            boolean goingTrough = false;
            
            if( x % TILE_WIDTH > PADDING_EAST )
            {
                goingTrough = this.tryShiftEast();
            }
            else if(x % TILE_WIDTH < PADDING_WEST)
            {
                goingTrough = this.tryShiftWest();
            }
            
            this.y -= this.stepLength;
            this.moving = true;
            
            if(y % TILE_HEIGHT < PADDING_NORTH)
            {
                this.y = this.getMapY() * TILE_HEIGHT + PADDING_NORTH;
                
                if(goingTrough == false)
                    this.moving = false;
            }
        }
        else
        {
            this.y -= this.stepLength;
            
            this.moving = true;
            
            if( x % TILE_WIDTH > PADDING_EAST && 
                (!this.checkPassable(this.getMapX() + 1, this.getMapY()) ||
                 !this.checkPassable(this.getMapX() + 1, this.getMapY() - 1)) && 
                  this.checkPassable(this.getMapX(), this.getMapY() - 1))
            {
                this.x -= this.stepLength;
            }
            else if(x % TILE_WIDTH < PADDING_WEST && 
                (!this.checkPassable(this.getMapX() - 1, this.getMapY()) ||
                 !this.checkPassable(this.getMapX() - 1, this.getMapY() - 1))&& 
                  this.checkPassable(this.getMapX(), this.getMapY() - 1))
            {
                this.x += this.stepLength;
            }
            
        }
        
    }

    private void tryGoEast()
    {
        // for cases, where npc or hero is to quick and jumps over tile border.
        if(!Game.getCurrentRoom().isPassable(this.getMapX(), this.getMapY()))
        {
            this.x = (this.getMapX() - 1) * TILE_WIDTH + PADDING_EAST;
            this.moving = false;
            return;
        }
        
        Boolean passable = this.checkPassable(this.getMapX() + 1, this.getMapY());
        
        if(!passable && x % TILE_WIDTH > TILE_WIDTH / 2)
        {
            
            boolean goingTrough = false;
            
            if( y % TILE_HEIGHT > PADDING_SOUTH_FOR_SHIFT )
            {
                goingTrough = this.tryShiftSouth();
            }
            else if(y % TILE_HEIGHT < PADDING_NORTH)
            {
                goingTrough = this.tryShiftNorth();
            }
            
            this.x += this.stepLength;
            this.moving = true;
            
            if(x % TILE_WIDTH > PADDING_EAST)
            {
                this.x = this.getMapX() * TILE_WIDTH + PADDING_EAST;
                
                if(goingTrough == false)
                    this.moving = false;
            }
        }
        else
        {
            this.x += this.stepLength;
            
            this.moving = true;
            
            if( y % TILE_HEIGHT > PADDING_SOUTH && 
                (!this.checkPassable(this.getMapX(), this.getMapY() + 1) ||
                 !this.checkPassable(this.getMapX() + 1, this.getMapY() + 1))&& 
                  this.checkPassable(this.getMapX() + 1, this.getMapY()))
            {
                this.y -= this.stepLength;
            }
            else if(y % TILE_HEIGHT < PADDING_NORTH && 
                (!this.checkPassable(this.getMapX(), this.getMapY() - 1) ||
                 !this.checkPassable(this.getMapX() + 1, this.getMapY() - 1))&& 
                  this.checkPassable(this.getMapX() + 1, this.getMapY()))
            {
                this.y += this.stepLength;
            }
        }
    }

    private void tryGoSouth()
    {
        // for cases, where npc or hero is to quick and jumps over tile border.
        if(!Game.getCurrentRoom().isPassable(this.getMapX(), this.getMapY()))
        {
            this.y = (this.getMapY() - 1) * TILE_HEIGHT + PADDING_SOUTH;
            this.moving = false;
            return;
        }
        
        Boolean passable = this.checkPassable(this.getMapX(), this.getMapY() + 1);
        
        if(!passable && y % TILE_HEIGHT > PADDING_SOUTH)
        {
            boolean goingTrough = false;
            
            if( x % TILE_WIDTH > PADDING_EAST )
            {
                goingTrough = this.tryShiftEast();
            }
            else if(x % TILE_WIDTH < PADDING_WEST)
            {
                goingTrough = this.tryShiftWest();
            }
            
            this.y += this.stepLength;
            this.moving = true;
            
            if(y % TILE_HEIGHT > PADDING_SOUTH)
            {
                this.y = this.getMapY() * TILE_HEIGHT + PADDING_SOUTH;
                
                if(goingTrough == false)
                    this.moving = false;
            }
        }
        else
        {
            this.moving = true;
            this.y += this.stepLength;
            
            if( x % TILE_WIDTH > PADDING_EAST && 
                (!this.checkPassable(this.getMapX() + 1, this.getMapY()) ||
                 !this.checkPassable(this.getMapX() + 1, this.getMapY() + 1))&& 
                  this.checkPassable(this.getMapX(), this.getMapY() + 1))
            {
                this.x -= this.stepLength;
            }
            else if(x % TILE_WIDTH < PADDING_WEST && 
                (!this.checkPassable(this.getMapX() - 1, this.getMapY()) ||
                 !this.checkPassable(this.getMapX() - 1, this.getMapY() + 1))&& 
                  this.checkPassable(this.getMapX(), this.getMapY() + 1))
            {
                this.x += this.stepLength;
            }
        }
    }

    private void tryGoWest()
    {
        // for cases, where npc or hero is to quick and jumps over tile border.
        if(!Game.getCurrentRoom().isPassable(this.getMapX(), this.getMapY()))
        {
            this.x = (this.getMapX() + 1) * TILE_WIDTH + PADDING_WEST;
            this.moving = false;
            return;
        }
        
        Boolean passable = this.checkPassable(this.getMapX() - 1, this.getMapY());
        
        if(!passable && x % TILE_WIDTH < TILE_WIDTH / 2)
        {
            boolean goingTrough = false;
            
            if( y % TILE_HEIGHT > PADDING_SOUTH_FOR_SHIFT )
            {
                goingTrough = this.tryShiftSouth();
            }
            else if(y % TILE_HEIGHT < PADDING_NORTH)
            {
                goingTrough = this.tryShiftNorth();
            }
            
            this.x -= this.stepLength;
            this.moving = true;
            
            if(x % TILE_WIDTH < PADDING_WEST)
            {
                this.x = this.getMapX() * TILE_WIDTH + PADDING_WEST;
                
                if(goingTrough == false)
                    this.moving = false;
            }
        }
        else
        {
            this.x -= this.stepLength;
            
            this.moving = true;
            
            if( y % TILE_HEIGHT > PADDING_SOUTH && 
                (!this.checkPassable(this.getMapX(), this.getMapY() + 1) ||
                 !this.checkPassable(this.getMapX() - 1, this.getMapY() + 1))&& 
                  this.checkPassable(this.getMapX() - 1, this.getMapY()))
            {
                this.y -= this.stepLength;
            }
            else if(y % TILE_HEIGHT < PADDING_NORTH && 
                (!this.checkPassable(this.getMapX(), this.getMapY() - 1) ||
                 !this.checkPassable(this.getMapX() - 1, this.getMapY() - 1))&& 
                  this.checkPassable(this.getMapX() - 1, this.getMapY()))
            {
                this.y += this.stepLength;
            }
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

    public void setPosition(int x, int y)
    {
        this.x = x * TILE_WIDTH + TILE_WIDTH / 2;
        this.y = y * TILE_HEIGHT + TILE_HEIGHT / 2;
    }

    @Override
    public int getPicksX()
    {
        return this.x;
    }

    @Override
    public int getPicksY()
    {
        return this.y;
    }

    private boolean tryShiftEast()
    {
        boolean side, diagonal;
        
        side = this.checkPassable(this.getMapX() + 1, this.getMapY());
        
        if(this.direction == Direction.NORTH)
            diagonal = this.checkPassable(this.getMapX() + 1, this.getMapY() - 1);
        else
            diagonal = this.checkPassable(this.getMapX() + 1, this.getMapY() + 1);
        
        if(side && diagonal)
        {
            this.x += this.stepLength;
            return true;
        }
        
        return false;
    }

    private boolean tryShiftWest()
    {
        boolean side, diagonal;
        
        side = this.checkPassable(this.getMapX() - 1, this.getMapY());
        
        if(this.direction == Direction.NORTH)
            diagonal = this.checkPassable(this.getMapX() - 1, this.getMapY() - 1);
        else
            diagonal = this.checkPassable(this.getMapX() - 1, this.getMapY() + 1);
        
        if(side && diagonal)
        {
            this.x -= this.stepLength;
            return true;
        }
        
        return false;
    }

    private boolean tryShiftNorth()
    {
        boolean side, diagonal;
        
        side = this.checkPassable(this.getMapX(), this.getMapY() - 1);
        
        if(this.direction == Direction.EAST)
            diagonal = this.checkPassable(this.getMapX() + 1, this.getMapY() - 1);
        else
            diagonal = this.checkPassable(this.getMapX() - 1, this.getMapY() - 1);
        
        if(side && diagonal)
        {
            this.y -= this.stepLength;
            return true;
        }
        
        return false;
    }
    
    private boolean tryShiftSouth()
    {
        boolean side, diagonal;
        
        side = this.checkPassable(this.getMapX(), this.getMapY() + 1);
        
        if(this.direction == Direction.EAST)
            diagonal = this.checkPassable(this.getMapX() + 1, this.getMapY() + 1);
        else
            diagonal = this.checkPassable(this.getMapX() - 1, this.getMapY() + 1);
        
        if(side && diagonal)
        {
            this.y += this.stepLength;
            return true;
        }
        
        return false;
    }

    protected Boolean checkPassable(int x, int y)
    {
        return Game.getCurrentRoom().isPassable(x, y);
    }
    
    public boolean isMovable()
    {
        return this.stepDelay != 0 && this.stepLength != 0;
    }
}
