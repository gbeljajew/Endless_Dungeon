
package game.hero;

import game.GameConstants;
import game.dungeon.Direction;
import game.util.container.Coordinates2D;
import game.util.drawable.Drawable;
import game.util.NPC_Sprite;
import java.awt.Graphics2D;

/**
 * this represents hero figure on map
 * @author gbeljajew
 */
public class HeroFigure implements Drawable, Coordinates2D
{
    private int x, y, count, boostSteps;
    
    private final NPC_Sprite sprite;
    
    private boolean moving = false;
    
    private Direction direction = Direction.SOUTH;
    
    private final int stepTime;
    private final int movingSpeed;
    
    private boolean allowedGoingNorth = true;
    private boolean allowedGoingSouth = true;
    private boolean allowedGoingEast = true;
    private boolean allowedGoingWest = true;
    
    public HeroFigure(HeroClass heroClass)
    {
        this.sprite = heroClass.getHeroBody();
        this.stepTime = heroClass.getstepTime();
        this.movingSpeed = heroClass.getMovingSpeed();
        this.setStartPosition(5, 5);
    }

    
    
    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        sprite.draw(g, x + kameraOffsetX, y + kameraOffsetY);
        
    }
    
    public void update()
    {
        if(this.moving)
        {
            this.count++;
            
            int stime;
            
            if(this.boostSteps > 0)
                stime = this.stepTime / 2;
            else
                stime = this.stepTime;
            
            if(this.count % stime == 0)
            {
                this.sprite.step();
                
                this.boostSteps --;
                
                this.x += this.movingSpeed * this.direction.dx;
                this.y += this.movingSpeed * this.direction.dy;
                
            }
            
            if(!(this.allowedGoingEast && this.allowedGoingNorth && this.allowedGoingSouth && this.allowedGoingWest))
            {
                
                int tx = this.x / GameConstants.TILE_WIDTH;     // X in tiles
                int tdx = this.x % GameConstants.TILE_WIDTH;    // X in pixels inside tile
                
                int ty = this.y / GameConstants.TILE_HEIGHT;      // Y in tiles
                int tdy = this.y % GameConstants.TILE_HEIGHT;     // Y in pixels inside tile
                
                // NORTH
                if(!
                        this.allowedGoingNorth && 
                        tdy < GameConstants.TILE_HEIGHT / 2 && 
                        this.direction == Direction.NORTH)
                {
                    tdy = GameConstants.TILE_HEIGHT / 2;
                    this.y = ty * GameConstants.TILE_HEIGHT + tdy;
                }
                
                // SOUTH
                if(!
                        this.allowedGoingSouth && 
                        tdy > 3 * GameConstants.TILE_HEIGHT / 4 &&  // 3/4 because it looks stupid with 1/2
                        this.direction == Direction.SOUTH)
                {
                    tdy = 3 * GameConstants.TILE_HEIGHT / 4;
                    this.y = ty * GameConstants.TILE_HEIGHT + tdy;
                }
                
                // EAST
                if(!
                        this.allowedGoingEast && 
                        tdx > GameConstants.TILE_WIDTH / 2 && 
                        this.direction == Direction.EAST)
                {
                    tdx = GameConstants.TILE_WIDTH / 2;
                    this.x = tx * GameConstants.TILE_WIDTH + tdx;
                }
                
                // WEST
                if(!
                        this.allowedGoingWest && 
                        tdx < GameConstants.TILE_WIDTH / 2 && 
                        this.direction == Direction.WEST)
                {
                    tdx = GameConstants.TILE_WIDTH / 2;
                    this.x = tx * GameConstants.TILE_WIDTH + tdx;
                }
            }
            
            
        }
    }

    @Override
    public int getX()
    {
        return x / GameConstants.TILE_WIDTH;
    }

    @Override
    public int getY()
    {
        return y / GameConstants.TILE_HEIGHT;
    }
    
    /**
     * puts hero in the middle of tile(x,y)
     * @param x
     * @param y 
     */
    public final void setStartPosition(int x, int y)
    {
        this.x = x * GameConstants.TILE_WIDTH + GameConstants.TILE_WIDTH / 2;
        this.y = y * GameConstants.TILE_HEIGHT + GameConstants.TILE_HEIGHT / 2;
    }
    
    public void goingNorth()
    {
        this.moving = true;
        this.sprite.lookNorth();
        this.direction = Direction.NORTH;
    }
    
    public void goingEast()
    {
        this.moving = true;
        this.sprite.lookEast();
        this.direction = Direction.EAST;
    }
    
    public void goingSouth()
    {
        this.moving = true;
        this.sprite.lookSouth();
        this.direction = Direction.SOUTH;
    }
    
    public void goingWest()
    {
        this.moving = true;
        this.sprite.lookWest();
        this.direction = Direction.WEST;
    }
    
    public void stop()
    {
        this.moving = false;
        this.sprite.step0();
    }

    public void allowGoingWest(boolean b)
    {
        this.allowedGoingWest = b;
    }

    public void allowGoingEast(boolean b)
    {
        this.allowedGoingEast = b;
    }

    public void allowGoingNorth(boolean b)
    {
        this.allowedGoingNorth = b;
    }

    public void allowGoingSouth(boolean b)
    {
        this.allowedGoingSouth = b;
    }

    void going(Direction dir)
    {
        switch(dir)
        {
            case NORTH:
                this.goingNorth();
                break;
            case EAST:
                this.goingEast();
                break;
            case SOUTH:
                this.goingSouth();
                break;
            case WEST:
                this.goingWest();
                break;
            case NONE:
                break;
            default:
                throw new AssertionError(dir.name());
            
        }
    }
    
    void addSpeed(int steps)
    {
        if(this.boostSteps < 0)
            this.boostSteps = steps;
        else
            this.boostSteps += steps;
    }
    
}


