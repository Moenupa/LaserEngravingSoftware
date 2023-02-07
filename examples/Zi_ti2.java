package examples;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zi_ti2 extends JDialog {
    public static Font ziti = null;
    static int box1 = 0;
    static int box2 = 0;
    static int daxiao = 10;
    Board fu;
    private JSlider da_xiao;
    private JButton jButton1;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JTextArea wen_zi;
    private JComboBox<String> zi_ti_Box;
    private JComboBox<String> zi_xing_Box;

    public Zi_ti2(Frame parent, boolean modal) {
        super(parent, modal);
        this.initComponents();
    }

    public Zi_ti2(Board parent, boolean modal) {
        this.setTitle("输入文字");
        this.setLocation(new Point(200, 100));
        this.fu = parent;
        this.initComponents();
    }

    private void initComponents() {
        this.jLabel2 = new JLabel();
        this.jLabel3 = new JLabel();
        this.zi_ti_Box = new JComboBox();
        this.zi_xing_Box = new JComboBox();
        this.da_xiao = new JSlider();
        this.jScrollPane1 = new JScrollPane();
        this.wen_zi = new JTextArea();
        this.jButton1 = new JButton();
        this.jLabel1 = new JLabel();
        this.jCheckBox1 = new JCheckBox();
        this.jCheckBox2 = new JCheckBox();
        this.setDefaultCloseOperation(2);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                Zi_ti2.this.formWindowOpened(evt);
            }
        });
        this.jLabel2.setText("字型：");
        this.jLabel3.setText("尺寸:10");
        this.zi_ti_Box.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        this.zi_ti_Box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                Zi_ti2.this.zi_ti_BoxItemStateChanged(evt);
            }
        });
        this.zi_xing_Box.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        this.zi_xing_Box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                Zi_ti2.this.zi_xing_BoxItemStateChanged(evt);
            }
        });
        this.da_xiao.setMaximum(200);
        this.da_xiao.setValue(10);
        this.da_xiao.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                Zi_ti2.this.da_xiaoStateChanged(evt);
            }
        });
        this.wen_zi.setColumns(20);
        this.wen_zi.setRows(5);
        this.wen_zi.setText("ABCD");
        this.jScrollPane1.setViewportView(this.wen_zi);
        this.jButton1.setText("OK");
        this.jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Zi_ti2.this.jButton1ActionPerformed(evt);
            }
        });
        this.jLabel1.setText("字体：");
        this.jCheckBox1.setText("竖向");
        this.jCheckBox2.setText("矢量图5");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel1)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.zi_ti_Box, -2, 165, -2))).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.zi_xing_Box, -2, 149, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.da_xiao, -2, -1, -2).addComponent(this.jLabel3)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jCheckBox2).addComponent(this.jCheckBox1)).addGap(0, 198, 32767)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1)).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.jButton1, -2, 92, -2))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.zi_ti_Box, -2, -1, -2).addComponent(this.da_xiao, -2, 0, 32767))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.jLabel3).addComponent(this.jCheckBox2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.zi_xing_Box, -2, -1, -2))).addComponent(this.jCheckBox1)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -1, 517, 32767).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jButton1, -2, 34, -2).addContainerGap()));
        this.pack();
    }

    private void zi_ti_BoxItemStateChanged(ItemEvent evt) {
        String fontName = (String) evt.getItem();
        System.out.println(fontName);
        ziti = new Font(fontName, ziti.getStyle(), ziti.getSize());
        this.wen_zi.setFont(ziti);
        box1 = this.zi_ti_Box.getSelectedIndex();
    }

    public static int getFontStyleByCnName(String fontStyle) {
        if (fontStyle.equals(mainJFrame.str_routine)) {
            return 0;
        } else if (fontStyle.equals(mainJFrame.str_italic)) {
            return 2;
        } else if (fontStyle.equals(mainJFrame.str_bold)) {
            return 1;
        } else {
            return fontStyle.equals(mainJFrame.str_bold_italic) ? 3 : -1;
        }
    }

    private void zi_xing_BoxItemStateChanged(ItemEvent evt) {
        String fontStyle = (String) evt.getItem();
        System.out.println(fontStyle);
        ziti = new Font(ziti.getName(), getFontStyleByCnName(fontStyle), ziti.getSize());
        this.wen_zi.setFont(ziti);
        box2 = this.zi_xing_Box.getSelectedIndex();
    }

    private void da_xiaoStateChanged(ChangeEvent evt) {
        this.jLabel3.setText(mainJFrame.bundle.getString("str_chi_cun") + this.da_xiao.getValue());
        daxiao = 100 + this.da_xiao.getValue() * 2;
        ziti = new Font(ziti.getName(), ziti.getStyle(), daxiao);
        this.wen_zi.setFont(ziti);
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        if (this.jCheckBox1.isSelected()) {
            Board.bElements.add(BElement.chuang_jian_wen_zi_shu(this.wen_zi.getText(), ziti, 0, this.jCheckBox2.isSelected()));
        } else {
            Board.bElements.add(BElement.chuang_jian_wen_zi(this.wen_zi.getText(), ziti, this.jCheckBox2.isSelected()));
        }

        for (int i = 0; i < Board.bElements.size(); ++i) {
            ((BElement) Board.bElements.get(i)).selected = false;
        }

        ((BElement) Board.bElements.get(Board.bElements.size() - 1)).selected = true;
        BElement.center(Board.bElements);
        Che_xiao.tian_jia();
        this.fu.repaint();
        this.setVisible(false);
    }

    private void formWindowOpened(WindowEvent evt) {
        this.jLabel1.setText(mainJFrame.str_font);
        this.jLabel2.setText(mainJFrame.str_typeface);
        this.jLabel3.setText(mainJFrame.str_size + "10");
        this.jCheckBox1.setText(mainJFrame.str_vertical);
        this.jCheckBox2.setText(mainJFrame.str_vector);
        this.setIconImage((new ImageIcon(this.getClass().getResource("/tu/tu_biao.png"))).getImage());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        String[] fontNames2 = new String[fontNames.length];

        for (int i = 0; i < fontNames.length; ++i) {
            fontNames2[i] = fontNames[fontNames.length - i - 1];
        }

        this.zi_ti_Box.setModel(new DefaultComboBoxModel(fontNames2));
        String[] fontStyles = new String[]{mainJFrame.str_routine, mainJFrame.str_italic, mainJFrame.str_bold, mainJFrame.str_bold_italic};
        this.zi_xing_Box.setModel(new DefaultComboBoxModel(fontStyles));
        if (ziti == null) {
            daxiao = 120;
            ziti = new Font((String) this.zi_ti_Box.getItemAt(this.zi_ti_Box.getSelectedIndex()), 0, daxiao);
            this.wen_zi.setFont(ziti);
        } else {
            this.zi_ti_Box.setSelectedIndex(box1);
            this.zi_xing_Box.setSelectedIndex(box2);
            this.da_xiao.setValue((daxiao - 80) / 2);
        }

    }

    public static void main(String[] args) {
        try {
            LookAndFeelInfo[] var1 = UIManager.getInstalledLookAndFeels();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                LookAndFeelInfo info = var1[var3];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException var5) {
            Logger.getLogger(Zi_ti2.class.getName()).log(Level.SEVERE, (String) null, var5);
        } catch (InstantiationException var6) {
            Logger.getLogger(Zi_ti2.class.getName()).log(Level.SEVERE, (String) null, var6);
        } catch (IllegalAccessException var7) {
            Logger.getLogger(Zi_ti2.class.getName()).log(Level.SEVERE, (String) null, var7);
        } catch (UnsupportedLookAndFeelException var8) {
            Logger.getLogger(Zi_ti2.class.getName()).log(Level.SEVERE, (String) null, var8);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Zi_ti2 dialog = new Zi_ti2(new JFrame(), true);
                dialog.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
}
