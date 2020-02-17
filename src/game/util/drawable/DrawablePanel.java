/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * a component for displaying a drawable.
 * @autor gbeljajew
 */
public class DrawablePanel extends JPanel
{
    private final SizedDrawable drawable;
    private int dx = 0, dy = 0;
    
    /**
     * 
     * @param drawable we expect this drawable to be drawn with no dx and dy.
     */
    public DrawablePanel(SizedDrawable drawable)
    {
        this.drawable = drawable;
        this.setPreferredSize(new Dimension(drawable.getWidth(), drawable.getHeight()));
    }
    
    /**
     * for drawables, which are drawn shifted to 0,0 position.
     * @param drawable
     * @param dx compensation for shift
     * @param dy 
     */
    public DrawablePanel(SizedDrawable drawable, int dx, int dy)
    {
        this(drawable);
        this.dx = dx;
        this.dy = dy;
    }
    

    @Override
    protected void paintComponent(Graphics grphcs)
    {
        Graphics2D g = (Graphics2D)grphcs;
        this.drawable.draw(g, dx, dy);
        
        this.paintBorder(grphcs);
    }
    
    
}
