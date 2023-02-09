package engraver.model;

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

public class BElement implements Serializable {
    public boolean filled = false;

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

    public static Rectangle getBounds(BElement e) {
        GeneralPath path = new GeneralPath(e.path);
        path.transform(e.Tx);
        return path.getBounds();
    }

    public static Rectangle getBounds(List<BElement> list) {
        GeneralPath path = new GeneralPath();
        for (var e : list) {
            if (e.selected) {
                GeneralPath p = new GeneralPath(e.path);
                p.transform(e.Tx);
                path.append(p, true);
            }
        }
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
            g2d2.drawString(lines[i], 0.0F, line.getAscent() + (line.getAscent() + (float) (g + Board.BOARD_HEIGHT)) * (float) i);
        }

        if (!option) {
            return create(1, image2);
        }

        GeneralPath lj = Board.to_ls(image2);
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
            GeneralPath lj = Board.to_ls(bimg2);
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
        AffineTransform sf = AffineTransform.getScaleInstance(Board.scale, Board.scale);
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
                ele.bitMapImg = BImage.toGreyScale(image);
                ele.bitMapImg = BImage.toBlackAndWhite(ele.bitMapImg, 128);
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

    public static List<BPoint> getFill(BufferedImage image, int gap) {
        List<BPoint> ret = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int i = 1; i < height; i += gap) {
            for (int j = 1; j < width; ++j) {
                if (BPoint.isPoint(pixels[width * i + j])) {
                    ret.add(new BPoint(j, i));
                }
            }
        }

        return ret;
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
                                BPoint.getDistance(p1, pi),
                                BPoint.getDistance(p1, p2),
                                BPoint.getDistance(pi, p2)
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

    public void translate(double x, double y) {
        this.Tx.concatenate(AffineTransform.getTranslateInstance(x, y));
    }

    public void rotateByRad(double rad, double x_anchor, double y_anchor) {
        this.Tx.concatenate(AffineTransform.getRotateInstance(rad, x_anchor, y_anchor));
    }

    public void scale(double scale_x, double scale_y) {
        this.Tx.concatenate(AffineTransform.getScaleInstance(scale_x, scale_y));
    }

    public BufferedImage toImage() {
        Rectangle r = getBounds(this);
        BufferedImage ret = new BufferedImage(r.width, r.height, 2);
        Graphics2D g2D = ret.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, r.width, r.height);
        g2D.drawImage(
                this.bitMapImg2,
                new AffineTransform(
                        this.Tx.getScaleX(),
                        this.Tx.getShearY(),
                        this.Tx.getShearX(),
                        this.Tx.getScaleY(),
                        this.Tx.getTranslateX() - (double) r.x,
                        this.Tx.getTranslateY() - (double) r.y
                ),
                null
        );

        return process(ret);
    }

    public void process() {
        this.bitMapImg = process(this.bitMapImg2);
    }

    public BufferedImage process(BufferedImage image) {
        BufferedImage ret;
        int val = (int) (this.threshold * 2.56D);
        switch (this.process_code) {
            case 1:
                ret = BImage.toGreyScale(image);
                ret = BImage.toBlackAndWhite(ret, val);
                break;
            case 2:
                ret = BImage.toGreyScale(image);
                ret = BImage.convertGreyImgByFloyd(ret, val);
                break;
            case 3:
                ret = BImage.getEdges(image, val);
                return ret;
            case 4:
                ret = BImage.sketch(image);
                ret = BImage.toBlackAndWhite(ret, 50 + val);
                break;
            default:
                return image;
        }
        if (this.process_doInvert)
            ret = BImage.toInverted(ret);
        if (this.process_doMirrorX)
            ret = BImage.mirror_x(ret);
        if (this.process_doMirrorY)
            ret = BImage.mirror_y(ret);

        return ret;
    }
}
