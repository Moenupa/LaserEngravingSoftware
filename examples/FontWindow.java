package examples;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FontWindow extends JFrame {
    public static Font fontset = null;
    static int box1 = 0;
    static int box2 = 0;
    static int size = 14;
    private JSlider sd_size;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JTextArea wen_zi;
    private JComboBox<String> zi_ti_Box;
    private JComboBox<String> zi_xing_Box;

    public FontWindow() {
        this.initComponents();
    }

    public static int getFontStyleByCnName(String fontStyle) {
        return switch (fontStyle) {
            case "常规" -> 0;
            case "粗体" -> 1;
            case "斜体" -> 2;
            case "粗斜体" -> 3;
            default -> -1;
        };
    }

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo preset : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(preset.getName())) {
                    UIManager.setLookAndFeel(preset.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger("FONT").log(Level.SEVERE, null, e);
        }

        EventQueue.invokeLater(() -> (new FontWindow()).setVisible(true));
    }

    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jLabel3 = new JLabel();
        this.zi_ti_Box = new JComboBox();
        this.zi_xing_Box = new JComboBox();
        this.sd_size = new JSlider();
        this.jScrollPane1 = new JScrollPane();
        this.wen_zi = new JTextArea();
        this.jButton1 = new JButton();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("字体设置");
        this.setLocation(new Point(300, 300));
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                FontWindow.this.formWindowOpened(evt);
            }
        });
        this.jLabel1.setText("字体：");
        this.jLabel2.setText("字型：");
        this.jLabel3.setText("大小：");
        this.zi_ti_Box.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        this.zi_ti_Box.addItemListener(FontWindow.this::zi_ti_BoxItemStateChanged);
        this.zi_xing_Box.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        this.zi_xing_Box.addItemListener(FontWindow.this::zi_xing_BoxItemStateChanged);
        this.sd_size.setMaximum(200);
        this.sd_size.setValue(14);
        this.sd_size.addChangeListener(FontWindow.this::da_xiaoStateChanged);
        this.wen_zi.setColumns(20);
        this.wen_zi.setRows(5);
        this.wen_zi.setText("ABCD");
        this.jScrollPane1.setViewportView(this.wen_zi);
        this.jButton1.setText("OK");
        this.jButton1.addActionListener(FontWindow.this::jButton1ActionPerformed);
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel1)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.zi_ti_Box, -2, 165, -2))).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.zi_xing_Box, -2, 149, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.sd_size, -2, -1, -2).addComponent(this.jLabel3)).addGap(0, 142, 32767)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1)).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.jButton1, -2, 92, -2))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.zi_ti_Box, -2, -1, -2).addComponent(this.sd_size, -2, 0, 32767))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.jLabel3)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.zi_xing_Box, -2, -1, -2))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -1, 387, 32767).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jButton1, -2, 34, -2).addContainerGap()));
        this.pack();
    }

    private void formWindowOpened(WindowEvent evt) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        this.zi_ti_Box.setModel(new DefaultComboBoxModel(fontNames));
        String[] fontStyles = new String[]{"常规", "斜体", "粗体", "粗斜体"};
        this.zi_xing_Box.setModel(new DefaultComboBoxModel(fontStyles));
        if (fontset == null) {
            fontset = new Font(this.zi_ti_Box.getItemAt(this.zi_ti_Box.getSelectedIndex()), 0, 14);
            this.wen_zi.setFont(fontset);
        } else {
            this.zi_ti_Box.setSelectedIndex(box1);
            this.zi_xing_Box.setSelectedIndex(box2);
            this.sd_size.setValue(size);
        }

    }

    private void zi_ti_BoxItemStateChanged(ItemEvent evt) {
        String fontName = (String) evt.getItem();
        System.out.println(fontName);
        fontset = new Font(fontName, fontset.getStyle(), fontset.getSize());
        this.wen_zi.setFont(fontset);
        box1 = this.zi_ti_Box.getSelectedIndex();
    }

    private void zi_xing_BoxItemStateChanged(ItemEvent evt) {
        String fontStyle = (String) evt.getItem();
        System.out.println(fontStyle);
        fontset = new Font(fontset.getName(), getFontStyleByCnName(fontStyle), fontset.getSize());
        this.wen_zi.setFont(fontset);
        box2 = this.zi_xing_Box.getSelectedIndex();
    }

    private void da_xiaoStateChanged(ChangeEvent evt) {
        fontset = new Font(fontset.getName(), fontset.getStyle(), this.sd_size.getValue());
        this.wen_zi.setFont(fontset);
        size = this.sd_size.getValue();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        BElement.chuang_jian_wen_zi(this.wen_zi.getText(), fontset, false);
        this.setVisible(false);
    }
}
