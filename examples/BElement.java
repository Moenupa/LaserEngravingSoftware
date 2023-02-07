package examples;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Float;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BElement implements Serializable {
    // public static final int lei_xing_tupian = 1;
    // public static final int lei_xing_lujing = 0;
    public static int tian_chong_md = 5;
    public static Rectangle zui_zhong_wjx = new Rectangle();
    public static Rectangle wei_tu_wjx = new Rectangle();
    public static Rectangle shi_liang_wjx = new Rectangle();
    public static Rectangle shu_biao = new Rectangle();
    public static boolean tuo = false;
    // public static int dk_gonglv = 100;
    // public static int dk_shendu = 10;
    // public static int dk_gonglv_sl = 100;
    // public static int dk_shendu_sl = 10;
    // public static int dk_cishu = 1;
    public static List<BPoint> bPoints = null;
    static int tu_kuan = 0;
    static int tu_gao = 0;
    static int da;
    public int type = 0;
    public boolean fill = false;
    public boolean selected = false;
    public GeneralPath path = new GeneralPath();
    public transient BufferedImage wei_tu = null;
    public transient BufferedImage wei_tu_yuan = null;
    public int[] wei_tu_ = null;
    public int[] wei_tu_yuan_ = null;
    public int wt_w = 0;
    public int wt_g = 0;
    public int wty_w = 0;
    public int wty_g = 0;
    public int chuli_fs = 1;
    public boolean chuli_fan = false;
    public boolean chuli_jxx = false;
    public boolean chuli_jxy = false;
    public int yu_zhi = 50;
    public AffineTransform Tx = new AffineTransform();

    public static List<BElement> copyResult(List<BElement> ty) {
        List<BElement> res = new ArrayList<>();

        for (BElement bElement : ty) {
            res.add(copy(bElement));
        }

        return res;
    }

    public static BElement copy(BElement ty) {
        BElement res = chuang_jian(ty.type, ty.wei_tu);
        res.Tx = new AffineTransform(ty.Tx);
        res.selected = ty.selected;
        res.path = new GeneralPath(ty.path);
        res.chuli_fan = ty.chuli_fan;
        res.chuli_fs = ty.chuli_fs;
        res.chuli_jxx = ty.chuli_jxx;
        res.chuli_jxy = ty.chuli_jxy;
        res.fill = ty.fill;
        res.yu_zhi = ty.yu_zhi;
        return res;
    }

    static void center(List<BElement> sz) {
        Rectangle rect = qu_jv_xing(Board.bElements);
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

    public static Rectangle qu_jv_xing(BElement ty) {
        GeneralPath newPath = new GeneralPath(ty.path);
        newPath.transform(ty.Tx);
        return newPath.getBounds();
    }

    public static Rectangle qu_jv_xing(List<BElement> sz) {
        GeneralPath newPath = new GeneralPath();

        for (int i = 0; i < sz.size(); ++i) {
            if (sz.get(i).selected) {
                GeneralPath iPath = new GeneralPath((Board.bElements.get(i)).path);
                iPath.transform((Board.bElements.get(i)).Tx);
                newPath.append(iPath, true);
            }
        }

        return newPath.getBounds();
    }

    public static void select_byBoundingBox(List<BElement> sz, Rectangle rect) {
        for (int i = 1; i < sz.size(); ++i) {
            BElement cur = Board.bElements.get(i);
            GeneralPath lu_jing3 = new GeneralPath(cur.path);
            lu_jing3.transform(cur.Tx);
            cur.selected = rect.contains(lu_jing3.getBounds());
        }
    }

    public static BElement chuang_jian_wen_zi(String ss, Font zt, boolean sl) {
        BufferedImage bimg = new BufferedImage(10, 10, 2);
        Graphics2D g2d = (Graphics2D) bimg.getGraphics();
        Font font = new Font(zt.getName(), zt.getStyle(), zt.getSize());
        int g = g2d.getFontMetrics().getHeight();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics(font);
        LineMetrics line = fm.getLineMetrics(ss, g2d);
        String[] sz = ss.split("\n");
        int da = 0;

        for (String s : sz) {
            da = Math.max(da, fm.stringWidth(s));
        }

        BufferedImage bimg2 = new BufferedImage(da, (int) (line.getAscent() + line.getDescent()) * sz.length, 2);
        Graphics2D g2d2 = (Graphics2D) bimg2.getGraphics();
        g2d2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d2.setBackground(Color.WHITE);
        g2d2.setFont(font);
        g2d2.clearRect(0, 0, bimg2.getWidth(), bimg2.getHeight());
        g2d2.setColor(Color.BLACK);

        for (int i = 0; i < sz.length; ++i) {
            g2d2.drawString(sz[i], 0.0F, line.getAscent() + (line.getAscent() + (float) (g + Board.bHeight)) * (float) i);
        }

        if (!sl) {
            return chuang_jian(1, bimg2);
        } else {
            GeneralPath lj = to_ls(bimg2);
            BElement fh = chuang_jian(0, null);
            fh.path = new GeneralPath(lj);
            return fh;
        }
    }

    public static BElement chuang_jian_wen_zi_shu(String ss, Font zt, int gao, boolean sl) {
        BufferedImage bimg = new BufferedImage(10, 10, 2);
        Graphics2D g2d = (Graphics2D) bimg.getGraphics();
        Font font = new Font(zt.getName(), zt.getStyle(), zt.getSize());
        g2d.setFont(font);
        int k = g2d.getFontMetrics().stringWidth("信");
        int g = g2d.getFontMetrics().getHeight();
        FontMetrics fm = g2d.getFontMetrics(font);
        LineMetrics line = fm.getLineMetrics(ss, g2d);
        BufferedImage bimg2 = new BufferedImage(k, ss.length() * (g + gao), 2);
        Graphics2D g2d2 = (Graphics2D) bimg2.getGraphics();
        g2d2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d2.setBackground(Color.WHITE);
        g2d2.setFont(font);
        g2d2.clearRect(0, 0, bimg2.getWidth(), bimg2.getHeight());
        g2d2.setColor(Color.BLACK);

        for (int i = 0; i < ss.length(); ++i) {
            String tempStr = ss.substring(i, i + 1);
            g2d2.drawString(tempStr, 0.0F, line.getAscent() + (float) ((g + gao) * i));
        }

        if (!sl) {
            return chuang_jian(1, bimg2);
        } else {
            GeneralPath lj = to_ls(bimg2);
            BElement fh = chuang_jian(0, null);
            fh.path = new GeneralPath(lj);
            return fh;
        }
    }

    public static BElement chuang_jian(int leixing, BufferedImage wt) {
        BElement ty = new BElement();
        GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing2.transform((Board.bElements.get(0)).Tx);
        Rectangle rect = lu_jing2.getBounds();
        ty.Tx.translate(rect.x, rect.y);
        AffineTransform sf = AffineTransform.getScaleInstance(Board.quan_beishu, Board.quan_beishu);
        ty.Tx.concatenate(sf);
        switch (leixing) {
            case 0 -> {
                ty.type = 0;
                ty.path.moveTo(0.0F, 0.0F);
                ty.path.lineTo(400.0F, 0.0F);
                ty.path.lineTo(400.0F, 400.0F);
                ty.path.lineTo(0.0F, 400.0F);
                ty.path.closePath();
            }
            case 1 -> {
                double bi;
                if (wt.getWidth() > wt.getHeight()) {
                    if (wt.getWidth() > 1600) {
                        bi = 1600.0D / (double) wt.getWidth();
                        wt = BImage.zoomImage(wt, bi);
                    }
                } else if (wt.getHeight() > 1600) {
                    bi = 1600.0D / (double) wt.getHeight();
                    wt = BImage.zoomImage(wt, bi);
                }
                ty.wei_tu_yuan = wt;
                ty.type = 1;
                ty.chuli_fs = 1;
                ty.wei_tu = BImage.greyScale(wt);
                ty.wei_tu = BImage.blackAndWhite(ty.wei_tu, 128);
                ty.path.moveTo(0.0F, 0.0F);
                ty.path.lineTo((float) wt.getWidth(), 0.0F);
                ty.path.lineTo((float) wt.getWidth(), (float) wt.getHeight());
                ty.path.lineTo(0.0F, (float) wt.getHeight());
                ty.path.closePath();
            }
            case 2 -> {
                ty.type = 0;
                Float d = new Float(1.0F, 1.0F, 400.0F, 400.0F);
                ty.path.append(d, false);
            }
            case 3 -> {
                ty.type = 0;
                ty.path.moveTo(197.0F, 102.0F);
                ty.path.lineTo(212.0F, 69.0F);
                ty.path.lineTo(224.0F, 48.0F);
                ty.path.lineTo(242.0F, 27.0F);
                ty.path.lineTo(266.0F, 10.0F);
                ty.path.lineTo(304.0F, 0.0F);
                ty.path.lineTo(343.0F, 10.0F);
                ty.path.lineTo(363.0F, 27.0F);
                ty.path.lineTo(378.0F, 48.0F);
                ty.path.lineTo(387.0F, 69.0F);
                ty.path.lineTo(393.0F, 102.0F);
                ty.path.lineTo(390.0F, 150.0F);
                ty.path.lineTo(372.0F, 208.0F);
                ty.path.lineTo(343.0F, 264.0F);
                ty.path.lineTo(295.0F, 322.0F);
                ty.path.lineTo(197.0F, 394.0F);
                ty.path.lineTo(98.0F, 322.0F);
                ty.path.lineTo(50.0F, 264.0F);
                ty.path.lineTo(20.0F, 208.0F);
                ty.path.lineTo(3.0F, 150.0F);
                ty.path.lineTo(0.0F, 102.0F);
                ty.path.lineTo(6.0F, 69.0F);
                ty.path.lineTo(15.0F, 48.0F);
                ty.path.lineTo(29.0F, 27.0F);
                ty.path.lineTo(50.0F, 10.0F);
                ty.path.lineTo(88.0F, 0.0F);
                ty.path.lineTo(128.0F, 10.0F);
                ty.path.lineTo(151.0F, 27.0F);
                ty.path.lineTo(170.0F, 48.0F);
                ty.path.lineTo(183.0F, 69.0F);
                ty.path.closePath();
            }
            case 4 -> {
                ty.type = 0;
                ty.path.moveTo(121.0F, 0.0F);
                ty.path.lineTo(149.0F, 93.0F);
                ty.path.lineTo(241.0F, 94.0F);
                ty.path.lineTo(169.0F, 149.0F);
                ty.path.lineTo(196.0F, 241.0F);
                ty.path.lineTo(122.0F, 188.0F);
                ty.path.lineTo(46.0F, 241.0F);
                ty.path.lineTo(72.0F, 149.0F);
                ty.path.lineTo(0.0F, 94.0F);
                ty.path.lineTo(92.0F, 93.0F);
                ty.path.closePath();
            }
        }

        return ty;
    }

    public static void qu_jvxing(List<BElement> sz) {
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
        zui_zhong_wjx = (new Rectangle(z, d, y - z, x - d)).createIntersection(jx2).getBounds();
        if (zui_zhong_wjx.width > 0 && zui_zhong_wjx.height > 0) {
            zui_zhong_wjx = (new Rectangle(z, d, y - z, x - d)).createIntersection(jx2).getBounds();
        } else {
            zui_zhong_wjx = new Rectangle();
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

        shi_liang_wjx = new Rectangle(z, d, y - z, x - d);

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

        wei_tu_wjx = new Rectangle(z, d, y - z, x - d);
    }

    public static void qu_jvxing3(List<BElement> sz) {
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
        if (jx2.x > z) {
            z = jx2.x;
        }

        if (jx2.y > d) {
            d = jx2.y;
        }

        if (jx2.x + jx2.width < y) {
            y = jx2.x + jx2.width;
        }

        if (jx2.y + jx2.height < x) {
            x = jx2.y + jx2.height;
        }

        zui_zhong_wjx = new Rectangle(z, d, y - z, x - d);

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

        shi_liang_wjx = new Rectangle(z, d, y - z, x - d);

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

        wei_tu_wjx = new Rectangle(z, d, y - z, x - d);
    }

    public static void hui_fu() {
        GeneralPath lu_jing3 = new GeneralPath((Board.bElements.get(0)).path);
        lu_jing3.transform((Board.bElements.get(0)).Tx);
        Rectangle rect3 = lu_jing3.getBounds();
        Board.quan_x2 = rect3.x;
        Board.quan_y2 = rect3.y;
        Board.quan_beishu2 = Board.quan_beishu;

        for (int j = 0; j < 2; ++j) {
            GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(0)).path);
            lu_jing2.transform((Board.bElements.get(0)).Tx);
            Rectangle rect = lu_jing2.getBounds();
            AffineTransform sf = AffineTransform.getTranslateInstance(-rect.x, -rect.y);
            Board.quan_x = 0;
            Board.quan_y = 0;

            int i;
            AffineTransform fb;
            for (i = 0; i < Board.bElements.size(); ++i) {
                fb = new AffineTransform(sf);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }

            sf = AffineTransform.getScaleInstance(1.0D / Board.quan_beishu, 1.0D / Board.quan_beishu);
            Board.quan_beishu = 1.0D;

            for (i = 0; i < Board.bElements.size(); ++i) {
                fb = new AffineTransform(sf);
                fb.concatenate((Board.bElements.get(i)).Tx);
                (Board.bElements.get(i)).Tx = fb;
            }
        }

    }

    public static void hui_fu_xian_chang() {
        Board.quan_x = Board.quan_x2;
        Board.quan_y = Board.quan_y2;
        Board.quan_beishu = Board.quan_beishu2;
        AffineTransform sf = AffineTransform.getScaleInstance(Board.quan_beishu, Board.quan_beishu);

        int i;
        AffineTransform fb;
        for (i = 0; i < Board.bElements.size(); ++i) {
            fb = new AffineTransform(sf);
            fb.concatenate((Board.bElements.get(i)).Tx);
            (Board.bElements.get(i)).Tx = fb;
        }

        sf = AffineTransform.getTranslateInstance(Board.quan_x, Board.quan_y);

        for (i = 0; i < Board.bElements.size(); ++i) {
            fb = new AffineTransform(sf);
            fb.concatenate((Board.bElements.get(i)).Tx);
            (Board.bElements.get(i)).Tx = fb;
        }

    }

    public static void shu_chu(BufferedImage bb, String ss) {
        File outputfile = new File(ss);

        try {
            ImageIO.write(bb, "png", outputfile);
        } catch (IOException e) {
            Logger.getLogger(BElement.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public static BufferedImage qu_tu(List<BElement> sz) {
        BufferedImage fh = new BufferedImage((int) ((double) Board.bWidth / Board.resolution) - 2, (int) ((double) Board.bHeight / Board.resolution) - 2, 2);
        Graphics2D g2D = fh.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, fh.getWidth(), fh.getHeight());
        boolean you = false;

        for (int i = 1; i < Board.bElements.size(); ++i) {
            if ((Board.bElements.get(i)).type == 1 && (Board.bElements.get(i)).chuli_fs != 3) {
                GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(i)).path);
                lu_jing2.transform((Board.bElements.get(i)).Tx);
                Rectangle jx = lu_jing2.getBounds();
                g2D.drawImage((Board.bElements.get(i)).qu_tu(), jx.x, jx.y, null);
                you = true;
            }
        }

        if (you) {
            BufferedImage fh2 = new BufferedImage((int) ((double) Board.bWidth / Board.resolution), (int) ((double) Board.bHeight / Board.resolution), 2);
            Graphics2D g2D2 = fh2.createGraphics();
            g2D2.setBackground(Color.WHITE);
            int w = fh2.getWidth();
            int h = fh2.getHeight();
            g2D2.clearRect(0, 0, w, h);
            g2D2.drawImage(fh, 1, 1, null);
            qu_jvxing(Board.bElements);
            Rectangle jx = new Rectangle(0, 0, w, h);
            new Rectangle();
            if (zui_zhong_wjx.x == 0 && zui_zhong_wjx.y == 0 && zui_zhong_wjx.width == 0 && zui_zhong_wjx.height == 0) {
                return null;
            } else if (!jx.contains(zui_zhong_wjx.x, zui_zhong_wjx.y) && !jx.contains(zui_zhong_wjx.x + zui_zhong_wjx.width, zui_zhong_wjx.y + zui_zhong_wjx.height)) {
                return null;
            } else {
                Rectangle jx2 = jx.createIntersection(zui_zhong_wjx).getBounds();
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

    public static BufferedImage qu_tu_sl(List<BElement> sz) {
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

            if ((Board.bElements.get(i)).type == 1 && (Board.bElements.get(i)).chuli_fs == 3) {
                GeneralPath lu_jing2 = new GeneralPath((Board.bElements.get(i)).path);
                lu_jing2.transform((Board.bElements.get(i)).Tx);
                Rectangle jx = lu_jing2.getBounds();
                g2D.setColor(Color.BLACK);
                g2D.drawImage((Board.bElements.get(i)).qu_tu(), jx.x, jx.y, null);
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
                qu_jvxing(Board.bElements);
                Rectangle jx = new Rectangle(0, 0, w, h);
                new Rectangle();
                if (zui_zhong_wjx.x == 0 && zui_zhong_wjx.y == 0 && zui_zhong_wjx.width == 0 && zui_zhong_wjx.height == 0) {
                    return null;
                } else if (!jx.contains(zui_zhong_wjx.x, zui_zhong_wjx.y) && !jx.contains(zui_zhong_wjx.x + zui_zhong_wjx.width, zui_zhong_wjx.y + zui_zhong_wjx.height)) {
                    return null;
                } else {
                    Rectangle jx2 = jx.createIntersection(zui_zhong_wjx).getBounds();
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

    public static BufferedImage qu_tu_sl_tc(List<BElement> sz) {
        BufferedImage fh = new BufferedImage((int) ((double) Board.bWidth / Board.resolution), (int) ((double) Board.bHeight / Board.resolution), 2);
        Graphics2D g2D = fh.createGraphics();
        g2D.setBackground(Color.WHITE);
        g2D.clearRect(0, 0, fh.getWidth(), fh.getHeight());
        boolean you = false;

        for (int i = 1; i < Board.bElements.size(); ++i) {
            GeneralPath lu_jing4 = new GeneralPath((Board.bElements.get(i)).path);
            lu_jing4.transform((Board.bElements.get(i)).Tx);
            if ((Board.bElements.get(i)).type == 0 && (Board.bElements.get(i)).fill) {
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
            qu_jvxing(Board.bElements);
            Rectangle jx = new Rectangle(0, 0, w, h);
            new Rectangle();
            if (zui_zhong_wjx.x == 0 && zui_zhong_wjx.y == 0 && zui_zhong_wjx.width == 0 && zui_zhong_wjx.height == 0) {
                return null;
            } else if (!jx.contains(zui_zhong_wjx.x, zui_zhong_wjx.y) && !jx.contains(zui_zhong_wjx.x + zui_zhong_wjx.width, zui_zhong_wjx.y + zui_zhong_wjx.height)) {
                return null;
            } else {
                Rectangle jx2 = jx.createIntersection(zui_zhong_wjx).getBounds();
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

    static BPoint qu_xiao(BPoint d1, BPoint d2, BPoint d3) {
        int lxiao = Math.min(Math.abs(d1.x - d2.x), Math.abs(d1.y - d2.y));
        int lxiao2 = Math.min(Math.abs(d1.x - d3.x), Math.abs(d1.y - d3.y));
        return lxiao < lxiao2 ? d2 : d3;
    }

    static BPoint qu_jindian(BPoint d, BufferedImage bb) {
        BPoint fh = new BPoint(50000, 0);
        List<BPoint> zd = new ArrayList<>();

        for (int c = 1; c < da; ++c) {
            int ls = d.y - c;

            int b;
            for (b = d.x - c; b < d.x + c; ++b) {
                if (b > 0 && b < tu_kuan && ls > 0 && ls < tu_gao && (new Color(bb.getRGB(b, ls))).getRed() == 0) {
                    zd.add(new BPoint(b, ls));
                }
            }

            ls = d.x + c;

            for (b = d.y - c; b < d.y + c; ++b) {
                if (b > 0 && b < tu_gao && ls > 0 && ls < tu_kuan && (new Color(bb.getRGB(ls, b))).getRed() == 0) {
                    zd.add(new BPoint(ls, b));
                }
            }

            ls = d.y + c;

            for (b = d.x + c; b > d.x - c; --b) {
                if (b > 0 && b < tu_kuan && ls > 0 && ls < tu_gao && (new Color(bb.getRGB(b, ls))).getRed() == 0) {
                    zd.add(new BPoint(b, ls));
                }
            }

            ls = d.x - c;

            for (b = d.y + c; b > d.y - c; --b) {
                if (b > 0 && b < tu_gao && ls > 0 && ls < tu_kuan && (new Color(bb.getRGB(ls, b))).getRed() == 0) {
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

    static List<BPoint> pai_xu(BufferedImage tu_diaoke2) {
        BufferedImage bb = tu_diaoke2.getSubimage(0, 0, tu_diaoke2.getWidth(), tu_diaoke2.getHeight());
        List<BPoint> fh = new ArrayList<>();
        tu_kuan = bb.getWidth();
        tu_gao = bb.getHeight();
        da = Math.max(bb.getWidth(), bb.getHeight());

        for (int i = 0; i < bPoints.size(); ++i) {
            if (i == 0) {
                fh.add(bPoints.get(i));
                fh.add(new BPoint(30000, 30000));
                bb.setRGB(bPoints.get(i).x, bPoints.get(i).y, Color.WHITE.getRGB());
            } else {
                new BPoint(0, 0);
                BPoint d2;
                if (fh.get(fh.size() - 1).x != 30000 && fh.get(fh.size() - 1).x != 50000) {
                    d2 = fh.get(fh.size() - 1);
                } else {
                    d2 = fh.get(fh.size() - 2);
                }

                BPoint d = qu_jindian(d2, bb);
                if (d.x == 50000) {
                    break;
                }

                if (!xiang_lian(d, d2)) {
                    fh.add(new BPoint(50000, 50000));
                    fh.add(d);
                    fh.add(new BPoint(30000, 30000));
                } else {
                    fh.add(d);
                }

                bb.setRGB(d.x, d.y, Color.WHITE.getRGB());
            }
        }

        fh.add(new BPoint(50000, 50000));
        return fh;
    }

    static List<BPoint> pai_xu2(BufferedImage tu_diaoke2) {
        BufferedImage bb = tu_diaoke2.getSubimage(0, 0, tu_diaoke2.getWidth(), tu_diaoke2.getHeight());
        List<BPoint> fh = new ArrayList<>();
        tu_kuan = bb.getWidth();
        tu_gao = bb.getHeight();
        da = Math.max(bb.getWidth(), bb.getHeight());

        for (int i = 0; i < bPoints.size(); ++i) {
            if (i == 0) {
                fh.add(bPoints.get(i));
                bb.setRGB(bPoints.get(i).x, bPoints.get(i).y, Color.WHITE.getRGB());
            } else {
                if (fh.get(fh.size() - 1).x == 242 && fh.get(fh.size() - 1).y == 87) {
                    fh.get(fh.size() - 1).x = 242;
                }

                BPoint d = qu_jindian(fh.get(fh.size() - 1), bb);
                if (d.x == 50000) {
                    break;
                }

                fh.add(d);
                bb.setRGB(d.x, d.y, Color.WHITE.getRGB());
            }
        }

        return fh;
    }

    static List<BPoint> qu_tian_chong(BufferedImage img, int jian_ge) {
        List<BPoint> fh = new ArrayList<>();
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);

        for (int i = 1; i < height; i += jian_ge) {
            for (int j = 1; j < width; ++j) {
                if ((new Color(pixels[width * i + j])).getRed() == 0) {
                    fh.add(new BPoint(j, i));
                }
            }
        }

        return fh;
    }

    static List<BPoint> qudian(BufferedImage img) {
        List<BPoint> fh = new ArrayList<>();
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);

        for (int i = 1; i < height; ++i) {
            for (int j = 1; j < width; ++j) {
                if ((new Color(pixels[width * i + j])).getRed() == 0) {
                    fh.add(new BPoint(j, i));
                }
            }
        }

        return fh;
    }

    static int qu_fang_xiang(BPoint d1, BPoint d2) {
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
                fx = qu_fang_xiang(fh.get(fh.size() - 2), fh.get(fh.size() - 2));
            } else {
                int fx2 = qu_fang_xiang(fh.get(fh.size() - 1), dd.get(i));
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

    static boolean xiang_lian(BPoint a, BPoint b) {
        return Math.abs(a.x - b.x) <= 2 && Math.abs(a.y - b.y) <= 2;
    }

    public static double getDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static double qu_gao(double a, double b, double c) {
        double p = (a + b + c) / 2.0D;
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        return 2.0D * s / b;
    }

    public static double qu_gao_da(List<BPoint> dd, int d1, int d2) {
        double da = 0.0D;

        for (int i = 0; i < d2 - d1 - 1; ++i) {
            if (dd.get(i + d1).x != 30000 && dd.get(i + d1).x != 50000) {
                double a = getDistance(dd.get(d1).x, dd.get(i + d1).x, dd.get(d1).y, dd.get(i + d1).y);
                double b = getDistance(dd.get(d1).x, dd.get(d2).x, dd.get(d1).y, dd.get(d2).y);
                double c = getDistance(dd.get(i + d1).x, dd.get(d2).x, dd.get(i + d1).y, dd.get(d2).y);
                double d = qu_gao(a, b, c);
                if (d > da) {
                    da = d;
                }
            }
        }

        return da;
    }

    public static List<BPoint> you_hua2(List<BPoint> dd) {
        List<BPoint> fh = new ArrayList<>();
        int yi = 0;

        for (int i = 0; i < dd.size(); ++i) {
            if (dd.get(i).x == 30000) {
                fh.add(dd.get(i - 1));
                fh.add(dd.get(i));
                yi = i - 1;
            } else if (dd.get(i).x == 50000) {
                fh.add(dd.get(i - 1));
                fh.add(dd.get(i));
                yi = i + 1;
            } else if (i != 0) {
                int ge_shu = i - yi;
                if (ge_shu > 1) {
                    double da = qu_gao_da(dd, yi, i);
                    if (da > 0.7D) {
                        fh.add(dd.get(i - 1));
                        yi = i - 1;
                    }
                }
            }
        }

        return fh;
    }

    public static GeneralPath to_ls(BufferedImage bb) {
        List<BPoint> fh;
        GeneralPath lj = new GeneralPath();
        bb = BImage.qu_lunkuo(bb, 128);
        bPoints = qudian(bb);
        fh = pai_xu(bb);
        fh = you_hua2(fh);

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
                if (xiang_lian(qi, fh.get(i - 1))) {
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

        if (xiang_lian(qi, fh.get(fh.size() - 1))) {
            lj.closePath();
        }

        return lj;
    }

    public static List<BPoint> qu_dian(List<BElement> sz) {
        List<BPoint> fh = null;
        BufferedImage bb = qu_tu_sl(Board.bElements);
        if (bb != null) {
            bPoints = qudian(bb);
            fh = pai_xu2(bb);
        }

        BufferedImage bb2 = qu_tu_sl_tc(Board.bElements);
        if (bb2 != null) {
            fh.addAll(qu_tian_chong(bb2, 1 + tian_chong_md));
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

    public BufferedImage qu_tu() {
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
        g2D.drawImage(this.wei_tu_yuan, tx2, null);
        switch (this.chuli_fs) {
            case 1 -> {
                fh = BImage.greyScale(fh);
                fh = BImage.blackAndWhite(fh, (int) ((double) this.yu_zhi * 2.56D));
                if (this.chuli_fan) {
                    fh = BImage.fanse(fh);
                }
                if (this.chuli_jxx) {
                    fh = BImage.mirror_x(fh);
                }
                if (this.chuli_jxy) {
                    fh = BImage.mirror_y(fh);
                }
            }
            case 2 -> {
                fh = BImage.greyScale(fh);
                fh = BImage.convertGreyImgByFloyd(fh, (int) ((double) this.yu_zhi * 2.56D));
                if (this.chuli_fan) {
                    fh = BImage.fanse(fh);
                }
                if (this.chuli_jxx) {
                    fh = BImage.mirror_x(fh);
                }
                if (this.chuli_jxy) {
                    fh = BImage.mirror_y(fh);
                }
            }
            case 3 -> {
                fh = BImage.greyScale(fh);
                fh = BImage.blackAndWhite(fh, 128);
                fh = BImage.qu_lunkuo(fh, (int) ((double) this.yu_zhi * 2.56D));
            }
            case 4 -> {
                fh = BImage.su_miao(fh);
                fh = BImage.blackAndWhite(fh, 50 + (int) ((double) this.yu_zhi * 2.56D));
                if (this.chuli_fan) {
                    fh = BImage.fanse(fh);
                }
                if (this.chuli_jxx) {
                    fh = BImage.mirror_x(fh);
                }
                if (this.chuli_jxy) {
                    fh = BImage.mirror_y(fh);
                }
            }
        }

        return fh;
    }

    public void chu_li() {
        switch (this.chuli_fs) {
            case 1 -> {
                this.wei_tu = BImage.greyScale(this.wei_tu_yuan);
                this.wei_tu = BImage.blackAndWhite(this.wei_tu, (int) ((double) this.yu_zhi * 2.56D));
                if (this.chuli_fan) {
                    this.wei_tu = BImage.fanse(this.wei_tu);
                }
                if (this.chuli_jxx) {
                    this.wei_tu = BImage.mirror_x(this.wei_tu);
                }
                if (this.chuli_jxy) {
                    this.wei_tu = BImage.mirror_y(this.wei_tu);
                }
            }
            case 2 -> {
                this.wei_tu = BImage.greyScale(this.wei_tu_yuan);
                this.wei_tu = BImage.convertGreyImgByFloyd(this.wei_tu, (int) ((double) this.yu_zhi * 2.56D));
                if (this.chuli_fan) {
                    this.wei_tu = BImage.fanse(this.wei_tu);
                }
                if (this.chuli_jxx) {
                    this.wei_tu = BImage.mirror_x(this.wei_tu);
                }
                if (this.chuli_jxy) {
                    this.wei_tu = BImage.mirror_y(this.wei_tu);
                }
            }
            case 3 -> this.wei_tu = BImage.qu_lunkuo(this.wei_tu_yuan, (int) ((double) this.yu_zhi * 2.56D));
            case 4 -> {
                this.wei_tu = BImage.su_miao2(this.wei_tu_yuan);
                this.wei_tu = BImage.blackAndWhite(this.wei_tu, 50 + (int) ((double) this.yu_zhi * 2.56D));
                if (this.chuli_fan) {
                    this.wei_tu = BImage.fanse(this.wei_tu);
                }
                if (this.chuli_jxx) {
                    this.wei_tu = BImage.mirror_x(this.wei_tu);
                }
                if (this.chuli_jxy) {
                    this.wei_tu = BImage.mirror_y(this.wei_tu);
                }
            }
        }

    }
}
