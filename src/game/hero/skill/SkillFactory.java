/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.GameConstants;
import game.util.ResourcesContainer;
import game.panels.shop.ShopItem;
import game.util.Glob;
import game.util.container.RandomKeyImpl;
import game.util.container.RandomKey;
import game.util.drawable.SimpleSizedDrawable;
import game.util.logger.MyLogger;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gbeljajew
 */
public class SkillFactory
{

    private interface SkillGenerator
    {

        ActiveSkillCore generate();
    }

    private final static Map<String, SkillGenerator> GENERATORS = new HashMap<>();
    private final static Map<String, ShopItem> SHOP_ITEMS = new HashMap<>();
    
    private final static Map<String, RandomKey> SKILL_CHANCES_TO_GET = new HashMap<>();
    private static final List<RandomKey> BUYABLE_SKILLS = new ArrayList<>();

    public static ActiveSkillCore getActiveSkill(String skillKey)
    {
        
        SkillGenerator generator = GENERATORS.get(skillKey);

        if (generator == null)
        {
            MyLogger.warning("have not found specified skill: " + skillKey);
            return null; // TODO return new DummySkill();
        }

        return generator.generate();
        
    }

    public static ShopItem getShopItem(String skillKey)
    {
        
        ShopItem shopItem = SHOP_ITEMS.get(skillKey);

        if (shopItem == null)
        {
            MyLogger.warning("have not found specified shop item: " + skillKey);
            return null; // TODO return new DummySkill();
        }

        return shopItem;
       
    }

    
    public static RandomKey getChanceToGetSkill(String key)
    {
        RandomKey ret = SkillFactory.SKILL_CHANCES_TO_GET.get(key);
        
        if(ret == null)
        {
            MyLogger.warning("Unknown Key for getSkillRandomKey: " + key);
        }
        return ret;
    }

    public static void init()
    {
        loadSkills();
        
        
        
        // --------------------------------------------------------------------
        
        SHOP_ITEMS.put("/MELE", new BasicSkillShopItem(
                new SimpleSizedDrawable(ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
                "menu.mele", 0, 0, "/MELE"));
        SHOP_ITEMS.put("/MAGIC", new BasicSkillShopItem(
                new SimpleSizedDrawable(ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
                "menu.magic", 0, 0, "/MAGIC"));
        SHOP_ITEMS.put("/SUPPORT", new BasicSkillShopItem(
                new SimpleSizedDrawable(ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
                "menu.support", 0, 0, "/SUPPORT"));
        SHOP_ITEMS.put("/EXTRA", new BasicSkillShopItem(
                new SimpleSizedDrawable(ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[0]),
                "menu.extra", 0, 0, "/EXTRA"));

        // ----- Skills -------------------------------------------------------
        
        SkillFactory.GENERATORS.put("#SLASH", ()-> new Slash());
        SkillFactory.GENERATORS.put("#FIREBALL", ()-> new Fireball());
        SkillFactory.GENERATORS.put("#HEAL", ()-> new Heal());
        SkillFactory.GENERATORS.put("#LEACH", ()-> new Leach());
        SkillFactory.GENERATORS.put("#BITE", ()-> new Bite());
        
       
        
        // ----- Skill Random Key ---------------------------------------------
        
        for (RandomKey skill : BUYABLE_SKILLS)
        {
            SkillFactory.SKILL_CHANCES_TO_GET.put(skill.getKey(), skill);
        }
       
        
    }
    
    private static void loadSkills()
    {

        // "data/world_skill_list.dat" a list of skills, that can be learned
        try( InputStream dataStream = Glob.class.getClassLoader().getResourceAsStream( "data/skill_shop_item_list.dat" );
             BufferedReader br = new BufferedReader( new InputStreamReader( dataStream ) ) )
        {
            String line;

            while( ( line = br.readLine() ) != null )
            {
                // commentar or empty lines.
                if( line.startsWith( "//" ) || line.trim().isEmpty() )
                {
                    continue;
                }

                String[] split = line.split( ";" );

                if( split.length != 6 )
                {
                    MyLogger.debug( "strange line in data/skill_shop_item_list.dat : " + line );
                    continue;
                }

                try
                {
                    
                    double chance = Double.parseDouble( split[ 5 ].trim() );
                    String key = split[ 0 ].trim();
                    String lokalKey = split[1];
                    int index = Integer.parseInt(split[2]);
                    int p0 = Integer.parseInt(split[3]);
                    int plvl = Integer.parseInt(split[4]);
                    
                    SHOP_ITEMS.put(key, new BasicSkillShopItem(
                            new SimpleSizedDrawable(ResourcesContainer.getMiskTileSet(GameConstants.TILESET_SKILLS)[index]), 
                            lokalKey, p0, plvl, key));
                    BUYABLE_SKILLS.add(new RandomKeyImpl(key , chance ) );
                }
                catch( NumberFormatException ex )
                {
                    MyLogger.error( "Something got wrong with loading data/skill_shop_item_list.dat. line: " + line, ex );
                }

            }

            // TODO:DEBUG
//            System.out.println( "skills: " + BUYABLE_SKILLS.size() );
        }
        catch( IOException ex )
        {
            MyLogger.error( "skill load failed", ex );
        }

        // TODO:DEBUG
//        PassiveSkillOld[] values = PassiveSkillOld.values();
//        for( PassiveSkillOld value : values )
//        {
//            System.out.println( value );
//        }

    }

    public static List<RandomKey> getBuyableSkills()
    {
        return BUYABLE_SKILLS;
    }


}


