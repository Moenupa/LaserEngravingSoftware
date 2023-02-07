package examples;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wifi2 {
    BufferedInputStream bis;
    BufferedOutputStream bos;
    Wifi2.ReadThread readThread;
    DatagramSocket udp;
    InetAddress serverAddress;
    public byte[] data_r = new byte[100];
    public byte[] data_w = new byte[2000];
    public byte[] fanhui = null;
    public byte[] fanhui2 = null;
    public int fan_hui_ma = 0;
    public boolean lian_jie = false;
    public boolean mang = false;
    public ServerSocket fu_wu = null;
    Socket ke_hu;
    public int xin_tiao = 0;
    public JButton bt = null;
    public Board hb = null;
    public JComboBox fbl = null;
    public JSlider rg = null;
    public JProgressBar jdt = null;
    public mainJFrame win = null;

    public Wifi2() {
        this.fw_kai();
    }

    public void xin_tiao() {
        Runnable runnable2 = new Runnable() {
            public void run() {
                while (true) {
                    if (!Board.boundingBox && !mainJFrame.kai_shi && !Wifi2.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException var4) {
                            Logger.getLogger(Wifi2.class.getName()).log(Level.SEVERE, (String) null, var4);
                        }

                        if (!Wifi2.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException var3) {
                                Logger.getLogger(Wifi2.class.getName()).log(Level.SEVERE, (String) null, var3);
                            }

                            if (!Wifi2.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                                Wifi2.this.guan_bi();
                                return;
                            }
                        }
                    }

                    try {
                        Thread.sleep(6000L);
                    } catch (InterruptedException var2) {
                        Logger.getLogger(Wifi2.class.getName()).log(Level.SEVERE, (String) null, var2);
                    }
                }
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();
    }

    public void xie2323(byte[] data) {
        if (this.lian_jie) {
            this.data_w = data;
            Wifi2.WriteRead writeRead = new Wifi2.WriteRead();
            writeRead.start();
        }

        this.xin_tiao = 0;
    }

    public boolean lianjie(byte[] data, int chao) {
        this.fanhui = null;
        int m = 0;
        if (!this.lian_jie) {
            return false;
        } else {
            this.data_w = data;
            Wifi2.WriteRead writeRead = new Wifi2.WriteRead();
            writeRead.start();

            while (this.fanhui == null && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
            }

            if (this.fanhui == null) {
                return false;
            } else {
                return this.fanhui[0] == 9;
            }
        }
    }

    public boolean xie2(byte[] data, int chao) {
        this.fanhui = null;
        int m = 0;
        if (!this.lian_jie) {
            return false;
        } else {
            this.data_w = data;
            Wifi2.WriteRead writeRead = new Wifi2.WriteRead();
            writeRead.start();

            while (this.fanhui == null && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
            }

            return this.fanhui != null;
        }
    }

    public byte[] ban_ben(byte[] data, int chao) {
        this.fanhui = null;
        int m = 0;
        if (!this.lian_jie) {
            return null;
        } else {
            this.data_w = data;
            Wifi2.WriteRead writeRead = new Wifi2.WriteRead();
            writeRead.start();

            while (this.fanhui == null && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
            }

            return this.fanhui;
        }
    }

    public boolean kaishi(byte[] data, int chao) {
        byte[] fh = new byte[4];
        int ge_shu = 0;
        this.fanhui = null;
        int m = 0;
        if (!this.lian_jie) {
            return false;
        } else {
            this.data_w = data;
            Wifi2.WriteRead writeRead = new Wifi2.WriteRead();
            writeRead.start();
            boolean bl = false;

            while (m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }

                bl = false;
                if (this.fanhui == null) {
                    bl = true;
                } else if (this.fanhui.length < 4) {
                    int i;
                    for (i = 0; i < this.fanhui.length; ++i) {
                        System.out.println(this.fanhui[i]);
                    }

                    for (i = 0; i < this.fanhui.length; ++i) {
                        fh[ge_shu++] = this.fanhui[i];
                        if (ge_shu == 4) {
                            if (fh[0] == -1 && fh[1] == -1 && fh[2] == -1 && fh[3] == -2) {
                                return true;
                            }

                            ge_shu = 0;
                        }
                    }

                    bl = true;
                } else if (this.fanhui.length >= 4) {
                    if (this.fanhui[0] == -1 && this.fanhui[1] == -1 && this.fanhui[2] == -1 && this.fanhui[3] == -2) {
                        return true;
                    }

                    bl = true;
                }

                if (!bl) {
                    break;
                }
            }

            if (this.fanhui == null) {
                return false;
            } else if (this.fanhui.length > 3) {
                return this.fanhui[0] == -1 && this.fanhui[1] == -1 && this.fanhui[2] == -1 && this.fanhui[3] == -2;
            } else {
                return false;
            }
        }
    }

    public boolean xie_shuju(byte[] data, int chao) {
        this.fanhui = null;
        int m = 0;
        if (!this.lian_jie) {
            return false;
        } else {
            this.data_w = data;
            Wifi2.WriteRead writeRead = new Wifi2.WriteRead();
            writeRead.start();

            while (this.fanhui == null && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
            }

            if (this.fanhui == null) {
                return false;
            } else {
                return this.fanhui.length == 1 && this.fanhui[0] == 9;
            }
        }
    }

    private void fw_kai() {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    Wifi2.this.fu_wu = new ServerSocket(12346);
                    Wifi2.this.ke_hu = Wifi2.this.fu_wu.accept();
                    Wifi2.this.bis = new BufferedInputStream(Wifi2.this.ke_hu.getInputStream());
                    Wifi2.this.bos = new BufferedOutputStream(Wifi2.this.ke_hu.getOutputStream());
                    Wifi2.this.readThread = Wifi2.this.new ReadThread();
                    Wifi2.this.readThread.start();
                    Wifi2.this.lian_jie = true;
                    if (Wifi2.this.bt != null) {
                        byte[] fh = null;
                        int var2 = 0;

                        while (true) {
                            if (var2++ > 3) {
                                Wifi2.this.guan_bi();
                                return;
                            }

                            Thread.sleep(500L);
                            if (Wifi2.this.lianjie(new byte[]{10, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100)) {
                                byte[] fhx = Wifi2.this.ban_ben(new byte[]{-1, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100);
                                if (fhx != null) {
                                    Wifi2.this.hb.version(fhx, 2);
                                }

                                Thread.sleep(500L);
                                Wifi2.this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi.png")));
                                int rg2 = Wifi2.this.rg.getValue() * 2;
                                int jd = Wifi2.this.fbl.getSelectedIndex();
                                Board.resolution = 0.05D + (double) Wifi2.this.fbl.getSelectedIndex() * 0.0125D;
                                Wifi2.this.hb.di_tu();
                                Wifi2.this.lianjie(new byte[]{40, 0, 11, (byte) rg2, (byte) jd, 0, 0, 0, 0, 0, 0}, 200);
                                break;
                            }
                        }
                    }

                    Wifi2.this.xin_tiao();
                } catch (Exception var6) {
                    var6.printStackTrace();
                    Wifi2.this.lian_jie = false;
                    if (Wifi2.this.bt != null) {
                        Wifi2.this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
                    }
                }

            }
        })).start();
        (new Thread(new Runnable() {
            public void run() {
                while (!Wifi2.this.lian_jie) {
                    try {
                        Wifi2.this.udp = new DatagramSocket();
                        Wifi2.this.serverAddress = InetAddress.getByName("255.255.255.255");
                        byte[] data = ("IPjiakuo\"" + Wifi2.this.qu_ip() + "\",12346\r\n").getBytes();
                        byte[] data2 = new byte[data.length + 3];
                        data2[0] = 2;
                        data2[1] = (byte) (data2.length >> 8);
                        data2[2] = (byte) data2.length;

                        for (int i = 0; i < data.length; ++i) {
                            data2[i + 3] = data[i];
                        }

                        DatagramPacket packet = new DatagramPacket(data2, data2.length);
                        packet.setSocketAddress(new InetSocketAddress("255.255.255.255", 12345));
                        Wifi2.this.udp.setBroadcast(true);
                        Wifi2.this.udp.send(packet);
                    } catch (SocketException var5) {
                        var5.printStackTrace();
                    } catch (UnknownHostException var6) {
                        var6.printStackTrace();
                    } catch (IOException var7) {
                        var7.printStackTrace();
                    }

                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException var4) {
                        var4.printStackTrace();
                    }
                }

            }
        })).start();
    }

    String qu_ip() {
        String ip = "";

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var3) {
            Logger.getLogger(Wifi2.class.getName()).log(Level.SEVERE, (String) null, var3);
        }

        return ip;
    }

    private String intToIp(int i) {
        return (i & 255) + "." + (i >> 8 & 255) + "." + (i >> 16 & 255) + "." + (i >> 24 & 255);
    }

    public void guan_bi() {
        this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
        this.lian_jie = false;
        if (this.bis != null) {
            this.bis = null;
        }

        if (this.bos != null) {
            this.bos = null;
        }

        if (this.readThread != null) {
            this.readThread = null;
        }

        if (this.udp != null) {
            this.udp = null;
        }

        if (this.fu_wu != null) {
            try {
                this.fu_wu.close();
            } catch (IOException var3) {
                Logger.getLogger(Wifi2.class.getName()).log(Level.SEVERE, (String) null, var3);
            }

            this.fu_wu = null;
        }

        if (this.ke_hu != null) {
            try {
                this.ke_hu.close();
            } catch (IOException var2) {
                Logger.getLogger(Wifi2.class.getName()).log(Level.SEVERE, (String) null, var2);
            }

            this.ke_hu = null;
        }

        if (this.win.wifi != null) {
            this.win.wifi = null;
        }

    }

    private class WriteRead extends Thread {
        private WriteRead() {
        }

        public void run() {
            if (!Wifi2.this.mang) {
                Wifi2.this.mang = true;
                Wifi2.this.xin_tiao = 0;

                try {
                    Wifi2.this.bos.write(Wifi2.this.data_w);
                    Wifi2.this.bos.flush();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }

                Wifi2.this.mang = false;
            } else {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            }

        }

        // $FF: synthetic method
        WriteRead(Object x1) {
            this();
        }
    }

    private class ReadThread extends Thread {
        private ReadThread() {
        }

        public void run() {
            while (true) {
                try {
                    int count = 0;
                    if (Wifi2.this.bis != null) {
                        count = Wifi2.this.bis.available();
                    }

                    if (count > 0) {
                        int f = Wifi2.this.bis.read(Wifi2.this.data_r);
                        Wifi2.this.fanhui = new byte[f];

                        for (int i = 0; i < f; ++i) {
                        }
                    }

                    Thread.sleep(10L);
                } catch (IOException var4) {
                    var4.printStackTrace();
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
            }
        }

        // $FF: synthetic method
        ReadThread(Object x1) {
            this();
        }
    }

    private class ReadThread2 extends Thread {
        public void run() {
            while (true) {
                try {
                    int count = 0;
                    if (Wifi2.this.bis != null) {
                        count = Wifi2.this.bis.available();
                    }

                    if (count > 0) {
                        int f = Wifi2.this.bis.read(Wifi2.this.data_r);
                        Wifi2.this.fanhui = new byte[f];
                        Wifi2.this.fanhui2 = new byte[f];

                        for (int i = 0; i < f; ++i) {
                            Wifi2.this.fanhui[i] = Wifi2.this.data_r[i];
                            Wifi2.this.fanhui2[i] = Wifi2.this.data_r[i];
                        }

                        if (Wifi2.this.fanhui.length == 4) {
                            if (Wifi2.this.fanhui[0] == -1 && Wifi2.this.fanhui[1] == -1 && Wifi2.this.fanhui[2] == 0) {
                                if (Wifi2.this.win != null && !Wifi2.this.win.com_isOpened && Wifi2.this.lian_jie) {
                                }
                            } else if (Wifi2.this.fanhui[0] == -1 && Wifi2.this.fanhui[1] == -1 && Wifi2.this.fanhui[2] == -1 && Wifi2.this.fanhui[3] == -1) {
                                Wifi2.this.jdt.setValue(0);
                                Wifi2.this.jdt.setVisible(false);
                                mainJFrame.kai_shi = false;
                            }
                        } else {
                            Wifi2.this.fan_hui_ma = f;
                            Wifi2.this.xin_tiao = 0;
                        }
                    }

                    Thread.sleep(10L);
                } catch (IOException var4) {
                    var4.printStackTrace();
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
            }
        }
    }
}