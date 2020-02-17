/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import java.awt.Graphics2D;

/**
 *
 * @autor gbeljajew
 */
@Deprecated
public interface HeroStatusDrawer
{
    public void draw(Graphics2D g);
    public void click(int x, int y);
}
