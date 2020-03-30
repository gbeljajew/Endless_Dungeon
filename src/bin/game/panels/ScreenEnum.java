/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.panels;

/**
 *
 * @author gbeljajew
 */
public enum ScreenEnum
{
    /** starting screen. chose your hero here or create a new one. */
    MAIN_SCREEN, // ?don't bother and start in INN?
    /**  */
    OPTIONS,
    
    /** use your gold and xp to prepare for next run in dungeon */
    MAP,
    /** switch to another hero */
    INN,
    /** upgrade your equipment */
    SMITH,
    /** buy and upgrade your potions */
    ALCHEMIST,
    /** buy new and upgrade own skills */
    SKILL_SHOP,
    /** use xp to upgrade your stats */
    TRAINER,
    /** highscore */
    OBELISQUE,
    
    
    /** create new hero */
    NEW_HERO,
    /** look at hero stats and skills */
    HERO_STATS,
    
    
    /**  */
    GAME_SCREEN,
    /**  */
    FIGHT,
}
