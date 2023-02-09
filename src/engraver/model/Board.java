package engraver.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    public static int BOARD_WIDTH = 80;
    public static int BOARD_HEIGHT = 80;
    public static double RESOLUTION = 0.075D;
    public static int FILL_WIDTH = 5;

    public static List<BElement> bElements = new ArrayList<>();
    public static boolean inPreview = false;
    public static boolean lock = true;
    public static boolean dragged = false;

    public static int translateX = 0;
    public static int translateY = 0;
    public static double scale = 1.0D;
    public static int translateX2 = 0;
    public static int translateY2 = 0;
    public static double scale2 = 1.0D;

    public static Rectangle bounds = new Rectangle();
    public static Rectangle vector = new Rectangle();
    public static Rectangle mouse = new Rectangle();

    // inlay hints within the board
    public JPanel pn_inlay_hint;
    public JTextField tf_x;
    public JTextField tf_y;
    public JTextField tf_w;
    public JTextField tf_h;
    public int val_x = 0;
    public int val_y = 0;
    public int val_w = 0;
    public int val_h = 0;

    public void setPanelHints(JPanel hint, JTextField x, JTextField y, JTextField w, JTextField h) {
        this.pn_inlay_hint = hint;
        this.tf_x = x;
        this.tf_y = y;
        this.tf_w = w;
        this.tf_h = h;
    }

    public static BufferedImage toImage() {
        int width = (int) (BOARD_WIDTH / RESOLUTION);
        int height = (int) (BOARD_HEIGHT / RESOLUTION);

        BufferedImage image = new BufferedImage(width - 2, height - 2, 2);
        Graphics2D g2D = image.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, image.getWidth(), image.getHeight());
        boolean existed = false;

        for (int i = 1; i < bElements.size(); ++i) {
            BElement e = bElements.get(i);
            if (e.type == 1 && e.process_code != 3) {
                Rectangle rect = BElement.getBounds(e);
                g2D.drawImage(e.toImage(), rect.x, rect.y, null);
                existed = true;
            }
        }

        if (!existed) return null;

        BufferedImage image2 = new BufferedImage(width, height, 2);
        Graphics2D g2D2 = image2.createGraphics();
        g2D2.setBackground(Color.WHITE);
        g2D2.clearRect(0, 0, width, height);
        g2D2.drawImage(image, 1, 1, null);
        resetBoundingBox();
        Rectangle rect = new Rectangle(0, 0, width, height);
        if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
            return null;
        } else if (!rect.contains(bounds.x, bounds.y) && !rect.contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
            return null;
        } else {
            Rectangle r = rect.createIntersection(bounds).getBounds();
            if (r.x + r.width + 5 >= width) {
                width = r.width;
            } else {
                width = r.width + 5;
            }

            if (r.y + r.height + 5 >= height) {
                height = r.height;
            } else {
                height = r.height + 5;
            }

            return image2.getSubimage(r.x, r.y, width, height);
        }
    }

    public static BufferedImage toImageSL() {
        int width = (int) (BOARD_WIDTH / RESOLUTION);
        int height = (int) (BOARD_HEIGHT / RESOLUTION);

        BufferedImage ret = new BufferedImage(width, height, 2);
        Graphics2D g2D = ret.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, ret.getWidth(), ret.getHeight());
        boolean existed = false;

        for (int i = 1; i < bElements.size(); ++i) {
            var e = bElements.get(i);
            GeneralPath path = new GeneralPath(e.path);
            path.transform(e.Tx);
            if (e.type == 0) {
                g2D.setColor(Color.BLACK);
                g2D.draw(path);
                existed = true;
            }
            if (e.type == 1 && e.process_code == 3) {
                g2D.setColor(Color.BLACK);
                Rectangle r = BElement.getBounds(e);
                g2D.drawImage(e.toImage(), r.x, r.y, null);
                existed = true;
            }
        }

        if (!existed) return null;

        try {
            BufferedImage ret2 = new BufferedImage(width + 4, height + 4, 2);
            Graphics2D g2D2 = ret2.createGraphics();
            g2D2.setBackground(Color.WHITE);
            int w = ret2.getWidth();
            int h = ret2.getHeight();
            g2D2.clearRect(0, 0, w, h);
            g2D2.drawImage(ret, 2, 2, null);
            resetBoundingBox();
            Rectangle jx = new Rectangle(0, 0, w, h);
            if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
                return null;
            } else if (!jx.contains(bounds.x, bounds.y) && !jx.contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
                return null;
            } else {
                Rectangle jx2 = jx.createIntersection(bounds).getBounds();
                if (jx2.x + jx2.width + 5 >= w) {
                    w = jx2.width;
                } else {
                    w = jx2.width + 5;
                }

                if (jx2.y + jx2.height + 5 >= h) {
                    h = jx2.height;
                } else {
                    h = jx2.height + 5;
                }

                return ret2.getSubimage(jx2.x, jx2.y, w, h);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public static BufferedImage toImageSLTC() {
        int width = (int) (BOARD_WIDTH / RESOLUTION);
        int height = (int) (BOARD_HEIGHT / RESOLUTION);

        BufferedImage ret = new BufferedImage(width, height, 2);
        Graphics2D g2D = ret.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, ret.getWidth(), ret.getHeight());
        boolean existed = false;

        for (int i = 1; i < bElements.size(); ++i) {
            var e = bElements.get(i);
            GeneralPath path = new GeneralPath(e.path);
            path.transform(e.Tx);
            if (e.type == 0 && e.filled) {
                g2D.setColor(Color.BLACK);
                path.setWindingRule(0);
                g2D.fill(path);
                existed = true;
            }
        }

        if (!existed) return null;

        BufferedImage fh2 = new BufferedImage(width + 4, height + 4, 2);
        Graphics2D g2D2 = fh2.createGraphics();
        g2D2.setBackground(Color.WHITE);
        int w = fh2.getWidth();
        int h = fh2.getHeight();
        g2D2.clearRect(0, 0, w, h);
        g2D2.drawImage(ret, 2, 2, null);
        resetBoundingBox();

        Rectangle jx = new Rectangle(0, 0, w, h);
        if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
            return null;
        } else if (!jx.contains(bounds.x, bounds.y) && !jx.contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
            return null;
        }

        Rectangle jx2 = jx.createIntersection(bounds).getBounds();
        if (jx2.x + jx2.width + 5 >= w) {
            w = jx2.width;
        } else {
            w = jx2.width + 5;
        }

        if (jx2.y + jx2.height + 5 >= h) {
            h = jx2.height;
        } else {
            h = jx2.height + 5;
        }

        return fh2.getSubimage(jx2.x, jx2.y, w, h);
    }

    public static void center() {
        Rectangle r = BElement.getBounds(bElements);
        Rectangle r0 = BElement.getBounds(getBG());
        double x0 = r0.x + r0.width / 2.0, y0 = r0.y + r0.height / 2.0;
        double x = r.x + r.width / 2.0, y = r.y + r.height / 2.0;
        bElements.stream().filter(e -> e.selected).forEach(
            e -> e.translate((int) (x0 - x), (int) (y0 - y))
        );
    }

    public static List<BPoint> getPoints() {
        List<BPoint> ret = new ArrayList<>();
        BufferedImage bb = toImageSL();
        if (bb != null) {
            ret = sort(bb, false);
        }

        BufferedImage bb2 = toImageSLTC();
        if (bb2 != null) {
            ret.addAll(BElement.getFill(bb2, 1 + FILL_WIDTH));
        }

        return ret;
    }

    public static List<BPoint> getPoints(BufferedImage image) {
        List<BPoint> ret = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        for (int i = 1; i < height; ++i) {
            for (int j = 1; j < width; ++j) {
                if (BPoint.isPoint(pixels[width * i + j])) {
                    ret.add(new BPoint(j, i));
                }
            }
        }
        return ret;
    }

    public static List<BPoint> sort(BufferedImage image, boolean forceNotEmpty) {
        image = image.getSubimage(0, 0, image.getWidth(), image.getHeight());
        List<BPoint> points = getPoints(image);
        List<BPoint> ret = new ArrayList<>();

        if (points.size() == 0) return ret;

        BPoint p0 = points.get(0);
        ret.add(p0);
        if (forceNotEmpty) ret.add(new BPoint(30000, 30000));
        image.setRGB(p0.x, p0.y, Color.WHITE.getRGB());

        for (int i = 1; i < points.size(); ++i) {
            BPoint pLast = ret.get(ret.size() - 1), pLast2 = ret.get(ret.size() - 2);

            BPoint p2 = (pLast.x != 30000 && pLast.x != 50000) ? pLast : pLast2;
            BPoint p1 = BPoint.getNearestPoint(p2, image);

            if (p1.x == 50000) {
                break;
            }

            if (forceNotEmpty && !BPoint.isNeighbor(p1, p2)) {
                ret.add(new BPoint(50000, 50000));
                ret.add(new BPoint(30000, 30000));
            }

            ret.add(p1);
            image.setRGB(p1.x, p1.y, Color.WHITE.getRGB());
        }
        if (forceNotEmpty) ret.add(new BPoint(50000, 50000));
        return ret;
    }

    public static GeneralPath to_ls(BufferedImage image) {
        GeneralPath path = new GeneralPath();
        BufferedImage edges = BImage.getEdges(image, 128);
        List<BPoint> points = BElement.optimize(sort(edges, true));

        BPoint point = new BPoint(0, 0);
        boolean drawLine = false;

        if (points.size() > 0) {
            BPoint cur = points.get(0);
            path.moveTo((float) cur.x, (float) cur.y);
            for (int i = 1; i < points.size(); i++) {
                if (cur.x == 30000) {
                    drawLine = true;
                    point = new BPoint(points.get(i - 1));
                } else if (cur.x == 50000) {
                    drawLine = false;
                    if (BPoint.isNeighbor(point, points.get(i - 1))) {
                        path.closePath();
                    }
                } else if (drawLine) {
                    path.lineTo((float) cur.x, (float) cur.y);
                } else {
                    path.moveTo((float) cur.x, (float) cur.y);
                }
            }
        }

        if (BPoint.isNeighbor(point, points.get(points.size() - 1))) {
            path.closePath();
        }

        return path;
    }

    public static void resetBoundingBox() {
        resetVectorBoundingBox();
        Rectangle r = BElement.getBounds(Board.getBG());
        bounds = vector.createIntersection(r).getBounds();
        if (bounds.width <= 0 || bounds.height <= 0) {
            bounds = new Rectangle();
        }
    }

    public static void resetVectorBoundingBox() {
        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, x2 = 0, y2 = 0;
        for (int i = 1; i < Board.bElements.size(); ++i) {
            var e = Board.bElements.get(i);
            if (e.type == 0) {
                Rectangle r = BElement.getBounds(e);
                x1 = Math.min(x1, r.x);
                y1 = Math.min(y1, r.y);
                x2 = Math.max(x2, r.x + r.width);
                y2 = Math.max(y2, r.y + r.height);
            }
        }
        if (x1 == Integer.MAX_VALUE || y1 == Integer.MAX_VALUE || x2 == 0 || y2 == 0) {
            vector = new Rectangle();
            return;
        }
        vector = new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    public static void selectLast() {
        Board.unselectAll();
        bElements.get(bElements.size() - 1).selected = true;
    }

    public static void unselectAll() {
        for (var e : bElements) e.selected = false;
    }

    public static BElement getBG() {
        return bElements.get(0);
    }

    public static void backup() {
        Rectangle re = BElement.getBounds(Board.getBG());
        Board.translateX2 = re.x;
        Board.translateY2 = re.y;
        Board.scale2 = Board.scale;
        Board.translateX = 0;
        Board.translateY = 0;
        Board.scale = 1.0D;
        Board.bElements.forEach(e -> {
            e.Tx.concatenate(
                AffineTransform.getTranslateInstance(-re.x, -re.y)
            );
            e.Tx.concatenate(
                AffineTransform.getScaleInstance(1.0D / Board.scale, 1.0D / Board.scale)
            );
        });
    }

    public static void restore() {
        Board.translateX = Board.translateX2;
        Board.translateY = Board.translateY2;
        Board.scale = Board.scale2;
        Board.bElements.forEach(e -> {
            e.Tx.concatenate(
                AffineTransform.getScaleInstance(Board.scale, Board.scale)
            );
            e.Tx.concatenate(
                AffineTransform.getTranslateInstance(Board.translateX, Board.translateY)
            );
        });
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        boolean selected = false;

        for (int i = 0; i < bElements.size(); ++i) {
            var e = bElements.get(i);
            GeneralPath path = new GeneralPath(e.path);
            path.transform(e.Tx);
            Rectangle rect = path.getBounds();

            int w = Math.max(BOARD_WIDTH * 5 / rect.width, 1);

            if (i != 0) {
                g2D.setPaint(Color.BLACK);
                path.setWindingRule(0);
                if (e.filled) {
                    g2D.fill(path);
                }

                g2D.setColor(Color.BLUE);
                g2D.draw(path);
                if (dragged) {
                    g2D.draw(mouse);
                }

                g2D.setColor(Color.BLACK);
            } else {
                g2D.setColor(Color.LIGHT_GRAY);
                g2D.draw(path);

                for (int j = 0; j < BOARD_WIDTH / 10 + 1; ++j) {
                    if (j % w == 0) {
                        g2D.drawString(
                            String.valueOf(j),
                            rect.x + j * rect.width / BOARD_WIDTH * 10,
                            rect.y
                        );
                        g2D.drawString(
                            String.valueOf(j),
                            rect.x - 16,
                            rect.y + j * rect.height / BOARD_HEIGHT * 10 + 10
                        );
                    }
                }

                g2D.setColor(Color.BLACK);
            }

            if (e.type == 1) {
                g2D.drawImage(e.bitMapImg, e.Tx, null);
            }
            selected = e.selected;
        }

        if (selected && !dragged) {
            Rectangle rect = BElement.getBounds(bElements);
            g2D.setColor(Color.GREEN);
            g2D.draw(rect);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/ic_icon_delete.png")).getImage(),
                rect.x - 15, rect.y - 15, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/ic_icon_rotate.png")).getImage(),
                rect.x + rect.width - 15, rect.y - 15, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/ic_icon_scale.png")).getImage(),
                rect.x + rect.width - 15, rect.y + rect.height - 15, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/zhong_xin.png")).getImage(),
                rect.x + rect.width - 15, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/hei_bai.png")).getImage(),
                rect.x + rect.width + 25, rect.y - 20, 60, 65, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/hui_du.png")).getImage(),
                rect.x + rect.width + 25, rect.y + 45, 60, 65, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/lun_kuo.png")).getImage(),
                rect.x + rect.width + 25, rect.y + 110, 60, 65, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/su_miao.png")).getImage(),
                rect.x + rect.width + 25, rect.y + 175, 60, 65, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/jing_xiang_y.png")).getImage(),
                rect.x + rect.width - 50, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/jing_xiang_x.png")).getImage(),
                rect.x + rect.width - 85, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/fan_se.png")).getImage(),
                rect.x + rect.width - 120, rect.y + rect.height + 20, 30, 30, null);
            g2D.drawImage(
                new ImageIcon(this.getClass().getResource("/res/tian_chong.png")).getImage(),
                rect.x + rect.width - 155, rect.y + rect.height + 20, 30, 30, null);
            if (lock) {
                g2D.drawImage(
                    new ImageIcon(this.getClass().getResource("/res/suo1.png")).getImage(),
                    rect.x - 15, rect.y + rect.height - 15, 30, 30, null);
            } else {
                g2D.drawImage(
                    new ImageIcon(this.getClass().getResource("/res/suo2.png")).getImage(),
                    rect.x - 15, rect.y + rect.height - 15, 30, 30, null);
            }

            g2D.setColor(Color.BLUE);
            g2D.setFont(new Font(g2D.getFont().getName(), g2D.getFont().getStyle(), 16));
            Point2D d = this.transform(rect.getLocation());
            this.val_x = (int) Math.round(d.getX() * RESOLUTION);
            this.val_y = (int) Math.round(d.getY() * RESOLUTION);
            d = this.transform(new Point(rect.x + rect.width, rect.y + rect.height));
            this.val_w = (int) Math.round(d.getX() * RESOLUTION) - this.val_x;
            this.val_h = (int) Math.round(d.getY() * RESOLUTION) - this.val_y;
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
        Rectangle r = BElement.getBounds(getBG());
        AffineTransform.getTranslateInstance(-r.x, -r.y)
            .transform(m, m);
        AffineTransform.getScaleInstance(1.0D / scale, 1.0D / scale)
            .transform(m, m);
        return m;
    }

    public void boardSetup() {
        Board.backup();
        BElement bg = new BElement();
        bg.type = 0;
        scale = 1.0D;
        translateX = 0;
        translateY = 0;

        for (int i = 0; i < BOARD_HEIGHT + 1; ++i) {
            if (i % 5 == 0 || i == BOARD_HEIGHT) {
                bg.path.moveTo(0.0D, (double) i / RESOLUTION);
                bg.path.lineTo((double) BOARD_WIDTH / RESOLUTION, (double) i / RESOLUTION);
            }
        }

        for (int i = 0; i < BOARD_WIDTH + 1; ++i) {
            if (i % 5 == 0 || i == BOARD_WIDTH) {
                bg.path.moveTo((double) i / RESOLUTION, 0.0D);
                bg.path.lineTo((double) i / RESOLUTION, (double) BOARD_HEIGHT / RESOLUTION);
            }
        }

        bElements.set(0, bg);
        this.repaint();
        Board.restore();
    }

    public void version(byte[] bytes, int accuracy) {
        switch (bytes[2]) {
            case 4:
            case 6:
                BOARD_WIDTH = 80;
                BOARD_HEIGHT = 75;
                RESOLUTION = 0.05D;
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
                return;
            case 21:
            case 22:
                BOARD_WIDTH = 145;
                BOARD_HEIGHT = 175;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 23:
                BOARD_WIDTH = 145;
                BOARD_HEIGHT = 145;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 31:
                BOARD_WIDTH = 115;
                BOARD_HEIGHT = 225;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 32:
                BOARD_WIDTH = 185;
                BOARD_HEIGHT = 295;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 33:
                BOARD_WIDTH = 185;
                BOARD_HEIGHT = 245;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 34:
                BOARD_WIDTH = 140;
                BOARD_HEIGHT = 130;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 35:
                BOARD_WIDTH = 370;
                BOARD_HEIGHT = 410;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 36:
                BOARD_WIDTH = 370;
                BOARD_HEIGHT = 370;
                if (accuracy == 0) {
                    RESOLUTION = 0.05D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.065D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.075D;
                }
                break;
            case 37:
                BOARD_WIDTH = 190;
                BOARD_HEIGHT = 215;
                if (accuracy == 0) {
                    RESOLUTION = 0.064D;
                } else if (accuracy == 1) {
                    RESOLUTION = 0.08D;
                } else if (accuracy == 2) {
                    RESOLUTION = 0.096D;
                }
        }
        this.boardSetup();
    }

    public static void addPath(GeneralPath path) {
        path.setWindingRule(0);
        BElement e = BElement.create(0, null);
        e.path = new GeneralPath(path);
        Board.bElements.add(e);
        Board.selectLast();
    }
}
