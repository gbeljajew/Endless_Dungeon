package game.hero;

import game.GameLocale;
import game.hero.skill.ActiveSkillCore;
import game.hero.skill.SkillFactory;
import game.util.ResourcesContainer;
import game.items.Potion;
import game.panels.shop.ShopItem;
import java.awt.Image;

import game.util.NPC_Sprite;
import game.util.RPGM2K_NPC_Sprite;
import game.util.container.RandomKeyImpl;
import game.util.container.RandomContainer;
import game.util.container.RandomKey;
import game.util.container.RandomSet;
import game.util.container.RandomizerWrapper;
import game.util.logger.MyLogger;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HeroClass
{

    private final Image face;

    private final BufferedImage tiles;
    
    private final String classNameLID;
    
    RandomContainer<RandomizerWrapper<StatsEnum>> statsDistributor = new RandomSet<>();
    
    
    
    private List<RandomKey> classSkills = new ArrayList<>();
    private List<KeyInt> startSkills = new ArrayList<>();
    
    private double statPerLevel;
    private double bonusPerLevel;
    private int expPerLvl;
    private KeyInt[] startStats = new KeyInt[StatsEnum.values().length];
    private final List<KeyInt> potionList = new ArrayList<>();
    private int munition;
    private double critModifer;
    private boolean canGetCrit;
    

    

    public HeroClass(Node node)
    {
        if(!node.getNodeName().equals("hero"))
            throw new IllegalArgumentException("wrong node: " + node.getNodeName());
        
        Element el = (Element)node;
        
        this.classNameLID = el.getAttribute("namekey");
        
        this.statPerLevel = Double.parseDouble(el.getAttribute("statperlvl"));
        
        this.bonusPerLevel = Double.parseDouble(el.getAttribute("bonusperlvl"));
        
        this.expPerLvl = Integer.parseInt(el.getAttribute("expperlvl"));
        
        int figureIndex = Integer.parseInt(el.getAttribute("figureindex"));
        String figurekey = el.getAttribute("figurekey");
        
        
        tiles = ResourcesContainer.getMiskTileSet(figurekey)[figureIndex];
        
        
        int faceIndex = Integer.parseInt(el.getAttribute("faceindex"));
        String facekey = el.getAttribute("facekey");
        
        String cm = el.getAttribute("critmod");
        if(!cm.isEmpty())
            this.critModifer = Double.parseDouble( cm );
        else
            this.critModifer = 1;
        
        String cgc = el.getAttribute("cangetcrit");
        
        this.canGetCrit =   "true".equalsIgnoreCase( cgc) || 
                            "yes".equalsIgnoreCase( cgc);
        
        
        this.face = ResourcesContainer.getMiskTileSet(facekey)[faceIndex];
        
        // ----- Stats ---------------------------------------------------
        
        Node statElement = el.getElementsByTagName("stats").item(0);
        
        NodeList statNodes = statElement.getChildNodes();
        
        for(int i = 0; i < statNodes.getLength(); i++)
        {
            Node item = statNodes.item(i);
            if(item.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            Element e = (Element)item;
            
            String tagName = e.getNodeName();
            int sv = Integer.parseInt(e.getAttribute("sv"));
            double rnd = Double.parseDouble(e.getAttribute("rnd"));
            
            StatsEnum se = StatsEnum.valueOf(tagName);
            
            this.startStats[se.ordinal()] = new KeyInt(tagName, sv);
            this.statsDistributor.add(new RandomizerWrapper<>(se, rnd));
            
        }
        
        Node skillElement = el.getElementsByTagName("skills").item(0);
        NodeList skillNodes = skillElement.getChildNodes();
        for(int i = 0; i < skillNodes.getLength(); i++)
        {
            Node item = skillNodes.item(i);
            if(item.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            Element e = (Element)item;
            
            String skillName = e.getAttribute("name");
            int sv = Integer.parseInt(e.getAttribute("sv"));
            double rnd = Double.parseDouble(e.getAttribute("rnd"));
            
            if(sv > 0)
                this.startSkills.add(new KeyInt(skillName, sv));
            
            this.classSkills.add(new RandomKeyImpl(skillName, rnd));
            
            
            // Check if ShopItem and ActiveSkill exist for this skill
            ShopItem shopItem = SkillFactory.getShopItem(skillName);
            if(shopItem == null)
                MyLogger.debug("haven't found ShopItem for skill: " + 
                        skillName + " for HeroClass " + 
                        GameLocale.getString(this.classNameLID));
            
            if(skillName.startsWith("#"))
            {
                ActiveSkillCore activeSkill = SkillFactory.getActiveSkill(skillName);
                
                if(activeSkill == null)
                    MyLogger.debug("haven't found skill object for skill: " + 
                            skillName + " for HeroClass " + 
                            GameLocale.getString(this.classNameLID));
            }
            
            
            
        }
        
        Node potionElement = el.getElementsByTagName("potions").item(0);
        NodeList potionNodes = potionElement.getChildNodes();
        for(int i = 0; i < potionNodes.getLength(); i++)
        {
            Node item = potionNodes.item(i);
            if(item.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            Element e = (Element)item;
            
            String potionName = e.getAttribute("name");
            int amount = Integer.parseInt(e.getAttribute("amount"));
            
            Potion.valueOf(potionName);
            
            if(amount > 0)
                this.potionList.add(new KeyInt(potionName, amount));
            
        }
        
        Element munitionElement = (Element)el.getElementsByTagName("munition").item(0);
        
        this.munition = Integer.parseInt(munitionElement.getAttribute("amount"));
        
    }
    
    
    
    

    public Image getFace()
    {
        return face;
    }

    public NPC_Sprite getHeroBody()
    {
        return new RPGM2K_NPC_Sprite(tiles);
    }

    public String getClassName()
    {
        return GameLocale.getString(classNameLID);
    }
    
    /**
     * how many frames are between two steps
     * @return 
     */
    int getstepTime()
    {
        return 3;
    }
    
    /**
     * how far moves hero with one step.
     * step in both cases is when sprite changes its image.
     * @return 
     */
    int getMovingSpeed()
    {
        return 5;
    }

    RandomContainer<RandomizerWrapper<StatsEnum>> getStatsDistributor()
    {
        return statsDistributor;
    }

    /**
     * 
     * @return 
     */
    public Stat[] getStartStats()
    {
        
        // we have to clon it, so stat objects are not shared between heroes
        Stat[] res = new Stat[this.startStats.length];
        
        for (int i = 0; i < this.startStats.length; i++)
        {
            KeyInt s = this.startStats[i];
            
            res[i] = new Stat(StatsEnum.valueOf(s.key), s.value);
            
        }
       
        return res;
    }

    /**
     * HeroClass has a list of keys for activa and passive skills. those keys are for SkillFactory
     * @return 
     */
    public List<RandomKey> getClassSkills()
    {
        
       return this.classSkills;
    }

    public List<KeyInt> getStartSkills()
    {
        return this.startSkills;
    }

    public List<KeyInt> getStartingPotions()
    {
        return this.potionList;
    }

    public String getClassNameLID()
    {
        return classNameLID;
    }

    public double getStatPerLevel()
    {
        return statPerLevel;
    }

    public double getBonusPerLevel()
    {
        return bonusPerLevel;
    }

    public int getExpPerLvl()
    {
        return expPerLvl;
    }

    public int getMunition()
    {
        return munition;
    }

    boolean canGetCrit()
    {
        return this.canGetCrit;
    }

    double getCritModifer()
    {
        return this.critModifer;
    }
    
    
}
