/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @autor gbeljajew
 */
public class SimpleCentredSizeDrawable implements SizedDrawable
{
    private final Image image;
    private final int dx, dy, width, heigth;

    public SimpleCentredSizeDrawable(Image image)
    {
        this.image = image;
        this.width = this.image.getWidth(null);
        this.heigth = this.image.getHeight(null);
        this.dx = this.width / 2 * -1;
        this.dy = this.heigth / 2 * -1;
    }
    
    

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.heigth;
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        g.drawImage(this.image, dx + kameraOffsetX, dy + kameraOffsetY, null);
    }
    
}
