package game.panels;

import game.GameConstants;
import game.GameLocale;
import game.util.ResourcesContainer;
import game.GameStart;
import game.GameResources;
import game.hero.Hero;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author gbeljajew
 */
@SuppressWarnings( "serial" )
public class MainFrame extends JFrame
{

    static final String GAME = "game";
    static final String MENU = "menu";
    static final String OPTIONS = "options";
    
    private static MainFrame instance;

    /**
     * sometimes you need to toss repaint from some dialogue.
     */
    public static void repaintScreen()
    {
        instance.repaint();
    }

    

    private final GamePanel gamePanel;
    private final MenuPanel menuPanel;
    private final OptionsPanel optionsPanel;
    private final CardLayout panelSwitcher;
    private final JPanel screenHolder;
    
    /** it saves key for panel that called options */
    private String panelCalledOptions = "";

    public MainFrame()
    {
        super( GameLocale.getString( "title" ) );
        
        instance = this;
        
        // screens
        this.gamePanel = new GamePanel( this );
        this.menuPanel = new MenuPanel( this );
        this.optionsPanel = new OptionsPanel(this);

        this.panelSwitcher = new CardLayout();
        screenHolder = new JPanel( panelSwitcher );
        
        if( GameConstants.FULL_SCREEN_MODE )
        {
            this.createGuiFullScreen();
        }
        else
        {
            this.createGuiWindow();
        }

    }

    public void update()
    {
        if( this.gamePanel.isVisible() && this.gamePanel.isRunning() )
        {
            this.gamePanel.update();
        }
    }

    void newGame()
    {
        Hero hero = new Hero(GameResources.getHeroClasses().get(0), "GEO");
        
        this.gamePanel.init(hero);
        
        this.gamePanel.requestFocus();
        
        this.switchToGame();
    }

    void continueGame()
    {
        this.switchToGame();
    }

    private void switchToGame()
    {
        this.panelSwitcher.show( this.screenHolder, GAME );
    }
    
    void goBackFromOptions()
    {
        this.panelSwitcher.show( this.screenHolder, this.panelCalledOptions );
    }
    
    void switchToOptions(String caller)
    {
        this.panelCalledOptions = caller;
        
        this.panelSwitcher.show( this.screenHolder, OPTIONS );
    }

    void switchToMenu()
    {
        this.panelSwitcher.show( this.screenHolder, MENU );
    }

    void gameOver( long score )
    {
        this.switchToMenu();
        this.menuPanel.addScore( score );
    }

    

    private void createGuiFullScreen()
    {
        Image img = new ImageIcon(GameStart.class.getResource( GameConstants.ICON_IMAGE_PATH )).getImage();
        this.setIconImage(img);
        
        this.setUndecorated( true );
        this.setBounds( 0, 0, GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT );

        this.add( screenHolder );
        screenHolder.setPreferredSize( new Dimension( GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT ) );

        screenHolder.add( this.gamePanel, GAME );
        screenHolder.add( this.menuPanel, MENU );
        screenHolder.add( this.optionsPanel, OPTIONS );

        this.setLocationRelativeTo( null );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setResizable( false );
        this.switchToMenu();
    }

    private void createGuiWindow()
    {
        Image img = new ImageIcon(GameStart.class.getResource( GameConstants.ICON_IMAGE_PATH )).getImage();
        this.setIconImage(img);
        
        this.add( screenHolder );
        screenHolder.setPreferredSize( new Dimension( GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT ) );

        screenHolder.add( this.gamePanel, GAME );
        screenHolder.add( this.menuPanel, MENU );
        screenHolder.add( this.optionsPanel, OPTIONS );

        this.pack();
        this.setLocationRelativeTo( null );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setResizable( false );
        this.switchToMenu();
    }

    
}
