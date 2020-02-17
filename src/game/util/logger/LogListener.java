/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.logger;

/**
 *
 * @autor gbeljajew
 */
public interface LogListener
{
    /**
     * called every time a log is written
     * 
     * @param txt  text of log
     */
    public void talk(String txt);
}
