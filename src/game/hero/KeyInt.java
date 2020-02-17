/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

/**
 *
 * @autor gbeljajew
 */
public class KeyInt
{

    public final String key;
    public final int value;

    public KeyInt(String key, int level)
    {
        this.key = key;
        this.value = level;
    }

    @Override
    public String toString()
    {
        return "KeyInt{" + "key=" + key + ", level=" + value + '}';
    }
}
