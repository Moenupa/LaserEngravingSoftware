package engraver;

import engraver.control.*;
import engraver.model.*;
import engraver.view.FontDialog;
import engraver.view.ImagePreviewPanel;
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
    private boolean selection = false;
    public Wifi wifi = null;
    public byte[] driver_version = new byte[]{0, 0, 0, 0};
    public static Com handler = null;
    public static ResourceBundle bundle = null;
    public static boolean engraveStarted = false;
    public static boolean engraveFinished = false;
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
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                Main.this.formWindowOpened(evt);
            }
        });
        this.btn_openpic.setIcon(new ImageIcon(this.getClass().getResource("/res/tupian.png")));
        this.btn_openpic.setToolTipText("打开图片");
        this.btn_openpic.addActionListener(Main.this::evt_openpic);
        this.btn_text.setIcon(new ImageIcon(this.getClass().getResource("/res/wenzi.png")));
        this.btn_text.setToolTipText("输入文字");
        this.btn_text.addActionListener(Main.this::evt_text);
        this.btn_circle.setIcon(new ImageIcon(this.getClass().getResource("/res/yuan.png")));
        this.btn_circle.setToolTipText("圆形");
        this.btn_circle.addActionListener(Main.this::evt_circle);
        this.btn_square.setIcon(new ImageIcon(this.getClass().getResource("/res/fang.png")));
        this.btn_square.setToolTipText("正方形");
        this.btn_square.addActionListener(Main.this::evt_square);
        this.btn_heart.setIcon(new ImageIcon(this.getClass().getResource("/res/xin.png")));
        this.btn_heart.setToolTipText("心形");
        this.btn_heart.addActionListener(Main.this::evt_heart);
        this.btn_star.setIcon(new ImageIcon(this.getClass().getResource("/res/5xing.png")));
        this.btn_star.setToolTipText("五角星");
        this.btn_star.addActionListener(Main.this::evt_star);

        this.btn_preview.setIcon(new ImageIcon(this.getClass().getResource("/res/ding_wei.png")));
        this.btn_preview.setToolTipText("预览位置");
        this.btn_preview.addActionListener(Main.this::evt_preview_location);
        this.btn_engrave.setIcon(new ImageIcon(this.getClass().getResource("/res/diaoke.png")));
        this.btn_engrave.setToolTipText("开始/暂停");
        this.btn_engrave.addActionListener(Main.this::evt_engrave);
        this.btn_stop.setIcon(new ImageIcon(this.getClass().getResource("/res/tingzhi.png")));
        this.btn_stop.setToolTipText("停止");
        this.btn_stop.addActionListener((e) -> {
            engraveStarted = false;
            new Thread(() -> {
                Main.this.send(new byte[] {22, 0, 4, 0}, 2);
            }).start();
        });

        this.board.setBackground(new Color(255, 255, 255));
        this.board.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 255)));
        this.board.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                Main.this.evt_mouse_dragged(evt);
            }
        });
        this.board.addMouseWheelListener(Main.this::boardZoom);
        this.board.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                Main.this.evt_mouse_pressed(evt);
                Main.this.tf_inlay_x.requestFocus(true);
            }

            public void mouseReleased(MouseEvent evt) {
                Main.this.clicked = false;
                Board.dragged = false;
                if (Main.this.dragging) {
                    Main.this.dragging = false;
                    Undo.add();
                }
                Main.this.updateBoard();
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
        this.btn_usbconnect.setIcon(new ImageIcon(this.getClass().getResource("/res/usb2.png")));
        this.btn_usbconnect.setToolTipText("连接设备");
        this.btn_usbconnect.addActionListener(e -> new Thread(Main.this::connect_viaUSB).start());
        this.btn_wificonnect.setIcon(new ImageIcon(this.getClass().getResource("/res/wifi2.png")));
        this.btn_wificonnect.addActionListener(evt -> {
        });

        // binding settings right-panel
        this.btn_aux_positioning.addActionListener(Main.this::evt_aux_positioning);
        this.btn_aux_positioning.setIcon(new ImageIcon(this.getClass().getResource("/res/shi_zi.png")));
        this.btn_aux_positioning.setToolTipText("十字定位");
        this.btn_convertbmp.addActionListener(Main.this::evt_convertBMP);
        this.btn_convertbmp.setIcon(new ImageIcon(this.getClass().getResource("/res/bmp.png")));
        this.btn_convertbmp.setToolTipText("to bmp");
        this.btn_save.addActionListener(Main.this::evt_save);
        this.btn_save.setIcon(new ImageIcon(this.getClass().getResource("/res/baocun.png")));
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
        this.sd_carve_depth.addChangeListener(e -> this.lb_carve_depth.setText(bundle.getString("carve_depth") + this.sd_carve_depth.getValue() + "%"));
        this.sd_carve_depth.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (engraveStarted) Main.this.apply_settings_carving();
            }
        });
        this.sd_carve_depth.setValue(10);
        this.sd_carve_power.addChangeListener(e -> this.lb_carve_power.setText(bundle.getString("carve_power") + this.sd_carve_power.getValue() + "%"));
        this.sd_carve_power.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (engraveStarted) Main.this.apply_settings_carving();
            }
        });
        this.sd_carve_power.setValue(100);
        this.sd_contrast.addChangeListener(Main.this::jSlider6StateChanged);
        this.sd_cut_depth.addChangeListener(e -> this.lb_cut_depth.setText(bundle.getString("cut_depth") + this.sd_cut_depth.getValue() + "%"));
        this.sd_cut_depth.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (engraveStarted) Main.this.apply_settings_cutting();
            }
        });
        this.sd_cut_depth.setValue(10);
        this.sd_cut_power.addChangeListener(e -> this.lb_cut_power.setText(bundle.getString("cut_power") + this.sd_cut_power.getValue() + "%"));
        this.sd_cut_power.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                if (engraveStarted) Main.this.apply_settings_cutting();
            }
        });
        this.sd_cut_power.setValue(100);
        this.sd_fill.addChangeListener(Main.this::jSlider7StateChanged);
        this.sd_fill.setMaximum(10);
        this.sd_fill.setToolTipText("");
        this.sd_fill.setValue(5);
        this.sd_weak_light.addChangeListener(e -> this.lb_weak_light.setText(bundle.getString("weak_light") + this.sd_weak_light.getValue() + "%"));
        this.sd_weak_light.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                Main.this.apply_settings_general();
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

    public void updateBoard() {
        this.board.repaint();
    }

    void setLocale() {
        Locale locale = Locale.getDefault();
        if (locale == Locale.CHINA) {
            bundle = ResourceBundle.getBundle("examples.ui_zh_CN");
        } else {
            bundle = ResourceBundle.getBundle("examples.ui_en_US");
        }

        this.btn_openpic.setToolTipText(bundle.getString("open_pic"));
        this.btn_text.setToolTipText(bundle.getString("enter_text"));
        this.btn_circle.setToolTipText(bundle.getString("circle"));
        this.btn_square.setToolTipText(bundle.getString("square"));
        this.btn_heart.setToolTipText(bundle.getString("heart"));
        this.btn_star.setToolTipText(bundle.getString("star"));

        this.btn_save.setToolTipText(bundle.getString("save"));
        this.btn_convertbmp.setToolTipText(bundle.getString("convert_bmp"));
        this.btn_aux_positioning.setToolTipText(bundle.getString("aux_position"));
        this.btn_preview.setToolTipText(bundle.getString("preview_location"));
        this.btn_engrave.setToolTipText(bundle.getString("start_pause"));
        this.btn_stop.setToolTipText(bundle.getString("stop"));
        this.btn_usbconnect.setToolTipText(bundle.getString("connecting"));

        this.lb_weak_light.setText(bundle.getString("weak_light") + "10%");
        this.lb_carve_power.setText(bundle.getString("carve_power") + "100%");
        this.lb_carve_depth.setText(bundle.getString("carve_depth") + "10%");
        this.lb_cut_power.setText(bundle.getString("cut_power") + "100%");
        this.lb_cut_depth.setText(bundle.getString("cut_depth") + "10%");
        this.lb_num_times.setText(bundle.getString("num_times"));
        this.lb_accuracy.setText(bundle.getString("accuracy"));
        this.opt_accuracy.removeAllItems();
        this.opt_accuracy.addItem(bundle.getString("lv_hi"));
        this.opt_accuracy.addItem(bundle.getString("lv_mid"));
        this.opt_accuracy.addItem(bundle.getString("lv_low"));

        this.lb_contrast.setText(bundle.getString("contrast") + "50%");
        this.lb_fill.setText(bundle.getString("fill") + "5");
        this.setTitle(bundle.getString("project") + " " + software_version);

        str_font = bundle.getString("font");
        str_typeface = bundle.getString("typeface");
        str_size = bundle.getString("size");
        str_routine = bundle.getString("routine");
        str_italic = bundle.getString("text_it");
        str_bold = bundle.getString("text_bold");
        str_bold_italic = bundle.getString("text_bold_it");
        str_vertical = bundle.getString("vertical");
        str_vector = bundle.getString("vector");
        str_outdated = bundle.getString("outdated");
        str_set = bundle.getString("set");
        str_firmware = bundle.getString("firmware_update");
        str_model = bundle.getString("model");
        str_update = bundle.getString("update");
        str_download_fail = bundle.getString("downlaod_failed");
    }

    private void formWindowOpened(WindowEvent evt) {
        try {
            this.btn_wificonnect.setVisible(false);
            Update.update();
            FileTransferHandler ft = new FileTransferHandler();
            FileTransferHandler.board = this.board;
            this.board.setTransferHandler(ft);
            this.setLocale();
            BElement ty = new BElement();
            ty.type = 0;
            int i = 0;

            while (true) {
                if (i >= Board.BOARD_HEIGHT / 10 + 1) {
                    i = 0;

                    while (true) {
                        if (i >= Board.BOARD_WIDTH / 10 + 1) {
                            Board.bElements.add(ty);
                            this.updateBoard();
                            this.setIconImage((new ImageIcon(this.getClass().getResource("/res/tu_biao.png"))).getImage());
                            this.lb_wifi.setVisible(false);
                            this.lb_pwd.setVisible(false);
                            this.btn_unknown.setVisible(false);
                            this.board.setPanelHints(
                                this.pn_inlay_hint,
                                this.tf_inlay_x, this.tf_inlay_y, this.tf_inlay_w, this.tf_inlay_h
                            );
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
                                        Main.this.wifi.btn_wificonnect = Main.this.btn_wificonnect;
                                        Main.this.wifi.board = Main.this.board;
                                        Main.this.wifi.sd_accuracy = Main.this.opt_accuracy;
                                        Main.this.wifi.sd_weak_light = Main.this.sd_weak_light;
                                        Main.this.wifi.jdt = Main.this.jdt;
                                        Main.this.wifi.window = Main.this.window;
                                    }

                                    try {
                                        Thread.sleep(1000L);
                                    } catch (InterruptedException var2) {
                                        Logger.getLogger("MAIN").log(Level.SEVERE, null, var2);
                                    }

                                    if (Main.engraveFinished && !Main.this.paused) {
                                        ++Main.this.sec;
                                        Main.this.lb_execution_time.setText(Main.this.sec / 60 + "." + Main.this.sec % 60);
                                        if (Main.timeout++ > 3 && Main.timeout != 0) {
                                            System.out.println("&&&");
                                            Main.this.jdt.setValue(0);
                                            Main.this.jdt.setVisible(false);
                                            Main.engraveStarted = false;
                                            Main.timeout = 0;
                                            Main.engraveFinished = false;
                                        }
                                    }
                                }
                            }).start();
                            this.qu_yu();
                            return;
                        }
                        ty.path.moveTo(i / Board.RESOLUTION * 10.0D, 0.0D);
                        ty.path.lineTo(i / Board.RESOLUTION * 10.0D, Board.BOARD_HEIGHT / Board.RESOLUTION);
                        ++i;
                    }
                }

                ty.path.moveTo(0.0D, i / Board.RESOLUTION * 10.0D);
                ty.path.lineTo(Board.BOARD_WIDTH / Board.RESOLUTION, i / Board.RESOLUTION * 10.0D);
                ++i;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }
    }

    public static boolean inRange(int origin, int range, int val) {
        return inRange(origin, range, range, val);
    }
    public static boolean inRange(int origin, int l, int r, int val) {
        return origin - l < val && val < origin + r;
    }

    int qu_anniu(int x, int y) {
        Rectangle r = BElement.getBounds(Board.bElements);
        if (inRange(r.x, 15, x) && inRange(r.y, 15, y)) {
            return 1;
        } else if (inRange(r.x + r.width, 15, x) && inRange(r.y, 15, y)) {
            return 2;
        } else if (inRange(r.x + r.width, 15, x) && inRange(r.y + r.height, 15, y)) {
            return 3;
        } else if (inRange(r.x, 15, x) && inRange(r.y + r.height, 15, y)) {
            return 4;
        } else if (inRange(r.x + r.width, 15, x) && y > r.y + r.height + 20 && y < r.y + r.height + 50) {
            return 5;
        } else if (x > r.x + r.width + 25 && x < r.x + r.width + 25 + 60 && y > r.y - 20 && y < r.y - 20 + 65) {
            System.out.println(6);
            return 6;
        } else if (x > r.x + r.width + 25 && x < r.x + r.width + 25 + 60 && y > r.y + 45 && y < r.y + 45 + 65) {
            System.out.println(7);
            return 7;
        } else if (x > r.x + r.width + 25 && x < r.x + r.width + 25 + 60 && y > r.y + 110 && y < r.y + 110 + 65) {
            System.out.println(8);
            return 8;
        } else if (x > r.x + r.width + 25 && x < r.x + r.width + 25 + 60 && y > r.y + 175 && y < r.y + 175 + 65) {
            System.out.println(9);
            return 9;
        } else if (x > r.x + r.width - 50 && x < r.x + r.width + 50 && y > r.y + r.height + 20 && y < r.y + r.height + 50) {
            return 10;
        } else if (x > r.x + r.width - 85 && x < r.x + r.width + 85 && y > r.y + r.height + 20 && y < r.y + r.height + 50) {
            return 11;
        } else if (x > r.x + r.width - 120 && x < r.x + r.width + 120 && y > r.y + r.height + 20 && y < r.y + r.height + 50) {
            return 12;
        } else {
            return x > r.x + r.width - 155 && x < r.x + r.width + 155 && y > r.y + r.height + 20 && y < r.y + r.height + 50 ? 13 : 0;
        }
    }

    void evt_mouse_pressed(MouseEvent evt) {
        this.clicked = true;
        this.clickX = evt.getX();
        this.clickY = evt.getY();
        this.clickX1 = this.clickX;
        this.clickY1 = this.clickY;
        this.btnActive = evt.getButton();
        Rectangle rBoard = BElement.getBounds(Board.bElements);
        if (this.btnActive == 1) {
            this.an = this.qu_anniu(this.clickX, this.clickY);
            switch (this.an) {
                case 1:
                    Board.bElements = Board.bElements.stream().filter(e -> !e.selected).toList();
                    this.updateBoard();
                    Undo.add();
                    return;
                case 2:
                case 3:
                    return;
                case 4:
                    Board.lock = !Board.lock;
                    this.updateBoard();
                    return;
                case 5:
                    Board.center();
                    this.updateBoard();
                    Undo.add();
                    return;
                case 6:
                case 7:
                case 8:
                case 9:
                    Board.bElements.stream().filter(e -> e.selected && e.type == 1).findFirst().ifPresent(
                            e -> {
                                e.process_code = this.an - 5;
                                e.process();
                                this.updateBoard();
                                Undo.add();
                            }
                    );
                    return;
                case 10:
                case 11:
                case 12:
                    Board.bElements.stream().filter(e -> e.selected && e.type == 1).findFirst().ifPresent(
                        e -> {
                            switch (this.an) {
                                case 10 -> e.process_doMirrorY = !e.process_doMirrorY;
                                case 11 -> e.process_doMirrorX = !e.process_doMirrorX;
                                case 12 -> e.process_doInvert = !e.process_doInvert;
                            }
                            e.process();
                            this.updateBoard();
                            Undo.add();
                        }
                    );
                    return;
                case 13:
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
                    element.Tx.concatenate(AffineTransform.getTranslateInstance(-rect.x, -rect.y));
                    element.Tx.concatenate(AffineTransform.getScaleInstance((double) rBoard.width / rect.width, (double) rBoard.height / rect.height));
                    element.Tx.concatenate(AffineTransform.getTranslateInstance(rBoard.x, rBoard.y));
                    Board.bElements.add(element);
                    Undo.add();
                    return;
                default:
                    Rectangle rect_q = BElement.getBounds(Board.bElements);
                    if (this.clickX > rect_q.x && this.clickX < rect_q.x + rect_q.width && this.clickY > rect_q.y && this.clickY < rect_q.y + rect_q.height) {
                        this.selection = false;
                        this.updateBoard();
                    } else {
                        for (var e: Board.bElements) {
                            if (!e.selected) {
                                Rectangle r = BElement.getBounds(e);
                                if (
                                    inRange(r.x, 0, r.width, this.clickX)
                                    && inRange(r.y, 0, r.height, this.clickY)
                                ) {
                                    Board.unselectAll();
                                    e.selected = true;
                                    Board.bElements.remove(e);
                                    Board.bElements.add(1, e);
                                    this.sd_contrast.setValue(e.threshold);
                                    this.updateBoard();
                                    this.selection = false;
                                    return;
                                }
                            }

                            Board.unselectAll();
                            this.selection = true;
                            this.updateBoard();
                        }
                    }
            }

        } else if (this.btnActive == 3) {
        }
    }

    void evt_mouse_dragged(MouseEvent evt) {
        if (this.clicked) {
            int dx = evt.getX();
            int dy = evt.getY();
            if (this.btnActive == 1) {
                Rectangle rect = BElement.getBounds(Board.bElements);
                if (this.an == 0) {
                    if (this.selection) {
                        Board.dragged = true;
                        Board.mouse = new Rectangle(
                            Math.min(dx, this.clickX1),
                            Math.min(dy, this.clickY1),
                            Math.abs(this.clickX1 - dx),
                            Math.abs(this.clickY1 - dy)
                        );
                        BElement.selectByBoundingBox(Board.bElements, Board.mouse);
                    } else {
                        Board.bElements.stream().filter(e -> e.selected).forEach(
                            e -> e.translate(dx - this.clickX, dy - this.clickY)
                        );
                    }
                    this.clickX = dx;
                    this.clickY = dy;
                } else if (this.an == 2) {
                    float center_x = (float) (rect.x + rect.width / 2);
                    float center_y = (float) (rect.y + rect.height / 2);

                    double rad1 = Math.toDegrees(Math.atan2(this.clickY - center_y, this.clickX - center_x));
                    double rad2 = Math.toDegrees(Math.atan2(dy - center_y, dx - center_x));

                    if (dx != center_x && dy != center_y) {
                        Board.bElements.stream().filter(e -> e.selected).forEach(
                            e -> e.rotateByRad(rad2 - rad1, center_x, center_y)
                        );
                        this.clickX = dx;
                        this.clickY = dy;
                    }
                } else if (this.an == 3) {
                    double scaleX = (double) (this.clickX - rect.x) / (double) rect.width;
                    double scaleY = (double) (this.clickY - rect.y) / (double) rect.height;
                    if (Board.lock) {
                        if (scaleX > 0) {
                            Board.bElements.stream().filter(e -> e.selected).forEach(
                                e -> {
                                    e.translate(-rect.x, -rect.y);
                                    e.scale(scaleX, scaleX);
                                    e.translate(rect.x, rect.y);
                                }
                            );
                        }
                        this.clickX = dx;
                    } else {
                        if (scaleX > 0 && scaleY > 0) {
                            Board.bElements.stream().filter(e -> e.selected).forEach(
                                e -> {
                                    e.translate(-rect.x, -rect.y);
                                    e.scale(scaleX, scaleY);
                                    e.translate(rect.x, rect.y);
                                }
                            );
                        }
                        this.clickX = dx;
                        this.clickY = dy;
                    }
                }
                this.updateBoard();
                this.dragging = true;
            } else if (this.btnActive == 3) {
                Board.translateX = (int) ((Board.translateX + dx - this.clickX) / Board.scale);
                Board.translateY = (int) ((Board.translateY + dy - this.clickY) / Board.scale);

                Board.bElements.forEach(
                    e -> e.Tx.concatenate(AffineTransform.getTranslateInstance(dx - this.clickX, dy - this.clickY))
                );

                this.clickX = dx;
                this.clickY = dy;
                this.updateBoard();
            }
        }

    }

    private void evt_openpic(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fc.setAccessory(preview);
        fc.addPropertyChangeListener(preview);
        fc.setFileFilter(
            new FileNameExtensionFilter(
                "Picture Files (.nc,.bmp,.jpg,.png,.jpeg,.gif,.xj,plt)",
                "nc", "bmp", "jpg", "png", "jpeg", "gif", "xj", "plt")
        );
        int returnVal = fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null && returnVal == 0) {
            File file = fc.getSelectedFile();
            FileTransferHandler.importFile(file, this::updateBoard);
        }
    }

    private void boardZoom(MouseWheelEvent evt) {
        int dx = evt.getX();
        int dy = evt.getY();
        if (evt.getPreciseWheelRotation() < 0) {
            Board.scale *= 1.1;
            Board.bElements.forEach(
                e -> {
                    e.Tx.concatenate(AffineTransform.getTranslateInstance(-dx, -dy));
                    e.Tx.concatenate(AffineTransform.getScaleInstance(1.1, 1.1));
                    e.Tx.concatenate(AffineTransform.getTranslateInstance(dx, dy));
                }
            );
        } else {
            Board.scale *= 0.9;
            Board.bElements.forEach(
                e -> {
                    e.Tx.concatenate(AffineTransform.getTranslateInstance(-dx, -dy));
                    e.Tx.concatenate(AffineTransform.getScaleInstance(0.9, 0.9));
                    e.Tx.concatenate(AffineTransform.getTranslateInstance(dx, dy));
                }
            );
        }
        this.updateBoard();
    }

    private void evt_engrave(ActionEvent evt) {
        if (this.comOpened || this.wifi.connected) {
            if (!engraveStarted) {
                if (Board.inPreview) {
                    this.send(new byte[]{33, 0, 4, 0}, 3);
                    Board.inPreview = false;
                }

                this.send(new byte[]{22, 0, 4, 0}, 2);
                new Thread(() -> {
                    try {
                        Main.this.btn_engrave.setEnabled(false);
                        Main.this.engrave();
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
        if (engraveStarted) return;

        if (Board.inPreview) {
            new Thread(() -> {
                this.send(new byte[] {33, 0, 4, 0}, 3);
                Board.inPreview = false;
                this.updateBoard();
            }).start();
            return;
        }

        GeneralPath path = new GeneralPath((Board.getBG().path));
        path.transform(Board.getBG().Tx);
        Rectangle r = path.getBounds();
        Rectangle bound = new Rectangle(Board.bounds);
        bound = AffineTransform.getTranslateInstance(-r.x, -r.y)
            .createTransformedShape(bound).getBounds();
        bound = AffineTransform.getScaleInstance(1.0D / Board.scale, 1.0D / Board.scale)
            .createTransformedShape(bound).getBounds();
        byte w2 = (byte) (bound.width >> 8);
        byte w1 = (byte) bound.width;
        byte h2 = (byte) (bound.height >> 8);
        byte h1 = (byte) bound.height;
        byte x2 = (byte) (bound.x + 67 + bound.width / 2 >> 8);
        byte x1 = (byte) (bound.x + 67 + bound.width / 2);
        byte y2 = (byte) (bound.y + bound.height / 2 >> 8);
        byte y1 = (byte) (bound.y + bound.height / 2);
        new Thread(() -> {
            this.send(new byte[] {32, 0, 11, w2, w1, h2, h1, x2, x1, y2, y1}, 1);
            Board.inPreview = true;
            this.updateBoard();
        }).start();
    }

    private void evt_circle(ActionEvent evt) {
        Board.bElements.add(BElement.create(2, null));
        Board.selectLast();
        Board.center();
        Undo.add();
        this.updateBoard();
    }

    private void evt_heart(ActionEvent evt) {
        Board.bElements.add(BElement.create(3, null));
        Board.selectLast();
        Board.center();
        Undo.add();
        this.updateBoard();
    }

    private void evt_star(ActionEvent evt) {
        Board.bElements.add(BElement.create(4, null));
        Board.selectLast();
        Board.center();
        Undo.add();
        this.updateBoard();
    }

    private void evt_text(ActionEvent evt) {
        FontDialog dialog = new FontDialog(this.board, true);
        dialog.setDefaultCloseOperation(2);
        dialog.setVisible(true);
    }

    private void evt_square(ActionEvent evt) {
        Board.bElements.add(BElement.create(0, null));
        Board.selectLast();
        Board.center();
        Undo.add();
        this.updateBoard();
    }

    void apply_settings_carving() {
        new Thread(() -> {
            int dp = this.sd_carve_depth.getValue();
            int pw = this.sd_carve_power.getValue() * 10;
            this.send_offline(new byte[]{37, 0, 11, (byte) (dp >> 8), (byte) dp, (byte) (pw >> 8), (byte) pw, 0, 0, 0, 0}, 2);
        }).start();
    }

    void apply_settings_cutting() {
        new Thread(() -> {
            int dp = this.sd_cut_depth.getValue();
            int pw = this.sd_cut_power.getValue() * 10;
            this.send_offline(new byte[]{37, 0, 11, (byte) (dp >> 8), (byte) dp, (byte) (pw >> 8), (byte) pw, 0, 0, 0, 0}, 2);
        }).start();
    }

    void apply_settings_general() {
        new Thread(() -> {
            int weak_light = this.sd_weak_light.getValue() * 2;
            int accuracy = this.opt_accuracy.getSelectedIndex();
            Main.this.send(new byte[] {40, 0, 11, (byte) weak_light, (byte) accuracy, 0, 0, 0, 0, 0, 0}, 2);
        }).start();
    }

    private void changeAccuracy(ActionEvent evt) {
        if (this.wifi != null) {
            if (this.comOpened || this.wifi.connected) {
                if (this.driver_version[2] == 37) {
                    if (this.opt_accuracy.getSelectedIndex() == 0) {
                        Board.RESOLUTION = 0.064D;
                    } else if (this.opt_accuracy.getSelectedIndex() == 1) {
                        Board.RESOLUTION = 0.08D;
                    } else if (this.opt_accuracy.getSelectedIndex() == 2) {
                        Board.RESOLUTION = 0.096D;
                    }
                } else if (this.opt_accuracy.getSelectedIndex() == 0) {
                    Board.RESOLUTION = 0.05D;
                } else if (this.opt_accuracy.getSelectedIndex() == 1) {
                    Board.RESOLUTION = 0.0625D;
                } else if (this.opt_accuracy.getSelectedIndex() == 2) {
                    Board.RESOLUTION = 0.075D;
                }

                this.board.boardSetup();
                this.apply_settings_general();
            }

        }
    }

    private void evt_aux_positioning(ActionEvent evt) {
        new Thread(() -> {
            if (Main.this.auxPositioning) {
                Main.this.send(new byte[]{7, 0, 4, 0}, 2);
            } else {
                Main.this.send(new byte[]{6, 0, 4, 0}, 2);
            }
            Main.this.auxPositioning = !Main.this.auxPositioning;
        }).start();
    }

    private void evt_save(ActionEvent evt) {
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

            Board.backup();
            BufferedImage image = Board.toImage();
            Board.restore();
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

    private void evt_convertBMP(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fc.setAccessory(preview);
        fc.addPropertyChangeListener(preview);
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files (.bmp,.jpg,.png,.jpeg,.gif)", "bmp", "jpg", "png", "jpeg", "gif");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null && returnVal == 0) {
            for (File file : fc.getSelectedFiles()) {
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
        this.lb_contrast.setText(bundle.getString("contrast") + this.sd_contrast.getValue() + "%");
        if (Board.bElements.size() >= 2) {
            Board.bElements.stream().filter(e -> e.selected && e.type == 1)
                    .forEach(e -> {
                        e.threshold = this.sd_contrast.getValue();
                        e.process();
                    });
            this.updateBoard();
        }
    }

    private void jSlider7StateChanged(ChangeEvent evt) {
        this.lb_fill.setText(bundle.getString("fill") + this.sd_fill.getValue());
        Board.FILL_WIDTH = this.sd_fill.getValue();
        this.updateBoard();
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

            Board.scale *= b;
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

        this.updateBoard();
    }

    /**
     * tells the machine to go offline
     *
     * @param length        length of packet
     * @param version       version
     * @param carveWidth    carving width
     * @param carveHeight   carving height
     * @param carvePosition carving position
     * @param carvePower    carving power
     * @param carveDepth    carving depth
     * @param cutWidth      cutting width
     * @param cutHeight     cutting height
     * @param cutPosition   cutting position
     * @param cutPower      cutting power
     * @param cutDepth      cutting depth
     * @param cutNumPts     number of points in cutting
     * @param z
     * @param s
     * @param nTimes        number of times
     * @return true if successful
     */
    boolean engrave(
            int length, int version,
            int carveWidth, int carveHeight, int carvePosition, int carvePower, int carveDepth,
            int cutWidth, int cutHeight, int cutPosition, int cutPower, int cutDepth,
            int cutNumPts, int z, int s, int nTimes
    ) {
        byte[] data = new byte[] {
                35, 0, 38,
                (byte) (length >> 8), (byte) length,
                (byte) version,
                (byte) (carveWidth >> 8), (byte) carveWidth,
                (byte) (carveHeight >> 8), (byte) carveHeight,
                (byte) (carvePosition >> 8), (byte) carvePosition,
                (byte) (carvePower >> 8), (byte) carvePower,
                (byte) (carveDepth >> 8), (byte) carveDepth,
                (byte) (cutWidth >> 8), (byte) cutWidth,
                (byte) (cutHeight >> 8), (byte) cutHeight,
                (byte) (cutPosition >> 24), (byte) (cutPosition >> 16), (byte) (cutPosition >> 8), (byte) cutPosition,
                (byte) (cutPower >> 8), (byte) cutPower,
                (byte) (cutDepth >> 8), (byte) cutDepth,
                (byte) (cutNumPts >> 24), (byte) (cutNumPts >> 16), (byte) (cutNumPts >> 8), (byte) cutNumPts,
                (byte) (z >> 8), (byte) z,
                (byte) (s >> 8), (byte) s,
                (byte) nTimes, 0
        };
        if (this.comOpened) {
            return handler.send_offline(data, 2);
        } else {
            return this.wifi.connected && this.wifi.kaishi(data, 22);
        }
    }

    boolean send(byte[] data, int timeout) {
        return this.comOpened ? handler.send(data, timeout) : this.wifi.send(data, timeout * 100);
    }

    boolean send_offline(byte[] data, int timeout) {
        return this.comOpened ? handler.send_offline(data, timeout) : this.wifi.send(data, timeout * 100);
    }

    void engrave() {
        int carveWidth = 0;
        int carveHeight = 0;
        boolean doCarving, doCutting;
        int cutPosition;
        byte bl = 0;
        int carveWidthInByte = 0;

        Board.backup();
        BufferedImage image = Board.toImage();
        List<BPoint> bPoints = Board.getPoints();
        Board.restore();

        if (image == null && bPoints == null) {
            this.btn_engrave.setEnabled(true);
            return;
        }
        doCarving = (image != null);
        doCutting = (bPoints != null);

        if (doCarving) {
            carveWidth = image.getWidth();
            carveHeight = image.getHeight();
            carveWidthInByte = (carveWidth % 8 == 0) ? carveWidth / 8 : carveWidth / 8 + 1;
        }
        int carve_bytes = carveHeight * carveWidthInByte;
        cutPosition = 33 + carve_bytes;

        if (bPoints == null) {
            bPoints = new ArrayList<>();
        }

        int cutX2 = Board.bounds.x + Board.bounds.width / 2 + 67;
        int cutY2 = Board.bounds.y + Board.bounds.height / 2;
        byte[] points_data = new byte[carve_bytes + bPoints.size() * 4];

        engraveStarted = true;
        int packet_len = (33 + carve_bytes + bPoints.size() * 4) / 4094 + 1;
        this.engrave(
            packet_len,
                1,
                carveWidth, carveHeight, 33,
                this.sd_carve_power.getValue() * 10,
                this.sd_carve_depth.getValue(),
                Board.bounds.width,
                Board.bounds.height,
                cutPosition,
                this.sd_cut_power.getValue() * 10,
                this.sd_cut_depth.getValue(),
                bPoints.size(),
                cutX2,
                cutY2,
                this.opt_num_times.getSelectedIndex() + 1
        );

        try {
            Thread.sleep(40 * packet_len);
        } catch (InterruptedException e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }

        for (int i = 0; i < 2; i++) {
            this.send(new byte[] {10, 0, 4, 0}, 1);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
            }
        }

        byte[] bitVals = new byte[] {(byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        boolean doResend;
        int count = 0;
        if (doCarving) {
            int[] pixels = new int[carveWidth * carveHeight];
            image.getRGB(0, 0, carveWidth, carveHeight, pixels, 0, carveWidth);

            for (int i = 0; i < carveHeight; ++i) {
                // do for each row
                for (int j = 0; j < carveWidthInByte; ++j) {
                    for (int bit = 0; bit < 8; ++bit) {
                        // merge horizontally
                        // 8 pixels into 1 byte
                        int p = i * carveWidth + j * 8 + bit;
                        if (p < pixels.length && (pixels[p] & 0xff0000) < 0x0a0000) {
                            bl |= bitVals[bit] + 0x80 >> bit;
                        }
                    }
                    points_data[count++] = bl;
                }
            }
        }

        if (doCutting) {
            for (BPoint p : bPoints) {
                points_data[count++] = (byte) p.x;
                points_data[count++] = (byte) (p.x >> 8);
                points_data[count++] = (byte) p.y;
                points_data[count++] = (byte) (p.y >> 8);
            }
        }

        int MAX_PACKET_SIZE = 1900;
        byte[] engrave_data = new byte[MAX_PACKET_SIZE + 4];
        for (int i = 0; i < points_data.length / MAX_PACKET_SIZE; ++i) {
            System.arraycopy(
                points_data, i * MAX_PACKET_SIZE,
                engrave_data, 3,
                MAX_PACKET_SIZE
            );

            do {
                engrave_data[0] = 34;
                engrave_data[1] = (byte) (engrave_data.length >> 8);
                engrave_data[2] = (byte) engrave_data.length;
                engrave_data[engrave_data.length - 1] = Utils.checksum(engrave_data);
                doResend = !this.send(engrave_data, 2);
            } while (doResend);

            this.jdt.setVisible(true);
            this.jdt.setValue((int) (i / points_data.length * MAX_PACKET_SIZE * 100.0F));
        }

        engrave_data = new byte[points_data.length % MAX_PACKET_SIZE + 4];
        if (points_data.length % MAX_PACKET_SIZE > 0) {
            System.arraycopy(
                points_data, points_data.length / MAX_PACKET_SIZE * MAX_PACKET_SIZE,
                engrave_data, 3,
                points_data.length % MAX_PACKET_SIZE
            );

            do {
                engrave_data[0] = 34;
                engrave_data[1] = (byte) (engrave_data.length >> 8);
                engrave_data[2] = (byte) engrave_data.length;
                engrave_data[engrave_data.length - 1] = Utils.checksum(engrave_data);
                doResend = !this.send(engrave_data, 2);
            } while (doResend);
        }

        try {
            for (int i = 0; i < 2; i++) {
                Thread.sleep(200L);
                this.send(new byte[]{36, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 1);
            }
            Thread.sleep(500L);
        } catch (Exception e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }

        engraveFinished = true;
        if (handler != null) {
            handler.terminateWith(3, 0);
        }

        this.jdt.setVisible(false);
        this.btn_engrave.setEnabled(true);
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
                            if (Board.RESOLUTION == 0.096D) {
                                this.opt_accuracy.setSelectedIndex(2);
                            } else if (Board.RESOLUTION == 0.08D) {
                                this.opt_accuracy.setSelectedIndex(1);
                            } else if (Board.RESOLUTION == 0.064D) {
                                this.opt_accuracy.setSelectedIndex(0);
                            }
                        } else if (Board.RESOLUTION == 0.075D) {
                            this.opt_accuracy.setSelectedIndex(2);
                        } else if (Board.RESOLUTION == 0.0625D) {
                            this.opt_accuracy.setSelectedIndex(1);
                        } else if (Board.RESOLUTION == 0.05D) {
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
                                    this.getClass().getResource("/res/usb.png")
                            )
                    )
            );
        } else {
            this.btn_usbconnect.setIcon(
                    new ImageIcon(
                            Objects.requireNonNull(
                                    this.getClass().getResource("/res/usb2.png")
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
                    * Board.scale / Board.RESOLUTION;
            Board.bElements.stream().filter(e -> e.selected)
                    .forEach(e -> e.translate((int) var_x, e.path.getBounds().y));
            this.updateBoard();
        }
        if (this.tf_inlay_y.getText().length() != 0) {
            double var_y = (Integer.parseInt(this.tf_inlay_y.getText()) - this.board.val_y)
                    * Board.scale / Board.RESOLUTION;
            Board.bElements.stream().filter(e -> e.selected)
                    .forEach(e -> e.translate(e.path.getBounds().x, (int) var_y));
            this.updateBoard();
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
            this.updateBoard();
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
            this.updateBoard();
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
                        this.updateBoard();
                        Undo.add();
                    }
                }
                case 65 -> {
                    Board.bElements.forEach(e -> e.selected = true);
                    this.updateBoard();
                }
                case 90 -> {
                    Undo.undo();
                    this.updateBoard();
                }
                case 88 -> {
                    Board.bElements.forEach(e -> e.selected = true);
                    Undo.redo();
                    this.updateBoard();
                }
            }
        }
        if (key == 10) {
            this.set();
        } else if (key == 127) {
            Board.bElements = Board.bElements.stream().filter(e -> !e.selected).toList();
            this.updateBoard();
            Undo.add();
        }

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
