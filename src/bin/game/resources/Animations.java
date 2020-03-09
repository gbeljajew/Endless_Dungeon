/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.resources;

import bin.game.GameConstants;
import bin.game.util.Glob;
import bin.game.util.drawable.DefaultResetableAnimation;
import bin.game.util.drawable.FrameAnimation;
import bin.game.util.drawable.RainAnimation;
import bin.game.util.drawable.RepetableResetableAnimation;
import bin.game.util.drawable.ResetableAnimation;
import bin.game.util.logger.MyLogger;
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
 * @author gbeljajew
 */
public class Animations
{
    private final static Map<String, ResetableAnimation> ANIMATIONS = new HashMap<>();
    private Animations(){}// no constructor
    
    /**
     * get pregenerated animation with key or generate some default animations.
     * @param key 
     * @return  <br>
     * <ol>
     * <li>preset animation if there was one saved with this key</li>
     * <li><code>DefaultResetableAnimation</code> with standard delay if there was a misk animation with this key</li>
     * <li><code>DefaultResetableAnimation</code> with custom delay if key had format: miskAnimationKey;delay </li>
     * <li><code>RepetableResetableAnimation</code> if key had format: miskAnimationKey;delay;repeats</li>
     * <li>a special "bug" animation if all previous things failed.</li>
     * </ol>
     * newly generated animations are stored and awailiable with the same key
     */
    public static ResetableAnimation getAnimation(String key)
    {

        ResetableAnimation ba = ANIMATIONS.get(key);

        if (ba != null)
        {
            ba.reset(); // to be sure nobody forget to reset it
            return ba.cloneYourself();
        }

        // if we do not have this animation, then, maybe, 
        // it is key for misk animation we should use
        // delay should be stadard.
        Image[] miskAnimation = ResourcesContainer.getMiskAnimationSprites(key);
        
        if(miskAnimation != null)
        {
            ba = new DefaultResetableAnimation(miskAnimation, GameConstants.STANDARD_ANIMATION_DELAY);
            
            ANIMATIONS.put(key, ba);
            
            return ba;
        }
        
        // now we have 2 options
        // needed a default animation with custom delay
        // or repeatable animation
        String[] split = key.split(";");
        
        // Default animation with custom delay
        if(split.length == 2)
        {
            try
            {
                int delay = Integer.parseInt(split[1]);
                miskAnimation = ResourcesContainer.getMiskAnimationSprites(split[0]);
                
                if(miskAnimation != null)
                {
                    ba = new DefaultResetableAnimation(miskAnimation, delay);

                    ANIMATIONS.put(key, ba);

                    return ba;
                }
            } catch (NumberFormatException ex)
            {
                MyLogger.warning("some strange key in getBattleAnimation: " + key);
            }
        }
        
        if(split.length == 3)
        {
            try
            {
                int delay = Integer.parseInt(split[1]);
                int repeat = Integer.parseInt(split[2]);
                miskAnimation = ResourcesContainer.getMiskAnimationSprites(split[0]);
                
                if(miskAnimation != null)
                {
                    ba = new RepetableResetableAnimation(miskAnimation, delay, repeat);

                    ANIMATIONS.put(key, ba);

                    return ba;
                }
            } catch (NumberFormatException ex)
            {
                MyLogger.warning("some strange key in getBattleAnimation: " + key);
            }
        }
        
        // comming here means key was bugged
        MyLogger.warning("some strange key in getBattleAnimation: " + key);
        return ANIMATIONS.get("###BUG###");
    }
    
    public static void init()
    {
        ANIMATIONS.put("###BUG###", 
            new RepetableResetableAnimation(
                ResourcesContainer.getMiskAnimationSprites("###BUG###"), 10, 3));
        
        ANIMATIONS.put("#FIREBALL", 
            new DefaultResetableAnimation(
                Glob.getTiles("graphic/fire1.png", 96, 96), 
                    GameConstants.STANDARD_ANIMATION_DELAY));
        
        ANIMATIONS.put("#HEAL", 
            new RainAnimation(
                ResourcesContainer.getMiskAnimationSprites(
                    "heal drop"), 5, 5, 
                    GameConstants.STANDARD_ANIMATION_DELAY));
        
        loadFrameAnimations();
    }

    private static void loadFrameAnimations()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            
            Document document = builder.parse(Animations.class.getResourceAsStream("/data/animations.xml"));
            Element root = document.getDocumentElement();
            
            NodeList mon = root.getElementsByTagName("animation");
            
            
            for(int i = 0; i < mon.getLength(); i++)
            {
                Node item = mon.item(i);
                try
                {
                    FrameAnimation.loadAnimation(item);
                }
                catch(Exception ex)
                {
                    MyLogger.error("Failed to load Animation", ex);
                }
            }
            
            
        } catch (SAXException | IOException | ParserConfigurationException ex)
        {
            MyLogger.error("Trouble while loading animations.xml", ex);
        }
        
    }

    public static void putAnimation(String key, ResetableAnimation res)
    {
        // TODO:DEBUG
        System.out.println("Added animation: " + key);
        
        ANIMATIONS.put(key, res);
    }
}
