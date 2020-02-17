
package game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

/**
 * @author gbeljajew
 */
public class GameConstants 
{
    // OPTIONAL check all those constants and change, if needed.
    
    // a few constants needed for building up game gui.
    /** screen width. */
    public static final int PANEL_WIDTH;
    /** screen high. */
    public static final int PANEL_HEIGHT;
    /** true: this game starts in full screen, false: in a window. */
    public static final boolean FULL_SCREEN_MODE = false;
    
    /** time between updates. <br>
     * FPS = 1000 / TIMER_PERIOD */
    public static final int TIMER_PERIOD = 20;
    
    
    /** maximal number of scores that are saved */
    public static final int MAX_SCORE_ENTRIES = 20;
    
    /** Width of buttons in menu */
    public static final int BUTTON_WIDTH = GameConstants.PANEL_WIDTH / 4 - 5;
    /** Height of buttons in menu */
    public static final int BUTTON_HEIGHT = 20;
    
    
    
    /** path to folder, where this game was started */
    public static final String WORK_PATH;
    
    /** Path to save file for score */
    public static final String SAVE_PATH;
    
    public static final String ICON_IMAGE_PATH = "/graphic/GameSeedIcon.png";
    
    private static final String SAVE_FILE_NAME = "/score.dat";
    public static final int TILE_WIDTH = 48;
    public static final int TILE_HEIGHT = 48;
    public static final int MIN_ROOMS_ON_FLOOR_1 = 5;
    public static final int MAX_ROOMS_ON_FLOOR_1 = 10;
    public static final int MAX_ROOMS_GROW_EVERY_NUMBER_FLOORS = 15;
    public static final int MIN_ROOMS_GROW_EVERY_NUMBER_FLOORS = 20;
    
    public static final int HERO_TILESET_WIDTH = 144;
    public static final int HERO_TILESET_HEIGT = 256;
    public static final int HERO_SPRITE_WIDTH = 48;
    public static final int HERO_SPRITE_HEIGTH = 64;
    public static final int HERO_FACE_WIDTH = 96;
    public static final int HERO_FACE_HEIGTH = 96;
            
    /** max number of cristalls in a room */
    public static final int MAX_CRISTALLS = 16;
    /** minimal number of cristalls in a room */
    public static final int MIN_CRISTALLS = 8;
    
    /** how manyy times you have to repaint before you see next image in animation. */
    public static final int STANDARD_ANIMATION_DELAY = 5;
    
    
    // ----- Options Keys ----------------------------------------
    public static final String OPTION_KEY_LEVELUP = "levelup";
    
    
    
    // ----- Options Values --------------------------------------
    
    /** complet random distributing points on lvlup.  chances are based on your class*/
    public static final String OPTION_VALUE_LEVELUP_FULL_AUTO = "levelup_random";
    /** you get most of points randomely distributed and can add one(or more based on skill) manualy*/
    public static final String OPTION_VALUE_LEVELUP_HALF_AUTO = "levelup_part_random";
    /** kompletely manual distribution of points. */ 
    public static final String OPTION_VALUE_LEVELUP_MANUAL = "levelup_manual";


    // ----- Icon Keys -------------------------------------------
    
    public static final String ICON_KEY_EXP_BAR = "exp book";
    public static final String ICON_KEY_HP_BAR = "HP heart";
    public static final String ICON_KEY_MP_BAR = "mp heart";
    public static final String ICON_KEY_SWORD_ICON = "Sword icon";
    public static final String ICON_KEY_SHILD_ICON = "shild icon";
    public static final String ICON_KEY_WAND_ICON = "wand icon";
    public static final String ICON_KEY_GOLD_ICON = "gold icon";
    public static final String ICON_KEY_SPEED_ICON = "speed icon";
    public static final String ICON_KEY_CLOCK_ICON = "clock icon";

    public static final String ICON_KEY_HP_POTION = "hp potion";
    public static final String ICON_KEY_MP_POTION = "mp potion";
    public static final String ICON_KEY_GREEN_POTION = "green potion";
    public static final String ICON_KEY_GOLD_POTION = "gold potion";

    public static final String ICON_KEY_LVLUP_PLUS_BUTTON = "lvlup plus";
    
    public static final String ICON_KEY_SHOP_PIXY = "pixy";
    public static final String ICON_KEY_SHOP_SUCCUB = "succub";
    
    public static final String ICON_KEY_BACKGROUND_CAVE = "background cave";
    
    public static final String ICON_KEY_THROWING_STONE = "throwing stone";
    public static final String ICON_KEY_THROWING_SHURIKEN = "throwing shuriken";
    public static final String ICON_KEY_THROWING_KNIFE = "throwing knife";
    public static final String ICON_KEY_THROWING_KUNAI = "throwing kunai";
    public static final String ICON_KEY_THROWING_CHAKRAM = "throwing chakram";
    public static final String ICON_KEY_THROWING_SPEAR = "throwing spear";
    public static final String ICON_KEY_THROWING_BOMB = "throwing bomb";
    public static final String ICON_KEY_THROWING_TERMAL_DETONATOR = "throwing detonator";
    
    public static final String ICON_KEY_BUTTON_OPTION = "button options";
    public static final String ICON_KEY_BUTTON_SKILLS = "button skills";
    public static final String ICON_KEY_BUTTON_RETURN_WING = "button return wing";
    public static final String ICON_KEY_BUTTON_RETURN_WING_DISABLED = "button return wing disabled";
    
    public static final String IK_MONSTER_BAT = "monster bat";
    public static final String IK_MONSTER_SKELETON = "monster skeleton";
    
    // ----- Animation Keys ---------------------------------------
    
    public static final String ANIMATION_KEY_EXPLOSION1 = "boom";
    public static final String ANIMATION_KEY_BUG = "###BUG###";
    public static final String ANIMATION_KEY_BITE = "bite";
    public static final String ANIMATION_KEY_FIRE1 = "fire1";
    public static final String ANIMATION_KEY_FIRE2 = "fire2";
    public static final String ANIMATION_KEY_FIRE3 = "fire3";
    public static final String ANIMATION_KEY_FIRE4 = "fire4";
    
    public static final String ANIMATION_KEY_ARROW = "arrow";
    public static final String ANIMATION_KEY_BLOOD = "blood";
    public static final String ANIMATION_KEY_AXE1 = "axe1";
    public static final String ANIMATION_KEY_AXE2 = "axe2";
    public static final String ANIMATION_KEY_HEAL_DROP = "heal drop";
    public static final String ANIMATION_KEY_BOOM1 = "boom1";
    public static final String ANIMATION_KEY_BOOM2 = "boom2";
    public static final String ANIMATION_KEY_CLAW = "claw";
    public static final String ANIMATION_KEY_CUT1 = "cut1";
    public static final String ANIMATION_KEY_CUT2 = "cut2";
    public static final String ANIMATION_KEY_CUT3 = "cut3";
    public static final String ANIMATION_KEY_CUT4 = "cut4";
    public static final String ANIMATION_KEY_CUT5 = "cut5";
    public static final String ANIMATION_KEY_CUT6 = "cut6";
    public static final String ANIMATION_KEY_CUT7 = "cut7";
    public static final String ANIMATION_KEY_CUT8 = "cut8";
    public static final String ANIMATION_KEY_CUT9 = "cut9";
    public static final String ANIMATION_KEY_WHIP1 = "whip1";
    public static final String ANIMATION_KEY_WHIP2 = "whip2";
    
    
    // ----- Tileset Keys ---------------------------------------
    
    public static final String TILESET_SKILLS = "skills";
    public static final String TILESET_THROW_WEAPONS = "throwing weapons";
    public static final String TILESET_HEOR_FIGURES = "chars_tiles";
    public static final String TILESET_HERO_FACES = "faces_tiles";
    
    static
    {
        // OPTIONAL if you want you can load those constants from config.
        
        if(FULL_SCREEN_MODE)
        {
            Dimension desctop = Toolkit.getDefaultToolkit().getScreenSize();
            PANEL_WIDTH = desctop.width;
            PANEL_HEIGHT = desctop.height;
        }
        else
        {
            PANEL_WIDTH = 800;
            PANEL_HEIGHT = 600;
        }
        
        URI location;
        String workPath = ".";
        try
        {
            location = GameConstants.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            
            Path path = Paths.get( location).getParent();
            
            workPath = path.toAbsolutePath().toString();
        }
        catch( URISyntaxException ex )
        {
            // i didn't used here method from game.util.Utils because i did not want to create circular dependances.
            JOptionPane.showMessageDialog( null, 
                    ex, 
                    GameLocale.getString( "error.generic_error"), 
                    JOptionPane.ERROR_MESSAGE);
            
        }
        
        WORK_PATH = workPath;
        
        SAVE_PATH = WORK_PATH + SAVE_FILE_NAME;
        
    }

}
