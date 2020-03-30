/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels;

import bin.game.Game;
import bin.game.GameConstants;
import bin.game.dungeon.Room;
import bin.game.util.Direction;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.EnumSet;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import static bin.game.GameConstants.MAP_SCREEN_WIDTH;
import static bin.game.GameConstants.MAP_SCREEN_HEIGHT;

/**
 *
 * @author gbeljajew
 */
public class MapScreen extends JPanel
{
    MapPanel mp;
    HeroSidePanel hsp;
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MapScreen()
    {
        this.setLayout(null);
        
        this.setPreferredSize(new Dimension(800, 600));
        
        
        mp = new MapPanel();
        this.add(mp);
        mp.setBounds(12, 12, MAP_SCREEN_WIDTH, MAP_SCREEN_HEIGHT);
        
        
        hsp = new HeroSidePanel();
        this.add(hsp);
        hsp.setBounds(612, 12, 176, MAP_SCREEN_HEIGHT);
        
        
        this.configureKeyListener();
        this.configureInput();
    }
    
    
    private void configureKeyListener()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher()
                {

                    private final EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
                    private Direction lastDirection;

                    @Override
                    public boolean dispatchKeyEvent(KeyEvent keyEvent)
                    {
                        if (Game.getHero() == null)
                        {
                            return false;
                        }

                        if (keyEvent.getID() == KeyEvent.KEY_PRESSED)
                        {
                            int keyCode = keyEvent.getKeyCode();

                            switch (keyCode)
                            {
                                case KeyEvent.VK_UP:
                                case KeyEvent.VK_W:
                                    this.directions.add(Direction.NORTH);
                                    this.lastDirection = Direction.NORTH;
                                    break;

                                case KeyEvent.VK_DOWN:
                                case KeyEvent.VK_S:
                                    this.directions.add(Direction.SOUTH);
                                    this.lastDirection = Direction.SOUTH;
                                    break;

                                case KeyEvent.VK_RIGHT:
                                case KeyEvent.VK_D:
                                    this.directions.add(Direction.EAST);
                                    this.lastDirection = Direction.EAST;
                                    break;

                                case KeyEvent.VK_LEFT:
                                case KeyEvent.VK_A:
                                    this.directions.add(Direction.WEST);
                                    this.lastDirection = Direction.WEST;
                                    break;
                            }
                        } else if (keyEvent.getID() == KeyEvent.KEY_RELEASED)
                        {
                            int keyCode = keyEvent.getKeyCode();

                            switch (keyCode)
                            {
                                case KeyEvent.VK_UP:
                                case KeyEvent.VK_W:
                                    this.directions.remove(Direction.NORTH);
                                    break;

                                case KeyEvent.VK_DOWN:
                                case KeyEvent.VK_S:
                                    this.directions.remove(Direction.SOUTH);
                                    break;

                                case KeyEvent.VK_RIGHT:
                                case KeyEvent.VK_D:
                                    this.directions.remove(Direction.EAST);
                                    break;

                                case KeyEvent.VK_LEFT:
                                case KeyEvent.VK_A:
                                    this.directions.remove(Direction.WEST);
                                    break;
                            }
                        }

                        if (directions.isEmpty())
                        {
                            Game.getHero().stop();
                        } else if (directions.size() == 1)
                        {
                            Game.getHero().going(directions.iterator().next());
                        } else
                        {
                            Game.getHero().going(lastDirection);
                        }

                        return false;
                    }

                });

    }

    private void configureInput()
    {
        this.getActionMap().put("retreat", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.retreat();
            }
        });
        
        this.getActionMap().put("skill", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.showSkills();
            }
        });
        
        this.getActionMap().put("potion1", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.drinkPotion(1);
            }
        });
        
        this.getActionMap().put("potion2", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.drinkPotion(2);
            }
        });
        
        this.getActionMap().put("potion3", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.drinkPotion(3);
            }
        });
        
        this.getActionMap().put("potion4", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.drinkPotion(4);
            }
        });
        
        this.getActionMap().put("options", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                MapScreen.this.gotoOptionsScreen();
            }
        });
        
        
        
        // ----- KeyBindings ---------------------------------------------
        
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),   "options");
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0),        "retreat");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0),  "retreat");
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),        "skill");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0),  "skill");
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0),  "potion1");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0),  "potion2");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0),  "potion3");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0),  "potion4");
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),  "potion1");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),  "potion2");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),  "potion3");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0),  "potion4");
        
        
    }

    private void retreat()
    {
    }

    private void showSkills()
    {
    }

    private void drinkPotion(int i)
    {
    }

    private void gotoOptionsScreen()
    {
    }
    
}

class MapPanel extends JPanel
{

    @Override
    public void paint(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;
        
        Room room = Game.getCurrentRoom();
        
        room.draw(g, Game.CAMERA.getCameraOffsetX(), Game.CAMERA.getCameraOffsetY());
        
        
    }
    
    
}

class HeroSidePanel extends JPanel
{

    @Override
    public void paint(Graphics g)
    {
        g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    }
    
}

