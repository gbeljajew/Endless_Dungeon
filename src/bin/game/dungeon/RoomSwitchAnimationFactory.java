/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.dungeon;

import bin.game.GameConstants;
import static bin.game.GameConstants.MAP_SCREEN_HEIGHT;
import static bin.game.GameConstants.MAP_SCREEN_WIDTH;
import bin.game.util.Direction;
import bin.game.util.drawable.Animation;
import java.awt.Graphics2D;

/**
 *
 * @author gbeljajew
 */
public class RoomSwitchAnimationFactory 
{
    private static final int ANIMATION_STEP = 20;
    
    static Animation getEntryAnimation(Direction entryDirection)
    {
        
        System.out.println("Entry " + entryDirection);
        
        switch(entryDirection)
        {
            case SOUTH:
                return new Animation()
                {
                    private int width = 0;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        g.fillRect(x, y + width, GameConstants.MAP_SCREEN_WIDTH, GameConstants.MAP_SCREEN_HEIGHT);
                        
                        width += ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        return width >= GameConstants.MAP_SCREEN_HEIGHT;
                    }
                };
            case WEST:
                return new Animation()
                {
                    private int width = 0;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        g.fillRect(x, y, MAP_SCREEN_WIDTH - width, MAP_SCREEN_HEIGHT);
                        
                        width += ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        return width >= MAP_SCREEN_WIDTH;
                    }
                };
            case NORTH:
                return new Animation()
                {
                    private int width = 0;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        
                        g.fillRect(x, y, MAP_SCREEN_WIDTH, MAP_SCREEN_HEIGHT - width);
                        
                        width += ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        return width >= MAP_SCREEN_HEIGHT;
                    }
                };
            case EAST:
                return new Animation()
                {
                    private int width = 0;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        g.fillRect(width, y, MAP_SCREEN_WIDTH - width, MAP_SCREEN_HEIGHT);
                        
                        width += ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        return width >= GameConstants.MAP_SCREEN_WIDTH;
                    }
                };
            case NONE:
                return new Animation()
                {
                    private int width = 0;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        // top left
                        g.fillRect(x, y, MAP_SCREEN_WIDTH / 2 - width / 2, MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        // top right
                        g.fillRect(MAP_SCREEN_WIDTH / 2 + width / 2, 
                                y, 
                                MAP_SCREEN_WIDTH / 2 - width / 2,
                                MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        // bottom left
                        g.fillRect(x, MAP_SCREEN_HEIGHT / 2 + width / 2, 
                                MAP_SCREEN_WIDTH / 2 - width / 2, 
                                MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        // bottom right
                        g.fillRect(MAP_SCREEN_WIDTH / 2 + width / 2, 
                                MAP_SCREEN_HEIGHT / 2 + width / 2, 
                                MAP_SCREEN_WIDTH / 2 - width / 2, 
                                MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        width += ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        return width >= GameConstants.MAP_SCREEN_HEIGHT;
                    }
                };
            default:
                throw new AssertionError(entryDirection.name());
            
        }
        
        
    }
    
    static Animation getExitAnimation(Direction exitDirection, Floor floor)
    {
        System.out.println("Exit " + exitDirection);
        
        
        switch(exitDirection)
        {
            case NORTH:
                return new Animation()
                {
                    private int width = MAP_SCREEN_HEIGHT;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        g.fillRect(x, y + width, GameConstants.MAP_SCREEN_WIDTH, GameConstants.MAP_SCREEN_HEIGHT);
                        
                        width -= ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        if(width <= 0)
                        {
                            floor.nextRoom();
                            return false;
                        }
                        else
                            return false;
                    }
                };
            case EAST:
                return new Animation()
                {
                    private int width = MAP_SCREEN_WIDTH;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        g.fillRect(x, y, MAP_SCREEN_WIDTH - width, MAP_SCREEN_HEIGHT);
                        
                        width -= ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        if(width <= 0)
                        {
                            floor.nextRoom();
                            return false;
                        }
                        else
                            return false;
                    }
                };
            case SOUTH:
                return new Animation()
                {
                    private int width = MAP_SCREEN_HEIGHT;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        
                        g.fillRect(x, y, MAP_SCREEN_WIDTH, MAP_SCREEN_HEIGHT - width);
                        
                        width -= ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        if(width <= 0)
                        {
                            floor.nextRoom();
                            return false;
                        }
                        else
                            return false;
                    }
                };
            case WEST:
                return new Animation()
                {
                    private int width = MAP_SCREEN_WIDTH;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        g.fillRect(width, y, MAP_SCREEN_WIDTH - width, MAP_SCREEN_HEIGHT);
                        
                        width -= ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        if(width <= 0)
                        {
                            floor.nextRoom();
                            return false;
                        }
                        else
                            return false;
                    }
                };
            case NONE:
                return new Animation()
                {
                    private int width = MAP_SCREEN_WIDTH;
                    
                    
                    @Override
                    public void draw(Graphics2D g, int x, int y)
                    {
                        // top left
                        g.fillRect(x, y, MAP_SCREEN_WIDTH / 2 - width / 2, MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        // top right
                        g.fillRect(MAP_SCREEN_WIDTH / 2 + width / 2, 
                                y, 
                                MAP_SCREEN_WIDTH / 2 - width / 2,
                                MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        // bottom left
                        g.fillRect(x, MAP_SCREEN_HEIGHT / 2 + width / 2, 
                                MAP_SCREEN_WIDTH / 2 - width / 2, 
                                MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        // bottom right
                        g.fillRect(MAP_SCREEN_WIDTH / 2 + width / 2, 
                                MAP_SCREEN_HEIGHT / 2 + width / 2, 
                                MAP_SCREEN_WIDTH / 2 - width / 2, 
                                MAP_SCREEN_HEIGHT / 2 - width / 2);
                        
                        width -= ANIMATION_STEP;
                    }

                    @Override
                    public boolean isDone()
                    {
                        if(width <= 0)
                        {
                            floor.nextFloor();
                            return false;
                        }
                        else
                            return false;
                    }
                };
            default:
                throw new AssertionError(exitDirection.name());
            
        }
    
    
    }
    
    
    private RoomSwitchAnimationFactory(){}
}
