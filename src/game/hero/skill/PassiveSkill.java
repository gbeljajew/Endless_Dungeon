/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

public class PassiveSkill
{

    private int lvl;
    private final String name;

    public PassiveSkill(String name)
    {
        this(name, 1);
    }

    public PassiveSkill(String name, int lvl)
    {
        this.lvl = lvl;
        this.name = name;
    }

    public void levelSkill()
    {
        this.lvl++;
    }

    public int getLevel()
    {
        return this.lvl;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "PassiveSkill{" + name + ":" + lvl + '}';
    }

}
