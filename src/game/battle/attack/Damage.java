/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle.attack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import game.battle.enemy.Contrahent;

/**
 *
 *@author gbeljajew
 */
public class Damage
{
    private final Map<ElementeEnum, Double> damage = new HashMap<>();
    private final Map<StatusAligments, Double> statusEffects = new HashMap<>();
    private boolean canBeBlocked;
    
    public void addDamagePart(ElementeEnum element, double damage)
    {
        this.damage.put(element, damage);
    }
    
    public void addStatusAligment(StatusAligments status, double intensity)
    {
        this.statusEffects.put(status, intensity);
    }
    
    public Set<Map.Entry<ElementeEnum, Double>> damageSet()
    {
        return this.damage.entrySet();
    }
    
    public Set<Map.Entry<StatusAligments, Double>> statusAligmentsSet()
    {
        return this.statusEffects.entrySet();
    }
    
    public Damage toCrit(Contrahent me)
    {
        Damage res = new Damage();
        
        for (ElementeEnum element : this.damage.keySet())
        {
            Double d = this.damage.get(element);
            res.addDamagePart(element, me.calculateCritDamage(d));
        }
        
        for (StatusAligments sa : this.statusEffects.keySet())
        {
            Double d = this.statusEffects.get(sa);
            res.addStatusAligment(sa, me.calculateCritDamage(d));
        }
        
        return res;
    }

    public Damage toSuperCrit(Contrahent me)
    {
        Damage res = new Damage();
        
        for (ElementeEnum element : this.damage.keySet())
        {
            Double d = this.damage.get(element);
            res.addDamagePart(element, me.calculateSuperCritDamage(d));
        }
        
        for (StatusAligments sa : this.statusEffects.keySet())
        {
            Double d = this.statusEffects.get(sa);
            res.addStatusAligment(sa, me.calculateSuperCritDamage(d));
        }
        
        return res;
        
    }

    public Damage blocked(Contrahent them)
    {
        Damage res = new Damage();
        
        for (ElementeEnum element : this.damage.keySet())
        {
            Double d = this.damage.get(element);
            res.addDamagePart(element, them.calculateBlockDamage(d));
        }
        
        for (StatusAligments sa : this.statusEffects.keySet())
        {
            Double d = this.statusEffects.get(sa);
            res.addStatusAligment(sa, them.calculateBlockDamage(d));
        }
        
        return res;
    }
    
    public boolean canBeBlocked()
    {
        return this.canBeBlocked;
    }
    
    public void setBlockable(boolean blockable)
    {
        this.canBeBlocked = blockable;
    }
}
