/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.panels.shop;

import com.sun.java.swing.plaf.motif.MotifBorders;
import com.sun.java.swing.plaf.windows.WindowsBorders;
import game.Game;
import game.GameState;
import game.panels.GamePanel;
import game.util.drawable.DrawablePanel;
import game.util.drawable.SizedDrawable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


/**
 *
 * @author gbeljajew
 */
public class ShopPanel extends JDialog
{

    private final JPanel panel = new JPanel();
    private final DrawablePanel shopOwner;
    private final String name;
    DefaultListModel<ShopPosition> model = new DefaultListModel<>();
    private final JList shopList = new JList(model);
    //private final JScrollPane scrollPane = new JScrollPane(shopList);
    
    public ShopPanel(Shop shop)
    {
        this.shopOwner = new DrawablePanel(shop.getShopOwner());
        this.name = shop.getShopName();
        this.add(this.panel);
        this.setUndecorated(true);
        
        Game.setGameState(GameState.DIALOG);
        
        panel.setLayout(new BorderLayout());
        
      
        panel.setBackground(Color.WHITE);
        panel.setOpaque(false);
        
       JLabel shopName = new JLabel(name);
       this.add(shopName, BorderLayout.NORTH);
        
        panel.add(this.shopOwner, BorderLayout.WEST);
        panel.add(this.shopList, BorderLayout.CENTER);
        
        for (ShopItem shopItem : shop.getShopItems())
        {
            model.addElement(new ShopPosition(shopItem));
        }
        
        this.shopList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                ShopPanel.this.buySelected();
            }
        });
        
        this.shopList.setCellRenderer(new MyCellRenderer());
        this.shopList.setSelectedIndex(0);
        
        this.shopList.setBackground(Color.WHITE);
        this.shopList.setOpaque(false);
        
        this.shopOwner.setBackground(Color.WHITE);
        this.shopOwner.setOpaque(false);
        
        
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener((x)-> this.dispose());
        
        JPanel buttonPanel = new JPanel();
        this.panel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createHorizontalGlue());
        
        
        
        this.configureKeys();
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.panel.setBorder(new MotifBorders.FrameBorder(panel));
        this.pack();
        
        Point p = GamePanel.getCenter();
        
        this.setLocation(p.x - panel.getWidth() / 2, p.y - panel.getHeight() / 2);
    }

    @Override
    public String getName()
    {
        return name;
    }

    private void configureKeys()
    {
        panel.getActionMap().put("up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                ShopPanel.this.goUp();
            }
        });
        
        panel.getActionMap().put("down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                ShopPanel.this.goDown();
            }
        });
        
        panel.getActionMap().put("enter", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                ShopPanel.this.buySelected();
            }
        });
        
         panel.getActionMap().put("exit", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                ShopPanel.this.dispose();
            }
        });
        
        // ----- Buttons ----------------------------------------------
        
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
        
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
        
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "enter");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0), "enter");
        
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "exit");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "exit");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "exit");
        
    }
    
    @Override
    public void dispose()
    {
        Game.setGameState(GameState.MAP);
        super.dispose(); 
    }
    
    private void buySelected()
    {
        int index = shopList.getSelectedIndex();
                
                if(index < 0)
                {
                    this.dispose();
                    return;
                }
                
                ShopPosition shopPosition = model.getElementAt(index);
                
                ShopItem item = shopPosition.getItem();
                
                boolean bought = Game.getHero().buy(item, item.detPrice());
                
                if(bought == true)
                {
                    model.remove(index);
                    
                    if(model.size() > 0)
                    {
                        if(index == 0)
                            this.shopList.setSelectedIndex(0);
                        else if( index < model.getSize())
                        {
                            this.shopList.setSelectedIndex(index);
                        }
                        else
                            this.shopList.setSelectedIndex(index - 1);
                    }
                }
                
                
                Game.repaintScreen();
    }
    
    private void goUp()
    {
        if(this.model.size() == 0)
            return;
        
        int index = this.shopList.getSelectedIndex();
        
        index --;
        
        if(index < 0)
            index = this.model.getSize() - 1;
        
        this.shopList.setSelectedIndex(index);
        
        
    }
    
    private void goDown()
    {
        if(this.model.size() == 0)
            return;
        
        int index = this.shopList.getSelectedIndex();
        
        index ++;
        
        if(index >= this.model.getSize())
            index = 0;
        
        this.shopList.setSelectedIndex(index);
    }
}

class ShopPosition extends JPanel
{
    
    private final ShopItem item;
    
    private final Border normalBorder = new LineBorder(Color.BLACK, 1);
    private final Border selectedBorder = new WindowsBorders.DashedBorder(Color.RED);
    private final Color background;
    
    public ShopPosition(ShopItem item)
    {
        this.item = item;
        
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 3;
        gbc.ipady = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.insets = new Insets(3, 3, 3, 0);
        
        SizedDrawable drawable = item.getIcon();
        DrawablePanel imagePanel = new DrawablePanel(drawable, 0, 0);
        this.add(imagePanel, gbc);
        
        JLabel nameLbl = new JLabel("<html>" + item.getName() + "</html>");
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.weightx = 1;
        this.add(nameLbl, gbc);
        
        JLabel descriptionLbl = new JLabel("<html>" + item.getDescription()+ "</html>");
        gbc.gridy = 1;
        this.add(descriptionLbl, gbc);
        
        JLabel priceLbl = new JLabel(String.valueOf(item.detPrice()));
        gbc.weightx = 0;
        gbc.gridx = 2;
        gbc.gridy = 0;
        this.add(priceLbl, gbc);
        
        this.setBorder(normalBorder);
        
        background = this.getBackground();
        
        
    }

    public ShopItem getItem()
    {
        return item;
    }
    
    public void setSelected(boolean selected)
    {
        if(selected == true)
        {
            this.setBackground(Color.CYAN);
            this.setBorder(this.selectedBorder);
        }
        else
        {
            this.setBackground(this.background);
            this.setBorder(this.normalBorder);
        }
    }
    
}


class MyCellRenderer implements ListCellRenderer<ShopPosition>
{

    @Override
    public Component getListCellRendererComponent(
            JList<? extends ShopPosition> jlist, 
            ShopPosition e, 
            int i, 
            boolean selected, 
            boolean bln1)
    {
        e.setSelected(selected);
        return e;
    }
    
}
