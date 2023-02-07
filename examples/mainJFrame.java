package examples;

import gnu.io.SerialPort;
import net.sf.image4j.codec.bmp.BMPEncoder;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainJFrame extends JFrame implements KeyListener {
    public static String software_version = "v1.1.1";
    JMenuBar jmb;
    JMenu menu1;
    JMenuItem item1;
    public byte[] driver_version = new byte[]{0, 0, 0, 0};
    public static String ce_shi = "";
    public Wifi wifi = null;
    boolean an_xia = false;
    int anxia_x = 0;
    int anxia_y = 0;
    int anxia_x_1 = 0;
    int anxia_y_1 = 0;
    int an = 0;
    int an_niu = 0;
    SerialPort port = null;
    public static Com handler = null;
    boolean com_isOpened = false;
    private Object suo_fhm = new Object();
    private boolean kuang = false;
    boolean fan_hui_ma = false;
    public static boolean kai_shi = false;
    boolean zan_ting = false;
    boolean wifi_moshi = false;
    boolean shi_zi = false;
    mainJFrame win = null;
    public static int timeout = 0;
    public static boolean kai_shi2 = false;
    public static ResourceBundle bundle = null;
    int miao = 0;
    boolean fu_zhi = false;
    List<BElement> ty_fu_zhi = new ArrayList<>();
    boolean tuo_dong = false;

    public static String str_font = "";
    public static String str_typeface = "";
    public static String str_size = "";
    public static String str_routine = "";
    public static String str_italic = "";
    public static String str_bold = "";
    public static String str_bold_italic = "";
    public static String str_vertical = "";
    public static String str_vector = "";
    public static String str_outdated = "";
    public static String str_set = "";
    public static String str_firmware = "";
    public static String str_model = "";
    public static String str_update = "";
    public static String str_download_fail = "";

    private Board board1 = new Board();
    private JPanel pn_inlay_hint = new JPanel();
    private JPanel pn_main_right = new JPanel();
    private JDialog dialog = new JDialog();

    // buttons that draw shapes on board
    private JButton btn_openpic = new JButton();
    private JButton btn_text = new JButton();
    private JButton btn_circle = new JButton();
    private JButton btn_square = new JButton();
    private JButton btn_heart = new JButton();
    private JButton btn_star = new JButton();

    // buttons resp for main functions
    private JButton btn_convertbmp = new JButton();
    private JButton btn_save = new JButton();
    private JButton btn_preview = new JButton();
    private JButton btn_engrave = new JButton();
    private JButton btn_aux_positioning = new JButton();
    private JButton btn_stop = new JButton();
    private JButton btn_usbconnect = new JButton();
    private JButton btn_wificonnect = new JButton();
    private JButton btn_unknown = new JButton();

    private JLabel lb_inlay_x = new JLabel();
    private JLabel lb_inlay_y = new JLabel();
    private JLabel lb_inlay_h = new JLabel();
    private JLabel lb_inlay_w = new JLabel();

    private JLabel lb_wifi = new JLabel();
    private JLabel lb_pwd = new JLabel();
    private JLabel lb_execution_time = new JLabel();

    // labels in right-panel settings
    private JLabel lb_weak_light = new JLabel();
    private JLabel lb_carve_power = new JLabel();
    private JLabel lb_carve_depth = new JLabel();
    private JLabel lb_cut_power = new JLabel();
    private JLabel lb_cut_depth = new JLabel();
    private JLabel lb_contrast = new JLabel();
    private JLabel lb_fill = new JLabel();
    private JLabel lb_num_times = new JLabel();
    private JLabel lb_accuracy = new JLabel();

    // interactive controllers in right-panel settings
    private JSlider sd_weak_light = new JSlider();
    private JSlider sd_carve_power = new JSlider();
    private JSlider sd_carve_depth = new JSlider();
    private JSlider sd_cut_power = new JSlider();
    private JSlider sd_cut_depth = new JSlider();
    private JSlider sd_contrast = new JSlider();
    private JSlider sd_fill = new JSlider();
    private JComboBox<String> opt_num_times = new JComboBox<>();
    private JComboBox<String> opt_accuracy = new JComboBox<>();

    private JTextField tf_inlay_x = new JTextField();
    private JTextField tf_inlay_y = new JTextField();
    private JTextField tf_inlay_w = new JTextField();
    private JTextField tf_inlay_h = new JTextField();
    private JTextField jTextField5 = new JTextField();
    private JTextField jTextField6 = new JTextField();

    private JProgressBar jdt;

    public mainJFrame() {
        this.initComponents();
    }

    private void initComponents() {
        this.btn_unknown = new JButton();
        this.lb_execution_time = new JLabel();
        this.jdt = new JProgressBar();
        GroupLayout dialog_layout = new GroupLayout(this.dialog.getContentPane());
        this.dialog.getContentPane().setLayout(dialog_layout);
        dialog_layout.setHorizontalGroup(dialog_layout.createParallelGroup(Alignment.LEADING).addGap(0, 400, 32767));
        dialog_layout.setVerticalGroup(dialog_layout.createParallelGroup(Alignment.LEADING).addGap(0, 300, 32767));

        this.setDefaultCloseOperation(3);
        this.setTitle("激光雕刻机");
        this.setBackground(new Color(204, 204, 204));
        this.setLocation(new Point(400, 200));
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                mainJFrame.this.formComponentResized(evt);
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                mainJFrame.this.formWindowOpened(evt);
            }
        });
        this.btn_openpic.setIcon(new ImageIcon(this.getClass().getResource("/tu/tupian.png")));
        this.btn_openpic.setToolTipText("打开图片");
        this.btn_openpic.addActionListener(mainJFrame.this::evt_openpic);
        this.btn_text.setIcon(new ImageIcon(this.getClass().getResource("/tu/wenzi.png")));
        this.btn_text.setToolTipText("输入文字");
        this.btn_text.addActionListener(mainJFrame.this::evt_text);
        this.btn_circle.setIcon(new ImageIcon(this.getClass().getResource("/tu/yuan.png")));
        this.btn_circle.setToolTipText("圆形");
        this.btn_circle.addActionListener(mainJFrame.this::evt_circle);
        this.btn_square.setIcon(new ImageIcon(this.getClass().getResource("/tu/fang.png")));
        this.btn_square.setToolTipText("正方形");
        this.btn_square.addActionListener(mainJFrame.this::evt_square);
        this.btn_heart.setIcon(new ImageIcon(this.getClass().getResource("/tu/xin.png")));
        this.btn_heart.setToolTipText("心形");
        this.btn_heart.addActionListener(mainJFrame.this::evt_heart);
        this.btn_star.setIcon(new ImageIcon(this.getClass().getResource("/tu/5xing.png")));
        this.btn_star.setToolTipText("五角星");
        this.btn_star.addActionListener(mainJFrame.this::evt_star);

        this.btn_preview.setIcon(new ImageIcon(this.getClass().getResource("/tu/ding_wei.png")));
        this.btn_preview.setToolTipText("预览位置");
        this.btn_preview.addActionListener(mainJFrame.this::evt_preview_location);
        this.btn_engrave.setIcon(new ImageIcon(this.getClass().getResource("/tu/diaoke.png")));
        this.btn_engrave.setToolTipText("开始/暂停");
        this.btn_engrave.addActionListener(mainJFrame.this::evt_engrave);
        this.btn_stop.setIcon(new ImageIcon(this.getClass().getResource("/tu/tingzhi.png")));
        this.btn_stop.setToolTipText("停止");
        this.btn_stop.addActionListener((e) -> {
            kai_shi = false;
            Thread t = new Thread(() -> {
                if (this.com_isOpened) {
                    handler.send(new byte[]{22, 0, 4, 0}, 2);
                } else if (mainJFrame.this.wifi.connected) {
                    this.wifi.xie2(new byte[]{22, 0, 4, 0}, 200);
                }
            });
            t.start();
        });

        this.board1.setBackground(new Color(255, 255, 255));
        this.board1.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 255)));
        this.board1.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                mainJFrame.this.hua_ban1MouseDragged(evt);
            }
        });
        this.board1.addMouseWheelListener(mainJFrame.this::hua_ban1MouseWheelMoved);
        this.board1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                mainJFrame.this.hua_ban1MousePressed(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                mainJFrame.this.hua_ban1MouseReleased(evt);
            }
        });
        this.board1.setLayout(new AbsoluteLayout());

        // format inlay hint layout
        this.lb_inlay_h.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_inlay_h.setText("H:");
        this.lb_inlay_w.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_inlay_w.setText("W:");
        this.lb_inlay_x.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_inlay_x.setText(" X:");
        this.lb_inlay_y.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_inlay_y.setText("Y:");
        this.tf_inlay_h.setMinimumSize(new Dimension(6, 20));
        this.tf_inlay_h.setPreferredSize(new Dimension(12, 20));
        this.tf_inlay_h.setText("0");
        this.tf_inlay_w.setMinimumSize(new Dimension(6, 20));
        this.tf_inlay_w.setPreferredSize(new Dimension(12, 20));
        this.tf_inlay_w.setText("0");
        this.tf_inlay_x.setMinimumSize(new Dimension(6, 20));
        this.tf_inlay_x.setPreferredSize(new Dimension(12, 20));
        this.tf_inlay_x.setText("0");
        this.tf_inlay_y.setMinimumSize(new Dimension(6, 20));
        this.tf_inlay_y.setPreferredSize(new Dimension(12, 20));
        this.tf_inlay_y.setText("0");
        this.pn_inlay_hint.setBackground(new Color(255, 255, 255));
        this.pn_inlay_hint.setLayout(new AbsoluteLayout());
        this.pn_inlay_hint.setPreferredSize(new Dimension(3, 3));
        this.pn_inlay_hint.add(this.lb_inlay_h, new AbsoluteConstraints(185, 10, -1, 20));
        this.pn_inlay_hint.add(this.lb_inlay_w, new AbsoluteConstraints(130, 10, -1, 23));
        this.pn_inlay_hint.add(this.lb_inlay_x, new AbsoluteConstraints(10, 10, 24, 23));
        this.pn_inlay_hint.add(this.lb_inlay_y, new AbsoluteConstraints(75, 10, -1, 23));
        this.pn_inlay_hint.add(this.tf_inlay_h, new AbsoluteConstraints(200, 10, 30, 25));
        this.pn_inlay_hint.add(this.tf_inlay_w, new AbsoluteConstraints(150, 10, 30, 25));
        this.pn_inlay_hint.add(this.tf_inlay_x, new AbsoluteConstraints(38, 10, 30, 25));
        this.pn_inlay_hint.add(this.tf_inlay_y, new AbsoluteConstraints(93, 10, 30, 25));
        this.board1.add(this.pn_inlay_hint, new AbsoluteConstraints(30, 20, 20, 10));

        // binding connection
        this.btn_usbconnect.setIcon(new ImageIcon(this.getClass().getResource("/tu/usb2.png")));
        this.btn_usbconnect.setToolTipText("连接设备");
        this.btn_usbconnect.addActionListener(e -> {
            Thread t = new Thread(mainJFrame.this::connect_viaUSB);
            t.start();
        });
        this.btn_wificonnect.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
        this.btn_wificonnect.addActionListener(evt -> {
        });

        // binding settings right-panel
        this.btn_aux_positioning.addActionListener(mainJFrame.this::jButton16ActionPerformed);
        this.btn_aux_positioning.setIcon(new ImageIcon(this.getClass().getResource("/tu/shi_zi.png")));
        this.btn_aux_positioning.setToolTipText("十字定位");
        this.btn_convertbmp.addActionListener(mainJFrame.this::jButton13ActionPerformed);
        this.btn_convertbmp.setIcon(new ImageIcon(this.getClass().getResource("/tu/bmp.png")));
        this.btn_convertbmp.setToolTipText("to bmp");
        this.btn_save.addActionListener(mainJFrame.this::jButton12ActionPerformed);
        this.btn_save.setIcon(new ImageIcon(this.getClass().getResource("/tu/baocun.png")));
        this.btn_save.setToolTipText("保存");
        this.btn_unknown.setFont(new Font("宋体", Font.PLAIN, 14));
        this.btn_unknown.setText("写入");
        this.jdt.setRequestFocusEnabled(false);
        this.jdt.setStringPainted(true);
        this.lb_accuracy.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_accuracy.setText("精度：");
        this.lb_carve_depth.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_carve_depth.setText("深度：%10");
        this.lb_carve_power.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_carve_power.setText("功率：100%");
        this.lb_contrast.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_contrast.setText("对比度：50%");
        this.lb_cut_depth.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_cut_depth.setText("切割深度：10%");
        this.lb_cut_power.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_cut_power.setText("切割功率：100%");
        this.lb_execution_time.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_execution_time.setHorizontalAlignment(4);
        this.lb_execution_time.setText("0.0");
        this.lb_fill.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_fill.setText("填充密度：5");
        this.lb_num_times.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_num_times.setText("次数：");
        this.lb_pwd.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_pwd.setText("密码：");
        this.lb_weak_light.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_weak_light.setText("弱光功率：10%");
        this.lb_wifi.setFont(new Font("宋体", Font.PLAIN, 14));
        this.lb_wifi.setText("WIFI：");
        this.lb_wifi.setToolTipText("");
        this.opt_accuracy.addActionListener(mainJFrame.this::changeAccuracy);
        this.opt_accuracy.setModel(new DefaultComboBoxModel(new String[]{"高", "中", "低"}));
        this.opt_accuracy.setSelectedIndex(2);
        this.opt_num_times.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
        this.sd_carve_depth.addChangeListener(e -> this.lb_carve_depth.setText(bundle.getString("str_shen_du") + this.sd_carve_depth.getValue() + "%"));
        this.sd_carve_depth.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (kai_shi) mainJFrame.this.apply_settings_carving();
            }
        });
        this.sd_carve_depth.setValue(10);
        this.sd_carve_power.addChangeListener(e -> this.lb_carve_power.setText(bundle.getString("str_gong_lv") + this.sd_carve_power.getValue() + "%"));
        this.sd_carve_power.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (kai_shi) mainJFrame.this.apply_settings_carving();
            }
        });
        this.sd_carve_power.setValue(100);
        this.sd_contrast.addChangeListener(mainJFrame.this::jSlider6StateChanged);
        this.sd_cut_depth.addChangeListener(e -> this.lb_cut_depth.setText(bundle.getString("str_shen_du_sl") + this.sd_cut_depth.getValue() + "%"));
        this.sd_cut_depth.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                mainJFrame.this.jSlider4MouseReleased(evt);
            }
        });
        this.sd_cut_depth.setValue(10);
        this.sd_cut_power.addChangeListener(e -> this.lb_cut_power.setText(bundle.getString("str_gong_lv_sl") + this.sd_cut_power.getValue() + "%"));
        this.sd_cut_power.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                mainJFrame.this.jSlider3MouseReleased(evt);
            }
        });
        this.sd_cut_power.setValue(100);
        this.sd_fill.addChangeListener(mainJFrame.this::jSlider7StateChanged);
        this.sd_fill.setMaximum(10);
        this.sd_fill.setToolTipText("");
        this.sd_fill.setValue(5);
        this.sd_weak_light.addChangeListener(e -> this.lb_weak_light.setText(bundle.getString("str_ruo_guang") + this.sd_weak_light.getValue() + "%"));
        this.sd_weak_light.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                mainJFrame.this.jSlider9MouseReleased(evt);
            }
        });
        this.sd_weak_light.setValue(10);

        // format setting panel layout
        this.pn_main_right.setBackground(new Color(204, 204, 204));
        this.pn_main_right.setLayout(new AbsoluteLayout());
        this.pn_main_right.add(this.btn_unknown, new AbsoluteConstraints(70, 500, -1, 30));
        this.pn_main_right.add(this.jTextField5, new AbsoluteConstraints(10, 420, 180, -1));
        this.pn_main_right.add(this.jTextField6, new AbsoluteConstraints(10, 470, 180, -1));
        this.pn_main_right.add(this.lb_accuracy, new AbsoluteConstraints(10, 370, -1, -1));
        this.pn_main_right.add(this.lb_carve_depth, new AbsoluteConstraints(10, 90, -1, -1));
        this.pn_main_right.add(this.lb_carve_power, new AbsoluteConstraints(10, 40, -1, -1));
        this.pn_main_right.add(this.lb_contrast, new AbsoluteConstraints(10, 240, -1, -1));
        this.pn_main_right.add(this.lb_cut_depth, new AbsoluteConstraints(10, 190, -1, -1));
        this.pn_main_right.add(this.lb_cut_power, new AbsoluteConstraints(10, 140, -1, -1));
        this.pn_main_right.add(this.lb_fill, new AbsoluteConstraints(10, 290, -1, -1));
        this.pn_main_right.add(this.lb_num_times, new AbsoluteConstraints(10, 340, -1, -1));
        this.pn_main_right.add(this.lb_pwd, new AbsoluteConstraints(10, 450, -1, -1));
        this.pn_main_right.add(this.lb_weak_light, new AbsoluteConstraints(10, 0, -1, -1));
        this.pn_main_right.add(this.lb_wifi, new AbsoluteConstraints(10, 400, -1, -1));
        this.pn_main_right.add(this.opt_accuracy, new AbsoluteConstraints(120, 370, 70, -1));
        this.pn_main_right.add(this.opt_num_times, new AbsoluteConstraints(120, 340, 70, -1));
        this.pn_main_right.add(this.sd_carve_depth, new AbsoluteConstraints(0, 110, 190, -1));
        this.pn_main_right.add(this.sd_carve_power, new AbsoluteConstraints(0, 60, 190, -1));
        this.pn_main_right.add(this.sd_contrast, new AbsoluteConstraints(0, 260, 190, -1));
        this.pn_main_right.add(this.sd_cut_depth, new AbsoluteConstraints(0, 210, 190, -1));
        this.pn_main_right.add(this.sd_cut_power, new AbsoluteConstraints(0, 160, 190, -1));
        this.pn_main_right.add(this.sd_fill, new AbsoluteConstraints(0, 310, 190, -1));
        this.pn_main_right.add(this.sd_weak_light, new AbsoluteConstraints(0, 20, 190, -1));

        // format main frame layout
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addContainerGap().addGroup(
                                layout.createParallelGroup(Alignment.LEADING).addGroup(
                                        layout.createSequentialGroup()
                                                .addComponent(this.btn_openpic, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_text, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_circle, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_square, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_heart, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_star, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_save, -2, 60, -2)
                                                .addGap(7, 7, 7)
                                                .addComponent(this.btn_convertbmp, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(this.btn_aux_positioning, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_preview, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_engrave, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_stop, -2, 60, -2)
                                                .addGap(54, 54, 54)
                                                .addComponent(this.btn_usbconnect, -2, 60, -2)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.btn_wificonnect, -2, 60, -2)
                                                .addGap(0, 0, 32767)
                                ).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addGroup(
                                                layout.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(this.jdt, Alignment.LEADING, -1, -1, 32767)
                                                        .addComponent(this.board1, -1, -1, 32767))
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                layout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(this.pn_main_right, -2, -1, -2)
                                                        .addComponent(this.lb_execution_time, Alignment.TRAILING, -2, 110, -2)
                                        )
                                )
                        ).addContainerGap()
                )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addContainerGap().addGroup(
                                        layout.createParallelGroup(Alignment.LEADING)
                                                .addComponent(this.btn_openpic, -2, 60, -2)
                                                .addComponent(this.btn_text, -2, 60, -2)
                                                .addComponent(this.btn_circle, -2, 60, -2)
                                                .addComponent(this.btn_square, -2, 60, -2)
                                                .addComponent(this.btn_heart, -2, 60, -2)
                                                .addComponent(this.btn_star, -2, 60, -2)
                                                .addComponent(this.btn_preview, -2, 60, -2)
                                                .addComponent(this.btn_engrave, -2, 60, -2)
                                                .addComponent(this.btn_stop, -2, 60, -2)
                                                .addComponent(this.btn_usbconnect, -2, 60, -2)
                                                .addComponent(this.btn_wificonnect, -2, 60, -2)
                                                .addComponent(this.btn_aux_positioning, -2, 60, -2)
                                                .addComponent(this.btn_save, -2, 60, -2)
                                                .addComponent(this.btn_convertbmp, -2, 60, -2)
                                ).addPreferredGap(ComponentPlacement.RELATED).addGroup(
                                        layout.createParallelGroup(Alignment.LEADING)
                                                .addComponent(this.pn_main_right, -1, -1, 32767)
                                                .addComponent(this.board1, -1, 580, 32767)
                                ).addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(this.jdt, -2, -1, -2)
                                                .addComponent(this.lb_execution_time)
                                )
                )
        );
        this.pack();
    }

    public void up() {
        this.board1.repaint();
    }

    void setLocale() {
        Locale locale = Locale.getDefault();
        if (locale == Locale.CHINA) {
            bundle = ResourceBundle.getBundle("examples.diao_zh_CN");
        } else if (locale == Locale.US) {
            bundle = ResourceBundle.getBundle("examples.diao_en_US");
        } else {
            bundle = ResourceBundle.getBundle("examples.diao_");
        }

        this.btn_openpic.setToolTipText(bundle.getString("str_da_kai"));
        this.btn_text.setToolTipText(bundle.getString("str_wen_zi"));
        this.btn_circle.setToolTipText(bundle.getString("str_yuan"));
        this.btn_square.setToolTipText(bundle.getString("str_fang"));
        this.btn_heart.setToolTipText(bundle.getString("str_xin"));
        this.btn_star.setToolTipText(bundle.getString("str_xing"));

        this.btn_save.setToolTipText(bundle.getString("str_bao_cun"));
        this.btn_convertbmp.setToolTipText(bundle.getString("str_bmp"));
        this.btn_aux_positioning.setToolTipText(bundle.getString("str_shi_zi"));
        this.btn_preview.setToolTipText(bundle.getString("str_yu_lan"));
        this.btn_engrave.setToolTipText(bundle.getString("str_kai_shi"));
        this.btn_stop.setToolTipText(bundle.getString("str_ting_zhi"));
        this.btn_usbconnect.setToolTipText(bundle.getString("str_lian_jie"));

        this.lb_weak_light.setText(bundle.getString("str_ruo_guang") + "10%");
        this.lb_carve_power.setText(bundle.getString("str_gong_lv") + "100%");
        this.lb_carve_depth.setText(bundle.getString("str_shen_du") + "10%");
        this.lb_cut_power.setText(bundle.getString("str_gong_lv_sl") + "100%");
        this.lb_cut_depth.setText(bundle.getString("str_shen_du_sl") + "10%");
        this.lb_num_times.setText(bundle.getString("str_ci_shu"));
        this.lb_accuracy.setText(bundle.getString("str_jing_du"));
        this.opt_accuracy.removeAllItems();
        this.opt_accuracy.addItem(bundle.getString("str_gao"));
        this.opt_accuracy.addItem(bundle.getString("str_zhong"));
        this.opt_accuracy.addItem(bundle.getString("str_di"));

        this.lb_contrast.setText(bundle.getString("str_dui_bi") + "50%");
        this.lb_fill.setText(bundle.getString("str_tian_chong") + "5");
        this.setTitle(bundle.getString("str_ji_guang") + " " + software_version);

        str_font = bundle.getString("str_zi_ti");
        str_typeface = bundle.getString("str_zi_xing");
        str_size = bundle.getString("str_chi_cun");
        str_routine = bundle.getString("str_chang_gui");
        str_italic = bundle.getString("str_xie_ti");
        str_bold = bundle.getString("str_cu_ti");
        str_bold_italic = bundle.getString("str_cu_xie");
        str_vertical = bundle.getString("str_shu");
        str_vector = bundle.getString("str_shi_liang");
        str_outdated = bundle.getString("str_geng_xin");
        str_set = bundle.getString("str_shi_zhi");
        str_firmware = bundle.getString("str_gu_jian");
        str_model = bundle.getString("str_xing_hao");
        str_update = bundle.getString("str_kai_shi_geng_xin");
        str_download_fail = bundle.getString("str_xia_zai_shi_bai");
    }

    void cai_dan() {
        this.jmb = new JMenuBar();
        this.menu1 = new JMenu(str_set + "(S)");
        this.menu1.setMnemonic('s');
        this.item1 = new JMenuItem(str_firmware);
        this.item1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Firmware gj = new Firmware();
                gj.setVisible(true);
                gj.setDefaultCloseOperation(2);
            }
        });
        this.menu1.add(this.item1);
        this.jmb.add(this.menu1);
        this.setJMenuBar(this.jmb);
    }

    private void formWindowOpened(WindowEvent evt) {
        try {
            this.btn_wificonnect.setVisible(false);
            Update.geng_xin();
            FileTransferHandler ft = new FileTransferHandler();
            FileTransferHandler.hb = this.board1;
            this.board1.setTransferHandler(ft);
            this.setLocale();
            BElement ty = new BElement();
            ty.type = 0;
            int i = 0;

            while (true) {
                double var10002;
                double var8;
                if (i >= Board.bHeight / 10 + 1) {
                    i = 0;

                    while (true) {
                        if (i >= Board.bWidth / 10 + 1) {
                            Board.bElements.add(ty);
                            this.up();
                            this.setIconImage((new ImageIcon(this.getClass().getResource("/tu/tu_biao.png"))).getImage());
                            this.lb_wifi.setVisible(false);
                            this.lb_pwd.setVisible(false);
                            this.jTextField5.setVisible(false);
                            this.jTextField6.setVisible(false);
                            this.btn_unknown.setVisible(false);
                            this.board1.window = this;
                            this.board1.pn_inlay_hint = this.pn_inlay_hint;
                            this.board1.pn_settings = this.pn_main_right;
                            this.board1.tf_x = this.tf_inlay_x;
                            this.board1.tf_y = this.tf_inlay_y;
                            this.board1.tf_w = this.tf_inlay_w;
                            this.board1.tf_h = this.tf_inlay_h;
                            this.jdt.setVisible(false);
                            this.tf_inlay_x.addKeyListener(this);
                            this.tf_inlay_y.addKeyListener(this);
                            this.tf_inlay_w.addKeyListener(this);
                            this.tf_inlay_h.addKeyListener(this);
                            this.tf_inlay_x.requestFocus();
                            this.getContentPane().setBackground(new Color(240, 240, 240));
                            this.pn_main_right.setBackground(new Color(240, 240, 240));
                            this.win = this;
                            Runnable runnable2 = () -> {
                                while (true) {
                                    if (mainJFrame.this.wifi == null) {
                                        mainJFrame.this.wifi = new Wifi();
                                        mainJFrame.this.wifi.bt = mainJFrame.this.btn_wificonnect;
                                        mainJFrame.this.wifi.board = mainJFrame.this.board1;
                                        mainJFrame.this.wifi.fbl = mainJFrame.this.opt_accuracy;
                                        mainJFrame.this.wifi.rg = mainJFrame.this.sd_weak_light;
                                        mainJFrame.this.wifi.jdt = mainJFrame.this.jdt;
                                        mainJFrame.this.wifi.window = mainJFrame.this.win;
                                    }

                                    try {
                                        Thread.sleep(1000L);
                                    } catch (InterruptedException var2) {
                                        Logger.getLogger("MAIN").log(Level.SEVERE, null, var2);
                                    }

                                    if (mainJFrame.kai_shi2 && !mainJFrame.this.zan_ting) {
                                        ++mainJFrame.this.miao;
                                        mainJFrame.this.lb_execution_time.setText(mainJFrame.this.miao / 60 + "." + mainJFrame.this.miao % 60);
                                        if (mainJFrame.timeout++ > 3 && mainJFrame.timeout != 0) {
                                            System.out.println("&&&");
                                            mainJFrame.this.jdt.setValue(0);
                                            mainJFrame.this.jdt.setVisible(false);
                                            mainJFrame.kai_shi = false;
                                            mainJFrame.timeout = 0;
                                            mainJFrame.kai_shi2 = false;
                                        }
                                    }
                                }
                            };
                            Thread thread2 = new Thread(runnable2);
                            thread2.start();
                            this.qu_yu();
                            return;
                        }

                        var8 = i;
                        ty.path.moveTo(var8 / Board.resolution * 10.0D, 0.0D);
                        var8 = i;
                        var8 = var8 / Board.resolution * 10.0D;
                        var10002 = Board.bHeight;
                        ty.path.lineTo(var8, var10002 / Board.resolution);
                        ++i;
                    }
                }

                var10002 = i;
                ty.path.moveTo(0.0D, var10002 / Board.resolution * 10.0D);
                var8 = Board.bWidth;
                var8 /= Board.resolution;
                var10002 = i;
                ty.path.lineTo(var8, var10002 / Board.resolution * 10.0D);
                ++i;
            }
        } catch (Exception var6) {
            JOptionPane.showMessageDialog(null, var6);
            Logger.getLogger("MAIN").log(Level.SEVERE, null, var6);
        }
    }

    int qu_anniu(int x, int y) {
        Rectangle rect = BElement.qu_jv_xing(Board.bElements);
        if (x > rect.x - 15 && x < rect.x + 15 && y > rect.y - 15 && y < rect.y + 15) {
            return 1;
        } else if (x > rect.x + rect.width - 15 && x < rect.x + rect.width + 15 && y > rect.y - 15 && y < rect.y + 15) {
            return 2;
        } else if (x > rect.x + rect.width - 15 && x < rect.x + rect.width + 15 && y > rect.y + rect.height - 15 && y < rect.y + rect.height + 15) {
            return 3;
        } else if (x > rect.x - 15 && x < rect.x + 15 && y > rect.y + rect.height - 15 && y < rect.y + rect.height + 15) {
            return 4;
        } else if (x > rect.x + rect.width - 15 && x < rect.x + rect.width + 15 && y > rect.y + rect.height + 20 && y < rect.y + rect.height + 50) {
            return 5;
        } else if (x > rect.x + rect.width + 25 && x < rect.x + rect.width + 25 + 60 && y > rect.y - 20 && y < rect.y - 20 + 65) {
            System.out.println(6);
            return 6;
        } else if (x > rect.x + rect.width + 25 && x < rect.x + rect.width + 25 + 60 && y > rect.y + 45 && y < rect.y + 45 + 65) {
            System.out.println(7);
            return 7;
        } else if (x > rect.x + rect.width + 25 && x < rect.x + rect.width + 25 + 60 && y > rect.y + 110 && y < rect.y + 110 + 65) {
            System.out.println(8);
            return 8;
        } else if (x > rect.x + rect.width + 25 && x < rect.x + rect.width + 25 + 60 && y > rect.y + 175 && y < rect.y + 175 + 65) {
            System.out.println(9);
            return 9;
        } else if (x > rect.x + rect.width - 50 && x < rect.x + rect.width + 50 && y > rect.y + rect.height + 20 && y < rect.y + rect.height + 50) {
            return 10;
        } else if (x > rect.x + rect.width - 85 && x < rect.x + rect.width + 85 && y > rect.y + rect.height + 20 && y < rect.y + rect.height + 50) {
            return 11;
        } else if (x > rect.x + rect.width - 120 && x < rect.x + rect.width + 120 && y > rect.y + rect.height + 20 && y < rect.y + rect.height + 50) {
            return 12;
        } else {
            return x > rect.x + rect.width - 155 && x < rect.x + rect.width + 155 && y > rect.y + rect.height + 20 && y < rect.y + rect.height + 50 ? 13 : 0;
        }
    }

    void shu_an_xia2(MouseEvent evt) {
        this.an_xia = true;
        this.anxia_x = evt.getX();
        this.anxia_y = evt.getY();
        this.anxia_x_1 = this.anxia_x;
        this.anxia_y_1 = this.anxia_y;
        this.an_niu = evt.getButton();
        if (this.an_niu == 1) {
            this.an = this.qu_anniu(this.anxia_x, this.anxia_y);
            int i;
            switch (this.an) {
                case 1:
                    List<BElement> sz = new ArrayList<>();
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if (!Board.bElements.get(i).selected) {
                            sz.add(Board.bElements.get(i));
                        }
                    }
                    Board.bElements = sz;
                    this.up();
                    Undo.tian_jia();
                    return;
                case 2:
                case 3:
                    return;
                case 4:
                    Board.lock = !Board.lock;
                    this.up();
                    return;
                case 5:
                    BElement.center(Board.bElements);
                    this.up();
                    Undo.tian_jia();
                    return;
                case 6:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if ((Board.bElements.get(i)).selected && (Board.bElements.get(i)).type == 1) {
                            (Board.bElements.get(i)).chuli_fs = 1;
                            (Board.bElements.get(i)).chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }
                    return;
                case 7:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if ((Board.bElements.get(i)).selected && (Board.bElements.get(i)).type == 1) {
                            (Board.bElements.get(i)).chuli_fs = 2;
                            (Board.bElements.get(i)).chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }
                    return;
                case 8:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        BElement cur = Board.bElements.get(i);
                        if (cur.selected && cur.type == 1) {
                            cur.chuli_fs = 3;
                            cur.chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }
                    return;
                case 9:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if ((Board.bElements.get(i)).selected && (Board.bElements.get(i)).type == 1) {
                            (Board.bElements.get(i)).chuli_fs = 4;
                            (Board.bElements.get(i)).chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }

                    return;
                case 10:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if ((Board.bElements.get(i)).selected && (Board.bElements.get(i)).type == 1) {
                            (Board.bElements.get(i)).chuli_jxy = !(Board.bElements.get(i)).chuli_jxy;
                            (Board.bElements.get(i)).chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }

                    return;
                case 11:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if ((Board.bElements.get(i)).selected && (Board.bElements.get(i)).type == 1) {
                            (Board.bElements.get(i)).chuli_jxx = !(Board.bElements.get(i)).chuli_jxx;
                            (Board.bElements.get(i)).chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }
                    return;


                case 12:
                    for (i = 0; i < Board.bElements.size(); ++i) {
                        if (Board.bElements.get(i).selected && Board.bElements.get(i).type == 1) {
                            Board.bElements.get(i).chuli_fan = !Board.bElements.get(i).chuli_fan;
                            (Board.bElements.get(i)).chu_li();
                            this.up();
                            Undo.tian_jia();
                            return;
                        }
                    }
                    return;
            }


            Rectangle rect_q;
            int ii;
            if (this.an == 13) {
                rect_q = BElement.qu_jv_xing(Board.bElements);
                BElement ty = BElement.chuang_jian(0, null);
                ty.path = new GeneralPath();
                int wei_zhi = 0;

                for (ii = 0; ii < Board.bElements.size(); ++ii) {
                    if ((Board.bElements.get(ii)).selected && (Board.bElements.get(ii)).type == 0) {
                        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(ii)).path);
                        lu_jing2.transform((Board.bElements.get(ii)).Tx);
                        ty.path.append(lu_jing2, false);
                        ++ii;
                        wei_zhi = ii;
                    }
                }

                if (ii == 1) {
                    ty.fill = !(Board.bElements.get(wei_zhi)).fill;
                } else {
                    ty.fill = true;
                }

                List<BElement> sz = new ArrayList<>();

                for (i = 0; i < Board.bElements.size(); ++i) {
                    if (!(Board.bElements.get(i)).selected || (Board.bElements.get(i)).type == 1) {
                        sz.add(Board.bElements.get(i));
                    }
                }

                Board.bElements = sz;
                ty.selected = true;
                Rectangle r = BElement.qu_jv_xing(ty);
                AffineTransform sf1 = AffineTransform.getTranslateInstance(-r.x, -r.y);
                AffineTransform fb = new AffineTransform(sf1);
                fb.concatenate(ty.Tx);
                ty.Tx = fb;
                AffineTransform sf3 = AffineTransform.getScaleInstance((double) rect_q.width / (double) r.width, (double) rect_q.height / (double) r.height);
                AffineTransform fb3 = new AffineTransform(sf3);
                fb3.concatenate(ty.Tx);
                ty.Tx = fb3;
                AffineTransform sf2 = AffineTransform.getTranslateInstance(rect_q.x, rect_q.y);
                AffineTransform fb2 = new AffineTransform(sf2);
                fb2.concatenate(ty.Tx);
                ty.Tx = fb2;
                Board.bElements.add(ty);
                Undo.tian_jia();
                return;
            }

            rect_q = BElement.qu_jv_xing(Board.bElements);
            if (this.anxia_x > rect_q.x && this.anxia_x < rect_q.x + rect_q.width && this.anxia_y > rect_q.y && this.anxia_y < rect_q.y + rect_q.height) {
                this.kuang = false;
                this.up();
            } else {
                for (i = 1; i < Board.bElements.size(); ++i) {
                    if (!(Board.bElements.get(i)).selected) {
                        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(i)).path);
                        lu_jing2.transform((Board.bElements.get(i)).Tx);
                        Rectangle rect = lu_jing2.getBounds();
                        if (this.anxia_x > rect.x && this.anxia_x < rect.x + rect.width && this.anxia_y > rect.y && this.anxia_y < rect.y + rect.height) {
                            for (ii = 0; ii < Board.bElements.size(); ++ii) {
                                (Board.bElements.get(ii)).selected = false;
                            }

                            (Board.bElements.get(i)).selected = true;
                            BElement ty = Board.bElements.get(i);
                            Board.bElements.remove(i);
                            Board.bElements.add(1, ty);
                            this.sd_contrast.setValue(ty.yu_zhi);
                            this.up();
                            this.kuang = false;
                            return;
                        }
                    }

                    for (ii = 0; ii < Board.bElements.size(); ++ii) {
                        (Board.bElements.get(ii)).selected = false;
                    }

                    this.kuang = true;
                    this.up();
                }
            }
        } else if (this.an_niu == 3) {
        }
    }

    private void hua_ban1MousePressed(MouseEvent evt) {
        this.shu_an_xia2(evt);
        this.tf_inlay_x.requestFocus(true);
    }

    private void hua_ban1MouseReleased(MouseEvent evt) {
        this.an_xia = false;
        BElement.tuo = false;
        if (this.tuo_dong) {
            this.tuo_dong = false;
            Undo.tian_jia();
        }

        this.up();
    }

    void shu_yi_dong2(MouseEvent evt) {
        if (this.an_xia) {
            int dx = evt.getX();
            int dy = evt.getY();
            int x;
            if (this.an_niu == 1) {
                Rectangle rect = BElement.qu_jv_xing(Board.bElements);
                int i;
                if (this.an == 0) {
                    if (this.kuang) {
                        if (dx < this.anxia_x_1) {
                            x = dx;
                            i = this.anxia_x_1 - dx;
                        } else {
                            x = this.anxia_x_1;
                            i = dx - this.anxia_x_1;
                        }

                        int y;
                        int g;
                        if (dy < this.anxia_y_1) {
                            y = dy;
                            g = this.anxia_y_1 - dy;
                        } else {
                            y = this.anxia_y_1;
                            g = dy - this.anxia_y_1;
                        }

                        BElement.tuo = true;
                        BElement.shu_biao = new Rectangle(x, y, i, g);
                        BElement.select_byBoundingBox(Board.bElements, BElement.shu_biao);
                    } else {
                        for (x = 0; x < Board.bElements.size(); ++x) {
                            if ((Board.bElements.get(x)).selected) {
                                (Board.bElements.get(x)).translate(dx - this.anxia_x, dy - this.anxia_y);
                            }
                        }
                    }

                    this.anxia_x = dx;
                    this.anxia_y = dy;
                    this.up();
                } else if (this.an == 2) {
                    float center_x, center_y;
                    float deg1 = 0.0F;
                    float deg2;
                    center_x = (float) (rect.x + rect.width / 2);
                    center_y = (float) (rect.y + rect.height / 2);
                    if ((float) this.anxia_x > center_x && (float) this.anxia_y < center_y) {
                        deg1 = 360.0F - (float) Math.toDegrees(Math.atan((center_y - (float) this.anxia_y) / ((float) this.anxia_x - center_x)));
                    } else if ((float) this.anxia_x < center_x && (float) this.anxia_y < center_y) {
                        deg1 = 270.0F - (float) Math.toDegrees(Math.atan((center_x - (float) this.anxia_x) / (center_y - (float) this.anxia_y)));
                    } else if ((float) this.anxia_x < center_x && (float) this.anxia_y > center_y) {
                        deg1 = 90.0F + (float) Math.toDegrees(Math.atan((center_x - (float) this.anxia_x) / ((float) this.anxia_y - center_y)));
                    } else if ((float) this.anxia_x > center_x && (float) this.anxia_y > center_y) {
                        deg1 = (float) Math.toDegrees(Math.atan(((float) this.anxia_y - center_y) / ((float) this.anxia_x - center_x)));
                    }

                    if ((float) dx > center_x && (float) dy < center_y) {
                        deg2 = 360.0F - (float) Math.toDegrees(Math.atan((center_y - (float) dy) / ((float) dx - center_x)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.anxia_x = dx;
                        this.anxia_y = dy;
                    } else if ((float) dx < center_x && (float) dy < center_y) {
                        deg2 = 270.0F - (float) Math.toDegrees(Math.atan((center_x - (float) dx) / (center_y - (float) dy)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.anxia_x = dx;
                        this.anxia_y = dy;
                    } else if ((float) dx < center_x && (float) dy > center_y) {
                        deg2 = 90.0F + (float) Math.toDegrees(Math.atan((center_x - (float) dx) / ((float) dy - center_y)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.anxia_x = dx;
                        this.anxia_y = dy;
                    } else if ((float) dx > center_x && (float) dy > center_y) {
                        deg2 = (float) Math.toDegrees(Math.atan(((float) dy - center_y) / ((float) dx - center_x)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.anxia_x = dx;
                        this.anxia_y = dy;
                    }

                    this.up();
                }

                if (this.an == 3) {
                    double sf;
                    if (Board.lock) {
                        sf = (double) (this.anxia_x - rect.x) / (double) rect.width;
                        if (sf > 0.0D) {
                            for (i = 0; i < Board.bElements.size(); ++i) {
                                if ((Board.bElements.get(i)).selected) {
                                    (Board.bElements.get(i)).translate(-rect.x, -rect.y);
                                    (Board.bElements.get(i)).scale(sf, sf);
                                    (Board.bElements.get(i)).translate(rect.x, rect.y);
                                }
                            }
                        }

                        this.anxia_x = dx;
                    } else {
                        sf = (double) (this.anxia_x - rect.x) / (double) rect.width;
                        double sf_y = (double) (this.anxia_y - rect.y) / (double) rect.height;
                        if (sf > 0.0D && sf_y > 0.0D) {
                            for (i = 0; i < Board.bElements.size(); ++i) {
                                if ((Board.bElements.get(i)).selected) {
                                    (Board.bElements.get(i)).translate(-rect.x, -rect.y);
                                    (Board.bElements.get(i)).scale(sf, sf_y);
                                    (Board.bElements.get(i)).translate(rect.x, rect.y);
                                }
                            }
                        }

                        this.anxia_x = dx;
                        this.anxia_y = dy;
                    }

                    this.up();
                }

                this.tuo_dong = true;
            } else if (this.an_niu == 3) {
                AffineTransform py = AffineTransform.getTranslateInstance(dx - this.anxia_x, dy - this.anxia_y);
                Board.quan_x = (int) ((double) (Board.quan_x + (dx - this.anxia_x)) / Board.quan_beishu);
                Board.quan_y = (int) ((double) (Board.quan_y + (dy - this.anxia_y)) / Board.quan_beishu);

                for (x = 0; x < Board.bElements.size(); ++x) {
                    AffineTransform fb = new AffineTransform(py);
                    fb.concatenate((Board.bElements.get(x)).Tx);
                    (Board.bElements.get(x)).Tx = fb;
                }

                this.anxia_x = dx;
                this.anxia_y = dy;
                this.up();
            }
        }

    }

    private void hua_ban1MouseDragged(MouseEvent evt) {
        this.shu_yi_dong2(evt);
    }

    public static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    private void evt_openpic(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fc.setAccessory(preview);
        fc.addPropertyChangeListener(preview);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files (.nc,.bmp,.jpg,.png,.jpeg,.gif,.xj,plt)", "nc", "bmp", "jpg", "png", "jpeg", "gif", "xj", "plt");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null && returnVal == 0) {
            File gcodeFile = fc.getSelectedFile();
            String gcodeFilePath = fc.getSelectedFile().getPath();
            String fileName = gcodeFile.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            suffix = suffix.toUpperCase();
            BufferedImage img;
            if (!suffix.equals("BMP") && !suffix.equals("JPG") && !suffix.equals("PNG") && !suffix.equals("JPEG") && !suffix.equals("GIF")) {
                if (suffix.equals("XJ")) {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(gcodeFilePath));

                        try {
                            Board.bElements = (List) ois.readObject();
                        } catch (Throwable var14) {
                            try {
                                ois.close();
                            } catch (Throwable var13) {
                                var14.addSuppressed(var13);
                            }

                            throw var14;
                        }

                        ois.close();
                    } catch (Exception var15) {
                        var15.printStackTrace();
                    }

                    for (int i = 0; i < Board.bElements.size(); ++i) {
                        if ((Board.bElements.get(i)).type == 1) {
                            (Board.bElements.get(i)).wei_tu = new BufferedImage((Board.bElements.get(i)).wt_w, (Board.bElements.get(i)).wt_g, 2);
                            (Board.bElements.get(i)).wei_tu_yuan = new BufferedImage((Board.bElements.get(i)).wty_w, (Board.bElements.get(i)).wty_g, 2);
                            (Board.bElements.get(i)).wei_tu.setRGB(0, 0, (Board.bElements.get(i)).wt_w, (Board.bElements.get(i)).wt_g, (Board.bElements.get(i)).wei_tu_, 0, (Board.bElements.get(i)).wt_w);
                            (Board.bElements.get(i)).wei_tu_yuan.setRGB(0, 0, (Board.bElements.get(i)).wty_w, (Board.bElements.get(i)).wty_g, (Board.bElements.get(i)).wei_tu_yuan_, 0, (Board.bElements.get(i)).wty_w);
                        }
                    }

                    Undo.tian_jia();
                    this.up();
                } else if (suffix.equals("PLT")) {
                    jie_xi_PLT plt = new jie_xi_PLT();
                    plt.analyzePLT(gcodeFile);
                    Undo.tian_jia();
                    this.up();
                }
            } else {
                try {
                    img = ImageIO.read(gcodeFile);
                    Board.bElements.add(BElement.chuang_jian(1, img));
                    Board.selectLast();
                    BElement.center(Board.bElements);
                    Undo.tian_jia();
                    this.up();
                } catch (IOException e) {
                }
            }
        }

    }

    private void hua_ban1MouseWheelMoved(MouseWheelEvent evt) {
        int dx = evt.getX();
        int dy = evt.getY();
        AffineTransform sf2;
        AffineTransform fb;
        if (evt.getPreciseWheelRotation() < 0.0D) {
            AffineTransform sf1 = AffineTransform.getTranslateInstance(-dx, -dy);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                sf2 = new AffineTransform(sf1);
                sf2.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = sf2;
            }

            Board.quan_beishu *= 1.1D;
            AffineTransform sf = AffineTransform.getScaleInstance(1.1D, 1.1D);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                fb = new AffineTransform(sf);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }

            sf2 = AffineTransform.getTranslateInstance(dx, dy);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                fb = new AffineTransform(sf2);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }
        } else {
            GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
            lu_jing2.transform((Board.bElements.get(0)).Tx);
            Rectangle rect = lu_jing2.getBounds();
            sf2 = AffineTransform.getTranslateInstance(-dx, -dy);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                fb = new AffineTransform(sf2);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }

            Board.quan_beishu *= 0.9D;
            fb = AffineTransform.getScaleInstance(0.9D, 0.9D);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                // AffineTransform fb = new AffineTransform(fb);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }

            fb = AffineTransform.getTranslateInstance(dx, dy);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                // AffineTransform fb = new AffineTransform(fb);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }
        }

        this.up();
    }

    public void jie_xi_dxf() throws ParseException {
        try {
            Parser dxfParser = ParserBuilder.createDefaultParser();
            dxfParser.parse(new FileInputStream("C:\\Users\\Administrator\\Desktop\\dxf.dxf"), "UTF-8");
            DXFDocument doc = dxfParser.getDocument();
            Iterator kuai_list = doc.getDXFLayerIterator();

            while (kuai_list.hasNext()) {
                DXFLayer kuai = (DXFLayer) kuai_list.next();
                Iterator shi_ti = kuai.getDXFEntityTypeIterator();

                while (shi_ti.hasNext()) {
                    String shi_ti2 = (String) shi_ti.next();
                    System.out.println(shi_ti2);
                    List<DXFEntity> st = kuai.getDXFEntities(shi_ti2);

                    for (int i = 0; i < st.size(); ++i) {
                        System.out.println(st.get(i).getType());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }

    }

    private void evt_engrave(ActionEvent evt) {
        if (this.com_isOpened || this.wifi.connected) {
            if (!kai_shi) {
                if (Board.boundingBox) {
                    handler.send(new byte[]{33, 0, 4, 0}, 3);
                    Board.boundingBox = false;
                }

                this.xie_ru(new byte[]{22, 0, 4, 0}, 2);
                Thread t = new Thread(() -> {
                    try {
                        mainJFrame.this.btn_engrave.setEnabled(false);
                        mainJFrame.this.tuo_ji2();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                    }
                });
                t.start();
                this.miao = 0;
                timeout = 0;
            }

        }
    }

    private void evt_preview_location(ActionEvent evt) {
        if (kai_shi) return;

        Thread t;
        if (this.com_isOpened) {
            if (Board.boundingBox) {
                t = new Thread(() -> {
                    handler.send(new byte[]{33, 0, 4, 0}, 3);
                    Board.boundingBox = false;
                    this.up();
                });
                t.start();
            } else {
                t = new Thread(() -> {
                    BElement.qu_jvxing(Board.bElements);
                    GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
                    lu_jing2.transform((Board.bElements.get(0)).Tx);
                    Rectangle rect = lu_jing2.getBounds();
                    Rectangle zui_zhong_wjx2 = new Rectangle(BElement.zui_zhong_wjx);
                    AffineTransform sf = AffineTransform.getTranslateInstance(-rect.x, -rect.y);
                    zui_zhong_wjx2 = sf.createTransformedShape(zui_zhong_wjx2).getBounds();
                    sf = AffineTransform.getScaleInstance(1.0D / Board.quan_beishu, 1.0D / Board.quan_beishu);
                    zui_zhong_wjx2 = sf.createTransformedShape(zui_zhong_wjx2).getBounds();
                    if (zui_zhong_wjx2.width >= 2 || zui_zhong_wjx2.height >= 2) {
                        byte kg = (byte) (zui_zhong_wjx2.width >> 8);
                        byte kd = (byte) zui_zhong_wjx2.width;
                        byte gg = (byte) (zui_zhong_wjx2.height >> 8);
                        byte gd = (byte) zui_zhong_wjx2.height;
                        byte xg = (byte) (zui_zhong_wjx2.x + 67 + zui_zhong_wjx2.width / 2 >> 8);
                        byte xd = (byte) (zui_zhong_wjx2.x + 67 + zui_zhong_wjx2.width / 2);
                        byte yg = (byte) (zui_zhong_wjx2.y + zui_zhong_wjx2.height / 2 >> 8);
                        byte yd = (byte) (zui_zhong_wjx2.y + zui_zhong_wjx2.height / 2);
                        if (mainJFrame.handler.send(new byte[]{32, 0, 11, kg, kd, gg, gd, xg, xd, yg, yd}, 1)) {
                        }

                        Board.boundingBox = true;
                        mainJFrame.this.up();
                    }
                });
                t.start();
            }
        } else if (this.wifi.connected) {
            if (Board.boundingBox) {
                t = new Thread(() -> {
                    this.wifi.xie2(new byte[]{33, 0, 4, 0}, 300);
                    Board.boundingBox = false;
                    this.up();
                });
                t.start();
            } else {
                t = new Thread(() -> {
                    BElement.qu_jvxing(Board.bElements);
                    GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
                    lu_jing2.transform((Board.bElements.get(0)).Tx);
                    Rectangle rect = lu_jing2.getBounds();
                    Rectangle zui_zhong_wjx2 = new Rectangle(BElement.zui_zhong_wjx);
                    AffineTransform sf = AffineTransform.getTranslateInstance(-rect.x, -rect.y);
                    zui_zhong_wjx2 = sf.createTransformedShape(zui_zhong_wjx2).getBounds();
                    sf = AffineTransform.getScaleInstance(1.0D / Board.quan_beishu, 1.0D / Board.quan_beishu);
                    zui_zhong_wjx2 = sf.createTransformedShape(zui_zhong_wjx2).getBounds();
                    byte kg = (byte) (zui_zhong_wjx2.width >> 8);
                    byte kd = (byte) zui_zhong_wjx2.width;
                    byte gg = (byte) (zui_zhong_wjx2.height >> 8);
                    byte gd = (byte) zui_zhong_wjx2.height;
                    byte xg = (byte) (zui_zhong_wjx2.x + 67 + zui_zhong_wjx2.width / 2 >> 8);
                    byte xd = (byte) (zui_zhong_wjx2.x + 67 + zui_zhong_wjx2.width / 2);
                    byte yg = (byte) (zui_zhong_wjx2.y + zui_zhong_wjx2.height / 2 >> 8);
                    byte yd = (byte) (zui_zhong_wjx2.y + zui_zhong_wjx2.height / 2);
                    mainJFrame.this.wifi.xie2(new byte[]{32, 0, 11, kg, kd, gg, gd, xg, xd, yg, yd}, 100);
                    Board.boundingBox = true;
                    mainJFrame.this.up();
                });
                t.start();
            }
        }


    }

    private void evt_circle(ActionEvent evt) {
        Board.bElements.add(BElement.chuang_jian(2, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.tian_jia();
        this.up();
    }

    private void evt_heart(ActionEvent evt) {
        Board.bElements.add(BElement.chuang_jian(3, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.tian_jia();
        this.up();
    }

    private void evt_star(ActionEvent evt) {
        Board.bElements.add(BElement.chuang_jian(4, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.tian_jia();
        this.up();
    }

    private void evt_text(ActionEvent evt) {
        FontDialog dialog = new FontDialog(this.board1, true);
        dialog.setDefaultCloseOperation(2);
        dialog.setVisible(true);
    }

    private void evt_square(ActionEvent evt) {
        Board.bElements.add(BElement.chuang_jian(0, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.tian_jia();
        this.up();
    }

    private void formComponentResized(ComponentEvent evt) {
    }


    void apply_settings_carving() {
        Thread t = new Thread(() -> {
            int sd = this.sd_carve_depth.getValue();
            int gl = this.sd_carve_power.getValue() * 10;
            if (this.com_isOpened) {
                handler.send_settings(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 2);
            } else if (this.wifi.connected) {
                this.wifi.xie2(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 200);
            }

        });
        t.start();
    }

    void apply_settings_cutting() {
        Thread t = new Thread(() -> {
            int sd = this.sd_cut_depth.getValue();
            int gl = this.sd_cut_power.getValue() * 10;
            if (this.com_isOpened) {
                handler.send_settings(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 2);
            } else if (this.wifi.connected) {
                this.wifi.xie2(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 200);
            }

        });
        t.start();
    }

    void she_zhi_can_shu() {
        Thread t = new Thread(() -> {
            int rg = this.sd_weak_light.getValue() * 2;
            int jd = this.opt_accuracy.getSelectedIndex();
            if (this.com_isOpened) {
                handler.send(new byte[]{40, 0, 11, (byte) rg, (byte) jd, 0, 0, 0, 0, 0, 0}, 2);
            } else if (this.wifi.connected) {
                this.wifi.xie2(new byte[]{40, 0, 11, (byte) rg, (byte) jd, 0, 0, 0, 0, 0, 0}, 200);
            }
        });
        t.start();
    }

    private void jSlider9MouseReleased(MouseEvent evt) {
        if (this.com_isOpened) {
            this.she_zhi_can_shu();
        } else if (this.wifi.connected) {
            Thread t = new Thread(() -> {
                int rg = mainJFrame.this.sd_weak_light.getValue() * 2;
                int jd = mainJFrame.this.opt_accuracy.getSelectedIndex();
                mainJFrame.this.wifi.xie2(new byte[]{40, 0, 11, (byte) rg, (byte) jd, 0, 0, 0, 0, 0, 0}, 200);
            });
            t.start();
        }

    }

    private void changeAccuracy(ActionEvent evt) {
        if (this.wifi != null) {
            if (this.com_isOpened || this.wifi.connected) {
                if (this.driver_version[2] == 37) {
                    if (this.opt_accuracy.getSelectedIndex() == 0) {
                        Board.resolution = 0.064D;
                    } else if (this.opt_accuracy.getSelectedIndex() == 1) {
                        Board.resolution = 0.08D;
                    } else if (this.opt_accuracy.getSelectedIndex() == 2) {
                        Board.resolution = 0.096D;
                    }
                } else if (this.opt_accuracy.getSelectedIndex() == 0) {
                    Board.resolution = 0.05D;
                } else if (this.opt_accuracy.getSelectedIndex() == 1) {
                    Board.resolution = 0.0625D;
                } else if (this.opt_accuracy.getSelectedIndex() == 2) {
                    Board.resolution = 0.075D;
                }

                this.board1.di_tu();
                this.she_zhi_can_shu();
            }

        }
    }

    private void jButton16ActionPerformed(ActionEvent evt) {
        Thread t = new Thread(() -> {
            if (mainJFrame.this.com_isOpened) {
                if (mainJFrame.this.shi_zi) {
                    mainJFrame.handler.send(new byte[]{7, 0, 4, 0}, 2);
                } else {
                    mainJFrame.handler.send(new byte[]{6, 0, 4, 0}, 2);
                }

                mainJFrame.this.shi_zi = !mainJFrame.this.shi_zi;
            } else if (mainJFrame.this.wifi.connected) {
                if (mainJFrame.this.shi_zi) {
                    mainJFrame.this.wifi.xie2(new byte[]{7, 0, 4, 0}, 200);
                } else {
                    mainJFrame.this.wifi.xie2(new byte[]{6, 0, 4, 0}, 200);
                }

                mainJFrame.this.shi_zi = !mainJFrame.this.shi_zi;
            }

        });
        t.start();
    }

    private void jButton12ActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files (.xj)", "xj");
        chooser.setFileFilter(filter);
        int option = chooser.showSaveDialog(this);
        if (option == 0) {
            for (int i = 0; i < Board.bElements.size(); ++i) {
                if (Board.bElements.get(i).type == 1) {
                    Board.bElements.get(i).wt_w = Board.bElements.get(i).wei_tu.getWidth();
                    Board.bElements.get(i).wt_g = Board.bElements.get(i).wei_tu.getHeight();
                    Board.bElements.get(i).wty_w = Board.bElements.get(i).wei_tu_yuan.getWidth();
                    Board.bElements.get(i).wty_g = (Board.bElements.get(i)).wei_tu_yuan.getHeight();
                    (Board.bElements.get(i)).wei_tu_ = new int[(Board.bElements.get(i)).wei_tu.getWidth() * (Board.bElements.get(i)).wei_tu.getHeight()];
                    (Board.bElements.get(i)).wei_tu.getRGB(0, 0, (Board.bElements.get(i)).wei_tu.getWidth(), (Board.bElements.get(i)).wei_tu.getHeight(), (Board.bElements.get(i)).wei_tu_, 0, (Board.bElements.get(i)).wei_tu.getWidth());
                    (Board.bElements.get(i)).wei_tu_yuan_ = new int[(Board.bElements.get(i)).wei_tu_yuan.getWidth() * (Board.bElements.get(i)).wei_tu_yuan.getHeight()];
                    (Board.bElements.get(i)).wei_tu_yuan.getRGB(0, 0, (Board.bElements.get(i)).wei_tu_yuan.getWidth(), (Board.bElements.get(i)).wei_tu_yuan.getHeight(), (Board.bElements.get(i)).wei_tu_yuan_, 0, (Board.bElements.get(i)).wei_tu_yuan.getWidth());
                }
            }

            BElement.hui_fu();
            BufferedImage tu_diaoke2 = BElement.qu_tu(Board.bElements);
            BElement.hui_fu_xian_chang();
            String ss = chooser.getSelectedFile().getPath();
            ss = ss.toLowerCase();
            if (!ss.endsWith(".xj")) {
                ss = ss + ".xj";
            }

            File f;
            if (tu_diaoke2 != null) {
                f = new File(ss + ".bmp");

                try {
                    BMPEncoder.write(tu_diaoke2, f);
                } catch (IOException var14) {
                    Logger.getLogger(BElement.class.getName()).log(Level.SEVERE, null, var14);
                }
            }

            f = new File(ss);

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));

                try {
                    oos.writeObject(Board.bElements);
                } catch (Throwable var12) {
                    try {
                        oos.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                    }

                    throw var12;
                }

                oos.close();
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }

    }

    private void jButton13ActionPerformed(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fc.setAccessory(preview);
        fc.addPropertyChangeListener(preview);
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files (.bmp,.jpg,.png,.jpeg,.gif)", "bmp", "jpg", "png", "jpeg", "gif");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null && returnVal == 0) {
            File[] files = fc.getSelectedFiles();
            File[] var7 = files;
            int var8 = files.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                File file = var7[var9];

                try {
                    BufferedImage image = ImageIO.read(file);
                    BufferedImage mBitmap = new BufferedImage(image.getWidth(), image.getHeight(), 2);
                    mBitmap.createGraphics().drawImage(image, 0, 0, null);
                    File output = new File(file.getPath() + ".bmp");
                    BMPEncoder.write(mBitmap, output);
                } catch (IOException e) {
                    Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                }
            }
        }

    }

    private void jSlider6StateChanged(ChangeEvent evt) {
        this.lb_contrast.setText(bundle.getString("str_dui_bi") + this.sd_contrast.getValue() + "%");
        if (Board.bElements.size() >= 2) {
            for (int i = 0; i < Board.bElements.size(); ++i) {
                if ((Board.bElements.get(i)).selected && (Board.bElements.get(i)).type == 1) {
                    (Board.bElements.get(i)).yu_zhi = this.sd_contrast.getValue();
                    (Board.bElements.get(i)).chu_li();
                }
            }

            this.up();
        }
    }

    private void jSlider7StateChanged(ChangeEvent evt) {
        this.lb_fill.setText(bundle.getString("str_tian_chong") + this.sd_fill.getValue());
        BElement.tian_chong_md = this.sd_fill.getValue();
        this.up();
    }

    private void jButton4ActionPerformed(ActionEvent evt) {
        this.qu_yu();
    }

    private void jSlider3MouseReleased(MouseEvent evt) {
        if (kai_shi) {
            this.apply_settings_cutting();
        }
    }

    private void jSlider4MouseReleased(MouseEvent evt) {
        if (kai_shi) {
            this.apply_settings_cutting();
        }
    }

    void qu_yu() {
        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing2.transform((Board.bElements.get(0)).Tx);
        Rectangle rect = lu_jing2.getBounds();
        System.out.print(rect);
        AffineTransform fb;
        if (rect.width > this.board1.getWidth() || rect.height > this.board1.getHeight()) {
            double b;
            if (rect.width - this.board1.getWidth() > rect.height - this.board1.getHeight()) {
                b = (double) this.board1.getWidth() / (double) rect.width;
            } else {
                b = (double) this.board1.getHeight() / (double) rect.height;
            }

            Board.quan_beishu *= b;
            AffineTransform sf = AffineTransform.getScaleInstance(b, b);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                fb = new AffineTransform(sf);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }
        }

        lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing2.transform((Board.bElements.get(0)).Tx);
        rect = lu_jing2.getBounds();
        int x1 = rect.x + rect.width / 2;
        int y1 = rect.y + rect.height / 2;
        int x2 = this.board1.getWidth() / 2;
        int y2 = this.board1.getHeight() / 2;
        fb = AffineTransform.getTranslateInstance(x2 - x1, y2 - y1);

        for (int i = 0; i < Board.bElements.size(); ++i) {
            // AffineTransform fb = new AffineTransform(fb);
            fb.concatenate((Board.bElements.get(i)).Tx);
            (Board.bElements.get(i)).Tx = fb;
        }

        this.up();
    }

    boolean kai_shi_tuo_ji(int shan_qu, int ban_ben, int kuan_wt, int gao_wt, int weizhi_wt, int gonglv_wt, int shendu_wt, int kuan_sl, int gao_sl, int weizhi_sl, int gonglv_sl, int shendu_sl, int dianshu_sl, int z, int s, int ci_shu, int z_sl, int s_sl) {
        byte[] sz = new byte[]{35, 0, 38, (byte) (shan_qu >> 8), (byte) shan_qu, (byte) ban_ben, (byte) (kuan_wt >> 8), (byte) kuan_wt, (byte) (gao_wt >> 8), (byte) gao_wt, (byte) (weizhi_wt >> 8), (byte) weizhi_wt, (byte) (gonglv_wt >> 8), (byte) gonglv_wt, (byte) (shendu_wt >> 8), (byte) shendu_wt, (byte) (kuan_sl >> 8), (byte) kuan_sl, (byte) (gao_sl >> 8), (byte) gao_sl, (byte) (weizhi_sl >> 24), (byte) (weizhi_sl >> 16), (byte) (weizhi_sl >> 8), (byte) weizhi_sl, (byte) (gonglv_sl >> 8), (byte) gonglv_sl, (byte) (shendu_sl >> 8), (byte) shendu_sl, (byte) (dianshu_sl >> 24), (byte) (dianshu_sl >> 16), (byte) (dianshu_sl >> 8), (byte) dianshu_sl, (byte) (z >> 8), (byte) z, (byte) (s >> 8), (byte) s, (byte) ci_shu, 0};
        if (this.com_isOpened) {
            return handler.fa_song_fe(sz, 2);
        } else {
            return this.wifi.connected && this.wifi.kaishi(sz, 22);
        }
    }

    boolean xie_ru(byte[] m, int chao_shi) {
        return !this.com_isOpened ? this.wifi.xie_shuju(m, chao_shi * 100) : handler.send(m, chao_shi);
    }

    byte jiao_yan(byte[] bao) {
        int sum = 0;

        for (int i = 0; i < bao.length - 1; ++i) {
            int a = 255 & bao[i];
            sum += a;
        }

        if (sum > 255) {
            sum = ~sum;
            ++sum;
        }

        sum &= 255;
        return (byte) sum;
    }

    int jiao_yan2(byte[] m) {
        int jiao = 0;

        for (byte b : m) {
            jiao += b;
        }

        if (jiao > 255) {
            jiao = ~jiao;
            ++jiao;
        }

        jiao &= 255;
        return jiao;
    }

    void tuo_ji2() {
        int jishu = 0;
        int k = 0;
        int g = 0;
        int k_sl = 0;
        int g_sl = 0;
        int wz_sl = 1;
        int k_len = 0;
        boolean wt_ = false;
        boolean sl_ = false;
        boolean cuo = true;
        byte bl = 0;
        byte[] bao;
        int len = 0;
        BElement.hui_fu();
        BufferedImage tu_diaoke2 = BElement.qu_tu(Board.bElements);
        List<BPoint> bPoints = BElement.qu_dian(Board.bElements);
        BElement.hui_fu_xian_chang();
        if (tu_diaoke2 == null && bPoints == null) {
            this.btn_engrave.setEnabled(true);
        } else {
            // int jishu = 0;
            // int wz_sl;
            if (tu_diaoke2 != null) {
                g = tu_diaoke2.getHeight();
                k = tu_diaoke2.getWidth();
                if (tu_diaoke2.getWidth() % 8 > 0) {
                    bao = new byte[tu_diaoke2.getWidth() / 8 + 1];
                    k_len = tu_diaoke2.getWidth() / 8 + 1;
                } else {
                    bao = new byte[tu_diaoke2.getWidth() / 8];
                    k_len = tu_diaoke2.getWidth() / 8;
                }

                wz_sl = 33 + g * bao.length;
                wt_ = true;
                len = bao.length;
            } else {
                wz_sl = 33;
            }

            if (bPoints != null) {
                k_sl = BElement.zui_zhong_wjx.width;
                g_sl = BElement.zui_zhong_wjx.height;
                sl_ = true;
            } else {
                bPoints = new ArrayList<>();
            }

            int z = BElement.zui_zhong_wjx.x + BElement.zui_zhong_wjx.width / 2 + 67;
            int s = BElement.zui_zhong_wjx.y + BElement.zui_zhong_wjx.height / 2;
            int z_sl = BElement.shi_liang_wjx.x + BElement.shi_liang_wjx.width / 2 + 67;
            int s_sl = BElement.shi_liang_wjx.y + BElement.shi_liang_wjx.height / 2;
            bao = new byte[wz_sl - 33 + bPoints.size() * 4];
            kai_shi = true;
            this.kai_shi_tuo_ji((33 + len * g + bPoints.size() * 4) / 4094 + 1, 1, k, g, 33, this.sd_carve_power.getValue() * 10, this.sd_carve_depth.getValue(), k_sl, g_sl, wz_sl, this.sd_cut_power.getValue() * 10, this.sd_cut_depth.getValue(), bPoints.size(), z, s, this.opt_num_times.getSelectedIndex() + 1, z_sl, s_sl);

            try {
                Thread.sleep(40 * ((33 + len * g + bPoints.size() * 4) / 4094 + 1));
            } catch (InterruptedException var31) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var31);
            }

            this.xie_ru(new byte[]{10, 0, 4, 0}, 1);

            try {
                Thread.sleep(500L);
            } catch (InterruptedException var30) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var30);
            }

            this.xie_ru(new byte[]{10, 0, 4, 0}, 1);

            try {
                Thread.sleep(500L);
            } catch (InterruptedException var29) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var29);
            }

            byte[] yi;
            boolean chong_fa;
            if (wt_) {
                int[] pixels = new int[k * g];
                tu_diaoke2.getRGB(0, 0, k, g, pixels, 0, k);
                yi = new byte[]{-128, 64, 32, 16, 8, 4, 2, 1};

                for (int i = 0; i < g; ++i) {
                    for (int j = 0; j < k_len; ++j) {
                        chong_fa = false;
                        // byte bl = 0;

                        for (int ba = 0; ba < 8; ++ba) {
                            if (i * k + j * 8 + ba < pixels.length) {
                                int clr = pixels[i * k + j * 8 + ba];
                                clr = (clr & 16711680) >> 16;
                                if (clr < 10) {
                                    bl |= yi[ba];
                                }
                            }
                        }

                        bao[jishu] = bl;
                        ++jishu;
                    }
                }
            }

            if (sl_) {
                for (int j = 0; j < bPoints.size(); ++j) {
                    bao[jishu++] = (byte) ((BPoint) ((List) bPoints).get(j)).x;
                    bao[jishu++] = (byte) (((BPoint) ((List) bPoints).get(j)).x >> 8);
                    bao[jishu++] = (byte) ((BPoint) ((List) bPoints).get(j)).y;
                    bao[jishu++] = (byte) (((BPoint) ((List) bPoints).get(j)).y >> 8);
                }
            }

            int bao_chicuo = 1900;
            yi = new byte[bao_chicuo + 4];

            for (int i = 0; i < bao.length / bao_chicuo; ++i) {
                for (int j = 0; j < bao_chicuo; ++j) {
                    yi[j + 3] = bao[i * bao_chicuo + j];
                }

                do {
                    yi[0] = 34;
                    yi[1] = (byte) (yi.length >> 8);
                    yi[2] = (byte) yi.length;
                    yi[yi.length - 1] = this.jiao_yan(yi);
                    chong_fa = !this.xie_ru(yi, 2);
                } while (chong_fa);

                this.jdt.setVisible(true);
                this.jdt.setValue((int) ((float) i / (float) (bao.length / bao_chicuo) * 100.0F));
            }

            yi = new byte[bao.length % bao_chicuo + 4];
            if (bao.length % bao_chicuo > 0) {
                for (int i = 0; i < bao.length % bao_chicuo; ++i) {
                    yi[i + 3] = bao[bao.length / bao_chicuo * bao_chicuo + i];
                }

                chong_fa = false;

                do {
                    yi[0] = 34;
                    yi[1] = (byte) (yi.length >> 8);
                    yi[2] = (byte) yi.length;
                    yi[yi.length - 1] = this.jiao_yan(yi);
                    chong_fa = !this.xie_ru(yi, 2);
                } while (chong_fa);
            }

            try {
                Thread.sleep(200L);
            } catch (InterruptedException var28) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var28);
            }

            this.xie_ru(new byte[]{36, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 1);

            try {
                Thread.sleep(200L);
            } catch (InterruptedException var27) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var27);
            }

            this.xie_ru(new byte[]{36, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 1);

            try {
                Thread.sleep(500L);
            } catch (InterruptedException var26) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var26);
            }

            kai_shi2 = true;
            if (handler != null) {
                handler.terminate_count = 0;
                handler.terminate_type = 3;
                handler.terminate_buffer.clear();
            }

            this.jdt.setVisible(false);
            this.btn_engrave.setEnabled(true);
        }
    }

    void connect_viaUSB() {
        if (!this.com_isOpened) {
            // getting all serial ports
            List<String> p = SerialPortUtil.getSerialPortList();
            p.forEach(System.out::println);

            int i;
            for (i = 0; i < p.size(); ++i) {
                if (p.get(i).contains("Bluetooth")) continue;

                // not a Bluetooth port, which implies USB port
                try {
                    this.port = SerialPortUtil.openSerialPort(p.get(i), 115200);
                    handler = new Com(this.port);
                    if (handler.send(new byte[]{10, 0, 4, 0}, 2)) {
                        handler.jdt = this.jdt;
                        toggle_connect_status(true);
                        if (handler.read_version()) {
                            this.driver_version = new byte[]{handler.ret_val[0], handler.ret_val[1], handler.ret_val[2]};
                            this.board1.version(this.driver_version, 2);
                            if (this.driver_version[2] == 37) {
                                if (Board.resolution == 0.096D) {
                                    this.opt_accuracy.setSelectedIndex(2);
                                } else if (Board.resolution == 0.08D) {
                                    this.opt_accuracy.setSelectedIndex(1);
                                } else if (Board.resolution == 0.064D) {
                                    this.opt_accuracy.setSelectedIndex(0);
                                }
                            } else if (Board.resolution == 0.075D) {
                                this.opt_accuracy.setSelectedIndex(2);
                            } else if (Board.resolution == 0.0625D) {
                                this.opt_accuracy.setSelectedIndex(1);
                            } else if (Board.resolution == 0.05D) {
                                this.opt_accuracy.setSelectedIndex(0);
                            }

                            this.qu_yu();
                        }
                        break;
                    }
                    handler.close();
                } catch (Exception e) {
                    Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                }
            }
        } else {
            handler.close();
            handler = null;
            toggle_connect_status(false);
        }
    }

    void toggle_connect_status(boolean connected) {
        this.com_isOpened = connected;
        if (connected) {
            this.btn_usbconnect.setIcon(
                    new ImageIcon(
                            Objects.requireNonNull(
                                    this.getClass().getResource("/tu/usb.png")
                            )
                    )
            );
        } else {
            this.btn_usbconnect.setIcon(
                    new ImageIcon(
                            Objects.requireNonNull(
                                    this.getClass().getResource("/tu/usb2.png")
                            )
                    )
            );
        }
    }

    boolean du_banben() {
        final Object suo_ = new Object();
        synchronized (this.suo_fhm) {
            this.fan_hui_ma = false;
            SerialPortUtil.sendData(this.port, new byte[]{-1, 0, 4, 0});
        }

        Runnable runnable2 = new Runnable() {
            public void run() {
                synchronized (suo_) {
                    for (int i = 200; i > 0; --i) {
                        synchronized (mainJFrame.this.suo_fhm) {
                            if (mainJFrame.this.fan_hui_ma) {
                                if (mainJFrame.handler.ret_val.length != 3) {
                                    mainJFrame.this.fan_hui_ma = false;
                                }
                                break;
                            }
                        }

                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException var6) {
                            Logger.getLogger("MAIN").log(Level.SEVERE, null, var6);
                        }
                    }

                }
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();

        try {
            Thread.sleep(100L);
        } catch (InterruptedException var7) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, var7);
        }

        synchronized (suo_) {
            return this.fan_hui_ma;
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
        } catch (Exception e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }

        EventQueue.invokeLater(() -> (new mainJFrame()).setVisible(true));
    }

    void set() {
        double x;
        int i;
        if (!Objects.equals(this.tf_inlay_x.getText(), "")) {
            x = (double) Integer.valueOf(this.tf_inlay_x.getText());
            x -= this.board1.val_x;
            x /= Board.resolution;
            x *= Board.quan_beishu;

            for (i = 0; i < Board.bElements.size(); ++i) {
                if ((Board.bElements.get(i)).selected) {
                    (Board.bElements.get(i)).translate((int) x, (Board.bElements.get(i)).path.getBounds().y);
                }
            }

            this.up();
        }

        if (this.tf_inlay_y.getText() != "") {
            x = (double) Integer.valueOf(this.tf_inlay_y.getText());
            x -= this.board1.val_y;
            x /= Board.resolution;
            x *= Board.quan_beishu;

            for (i = 0; i < Board.bElements.size(); ++i) {
                if ((Board.bElements.get(i)).selected) {
                    (Board.bElements.get(i)).translate((Board.bElements.get(i)).path.getBounds().x, (int) x);
                }
            }

            this.up();
        }

        Rectangle rect;
        if (this.tf_inlay_w.getText() != "") {
            x = (double) Integer.valueOf(this.tf_inlay_w.getText());
            x /= this.board1.val_w;
            rect = BElement.qu_jv_xing(Board.bElements);

            for (i = 0; i < Board.bElements.size(); ++i) {
                if ((Board.bElements.get(i)).selected) {
                    (Board.bElements.get(i)).translate(-rect.x, -rect.y);
                    if (Board.lock) {
                        (Board.bElements.get(i)).scale(x, x);
                    } else {
                        (Board.bElements.get(i)).scale(x, 1.0D);
                    }

                    (Board.bElements.get(i)).translate(rect.x, rect.y);
                }
            }

            this.up();
        }

        if (this.tf_inlay_h.getText() != "") {
            x = (double) Integer.valueOf(this.tf_inlay_h.getText());
            x /= this.board1.val_h;
            rect = BElement.qu_jv_xing(Board.bElements);

            for (i = 0; i < Board.bElements.size(); ++i) {
                if ((Board.bElements.get(i)).selected) {
                    (Board.bElements.get(i)).translate(-rect.x, -rect.y);
                    if (Board.lock) {
                        (Board.bElements.get(i)).scale(x, x);
                    } else {
                        (Board.bElements.get(i)).scale(1.0D, x);
                    }

                    (Board.bElements.get(i)).translate(rect.x, rect.y);
                }
            }

            this.up();
        }

    }

    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (e.isControlDown() && e.getKeyCode() == 67) {
            this.ty_fu_zhi.clear();

            for (int i = 0; i < Board.bElements.size(); ++i) {
                if ((Board.bElements.get(i)).selected) {
                    this.ty_fu_zhi.add(BElement.copy(Board.bElements.get(i)));
                }
            }

            this.fu_zhi = true;
        } else if (e.isControlDown() && e.getKeyCode() == 86) {
            if (this.fu_zhi) {
                for (BElement BEle : this.ty_fu_zhi) {
                    Board.bElements.add(BElement.copy(BEle));
                }

                this.up();
                Undo.tian_jia();
            }
        } else if (e.isControlDown() && e.getKeyCode() == 65) {
            for (int i = 1; i < Board.bElements.size(); ++i) {
                (Board.bElements.get(i)).selected = true;
            }

            this.up();
        } else if (e.isControlDown() && e.getKeyCode() == 90) {
            Undo.che_xiao();
            this.up();
        } else if (e.isControlDown() && e.getKeyCode() == 88) {
            for (int i = 1; i < Board.bElements.size(); ++i) {
                (Board.bElements.get(i)).selected = true;
            }

            Undo.chong_zuo();
            this.up();
        }

        if (c == 10) {
            this.set();
        } else if (c == 127) {
            List<BElement> sz = new ArrayList<>();

            for (int i = 0; i < Board.bElements.size(); ++i) {
                if (!(Board.bElements.get(i)).selected) {
                    sz.add(Board.bElements.get(i));
                }
            }

            Board.bElements = sz;
            this.up();
            Undo.tian_jia();
        }

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
