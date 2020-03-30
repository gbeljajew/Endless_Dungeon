/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game;

import bin.game.dungeon.Dungeon;
import bin.game.dungeon.Room;
import bin.game.dungeon.Village;
import bin.game.hero.Hero;
import bin.game.resources.GameResources;
import bin.game.util.drawable.Camera;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gbeljajew
 */
public class Game 
{
    public static final Camera CAMERA = new Camera();
    
    
    
    
    private static GameState gameState = GameState.VILLAGE;
    
    private static final List<Updatable> UPDATABLES = new ArrayList<>();
    
    private final static Village village = new Village();
    
    private static Dungeon dungeon = new Dungeon(1);
    
    private static Hero hero = new Hero(GameResources.getHeroClasses().get(0));
    
    static
    {
        UPDATABLES.add(new Updatable()
        {
            @Override
            public void update()
            {
                Game.getCurrentRoom().update();
            }
        });
    }
    
    
    public static void update()
    {
        for (Updatable updatable : UPDATABLES)
        {
            updatable.update();
        }
    }

    public static int getCurrentDungeonLevel()
    {
        if(gameState == GameState.VILLAGE)
            return 0;
        else
            return dungeon.getCurrentFloor();
    }

    public static Room getCurrentRoom()
    {
        if(gameState == GameState.VILLAGE)
            return village;
        else
            return dungeon;
    }
    
    
    
    
    
    
    
    
    public static void addUpdatable(Updatable l)
    {
        UPDATABLES.add(l);
    }
    
    public static GameState getGameState()
    {
        return gameState;
    }

    public static void setGameState(GameState gameState)
    {
        Game.gameState = gameState;
    }

    public static Hero getHero()
    {
        return hero;
    }

    public static void setHero(Hero hero)
    {
        Game.hero = hero;
    }
    
    public static void startNewRun(int level)
    {
        dungeon =new Dungeon(level);
    }
    
    
    
    
    
}
