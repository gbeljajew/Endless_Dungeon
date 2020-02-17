/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util;

/**
 * represents amount of something. 
 * @author gbeljajew
 */
public class Amount
{
    private int amount;

    public Amount()
    {
        amount = 0;
    }

    public Amount(int amount)
    {
        this.amount = amount;
    }

    public int getAmount()
    {
        return amount;
    }
    
    public void add(int n)
    {
        this.amount += n;
    }
    
    public void substract(int n)
    {
        this.amount -= n;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
}
