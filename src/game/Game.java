/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Graphics2D;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import game.dungeon.Dungeon;
import game.dungeon.FloorType;
import game.hero.Hero;
import game.panels.MainFrame;
import game.util.ResourcesContainer;

/**
 *
 * @author gbeljajew
 */
public class Game
{
    private static Hero hero;
    private static Dungeon dungeon;
    private static GameState gameState = GameState.MAP;
    private static final Properties OPTIONS = new Properties();
    private static ImageIcon returnWingIcon;
    /** 
     * true if hero used return wing. means we can stop playing and go to menu.
     * hero is then saved.
     */
    private static boolean usedReturnWing;
    
    /**
     * a flag for finding return wing. if true hero can use return wing to return from dungeon.
     */
    private static boolean foundRW;
    
    static
    {
        OPTIONS.setProperty(GameConstants.OPTION_KEY_LEVELUP, GameConstants.OPTION_VALUE_LEVELUP_MANUAL);
    }
    
    public static String getOption(String key)
    {
        return OPTIONS.getProperty(key, "");
    }
    
    public static void update()
    {
        if(dungeon != null)
            dungeon.update();
        
        if(hero != null)
            hero.update();
    }
    
    public static GameState getGameState()
    {
        return gameState;
    }
    
    public static void setGameState(GameState gameState)
    {
        Game.gameState = gameState;
    }
    
    public static void enterNewDungeon(Hero hero)
    {
        Game.hero = hero;
        Game.dungeon = new Dungeon(1);
        Game.returnWingIcon.setImage(ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_BUTTON_RETURN_WING_DISABLED));
        Game.usedReturnWing = false;
        Game.foundRW = false;
    }
    
    public static void enterNewDungeon(Hero hero, int lvl)
    {
        Game.hero = hero;
        Game.dungeon = new Dungeon(lvl);
        Game.returnWingIcon.setImage(ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_BUTTON_RETURN_WING_DISABLED));
        Game.usedReturnWing = false;
        Game.foundRW = false;
    }

    public static void drawCurrentRoom(Graphics2D graphics2D, int x, int y)
    {
        Game.dungeon.drawCurrentRoom(graphics2D, x, y);
    }
    
    public static Hero getHero()
    {
        return Game.hero;
    }

    public static int getCurrentDungeonLevel()
    {
        if(Game.dungeon == null)
            return 1;
        
        return Game.dungeon.getLevel();
    }

    public static FloorType getCurrentFloorType()
    {
        return Game.dungeon.getFloorType();
    }

    public static void repaintScreen()
    {
        MainFrame.repaintScreen();
    }
    
    public static void connectReturnWingIcon(ImageIcon returnWingIcon)
    {
        Game.returnWingIcon = returnWingIcon;
    }
    
    public static void foundReturnWing()
    {
        Game.returnWingIcon.setImage(ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_BUTTON_RETURN_WING));
        Game.foundRW = true;
    }
    
    public static void useReturnWing()
    {
        System.out.println("useReturnWing");
        
        if(Game.foundRW == true)
        {
            int confirm = JOptionPane.showConfirmDialog(MainFrame.getFrames()[0], 
                    GameLocale.getString("return.wing.confirm.text"),
                    GameLocale.getString("return.wing.confirm.title"), 
                    JOptionPane.PLAIN_MESSAGE);
            
            if(confirm == JOptionPane.YES_OPTION)
                Game.usedReturnWing = true;
        }
            
    }
    
    public static boolean wasReturnWingUsed()
    {
        return Game.usedReturnWing;
    }

    public static void setOption(String key, String value)
    {
        Game.OPTIONS.put(key, value);
    }
}
