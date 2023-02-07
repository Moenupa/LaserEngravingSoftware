package examples;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;

class FileTransferHandler extends TransferHandler {
    public static Hua_ban hb = null;

    public boolean importData(JComponent comp, Transferable t) {
        try {
            List<File> list = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            Iterator var4 = list.iterator();

            while (true) {
                while (true) {
                    while (var4.hasNext()) {
                        File f = (File) var4.next();
                        System.out.println(f.getAbsolutePath());
                        String fileName = f.getAbsolutePath();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        suffix = suffix.toUpperCase();
                        BufferedImage plt;
                        if (!suffix.equals("BMP") && !suffix.equals("JPG") && !suffix.equals("PNG") && !suffix.equals("JPEG") && !suffix.equals("GIF")) {
                            if (suffix.equals("XJ")) {
                                try {
                                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));

                                    try {
                                        Hua_ban.ty_shuzu = (List) ois.readObject();
                                    } catch (Throwable var12) {
                                        try {
                                            ois.close();
                                        } catch (Throwable var11) {
                                            var12.addSuppressed(var11);
                                        }

                                        throw var12;
                                    }

                                    ois.close();
                                } catch (Exception var13) {
                                    var13.printStackTrace();
                                }

                                for (int i = 0; i < Hua_ban.ty_shuzu.size(); ++i) {
                                    if (((Tu_yuan) Hua_ban.ty_shuzu.get(i)).type == 1) {
                                        ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wei_tu = new BufferedImage(((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wt_w, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wt_g, 2);
                                        ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wei_tu_yuan = new BufferedImage(((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wty_w, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wty_g, 2);
                                        ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wei_tu.setRGB(0, 0, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wt_w, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wt_g, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wei_tu_, 0, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wt_w);
                                        ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wei_tu_yuan.setRGB(0, 0, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wty_w, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wty_g, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wei_tu_yuan_, 0, ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).wty_w);
                                    }
                                }

                                if (hb != null) {
                                    hb.repaint();
                                }
                            } else if (suffix.equals("PLT")) {
                                jie_xi_PLT plt = new jie_xi_PLT();
                                plt.jie_xi_PLT(f);
                                plt = null;
                                hb.updateUI();
                            }
                        } else {
                            try {
                                plt = ImageIO.read(new File(fileName));
                                Hua_ban.ty_shuzu.add(Tu_yuan.chuang_jian(1, plt));

                                for (int i = 0; i < Hua_ban.ty_shuzu.size(); ++i) {
                                    ((Tu_yuan) Hua_ban.ty_shuzu.get(i)).selected = false;
                                }

                                ((Tu_yuan) Hua_ban.ty_shuzu.get(Hua_ban.ty_shuzu.size() - 1)).selected = true;
                                Tu_yuan.center(Hua_ban.ty_shuzu);
                                if (hb != null) {
                                    hb.repaint();
                                }
                            } catch (IOException var14) {
                            }
                        }
                    }

                    return true;
                }
            }
        } catch (Exception var15) {
            var15.printStackTrace();
            return true;
        }
    }

    public boolean canImport(TransferSupport support) {
        return true;
    }
}
