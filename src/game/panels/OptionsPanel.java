/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.panels;

import game.Game;
import game.GameConstants;
import game.GameLocale;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

/**
 *
 * @autor gbeljajew
 */
public class OptionsPanel extends JPanel
{
    
    private final MainFrame mainFrame;
    private String back;
    
    private final JRadioButton levelingManualRB, levelingHalfManualRB, levelingAutoRB;
    

    public OptionsPanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        
        this.setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Options");
        this.add(lbl, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.add(mainPanel, BorderLayout.CENTER);
        
        
        // ----- Leveling --------------------------------------------
        JPanel lvlPanel = new JPanel();
        lvlPanel.setLayout(new BoxLayout(lvlPanel, BoxLayout.Y_AXIS));
        lvlPanel.setBorder(new TitledBorder(GameLocale.getString("options.leveling.group")));
        mainPanel.add(lvlPanel);
        ButtonGroup lvlGroup = new ButtonGroup();
        
        
        this.levelingManualRB = new JRadioButton(GameLocale.getString("options.leveling.manual.name"));
        lvlPanel.add(this.levelingManualRB);
        lvlGroup.add(levelingManualRB);
        this.levelingManualRB.setToolTipText(GameLocale.getString("options.leveling.manual.tt"));
        this.levelingManualRB.setSelected(true);
        
        this.levelingHalfManualRB = new JRadioButton(GameLocale.getString("options.leveling.half.name"));
        lvlPanel.add(this.levelingHalfManualRB);
        lvlGroup.add(levelingHalfManualRB);
        this.levelingHalfManualRB.setToolTipText(GameLocale.getString("options.leveling.half.tt"));
        
        this.levelingAutoRB = new JRadioButton(GameLocale.getString("options.leveling.auto.name"));
        lvlPanel.add(this.levelingAutoRB);
        lvlGroup.add(levelingAutoRB);
        this.levelingAutoRB.setToolTipText(GameLocale.getString("options.leveling.auto.tt"));
        
        
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        buttonPanel.add(Box.createHorizontalGlue());
        
        JButton btnOk = new JButton(GameLocale.getString("button.ok"));
        buttonPanel.add(btnOk);
        btnOk.addActionListener( (x) -> confirm() );
        
        buttonPanel.add(Box.createHorizontalStrut(5));
        
        JButton btnBack = new JButton(GameLocale.getString("button.back"));
        buttonPanel.add(btnBack);
        btnBack.addActionListener( (x) -> goBack() );
        
        buttonPanel.add(Box.createHorizontalStrut(5));
        
    }
    
    void initBeforeUse()
    {
        // TODO here read values from Game.options and set them on GUI.
        
        // ----- Leveling -------------------------------------------
        
        String s = Game.getOption(GameConstants.OPTION_KEY_LEVELUP);
        
        if(GameConstants.OPTION_VALUE_LEVELUP_FULL_AUTO.equals(s))
            this.levelingAutoRB.setSelected(true);
        if(GameConstants.OPTION_VALUE_LEVELUP_MANUAL.equals(s))
            this.levelingManualRB.setSelected(true);
        if(GameConstants.OPTION_VALUE_LEVELUP_HALF_AUTO.equals(s))
            this.levelingHalfManualRB.setSelected(true);
        
    }
    
    private void confirm()
    {
        // TODO here write chaged options to Game.options
        
        // ----- Leveling -------------------------------------------
        
        if(this.levelingAutoRB.isSelected())
            Game.setOption(GameConstants.OPTION_KEY_LEVELUP, 
                    GameConstants.OPTION_VALUE_LEVELUP_FULL_AUTO);
        if(this.levelingHalfManualRB.isSelected())
            Game.setOption(GameConstants.OPTION_KEY_LEVELUP, 
                    GameConstants.OPTION_VALUE_LEVELUP_HALF_AUTO);
        if(this.levelingManualRB.isSelected())
            Game.setOption(GameConstants.OPTION_KEY_LEVELUP, 
                    GameConstants.OPTION_VALUE_LEVELUP_MANUAL);
        
        
        
        this.goBack();
    }
    
    private void goBack()
    {
        mainFrame.goBackFromOptions();
    }
}
