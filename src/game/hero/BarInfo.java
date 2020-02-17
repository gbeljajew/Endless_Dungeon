/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import java.awt.Color;
import java.awt.Image;

/**
 *
 * @author gbeljajew
 */
public interface BarInfo
{
    int getValue();
    int getMaxValue();
    Image getIcon();
    Color getBarColor();
}
