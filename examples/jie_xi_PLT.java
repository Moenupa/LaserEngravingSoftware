package examples;

import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class jie_xi_PLT {
    void add_lj(GeneralPath lj) {
        lj.setWindingRule(0);
        new Tu_yuan();
        Tu_yuan ty = Tu_yuan.chuang_jian(0, (BufferedImage) null);
        ty.path = new GeneralPath(lj);
        Hua_ban.ty_shuzu.add(ty);

        for (int i = 0; i < Hua_ban.ty_shuzu.size(); ++i) {
            ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).selected = false;
        }

        ((Tu_yuan) Hua_ban.ty_shuzu.get(Hua_ban.ty_shuzu.size() - 1)).selected = true;
    }

    void jie_xi_PLT(File file) {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();

        String plt;
        try {
            reader = new BufferedReader(new FileReader(file));

            while ((plt = reader.readLine()) != null) {
                sbf.append(plt);
            }

            reader.close();
        } catch (IOException var31) {
            var31.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var30) {
                    var30.printStackTrace();
                }
            }

        }

        plt = sbf.toString();
        plt.replaceAll("\r|\n", "");
        String[] strArr = plt.split(";");
        GeneralPath lj = new GeneralPath();
        boolean yi = true;
        boolean jue_dui = true;
        double d_x = 0.0D;
        double d_y = 0.0D;
        double q_x = 0.0D;
        double q_y = 0.0D;

        for (int i = 0; i < strArr.length; ++i) {
            String ml = strArr[i].toUpperCase();
            String zb = "";
            ml = strArr[i].substring(0, 2);
            String[] zb2;
            double x;
            double y;
            if (ml.equals("PU")) {
                zb = strArr[i].substring(2, strArr[i].length());
                zb2 = zb.split(" ");
                if (zb2.length == 2) {
                    x = Double.valueOf(zb2[0]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = Double.valueOf(zb2[1]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = 0.0D - y;
                    if (jue_dui) {
                        d_x = x;
                        d_y = y;
                    } else {
                        d_x += x;
                        d_y += y;
                    }

                    if (yi) {
                        lj.moveTo(d_x, d_y);
                        yi = false;
                    } else {
                        if (q_x == d_x && q_y == d_y) {
                        }

                        this.add_lj(lj);
                        lj = new GeneralPath();
                        lj.moveTo(d_x, d_y);
                    }

                    q_x = d_x;
                    q_y = d_y;
                }
            } else if (ml.equals("PD")) {
                zb = strArr[i].substring(2, strArr[i].length());
                zb2 = zb.split(" ");
                if (zb2.length == 2) {
                    x = Double.valueOf(zb2[0]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = Double.valueOf(zb2[1]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = 0.0D - y;
                    if (jue_dui) {
                        d_x = x;
                        d_y = y;
                    } else {
                        d_x += x;
                        d_y += y;
                    }

                    lj.lineTo(d_x, d_y);
                }
            } else if (ml.equals("PA")) {
                zb = strArr[i].substring(2, strArr[i].length());
                zb2 = zb.split(" ");
                if (zb2.length == 2) {
                    x = Double.valueOf(zb2[0]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = Double.valueOf(zb2[1]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = 0.0D - y;
                    jue_dui = true;
                    if (jue_dui) {
                        d_x = x;
                        d_y = y;
                    } else {
                        d_x += x;
                        d_y += y;
                    }

                    lj.lineTo(d_x, d_y);
                }
            } else if (ml.equals("PR")) {
                zb = strArr[i].substring(2, strArr[i].length());
                zb2 = zb.split(" ");
                if (zb2.length == 2) {
                    x = Double.valueOf(zb2[0]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = Double.valueOf(zb2[1]) / 40.0D / Hua_ban.fen_bian_lv;
                    y = 0.0D - y;
                    jue_dui = false;
                    if (jue_dui) {
                        d_x = x;
                        d_y = y;
                    } else {
                        d_x += x;
                        d_y += y;
                    }

                    lj.lineTo(d_x, d_y);
                }
            }
        }

        this.add_lj(lj);
    }
}
