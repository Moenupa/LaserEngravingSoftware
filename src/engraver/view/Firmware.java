package engraver.view;

import engraver.Main;
import engraver.Utils;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Firmware extends JFrame {
    private final JButton btn_update = new JButton(Main.str_update);
    private final JComboBox<String> opt_model = new JComboBox<>();
    private final JLabel lb_model = new JLabel(Main.str_model);
    private final JProgressBar progressBar = new JProgressBar();

    public Firmware() {
        this.initComponents();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBackground(SystemColor.controlHighlight);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                Firmware.this.formWindowOpened(evt);
            }
        });
        this.btn_update.addActionListener((e) -> {
            if (this.opt_model.getSelectedIndex() == 0) {
                this.updateFirmware("http://jiakuo25.0594.bftii.com/K6.bin");
            } else {
                System.out.print(this.opt_model.getSelectedIndex());
            }
        });
        this.opt_model.setModel(new DefaultComboBoxModel<>(new String[]
            {"K6", "JL1", "JL1_S", "JL2", "JL3", "JL3_S", "JL4", "JL4_S", "L1", "L1_S", " "}));

        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(66, 66, 66)
                    .addComponent(this.lb_model)
                    .addPreferredGap(ComponentPlacement.RELATED, 72, 32767)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(this.btn_update, -1, -1, 32767)
                        .addComponent(this.opt_model, -2, 164, -2))
                    .addGap(50, 50, 50))
                .addComponent(this.progressBar, -1, -1, 32767));
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addGroup(
                        layout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(this.opt_model, -2, -1, -2)
                            .addComponent(this.lb_model))
                    .addGap(33, 33, 33)
                    .addComponent(this.btn_update, -2, 37, -2)
                    .addPreferredGap(ComponentPlacement.RELATED, 11, 32767)
                    .addComponent(this.progressBar, -2, -1, -2)));
        this.pack();
    }

    private void formWindowOpened(WindowEvent evt) {
        this.setBounds(500, 300, this.getWidth(), this.getHeight());
        this.setIconImage((new ImageIcon(this.getClass().getResource("/res/tu_biao.png"))).getImage());
    }

    public static boolean downloadPkg(String url_str) {
        try {
            URL url = new URL(url_str);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            String filepath = System.getProperty("user.dir");
            FileOutputStream fs = new FileOutputStream(filepath + "/bin.bin");
            byte[] buffer = new byte[1204];

            int byteread;
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }

            inStream.close();
            fs.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void toggleUpdateStatus(boolean start) {
        if (start)
            Main.com.send(new byte[]{-2, 0, 4, 0}, 1);
        else
            Main.com.send(new byte[]{4, 0, 4, 0}, 1);
        try {
            Thread.sleep(600L);
        } catch (Exception e) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static byte[] readBytes(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        } else {
            FileInputStream fi = new FileInputStream(file);
            byte[] buffer = new byte[(int) fileSize];
            int offset = 0;

            int numRead;
            while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset != buffer.length) {
                throw new IOException("Could not completely read file " + file.getName());
            } else {
                fi.close();
                return buffer;
            }
        }
    }

    private static byte[] toBytes(List<Byte> list) {
        if (list == null) return null;

        byte[] bytes = new byte[list.size()];
        int i = 0;

        for (Iterator<Byte> iterator = list.iterator(); iterator.hasNext(); ++i) {
            bytes[i] = iterator.next();
        }

        return bytes;
    }

    public void transferPkg() {
        byte[] bytesData = null;
        Main.com.send(new byte[]{2, 0, 5, 0, 115}, 1);

        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, e);
        }

        String filepath = System.getProperty("user.dir");

        try {
            bytesData = readBytes(filepath + "/bin.bin");
        } catch (IOException e) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, e);
        }

        if (bytesData != null) {
            List<Byte> packets = new ArrayList<>();
            int l = (int) Math.ceil(bytesData.length / 1024.0);

            for (int j = 0; j < l; ++j) {
                for (int i = 0; i < 1024; ++i) {
                    if (j * 1024 + i < bytesData.length) {
                        packets.add(bytesData[j * 1024 + i]);
                    } else {
                        packets.add((byte) -1);
                    }
                }

                packets.add(0, (byte) 4);
                packets.add(0, (byte) 4);
                packets.add(0, (byte) 3);
                packets.add(Utils.checksum(packets));

                do {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, e);
                    }
                } while (!Main.com.send(toBytes(packets), 1));

                packets.clear();
                Dimension d = this.progressBar.getSize();
                Rectangle rect = new Rectangle(0, 0, d.width, d.height);
                this.progressBar.setValue((int) ((double) (j / l) * 100.0D));
                this.progressBar.paintImmediately(rect);
            }

        }
    }

    void updateFirmware(final String url) {
        new Thread(() -> {
            if (downloadPkg(url)) {
                toggleUpdateStatus(true);
                this.transferPkg();
                toggleUpdateStatus(false);
            } else {
                JOptionPane.showMessageDialog(null, Main.str_download_fail);
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, e);
        }

        EventQueue.invokeLater(() -> (new Firmware()).setVisible(true));
    }
}
