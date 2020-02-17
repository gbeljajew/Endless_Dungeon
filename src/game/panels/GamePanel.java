package game.panels;

import game.Game;
import game.GameConstants;
import game.util.ResourcesContainer;
import game.dungeon.Direction;
import game.hero.BarInfo;
import game.hero.Hero;
import game.hero.HeroOld;
import game.hero.PotionPouch;
import game.hero.Stat;
import game.hero.StatsEnum;
import game.hero.skill.SkillPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * this class will contain all you game logic and graphic<br>
 * use it for smaller games
 *
 * @author gbeljajew
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel
{

    public static Point getCenter()
    {
        Point p = instance.getLocationOnScreen();
        
        System.out.println(p);
        
        return new Point(p.x + 300, p.y + 300);
        
    }
    
    private static GamePanel instance;

    private final MainFrame mainFrame;

    /**
     * true = game running, false = paused.
     */
    private boolean running = false;
    /**
     * true = game can be continued, false = after game over and/or before new
     * game.
     */
    private boolean inGame = false;
    /**
     * this field contains score of your game.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private long score = 0;

    private final HeroPanel heroPanel;
    private final MapPanel mapPanel;

    GamePanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        instance = this;
        
        this.setLayout(null);

        
        mapPanel = new MapPanel();
        this.add(mapPanel);
        mapPanel.setBounds(0, 0, 600, 600);

        heroPanel = new HeroPanel(this);
        this.add(heroPanel);
        heroPanel.setBounds(600, 0, 200, 600);

        this.configureInput();
        this.setupKeyListener();

    }

    
    
    public void init(Hero hero)
    {
        this.running = true;  // set false, if you want to start paused.
        this.inGame = true;

        Game.enterNewDungeon(hero);

        this.heroPanel.revire();
    }

    /**
     * this method will be started every time before redrawing. game logic comes
     * here.
     */
    public void update()
    {
        // TODO: main logic of your game comes here.

        Game.update();
        
        if(this.gameEndCondition())
        {
            this.gameEnd();
        }

        // this block controls if this game session has ended and we should save the score.
        // and yes, wining a game is a game over too.
        if (this.gameOverCondition())
        {
            this.gameOver();
        }
    }

    /**
     * call this method when the game session ends and you want to save score.
     * if you do not want to save score - use gameEnd() instead.
     */
    private void gameOver()
    {
        long lastScore = Game.getHero().getScore();

        //OPTIONAL: last calculations of score are made here (if needed)
        this.pause();
        this.inGame = false;  	// with this you can not come back to this game session using "Continue" button in menu.

        this.mainFrame.gameOver(lastScore);
    }

    /**
     * call this method if you want to end game session without saving score.
     */
    private void gameEnd()
    {
        this.pause();
        this.inGame = false;
        this.mainFrame.switchToMenu();
    }

    /**
     * call this method if you want to go to Menu without ending the game
     * session.
     */
    @SuppressWarnings("unused")
    private void goToMenu()
    {
        this.pause();
        this.mainFrame.switchToMenu();
    }
    
    void gotoOptionsScreen()
    {
        
        this.mainFrame.switchToOptions(MainFrame.GAME);
    }

    private boolean gameEndCondition()
    {
        boolean res = Game.wasReturnWingUsed();
        
        
        return res;
    }
    
    

    private boolean gameOverCondition()
    {

        return !Game.getHero().isHeroAlive();
    }

    /**
     * method for pausing your game. those methods both are private because
     * there should be no need to pause/unpause your game from outside.
     */
    private void pause()
    {
        this.running = false;
    }

    /**
     *
     */
    private void unpause()
    {
        this.running = true;
    }

    boolean isRunning()
    {
        return this.running;
    }

    boolean isInGame()
    {
        return this.inGame;
    }

    private void setupKeyListener()
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
        mapPanel.getActionMap().put("retreat", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.retreat();
            }
        });
        
        mapPanel.getActionMap().put("skill", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.showSkills();
            }
        });
        
        mapPanel.getActionMap().put("potion1", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.drinkPotion(1);
            }
        });
        
        mapPanel.getActionMap().put("potion2", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.drinkPotion(2);
            }
        });
        
        mapPanel.getActionMap().put("potion3", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.drinkPotion(3);
            }
        });
        
        mapPanel.getActionMap().put("potion4", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.drinkPotion(4);
            }
        });
        
        mapPanel.getActionMap().put("options", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                GamePanel.this.gotoOptionsScreen();
            }
        });
        
        
        
        // ----- KeyBindings ---------------------------------------------
        
        
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),   "options");
        
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0),        "retreat");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0),  "retreat");
        
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),        "skill");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0),  "skill");
        
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0),  "potion1");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0),  "potion2");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0),  "potion3");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0),  "potion4");
        
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),  "potion1");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),  "potion2");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),  "potion3");
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0),  "potion4");
        
        
    }

    private void retreat()
    {
        Game.useReturnWing();
    }

    private void showSkills()
    {
        this.heroPanel.showSkillWindow();
    }

    private void drinkPotion(int i)
    {
        this.heroPanel.drinkPotion(i);
    }
    
    

}

class MapPanel extends JPanel
{

    @Override
    public void paint(Graphics grphcs)
    {

        for (int i = 0; i < 600; i += 5)
        {
            grphcs.drawLine(i, 0, 0, i);
            grphcs.drawLine(i, 600, 600, i);
        }

        // TODO 
        Game.drawCurrentRoom((Graphics2D) grphcs, 12, 12);

        // TODO:DEBUG
        for (int i = 12; i < 600; i += GameConstants.TILE_WIDTH)
        {
            grphcs.drawLine(i, 0, i, 600);
            grphcs.drawLine(0, i, 600, i);
        }

    }

}

class HeroPanel extends JPanel
{

    private final GamePanel gp;
    
    private final JLabel heroFaceLbl = new JLabel();
    private final JLabel heroNameLbl = new JLabel();
    private final JLabel heroClassLbl = new JLabel();
    private final JLabel heroLvlLbl = new JLabel();
    
    private final GoldLabel goldLabel = new GoldLabel();
    
    private final GameBar healthBar = new GameBar();
    private final GameBar manaBar = new GameBar();
    private final GameBar expBar = new GameBar();

    private final StatLabel strLabel = new StatLabel();
    private final StatLabel magLabel = new StatLabel();
    private final StatLabel defLabel = new StatLabel();
    private final StatLabel vitLabel = new StatLabel();
    private final StatLabel spdLabel = new StatLabel();
    
    private final PotionSlotButton munitionPouch = new PotionSlotButton(190, 20, false);
    private final PotionSlotButton hpPotion = new PotionSlotButton(190, 30);
    private final PotionSlotButton mpPotion = new PotionSlotButton(190, 30);
    private final PotionSlotButton speedPotion = new PotionSlotButton(190, 30);
    private final PotionSlotButton boostPotion = new PotionSlotButton(190, 30);
    
    private final ImageIcon returnWingIcon = new ImageIcon(
            ResourcesContainer.getMiskImage(
                    GameConstants.ICON_KEY_BUTTON_RETURN_WING_DISABLED));
    private final JLabel returnWingButton = new JLabel(returnWingIcon);
    
    private final JLabel skillButton = new JLabel(new ImageIcon(
                    ResourcesContainer.getMiskImage(
                    GameConstants.ICON_KEY_BUTTON_SKILLS)));
    private final JLabel optionButton = new JLabel(new ImageIcon(
                    ResourcesContainer.getMiskImage(
                    GameConstants.ICON_KEY_BUTTON_OPTION)));

    public HeroPanel(GamePanel gp)
    {
        this.gp = gp;

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipady = 5;
        gbc.weightx = 1;

        gbc.gridwidth = 4;
        this.add(this.heroFaceLbl, gbc);
       
        gbc.gridy = 1;
        this.add(this.heroNameLbl, gbc);
        
        
        gbc.gridy = 2;
        this.add(this.heroClassLbl, gbc);
        
        gbc.gridy = 3;
        this.add(this.heroLvlLbl, gbc);
        
        gbc.gridy = 4;
        this.add(this.goldLabel, gbc);
        
        gbc.gridy = 5;
        this.add(this.expBar, gbc);
        
        gbc.gridy = 6;
        this.add(this.healthBar, gbc);
        
        gbc.gridy = 7;
        this.add(this.manaBar, gbc);
        
        gbc.gridy = 8;
        this.add(this.strLabel, gbc);
        gbc.gridy = 9;
        this.add(this.magLabel, gbc);
        gbc.gridy = 10;
        this.add(this.defLabel, gbc);
        gbc.gridy = 11;
        this.add(this.vitLabel, gbc);
        gbc.gridy = 12;
        this.add(this.spdLabel, gbc);
        
        gbc.gridy = 13;
        this.add(this.munitionPouch, gbc);
        gbc.gridy = 14;
        this.add(this.hpPotion, gbc);
        gbc.gridy = 15;
        this.add(this.mpPotion, gbc);
        gbc.gridy = 16;
        this.add(this.speedPotion, gbc);
        gbc.gridy = 17;
        this.add(this.boostPotion, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 18;
        gbc.gridx = 1;
        this.add(this.returnWingButton, gbc);
        
        gbc.gridx = 2;
        this.add(this.skillButton, gbc);
        
        gbc.gridx = 3;
        this.add(this.optionButton, gbc);
        
        // image in this icon will change based on if hero has returnWing or not
        Game.connectReturnWingIcon(returnWingIcon);
        
        MouseClickListener mcl = new MouseClickListener();
        
        this.returnWingButton.addMouseListener(mcl);
        this.optionButton.addMouseListener(mcl);
        this.skillButton.addMouseListener(mcl);
        
        

    }

    void revire()
    {
        this.heroFaceLbl.setIcon(new ImageIcon(Game.getHero().getHeroClass().getFace()));
        this.heroNameLbl.setText(Game.getHero().getName());
        this.heroClassLbl.setText(Game.getHero().getHeroClass().getClassName());
        
        
        this.expBar.setInfo(Game.getHero().getExpBarInfo());
        this.healthBar.setInfo(Game.getHero().getHealthBarInfo());
        this.manaBar.setInfo(Game.getHero().getManaBarInfo());
        
        this.strLabel.setStat(Game.getHero().getStat(StatsEnum.STR));
        this.magLabel.setStat(Game.getHero().getStat(StatsEnum.MAG));
        this.defLabel.setStat(Game.getHero().getStat(StatsEnum.ARM));
        this.vitLabel.setStat(Game.getHero().getStat(StatsEnum.VIT));
        this.spdLabel.setStat(Game.getHero().getStat(StatsEnum.SPD));
        
        this.munitionPouch.setPouch(Game.getHero().getMunitionPouch());
        this.hpPotion.setPouch(Game.getHero().getHpPotionPouch());
        this.mpPotion.setPouch(Game.getHero().getManaPotionPouch());
        this.speedPotion.setPouch(Game.getHero().getSpeedPotionPouch());
        this.boostPotion.setPouch(Game.getHero().getBoostPotionPouch());
    }

    @Override
    public void paint(Graphics gr)
    {
        this.heroLvlLbl.setText(String.format("LVL: %d", Game.getHero().getLvl()));
        super.paint(gr);
    }
    
    void showSkillWindow()
    {
        
        // TODO
        SkillPanel skillPanel = new SkillPanel();
        
    }

    void drinkPotion(int i)
    {
        switch(i)
        {
            case 1:
                this.hpPotion.drink();
                break;
            case 2:
                this.mpPotion.drink();
                break;
            case 3:
                this.speedPotion.drink();
                break;
            case 4:
                this.boostPotion.drink();
                break;
            default:
                throw new IllegalArgumentException("unknown potion: " + i);

        }
    }
    
    class MouseClickListener extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent me)
        {
            if(me.getSource() == HeroPanel.this.returnWingButton)
            {
                Game.useReturnWing();
            }
            
            if(me.getSource() == HeroPanel.this.optionButton)
            {
                HeroPanel.this.gp.gotoOptionsScreen();
            }
            
            if(me.getSource() == HeroPanel.this.skillButton)
            {
                HeroPanel.this.showSkillWindow();
            }
            
        }
        
    }
}

class GameBar extends JPanel
{

    {
        this.setPreferredSize(new Dimension(190, 20));
    }
    
    private BarInfo info;

    public void setInfo(BarInfo info)
    {
        this.info = info;
    }

    @Override
    public void paint(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;

        int width = this.getWidth();
        int height = this.getHeight();

        Image icon = this.info.getIcon();

        int imageHigh = icon.getHeight(null);

        int yDiff = (height - imageHigh) / 2;
        int imageWidth = icon.getWidth(null);
        // TODO:DEBUG
        //g.drawRect(0, 0, width - 1, height - 1);

        g.drawImage(icon, 5, yDiff, null);

        int hpPercent = (width - imageWidth - 15) * this.info.getValue() / this.info.getMaxValue();

        Color c = g.getColor();
        g.setColor(info.getBarColor());
        g.fillRect(imageWidth + 10, yDiff, hpPercent, imageHigh);
        g.setColor(c);

        String text = String.format("%d/%d", this.info.getValue(), this.info.getMaxValue());
        int strWidth = g.getFontMetrics().stringWidth(text);
        int strHigh = g.getFontMetrics().getAscent();
        g.drawString(text, 100 - strWidth / 2, height / 2 + strHigh / 2);

        g.drawRect(imageWidth + 10, yDiff, width - imageWidth - 15, imageHigh);

    }
}

class PotionSlotButton extends JPanel implements MouseListener
{

    private PotionPouch pouch;
    private boolean pressed = false;
    private boolean isButton = true;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public PotionSlotButton(int width, int height)
    {
        this.setPreferredSize(new Dimension(width, height));
        
        this.addMouseListener(this);
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public PotionSlotButton(int width, int height, boolean isButton)
    {
        this.setPreferredSize(new Dimension(width, height));
        this.isButton = isButton;
        
        if(isButton == true)
            this.addMouseListener(this);
    }

    public void setPouch(PotionPouch pouch)
    {
        this.pouch = pouch;
    }

    @Override
    public void paint(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;

        int count = this.pouch.getCount();
        Image image = this.pouch.getImage();

        if (count > 0)
        {
            //Image hpPotion = GameResources.getMiskImage("healthPotion");
            int imageWidth = image.getWidth(null);
            double xStep;
            int xStart;
            
            if(this.getWidth() < imageWidth * count)
            {
                xStep = (this.getWidth() - 3 - imageWidth) / (double)(count - 1);
                xStart = (int)(this.getWidth() - (imageWidth + xStep * (count - 1))) / 2;
            }
                
            else
            {
                xStep = imageWidth;
                xStart = (this.getWidth() - (imageWidth * (count))) / 2;
            }
                
            int yStart = (this.getHeight() - image.getHeight(null)) / 2;

            if(this.isButton == true)
            {
                // ----- Shadow -------------------------------------------------
                Color c = g.getColor();
                g.setColor(Color.GRAY);
                g.drawRoundRect(1, 1, this.getWidth() - 2, this.getHeight() - 2, 3, 3);
                g.setColor(c);

                // ----- Button -------------------------------------------------
                int x = 0, y = 0;
                if(this.pressed == true)
                {
                    x = 1;
                    y = 1;
                }
                g.drawRoundRect(x, y, this.getWidth() - 2, this.getHeight() - 2, 3, 3);
            }else
            {
                g.drawRoundRect(0, 0, this.getWidth() - 2, this.getHeight() - 2, 3, 3);
            }
            
            // ----- Flask --------------------------------------------------
            for (int i = 0; i < count; i++)
            {
                g.drawImage(image, (int)((count - i - 1) * xStep + xStart), yStart, null);
            }
        }
        else
        {
            if(this.isButton == true)
            {
                // ----- Shadow -------------------------------------------------
                Color c = g.getColor();
                g.setColor(Color.GRAY);
                g.drawRoundRect(1, 1, this.getWidth() - 2, this.getHeight() - 2, 3, 3);
                g.setColor(c);

                // ----- Button -------------------------------------------------
                int x = 0, y = 0;
                if(this.pressed == true)
                {
                    x = 1;
                    y = 1;
                }
                g.drawRoundRect(x, y, this.getWidth() - 2, this.getHeight() - 2, 3, 3);
            }else
            {
                g.drawRoundRect(0, 0, this.getWidth() - 2, this.getHeight() - 2, 3, 3);
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
        if(this.pouch != null)
            pouch.drink();
    }

    @Override
    public void mousePressed(MouseEvent me)
    {
        this.pressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent me)
    {
        this.pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
 
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
        this.pressed = false;
    }

    void drink()
    {
        this.pouch.drink();
    }
}

class StatLabel extends JPanel
{
    
    private Stat stat;

    public StatLabel()
    {
        this.setPreferredSize(new Dimension(190, 20));
    }

    public void setStat(Stat stat)
    {
        this.stat = stat;
    }
    
    
    @Override
    public void paint(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;

        int width = this.getWidth();
        int height = this.getHeight();

        Image statImage = stat.getIcon();
        

        int imageHigh = statImage.getHeight(null);

        int yDiff = (height - imageHigh) / 2;
        int imageWidth = statImage.getWidth(null);
        // TODO:DEBUG
        //g.drawRect(0, 0, width - 1, height - 1);

        g.drawImage(statImage, 5, yDiff, null);

        

        String text = String.format("%d/%d", this.stat.getRealValue(), this.stat.getValue());
        int strWidth = g.getFontMetrics().stringWidth(text);
        int strHigh = g.getFontMetrics().getAscent();
        g.drawString(text, 100 - strWidth / 2, height / 2 + strHigh / 2);

        g.drawRect(imageWidth + 10, yDiff, width - imageWidth - 15, imageHigh);
    }
    
}

class GoldLabel extends JPanel
{
    
    {
        this.setPreferredSize(new Dimension(190, 20));
    }
    
    @Override
    public void paint(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;

        int width = this.getWidth();
        int height = this.getHeight();

        Image statImage = ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_GOLD_ICON);
        

        int imageHigh = statImage.getHeight(null);

        int yDiff = (height - imageHigh) / 2;
        int imageWidth = statImage.getWidth(null);
        // TODO:DEBUG
        //g.drawRect(0, 0, width - 1, height - 1);

        g.drawImage(statImage, 5, yDiff, null);

        

        String text = String.format("%d", Game.getHero().getGoldAmount());
        int strWidth = g.getFontMetrics().stringWidth(text);
        int strHigh = g.getFontMetrics().getAscent();
        g.drawString(text, 100 - strWidth / 2, height / 2 + strHigh / 2);

        g.drawRect(imageWidth + 10, yDiff, width - imageWidth - 15, imageHigh);
    }
}
