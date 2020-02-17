/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.battle.enemy.Monster;
import game.dungeon.FloorType;
import game.hero.HeroClass;
import game.util.container.RandomSet;
import game.util.logger.MyLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * global container for game stuff.
 * @author gbeljajew
 */
public class GameResources
{
    private static final RandomSet<FloorType> FLOOR_TYPES = new RandomSet<>();
    private static final List<HeroClass> HERO_CLASSES = new ArrayList<>();
    
    
    public static FloorType getRandomFloorType()
    {
        return FLOOR_TYPES.get();
    }

    public static List<HeroClass> getHeroClasses()
    {
        return HERO_CLASSES;
    }

    private static void initFlorTypes()
    {
//        Image[] images = Glob.getTiles( "graphic/floor_cave.png", 48, 48 );
//
//        FLOOR_TYPES.add( new FloorType( images, 1, 3, 3, 7, getMiskImage( GameConstants.ICON_KEY_BACKGROUND_CAVE ) ) );
        
        
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            
            Document document = builder.parse(Monster.class.getResourceAsStream("/data/floors.xml"));
            Element root = document.getDocumentElement();
            
            NodeList mon = root.getElementsByTagName("floor");
            
            
            for(int i = 0; i < mon.getLength(); i++)
            {
                try
                {
                    FLOOR_TYPES.add(new FloorType(mon.item(i)));
                } catch (Exception ex)
                {
                    MyLogger.error("Error while creating a floor", ex);
                }
            }
            
            
        } catch (ParserConfigurationException | SAXException | IOException ex)
        {
            MyLogger.error("erroe while reading floors.xml", ex);
        } 
        
        
        
        
    }
    
    private static void initHeroClasses()
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse( Monster.class.getResourceAsStream( "/data/hero_classes.xml" ) );
            Element root = document.getDocumentElement();

            NodeList mon = root.getElementsByTagName( "hero" );

            for( int i = 0; i < mon.getLength(); i++ )
            {
                Node item = mon.item( i );

                try
                {
                    HeroClass heroClass = new HeroClass( item );
                    HERO_CLASSES.add( heroClass );

                    System.out.println( "hero class generated: " + heroClass.getClassName() );

                }
                catch( Exception ex )
                {
                    MyLogger.error( "some errors while parsing heroes node", ex );
                }
            }
        }
        catch( ParserConfigurationException | SAXException | IOException ex )
        {
            MyLogger.error( "some errors while parsing heroes.xml", ex );
        }

    }
    
    public static void init()
    {
        initFlorTypes();
        initHeroClasses();
    }
}
