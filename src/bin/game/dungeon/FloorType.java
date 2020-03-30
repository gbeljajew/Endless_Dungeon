/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.dungeon;

import bin.game.Game;
import bin.game.GameConstants;
import bin.game.util.Glob;
import bin.game.util.container.RandomSet;
import bin.game.util.container.Randomizable;
import bin.game.util.logger.MyLogger;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @autor gbeljajew
 */
public class FloorType implements Randomizable
{
    /** wall tiles **/
    private Image[] wall;
    /** floor tiles **/
    private Image[] floor;
    private Image[] cluter;
    private Image stairs;
    private Image background;
    private final List<MonsterKey> monsters = new ArrayList<>();
    private String name;
    private double chance;

    /**
     *
     * @param images all tiles
     * @param walls number of wall tiles
     * @param floors number of floor tiles
     * @param clutter number of decorations images
     * @param stair
     * @param background
     */
    public FloorType(Image[] images, int walls, int floors, int clutter, int stair, Image background)
    {
        if (walls + floors + clutter > images.length)
        {
            throw new ArrayIndexOutOfBoundsException("there are less images than needed");
        }

        this.wall = new Image[walls];
        System.arraycopy(images, 0, this.wall, 0, walls);

        this.floor = new Image[floors];
        System.arraycopy(images, walls, this.floor, 0, floors);

        this.cluter = new Image[clutter];
        System.arraycopy(images, walls + floors, this.cluter, 0, clutter);
        
        this.stairs = images[stair];

        this.background = background;
        this.chance = 1;
    }
    
    
    public FloorType(Node node) throws Exception
    {
        if(!node.getNodeName().equals("floor"))
            throw new IllegalArgumentException("wrong node: " + node.getNodeName());
        
        Element el = (Element)node;
        
        this.name = el.getAttribute("name");
        
        int walls, floors, clutter, stair;
        
        Image[] images = Glob.getTiles(el.getAttribute("tiles"), 
                GameConstants.TILE_WIDTH, 
                GameConstants.TILE_HEIGHT);
        
        this.background = Glob.getImage(el.getAttribute("background"));
        
        walls = Integer.parseInt(el.getAttribute("walls"));
        floors = Integer.parseInt(el.getAttribute("floors"));
        clutter = Integer.parseInt(el.getAttribute("clutter"));
        stair = Integer.parseInt(el.getAttribute("stair"));
        
        if (walls + floors + clutter > images.length)
        {
            throw new ArrayIndexOutOfBoundsException("there are less images than needed");
        }
        
        if(walls <= 0 || floors <= 0 || stair < 0 || stair >= images.length)
        {
            throw new ArrayIndexOutOfBoundsException("illegal indexes while reading " + name);
        }

        this.wall = new Image[walls];
        System.arraycopy(images, 0, this.wall, 0, walls);

        this.floor = new Image[floors];
        System.arraycopy(images, walls, this.floor, 0, floors);

        this.cluter = new Image[clutter];
        System.arraycopy(images, walls + floors, this.cluter, 0, clutter);
        
        this.stairs = images[stair];
        
        this.chance = Double.parseDouble(el.getAttribute("chance"));
        
        NodeList monsterNodes = el.getElementsByTagName("mon");
        
        for(int i = 0; i < monsterNodes.getLength(); i++)
        {
            Node item = monsterNodes.item(i);
            
            Element e = (Element)item;
            
            String monKey = e.getAttribute("key");
            int f = Integer.parseInt(e.getAttribute("floor"));
            double rnd = Double.parseDouble(e.getAttribute("rnd"));
            
            this.monsters.add(new MonsterKey(monKey, f, rnd));
        }
        
    }
    
    
    public Image getStairsImage()
    {
        return this.stairs;
    }

    public Image getBaseWallTile()
    {
        return this.wall[0];
    }

    public Image getWallTile()
    {
        if (Math.random() < 0.9)
        {
            return this.wall[0];
        }

        int pos = (int) (Math.random() * this.wall.length);

        return this.wall[pos];
    }

    public Image getFloorTile()
    {
        if (Math.random() < 0.9)
        {
            return this.floor[0];
        }

        int pos = (int) (Math.random() * this.floor.length);

        return this.floor[pos];
    }

    public Image getClutterImage()
    {
        if (this.cluter.length == 0)
        {
            return null;
        }
        if (Math.random() < 0.9)
        {
            return null;
        }

        int pos = (int) (Math.random() * this.cluter.length);

        return this.cluter[pos];
    }

    public Image getBackgroundImage()
    {
        return background;
    }

    public String getMonsterKey()
    {
        
        RandomSet<MonsterKey> rs = new RandomSet<>();
        
        for( MonsterKey monster : this.monsters)
        {
            System.out.println(monster);
            
            
            if(monster.minFloor <= Game.getCurrentDungeonLevel())
                rs.add( monster );
        }
        
        if(rs.size() > 0)
            return rs.get().key;
        
        MyLogger.debug( name + " has no monsters for floor " + Game.getCurrentDungeonLevel() );
        
        return "SLIME";
    }

    public Image getBaseFloorTile()
    {
        return this.floor[0];
    }

    @Override
    public double getChance()
    {
        return this.chance;
    }
    
    private class MonsterKey implements Randomizable
    {
        private final String key;
        private final int minFloor;
        private final double spawnChance; 

        public MonsterKey(String key, int floor, double chance)
        {
            this.key = key;
            this.minFloor = floor;
            this.spawnChance = chance;
        }
        
        
        
        @Override
        public double getChance()
        {
            return this.spawnChance;
        }

        

        @Override
        public String toString()
        {
            return "MonsterKey{" + "key=" + key + ", floor=" + minFloor + ", chance=" + spawnChance + '}';
        }
        
        
        
    }
}
