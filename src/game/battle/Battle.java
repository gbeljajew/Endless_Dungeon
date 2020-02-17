/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle;

import java.awt.Point;

import game.Game;
import game.battle.enemy.Contrahent;
import game.battle.enemy.MonsterAi;
import game.battle.enemy.MonsterFactory;
import game.hero.skill.ActiveSkillCore;
import game.util.Animations;
import game.util.drawable.Animation;
import game.util.drawable.SizedDrawable;

/**
 *
 * @autor gbeljajew
 */
public class Battle
{
    
    private final Contrahent hero, monster;
    private final SizedDrawable monsterImage;
    private Animation animation;
    
    private volatile BattlePhase battlePhase = BattlePhase.WAIT;
    private final MonsterAi monsterAi;
    
    public Battle() throws Exception
    {
        String monsterKey = Game.getCurrentFloorType().getMonsterKey();
        this.monsterAi = MonsterFactory.getMonster(monsterKey);
        this.monster = monsterAi.getBattleCore();
        this.monsterImage = MonsterFactory.getMonsterImage(monsterKey);
        
        hero = Game.getHero().getheroBattleCore();
    }
    

    SizedDrawable getMonsterImage()
    {
        return this.monsterImage;
    }

    void setMosterHitPosition(Point point)
    {
        this.monster.setHitPosition(point);
    }

    void setMonsterMissPosition(Point point)
    {
        this.monster.setMissPosition(point);
    }

    void setHeroHitPosition(Point point)
    {
        this.hero.setHitPosition(point);
    }

    void setHeroMissPosition(Point point)
    {
        this.hero.setMissPosition(point);
    }

    Animation getCurrentAnimation()
    {
        return this.animation;
    }

    synchronized void update()
    {
        if(this.animation == null)
            this.battlePhase = BattlePhase.WAIT;
        
        if(this.battlePhase == BattlePhase.ANIMATION)
        {
            if(this.animation.isDone())
            {
                this.animation = null;
                this.battlePhase = BattlePhase.WAIT;
                
                if(!(hero.isAlive() && monster.isAlive()))
                    this.battlePhase = BattlePhase.BATTLE_ENDED;
                
                
            }
        }
        
        if(this.battlePhase == BattlePhase.WAIT)
        {
            hero.update();
            monster.update();
            
            if(hero.isReady())
            {
                this.battlePhase = BattlePhase.HERO_CONTROL;
                this.hero.turnPassed();
            }
            else if(monster.isReady())
            {
                this.monster.turnPassed();
                ActiveSkillCore nextSkill = this.monsterAi.decide(hero);
                
                if(nextSkill == null)
                {
                    this.animation = Animations.getAnimation("CONFUSED");
                    this.battlePhase = BattlePhase.ANIMATION;
                }
                else
                {
                    this.animation = nextSkill.use(monster, hero);
                    this.battlePhase = BattlePhase.ANIMATION;
                }
            }
            
        }
        
    }

    Contrahent getMonster()
    {
        return this.monster;
    }

    Contrahent getHero()
    {
        return this.hero;
    }

    synchronized void useSkill(ActiveSkillCore skill)
    {
        this.animation = skill.use(hero, monster);
        this.battlePhase = BattlePhase.ANIMATION;
        this.hero.doneWithTurn();
    }
    
    synchronized boolean needShowHeroControl()
    {
        return this.battlePhase == BattlePhase.HERO_CONTROL;
    }

    synchronized boolean battleEnded()
    {
        return this.battlePhase == BattlePhase.BATTLE_ENDED;
    }
    
    String getResult()
    {
        String res;
        if(hero.isAlive())
        {
            res = String.format("you got:%n%d Gold%n%dEXP", 
                    monster.getGoldForKilling(),
                    monster.getExpForKilling());
        }
        else
        {
            res = "you died!";
        }
        
        return res;
    }

    void collectSpoils()
    {
        if(hero.isAlive())
        {
            Game.getHero().addExp(monster.getExpForKilling());
            Game.getHero().addGold(monster.getGoldForKilling());
        }
    }
    
    private enum BattlePhase
    {
        WAIT, ANIMATION, HERO_CONTROL, BATTLE_ENDED;
    }
    
}
