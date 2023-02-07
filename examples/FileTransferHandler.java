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
import java.util.List;

class FileTransferHandler extends TransferHandler {
    public static Board hb = null;

    public boolean importData(JComponent comp, Transferable t) {
        try {
            List<File> list = (List) t.getTransferData(DataFlavor.javaFileListFlavor);

            for (File f : list) {
                System.out.println(f.getAbsolutePath());
                String fileName = f.getAbsolutePath();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                suffix = suffix.toUpperCase();
                BufferedImage img;
                if (!suffix.equals("BMP") && !suffix.equals("JPG") && !suffix.equals("PNG") && !suffix.equals("JPEG") && !suffix.equals("GIF")) {
                    if (suffix.equals("XJ")) {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));

                            try {
                                Board.bElements = (List) ois.readObject();
                            } catch (Throwable e) {
                                try {
                                    ois.close();
                                } catch (Throwable var11) {
                                    e.addSuppressed(var11);
                                }
                                throw e;
                            }

                            ois.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < Board.bElements.size(); ++i) {
                            if (Board.bElements.get(i).type == 1) {
                                Board.bElements.get(i).wei_tu = new BufferedImage(Board.bElements.get(i).wt_w, Board.bElements.get(i).wt_g, 2);
                                Board.bElements.get(i).wei_tu_yuan = new BufferedImage(Board.bElements.get(i).wty_w, Board.bElements.get(i).wty_g, 2);
                                Board.bElements.get(i).wei_tu.setRGB(0, 0, Board.bElements.get(i).wt_w, Board.bElements.get(i).wt_g, Board.bElements.get(i).wei_tu_, 0, Board.bElements.get(i).wt_w);
                                Board.bElements.get(i).wei_tu_yuan.setRGB(0, 0, Board.bElements.get(i).wty_w, Board.bElements.get(i).wty_g, Board.bElements.get(i).wei_tu_yuan_, 0, Board.bElements.get(i).wty_w);
                            }
                        }

                        if (hb != null) {
                            hb.repaint();
                        }
                    } else if (suffix.equals("PLT")) {
                        jie_xi_PLT plt = new jie_xi_PLT();
                        plt.analyzePLT(f);
                        hb.updateUI();
                    }
                } else {
                    try {
                        img = ImageIO.read(new File(fileName));
                        Board.bElements.add(BElement.chuang_jian(1, img));
                        Board.selectLast();
                        BElement.center(Board.bElements);
                        if (hb != null) {
                            hb.repaint();
                        }
                    } catch (IOException e) {
                    }
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean canImport(TransferSupport support) {
        return true;
    }
}
