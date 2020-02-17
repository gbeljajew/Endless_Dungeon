package game.panels.shop;

import game.util.drawable.Drawable;
import game.util.drawable.SizedDrawable;
import java.util.List;

/**
 *
 * @author gbeljajew
 */
public interface Shop
{
    public List<ShopItem> getShopItems();
    
    public SizedDrawable getShopOwner();
    
    public String getShopName();
}
