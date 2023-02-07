package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Float;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BElement implements Serializable {
    // public static final int lei_xing_tupian = 1;
    // public static final int lei_xing_lujing = 0;
    // public static int dk_gonglv = 100;
    // public static int dk_shendu = 10;
    // public static int dk_gonglv_sl = 100;
    // public static int dk_shendu_sl = 10;
    // public static int dk_cishu = 1;
    public static Rectangle bounds = new Rectangle();
    public static Rectangle vector = new Rectangle();
    public static Rectangle mouse = new Rectangle();

    public static boolean dragged = false;
    public boolean filled = false;
    public static int fillGap = 5;

    public static List<BPoint> bPoints = null;
    static int imgH = 0;
    static int imgW = 0;
    static int maxSideLen;
    public int type = 0;
    public boolean selected = false;
    public GeneralPath path = new GeneralPath();
    public transient BufferedImage bitMapImg = null;
    public transient BufferedImage bitMapImg2 = null;
    public int[] bitMap = null;
    public int[] bitMap2 = null;
    public int bitMapW = 0;
    public int bitMapH = 0;
    public int bitMap2W = 0;
    public int bitMap2H = 0;
    public int process_code = 1;
    public boolean process_doInvert = false;
    public boolean process_doMirrorX = false;
    public boolean process_doMirrorY = false;
    public int threshold = 50;
    public AffineTransform Tx = new AffineTransform();

    /**
     * Copy a list of BElements
     *
     * @param list src list
     * @return copied target list
     */
    public static List<BElement> copy(List<BElement> list) {
        return list.stream().map(BElement::copy).collect(Collectors.toList());
    }

    public static BElement copy(BElement ty) {
        BElement res = create(ty.type, ty.bitMapImg);
        res.Tx = new AffineTransform(ty.Tx);
        res.selected = ty.selected;
        res.path = new GeneralPath(ty.path);
        res.process_doInvert = ty.process_doInvert;
        res.process_code = ty.process_code;
        res.process_doMirrorX = ty.process_doMirrorX;
        res.process_doMirrorY = ty.process_doMirrorY;
        res.filled = ty.filled;
        res.threshold = ty.threshold;
        return res;
    }

    static void center(List<BElement> sz) {
        Rectangle rect = getBounds(Board.bElements);
        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing2.transform(Board.bElements.get(0).Tx);
        Rectangle rect2 = lu_jing2.getBounds();
        double x = rect2.x + rect2.width / 2.0, y = rect2.y + rect2.height / 2.0;

        for (int i = 0; i < Board.bElements.size(); ++i) {
            if ((Board.bElements.get(i)).selected) {
                (Board.bElements.get(i)).translate((int) (x - (double) (rect.x + rect.width / 2)), (int) (y - (double) (rect.y + rect.height / 2)));
            }
        }

    }

    public static Rectangle getBounds(BElement element) {
        GeneralPath path = new GeneralPath(element.path);
        path.transform(element.Tx);
        return path.getBounds();
    }

    public static Rectangle getBounds(List<BElement> sz) {
        GeneralPath path = new GeneralPath();

        IntStream.range(0, sz.size()).filter(i -> sz.get(i).selected).forEach(i -> {
            GeneralPath iPath = new GeneralPath(Board.bElements.get(i).path);
            iPath.transform(Board.bElements.get(i).Tx);
            path.append(iPath, true);
        });

        return path.getBounds();
    }

    public static void selectByBoundingBox(List<BElement> sz, Rectangle rect) {
        for (int i = 1; i < sz.size(); ++i) {
            BElement cur = Board.bElements.get(i);
            GeneralPath path = new GeneralPath(cur.path);
            path.transform(cur.Tx);
            cur.selected = rect.contains(path.getBounds());
        }
    }

    public static BElement createText(String string, Font font, boolean option) {
        BufferedImage image = new BufferedImage(10, 10, 2);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics(font);
        int g = fm.getHeight();
        LineMetrics line = fm.getLineMetrics(string, g2d);
        String[] lines = string.split("\n");

        int size = 0;
        for (String s : lines) {
            size = Math.max(size, fm.stringWidth(s));
        }

        BufferedImage image2 = new BufferedImage(size, (int) (line.getAscent() + line.getDescent()) * lines.length, 2);
        Graphics2D g2d2 = (Graphics2D) image2.getGraphics();
        g2d2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d2.setBackground(Color.WHITE);
        g2d2.setFont(font);
        g2d2.clearRect(0, 0, image2.getWidth(), image2.getHeight());
        g2d2.setColor(Color.BLACK);

        for (int i = 0; i < lines.length; ++i) {
            g2d2.drawString(lines[i], 0.0F, line.getAscent() + (line.getAscent() + (float) (g + Board.bHeight)) * (float) i);
        }

        if (!option) {
            return create(1, image2);
        }

        GeneralPath lj = to_ls(image2);
        BElement res = create(0, null);
        res.path = new GeneralPath(lj);
        return res;
    }

    public static BElement createTextNum(String string, Font font, int gao, boolean option) {
        BufferedImage bimg = new BufferedImage(10, 10, 2);
        Graphics2D g2d = (Graphics2D) bimg.getGraphics();
        g2d.setFont(font);
        int k = g2d.getFontMetrics().stringWidth("信");
        int g = g2d.getFontMetrics().getHeight();
        FontMetrics fm = g2d.getFontMetrics(font);
        LineMetrics line = fm.getLineMetrics(string, g2d);

        BufferedImage bimg2 = new BufferedImage(k, string.length() * (g + gao), 2);
        Graphics2D g2d2 = (Graphics2D) bimg2.getGraphics();
        g2d2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d2.setBackground(Color.WHITE);
        g2d2.setFont(font);
        g2d2.clearRect(0, 0, bimg2.getWidth(), bimg2.getHeight());
        g2d2.setColor(Color.BLACK);

        for (int i = 0; i < string.length(); ++i) {
            String tempStr = string.substring(i, i + 1);
            g2d2.drawString(tempStr, 0.0F, line.getAscent() + (float) ((g + gao) * i));
        }

        if (!option) {
            return create(1, bimg2);
        } else {
            GeneralPath lj = to_ls(bimg2);
            BElement fh = create(0, null);
            fh.path = new GeneralPath(lj);
            return fh;
        }
    }

    public static BElement create(int type, BufferedImage image) {
        final BElement e0 = Board.bElements.get(0);
        BElement ele = new BElement();
        GeneralPath path = new GeneralPath(e0.path);
        path.transform(e0.Tx);
        Rectangle rect = path.getBounds();

        ele.Tx.translate(rect.x, rect.y);
        AffineTransform sf = AffineTransform.getScaleInstance(Board.quan_scale, Board.quan_scale);
        ele.Tx.concatenate(sf);

        switch (type) {
            case 0 -> {
                ele.type = 0;
                ele.path.moveTo(0.0F, 0.0F);
                ele.path.lineTo(400.0F, 0.0F);
                ele.path.lineTo(400.0F, 400.0F);
                ele.path.lineTo(0.0F, 400.0F);
                ele.path.closePath();
            }
            case 1 -> {
                double bi;
                if (image.getWidth() > image.getHeight()) {
                    if (image.getWidth() > 1600) {
                        bi = 1600.0D / (double) image.getWidth();
                        image = BImage.zoomImage(image, bi);
                    }
                } else if (image.getHeight() > 1600) {
                    bi = 1600.0D / (double) image.getHeight();
                    image = BImage.zoomImage(image, bi);
                }
                ele.bitMapImg2 = image;
                ele.type = 1;
                ele.process_code = 1;
                ele.bitMapImg = BImage.greyScale(image);
                ele.bitMapImg = BImage.blackAndWhite(ele.bitMapImg, 128);
                ele.path.moveTo(0.0F, 0.0F);
                ele.path.lineTo((float) image.getWidth(), 0.0F);
                ele.path.lineTo((float) image.getWidth(), (float) image.getHeight());
                ele.path.lineTo(0.0F, (float) image.getHeight());
                ele.path.closePath();
            }
            case 2 -> {
                ele.type = 0;
                Float d = new Float(1.0F, 1.0F, 400.0F, 400.0F);
                ele.path.append(d, false);
            }
            case 3 -> {
                ele.type = 0;
                ele.path.moveTo(197.0F, 102.0F);
                ele.path.lineTo(212.0F, 69.0F);
                ele.path.lineTo(224.0F, 48.0F);
                ele.path.lineTo(242.0F, 27.0F);
                ele.path.lineTo(266.0F, 10.0F);
                ele.path.lineTo(304.0F, 0.0F);
                ele.path.lineTo(343.0F, 10.0F);
                ele.path.lineTo(363.0F, 27.0F);
                ele.path.lineTo(378.0F, 48.0F);
                ele.path.lineTo(387.0F, 69.0F);
                ele.path.lineTo(393.0F, 102.0F);
                ele.path.lineTo(390.0F, 150.0F);
                ele.path.lineTo(372.0F, 208.0F);
                ele.path.lineTo(343.0F, 264.0F);
                ele.path.lineTo(295.0F, 322.0F);
                ele.path.lineTo(197.0F, 394.0F);
                ele.path.lineTo(98.0F, 322.0F);
                ele.path.lineTo(50.0F, 264.0F);
                ele.path.lineTo(20.0F, 208.0F);
                ele.path.lineTo(3.0F, 150.0F);
                ele.path.lineTo(0.0F, 102.0F);
                ele.path.lineTo(6.0F, 69.0F);
                ele.path.lineTo(15.0F, 48.0F);
                ele.path.lineTo(29.0F, 27.0F);
                ele.path.lineTo(50.0F, 10.0F);
                ele.path.lineTo(88.0F, 0.0F);
                ele.path.lineTo(128.0F, 10.0F);
                ele.path.lineTo(151.0F, 27.0F);
                ele.path.lineTo(170.0F, 48.0F);
                ele.path.lineTo(183.0F, 69.0F);
                ele.path.closePath();
            }
            case 4 -> {
                ele.type = 0;
                ele.path.moveTo(121.0F, 0.0F);
                ele.path.lineTo(149.0F, 93.0F);
                ele.path.lineTo(241.0F, 94.0F);
                ele.path.lineTo(169.0F, 149.0F);
                ele.path.lineTo(196.0F, 241.0F);
                ele.path.lineTo(122.0F, 188.0F);
                ele.path.lineTo(46.0F, 241.0F);
                ele.path.lineTo(72.0F, 149.0F);
                ele.path.lineTo(0.0F, 94.0F);
                ele.path.lineTo(92.0F, 93.0F);
                ele.path.closePath();
            }
        }

        return ele;
    }

    public static void getRectangle(List<BElement> sz) {
        int z = 0;
        int d = 0;
        int y = 0;
        int x = 0;

        for (int i = 1; i < Board.bElements.size(); ++i) {
            GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(i)).path);
            lu_jing2.transform((Board.bElements.get(i)).Tx);
            Rectangle jx = lu_jing2.getBounds();
            if (i == 1) {
                z = jx.x;
                d = jx.y;
                y = jx.x + jx.width;
                x = jx.y + jx.height;
            } else {
                if (jx.x < z) {
                    z = jx.x;
                }

                if (jx.y < d) {
                    d = jx.y;
                }

                if (jx.x + jx.width > y) {
                    y = jx.x + jx.width;
                }

                if (jx.y + jx.height > x) {
                    x = jx.y + jx.height;
                }
            }
        }

        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing2.transform((Board.bElements.get(0)).Tx);
        Rectangle jx2 = lu_jing2.getBounds();
        jx2.createIntersection(new Rectangle(z, d, y - z, x - d));
        bounds = (new Rectangle(z, d, y - z, x - d)).createIntersection(jx2).getBounds();
        if (bounds.width > 0 && bounds.height > 0) {
            bounds = (new Rectangle(z, d, y - z, x - d)).createIntersection(jx2).getBounds();
        } else {
            bounds = new Rectangle();
        }

        GeneralPath lu_jing3;
        Rectangle jx;
        int i;
        for (i = 1; i < Board.bElements.size(); ++i) {
            if ((Board.bElements.get(i)).type == 0) {
                lu_jing3 = new GeneralPath((Board.bElements.get(i)).path);
                lu_jing3.transform((Board.bElements.get(i)).Tx);
                jx = lu_jing3.getBounds();
                if (i == 1) {
                    z = jx.x;
                    d = jx.y;
                    y = jx.x + jx.width;
                    x = jx.y + jx.height;
                } else {
                    if (jx.x < z) {
                        z = jx.x;
                    }

                    if (jx.y < d) {
                        d = jx.y;
                    }

                    if (jx.x + jx.width > y) {
                        y = jx.x + jx.width;
                    }

                    if (jx.y + jx.height > x) {
                        x = jx.y + jx.height;
                    }
                }
            }
        }

        vector = new Rectangle(z, d, y - z, x - d);

        for (i = 1; i < Board.bElements.size(); ++i) {
            if ((Board.bElements.get(i)).type == 1) {
                lu_jing3 = new GeneralPath((Board.bElements.get(i)).path);
                lu_jing3.transform((Board.bElements.get(i)).Tx);
                jx = lu_jing3.getBounds();
                if (i == 1) {
                    z = jx.x;
                    d = jx.y;
                    y = jx.x + jx.width;
                    x = jx.y + jx.height;
                } else {
                    if (jx.x < z) {
                        z = jx.x;
                    }

                    if (jx.y < d) {
                        d = jx.y;
                    }

                    if (jx.x + jx.width > y) {
                        y = jx.x + jx.width;
                    }

                    if (jx.y + jx.height > x) {
                        x = jx.y + jx.height;
                    }
                }
            }
        }
    }

    public static void backup() {
        GeneralPath path = new GeneralPath(Board.getFirstBE().path);
        path.transform(Board.getFirstBE().Tx);
        Rectangle re = path.getBounds();
        Board.backup(re.x, re.y);

        for (int j = 0; j < 2; ++j) {
            GeneralPath p = new GeneralPath(Board.getFirstBE().path);
            p.transform(Board.getFirstBE().Tx);
            Rectangle r = p.getBounds();
            AffineTransform instance = AffineTransform.getTranslateInstance(-r.x, -r.y);
            Board.quan_x = 0;
            Board.quan_y = 0;

            AffineTransform transform;
            for (BElement e : Board.bElements) {
                transform = new AffineTransform(instance);
                transform.concatenate(e.Tx);
                e.Tx = transform;
            }
            instance = AffineTransform.getScaleInstance(1.0D / Board.quan_scale, 1.0D / Board.quan_scale);
            Board.quan_scale = 1.0D;
            for (BElement e : Board.bElements) {
                transform = new AffineTransform(instance);
                transform.concatenate(e.Tx);
                e.Tx = transform;
            }
        }

    }

    public static void restore() {
        Board.restore();
        AffineTransform instance = AffineTransform.getScaleInstance(Board.quan_scale, Board.quan_scale);
        AffineTransform transform;
        for (BElement e : Board.bElements) {
            transform = new AffineTransform(instance);
            transform.concatenate(e.Tx);
            e.Tx = transform;
        }
        instance = AffineTransform.getTranslateInstance(Board.quan_x, Board.quan_y);
        for (BElement e : Board.bElements) {
            transform = new AffineTransform(instance);
            transform.concatenate(e.Tx);
            e.Tx = transform;
        }
    }

    public static BufferedImage getImage(List<BElement> sz) {
        BufferedImage image = new BufferedImage((int) ((double) Board.bWidth / Board.resolution) - 2, (int) ((double) Board.bHeight / Board.resolution) - 2, 2);
        Graphics2D g2D = image.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, image.getWidth(), image.getHeight());
        boolean existed = false;

        for (int i = 1; i < Board.bElements.size(); ++i) {
            BElement e = Board.bElements.get(i);
            if (e.type == 1 && e.process_code != 3) {
                GeneralPath newPath = new GeneralPath(e.path);
                newPath.transform(e.Tx);
                Rectangle rect = newPath.getBounds();
                g2D.drawImage(e.getImage(), rect.x, rect.y, null);
                existed = true;
            }
        }

        if (!existed) return null;

        BufferedImage image2 = new BufferedImage((int) ((double) Board.bWidth / Board.resolution), (int) ((double) Board.bHeight / Board.resolution), 2);
        Graphics2D g2D2 = image2.createGraphics();
        g2D2.setBackground(Color.WHITE);
        int w = image2.getWidth();
        int h = image2.getHeight();
        g2D2.clearRect(0, 0, w, h);
        g2D2.drawImage(image, 1, 1, null);
        getRectangle(Board.bElements);
        Rectangle jx = new Rectangle(0, 0, w, h);
        if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
            return null;
        } else if (!jx.contains(bounds.x, bounds.y) && !jx.contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
            return null;
        } else {
            Rectangle rect2 = jx.createIntersection(bounds).getBounds();
            if (rect2.x + rect2.width + 5 >= image2.getWidth()) {
                w = rect2.width;
            } else {
                w = rect2.width + 5;
            }

            if (rect2.y + rect2.height + 5 >= image2.getHeight()) {
                h = rect2.height;
            } else {
                h = rect2.height + 5;
            }

            return image2.getSubimage(rect2.x, rect2.y, w, h);
        }
    }

    public static BufferedImage getImage_sl(List<BElement> sz) {
        BufferedImage fh = new BufferedImage((int) ((double) Board.bWidth / Board.resolution), (int) ((double) Board.bHeight / Board.resolution), 2);
        Graphics2D g2D = fh.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, fh.getWidth(), fh.getHeight());
        boolean you = false;

        for (int i = 1; i < Board.bElements.size(); ++i) {
            GeneralPath lu_jing4 = new GeneralPath((Board.bElements.get(i)).path);
            lu_jing4.transform((Board.bElements.get(i)).Tx);
            if ((Board.bElements.get(i)).type == 0) {
                g2D.setColor(Color.BLACK);
                g2D.draw(lu_jing4);
                you = true;
            }

            if ((Board.bElements.get(i)).type == 1 && (Board.bElements.get(i)).process_code == 3) {
                GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(i)).path);
                lu_jing2.transform((Board.bElements.get(i)).Tx);
                Rectangle jx = lu_jing2.getBounds();
                g2D.setColor(Color.BLACK);
                g2D.drawImage((Board.bElements.get(i)).getImage(), jx.x, jx.y, null);
                you = true;
            }
        }

        if (you) {
            try {
                BufferedImage fh2 = new BufferedImage((int) ((double) Board.bWidth / Board.resolution) + 4, (int) ((double) Board.bHeight / Board.resolution) + 4, 2);
                Graphics2D g2D2 = fh2.createGraphics();
                g2D2.setBackground(Color.WHITE);
                int w = fh2.getWidth();
                int h = fh2.getHeight();
                g2D2.clearRect(0, 0, w, h);
                g2D2.drawImage(fh, 2, 2, null);
                getRectangle(Board.bElements);
                Rectangle jx = new Rectangle(0, 0, w, h);
                new Rectangle();
                if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
                    return null;
                } else if (!jx.contains(bounds.x, bounds.y) && !jx.contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
                    return null;
                } else {
                    Rectangle jx2 = jx.createIntersection(bounds).getBounds();
                    if (jx2.x + jx2.width + 5 >= fh2.getWidth()) {
                        w = jx2.width;
                    } else {
                        w = jx2.width + 5;
                    }

                    if (jx2.y + jx2.height + 5 >= fh2.getHeight()) {
                        h = jx2.height;
                    } else {
                        h = jx2.height + 5;
                    }

                    try {
                        return fh2.getSubimage(jx2.x, jx2.y, w, h);
                    } catch (Exception var15) {
                        JOptionPane.showMessageDialog(null, var15);
                        return null;
                    }
                }
            } catch (Exception var16) {
                JOptionPane.showMessageDialog(null, var16);
                return null;
            }
        } else {
            return null;
        }
    }

    public static BufferedImage getImage_sl_tc(List<BElement> sz) {
        BufferedImage fh = new BufferedImage((int) ((double) Board.bWidth / Board.resolution), (int) ((double) Board.bHeight / Board.resolution), 2);
        Graphics2D g2D = fh.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, fh.getWidth(), fh.getHeight());
        boolean you = false;

        for (int i = 1; i < Board.bElements.size(); ++i) {
            GeneralPath lu_jing4 = new GeneralPath((Board.bElements.get(i)).path);
            lu_jing4.transform((Board.bElements.get(i)).Tx);
            if ((Board.bElements.get(i)).type == 0 && (Board.bElements.get(i)).filled) {
                g2D.setColor(Color.BLACK);
                lu_jing4.setWindingRule(0);
                g2D.fill(lu_jing4);
                you = true;
            }
        }

        if (you) {
            BufferedImage fh2 = new BufferedImage((int) ((double) Board.bWidth / Board.resolution) + 4, (int) ((double) Board.bHeight / Board.resolution) + 4, 2);
            Graphics2D g2D2 = fh2.createGraphics();
            g2D2.setBackground(Color.WHITE);
            int w = fh2.getWidth();
            int h = fh2.getHeight();
            g2D2.clearRect(0, 0, w, h);
            g2D2.drawImage(fh, 2, 2, null);
            getRectangle(Board.bElements);
            Rectangle jx = new Rectangle(0, 0, w, h);
            new Rectangle();
            if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
                return null;
            } else if (!jx.contains(bounds.x, bounds.y) && !jx.contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
                return null;
            } else {
                Rectangle jx2 = jx.createIntersection(bounds).getBounds();
                if (jx2.x + jx2.width + 5 >= fh2.getWidth()) {
                    w = jx2.width;
                } else {
                    w = jx2.width + 5;
                }

                if (jx2.y + jx2.height + 5 >= fh2.getHeight()) {
                    h = jx2.height;
                } else {
                    h = jx2.height + 5;
                }

                return fh2.getSubimage(jx2.x, jx2.y, w, h);
            }
        } else {
            return null;
        }
    }

    static BPoint qu_xiao(BPoint p1, BPoint p2, BPoint p3) {
        int cmp1 = Math.min(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
        int cmp2 = Math.min(Math.abs(p1.x - p3.x), Math.abs(p1.y - p3.y));
        return cmp1 < cmp2 ? p2 : p3;
    }

    static BPoint qu_jindian(BPoint d, BufferedImage bb) {
        BPoint fh = new BPoint(50000, 0);
        List<BPoint> zd = new ArrayList<>();

        for (int c = 1; c < maxSideLen; ++c) {
            int ls = d.y - c;

            int b;
            for (b = d.x - c; b < d.x + c; ++b) {
                if (b > 0 && b < imgW && ls > 0 && ls < imgH && (new Color(bb.getRGB(b, ls))).getRed() == 0) {
                    zd.add(new BPoint(b, ls));
                }
            }

            ls = d.x + c;

            for (b = d.y - c; b < d.y + c; ++b) {
                if (b > 0 && b < imgH && ls > 0 && ls < imgW && (new Color(bb.getRGB(ls, b))).getRed() == 0) {
                    zd.add(new BPoint(ls, b));
                }
            }

            ls = d.y + c;

            for (b = d.x + c; b > d.x - c; --b) {
                if (b > 0 && b < imgW && ls > 0 && ls < imgH && (new Color(bb.getRGB(b, ls))).getRed() == 0) {
                    zd.add(new BPoint(b, ls));
                }
            }

            ls = d.x - c;

            for (b = d.y + c; b > d.y - c; --b) {
                if (b > 0 && b < imgH && ls > 0 && ls < imgW && (new Color(bb.getRGB(ls, b))).getRed() == 0) {
                    zd.add(new BPoint(ls, b));
                }
            }

            BPoint fh2 = new BPoint(0, 0);
            if (zd.size() > 0) {
                for (int i = 0; i < zd.size(); ++i) {
                    if (i == 0) {
                        fh2 = zd.get(i);
                    } else {
                        fh2 = qu_xiao(d, fh2, zd.get(i));
                    }
                }

                return fh2;
            }
        }

        return fh;
    }

    static List<BPoint> sort(BufferedImage image) {
        BufferedImage buff = image.getSubimage(0, 0, image.getWidth(), image.getHeight());
        List<BPoint> ret = new ArrayList<>();
        imgW = buff.getWidth();
        imgH = buff.getHeight();
        maxSideLen = Math.max(buff.getWidth(), buff.getHeight());
        for (int i = 0; i < bPoints.size(); ++i) {
            if (i == 0) {
                ret.add(bPoints.get(i));
                ret.add(new BPoint(30000, 30000));
                buff.setRGB(bPoints.get(i).x, bPoints.get(i).y, Color.WHITE.getRGB());
            } else {
                BPoint p2;
                if (ret.get(ret.size() - 1).x != 30000 && ret.get(ret.size() - 1).x != 50000) {
                    p2 = ret.get(ret.size() - 1);
                } else {
                    p2 = ret.get(ret.size() - 2);
                }

                BPoint p1 = qu_jindian(p2, buff);
                if (p1.x == 50000) {
                    break;
                }

                if (!connectPoints(p1, p2)) {
                    ret.add(new BPoint(50000, 50000));
                    ret.add(p1);
                    ret.add(new BPoint(30000, 30000));
                } else {
                    ret.add(p1);
                }

                buff.setRGB(p1.x, p1.y, Color.WHITE.getRGB());
            }
        }
        ret.add(new BPoint(50000, 50000));
        return ret;
    }

    static List<BPoint> sort2(BufferedImage image) {
        BufferedImage buff = image.getSubimage(0, 0, image.getWidth(), image.getHeight());
        List<BPoint> ret = new ArrayList<>();
        imgW = buff.getWidth();
        imgH = buff.getHeight();
        maxSideLen = Math.max(buff.getWidth(), buff.getHeight());

        for (int i = 0; i < bPoints.size(); ++i) {
            if (i == 0) {
                ret.add(bPoints.get(i));
                buff.setRGB(bPoints.get(i).x, bPoints.get(i).y, Color.WHITE.getRGB());
            } else {
                if (ret.get(ret.size() - 1).x == 242 && ret.get(ret.size() - 1).y == 87) {
                    ret.get(ret.size() - 1).x = 242;
                }

                BPoint d = qu_jindian(ret.get(ret.size() - 1), buff);
                if (d.x == 50000) {
                    break;
                }

                ret.add(d);
                buff.setRGB(d.x, d.y, Color.WHITE.getRGB());
            }
        }

        return ret;
    }

    static List<BPoint> getFill(BufferedImage img, int gap) {
        List<BPoint> fh = new ArrayList<>();
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);

        for (int i = 1; i < height; i += gap) {
            for (int j = 1; j < width; ++j) {
                if ((new Color(pixels[width * i + j])).getRed() == 0) {
                    fh.add(new BPoint(j, i));
                }
            }
        }

        return fh;
    }

    static List<BPoint> getPoints(BufferedImage image) {
        List<BPoint> ret = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        for (int i = 1; i < height; ++i) {
            for (int j = 1; j < width; ++j) {
                if (new Color(pixels[width * i + j]).getRed() == 0) {
                    ret.add(new BPoint(j, i));
                }
            }
        }
        return ret;
    }

    static int getDirection(BPoint d1, BPoint d2) {
        if (d2.x - d1.x == 1 && d2.y - d1.y == 1) {
            return 4;
        } else if (d2.x - d1.x == -1 && d2.y - d1.y == -1) {
            return 8;
        } else if (d2.x - d1.x == 1 && d2.y - d1.y == -1) {
            return 2;
        } else if (d2.x - d1.x == -1 && d2.y - d1.y == 1) {
            return 6;
        } else if (d2.x - d1.x == -1 && d2.y - d1.y == 0) {
            return 7;
        } else if (d2.x - d1.x == 1 && d2.y - d1.y == 0) {
            return 3;
        } else if (d2.x - d1.x == 0 && d2.y - d1.y == -1) {
            return 1;
        } else if (d2.x - d1.x == 0 && d2.y - d1.y == 1) {
            return 5;
        } else {
            return 9;
        }
    }

    public static List<BPoint> you_hua(List<BPoint> dd) {
        List<BPoint> fh = new ArrayList<>();
        int fx = 0;

        for (int i = 0; i < dd.size(); ++i) {
            if (i == 0) {
                fh.add(dd.get(i));
            } else if (i == 1) {
                fh.add(dd.get(i));
                fx = getDirection(fh.get(fh.size() - 2), fh.get(fh.size() - 2));
            } else {
                int fx2 = getDirection(fh.get(fh.size() - 1), dd.get(i));
                if (fx == fx2 && fx != 9) {
                    fh.remove(fh.size() - 1);
                    fh.add(dd.get(i));
                } else {
                    fh.add(dd.get(i));
                    fx = fx2;
                }
            }
        }

        return fh;
    }

    static boolean connectPoints(BPoint a, BPoint b) {
        return Math.abs(a.x - b.x) <= 2 && Math.abs(a.y - b.y) <= 2;
    }

    public static double getDistance(BPoint p1, BPoint p2) {
        return getDistance(p1.x, p2.x, p1.y, p2.y);
    }

    public static double getDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static double getHeight(double a, double b, double c) {
        double p = (a + b + c) / 2.0D;
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        return 2.0D * s / b;
    }

    public static double getMaxHeight(List<BPoint> dd, int i1, int i2) {
        double max = 0.0D;
        for (int i = 0; i < i2 - i1 - 1; ++i) {
            BPoint p1 = dd.get(i1), p2 = dd.get(i2), pi = dd.get(i + i1);
            if (pi.x != 30000 && pi.x != 50000) {
                max = Math.max(
                        max,
                        getHeight(
                                getDistance(p1, pi),
                                getDistance(p1, p2),
                                getDistance(pi, p2)
                        )
                );
            }
        }
        return max;
    }

    public static List<BPoint> optimize(List<BPoint> src) {
        List<BPoint> ret = new ArrayList<>();
        int yi = 0;

        for (int i = 0; i < src.size(); ++i) {
            if (src.get(i).x == 30000) {
                ret.add(src.get(i - 1));
                ret.add(src.get(i));
                yi = i - 1;
            } else if (src.get(i).x == 50000) {
                ret.add(src.get(i - 1));
                ret.add(src.get(i));
                yi = i + 1;
            } else if (i != 0) {
                if (i - yi > 1) {
                    double da = getMaxHeight(src, yi, i);
                    if (da > 0.7D) {
                        ret.add(src.get(i - 1));
                        yi = i - 1;
                    }
                }
            }
        }

        return ret;
    }

    public static GeneralPath to_ls(BufferedImage bb) {
        List<BPoint> fh;
        GeneralPath lj = new GeneralPath();
        bb = BImage.qu_lunkuo(bb, 128);
        bPoints = getPoints(bb);
        fh = sort(bb);
        fh = optimize(fh);

        new BPoint(0, 0);
        BPoint qi = new BPoint(0, 0);
        boolean kai_jg = false;

        for (int i = 0; i < fh.size(); ++i) {
            BPoint ls = fh.get(i);
            if (i == 0) {
                lj.moveTo((float) ls.x, (float) ls.y);
                new BPoint(ls.x, ls.y);
            } else if (fh.get(i).x == 30000) {
                kai_jg = true;
                qi = new BPoint(fh.get(i - 1).x, fh.get(i - 1).y);
            } else if (fh.get(i).x == 50000) {
                kai_jg = false;
                if (connectPoints(qi, fh.get(i - 1))) {
                    lj.closePath();
                }
            } else if (kai_jg) {
                lj.lineTo((float) ls.x, (float) ls.y);
                new BPoint(ls.x, ls.y);
            } else {
                lj.moveTo((float) ls.x, (float) ls.y);
                new BPoint(ls.x, ls.y);
            }
        }

        if (connectPoints(qi, fh.get(fh.size() - 1))) {
            lj.closePath();
        }

        return lj;
    }

    public static List<BPoint> getPoints(List<BElement> sz) {
        List<BPoint> fh = null;
        BufferedImage bb = getImage_sl(Board.bElements);
        if (bb != null) {
            bPoints = getPoints(bb);
            fh = sort2(bb);
        }

        BufferedImage bb2 = getImage_sl_tc(Board.bElements);
        if (bb2 != null) {
            fh.addAll(getFill(bb2, 1 + fillGap));
        }

        return fh;
    }

    void translate(double x, double y) {
        AffineTransform Tx2 = AffineTransform.getTranslateInstance(x, y);
        Tx2.concatenate(this.Tx);
        this.Tx = Tx2;
    }

    void rotate(double angle, double x_anchor, double y_anchor) {
        AffineTransform Tx2 = AffineTransform.getRotateInstance(angle * 3.14D / 180.0D, x_anchor, y_anchor);
        Tx2.concatenate(this.Tx);
        this.Tx = Tx2;
    }

    void scale(double scale_x, double scale_y) {
        AffineTransform Tx2 = AffineTransform.getScaleInstance(scale_x, scale_y);
        Tx2.concatenate(this.Tx);
        this.Tx = Tx2;
    }

    public BufferedImage getImage() {
        GeneralPath lu_jing2 = new GeneralPath(this.path);
        lu_jing2.transform(this.Tx);
        Rectangle jx = lu_jing2.getBounds();
        System.out.println(jx);
        System.out.println(this.Tx);
        AffineTransform tx2 = new AffineTransform(this.Tx.getScaleX(), this.Tx.getShearY(), this.Tx.getShearX(), this.Tx.getScaleY(), this.Tx.getTranslateX() - (double) jx.x, this.Tx.getTranslateY() - (double) jx.y);
        System.out.println(tx2);
        BufferedImage fh = new BufferedImage(jx.width, jx.height, 2);
        Graphics2D g2D = fh.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, jx.width, jx.height);
        g2D.drawImage(this.bitMapImg2, tx2, null);
        switch (this.process_code) {
            case 1 -> {
                fh = BImage.greyScale(fh);
                fh = BImage.blackAndWhite(fh, (int) ((double) this.threshold * 2.56D));
                if (this.process_doInvert) {
                    fh = BImage.invertColor(fh);
                }
                if (this.process_doMirrorX) {
                    fh = BImage.mirror_x(fh);
                }
                if (this.process_doMirrorY) {
                    fh = BImage.mirror_y(fh);
                }
            }
            case 2 -> {
                fh = BImage.greyScale(fh);
                fh = BImage.convertGreyImgByFloyd(fh, (int) ((double) this.threshold * 2.56D));
                if (this.process_doInvert) {
                    fh = BImage.invertColor(fh);
                }
                if (this.process_doMirrorX) {
                    fh = BImage.mirror_x(fh);
                }
                if (this.process_doMirrorY) {
                    fh = BImage.mirror_y(fh);
                }
            }
            case 3 -> {
                fh = BImage.greyScale(fh);
                fh = BImage.blackAndWhite(fh, 128);
                fh = BImage.qu_lunkuo(fh, (int) ((double) this.threshold * 2.56D));
            }
            case 4 -> {
                fh = BImage.su_miao(fh);
                fh = BImage.blackAndWhite(fh, 50 + (int) ((double) this.threshold * 2.56D));
                if (this.process_doInvert) {
                    fh = BImage.invertColor(fh);
                }
                if (this.process_doMirrorX) {
                    fh = BImage.mirror_x(fh);
                }
                if (this.process_doMirrorY) {
                    fh = BImage.mirror_y(fh);
                }
            }
        }

        return fh;
    }

    public void process() {
        switch (this.process_code) {
            case 1 -> {
                this.bitMapImg = BImage.greyScale(this.bitMapImg2);
                this.bitMapImg = BImage.blackAndWhite(this.bitMapImg, (int) ((double) this.threshold * 2.56D));
                if (this.process_doInvert) {
                    this.bitMapImg = BImage.invertColor(this.bitMapImg);
                }
                if (this.process_doMirrorX) {
                    this.bitMapImg = BImage.mirror_x(this.bitMapImg);
                }
                if (this.process_doMirrorY) {
                    this.bitMapImg = BImage.mirror_y(this.bitMapImg);
                }
            }
            case 2 -> {
                this.bitMapImg = BImage.greyScale(this.bitMapImg2);
                this.bitMapImg = BImage.convertGreyImgByFloyd(this.bitMapImg, (int) ((double) this.threshold * 2.56D));
                if (this.process_doInvert) {
                    this.bitMapImg = BImage.invertColor(this.bitMapImg);
                }
                if (this.process_doMirrorX) {
                    this.bitMapImg = BImage.mirror_x(this.bitMapImg);
                }
                if (this.process_doMirrorY) {
                    this.bitMapImg = BImage.mirror_y(this.bitMapImg);
                }
            }
            case 3 -> this.bitMapImg = BImage.qu_lunkuo(this.bitMapImg2, (int) ((double) this.threshold * 2.56D));
            case 4 -> {
                this.bitMapImg = BImage.su_miao2(this.bitMapImg2);
                this.bitMapImg = BImage.blackAndWhite(this.bitMapImg, 50 + (int) ((double) this.threshold * 2.56D));
                if (this.process_doInvert) {
                    this.bitMapImg = BImage.invertColor(this.bitMapImg);
                }
                if (this.process_doMirrorX) {
                    this.bitMapImg = BImage.mirror_x(this.bitMapImg);
                }
                if (this.process_doMirrorY) {
                    this.bitMapImg = BImage.mirror_y(this.bitMapImg);
                }
            }
        }
    }
}
