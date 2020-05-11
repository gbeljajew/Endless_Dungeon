/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
class HeroSidePanel extends JPanel 
{
    
    @Override
    public void paint(Graphics g)
    {
        g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    }

}
