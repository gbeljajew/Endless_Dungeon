/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import game.Game;
import game.GameConstants;
import game.util.ResourcesContainer;
import game.util.drawable.AnimatedSizeDrawable;
import game.util.drawable.SimpleCentredSizeDrawable;
import game.util.drawable.SizedDrawable;
import game.util.logger.MyLogger;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @autor gbeljajew
 */
public class MonsterFactory
{
    private static final Map<String, Monster> MONSTERS = new HashMap<>();
    
    
    public static void init()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            
            Document document = builder.parse(Monster.class.getResourceAsStream("/data/monsters.xml"));
            Element root = document.getDocumentElement();
            
            NodeList mon = root.getElementsByTagName("monster");
            
            
            for(int i = 0; i < mon.getLength(); i++)
            {
                Node item = mon.item(i);
                
                Monster monster;
                try
                {
                    monster = new Monster(item);
                    MONSTERS.put(monster.getKey(), monster);
                    
                    //System.out.println("monster generated: " + monster.getKey());
                    
                } catch (Exception ex)
                {
                    MyLogger.error("some errors while parsing monsters.xml", ex);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex)
        {
            MyLogger.error("some errors while parsing monsters.xml", ex);
        } 
    }

    public static MonsterAi getMonster(String monsterKey)
    {
        Monster res = MONSTERS.get(monsterKey);
        
        if(res == null)
        {
            MyLogger.error(monsterKey + " not found in monsters");
            return null;
        }
        
        return new MonsterAiDefaultImpl(res.generateEnemy(Game.getCurrentDungeonLevel()), res) ;
    }
    
    public static SizedDrawable getMonsterImage(String key)
    {
        Monster monster = MONSTERS.get(key);
        String imageKey = monster.getImageKey();
        
        System.out.println(imageKey);
        
        Image miskImage = ResourcesContainer.getMiskImage(imageKey);
        
        if(miskImage == null)
        {
            MyLogger.error(key + " not found in monster images");
            return new AnimatedSizeDrawable(
                    ResourcesContainer.getMiskAnimationSprites(
                            GameConstants.ANIMATION_KEY_BUG));
        }
        
        return new SimpleCentredSizeDrawable(miskImage);
    }
}
