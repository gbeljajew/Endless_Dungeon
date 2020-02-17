/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.Game;
import game.GameConstants;
import game.util.ResourcesContainer;
import game.GameState;
import game.battle.enemy.Contrahent;
import game.dungeon.Direction;
import game.hero.skill.ActiveSkillCore;
import game.hero.skill.BasicSkillShopItem;
import game.hero.skill.SkillFactory;
import game.items.ArtefactItem;
import game.items.GoldItem;
import game.items.Item;
import game.items.JewelryItem;
import game.items.MunitionItem;
import game.items.Potion;
import game.panels.shop.ShopItem;
import game.util.Animations;
import game.util.container.RandomKey;
import game.util.drawable.Animation;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Hero implements HeroInterface
{

    private final HeroCore core;
    private final HeroClass heroClass;
    private final HeroFigure figure;
    private final String name;

    private final PotionPouch hpPotionPouch = new PotionPouchImpl(
            GameConstants.ICON_KEY_HP_POTION, Potion.HP_POTION);

    private final PotionPouch manaPotionPouch = new PotionPouchImpl(
            GameConstants.ICON_KEY_MP_POTION, Potion.MP_POTION);

    private PotionPouch sppedPotionPouch = new PotionPouchImpl(
            GameConstants.ICON_KEY_GREEN_POTION, Potion.SPEED_POTION);

    private final PotionPouch boostPotionPouch
            = new PotionPouchImpl(GameConstants.ICON_KEY_GOLD_POTION, Potion.BOOST_POTION);
    private long gold;
    private long score;

    public Hero(HeroClass heroClass, String name)
    {
        this.heroClass = heroClass;
        this.name = name;

        this.core = new HeroCore(heroClass);
        this.figure = new HeroFigure(heroClass);
        this.core.setName(name);
    }

    public Contrahent getheroBattleCore()
    {
        return this.core;
    }

    @Override
    public boolean buy(ShopItem item, int price)
    {
        if(this.getGoldAmount() < price)
            return false;
        
         this.gold -= price;
        
        if(item instanceof Item)
            this.foundItem((Item)item);
        
        if(item instanceof BasicSkillShopItem)
            this.foundSkill(item);
        
        return true;
    }

    @Override
    public Animation sprungTrap(int damage, int dificulty, ActiveSkillCore.AttackProperty... attackProperty)
    {
        double dmg = damage;
        
        if (this.core.tryEvadeTrap(dificulty, attackProperty))
        {
            return Animations.getAnimation("TRAP_EVADE");
        }

        if (this.core.tryBlockTrap(dificulty, attackProperty))
        {
            dmg = this.core.calculateBlockDamage(damage);
        }

        this.core.trapDamage((int)dmg, attackProperty);
        
        return Animations.getAnimation("BOOM");
    }

    @Override
    public void foundItem(Item item)
    {
        //TODO:DEBUG
        System.out.println("Found Item: " + item.getClass().getSimpleName());

        if (item instanceof Potion)
        {
            this.core.addPotion((Potion) item);
        } else if (item instanceof JewelryItem)
        {
            this.stashJewelry((JewelryItem) item);
        } else if (item instanceof GoldItem)
        {
            this.stashGold((GoldItem) item);
        } else if (item instanceof ArtefactItem)
        {
            this.stashAtrefact((ArtefactItem) item);
        } else if (item instanceof MunitionItem)
        {
            this.core.stashMunition((MunitionItem) item);
        }
    }

    @Override
    public void foundSkill(ShopItem item)
    {
        this.core.addSkill(item.getKey());
    }

    @Override
    public List<RandomKey> getAllKnownSkills()
    {
        List<RandomKey> res = new ArrayList<>();

        List<String> ownedSkills = this.core.getOwnedSkills();
        for (String ownedSkill : ownedSkills)
        {
            RandomKey chanceToGetSkill = SkillFactory.getChanceToGetSkill(ownedSkill);

            if (chanceToGetSkill != null)
            {
                res.add(chanceToGetSkill);
            }
        }
        return res;
    }

    @Override
    public PotionPouch getBoostPotionPouch()
    {
        return this.boostPotionPouch;
    }

    @Override
    public BarInfo getExpBarInfo()
    {
        return this.core.getExpBar();
    }

    @Override
    public HeroFigure getFigure()
    {
        return this.figure;
    }

    @Override
    public long getGoldAmount()
    {
        return this.gold;
    }

    @Override
    public PotionPouch getHpPotionPouch()
    {
        return this.hpPotionPouch;
    }

    @Override
    public BarInfo getHealthBarInfo()
    {
        return this.core.getHpBar();
    }

    @Override
    public HeroClass getHeroClass()
    {
        return this.heroClass;
    }

    @Override
    public int getLvl()
    {
        return this.core.getLevel();
    }

    @Override
    public BarInfo getManaBarInfo()
    {
        return this.core.getMpBar();
    }

    @Override
    public PotionPouch getManaPotionPouch()
    {
        return this.manaPotionPouch;
    }

    @Override
    public PotionPouch getMunitionPouch()
    {
        return this.munitionsPouch;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public long getScore()
    {
        return this.score;
    }

    @Override
    public int getSkillLevel(String key)
    {
        return this.core.getSkillLevel(key);
    }

    @Override
    public PotionPouch getSpeedPotionPouch()
    {
        return this.sppedPotionPouch;
    }

    @Override
    public Stat getStat(StatsEnum key)
    {
        return this.core.getStat(key);
    }

    @Override
    public void going(Direction dir)
    {
        figure.going(dir);
    }

    @Override
    public boolean isHeroAlive()
    {
        return this.core.getHP() > 0;
    }

    @Override
    public Animation addExp(int xp)
    {
        return this.core.addExp(xp);
    }

    @Override
    public void stop()
    {
        this.figure.stop();
    }

    @Override
    public void update()
    {
        if (Game.getGameState() == GameState.MAP)
        {
            this.figure.update();
        }
    }

    @Override
    public void addSkill(String key)
    {
        this.core.addSkill(key);
    }

    @Override
    public List<String> getOwnedSkills()
    {
        return this.core.getOwnedSkills();
    }

    @Override
    public List<ActiveSkillCore> getActiveSkills()
    {
        return this.core.getActiveSkills();
    }

    @Override
    public List<ActiveSkillCore> getSkills(ActiveSkillCore.SkillType skillType)
    {
        return this.core.getSkills(skillType);
    }

    @Override
    public int getPotionAmount(Potion potion)
    {
        return this.core.getPotionAmount(potion);
    }

    @Override
    public void usePotion(Potion potion)
    {
        core.usePotion(potion);
    }

    @Override
    public void addPotion(Potion potion)
    {
        core.addPotion(potion);
    }

    private final PotionPouch munitionsPouch = new PotionPouch()
    {
        @Override
        public int getCount()
        {
            return Hero.this.core.getMunitionAmount();
        }

        @Override
        public void drink()
        {
            // will we use it outside battle?
        }

        @Override
        public Image getImage()
        {

            int skillLevel = Hero.this.getSkillLevel("MUNITION_QUALITY");

            Image[] throwingWeapons = ResourcesContainer.getMiskTileSet(GameConstants.TILESET_THROW_WEAPONS);

            int index = skillLevel / 5;

            if (index >= throwingWeapons.length)
            {
                index = throwingWeapons.length - 1;
            }

            return throwingWeapons[index];
        }
    };

    private void stashJewelry(JewelryItem jewelryItem)
    {
        // TODO
        
        this.gold += jewelryItem.getSumm();
        this.score += jewelryItem.getSumm();
    }

    private void stashGold(GoldItem goldItem)
    {
        this.gold += goldItem.getSumm();
        this.score += goldItem.getSumm();
    }

    private void stashAtrefact(ArtefactItem artefactItem)
    {
        // TODO:
    }

    public void addGold(int gold)
    {
        this.gold += gold;
        this.score += gold;
    }

    private class PotionPouchImpl implements PotionPouch
    {

        private final String imageKey;
        private final Potion potion;

        public PotionPouchImpl(String imageKey, Potion potion)
        {
            this.imageKey = imageKey;
            this.potion = potion;
        }

        @Override
        public int getCount()
        {
            return Hero.this.core.getPotionAmount(this.potion);
        }

        @Override
        public void drink()
        {
            Hero.this.core.usePotion(this.potion);
        }

        @Override
        public Image getImage()
        {
            return ResourcesContainer.getMiskImage(this.imageKey);
        }

    }
}
