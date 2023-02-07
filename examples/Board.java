package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    public static int bWidth = 80;
    public static int bHeight = 80;
    public static List<BElement> bElements = new ArrayList<>();
    public static double resolution = 0.075D;
    public static boolean boundingBox = false;
    public static boolean lock = true;

    public static int quan_x = 0;
    public static int quan_y = 0;
    public static double quan_beishu = 1.0D;
    public static int quan_x2 = 0;
    public static int quan_y2 = 0;
    public static double quan_beishu2 = 1.0D;

    // parent and child window/panels
    public mainJFrame window = null;
    public JPanel pn_settings;
    public JPanel pn_inlay_hint;
    // public static int dang_qian2 = -1;
    // public AffineTransform Tx = new AffineTransform();
    // public JPanel jp2;
    // public JLabel wb;

    // text fields and values for inlay hint
    public JTextField tf_x;
    public JTextField tf_y;
    public JTextField tf_w;
    public JTextField tf_h;
    public int val_x = 0;
    public int val_y = 0;
    public int val_w = 0;
    public int val_h = 0;

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        boolean selected = false;

        for (int i = 0; i < bElements.size(); ++i) {
            GeneralPath newPath = new GeneralPath(bElements.get(i).path);

            newPath.transform(bElements.get(i).Tx);
            Rectangle rect = newPath.getBounds();
            double mm = (double) bWidth / 10.0D / (double) rect.width;
            int w = (int) (mm / 0.02D);
            if (w < 1) {
                w = 1;
            }

            if (i != 0) {
                g2D.setPaint(Color.BLACK);
                newPath.setWindingRule(0);
                if (bElements.get(i).fill) {
                    g2D.fill(newPath);
                }

                g2D.setColor(Color.BLUE);
                g2D.draw(newPath);
                g2D.setColor(Color.BLUE);
                if (BElement.tuo) {
                    g2D.draw(BElement.shu_biao);
                }

                g2D.setColor(Color.RED);
                g2D.setColor(Color.BLACK);
            } else {
                g2D.setColor(Color.LIGHT_GRAY);
                g2D.draw(newPath);

                int j;
                for (j = 0; j < bWidth / 10 + 1; ++j) {
                    if ((double) j % (double) w == 0.0D) {
                        g2D.drawString(String.valueOf(j), (int) ((double) rect.x + (double) j * ((double) rect.width / ((double) bWidth / 10.0D))), rect.y);
                    }
                }

                for (j = 0; j < bHeight / 10 + 1; ++j) {
                    if ((double) j % (double) w == 0.0D) {
                        g2D.drawString(String.valueOf(j), rect.x - 16, (int) ((double) rect.y + (double) j * ((double) rect.height / ((double) bHeight / 10.0D)) + 10.0D));
                    }
                }

                g2D.setColor(Color.BLACK);
            }

            if (bElements.get(i).type == 1) {
                g2D.drawImage(bElements.get(i).wei_tu, bElements.get(i).Tx, null);
            }

            if (bElements.get(i).selected) {
                selected = true;
            }
        }

        if (selected && !BElement.tuo) {
            Rectangle rect = BElement.qu_jv_xing(bElements);
            g2D.setColor(Color.GREEN);
            g2D.draw(rect);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/ic_icon_delete.png"))).getImage(), rect.x - 15, rect.y - 15, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/ic_icon_rotate.png"))).getImage(), rect.x + rect.width - 15, rect.y - 15, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/ic_icon_scale.png"))).getImage(), rect.x + rect.width - 15, rect.y + rect.height - 15, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/zhong_xin.png"))).getImage(), rect.x + rect.width - 15, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/hei_bai.png"))).getImage(), rect.x + rect.width + 25, rect.y - 20, 60, 65, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/hui_du.png"))).getImage(), rect.x + rect.width + 25, rect.y + 45, 60, 65, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/lun_kuo.png"))).getImage(), rect.x + rect.width + 25, rect.y + 110, 60, 65, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/su_miao.png"))).getImage(), rect.x + rect.width + 25, rect.y + 175, 60, 65, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/jing_xiang_y.png"))).getImage(), rect.x + rect.width - 50, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/jing_xiang_x.png"))).getImage(), rect.x + rect.width - 85, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/fan_se.png"))).getImage(), rect.x + rect.width - 120, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/tian_chong.png"))).getImage(), rect.x + rect.width - 155, rect.y + rect.height + 20, 30, 30, null);
            if (lock) {
                g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/suo1.png"))).getImage(), rect.x - 15, rect.y + rect.height - 15, 30, 30, null);
            } else {
                g2D.drawImage((new ImageIcon(this.getClass().getResource("/tu/suo2.png"))).getImage(), rect.x - 15, rect.y + rect.height - 15, 30, 30, null);
            }

            g2D.setColor(Color.BLUE);
            g2D.setFont(new Font(g2D.getFont().getName(), g2D.getFont().getStyle(), 16));
            Point2D d = this.transform(rect.getLocation());
            this.val_x = (int) Math.round(d.getX() * resolution);
            this.val_y = (int) Math.round(d.getY() * resolution);
            d = this.transform(new Point(rect.x + rect.width, rect.y + rect.height));
            this.val_w = (int) Math.round(d.getX() * resolution) - this.val_x;
            this.val_h = (int) Math.round(d.getY() * resolution) - this.val_y;
            this.pn_inlay_hint.setLocation(rect.x - 18, rect.y - 60);
            this.pn_inlay_hint.setSize(245, 35);
            this.tf_x.setText(String.valueOf(this.val_x));
            this.tf_y.setText(String.valueOf(this.val_y));
            this.tf_w.setText(String.valueOf(this.val_w));
            this.tf_h.setText(String.valueOf(this.val_h));
            g2D.setColor(Color.BLACK);
        }

        if (this.pn_inlay_hint != null && !selected) {
            this.pn_inlay_hint.setLocation(2, -this.pn_inlay_hint.getHeight());
        }

    }

    Point2D transform(Point2D m) {
        BElement ele = bElements.get(0);
        GeneralPath newPath = new GeneralPath(ele.path);
        newPath.transform(ele.Tx);
        Rectangle rect = newPath.getBounds();

        AffineTransform sf = AffineTransform.getTranslateInstance(-rect.x, -rect.y);
        sf.transform(m, m);
        sf = AffineTransform.getScaleInstance(1.0D / quan_beishu, 1.0D / quan_beishu);
        sf.transform(m, m);
        return m;
    }

    public void di_tu() {
        BElement.hui_fu();
        BElement element = new BElement();
        element.type = 0;
        quan_beishu = 1.0D;
        quan_x = 0;
        quan_y = 0;

        for (int i = 0; i < bHeight + 1; ++i) {
            if (i % 5 == 0 || i == bHeight) {
                element.path.moveTo(0.0D, (double) i / resolution);
                element.path.lineTo((double) bWidth / resolution, (double) i / resolution);
            }
        }

        for (int i = 0; i < bWidth + 1; ++i) {
            if (i % 5 == 0 || i == bWidth) {
                element.path.moveTo((double) i / resolution, 0.0D);
                element.path.lineTo((double) i / resolution, (double) bHeight / resolution);
            }
        }

        bElements.set(0, element);
        this.repaint();
        BElement.hui_fu_xian_chang();
    }

    public void version(byte[] bytes, int accuracy) {
        switch (bytes[2]) {
            case 4:
            case 6:
                bWidth = 80;
                bHeight = 75;
                resolution = 0.05D;
                this.di_tu();
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            default:
                break;
            case 21:
            case 22:
                bWidth = 145;
                bHeight = 175;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 23:
                bWidth = 145;
                bHeight = 145;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 31:
                bWidth = 115;
                bHeight = 225;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 32:
                bWidth = 185;
                bHeight = 295;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 33:
                bWidth = 185;
                bHeight = 245;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 34:
                bWidth = 140;
                bHeight = 130;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 35:
                bWidth = 370;
                bHeight = 410;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 36:
                bWidth = 370;
                bHeight = 370;
                if (accuracy == 0) {
                    resolution = 0.05D;
                } else if (accuracy == 1) {
                    resolution = 0.065D;
                } else if (accuracy == 2) {
                    resolution = 0.075D;
                }

                this.di_tu();
                break;
            case 37:
                bWidth = 190;
                bHeight = 215;
                if (accuracy == 0) {
                    resolution = 0.064D;
                } else if (accuracy == 1) {
                    resolution = 0.08D;
                } else if (accuracy == 2) {
                    resolution = 0.096D;
                }

                this.di_tu();
        }

    }

    public static void selectLast() {
        for (BElement bElement : bElements) bElement.selected = false;
        bElements.get(bElements.size() - 1).selected = true;
    }
}
