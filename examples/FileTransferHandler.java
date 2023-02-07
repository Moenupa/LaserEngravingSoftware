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
    public static Board hb = null;

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
                                        Board.bElements = (List) ois.readObject();
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

                                for (int i = 0; i < Board.bElements.size(); ++i) {
                                    if (((BElement) Board.bElements.get(i)).type == 1) {
                                        ((BElement) Board.bElements.get(i)).wei_tu = new BufferedImage(((BElement) Board.bElements.get(i)).wt_w, ((BElement) Board.bElements.get(i)).wt_g, 2);
                                        ((BElement) Board.bElements.get(i)).wei_tu_yuan = new BufferedImage(((BElement) Board.bElements.get(i)).wty_w, ((BElement) Board.bElements.get(i)).wty_g, 2);
                                        ((BElement) Board.bElements.get(i)).wei_tu.setRGB(0, 0, ((BElement) Board.bElements.get(i)).wt_w, ((BElement) Board.bElements.get(i)).wt_g, ((BElement) Board.bElements.get(i)).wei_tu_, 0, ((BElement) Board.bElements.get(i)).wt_w);
                                        ((BElement) Board.bElements.get(i)).wei_tu_yuan.setRGB(0, 0, ((BElement) Board.bElements.get(i)).wty_w, ((BElement) Board.bElements.get(i)).wty_g, ((BElement) Board.bElements.get(i)).wei_tu_yuan_, 0, ((BElement) Board.bElements.get(i)).wty_w);
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
                                Board.bElements.add(BElement.chuang_jian(1, plt));

                                for (int i = 0; i < Board.bElements.size(); ++i) {
                                    ((BElement) Board.bElements.get(i)).selected = false;
                                }

                                ((BElement) Board.bElements.get(Board.bElements.size() - 1)).selected = true;
                                BElement.center(Board.bElements);
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
