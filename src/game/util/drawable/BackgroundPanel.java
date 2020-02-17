package game.util.drawable;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * 
 * @author gbeljajew
 */
public class BackgroundPanel extends JPanel
{
    private Image background;

    public BackgroundPanel(Image background)
    {
        this.background = background;
    }

    public void setBackground(Image background)
    {
        this.background = background;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        
        g.drawImage(background, 0, 0, null);
    }
    
    
    
}
