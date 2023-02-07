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
                                Board.bElements.get(i).bitMapImg = new BufferedImage(Board.bElements.get(i).bitMapW, Board.bElements.get(i).bitMapH, 2);
                                Board.bElements.get(i).bitMapImg2 = new BufferedImage(Board.bElements.get(i).bitMap2W, Board.bElements.get(i).bitMap2H, 2);
                                Board.bElements.get(i).bitMapImg.setRGB(0, 0, Board.bElements.get(i).bitMapW, Board.bElements.get(i).bitMapH, Board.bElements.get(i).bitMap, 0, Board.bElements.get(i).bitMapW);
                                Board.bElements.get(i).bitMapImg2.setRGB(0, 0, Board.bElements.get(i).bitMap2W, Board.bElements.get(i).bitMap2H, Board.bElements.get(i).bitMap2, 0, Board.bElements.get(i).bitMap2W);
                            }
                        }

                        if (hb != null) {
                            hb.repaint();
                        }
                    } else if (suffix.equals("PLT")) {
                        PLT plt = new PLT();
                        plt.analyze(f);
                        hb.updateUI();
                    }
                } else {
                    try {
                        img = ImageIO.read(new File(fileName));
                        Board.bElements.add(BElement.create(1, img));
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
