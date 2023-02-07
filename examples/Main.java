package examples;

import gnu.io.SerialPort;
import net.sf.image4j.codec.bmp.BMPEncoder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
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

public class Main extends JFrame implements KeyListener {
    public final static String software_version = "v1.1.1";

    List<BElement> bElementsCopy = new ArrayList<>();
    SerialPort port = null;
    boolean clicked = false;
    boolean comOpened = false;
    boolean copied = false;
    boolean auxPositioning = false;
    boolean dragging = false;
    boolean paused = false;
    int an = 0;
    int btnActive = 0;
    int clickX = 0;
    int clickX1 = 0;
    int clickY = 0;
    int clickY1 = 0;
    int sec = 0;
    private boolean kuang = false;
    public Wifi wifi = null;
    public byte[] driver_version = new byte[]{0, 0, 0, 0};
    public static Com handler = null;
    public static ResourceBundle bundle = null;
    public static boolean kai_shi = false;
    public static boolean kai_shi2 = false;
    public static int timeout = 0;

    Main window = null;

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

    private final Board board = new Board();
    private final JPanel pn_inlay_hint = new JPanel();
    private final JPanel pn_main_right = new JPanel();
    private final JDialog dialog = new JDialog();

    // buttons that draw shapes on board
    private final JButton btn_openpic = new JButton();
    private final JButton btn_text = new JButton();
    private final JButton btn_circle = new JButton();
    private final JButton btn_square = new JButton();
    private final JButton btn_heart = new JButton();
    private final JButton btn_star = new JButton();

    // buttons resp for main functions
    private final JButton btn_convertbmp = new JButton();
    private final JButton btn_save = new JButton();
    private final JButton btn_preview = new JButton();
    private final JButton btn_engrave = new JButton();
    private final JButton btn_aux_positioning = new JButton();
    private final JButton btn_stop = new JButton();
    private final JButton btn_usbconnect = new JButton();
    private final JButton btn_wificonnect = new JButton();
    private final JButton btn_unknown = new JButton();

    private final JLabel lb_inlay_x = new JLabel();
    private final JLabel lb_inlay_y = new JLabel();
    private final JLabel lb_inlay_h = new JLabel();
    private final JLabel lb_inlay_w = new JLabel();

    private final JLabel lb_wifi = new JLabel();
    private final JLabel lb_pwd = new JLabel();
    private final JLabel lb_execution_time = new JLabel();

    // labels in right-panel settings
    private final JLabel lb_weak_light = new JLabel();
    private final JLabel lb_carve_power = new JLabel();
    private final JLabel lb_carve_depth = new JLabel();
    private final JLabel lb_cut_power = new JLabel();
    private final JLabel lb_cut_depth = new JLabel();
    private final JLabel lb_contrast = new JLabel();
    private final JLabel lb_fill = new JLabel();
    private final JLabel lb_num_times = new JLabel();
    private final JLabel lb_accuracy = new JLabel();

    // interactive controllers in right-panel settings
    private final JSlider sd_weak_light = new JSlider();
    private final JSlider sd_carve_power = new JSlider();
    private final JSlider sd_carve_depth = new JSlider();
    private final JSlider sd_cut_power = new JSlider();
    private final JSlider sd_cut_depth = new JSlider();
    private final JSlider sd_contrast = new JSlider();
    private final JSlider sd_fill = new JSlider();
    private final JComboBox<String> opt_num_times = new JComboBox<>();
    private final JComboBox<String> opt_accuracy = new JComboBox<>();

    private final JTextField tf_inlay_x = new JTextField();
    private final JTextField tf_inlay_y = new JTextField();
    private final JTextField tf_inlay_w = new JTextField();
    private final JTextField tf_inlay_h = new JTextField();

    private final JProgressBar jdt = new JProgressBar();

    public Main() {
        this.initComponents();
    }

    private void initComponents() {
        GroupLayout dialog_layout = new GroupLayout(this.dialog.getContentPane());
        this.dialog.getContentPane().setLayout(dialog_layout);
        dialog_layout.setHorizontalGroup(dialog_layout.createParallelGroup(Alignment.LEADING).addGap(0, 400, 32767));
        dialog_layout.setVerticalGroup(dialog_layout.createParallelGroup(Alignment.LEADING).addGap(0, 300, 32767));

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("激光雕刻机");
        this.setBackground(new Color(204, 204, 204));
        this.setLocation(new Point(400, 200));
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Main.this.formComponentResized(evt);
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                Main.this.formWindowOpened(evt);
            }
        });
        this.btn_openpic.setIcon(new ImageIcon(this.getClass().getResource("/tu/tupian.png")));
        this.btn_openpic.setToolTipText("打开图片");
        this.btn_openpic.addActionListener(Main.this::evt_openpic);
        this.btn_text.setIcon(new ImageIcon(this.getClass().getResource("/tu/wenzi.png")));
        this.btn_text.setToolTipText("输入文字");
        this.btn_text.addActionListener(Main.this::evt_text);
        this.btn_circle.setIcon(new ImageIcon(this.getClass().getResource("/tu/yuan.png")));
        this.btn_circle.setToolTipText("圆形");
        this.btn_circle.addActionListener(Main.this::evt_circle);
        this.btn_square.setIcon(new ImageIcon(this.getClass().getResource("/tu/fang.png")));
        this.btn_square.setToolTipText("正方形");
        this.btn_square.addActionListener(Main.this::evt_square);
        this.btn_heart.setIcon(new ImageIcon(this.getClass().getResource("/tu/xin.png")));
        this.btn_heart.setToolTipText("心形");
        this.btn_heart.addActionListener(Main.this::evt_heart);
        this.btn_star.setIcon(new ImageIcon(this.getClass().getResource("/tu/5xing.png")));
        this.btn_star.setToolTipText("五角星");
        this.btn_star.addActionListener(Main.this::evt_star);

        this.btn_preview.setIcon(new ImageIcon(this.getClass().getResource("/tu/ding_wei.png")));
        this.btn_preview.setToolTipText("预览位置");
        this.btn_preview.addActionListener(Main.this::evt_preview_location);
        this.btn_engrave.setIcon(new ImageIcon(this.getClass().getResource("/tu/diaoke.png")));
        this.btn_engrave.setToolTipText("开始/暂停");
        this.btn_engrave.addActionListener(Main.this::evt_engrave);
        this.btn_stop.setIcon(new ImageIcon(this.getClass().getResource("/tu/tingzhi.png")));
        this.btn_stop.setToolTipText("停止");
        this.btn_stop.addActionListener((e) -> {
            kai_shi = false;
            new Thread(() -> {
                if (this.comOpened) {
                    handler.send(new byte[]{22, 0, 4, 0}, 2);
                } else if (Main.this.wifi.connected) {
                    this.wifi.xie2(new byte[]{22, 0, 4, 0}, 200);
                }
            }).start();
        });

        this.board.setBackground(new Color(255, 255, 255));
        this.board.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 255)));
        this.board.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                Main.this.hua_ban1MouseDragged(evt);
            }
        });
        this.board.addMouseWheelListener(Main.this::hua_ban1MouseWheelMoved);
        this.board.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                Main.this.hua_ban1MousePressed(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                Main.this.hua_ban1MouseReleased(evt);
            }
        });
        this.board.setLayout(new AbsoluteLayout());

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
        this.board.add(this.pn_inlay_hint, new AbsoluteConstraints(30, 20, 20, 10));

        // binding connection
        this.btn_usbconnect.setIcon(new ImageIcon(this.getClass().getResource("/tu/usb2.png")));
        this.btn_usbconnect.setToolTipText("连接设备");
        this.btn_usbconnect.addActionListener(e -> new Thread(Main.this::connect_viaUSB).start());
        this.btn_wificonnect.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
        this.btn_wificonnect.addActionListener(evt -> {
        });

        // binding settings right-panel
        this.btn_aux_positioning.addActionListener(Main.this::jButton16ActionPerformed);
        this.btn_aux_positioning.setIcon(new ImageIcon(this.getClass().getResource("/tu/shi_zi.png")));
        this.btn_aux_positioning.setToolTipText("十字定位");
        this.btn_convertbmp.addActionListener(Main.this::jButton13ActionPerformed);
        this.btn_convertbmp.setIcon(new ImageIcon(this.getClass().getResource("/tu/bmp.png")));
        this.btn_convertbmp.setToolTipText("to bmp");
        this.btn_save.addActionListener(Main.this::jButton12ActionPerformed);
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
        this.opt_accuracy.addActionListener(Main.this::changeAccuracy);
        this.opt_accuracy.setModel(new DefaultComboBoxModel(new String[]{"高", "中", "低"}));
        this.opt_accuracy.setSelectedIndex(2);
        this.opt_num_times.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
        this.sd_carve_depth.addChangeListener(e -> this.lb_carve_depth.setText(bundle.getString("str_shen_du") + this.sd_carve_depth.getValue() + "%"));
        this.sd_carve_depth.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (kai_shi) Main.this.apply_settings_carving();
            }
        });
        this.sd_carve_depth.setValue(10);
        this.sd_carve_power.addChangeListener(e -> this.lb_carve_power.setText(bundle.getString("str_gong_lv") + this.sd_carve_power.getValue() + "%"));
        this.sd_carve_power.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (kai_shi) Main.this.apply_settings_carving();
            }
        });
        this.sd_carve_power.setValue(100);
        this.sd_contrast.addChangeListener(Main.this::jSlider6StateChanged);
        this.sd_cut_depth.addChangeListener(e -> this.lb_cut_depth.setText(bundle.getString("str_shen_du_sl") + this.sd_cut_depth.getValue() + "%"));
        this.sd_cut_depth.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (kai_shi) Main.this.apply_settings_cutting();
            }
        });
        this.sd_cut_depth.setValue(10);
        this.sd_cut_power.addChangeListener(e -> this.lb_cut_power.setText(bundle.getString("str_gong_lv_sl") + this.sd_cut_power.getValue() + "%"));
        this.sd_cut_power.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (kai_shi) Main.this.apply_settings_cutting();
            }
        });
        this.sd_cut_power.setValue(100);
        this.sd_fill.addChangeListener(Main.this::jSlider7StateChanged);
        this.sd_fill.setMaximum(10);
        this.sd_fill.setToolTipText("");
        this.sd_fill.setValue(5);
        this.sd_weak_light.addChangeListener(e -> this.lb_weak_light.setText(bundle.getString("str_ruo_guang") + this.sd_weak_light.getValue() + "%"));
        this.sd_weak_light.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                Main.this.jSlider9MouseReleased(evt);
            }
        });
        this.sd_weak_light.setValue(10);

        // format setting panel layout
        this.pn_main_right.setBackground(new Color(204, 204, 204));
        this.pn_main_right.setLayout(new AbsoluteLayout());
        this.pn_main_right.add(this.btn_unknown, new AbsoluteConstraints(70, 500, -1, 30));
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
                                                        .addComponent(this.board, -1, -1, 32767))
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
                                                .addComponent(this.board, -1, 580, 32767)
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
        this.board.repaint();
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

    /**
     * void cai_dan() {
     * this.jmb = new JMenuBar();
     * this.menu1 = new JMenu(str_set + "(S)");
     * this.menu1.setMnemonic('s');
     * this.item1 = new JMenuItem(str_firmware);
     * this.item1.addActionListener(new ActionListener() {
     * public void actionPerformed(ActionEvent e) {
     * Firmware gj = new Firmware();
     * gj.setVisible(true);
     * gj.setDefaultCloseOperation(2);
     * }
     * });
     * this.menu1.add(this.item1);
     * this.jmb.add(this.menu1);
     * this.setJMenuBar(this.jmb);
     * }
     */

    private void formWindowOpened(WindowEvent evt) {
        try {
            this.btn_wificonnect.setVisible(false);
            Update.update();
            FileTransferHandler ft = new FileTransferHandler();
            FileTransferHandler.hb = this.board;
            this.board.setTransferHandler(ft);
            this.setLocale();
            BElement ty = new BElement();
            ty.type = 0;
            int i = 0;

            while (true) {
                if (i >= Board.bHeight / 10 + 1) {
                    i = 0;

                    while (true) {
                        if (i >= Board.bWidth / 10 + 1) {
                            Board.bElements.add(ty);
                            this.up();
                            this.setIconImage((new ImageIcon(this.getClass().getResource("/tu/tu_biao.png"))).getImage());
                            this.lb_wifi.setVisible(false);
                            this.lb_pwd.setVisible(false);
                            this.btn_unknown.setVisible(false);
                            this.board.window = this;
                            this.board.pn_inlay_hint = this.pn_inlay_hint;
                            this.board.pn_settings = this.pn_main_right;
                            this.board.tf_x = this.tf_inlay_x;
                            this.board.tf_y = this.tf_inlay_y;
                            this.board.tf_w = this.tf_inlay_w;
                            this.board.tf_h = this.tf_inlay_h;
                            this.jdt.setVisible(false);
                            this.tf_inlay_x.addKeyListener(this);
                            this.tf_inlay_y.addKeyListener(this);
                            this.tf_inlay_w.addKeyListener(this);
                            this.tf_inlay_h.addKeyListener(this);
                            this.tf_inlay_x.requestFocus();
                            this.getContentPane().setBackground(new Color(240, 240, 240));
                            this.pn_main_right.setBackground(new Color(240, 240, 240));
                            this.window = this;
                            new Thread(() -> {
                                while (true) {
                                    if (Main.this.wifi == null) {
                                        Main.this.wifi = new Wifi();
                                        Main.this.wifi.bt = Main.this.btn_wificonnect;
                                        Main.this.wifi.board = Main.this.board;
                                        Main.this.wifi.fbl = Main.this.opt_accuracy;
                                        Main.this.wifi.rg = Main.this.sd_weak_light;
                                        Main.this.wifi.jdt = Main.this.jdt;
                                        Main.this.wifi.window = Main.this.window;
                                    }

                                    try {
                                        Thread.sleep(1000L);
                                    } catch (InterruptedException var2) {
                                        Logger.getLogger("MAIN").log(Level.SEVERE, null, var2);
                                    }

                                    if (Main.kai_shi2 && !Main.this.paused) {
                                        ++Main.this.sec;
                                        Main.this.lb_execution_time.setText(Main.this.sec / 60 + "." + Main.this.sec % 60);
                                        if (Main.timeout++ > 3 && Main.timeout != 0) {
                                            System.out.println("&&&");
                                            Main.this.jdt.setValue(0);
                                            Main.this.jdt.setVisible(false);
                                            Main.kai_shi = false;
                                            Main.timeout = 0;
                                            Main.kai_shi2 = false;
                                        }
                                    }
                                }
                            }).start();
                            this.qu_yu();
                            return;
                        }
                        ty.path.moveTo(i / Board.resolution * 10.0D, 0.0D);
                        ty.path.lineTo(i / Board.resolution * 10.0D, Board.bHeight / Board.resolution);
                        ++i;
                    }
                }

                ty.path.moveTo(0.0D, i / Board.resolution * 10.0D);
                ty.path.lineTo(Board.bWidth / Board.resolution, i / Board.resolution * 10.0D);
                ++i;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }
    }

    int qu_anniu(int x, int y) {
        Rectangle rect = BElement.getBounds(Board.bElements);
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
        this.clicked = true;
        this.clickX = evt.getX();
        this.clickY = evt.getY();
        this.clickX1 = this.clickX;
        this.clickY1 = this.clickY;
        this.btnActive = evt.getButton();
        if (this.btnActive == 1) {
            this.an = this.qu_anniu(this.clickX, this.clickY);
            switch (this.an) {
                case 1:
                    Board.bElements = Board.bElements.stream().filter(e -> !e.selected).toList();
                    this.up();
                    Undo.add();
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
                    Undo.add();
                    return;
                case 6:
                case 7:
                case 8:
                case 9:
                    for (BElement e : Board.bElements) {
                        if (e.selected && e.type == 1) {
                            // 1 for case 6; 2 for 7; 3 for 8
                            e.process_code = this.an - 5;
                            e.process();
                            this.up();
                            Undo.add();
                            return;
                        }
                    }
                    return;
                case 10:
                case 11:
                case 12:
                    for (BElement e : Board.bElements) {
                        if (e.selected && e.type == 1) {
                            switch (this.an) {
                                case 10 -> e.process_doMirrorY = !e.process_doMirrorY;
                                case 11 -> e.process_doMirrorX = !e.process_doMirrorX;
                                case 12 -> e.process_doInvert = !e.process_doInvert;
                            }
                            e.process();
                            this.up();
                            Undo.add();
                            return;
                        }
                    }
                    return;
                case 13:
                    Rectangle rectAll = BElement.getBounds(Board.bElements);
                    BElement element = BElement.create(0, null);
                    element.path = new GeneralPath();
                    BElement last = null;
                    for (var e: Board.bElements) {
                        if (e.selected && e.type == 0) {
                            GeneralPath path = new GeneralPath(e.path);
                            path.transform(e.Tx);
                            element.path.append(path, false);
                            last = e;
                        }
                    }

                    element.filled = Board.bElements.size() != 1 || !Objects.requireNonNull(last).filled;
                    element.selected = true;
                    Board.bElements = Board.bElements.stream().filter(e -> !e.selected || e.type == 1).toList();

                    Rectangle rect = BElement.getBounds(element);
                    AffineTransform tx1 = new AffineTransform(
                            AffineTransform.getTranslateInstance(-rect.x, -rect.y));
                    tx1.concatenate(element.Tx);
                    element.Tx = tx1;
                    AffineTransform tx2 = new AffineTransform(
                            AffineTransform.getScaleInstance((double) rectAll.width / rect.width, (double) rectAll.height / rect.height));
                    tx2.concatenate(element.Tx);
                    element.Tx = tx2;
                    AffineTransform tx3 = new AffineTransform(
                            AffineTransform.getTranslateInstance(rectAll.x, rectAll.y));
                    tx3.concatenate(element.Tx);
                    element.Tx = tx3;
                    Board.bElements.add(element);
                    Undo.add();
                    return;
                default:
                    Rectangle rect_q;

                    rect_q = BElement.getBounds(Board.bElements);
                    if (this.clickX > rect_q.x && this.clickX < rect_q.x + rect_q.width && this.clickY > rect_q.y && this.clickY < rect_q.y + rect_q.height) {
                        this.kuang = false;
                        this.up();
                    } else {
                        for (var e: Board.bElements) {
                            if (!e.selected) {
                                GeneralPath path = new GeneralPath(e.path);
                                path.transform(e.Tx);
                                Rectangle r = path.getBounds();
                                if (this.clickX > r.x && this.clickX < r.x + r.width && this.clickY > r.y && this.clickY < r.y + r.height) {
                                    Board.unselectAll();
                                    e.selected = true;
                                    Board.bElements.remove(e);
                                    Board.bElements.add(1, e);
                                    this.sd_contrast.setValue(e.threshold);
                                    this.up();
                                    this.kuang = false;
                                    return;
                                }
                            }

                            Board.unselectAll();
                            this.kuang = true;
                            this.up();
                        }
                    }
            }

        } else if (this.btnActive == 3) {
        }
    }

    private void hua_ban1MousePressed(MouseEvent evt) {
        this.shu_an_xia2(evt);
        this.tf_inlay_x.requestFocus(true);
    }

    private void hua_ban1MouseReleased(MouseEvent evt) {
        this.clicked = false;
        BElement.dragged = false;
        if (this.dragging) {
            this.dragging = false;
            Undo.add();
        }

        this.up();
    }

    void shu_yi_dong2(MouseEvent evt) {
        if (this.clicked) {
            int dx = evt.getX();
            int dy = evt.getY();
            int x;
            if (this.btnActive == 1) {
                Rectangle rect = BElement.getBounds(Board.bElements);
                int i;
                if (this.an == 0) {
                    if (this.kuang) {
                        if (dx < this.clickX1) {
                            x = dx;
                            i = this.clickX1 - dx;
                        } else {
                            x = this.clickX1;
                            i = dx - this.clickX1;
                        }

                        int y;
                        int g;
                        if (dy < this.clickY1) {
                            y = dy;
                            g = this.clickY1 - dy;
                        } else {
                            y = this.clickY1;
                            g = dy - this.clickY1;
                        }

                        BElement.dragged = true;
                        BElement.mouse = new Rectangle(x, y, i, g);
                        BElement.selectByBoundingBox(Board.bElements, BElement.mouse);
                    } else {
                        for (x = 0; x < Board.bElements.size(); ++x) {
                            if ((Board.bElements.get(x)).selected) {
                                (Board.bElements.get(x)).translate(dx - this.clickX, dy - this.clickY);
                            }
                        }
                    }

                    this.clickX = dx;
                    this.clickY = dy;
                    this.up();
                } else if (this.an == 2) {
                    float center_x, center_y;
                    float deg1 = 0.0F;
                    float deg2;
                    center_x = (float) (rect.x + rect.width / 2);
                    center_y = (float) (rect.y + rect.height / 2);
                    if ((float) this.clickX > center_x && (float) this.clickY < center_y) {
                        deg1 = 360.0F - (float) Math.toDegrees(Math.atan((center_y - (float) this.clickY) / ((float) this.clickX - center_x)));
                    } else if ((float) this.clickX < center_x && (float) this.clickY < center_y) {
                        deg1 = 270.0F - (float) Math.toDegrees(Math.atan((center_x - (float) this.clickX) / (center_y - (float) this.clickY)));
                    } else if ((float) this.clickX < center_x && (float) this.clickY > center_y) {
                        deg1 = 90.0F + (float) Math.toDegrees(Math.atan((center_x - (float) this.clickX) / ((float) this.clickY - center_y)));
                    } else if ((float) this.clickX > center_x && (float) this.clickY > center_y) {
                        deg1 = (float) Math.toDegrees(Math.atan(((float) this.clickY - center_y) / ((float) this.clickX - center_x)));
                    }

                    if ((float) dx > center_x && (float) dy < center_y) {
                        deg2 = 360.0F - (float) Math.toDegrees(Math.atan((center_y - (float) dy) / ((float) dx - center_x)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.clickX = dx;
                        this.clickY = dy;
                    } else if ((float) dx < center_x && (float) dy < center_y) {
                        deg2 = 270.0F - (float) Math.toDegrees(Math.atan((center_x - (float) dx) / (center_y - (float) dy)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.clickX = dx;
                        this.clickY = dy;
                    } else if ((float) dx < center_x && (float) dy > center_y) {
                        deg2 = 90.0F + (float) Math.toDegrees(Math.atan((center_x - (float) dx) / ((float) dy - center_y)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.clickX = dx;
                        this.clickY = dy;
                    } else if ((float) dx > center_x && (float) dy > center_y) {
                        deg2 = (float) Math.toDegrees(Math.atan(((float) dy - center_y) / ((float) dx - center_x)));

                        for (i = 0; i < Board.bElements.size(); ++i) {
                            if ((Board.bElements.get(i)).selected) {
                                (Board.bElements.get(i)).rotate(deg2 - deg1, center_x, center_y);
                            }
                        }

                        this.clickX = dx;
                        this.clickY = dy;
                    }

                    this.up();
                }

                if (this.an == 3) {
                    double sf;
                    if (Board.lock) {
                        sf = (double) (this.clickX - rect.x) / (double) rect.width;
                        if (sf > 0.0D) {
                            for (i = 0; i < Board.bElements.size(); ++i) {
                                if ((Board.bElements.get(i)).selected) {
                                    (Board.bElements.get(i)).translate(-rect.x, -rect.y);
                                    (Board.bElements.get(i)).scale(sf, sf);
                                    (Board.bElements.get(i)).translate(rect.x, rect.y);
                                }
                            }
                        }

                        this.clickX = dx;
                    } else {
                        sf = (double) (this.clickX - rect.x) / (double) rect.width;
                        double sf_y = (double) (this.clickY - rect.y) / (double) rect.height;
                        if (sf > 0.0D && sf_y > 0.0D) {
                            for (i = 0; i < Board.bElements.size(); ++i) {
                                if ((Board.bElements.get(i)).selected) {
                                    (Board.bElements.get(i)).translate(-rect.x, -rect.y);
                                    (Board.bElements.get(i)).scale(sf, sf_y);
                                    (Board.bElements.get(i)).translate(rect.x, rect.y);
                                }
                            }
                        }

                        this.clickX = dx;
                        this.clickY = dy;
                    }

                    this.up();
                }

                this.dragging = true;
            } else if (this.btnActive == 3) {
                AffineTransform py = AffineTransform.getTranslateInstance(dx - this.clickX, dy - this.clickY);
                Board.quan_x = (int) ((double) (Board.quan_x + (dx - this.clickX)) / Board.quan_scale);
                Board.quan_y = (int) ((double) (Board.quan_y + (dy - this.clickY)) / Board.quan_scale);

                for (x = 0; x < Board.bElements.size(); ++x) {
                    AffineTransform fb = new AffineTransform(py);
                    fb.concatenate((Board.bElements.get(x)).Tx);
                    (Board.bElements.get(x)).Tx = fb;
                }

                this.clickX = dx;
                this.clickY = dy;
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
            File file = fc.getSelectedFile();
            String filePath = fc.getSelectedFile().getPath();
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
            BufferedImage img;
            if (!suffix.equals("BMP") && !suffix.equals("JPG") && !suffix.equals("PNG") && !suffix.equals("JPEG") && !suffix.equals("GIF")) {
                if (suffix.equals("XJ")) {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
                        Board.bElements = (List) ois.readObject();
                        ois.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Board.bElements.stream().filter(e -> e.type == 1).forEach(e -> {
                        e.bitMapImg = new BufferedImage(e.bitMapW, e.bitMapH, 2);
                        e.bitMapImg2 = new BufferedImage(e.bitMap2W, e.bitMap2H, 2);
                        e.bitMapImg.setRGB(0, 0, e.bitMapW, e.bitMapH, e.bitMap, 0, e.bitMapW);
                        e.bitMapImg2.setRGB(0, 0, e.bitMap2W, e.bitMap2H, e.bitMap2, 0, e.bitMap2W);
                    });

                    Undo.add();
                    this.up();
                } else if (suffix.equals("PLT")) {
                    PLT plt = new PLT();
                    plt.analyze(file);
                    Undo.add();
                    this.up();
                }
            } else {
                try {
                    img = ImageIO.read(file);
                    Board.bElements.add(BElement.create(1, img));
                    Board.selectLast();
                    BElement.center(Board.bElements);
                    Undo.add();
                    this.up();
                } catch (IOException e) {
                }
            }
        }

    }

    private void hua_ban1MouseWheelMoved(MouseWheelEvent evt) {
        int dx = evt.getX();
        int dy = evt.getY();
        AffineTransform tx1;
        AffineTransform tx2;
        if (evt.getPreciseWheelRotation() < 0.0D) {
            for (var e : Board.bElements) {
                tx1 = new AffineTransform(AffineTransform.getTranslateInstance(-dx, -dy));
                tx1.concatenate(e.Tx);
                e.Tx = tx1;
            }

            Board.quan_scale *= 1.1D;
            for (var e: Board.bElements) {
                tx2 = new AffineTransform(AffineTransform.getScaleInstance(1.1D, 1.1D));
                tx2.concatenate(e.Tx);
                e.Tx = tx2;
            }

            for (var e: Board.bElements) {
                tx2 = new AffineTransform(AffineTransform.getTranslateInstance(dx, dy));
                tx2.concatenate(e.Tx);
                e.Tx = tx2;
            }
        } else {
            GeneralPath path = new GeneralPath((Board.bElements.get(0)).path);
            path.transform((Board.bElements.get(0)).Tx);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                tx2 = new AffineTransform(AffineTransform.getTranslateInstance(-dx, -dy));
                tx2.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = tx2;
            }

            Board.quan_scale *= 0.9D;
            tx2 = AffineTransform.getScaleInstance(0.9D, 0.9D);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                // AffineTransform tx2 = new AffineTransform(tx2);
                tx2.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = tx2;
            }

            tx2 = AffineTransform.getTranslateInstance(dx, dy);

            for (int i = 0; i < Board.bElements.size(); ++i) {
                // AffineTransform tx2 = new AffineTransform(tx2);
                tx2.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = tx2;
            }
        }

        this.up();
    }

    private void evt_engrave(ActionEvent evt) {
        if (this.comOpened || this.wifi.connected) {
            if (!kai_shi) {
                if (Board.boundingBox) {
                    handler.send(new byte[]{33, 0, 4, 0}, 3);
                    Board.boundingBox = false;
                }

                this.write(new byte[]{22, 0, 4, 0}, 2);
                new Thread(() -> {
                    try {
                        Main.this.btn_engrave.setEnabled(false);
                        Main.this.goOffline();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                    }
                }).start();
                this.sec = 0;
                timeout = 0;
            }

        }
    }

    private void evt_preview_location(ActionEvent evt) {
        if (kai_shi) return;

        if (this.comOpened) {
            if (Board.boundingBox) {
                new Thread(() -> {
                    handler.send(new byte[]{33, 0, 4, 0}, 3);
                    Board.boundingBox = false;
                    this.up();
                }).start();
            } else {
                new Thread(() -> {
                    BElement.getRectangle(Board.bElements);
                    GeneralPath path = new GeneralPath((Board.bElements.get(0)).path);
                    path.transform((Board.bElements.get(0)).Tx);
                    Rectangle r = path.getBounds();
                    Rectangle bound = new Rectangle(BElement.bounds);
                    bound = AffineTransform.getTranslateInstance(-r.x, -r.y)
                            .createTransformedShape(bound).getBounds();
                    bound = AffineTransform.getScaleInstance(1.0D / Board.quan_scale, 1.0D / Board.quan_scale)
                            .createTransformedShape(bound).getBounds();
                    if (bound.width >= 2 || bound.height >= 2) {
                        byte kg = (byte) (bound.width >> 8);
                        byte kd = (byte) bound.width;
                        byte gg = (byte) (bound.height >> 8);
                        byte gd = (byte) bound.height;
                        byte xg = (byte) (bound.x + 67 + bound.width / 2 >> 8);
                        byte xd = (byte) (bound.x + 67 + bound.width / 2);
                        byte yg = (byte) (bound.y + bound.height / 2 >> 8);
                        byte yd = (byte) (bound.y + bound.height / 2);

                        Main.handler.send(new byte[]{32, 0, 11, kg, kd, gg, gd, xg, xd, yg, yd}, 1);

                        Board.boundingBox = true;
                        Main.this.up();
                    }
                }).start();
            }
        } else if (this.wifi.connected) {
            if (Board.boundingBox) {
                new Thread(() -> {
                    this.wifi.xie2(new byte[]{33, 0, 4, 0}, 300);
                    Board.boundingBox = false;
                    this.up();
                }).start();
            } else {
                new Thread(() -> {
                    BElement.getRectangle(Board.bElements);
                    GeneralPath path = new GeneralPath((Board.bElements.get(0)).path);
                    path.transform((Board.bElements.get(0)).Tx);
                    Rectangle r = path.getBounds();
                    Rectangle bound = new Rectangle(BElement.bounds);
                    bound = AffineTransform.getTranslateInstance(-r.x, -r.y)
                            .createTransformedShape(bound).getBounds();
                    bound = AffineTransform.getScaleInstance(1.0D / Board.quan_scale, 1.0D / Board.quan_scale)
                            .createTransformedShape(bound).getBounds();
                    byte kg = (byte) (bound.width >> 8);
                    byte kd = (byte) bound.width;
                    byte gg = (byte) (bound.height >> 8);
                    byte gd = (byte) bound.height;
                    byte xg = (byte) (bound.x + 67 + bound.width / 2 >> 8);
                    byte xd = (byte) (bound.x + 67 + bound.width / 2);
                    byte yg = (byte) (bound.y + bound.height / 2 >> 8);
                    byte yd = (byte) (bound.y + bound.height / 2);
                    Main.this.wifi.xie2(new byte[]{32, 0, 11, kg, kd, gg, gd, xg, xd, yg, yd}, 100);
                    Board.boundingBox = true;
                    Main.this.up();
                }).start();
            }
        }


    }

    private void evt_circle(ActionEvent evt) {
        Board.bElements.add(BElement.create(2, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.add();
        this.up();
    }

    private void evt_heart(ActionEvent evt) {
        Board.bElements.add(BElement.create(3, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.add();
        this.up();
    }

    private void evt_star(ActionEvent evt) {
        Board.bElements.add(BElement.create(4, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.add();
        this.up();
    }

    private void evt_text(ActionEvent evt) {
        FontDialog dialog = new FontDialog(this.board, true);
        dialog.setDefaultCloseOperation(2);
        dialog.setVisible(true);
    }

    private void evt_square(ActionEvent evt) {
        Board.bElements.add(BElement.create(0, null));
        Board.selectLast();
        BElement.center(Board.bElements);
        Undo.add();
        this.up();
    }

    private void formComponentResized(ComponentEvent evt) {
    }

    void apply_settings_carving() {
        new Thread(() -> {
            int sd = this.sd_carve_depth.getValue();
            int gl = this.sd_carve_power.getValue() * 10;
            if (this.comOpened) {
                handler.send_settings(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 2);
            } else if (this.wifi.connected) {
                this.wifi.xie2(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 200);
            }

        }).start();
    }

    void apply_settings_cutting() {
        new Thread(() -> {
            int sd = this.sd_cut_depth.getValue();
            int gl = this.sd_cut_power.getValue() * 10;
            if (this.comOpened) {
                handler.send_settings(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 2);
            } else if (this.wifi.connected) {
                this.wifi.xie2(new byte[]{37, 0, 11, (byte) (sd >> 8), (byte) sd, (byte) (gl >> 8), (byte) gl, 0, 0, 0, 0}, 200);
            }

        }).start();
    }

    void she_zhi_can_shu() {
        new Thread(() -> {
            int rg = this.sd_weak_light.getValue() * 2;
            int jd = this.opt_accuracy.getSelectedIndex();
            if (this.comOpened) {
                handler.send(new byte[]{40, 0, 11, (byte) rg, (byte) jd, 0, 0, 0, 0, 0, 0}, 2);
            } else if (this.wifi.connected) {
                this.wifi.xie2(new byte[]{40, 0, 11, (byte) rg, (byte) jd, 0, 0, 0, 0, 0, 0}, 200);
            }
        }).start();
    }

    private void jSlider9MouseReleased(MouseEvent evt) {
        if (this.comOpened) {
            this.she_zhi_can_shu();
        } else if (this.wifi.connected) {
            new Thread(() -> {
                int rg = Main.this.sd_weak_light.getValue() * 2;
                int jd = Main.this.opt_accuracy.getSelectedIndex();
                Main.this.wifi.xie2(new byte[]{40, 0, 11, (byte) rg, (byte) jd, 0, 0, 0, 0, 0, 0}, 200);
            }).start();
        }

    }

    private void changeAccuracy(ActionEvent evt) {
        if (this.wifi != null) {
            if (this.comOpened || this.wifi.connected) {
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

                this.board.di_tu();
                this.she_zhi_can_shu();
            }

        }
    }

    private void jButton16ActionPerformed(ActionEvent evt) {
        new Thread(() -> {
            if (Main.this.comOpened) {
                if (Main.this.auxPositioning) {
                    Main.handler.send(new byte[]{7, 0, 4, 0}, 2);
                } else {
                    Main.handler.send(new byte[]{6, 0, 4, 0}, 2);
                }
                Main.this.auxPositioning = !Main.this.auxPositioning;
            } else if (Main.this.wifi.connected) {
                if (Main.this.auxPositioning) {
                    Main.this.wifi.xie2(new byte[]{7, 0, 4, 0}, 200);
                } else {
                    Main.this.wifi.xie2(new byte[]{6, 0, 4, 0}, 200);
                }

                Main.this.auxPositioning = !Main.this.auxPositioning;
            }
        }).start();
    }

    private void jButton12ActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files (.xj)", "xj");
        chooser.setFileFilter(filter);
        int option = chooser.showSaveDialog(this);
        if (option == 0) {
            for (var e : Board.bElements) {
                if (e.type == 1) {
                    e.bitMapW = e.bitMapImg.getWidth();
                    e.bitMapH = e.bitMapImg.getHeight();
                    e.bitMap2W = e.bitMapImg2.getWidth();
                    e.bitMap2H = e.bitMapImg2.getHeight();

                    e.bitMapImg.getRGB(
                            0, 0, e.bitMapW, e.bitMapH,
                            new int[e.bitMapW * e.bitMapH], 0, e.bitMapW
                    );
                    e.bitMapImg2.getRGB(
                            0, 0, e.bitMap2W, e.bitMap2H,
                            new int[e.bitMap2W * e.bitMap2H], 0, e.bitMap2W
                    );
                }
            }

            BElement.backup();
            BufferedImage image = BElement.getImage(Board.bElements);
            BElement.restore();
            String filePath = chooser.getSelectedFile().getPath().toLowerCase();
            if (!filePath.endsWith(".xj")) filePath = filePath + ".xj";

            File file;
            if (image != null) {
                file = new File(filePath + ".bmp");

                try {
                    BMPEncoder.write(image, file);
                } catch (Exception e) {
                    Logger.getLogger(BElement.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            file = new File(filePath);

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(Board.bElements);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
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
            Board.bElements.stream().filter(e -> e.selected && e.type == 1)
                    .forEach(e -> {
                        e.threshold = this.sd_contrast.getValue();
                        e.process();
                    });
            this.up();
        }
    }

    private void jSlider7StateChanged(ChangeEvent evt) {
        this.lb_fill.setText(bundle.getString("str_tian_chong") + this.sd_fill.getValue());
        BElement.fillGap = this.sd_fill.getValue();
        this.up();
    }

    void qu_yu() {
        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing2.transform((Board.bElements.get(0)).Tx);
        Rectangle rect = lu_jing2.getBounds();
        System.out.print(rect);
        AffineTransform fb;
        if (rect.width > this.board.getWidth() || rect.height > this.board.getHeight()) {
            double b;
            if (rect.width - this.board.getWidth() > rect.height - this.board.getHeight()) {
                b = (double) this.board.getWidth() / (double) rect.width;
            } else {
                b = (double) this.board.getHeight() / (double) rect.height;
            }

            Board.quan_scale *= b;
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
        int x2 = this.board.getWidth() / 2;
        int y2 = this.board.getHeight() / 2;
        fb = AffineTransform.getTranslateInstance(x2 - x1, y2 - y1);

        for (int i = 0; i < Board.bElements.size(); ++i) {
            // AffineTransform fb = new AffineTransform(fb);
            fb.concatenate((Board.bElements.get(i)).Tx);
            (Board.bElements.get(i)).Tx = fb;
        }

        this.up();
    }

    /**
     * tells the machine to go offline
     *
     * @param shan_qu
     * @param version
     * @param w_wt       width
     * @param h_wt       height
     * @param pos_wt     position
     * @param power_wt   power
     * @param depth_wt   depth
     * @param w_sl       width
     * @param h_sl       height
     * @param pos_sl     position
     * @param power_sl   power
     * @param depth_sl   depth
     * @param dianshu_sl
     * @param z
     * @param s
     * @param nTimes     number of times
     * @param z_sl
     * @param s_sl
     * @return true if successful
     */
    boolean goOffline(
            int shan_qu, int version,
            int w_wt, int h_wt, int pos_wt, int power_wt, int depth_wt,
            int w_sl, int h_sl, int pos_sl, int power_sl, int depth_sl,
            int dianshu_sl, int z, int s, int nTimes, int z_sl, int s_sl
    ) {
        byte[] data = new byte[]{35, 0, 38, (byte) (shan_qu >> 8), (byte) shan_qu, (byte) version, (byte) (w_wt >> 8), (byte) w_wt, (byte) (h_wt >> 8), (byte) h_wt, (byte) (pos_wt >> 8), (byte) pos_wt, (byte) (power_wt >> 8), (byte) power_wt, (byte) (depth_wt >> 8), (byte) depth_wt, (byte) (w_sl >> 8), (byte) w_sl, (byte) (h_sl >> 8), (byte) h_sl, (byte) (pos_sl >> 24), (byte) (pos_sl >> 16), (byte) (pos_sl >> 8), (byte) pos_sl, (byte) (power_sl >> 8), (byte) power_sl, (byte) (depth_sl >> 8), (byte) depth_sl, (byte) (dianshu_sl >> 24), (byte) (dianshu_sl >> 16), (byte) (dianshu_sl >> 8), (byte) dianshu_sl, (byte) (z >> 8), (byte) z, (byte) (s >> 8), (byte) s, (byte) nTimes, 0};
        if (this.comOpened) {
            return handler.send_offline(data, 2);
        } else {
            return this.wifi.connected && this.wifi.kaishi(data, 22);
        }
    }

    boolean write(byte[] data, int timeout) {
        return !this.comOpened ? this.wifi.write(data, timeout * 100) : handler.send(data, timeout);
    }

    byte checksum(byte[] bytes) {
        int sum = 0;
        for (byte b : bytes)
            sum += (255 & b);

        if (sum > 255) {
            sum = ~sum;
            ++sum;
        }
        return (byte) (sum & 255);
    }

    void goOffline() {
        int count = 0;
        int k = 0;
        int g = 0;
        int k_sl = 0;
        int g_sl = 0;
        int wz_sl = 1;
        int k_len = 0;
        boolean wt_ = false;
        boolean sl_ = false;
        byte bl = 0;
        byte[] data;
        int len = 0;
        BElement.backup();
        BufferedImage image = BElement.getImage(Board.bElements);
        List<BPoint> bPoints = BElement.getPoints(Board.bElements);
        BElement.restore();
        if (image == null && bPoints == null) {
            this.btn_engrave.setEnabled(true);
        } else {
            if (image != null) {
                g = image.getHeight();
                k = image.getWidth();
                if (image.getWidth() % 8 > 0) {
                    data = new byte[image.getWidth() / 8 + 1];
                    k_len = image.getWidth() / 8 + 1;
                } else {
                    data = new byte[image.getWidth() / 8];
                    k_len = image.getWidth() / 8;
                }

                wz_sl = 33 + g * data.length;
                wt_ = true;
                len = data.length;
            } else {
                wz_sl = 33;
            }

            if (bPoints != null) {
                k_sl = BElement.bounds.width;
                g_sl = BElement.bounds.height;
                sl_ = true;
            } else {
                bPoints = new ArrayList<>();
            }

            int z = BElement.bounds.x + BElement.bounds.width / 2 + 67;
            int s = BElement.bounds.y + BElement.bounds.height / 2;
            int z_sl = BElement.vector.x + BElement.vector.width / 2 + 67;
            int s_sl = BElement.vector.y + BElement.vector.height / 2;
            data = new byte[wz_sl - 33 + bPoints.size() * 4];
            kai_shi = true;
            this.goOffline((33 + len * g + bPoints.size() * 4) / 4094 + 1, 1, k, g, 33, this.sd_carve_power.getValue() * 10, this.sd_carve_depth.getValue(), k_sl, g_sl, wz_sl, this.sd_cut_power.getValue() * 10, this.sd_cut_depth.getValue(), bPoints.size(), z, s, this.opt_num_times.getSelectedIndex() + 1, z_sl, s_sl);

            try {
                Thread.sleep(40 * ((33 + len * g + bPoints.size() * 4) / 4094 + 1));
            } catch (InterruptedException var31) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, var31);
            }

            for (int iter = 0; iter < 2; iter++) {
                this.write(new byte[]{10, 0, 4, 0}, 1);
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                }
            }

            byte[] yi;
            boolean doResend;
            if (wt_) {
                int[] pixels = new int[k * g];
                image.getRGB(0, 0, k, g, pixels, 0, k);
                yi = new byte[]{-128, 64, 32, 16, 8, 4, 2, 1};

                for (int i = 0; i < g; ++i) {
                    for (int j = 0; j < k_len; ++j) {
                        // byte bl = 0;
                        for (int ba = 0; ba < 8; ++ba) {
                            int p = i * k + j * 8 + ba;
                            if (p < pixels.length) {
                                int clr = pixels[p];
                                clr = (clr & 16711680) >> 16;
                                if (clr < 10) {
                                    bl |= yi[ba];
                                }
                            }
                        }

                        data[count] = bl;
                        ++count;
                    }
                }
            }

            if (sl_) {
                for (int j = 0; j < bPoints.size(); ++j) {
                    data[count++] = (byte) bPoints.get(j).x;
                    data[count++] = (byte) (bPoints.get(j).x >> 8);
                    data[count++] = (byte) bPoints.get(j).y;
                    data[count++] = (byte) (bPoints.get(j).y >> 8);
                }
            }

            int bao_chicuo = 1900;
            yi = new byte[bao_chicuo + 4];

            for (int i = 0; i < data.length / bao_chicuo; ++i) {
                for (int j = 0; j < bao_chicuo; ++j) {
                    yi[j + 3] = data[i * bao_chicuo + j];
                }

                do {
                    yi[0] = 34;
                    yi[1] = (byte) (yi.length >> 8);
                    yi[2] = (byte) yi.length;
                    yi[yi.length - 1] = this.checksum(yi);
                    doResend = !this.write(yi, 2);
                } while (doResend);

                this.jdt.setVisible(true);
                this.jdt.setValue((int) ((float) i / (float) (data.length / bao_chicuo) * 100.0F));
            }

            yi = new byte[data.length % bao_chicuo + 4];
            if (data.length % bao_chicuo > 0) {
                for (int i = 0; i < data.length % bao_chicuo; ++i) {
                    yi[i + 3] = data[data.length / bao_chicuo * bao_chicuo + i];
                }

                do {
                    yi[0] = 34;
                    yi[1] = (byte) (yi.length >> 8);
                    yi[2] = (byte) yi.length;
                    yi[yi.length - 1] = this.checksum(yi);
                    doResend = !this.write(yi, 2);
                } while (doResend);
            }

            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(200L);
                } catch (Exception e) {
                    Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                }

                this.write(new byte[]{36, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 1);
            }

            try {
                Thread.sleep(500L);
            } catch (Exception e) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
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
        if (this.comOpened) {
            handler.close();
            handler = null;
            toggle_connect_status(false);
        }

        // getting all serial ports
        List<String> ports = SerialPortUtil.getSerialPortList();
        ports.forEach(System.out::println);

        for (String port : ports) {
            if (port.contains("Bluetooth")) continue;

            try {
                this.port = SerialPortUtil.openSerialPort(port, 115200);
                handler = new Com(this.port);
                if (handler.send(new byte[]{10, 0, 4, 0}, 2)) {
                    handler.jdt = this.jdt;
                    toggle_connect_status(true);
                    if (handler.read_version()) {
                        this.driver_version = new byte[]{handler.ret_val[0], handler.ret_val[1], handler.ret_val[2]};
                        this.board.version(this.driver_version, 2);
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
    }

    void toggle_connect_status(boolean connected) {
        this.comOpened = connected;
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

    public static void main(String[] args) {
        try {
            for (var preset : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(preset.getName())) {
                    UIManager.setLookAndFeel(preset.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }
        EventQueue.invokeLater(() -> (new Main()).setVisible(true));
    }

    /**
     * set element's by input in inlay textfields
     * changes: x, y, h, w
     */
    void set() {
        if (this.tf_inlay_x.getText().length() != 0) {
            double var_x = (Integer.parseInt(this.tf_inlay_x.getText()) - this.board.val_x)
                    * Board.quan_scale / Board.resolution;
            Board.bElements.stream().filter(e -> e.selected)
                    .forEach(e -> e.translate((int) var_x, e.path.getBounds().y));
            this.up();
        }
        if (this.tf_inlay_y.getText().length() != 0) {
            double var_y = (Integer.parseInt(this.tf_inlay_y.getText()) - this.board.val_y)
                    * Board.quan_scale / Board.resolution;
            Board.bElements.stream().filter(e -> e.selected)
                    .forEach(e -> e.translate(e.path.getBounds().x, (int) var_y));
            this.up();
        }
        if (this.tf_inlay_w.getText().length() != 0) {
            double var_w = (double) Integer.parseInt(this.tf_inlay_w.getText()) / this.board.val_w;
            int x = BElement.getBounds(Board.bElements).x;
            int y = BElement.getBounds(Board.bElements).y;
            Board.bElements.stream().filter(e -> e.selected).forEach(e -> {
                e.translate(-x, -y);
                if (Board.lock) e.scale(var_w, var_w);
                else e.scale(var_w, 1.0D);
                e.translate(x, y);
            });
            this.up();
        }
        if (this.tf_inlay_h.getText().length() != 0) {
            double var_h = (double) Integer.parseInt(this.tf_inlay_h.getText()) / this.board.val_h;
            int x = BElement.getBounds(Board.bElements).x;
            int y = BElement.getBounds(Board.bElements).y;
            Board.bElements.stream().filter(e -> e.selected).forEach(e -> {
                e.translate(-x, -y);
                if (Board.lock) e.scale(var_h, var_h);
                else e.scale(1.0D, var_h);
                e.translate(x, y);
            });
            this.up();
        }
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        if (event.isControlDown()) {
            switch (key) {
                case 67 -> {
                    this.bElementsCopy.clear();
                    Board.bElements.stream().filter(e -> e.selected)
                            .forEach(e -> this.bElementsCopy.add(BElement.copy(e)));
                    this.copied = true;
                }
                case 86 -> {
                    if (this.copied) {
                        this.bElementsCopy.forEach(e -> Board.bElements.add(BElement.copy(e)));
                        this.up();
                        Undo.add();
                    }
                }
                case 65 -> {
                    Board.bElements.forEach(e -> e.selected = true);
                    this.up();
                }
                case 90 -> {
                    Undo.undo();
                    this.up();
                }
                case 88 -> {
                    Board.bElements.forEach(e -> e.selected = true);
                    Undo.redo();
                    this.up();
                }
            }
        }
        if (key == 10) {
            this.set();
        } else if (key == 127) {
            Board.bElements = Board.bElements.stream().filter(e -> !e.selected).toList();
            this.up();
            Undo.add();
        }

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
