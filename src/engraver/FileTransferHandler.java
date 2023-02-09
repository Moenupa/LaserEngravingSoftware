package engraver;

import engraver.model.BElement;
import engraver.model.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class FileTransferHandler extends TransferHandler {
    public static final Set<String> EXT_IMG = Set.of("BMP", "JPG", "PNG", "JPEG", "GIF");
    public static final Set<String> EXT_ENGRAVE = Set.of("XJ");
    private static final Set<String> PLT_INSTR = Set.of("PU", "PD", "PA", "PR");
    public static Board board = null;

    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".") + 1).toUpperCase());
    }
    public static String getFileExtension(File f) {
        return getFileExtension(f.getName()).orElse("");
    }

    public boolean importData(JComponent comp, Transferable t) {
        try {
            List<File> files = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : files) {
                importFile(file);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void importFile(File file, Runnable boardUpdate) {
        try {
            String ext = getFileExtension(file);
            if (EXT_IMG.contains(ext)) {
                Board.bElements.add(BElement.create(1, ImageIO.read(file)));
                Board.selectLast();
                Board.center();
                Undo.add();
                boardUpdate.run();
            } else if (EXT_ENGRAVE.contains(ext)) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Board.bElements = (List) ois.readObject();
                ois.close();

                Board.bElements.stream().filter(e -> e.type == 1).forEach(e -> {
                    e.bitMapImg = new BufferedImage(e.bitMapW, e.bitMapH, 2);
                    e.bitMapImg2 = new BufferedImage(e.bitMap2W, e.bitMap2H, 2);
                    e.bitMapImg.setRGB(0, 0, e.bitMapW, e.bitMapH, e.bitMap, 0, e.bitMapW);
                    e.bitMapImg2.setRGB(0, 0, e.bitMap2W, e.bitMap2H, e.bitMap2, 0, e.bitMap2W);
                });
                Undo.add();
                boardUpdate.run();
            } else if (ext.equals("PLT")) {
                FileTransferHandler.importPLT(file);
                Undo.add();
                boardUpdate.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importFile(File file) {
        importFile(file, () -> {if (board != null) board.repaint();});
    }

    public static void importPLT(File file) {
        String content = "";
        try {
            content = Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] strArr = content.replaceAll("[\r\n]", "").split(";");

        GeneralPath path = new GeneralPath();
        boolean isMoving = true;
        boolean isAbsolute = true;
        double d_x = 0.0D;
        double d_y = 0.0D;

        for (String s : strArr) {
            String instr = s.substring(0, 2);
            String[] val = s.substring(2).split(" ");

            if (val.length != 2) continue;
            if (!PLT_INSTR.contains(instr)) continue;

            double x = Double.parseDouble(val[0]) / 40.0D / Board.RESOLUTION;
            double y = - Double.parseDouble(val[1]) / 40.0D / Board.RESOLUTION;

            switch (instr) {
                case "PU" -> isMoving = true;
                case "PA" -> isAbsolute = true;
                case "PR" -> isAbsolute = false;
                case "PD" -> {}
            }

            if (isAbsolute) {
                d_x = x;
                d_y = y;
            } else {
                d_x += x;
                d_y += y;
            }
            if (isMoving) {
                path.moveTo(d_x, d_y);
                isMoving = false;
            } else {
                Board.addPath(path);
                path = new GeneralPath();
                path.moveTo(d_x, d_y);
            }

            path.lineTo(d_x, d_y);
        }

        Board.addPath(path);
    }

    public boolean canImport(TransferSupport support) {
        return true;
    }
}
