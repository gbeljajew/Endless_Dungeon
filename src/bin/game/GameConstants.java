/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game;

/**
 *
 * @author gbeljajew
 */
public class GameConstants 
{
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    
    public static final int MAP_SCREEN_WIDTH = 576;
    public static final int MAP_SCREEN_HEIGHT = 576;
    
    public static final int HERO_PANEL_HEIGHT = 514;
    public static final int HERO_PANEL_WIDTH = 188;
    
    public static final int BUTTON_PANEL_HEIGHT = 50;
    public static final int BUTTON_PANEL_WIDTH = HERO_PANEL_WIDTH;
    
    /** time between updates. <br>
     * FPS = 1000 / TIMER_PERIOD */
    public static final int TIMER_PERIOD = 20;
    
    /** how many frames one animation frame is shown */
    public static final int STANDARD_ANIMATION_DELAY = 5;
    
    /** width of a floor tile */
    public static final int TILE_WIDTH = 48;
    /** height of a floor tile */
    public static final int TILE_HEIGHT = 48;
    
    public static final int MIN_ROOM_SIZE = 11;
    public static final int MAX_ROOM_SIZE = 15;
    public static final int FLOORS_BETWEEN_PREMADE_MIN = 1;
    public static final int FLOORS_BETWEEN_PREMADE_MAX = 2;
    
    public static final int HERO_TILESET_WIDTH = 144;
    public static final int HERO_TILESET_HEIGT = 256;
    public static final int HERO_SPRITE_WIDTH = 48;
    public static final int HERO_SPRITE_HEIGTH = 64;
    public static final int HERO_FACE_WIDTH = 96;
    public static final int HERO_FACE_HEIGTH = 96;
    
    /** minimal number of ticks before NPC will get new text */
    public static final int TIME_TO_HOLD_ON_TEXT = (1000 / TIMER_PERIOD) * 5; 
    public static final int NPC_MOVEMENT_TICKS_MIN = 5;
    public static final int NPC_MOVEMENT_TICKS_MAX = 50;
    
   public static int getMinRoomsOnFloor()
   {
       // TODO make it based on floor
       return 5;
   }
   
   public static int getMaxRoomsOnFloor()
   {
       // TODO make it based on floor
       return 10;
   }
}
