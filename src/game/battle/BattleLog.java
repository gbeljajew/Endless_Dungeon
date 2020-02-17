/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @autor gbeljajew
 */
public class BattleLog
{
    private static final List<String> ENTRIES = new ArrayList<>();
    
    
    public static void reset()
    {
        ENTRIES.clear();
    }
    
    public static void log(String template, BattleLogItem... items)
    {
        
        // TODO add color antd template String
        // for now only String is saved.
        
        ENTRIES.add(template);
        
    }
    
    static String getText()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        
        for (String string : ENTRIES)
        {
            sb.append(string);
            sb.append("<br>");
        }
        sb.append("</html>");
        
        return sb.toString();
    }
    
}


