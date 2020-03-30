/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.container;

/**
 * default implementation for random key.
 * @author gbeljajew 
 */
public class RandomKeyImpl implements RandomKey
{

    private final String key;
    private final double chance;

    public RandomKeyImpl(String key, double chance)
    {
        this.key = key;
        this.chance = chance;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public double getChance()
    {
        return this.chance;
    }
}
