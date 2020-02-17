/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.dungeon.cristall_content;

import game.Game;
import game.GameState;
import game.battle.Battle;
import game.battle.BattleDialog;
import game.dungeon.FloorType;
import game.items.Item;
import game.items.ItemFactory;
import game.panels.shop.ShopFactory;
import game.util.container.RandomSet;
import game.util.container.Randomizable;
import game.util.drawable.Animation;
import game.util.logger.MyLogger;

/**
 *
 * @author gbeljajew
 */
public class CristallContentFactory
{
    private static final RandomSet<EventType> CRISTALL_CONTENT_RANDOMISER = new RandomSet<>();
    
    static
    {   // TODO chances and place are not final
        CRISTALL_CONTENT_RANDOMISER.add(EventType.TRAP);
        CRISTALL_CONTENT_RANDOMISER.add(EventType.FIGHT);
        CRISTALL_CONTENT_RANDOMISER.add(EventType.SHOP);
        CRISTALL_CONTENT_RANDOMISER.add(EventType.TREASURE);
        CRISTALL_CONTENT_RANDOMISER.add(EventType.EMPTY);
        CRISTALL_CONTENT_RANDOMISER.add(EventType.EXP);
    }
    
    
    
    public static CristallAction createRandomCristallAction(FloorType type)
    {
        return CRISTALL_CONTENT_RANDOMISER.get().generateAction();
    }
}

enum EventType implements Randomizable
{
    TRAP(1) {
        @Override
        CristallAction generateAction()
        {
            return new TrapCristall();
        }
    }, TREASURE(1) {
        @Override
        CristallAction generateAction()
        {
            return new ItemCristall(ItemFactory.getItem());
        }
    }, FIGHT(1) {
        @Override
        CristallAction generateAction()
        {
            return new FightCristall();        
        }
    }, SHOP(1) {
        @Override
        CristallAction generateAction()
        {
            return new ShopCristall();
        }
    }, EMPTY(1) {
        @Override
        CristallAction generateAction()
        {
            return new EmptyCristall();
        }
    }, EXP(1) {
        @Override
        CristallAction generateAction()
        {
            return new ExpCristall();
        }
    };

    private EventType(double chance)
    {
        this.chance = chance;
    }

    private final double chance;
    
    abstract CristallAction generateAction();
    
    @Override
    public double getChance()
    {
        return chance;
    }
}



class EmptyCristall implements CristallAction
{

    @Override
    public Animation cristallTouched()
    {
        //JOptionPane.showMessageDialog(null, "There was nothing inside");
        
        System.out.println("nothing");
        
        return null;
    }
}

class TrapCristall implements CristallAction
{

    @Override
    public Animation cristallTouched()
    {
        
        int dificulty = Game.getCurrentDungeonLevel() - 2 + (int)(Math.random() * 5);
        
        if(dificulty < 1)
            dificulty = 1;
        
        int damage = Game.getCurrentDungeonLevel() + dificulty + 5;
        
        
        return Game.getHero().sprungTrap(damage, dificulty); // TODO ajust damage, 
        // TODO different traps with different animations and diferent properties
        
        
    }
    
}

class ItemCristall implements CristallAction
{
    private final Item item;

    public ItemCristall(Item item)
    {
        this.item = item;
    }
    
    

    @Override
    public Animation cristallTouched()
    {
        Game.getHero().foundItem(this.item);
        
        return new TreasureEvent(this.item);
    }
    
}

class FightCristall implements CristallAction
{

    @Override
    public Animation cristallTouched()
    {
        // TODO:DEBUG
        System.out.println("FIGHT");   
        Game.setGameState(GameState.FIGHT);
        
        BattleDialog battleDialog;
        try
        {
            battleDialog = new BattleDialog(new Battle());
            battleDialog.setVisible(true);
        } catch (Exception ex)
        {
            MyLogger.error("failed to instatiate Battle", ex);
        }
        
        
         Game.setGameState(GameState.MAP);
        // TODO
        return null;
    }
    
}

class ShopCristall implements CristallAction
{

    @Override
    public Animation cristallTouched()
    {
        // TODO:DEBUG
        System.out.println("SHOP");   
        
        ShopFactory.showShop();
       
        
        return null;
    }
    
}

class ExpCristall implements CristallAction
{

    @Override
    public Animation cristallTouched()
    {
        int xp = (int)(Math.random() * 5 * Game.getCurrentDungeonLevel())
                + Game.getCurrentDungeonLevel() * 5;
        
        System.out.println("EXP: " + xp);
        
        return Game.getHero().addExp(xp);
        
        
    }
}