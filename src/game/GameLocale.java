/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.util.logger.MyLogger;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 *
 * @autor gbeljajew
 */
public class GameLocale
{
    /** Get your localized Strings from here */
    private static final ResourceBundle RESOURCE_BUNDLE = PropertyResourceBundle.getBundle("game.local.MyBundle");
    
    public static String getString(String key)
    {
        String res = key;
        
        try
        {
            res = RESOURCE_BUNDLE.getString(key);
        }
        catch(RuntimeException ex)
        {
            StackTraceElement[] stackTrace = ex.getStackTrace();
            
            StringBuilder sb = new StringBuilder();
            
            for (StackTraceElement ste : stackTrace)
            {
                sb.append(String.format("    %s.%s(%d)%n", ste.getClassName(), ste.getMethodName(), ste.getLineNumber()));
            }
            
            MyLogger.debug("Localized String not found. key: " + key + "\n" + sb.toString());
        }
        
        return res;
    }
}
