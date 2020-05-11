/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.util.component;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class ConditionalButton extends Button
{

    private final Condition condition;
    private final Image disabledImage, activeImage;

    public ConditionalButton(int x, 
                             int y, 
                             int width, 
                             int height, 
                             Image image, 
                             Action action, 
                             JPanel base, 
                             Image disabledImage, 
                             Condition condition)
    {
        super(x, y, width, height, image, action, base);
        
        this.condition = condition;
        this.disabledImage = disabledImage;
        this.activeImage = image;
    }

    @Override
    public void paint(Graphics g)
    {
        if(this.condition.test())
        {
            super.image = this.activeImage;
        }
        else
        {
            super.image = this.disabledImage;
        }
        
        super.paint(g);
    }

    @Override
    protected void performAction()
    {
        if(this.condition.test())
            super.performAction();
    }
    
    

}
