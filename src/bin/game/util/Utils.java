/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.util;

import bin.game.util.logger.MyLogger;

/**
 *
 * @author gbeljajew
 */
public class Utils 
{
    /**
     * generates a random int between min and max (both inclusive)
     * @param min
     * @param max
     * @return 
     * 
     * @throws IllegalArgumentException if max &lt; min.
     */
    public static int generateRandomInt(int min, int max)
    {
        if(max < min)
        {
            String s = "max<min (max=" + max + ", min=" + min + ")" ;
            
            IllegalArgumentException ex = new IllegalArgumentException(s);
            MyLogger.error(s, ex);
            throw ex;
        }
        
        
        int dif = max - min +1;
        
        int res;
        
        do
        {
            res = (int)(Math.random() * dif + min);
        }while(res > max);
        
        
        return res;
    }
}
