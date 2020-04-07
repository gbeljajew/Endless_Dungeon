/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.hero;

import bin.game.Game;
import bin.game.Updatable;
import bin.game.util.Direction;
import bin.game.util.drawable.Drawable;
import java.awt.Graphics2D;

/**
 *
 * @author gbeljajew
 */
public class Hero implements Drawable, Updatable
{
    private final HeroFigure figure;
    
    private final HeroClass heroClass;

    public Hero(HeroClass heroClass)
    {
        this.heroClass = heroClass;
        this.figure = new HeroFigure(heroClass);
        
        
        // TODO DEBUG will be removed after starting in INN is implemented.
        this.setPosition(10, 10);
    }
    
    
    

    public void stop()
    {
        this.figure.stop();
    }

    public void going(Direction next)
    {
        this.figure.going(next);
    }

    @Override
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY)
    {
        this.figure.draw(g, cameraOffsetX, cameraOffsetY);
    }

    @Override
    public void update()
    {
        this.figure.update();
        Game.getCurrentRoom().tap(this.figure.getMapX(), this.figure.getMapY());
        
        // TODO touching cristall.
    }
    
    public void setPosition(int x, int y)
    {
        this.figure.setPosition(x,y);
    }
}
