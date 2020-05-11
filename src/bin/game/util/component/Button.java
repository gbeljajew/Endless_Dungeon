/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.component;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class Button extends JPanel
{

    private int x, y, width, height;
    private final Action action;
    protected Image image;

    @SuppressWarnings(
            {
                "OverridableMethodCallInConstructor", "LeakingThisInConstructor"
            })
    public Button(int x, int y, int width, int height, Image image, Action action, JPanel base)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.action = action;
        this.image = image;

        this.setBounds(x, y, width, height);
        this.generateListener();
    }

    private void generateListener()
    {
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                performAction();
            }

            
        });
    }

    @Override
    public void paint(Graphics g)
    {
        // TODO make decent looking button
        this.setBounds(x, y, width, height);
        
        g.drawImage(image, (width - image.getWidth(null)) / 2, 
                (height - image.getHeight(null)) / 2, null);
        
        g.drawRect(0, 0, width - 1, height - 1);

    }
    
    protected void performAction()
    {
        this.action.performAction();
    }

}
