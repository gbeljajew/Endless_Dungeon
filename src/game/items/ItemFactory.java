/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import game.util.container.RandomSet;
import game.util.container.Randomizable;

/**
 *
 * @autor gbeljajew
 */
public class ItemFactory
{
    private ItemFactory(){} // no constructor.
    
    private static final RandomSet<ItemType> ITEMS = new RandomSet<>();
    
    static
    {
        ITEMS.add(ItemType.POTION);
        ITEMS.add(ItemType.ARTEFACT);
        ITEMS.add(ItemType.GOLD);
        ITEMS.add(ItemType.MUNITION);
        ITEMS.add(ItemType.TREASURE);
    }
    
    public static Item getItem()
    {
        return ITEMS.get().generateItem();
    }
    
    private enum ItemType implements Randomizable
    {

        POTION {
            @Override
            public double getChance()
            {
                return 3;
            }

            @Override
            public Item generateItem()
            {
                return Potion.getRandomPotion();
            }
        }, 
        GOLD {
            @Override
            public double getChance()
            {
                return 5;
            }

            @Override
            public Item generateItem()
            {
                return new GoldItem();
            }
        }, 
        TREASURE {
            @Override
            public double getChance()
            {
                return 4;
            }

            @Override
            public Item generateItem()
            {
                return new JewelryItem();
            }
        }, 
        ARTEFACT {
            @Override
            public double getChance()
            {
                return 0.1;
            }

            @Override
            public Item generateItem()
            {
                return new ArtefactItem();
            }
        }, 
        MUNITION {
            @Override
            public double getChance()
            {
                return 2;
            }

            @Override
            public Item generateItem()
            {
                return new MunitionItem();
            }
        };

        @Override
        public abstract double getChance();

        public abstract Item generateItem();
    }
}
