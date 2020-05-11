/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels.blend_in_out;

import bin.game.util.drawable.Animation;

/**
 *
 * @author gbeljajew
 */
public class BlendFactory 
{
    public static Animation getBlendInAnimation()
    {
        // TODO new blend in animations and randomiser
        
        return new BlendInTwoSquaresLeftRight();
    }
    
    public static Animation getBlendOutAnimation()
    {
        // TODO new blend out animations and randomiser
        
        return new BlendOutTwoSquaresRunLeftRight();
    }
}
