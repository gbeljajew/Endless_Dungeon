/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.dungeon;

import game.Game;
import game.GameConstants;
import game.dungeon.cristall_content.CristallAction;
import game.dungeon.cristall_content.CristallContentFactory;
import game.hero.HeroFigure;
import game.util.container.Coordinates2D;
import game.util.container.CoordinatesSet;
import game.util.drawable.Drawable;
import game.util.Glob;
import java.awt.Graphics2D;
import java.awt.Image;
import game.util.drawable.Animation;

public class Cristall implements Coordinates2D, Drawable
{
    private static final Image[] CRISTALL_SPRITES = Glob.getTiles("graphic/cristall1.png", 32, 32);
    // -----------------------------------------------------------------
    
    private int counter = 0, step = 0;
    private final int x;
    private final int y;
    private final int drawX;
    private final int drawY;
    private boolean touched;
    private final CristallAction action;

    private Cristall(int x, int y, CristallAction action)
    {
        this.x = x;
        this.y = y;
        this.action = action;
        
        // drawX and drawY are calkulated so, that cristall is drawn 
        // with its lowest point in the middle of tile it is on.
        this.drawX = x * GameConstants.TILE_WIDTH + GameConstants.TILE_WIDTH / 2 - 16;
        this.drawY = y * GameConstants.TILE_HEIGHT + GameConstants.TILE_HEIGHT / 2 - 25;
        
        this.counter = (int)(Math.random() * 100);
        this.step = (int)(Math.random() * CRISTALL_SPRITES.length);
        
    }
    
    
    public Animation touch()
    {
        
        if(!this.touched)
        {
            System.out.println("Action: " + action.getClass().getSimpleName());
            this.touched = true;
            
            return this.action.cristallTouched();
        }
        
        return null;
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

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        counter = (counter + 1) % 5;
        
        if(counter == 0)
            step = (step + 1) % CRISTALL_SPRITES.length;
        
        if(!touched)
            g.drawImage(CRISTALL_SPRITES[step], this.drawX + kameraOffsetX, this.drawY + kameraOffsetY, null);
        
    }
    
    static void fillWithCristalls(Room room, 
            CoordinatesSet<Cristall> cristalls, 
            FloorType floorType, int exitX, int exitY)
    {
        HeroFigure figure = Game.getHero().getFigure();
        int x, y;
        
        do
        {
            x = (int)(Math.random() * 10 + 1);
            y = (int)(Math.random() * 10 + 1);
        }while( x == figure.getX() || y == figure.getY()||  // there should be no cristall where hero is starting
                x == exitX || y == exitY );                 // key should be not on the same row or column with exit
                                                            // so you will not run accedently into exit after you get the key
        
        // this one has key. because it is just put on random position 
        //there is no problem if it is allways the first cristall added
        cristalls.add(new Cristall(x, y, new KeyFoundCristallEvent(CristallContentFactory.createRandomCristallAction(floorType), room))); 

        int numCristalls = (int)((
                GameConstants.MAX_CRISTALLS 
                - GameConstants.MIN_CRISTALLS) 
                * Math.random() 
                + GameConstants.MIN_CRISTALLS);
        
        while(cristalls.size() < numCristalls)
        {
            do
            {
                x = (int)(Math.random() * 10 + 1);
                y = (int)(Math.random() * 10 + 1);
            }while( (x == figure.getX() && y == figure.getY()) || (x == exitX && y == exitY) );
            
            cristalls.add(new Cristall(x, y, CristallContentFactory.createRandomCristallAction(floorType)));
        }
    }
    
    
    
}

class KeyFoundCristallEvent implements CristallAction
{
    private final CristallAction realAction;
    private final Room room;

    public KeyFoundCristallEvent(CristallAction realAction, Room room)
    {
        this.realAction = realAction;
        this.room = room;
    }

    @Override
    public Animation cristallTouched()
    {
        Animation result = this.realAction.cristallTouched();
        
        
        this.room.foundKey();
        
        System.out.println("Key Found");
        
        return result;
    }
    
}
