package game.hero.skill;


import game.Game;
import game.GameLocale;
import game.GameState;
import game.hero.Hero;
import game.panels.GamePanel;
import game.panels.shop.ShopItem;
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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author gbeljajew
 */
public class SkillPanel extends JDialog implements ActionListener
{

    

    
    private enum SkillType
    {
        ALL("label.skill.screen.all") {
            @Override
            List<SkillPosition> getPositions()
            {
                return SkillPanel.instance.allSkills;
            }
        }
        ,
        PASSIVE("label.skill.screen.passive") {
            @Override
            List<SkillPosition> getPositions()
            {
                return SkillPanel.instance.passiveSkills;
            }
        }
        ,
        ACTIVE("label.skill.screen.active") {
            @Override
            List<SkillPosition> getPositions()
            {
                return SkillPanel.instance.activeSkills;
            }
        }
        ,
        MELE("label.skill.screen.mele") {
            @Override
            List<SkillPosition> getPositions()
            {
                return SkillPanel.instance.meleSkills;
            }
        }
        ,
        MAGIC("label.skill.screen.magic") {
            @Override
            List<SkillPosition> getPositions()
            {
                return SkillPanel.instance.magicSkills;
            }
        }
        ,
        SUPPORT("label.skill.screen.support") {
            @Override
            List<SkillPosition> getPositions()
            {
                return SkillPanel.instance.supportSkills;
            }
        }
        ;

        private SkillType(String localKey)
        {
            this.localKey = localKey;
        }

        private final String localKey;

        abstract List<SkillPosition> getPositions();
        
        @Override
        public String toString()
        {
            return GameLocale.getString(localKey);
        }
    }
    
    
    private final DefaultListModel<SkillPosition> model = new DefaultListModel<>();
    private final JList skillList = new JList(model);
    private final JScrollPane scrollPane = new JScrollPane(skillList);
    
    private final JComboBox<SkillType> types = new JComboBox<>(SkillType.values());
    
    private final List<SkillPosition> allSkills = new ArrayList<>();
    private final List<SkillPosition> activeSkills = new ArrayList<>();
    private final List<SkillPosition> passiveSkills = new ArrayList<>();
    private final List<SkillPosition> meleSkills = new ArrayList<>();
    private final List<SkillPosition> magicSkills = new ArrayList<>();
    private final List<SkillPosition> supportSkills = new ArrayList<>();
    
    private static SkillPanel instance;

    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public SkillPanel()
    {
        this.fillLists();
        instance = this;
        Game.setGameState(GameState.DIALOG);
        
        this.setLayout(new BorderLayout());
        
        this.add(types, BorderLayout.NORTH);
        types.addActionListener(this);
        types.setEditable(false);
        types.setSelectedItem(SkillType.ALL);
        
        this.add(scrollPane, BorderLayout.CENTER);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.skillList.setCellRenderer(new MyCellRenderer());
        this.scrollPane.requestFocus();
        
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener((x)-> this.dispose());
        
        JPanel buttonPanel = new JPanel();
        this.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createHorizontalGlue());
        
        
        this.configureButtons();
        this.pack();
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        
        Point p = GamePanel.getCenter();
        this.setLocation(p.x - this.getWidth() / 2, p.y - this.getHeight() / 2);
        
        this.setVisible(true);
    }
    
    private void configureButtons()
    {
        this.scrollPane.getActionMap().put("exit", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                SkillPanel.this.dispose();
            }
        });
        
        this.scrollPane.getActionMap().put("right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                SkillPanel.this.nextKategorie();
            }
        });
        
        this.scrollPane.getActionMap().put("left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                SkillPanel.this.previousKategorie();
            }
        });
        
        //---------------------------------------------------------------
        
        
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "exit");
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0), "exit");
        
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left");
        this.scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        
    }

    @Override
    public void dispose()
    {
        Game.setGameState(GameState.MAP);
        super.dispose(); 
    }
    
    private void fillLists()
    {
        Hero hero = Game.getHero();
        
        
        hero.getOwnedSkills();
        
        List<String> skills = hero.getOwnedSkills();
        
        for (String skill : skills)
        {
            SkillPosition sp = new SkillPosition(SkillFactory.getShopItem(skill));
            this.allSkills.add(sp);
            
            if(!skill.startsWith("#"))
                this.passiveSkills.add(sp);
        }
        
        List<ActiveSkillCore> actSkills = hero.getSkills(ActiveSkillCore.SkillType.MELE);
        
        for (ActiveSkillCore skill : actSkills)
        {
            SkillPosition sp = new SkillPosition(SkillFactory.getShopItem(skill.getKey()));
            this.meleSkills.add(sp);
            this.activeSkills.add(sp);
        }
        
        actSkills = hero.getSkills(ActiveSkillCore.SkillType.MAGIC);
        
        for (ActiveSkillCore skill : actSkills)
        {
            SkillPosition sp = new SkillPosition(SkillFactory.getShopItem(skill.getKey()));
            
            this.magicSkills.add(sp);
            this.activeSkills.add(sp);
        }
        
        actSkills = hero.getSkills(ActiveSkillCore.SkillType.SUPPORT);
        
        for (ActiveSkillCore skill : actSkills)
        {
            SkillPosition sp = new SkillPosition(SkillFactory.getShopItem(skill.getKey()));
            
            this.supportSkills.add(sp);
            this.activeSkills.add(sp);
        }
        
        
        Collections.sort(this.activeSkills);
        Collections.sort(this.allSkills);
        Collections.sort(this.magicSkills);
        Collections.sort(this.meleSkills);
        Collections.sort(this.passiveSkills);
        Collections.sort(this.supportSkills);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        
       this.model.clear();
       
        SkillType type = (SkillType)this.types.getSelectedItem();
        
        for (SkillPosition position : type.getPositions())
        {
            model.addElement(position);
        }
        
    }
    
    private void nextKategorie()
    {
        int index = this.types.getSelectedIndex();
        ComboBoxModel<SkillType> cbModel = this.types.getModel();
        
        index ++;
        
        if(index >= cbModel.getSize())
            index = 0;
        
        this.types.setSelectedIndex(index);
        
    }

    private void previousKategorie()
    {
        int index = this.types.getSelectedIndex();
        ComboBoxModel<SkillType> cbModel = this.types.getModel();
        
        index --;
        
        if(index < 0)
            index = cbModel.getSize() - 1;
        
        this.types.setSelectedIndex(index);    }
}



class SkillPosition extends JPanel implements Comparable<SkillPosition>
{
    
    private final ShopItem item;

    @SuppressWarnings( "OverridableMethodCallInConstructor" )
    public SkillPosition(ShopItem item) 
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
        
        JLabel priceLbl = new JLabel("LVL: " + String.valueOf(Game.getHero().getSkillLevel(item.getKey())));
        gbc.weightx = 0;
        gbc.gridx = 2;
        gbc.gridy = 0;
        this.add(priceLbl, gbc);
        
        this.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 5, 5, 5), 
                new LineBorder(Color.BLACK, 1, true)));
        
    }

    @Override
    public int compareTo(SkillPosition t)
    {
        return this.item.getName().compareTo(t.item.getName());
    }

}


class MyCellRenderer implements ListCellRenderer<SkillPosition>
{

    @Override
    public Component getListCellRendererComponent(
            JList<? extends SkillPosition> jlist, 
            SkillPosition e, int i, boolean bln, boolean bln1)
    {
        return e;
    }

}


