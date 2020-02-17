/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.dungeon.Direction;
import game.hero.skill.ActiveSkillCore;
import game.items.Item;
import game.panels.shop.ShopItem;
import game.util.container.RandomKey;
import game.util.drawable.Animation;
import java.util.List;

/**
 *
 * @autor gbeljajew
 */
public interface HeroInterface extends StatOwner, SkillOwner, PotionOwner
{

    boolean buy(ShopItem item, int price);

    Animation sprungTrap(int damage, int dificulty, ActiveSkillCore.AttackProperty... attackProperty);

    void foundItem(Item item);

    void foundSkill(ShopItem item);

    /**
     *
     * @return all skills, activa and passive for Skill Shop.
     */
    List<RandomKey> getAllKnownSkills();

    PotionPouch getBoostPotionPouch();

    BarInfo getExpBarInfo();

    HeroFigure getFigure();

    long getGoldAmount();

    PotionPouch getHpPotionPouch();

    BarInfo getHealthBarInfo();

    HeroClass getHeroClass();

    int getLvl();

    

    BarInfo getManaBarInfo();

    PotionPouch getManaPotionPouch();


    PotionPouch getMunitionPouch();

    String getName();

    long getScore();

    

    @Override
    int getSkillLevel(String key);

    PotionPouch getSpeedPotionPouch();

    @Override
    Stat getStat(StatsEnum key);

    void going(Direction dir);

    boolean isHeroAlive();

    Animation addExp(int xp);

    void stop();

    void update();
    
}
