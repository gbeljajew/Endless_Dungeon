/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.logger;

/**
 * LogListener for debug purposes. it will write log to console.
 * @author gbeljajew
 */
public class SoutLogListener implements LogListener
{

    @Override
    public void talk(String txt)
    {
        System.out.println(txt);
    }
    
}
