/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.dungeon;

/**
 *
 * @autor gbeljajew
 */
public class OverlapException extends RuntimeException
{

    public OverlapException(String rooms_may_not_overlap)
    {
        super(rooms_may_not_overlap);
    }
    
}
