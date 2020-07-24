/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.resources;


import bin.game.util.Glob;
import bin.game.util.logger.MyLogger;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
public class GraphicResources
{

    private static final Map<String, Image> MISK_IMAGES = new HashMap<>();

    private static final Map<String, Image[]> MISK_ANIMATIONS = new HashMap<>();

    private static final Map<String, BufferedImage[]> MISK_TILESET = new HashMap<>();
    
    private static final Map<String, BufferedImage> FIGURES = new HashMap<>();
    
    private static boolean done = false;

    
    public static Image[] getMiskAnimationSprites( String key )
    {
        Image[] animationSprites = MISK_ANIMATIONS.get( key );
        
        if(animationSprites == null)
        {
            MyLogger.debug("unknown animation sprites was requestet with key: " + key);
        }
        
        return animationSprites;
    }

    public static BufferedImage[] getMiskTileSet( String key )
    {
        BufferedImage[] tiles = MISK_TILESET.get( key );
        
        if(tiles == null)
        {
            MyLogger.debug("TileSet was requested with unknown key: " + key);
        }
        
        return tiles;
    }

    /**
     * 
     * @param key
     * @return 
     */
    public static BufferedImage getFigur( String key )
    {
        BufferedImage figures = FIGURES.get( key );
        
        if(figures == null)
        {
            MyLogger.debug("Figure was requested with unknown key: " + key);
        }
        
        return figures;
    }
    
    /**
     * get an image.
     *
     * @param key
     * @return
     */
    public static Image getMiskImage( String key )
    {
        Image img = MISK_IMAGES.get( key );
        
        if(img == null)
        {
            MyLogger.debug("Image was requested with unknown key: " + key);
        }
        
        return img;
    }

    //-----------------------------------------------------------------------
    
    public static void init()
    {
        if(done == true) //no need to do it more than once.
            return;
        
        initImages();
        Animations.init();
        
        done = true;
    }

    private static void initImages()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(GraphicResources.class.getResourceAsStream( "/data/images.pd" ) );
            Element root = document.getDocumentElement();

            NodeList mon = root.getElementsByTagName( "image" );

            for( int i = 0; i < mon.getLength(); i++ )
            {
                Node item = mon.item( i );

                try
                {
                    loadImage( item );
                }
                catch( Exception ex )
                {
                    MyLogger.error( "Error while loading images", ex );
                }

            }

        }
        catch( ParserConfigurationException | SAXException | IOException ex )
        {
            MyLogger.error( "Error while loading images.pd ", ex );
        }
    }

    private static void loadImage( Node node ) throws Exception
    {

        Element el = ( Element )node;

        // ----- Image -----------------------------------------------------
        String name = el.getAttribute( "name" );

        BufferedImage bi = Glob.getBufferedImage( name );

        String key = el.getAttribute( "key" );

        if( !key.isEmpty() )  // no key means we do not want to store it yet.
        {
            MISK_IMAGES.put( key, Glob.toImage( bi ) );
        }

        // ------- Animation ------------------------------------------------
        NodeList animations = el.getElementsByTagName( "animation" );

        for( int i = 0; i < animations.getLength(); i++ )
        {
            Node item = animations.item( i );
            if( !item.getParentNode().equals( node ) )
            {
                continue;                           // this one is for 1 animation per file 
            }
            Element e = ( Element )item;

            int w = Integer.parseInt( e.getAttribute( "w" ) );
            int h = Integer.parseInt( e.getAttribute( "h" ) );
            String k = e.getAttribute( "key" );

            if( !k.isEmpty() && w > 0 && h > 0 )
            {
                MISK_ANIMATIONS.put( k, Glob.getTiles( bi, w, h ) );
            }

        }

        // ----- Tiles ------------------------------------------------------
        NodeList tileNodes = el.getElementsByTagName( "tile" );

        for( int i = 0; i < tileNodes.getLength(); i++ )
        {
            Node item = tileNodes.item( i );
            if( !item.getParentNode().equals( node ) )
            {
                continue;                           // this one is for 1 animation per file 
            }
            Element e = ( Element )item;

            int w = Integer.parseInt( e.getAttribute( "w" ) );
            int h = Integer.parseInt( e.getAttribute( "h" ) );

            BufferedImage[] tiles = Glob.getImageParts( bi, w, h );

            String k = e.getAttribute( "key" );

            if( !k.isEmpty() )
            {
                MISK_TILESET.put( k, tiles );
            }

            // ----- icon ---------------------------------------------
            NodeList iconNodes = e.getElementsByTagName( "icon" );

            for( int j = 0; j < iconNodes.getLength(); j++ )
            {
                Node item1 = iconNodes.item( j );
                Element e1 = ( Element )item1;

                int index = Integer.parseInt( e1.getAttribute( "index" ) );
                String ki = e1.getAttribute( "key" );

                if( !ki.isEmpty() )
                {
                    MISK_IMAGES.put( ki, tiles[ index ] );
                }
            }

            // ----- animations from tile ----------------------------------
            
            NodeList animNodes = e.getElementsByTagName( "animation" );
            for( int j = 0; j < animNodes.getLength(); j++ )
            {
                Node item1 = animNodes.item( j );
                Element e1 = ( Element )item1;
                
                int w1 = Integer.parseInt( e1.getAttribute( "w" ) );
                int h1 = Integer.parseInt( e1.getAttribute( "h" ) );
                int index = Integer.parseInt( e1.getAttribute( "index" ) );
                String k1 = e1.getAttribute( "key" );

                if( !k1.isEmpty() && w1 > 0 && h1 > 0 )
                {
                    MISK_ANIMATIONS.put( k1, Glob.getTiles( tiles[index], w1, h1 ) );
                }
            }
            
            NodeList figureNodes = e.getElementsByTagName( "figure" );

            for( int j = 0; j < figureNodes.getLength(); j++ )
            {
                Node item1 = figureNodes.item( j );
                Element e1 = ( Element )item1;

                int index = Integer.parseInt( e1.getAttribute( "index" ) );
                String ki = e1.getAttribute( "key" );

                if( !ki.isEmpty() )
                {
                    FIGURES.put( ki, tiles[ index ] );
                }
            }
        }

    }

    /**
     * for debug only.
     * to make sure your "/data/images.pd" file is correct and loads all needed images. 
     * @param args 
     */
    public static void main( String[] args )
    {
        initImages();
        
        System.out.println( "number of images            : " + MISK_IMAGES.size() );
        
        for (Map.Entry<String, Image> entry : MISK_IMAGES.entrySet())
        {
            String key = entry.getKey();
            Image value = entry.getValue();
            System.out.printf("    image: %25s  size: %5d X %5d%n", 
                    key, 
                    value.getWidth(null), 
                    value.getHeight(null));
        }
        System.out.println("-------------------------------------------------");
        
        System.out.println( "number of animation sprites : " + MISK_ANIMATIONS.size() );
        
        for (Map.Entry<String, Image[]> entry : MISK_ANIMATIONS.entrySet())
        {
            String key = entry.getKey();
            Image[] value = entry.getValue();
            System.out.printf("    animation: %25s number: %3d  size: %5d X %5d%n", 
                    key, 
                    value.length,
                    value[0].getWidth(null), 
                    value[0].getHeight(null));
        }
        System.out.println("-------------------------------------------------");
        
        System.out.println( "number of tilesets          : " + MISK_TILESET.size() );
        
        for (Map.Entry<String, BufferedImage[]> entry : MISK_TILESET.entrySet())
        {
            String key = entry.getKey();
            BufferedImage[] value = entry.getValue();
            System.out.printf("    tileset: %25s number: %3d  size: %5d X %5d%n", 
                    key, 
                    value.length,
                    value[0].getWidth(null), 
                    value[0].getHeight(null));
        }
        System.out.println("-------------------------------------------------");
    }

}
