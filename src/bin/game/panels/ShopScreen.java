/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels;

import static bin.game.GameConstants.BUTTON_PANEL_HEIGHT;
import static bin.game.GameConstants.BUTTON_PANEL_WIDTH;
import static bin.game.GameConstants.HERO_PANEL_HEIGHT;
import static bin.game.GameConstants.HERO_PANEL_WIDTH;
import bin.game.dungeon.shop.Shop;
import bin.game.resources.GraphicResources;
import bin.game.util.component.Button;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class ShopScreen extends JPanel
{
    private static final Font FONT = new Font(Font.SERIF, Font.BOLD, 25);
    
    private final Shop shop;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ShopScreen(Shop shop)
    {
        this.shop = shop;
        this.setLayout(null);
        
        ShopOwnerPanel sop = new ShopOwnerPanel();
        this.add(sop);
        sop.setBounds(12, 12, 176, 576);
        
        ShopNamePanel snp = new ShopNamePanel();
        this.add(snp);
        snp.setBounds(212, 12, 376, 50);
        
        JPanel shopPanel = shop.getShopPanel();
        this.add(shopPanel);
        shopPanel.setBounds(212, 74, 376, 526);
        
        HeroSidePanel heroPanel = new HeroSidePanel();
        this.add(heroPanel);
        heroPanel.setBounds(600, 12, HERO_PANEL_WIDTH, HERO_PANEL_HEIGHT);
        
        ButtonPanel bp = new ButtonPanel();
        this.add(bp);
        bp.setBounds(600, 538, BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT);
        
    }
    
    
    
    class ButtonPanel extends JPanel
    {

        @SuppressWarnings("OverridableMethodCallInConstructor")
        public ButtonPanel()
        {
            
            this.add(new Button(4, 4, 57, 42, 
                    GraphicResources.getMiskImage("core.button_skills"), 
                    () -> showSkills(), this));
            
            this.add(new Button(65, 4, 57, 42, 
                    GraphicResources.getMiskImage("core.button_options"), 
                    () -> gotoOptionsScreen(), this));
            
            this.add(new Button(126, 4, 58, 42, 
                    GraphicResources.getMiskImage("core.button_go_outside"), 
                    () -> exitShop(), this));
        }
        

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            
            g.drawRect(0, 0, BUTTON_PANEL_WIDTH - 1, BUTTON_PANEL_HEIGHT - 1);
        }

        private void showSkills()
        {
            ScreenControl.switchScreen(ScreenEnum.SKILLS_AND_STATS);
        }

        private void gotoOptionsScreen()
        {
            ScreenControl.switchScreen(ScreenEnum.OPTIONS);
        }

        private void exitShop()
        {
            shop.afterShop();
            ScreenControl.switchScreen(ScreenEnum.MAP);
        }

    }
    
    class ShopOwnerPanel extends JPanel
    {

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            
            Image shopOwner = shop.getShopOwner();
            
            int imageWidth = shopOwner.getWidth(null);
            int imageHeight = shopOwner.getHeight(null);
            
            int screenWidth = this.getWidth();
            int screenHeight = this.getHeight();
            
            g.drawImage(shopOwner, 
                    (screenWidth - imageWidth) / 2, 
                    (screenHeight - imageHeight) / 2, null);
            
           
        }
        
    }
    
    class ShopNamePanel extends JPanel
    {

        @Override
        public void paint(Graphics g)
        {
            Font backFont = g.getFont();
            g.setFont(FONT);


            FontMetrics metrics = g.getFontMetrics();
            
            String name = shop.getName();
            
            int width = (int) metrics.getStringBounds(name, g).getWidth();
            int height = (int) metrics.getStringBounds(name, g).getHeight();
            
            int x = (this.getWidth() - width) / 2;
            int y = (this.getHeight() + height) / 2;
            
            g.drawString(name, x, y);
            
            g.setFont(backFont);
            
        }
        
    }
}
