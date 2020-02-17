/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.enemy;

import game.GameLocale;
import game.hero.KeyInt;
import game.hero.Stat;
import game.hero.StatsEnum;
import game.hero.skill.ActiveSkillCore;
import game.hero.skill.PassiveSkill;
import game.hero.skill.SkillFactory;
import game.panels.shop.ShopItem;
import game.util.container.RandomKeyImpl;
import game.util.container.RandomSet;
import game.util.logger.MyLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * blueprint for Enemy.
 * @autor gbeljajew
 */
public class Monster 
{
    
    private final int minFloor;
    
    private final double statPerLevel, skillPerLevel, expPerLevel, goldPerLevel;
    
    private final String name;
    
    private final KeyInt [] startSkills;
    private final KeyInt [] startStats = new KeyInt[StatsEnum.values().length];
    private final RandomKeyImpl[] skillsAllRandom;
    private final RandomKeyImpl[] statRandomiser = new RandomKeyImpl[StatsEnum.values().length];
    private final String icon;
    private final String key;
    private final List<RandomKeyImpl> skillsAI_List = new ArrayList<>();
    private final double critModifer;
    private final boolean canGetCrit;

    public Monster(Node node) throws Exception
    {
        
        if(!node.getNodeName().equals("monster"))
            throw new IllegalArgumentException("wrong node: " + node.getNodeName());
        
        
        
        Element el = (Element)node;
        
        String nameKey = el.getAttribute("namekey");
        this.name = GameLocale.getString(nameKey);
        
        this.key = el.getAttribute("key");
        
        String minfloor = el.getAttribute("minfloor");
        this.minFloor = Integer.parseInt(minfloor);
        
        String statperlvl = el.getAttribute("statperlvl");
        this.statPerLevel = Double.parseDouble(statperlvl);
        
        String skillperlvl = el.getAttribute("skillperlvl");
        this.skillPerLevel = Double.parseDouble(skillperlvl);
        
        String expperlvl = el.getAttribute("expperlvl");
        this.expPerLevel = Double.parseDouble(expperlvl);
        
        String godperlvl = el.getAttribute("godperlvl");
        this.goldPerLevel = Double.parseDouble(godperlvl);
        
        this.icon = el.getAttribute("icon");
        
        String cm = el.getAttribute("critmod");
        
        if(!cm.isEmpty())
            this.critModifer = Double.parseDouble( cm );
        else
            this.critModifer = 1;
        
        String cgc = el.getAttribute("cangetcrit");
        
        this.canGetCrit =   "true".equalsIgnoreCase( cgc) || 
                            "yes".equalsIgnoreCase( cgc);
        
        
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
            this.statRandomiser[se.ordinal()] = new RandomKeyImpl(tagName, rnd);
            
        }
        
        List<KeyInt> skillsStart = new ArrayList<>();
        List<RandomKeyImpl> skillsAll = new ArrayList<>();
        
        
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
            
            String ai = e.getAttribute("use");
            
            if(!ai.isEmpty())
            {
                double aiRnd = Double.parseDouble(ai);
                this.skillsAI_List.add(new RandomKeyImpl(skillName, aiRnd));
            }
            else
            {
                this.skillsAI_List.add(new RandomKeyImpl(skillName, 1));
            }
            
            if(sv > 0)
                skillsStart.add(new KeyInt(skillName, sv));
            
            skillsAll.add(new RandomKeyImpl(skillName, rnd));
            
            // Check if ShopItem and ActiveSkill exist for this skill
            ShopItem shopItem = SkillFactory.getShopItem(skillName);
            if(shopItem == null)
                MyLogger.debug("haven't found ShopItem for skill: " + skillName + " for monster " + key);
            
            if(skillName.startsWith("#"))
            {
                ActiveSkillCore activeSkill = SkillFactory.getActiveSkill(skillName);
                
                if(activeSkill == null)
                    MyLogger.debug("haven't found skill object for skill: " + skillName + " for monster " + key);
            }
            
        }
        
        this.skillsAllRandom = skillsAll.toArray(new RandomKeyImpl[0]);
        
        this.startSkills = skillsStart.toArray(new KeyInt[0]);
        
    }
    
    public Contrahent generateEnemy(int floor)
    {
        int minlvl = floor - 5;
        if(minlvl < 1)
            minlvl = 1;
        
        int maxLvl = floor + floor / 2 + 3;
        
        if(floor > 10)
            maxLvl += 1;
        if(floor > 50)
            maxLvl += floor / 50;
        
        
        int lvl = (int)(Math.random() * (maxLvl - minlvl) + minlvl);
        
        // TODO:DEBUG
        System.out.println("level: " + lvl);
        
        MonsterLeveler ml = new MonsterLeveler(
                startSkills, 
                startStats, 
                skillsAllRandom, 
                statRandomiser, 
                statPerLevel, 
                skillPerLevel);
        
        ml.level(lvl);
        
        
        // TODO:DEBUG
        System.out.println(ml.getActiveSkills());
        System.out.println(ml.getPassiveSkills());
        System.out.println(ml.getStats());
        
        return new BasicEnemy(
                ml.getStats(), 
                ml.getActiveSkills(), 
                ml.getPassiveSkills(), 
                name, 
                lvl, 
                (int)(this.expPerLevel * lvl), 
                (int)(this.goldPerLevel * lvl),
                this.critModifer,
                this.canGetCrit);
    }
    
    /**
     * 
     * @return minimal floor from where we able to find those monsters
     */
    public int getMinFloor()
    {
        return this.minFloor;
    }

    public String getKey()
    {
        return this.key;
    }

    String getImageKey()
    {
        return this.icon;
    }

    List<RandomKeyImpl> getSkillAiList()
    {
        return this.skillsAI_List;
    }
    
    
    
    private class MonsterLeveler
    {

        private final Map<StatsEnum, Stat> stats = new HashMap<>();
        private final Map<String, PassiveSkill> passiveSkills = new HashMap<>();
        private final Map<String, ActiveSkillCore> activeSkills = new HashMap<>();

        
        @SuppressWarnings("hiding")
		private final RandomSet<RandomKeyImpl> statRandomiser = new RandomSet<>();
        private final RandomSet<RandomKeyImpl> skillRandomiser = new RandomSet<>();
        
        @SuppressWarnings("hiding")
		private final double statPerLevel, skillPerLevel;

        public MonsterLeveler(KeyInt[] startSkills, KeyInt[] startStats, RandomKeyImpl[] skillsAllRandom, RandomKeyImpl[] statRandom, double statPerLevel, double skillPerLevel)
        {
            
            this.statPerLevel = statPerLevel;
            this.skillPerLevel = skillPerLevel;
            
            this.skillRandomiser.put(skillsAllRandom);
            this.statRandomiser.put(statRandom);
            
            for (KeyInt startSkill : startSkills)
            {
                String key = startSkill.key;
                
                if(key.startsWith("#"))
                {
                    ActiveSkillCore skill = SkillFactory.getActiveSkill(key);
                    skill.setLevel(startSkill.value);
                    this.activeSkills.put(key, skill);
                }
                else
                {
                    PassiveSkill skill = new PassiveSkill(key, startSkill.value);
                    this.passiveSkills.put(key, skill);
                }
            }
            
            for (KeyInt ss : startStats)
            {
                StatsEnum se = StatsEnum.valueOf(ss.key);
                this.stats.put(se, new Stat(se, ss.value));
            }
        }

        public void level(int lvl)
        {
            int statPoints = (int)(this.statPerLevel * lvl);
            int skillPoints = (int)(this.skillPerLevel * lvl);
            
            for(int i = 0; i < statPoints; i++)
            {
                this.addStat(this.statRandomiser.get().getKey());
            }
            
            for (int i = 0; i < skillPoints; i++)
            {
                this.addSkill(this.skillRandomiser.get().getKey());
            }
            
            
            
        }
        
        
        private void addStat(String key)
        {
            StatsEnum se = StatsEnum.valueOf(key);
            
            Stat stat = this.stats.get(se);
            stat.pp();
            stat.useIncrement();
        }
        
        private void addSkill(String key)
        {
            System.out.println("add skill: " + key);
            
            if (key.startsWith("#")) // active skills
            {
                ActiveSkillCore skill = this.activeSkills.get(key);

                if (skill == null)
                {
                    skill = SkillFactory.getActiveSkill(key);
                    this.activeSkills.put(key, skill);
                }

                skill.levelSkill();
            } else // passive skill
            {
                PassiveSkill skill = this.passiveSkills.get(key);

                if (skill == null)
                {
                    skill = new PassiveSkill(key);
                    this.passiveSkills.put(key, skill);
                }

                skill.levelSkill();
            }

        }
        
        public List<ActiveSkillCore> getActiveSkills()
        {
            List<ActiveSkillCore> res = new ArrayList<>(this.activeSkills.values());
            
            return res;
        }
        
        public List<PassiveSkill> getPassiveSkills()
        {
            List<PassiveSkill> res = new ArrayList<>(this.passiveSkills.values());
            
            return res;
        }
        
        public List<Stat> getStats()
        {
            return new ArrayList<>(this.stats.values());
        }
    }
    
    
    
    
    public static void main(String[] args)
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
                
                Monster monster = new Monster(item);
                
                monster.generateEnemy(50);
            }
            
            
        } catch (ParserConfigurationException | SAXException | IOException ex)
        {
            Logger.getLogger(Monster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex)
        {
            Logger.getLogger(Monster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
