/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bin.game.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


// TODO this class is just in placeholder state.
/**
 *
 * @author gbeljajew
 */
public class OptionScreen extends JPanel
{

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public OptionScreen()
    {
        this.setLayout(null);
        
        JLabel label = new JLabel("Options");
        this.add(label);
        label.setBounds(10, 10, 100, 20);
        
        JButton backBtn = new JButton("BACK");
        this.add(backBtn);
        backBtn.setBounds(620, 530, 100, 50);
        backBtn.addActionListener((e) -> backAction());
        
    }
    
    private void backAction()
    {
        // TODO saving options
        
        
        ScreenControl.getBack();
    }
}
