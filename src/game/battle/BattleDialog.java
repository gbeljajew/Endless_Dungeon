/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.battle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.sun.java.swing.plaf.motif.MotifBorders;
import com.sun.java.swing.plaf.windows.WindowsBorders;

import game.Game;
import game.GameConstants;
import game.GameLocale;
import game.GameState;
import game.battle.attack.StatusAligment;
import game.battle.enemy.Contrahent;
import game.hero.BarInfo;
import game.hero.Hero;
import game.hero.skill.ActiveSkillCore;
import game.hero.skill.SkillFactory;
import game.panels.GamePanel;
import game.panels.shop.ShopItem;
import game.util.NPC_Sprite;
import game.util.drawable.Animation;
import game.util.drawable.DrawablePanel;
import game.util.drawable.SizedDrawable;

/**
 *
 * @autor gbeljajew
 */
public class BattleDialog extends JDialog
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Battle battle;

    public BattleDialog(Battle battle)
    {
        this.battle = battle;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setBorder(new MotifBorders.FrameBorder(panel));
        this.add(panel);

        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 3;

        panel.add(new BattlePanel(), gbc);
        
        

        this.pack();

        Point p = GamePanel.getCenter();
        this.setLocation(p.x - panel.getWidth() / 2, p.y - panel.getHeight() / 2);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                

                synchronized(battle)
                {
                    battle.update();
                    
                    if (battle.battleEnded())
                    {
                        JOptionPane.showMessageDialog(BattleDialog.this,
                                battle.getResult(),
                                GameLocale.getString("battle.result.title"),
                                JOptionPane.PLAIN_MESSAGE);
                        battle.collectSpoils();
                        BattleLog.reset();
                        BattleDialog.this.dispose();
                        timer.cancel();
                    }
                }

                repaint();
            }
        }, 20, GameConstants.TIMER_PERIOD);
    }

    private class BattlePanel extends JPanel
    {

        private static final long serialVersionUID = 1L;
        
        private final BattleField field = new BattleField();
        private final InfoBarPanel monsterStats;
        private final InfoBarPanel heroStats;
        private final ControlPanel controlPanel;

        public BattlePanel()
        {
            this.setLayout(null);

            this.field.setBounds(0, 0,
                    this.field.getPreferredSize().width,
                    this.field.getPreferredSize().height);

            int dx = (this.field.getWidth() - GameConstants.TILE_WIDTH * 3) / 2;

            int height = this.field.getHeight() - GameConstants.TILE_HEIGHT * 3;

            this.controlPanel = new ControlPanel();
            this.controlPanel.setBounds(
                    this.field.getWidth() / 4,
                    this.field.getHeight() / 2,
                    this.field.getWidth() / 2,
                    this.field.getHeight() / 2);
            this.add(this.controlPanel);

            monsterStats = new InfoBarPanel(battle.getMonster());
            monsterStats.setBounds(0, height, dx, GameConstants.TILE_HEIGHT * 3);
            this.add(monsterStats);

            heroStats = new InfoBarPanel(battle.getHero());
            heroStats.setBounds(dx + GameConstants.TILE_HEIGHT * 3, height,
                    dx, GameConstants.TILE_HEIGHT * 3);
            this.add(heroStats);

            this.add(this.field);

            BattleLogPanel bpl = new BattleLogPanel();
            bpl.setBounds(0, field.getHeight(), field.getWidth(), 100);
            this.add(bpl);

            this.setPreferredSize(
                    new Dimension(
                            this.field.getPreferredSize().width,
                            this.field.getPreferredSize().height + 100));
        }

        @Override
        public void paint(Graphics grphcs)
        {

            this.controlPanel.setVisible(battle.needShowHeroControl());
            this.controlPanel.requestFocus();

            super.paint(grphcs);
        }

    }

    private class BattleField extends JPanel
    {

        private static final long serialVersionUID = 1L;
        
        private final Image background;
        private final Image tile;
        private final SizedDrawable monsterImage;
        private final NPC_Sprite heroImage;
        private final int heroX, heroY, monsterX, monsterY;

        private Animation animation;

        public BattleField()
        {
            this.background = Game.getCurrentFloorType().getBackgroundImage();
            this.tile = Game.getCurrentFloorType().getBaseFloorTile();
            this.monsterImage = battle.getMonsterImage();
            this.heroImage = Game.getHero()
                    .getHeroClass()
                    .getHeroBody()
                    .lookNorth()
                    .step0();

            int width = this.background.getWidth(null);
            int height = this.background.getHeight(null);

            battle.setMosterHitPosition(new Point(width / 2, height / 2));
            battle.setMonsterMissPosition(new Point((width - monsterImage.getWidth() / 4), height / 2));

            this.monsterX = width / 2;
            this.monsterY = height / 2;

            this.heroX = width / 2;
            this.heroY = height + GameConstants.TILE_HEIGHT * 2;

            battle.setHeroHitPosition(
                    new Point(this.heroX,
                            this.heroY - GameConstants.HERO_SPRITE_HEIGTH / 2));
            battle.setHeroMissPosition(
                    new Point(this.heroX / 2,
                            this.heroY - GameConstants.HERO_SPRITE_HEIGTH / 2));

            height += GameConstants.TILE_HEIGHT * 3;
            this.setPreferredSize(new Dimension(width, height));

        }

        @Override
        public void paint(Graphics grphcs)
        {
            Graphics2D g = (Graphics2D) grphcs;
            // -------------------------------------------------
            g.drawImage(this.background, 0, 0, null);

            // -------------------------------------------------
            int width = this.background.getWidth(null);
            int height = this.background.getHeight(null);

            int tileWidth = GameConstants.TILE_WIDTH;
            int tileHeight = GameConstants.TILE_HEIGHT;

            int numTiles = width / tileWidth;
            int dx = (width % tileWidth) / 2;

            for (int y = 0; y < 3; y++)
            {
                for (int x = 0; x < numTiles; x++)
                {
                    g.drawImage(tile, dx + x * tileWidth, height + y * tileHeight, null);
                }
            }

            // -------------------------------------------------
            this.monsterImage.draw(g, this.monsterX, this.monsterY);

            // -------------------------------------------------
            this.heroImage.draw(g, this.heroX, this.heroY);

            // -------------------------------------------------
            if (this.animation == null)
            {
                this.animation = battle.getCurrentAnimation();
            }

            if (this.animation != null)
            {
                this.animation.draw(g, 0, 0);

                if (this.animation.isDone())
                {
                    this.animation = null;
                }
            }
        }

    }

    class BattleBar extends JPanel
    {

        private static final long serialVersionUID = 1L;
        
        private final BarInfo info;

        public BattleBar(BarInfo info)
        {
            this.info = info;
        }

        @Override
        public void paint(Graphics gr)
        {
            Graphics2D g = (Graphics2D) gr;

            int width = this.getWidth();
            int height = this.getHeight();

            Image icon = this.info.getIcon();

            int imageHigh = icon.getHeight(null);

            int yDiff = (height - imageHigh) / 2;
            int imageWidth = icon.getWidth(null);
            // TODO:DEBUG
            g.drawRect(0, 0, width - 1, height - 1);

            g.drawImage(icon, 5, yDiff, null);

            int hpPercent = (width - imageWidth - 15) * this.info.getValue() / this.info.getMaxValue();

            Color c = g.getColor();
            g.setColor(info.getBarColor());
            g.fillRect(imageWidth + 10, yDiff, hpPercent, imageHigh);
            g.setColor(c);

            String text = this.info.getValue() + "/" + this.info.getMaxValue();
            int strWidth = g.getFontMetrics().stringWidth(text);
            int strHigh = g.getFontMetrics().getAscent();
            g.drawString(text, 100 - strWidth / 2, height / 2 + strHigh / 2);

            g.drawRect(imageWidth + 10, yDiff, width - imageWidth - 15, imageHigh);

        }
    }

    private class InfoBarPanel extends JPanel
    {

        private final Contrahent contrahent;

        private final BattleBar timeBar, hpBar, mpBar;

        public InfoBarPanel(Contrahent contrahent)
        {
            this.contrahent = contrahent;

            this.timeBar = new BattleBar(this.contrahent.getTimeBar());
            this.hpBar = new BattleBar(this.contrahent.getHpBar());
            this.mpBar = new BattleBar(this.contrahent.getMpBar());

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JLabel nameLbl = new JLabel(this.contrahent.getName()); // TODO buil in color for name
            this.add(nameLbl);

            this.add(timeBar);
            this.add(hpBar);
            this.add(mpBar);
            this.add(new AilmentsPanel(contrahent));

            this.add(Box.createVerticalGlue());

            //this.setOpaque(false);
        }
        
        private class AilmentsPanel extends JPanel
        {
            @SuppressWarnings("hiding")
			private final Contrahent contrahent;

            public AilmentsPanel(Contrahent contrahent)
            {
                super();
                this.contrahent = contrahent;
                this.setPreferredSize(new Dimension(200,18));
            }

            @Override
            public void paint(Graphics g)
            {
                
                List<StatusAligment> aligments = this.contrahent.getStatAligements();
                Collections.sort(aligments);
                
                int x = 0;
                
                for (StatusAligment sa : aligments)
                {
                    Image icon = sa.getIcon();
                    
                    if(icon == null)
                        continue;
                    
                    g.drawImage(icon, x + 1, 1, null);
                    
                    x += 18;
                    
                    if(x > this.getWidth())
                        break;
                    
                }
                
            }
            
            
            
        }

    }

    private class ControlPanel extends JPanel
    {

        private final DefaultListModel<BattleMenuItem> model = new DefaultListModel<>();
        private final JList<BattleMenuItem> skillList = new JList<>(model);
        private final JScrollPane scrollPane = new JScrollPane(skillList);

        private final List<SkillPosition> meleSkills = new ArrayList<>();
        private final List<SkillPosition> magicSkills = new ArrayList<>();
        private final List<SkillPosition> supportSkills = new ArrayList<>();
        private final List<SkillPosition> extraSkills = new ArrayList<>();
        private final List<MenuPosition> menuItems = new ArrayList<>();

       
        public ControlPanel()
        {
            this.fillLists();

            Game.setGameState(GameState.DIALOG);

            this.setLayout(new BorderLayout());

            this.add(scrollPane, BorderLayout.CENTER);
            this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            this.skillList.setCellRenderer(new MyCellRenderer());
            this.scrollPane.requestFocus();

            this.configureButtons();

            Point p = GamePanel.getCenter();
            this.setLocation(p.x - this.getWidth() / 2, p.y - this.getHeight() / 2);

            this.setVisible(true);
        }

        private void configureButtons()
        {

            this.getActionMap().put("up", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    if (ControlPanel.this.isVisible())
                    {
                        ControlPanel.this.goUp();
                    }
                }
            });

            this.getActionMap().put("down", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    if (ControlPanel.this.isVisible())
                    {
                        ControlPanel.this.goDown();
                    }
                }
            });

            this.getActionMap().put("enter", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    if (ControlPanel.this.isVisible())
                    {
                        ControlPanel.this.enter();
                    }
                }
            });

            this.getActionMap().put("exit", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    if (ControlPanel.this.isVisible())
                    {
                        ControlPanel.this.toTopMenu();
                    }
                }
            });

            // ----- Buttons ----------------------------------------------
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");

            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");

            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "enter");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0), "enter");

            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "exit");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "exit");
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "exit");

        }

        private void fillLists()
        {
            Hero hero = Game.getHero();

            List<ActiveSkillCore> actSkills = hero.getSkills(ActiveSkillCore.SkillType.MELE);

            for (ActiveSkillCore skill : actSkills)
            {
                SkillPosition sp = new SkillPosition(skill);
                this.meleSkills.add(sp);
            }

            actSkills = hero.getSkills(ActiveSkillCore.SkillType.MAGIC);

            for (ActiveSkillCore skill : actSkills)
            {
                SkillPosition sp = new SkillPosition(skill);

                this.magicSkills.add(sp);
            }

            actSkills = hero.getSkills(ActiveSkillCore.SkillType.SUPPORT);

            for (ActiveSkillCore skill : actSkills)
            {
                SkillPosition sp = new SkillPosition(skill);

                this.supportSkills.add(sp);
            }

            this.menuItems.add(new MenuPosition(meleSkills, "/MELE"));
            this.menuItems.add(new MenuPosition(magicSkills, "/MAGIC"));
            this.menuItems.add(new MenuPosition(supportSkills, "/SUPPORT"));
            this.menuItems.add(new MenuPosition(extraSkills, "/EXTRA"));

            Collections.sort(this.magicSkills);
            Collections.sort(this.meleSkills);
            Collections.sort(this.supportSkills);

            for (MenuPosition menuItem : menuItems)
            {
                this.model.addElement(menuItem);
            }

            skillList.setSelectedIndex(0);
        }

        private void goUp()
        {
            if (this.model.size() == 0)
            {
                return;
            }

            int index = this.skillList.getSelectedIndex();

            index--;

            if (index < 0)
            {
                index = this.model.getSize() - 1;
            }

            this.skillList.setSelectedIndex(index);

        }

        private void goDown()
        {
            if (this.model.size() == 0)
            {
                return;
            }

            int index = this.skillList.getSelectedIndex();

            index++;

            if (index >= this.model.getSize())
            {
                index = 0;
            }

            this.skillList.setSelectedIndex(index);
        }

        private void toTopMenu()
        {
            ControlPanel.this.model.clear();

            for (MenuPosition skill : this.menuItems)
            {
                ControlPanel.this.model.addElement(skill);
            }

            if (model.size() > 0)
            {
                skillList.setSelectedIndex(0);
            }
        }

        private void enter()
        {
            if (this.model.size() == 0)
            {
                return;
            }

            int index = this.skillList.getSelectedIndex();

            if (index < 0 || index >= model.size())
            {
                return;
            }

            BattleMenuItem e = this.model.get(index);

            e.enter();
        }

        private class SkillPosition extends JPanel implements Comparable<SkillPosition>, BattleMenuItem
        {

            private final ShopItem item;
            private final ActiveSkillCore skill;

            private final JLabel cooldown = new JLabel();

            private final Border normalBorder = new LineBorder(Color.BLACK, 1);
            private final Border selectedBorder = new WindowsBorders.DashedBorder(Color.RED);
            private final Color background;

            public SkillPosition(ActiveSkillCore skill)
            {
                this.item = SkillFactory.getShopItem(skill.getKey());
                this.skill = skill;
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

                JLabel priceLbl = new JLabel("MP: " + String.valueOf(skill.getManaConsumption()));
                gbc.weightx = 0;
                gbc.gridx = 2;
                gbc.gridy = 0;
                this.add(priceLbl, gbc);

                gbc.gridy = 1;
                this.cooldown.setText("");
                this.add(this.cooldown);

                this.setBorder(normalBorder);

                background = this.getBackground();

            }

            @Override
            public int compareTo(SkillPosition t)
            {
                return this.item.getName().compareTo(t.item.getName());
            }

            @Override
            public void paint(Graphics grphcs)
            {
                String s = "";

                if (this.skill.getCoolDown() > 0)
                {
                    s = "[" + this.skill.getCoolDown() + "]";
                }

                this.cooldown.setText(s);

                super.paint(grphcs);
            }

            @Override
            public void setSelected(boolean selected)
            {
                Color c;
                Border b;

                if (selected == true)
                {
                    c = Color.CYAN;
                    b = this.selectedBorder;
                } else
                {
                    c = this.background;
                    b = this.normalBorder;
                }

                if (!this.skill.canBeUsed())
                {
                    c = Color.DARK_GRAY;
                }

                this.setBackground(c);
                this.setBorder(b);
            }

            @Override
            public void enter()
            {
                battle.useSkill(this.skill);
                ControlPanel.this.toTopMenu();
                ControlPanel.this.setVisible(false);
            }

            private boolean canBeUsed()
            {
                return this.skill.canBeUsed();
            }

        }

        private class MenuPosition extends JPanel implements Comparable<SkillPosition>, BattleMenuItem
        {

            private final ShopItem item;

            private final JLabel cooldown = new JLabel();

            private final Border normalBorder = new LineBorder(Color.BLACK, 1);
            private final Border selectedBorder = new WindowsBorders.DashedBorder(Color.RED);
            private final Color background;
            private final List<SkillPosition> skillPositions;

            public MenuPosition(List<SkillPosition> skills, String key)
            {
                this.item = SkillFactory.getShopItem(key);
                this.skillPositions = skills;

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

                gbc.weightx = 0;
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.gridy = 0;
                this.cooldown.setText("");
                this.add(this.cooldown);

                this.setBorder(normalBorder);

                background = this.getBackground();

            }

            @Override
            public int compareTo(SkillPosition t)
            {
                return this.item.getName().compareTo(t.item.getName());
            }

            @Override
            public void paint(Graphics grphcs)
            {

                int num = 0;

                for (SkillPosition skill : skillPositions)
                {
                    if (skill.canBeUsed())
                    {
                        num++;
                    }
                }

                this.cooldown.setText(String.format("[%d/%d]", num, this.skillPositions.size()));

                super.paint(grphcs);
            }

            @Override
            public void setSelected(boolean selected)
            {
                Color c;
                Border b;

                if (selected == true)
                {
                    c = Color.CYAN;
                    b = this.selectedBorder;
                } else
                {
                    c = this.background;
                    b = this.normalBorder;
                }

                this.setBackground(c);
                this.setBorder(b);
            }

            @Override
            public void enter()
            {
                ControlPanel.this.model.clear();

                for (SkillPosition skill : skillPositions)
                {
                    ControlPanel.this.model.addElement(skill);
                }

                if (model.size() > 0)
                {
                    skillList.setSelectedIndex(0);
                }

            }

        }

        private class MyCellRenderer implements ListCellRenderer<BattleMenuItem>
        {

            @Override
            public Component getListCellRendererComponent(
                    JList<? extends BattleMenuItem> jlist,
                    BattleMenuItem e, int i, boolean selected, boolean bln1)
            {
                e.setSelected(selected);
                return (JPanel) e;
            }

        }
    }

    private interface BattleMenuItem
    {

        public void setSelected(boolean selected);

        public void enter();
    }

    private class BattleLogPanel extends JPanel
    {

        private final JTextPane textArea = new JTextPane();
        private final JScrollPane scroller = new JScrollPane(textArea);

        public BattleLogPanel()
        {
            this.setLayout(new BorderLayout());
            this.textArea.setContentType("text/html");
            this.add(this.scroller, BorderLayout.CENTER);
        }

        @Override
        public void paint(Graphics grphcs)
        {
            this.textArea.setText(BattleLog.getText());

            super.paint(grphcs);
        }

    }
}
