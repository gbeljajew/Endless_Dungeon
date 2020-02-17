/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero.skill;

import game.Game;
import game.GameConstants;
import game.GameLocale;
import game.panels.shop.ShopItem;
import game.util.drawable.SizedDrawable;
import java.util.Objects;

/**
 *
 * @autor gbeljajew
 */
public class BasicSkillShopItem implements ShopItem
{
    private final SizedDrawable icon;
    private final String localiKey;
    private final int price0;
    private final int priceLvl;
    private final String skillKey;

    public BasicSkillShopItem(SizedDrawable icon, String localiKey, int price0, int priceLvl, String skillKey)
    {
        this.icon = icon;
        this.localiKey = localiKey;
        this.price0 = price0;
        this.priceLvl = priceLvl;
        this.skillKey = skillKey;
    }
    
    @Override
    public SizedDrawable getIcon()
    {
        return this.icon;
    }

    @Override
    public String getName()
    {
        return GameLocale.getString(localiKey + ".name");
    }

    @Override
    public String getDescription()
    {
        return GameLocale.getString(localiKey + ".description");
    }

    @Override
    public int detPrice()
    {
        int skillLevel = Game.getHero().getSkillLevel(this.getKey());
        
        return this.price0 + this.priceLvl * skillLevel;
    }

    @Override
    public String getKey()
    {
        return this.skillKey;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.localiKey);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final BasicSkillShopItem other = (BasicSkillShopItem) obj;
        if (!Objects.equals(this.localiKey, other.localiKey))
        {
            return false;
        }
        return true;
    }
}
