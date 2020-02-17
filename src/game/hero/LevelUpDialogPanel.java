/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.hero;

import game.GameConstants;
import game.GameLocale;
import game.util.ResourcesContainer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @autor gbeljajew
 */
public class LevelUpDialogPanel extends JPanel
{
    private final JLabel pointsLbl = new JLabel();
    ExtraPoints extraPoints;
    public LevelUpDialogPanel(Stat[] stats, ExtraPoints extraPoints)
    {
        this.setLayout(new GridBagLayout());
        this.extraPoints = extraPoints;
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        this.pointsLbl.setText(String.valueOf(this.extraPoints.getPoints()));
        this.add(this.pointsLbl, gbc);
        
        JLabel pointNameLbl = new JLabel(GameLocale.getString("label.lvlup.points"));
        gbc.gridx = 1;
        this.add(pointNameLbl, gbc);
        
        for (int i = 0; i < stats.length; i++)
        {
            Stat stat = stats[i];
            
            StatusLvlUpLine statusLvlUpLine = new StatusLvlUpLine(stat, this, i+1);
        }
    }

    class StatusLvlUpLine
    {
        private final Stat stat;
        
        private final JLabel statNameLbl, statValueLbl, incrementValueLbl;
        private final JButton plusButton; 

        /**
         * 
         * @param stat
         * @param panel
         * @param row row in grid
         */
        public StatusLvlUpLine(Stat stat, JPanel panel, int row)
        {
            this.stat = stat;
            
            this.statNameLbl = new JLabel(GameLocale.getString(this.stat.getNameKey().statNameKey));
            this.statValueLbl = new JLabel(String.valueOf(stat.getRealValue()));
            this.incrementValueLbl = new JLabel(String.valueOf(stat.getIncrement()));
            this.plusButton = new JButton(new ImageIcon(ResourcesContainer.getMiskImage(GameConstants.ICON_KEY_LVLUP_PLUS_BUTTON)));
            
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = row;
            gbc.ipadx = 5;
            
            gbc.gridx = 0;
            panel.add(this.statNameLbl, gbc);
            this.statNameLbl.setHorizontalAlignment(SwingConstants.LEFT);
            
            gbc.gridx = 1;
            panel.add(this.statValueLbl, gbc );
            
            gbc.gridx = 2;
            panel.add(this.incrementValueLbl, gbc);
            
            gbc.gridx = 3;
            panel.add(this.plusButton, gbc);
            
            this.plusButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    if(LevelUpDialogPanel.this.extraPoints.use() == true)
                    {
                        LevelUpDialogPanel.this.pointsLbl.setText(
                                String.valueOf(
                                        LevelUpDialogPanel.this.extraPoints.getPointsLeft()));
                        
                        StatusLvlUpLine.this.stat.pp();
                        StatusLvlUpLine.this.incrementValueLbl.setText(
                                String.valueOf(StatusLvlUpLine.this.stat.getIncrement()));
                        
                    }
                }
            });
            
        }
        
        
    }
}
