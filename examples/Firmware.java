package examples;

import gnu.io.SerialPort;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Firmware extends JFrame {
    private JButton jButton1;
    private JComboBox<String> jComboBox1;
    private JLabel jLabel1;
    private JProgressBar jProgressBar1;

    public Firmware() {
        this.initComponents();
    }

    private void initComponents() {
        this.jButton1 = new JButton();
        this.jComboBox1 = new JComboBox();
        this.jLabel1 = new JLabel();
        this.jProgressBar1 = new JProgressBar();
        this.setDefaultCloseOperation(3);
        this.setBackground(SystemColor.controlHighlight);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                Firmware.this.formWindowOpened(evt);
            }
        });
        this.jButton1.setText("更新");
        this.jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Firmware.this.jButton1ActionPerformed(evt);
            }
        });
        this.jComboBox1.setModel(new DefaultComboBoxModel(new String[]{"K6", "JL1", "JL1_S", "JL2", "JL3", "JL3_S", "JL4", "JL4_S", "L1", "L1_S", " "}));
        this.jLabel1.setText("设备型号");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(66, 66, 66).addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.RELATED, 72, 32767).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jButton1, -1, -1, 32767).addComponent(this.jComboBox1, -2, 164, -2)).addGap(50, 50, 50)).addComponent(this.jProgressBar1, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(42, 42, 42).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jComboBox1, -2, -1, -2).addComponent(this.jLabel1)).addGap(33, 33, 33).addComponent(this.jButton1, -2, 37, -2).addPreferredGap(ComponentPlacement.RELATED, 11, 32767).addComponent(this.jProgressBar1, -2, -1, -2)));
        this.pack();
    }

    private void formWindowOpened(WindowEvent evt) {
        SerialPort com = null;
        Main.handler = new Com(com);
        this.jLabel1.setText(Main.str_model);
        this.jButton1.setText(Main.str_update);
        this.setBounds(500, 300, this.getWidth(), this.getHeight());
        this.setIconImage((new ImageIcon(this.getClass().getResource("/tu/tu_biao.png"))).getImage());
    }

    public boolean downloadNet(String di_zhi) throws MalformedURLException {
        int bytesum = 0;
        URL url = new URL(di_zhi);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            String filepath = System.getProperty("user.dir");
            FileOutputStream fs = new FileOutputStream(filepath + "/bin.bin");
            byte[] buffer = new byte[1204];

            int byteread;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }

            inStream.close();
            fs.close();
            return true;
        } catch (FileNotFoundException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        return false;
    }

    void fu_wei() {
        Main.handler.send(new byte[]{-2, 0, 4, 0}, 1);

        try {
            Thread.sleep(600L);
        } catch (InterruptedException var2) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var2);
        }

    }

    public byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > 2147483647L) {
            System.out.println("file too big...");
            return null;
        } else {
            FileInputStream fi = new FileInputStream(file);
            byte[] buffer = new byte[(int) fileSize];
            int offset = 0;

            int numRead;
            for (boolean var8 = false; offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0; offset += numRead) {
            }

            if (offset != buffer.length) {
                throw new IOException("Could not completely read file " + file.getName());
            } else {
                fi.close();
                return buffer;
            }
        }
    }

    int jiao_yan(List<Byte> m) {
        int jiao = 0;

        for (int i = 0; i < m.size(); ++i) {
            jiao += m.get(i);
        }

        if (jiao > 255) {
            jiao = ~jiao;
            ++jiao;
        }

        jiao &= 255;
        return jiao;
    }

    private static byte[] listTobyte1(List<Byte> list) {
        if (list != null && list.size() >= 0) {
            byte[] bytes = new byte[list.size()];
            int i = 0;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); ++i) {
                bytes[i] = (Byte) iterator.next();
            }

            return bytes;
        } else {
            return null;
        }
    }

    void sheng() {
        byte[] byData = null;
        Main.handler.send(new byte[]{2, 0, 5, 0, 115}, 1);

        try {
            Thread.sleep(6000L);
        } catch (InterruptedException var10) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var10);
        }

        String filepath = System.getProperty("user.dir");

        try {
            byData = this.getContent(filepath + "/bin.bin");
        } catch (IOException var9) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var9);
        }

        if (!byData.equals(null)) {
            List<Byte> bao = new ArrayList();
            int l;
            if (byData.length % 1024 > 0) {
                l = byData.length / 1024 + 1;
            } else {
                l = byData.length / 1024;
            }

            for (int j = 0; j < l; ++j) {
                for (int i = 0; i < 1024; ++i) {
                    if (j * 1024 + i < byData.length) {
                        bao.add(byData[j * 1024 + i]);
                    } else {
                        bao.add((byte) -1);
                    }
                }

                bao.add(0, (byte) 4);
                bao.add(0, (byte) 4);
                bao.add(0, (byte) 3);
                bao.add((byte) this.jiao_yan(bao));

                do {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var8) {
                        Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var8);
                    }
                } while (!Main.handler.send(listTobyte1(bao), 1));

                bao.clear();
                Dimension d = this.jProgressBar1.getSize();
                Rectangle rect = new Rectangle(0, 0, d.width, d.height);
                this.jProgressBar1.setValue((int) ((double) (j / l) * 100.0D));
                this.jProgressBar1.paintImmediately(rect);
            }

        }
    }

    void geng_xin(final String di_zhi) {
        Runnable runnable2 = new Runnable() {
            public void run() {
                try {
                    if (Firmware.this.downloadNet(di_zhi)) {
                        Firmware.this.fu_wei();
                        Firmware.this.sheng();
                        Main.handler.send(new byte[]{4, 0, 4, 0}, 1);
                    } else {
                        JOptionPane.showMessageDialog(null, Main.str_download_fail);
                    }
                } catch (MalformedURLException var2) {
                    Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var2);
                }

            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        switch (this.jComboBox1.getSelectedIndex()) {
            case 0:
                this.geng_xin("http://jiakuo25.0594.bftii.com/K6.bin");
                break;
            case 1:
                System.out.print(1);
                break;
            case 2:
                System.out.print(2);
                break;
            case 3:
                System.out.print(3);
                break;
            case 4:
                System.out.print(4);
                break;
            case 5:
                System.out.print(4);
                break;
            case 6:
                System.out.print(4);
                break;
            case 7:
                System.out.print(4);
                break;
            case 8:
                System.out.print(4);
                break;
            case 9:
                System.out.print(4);
        }

    }

    public static void main(String[] args) {
        try {
            LookAndFeelInfo[] var1 = UIManager.getInstalledLookAndFeels();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                LookAndFeelInfo info = var1[var3];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException var5) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var5);
        } catch (InstantiationException var6) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var6);
        } catch (IllegalAccessException var7) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var7);
        } catch (UnsupportedLookAndFeelException var8) {
            Logger.getLogger(Firmware.class.getName()).log(Level.SEVERE, null, var8);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new Firmware()).setVisible(true);
            }
        });
    }
}
