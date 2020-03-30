/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels;

import bin.game.GameConstants;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
class MainFrame extends JFrame
{
    private final CardLayout panelSwitcher;
    private final JPanel screenHolder;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MainFrame() throws HeadlessException
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.panelSwitcher = new CardLayout();
        this.screenHolder = new JPanel(panelSwitcher);
        this.screenHolder.setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        
        this.add(this.screenHolder);
        
        this.addAllScreens();
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        this.switchScreen(ScreenEnum.INN);
    }

    void switchScreen(ScreenEnum screen)
    {
        this.panelSwitcher.show(this.screenHolder, screen.name());
    }

    private void addAllScreens()
    {
        this.addPanel(new MapScreen(), ScreenEnum.MAP);
    }

    private void addPanel(JPanel panel, ScreenEnum screenKey)
    {
        this.screenHolder.add(panel, screenKey.name());
    }
    
    
}
