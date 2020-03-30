/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.container;

/**
 * purpose of this interface is to bundle some random stuff together, 
 * get one random key and get needed item from Factory.
 * @author gbeljajew
 */
public interface RandomKey extends Randomizable
{
    public String getKey();
}
