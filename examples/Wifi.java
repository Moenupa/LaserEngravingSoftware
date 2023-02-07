package examples;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wifi {
    BufferedInputStream bis;
    BufferedOutputStream bos;
    Wifi.ReadThread readThread;
    DatagramSocket udp;
    InetAddress serverAddress;
    public byte[] data_r = new byte[100];
    public byte[] data_w = new byte[2000];
    public byte[] ret_val = null;
    public int ret_code = 0;
    public boolean connected = false;
    public boolean occupied = false;
    public ServerSocket serverSocket = null;
    Socket socket;
    public int xin_tiao = 0;

    public JButton bt = null;
    public Board board = null;
    public JComboBox fbl = null;
    public JSlider rg = null;
    public JProgressBar jdt = null;
    public mainJFrame window = null;

    byte[] recv = new byte[100];
    int recv_count = 0;
    boolean zhu_dong = true;
    final Object recv_lock = new Object();
    byte[] recv2 = new byte[100];
    int recv2_count = 0;

    public Wifi() {
        this.fw_kai();
    }

    public void xin_tiao() {
        Runnable runnable2 = () -> {
            while (true) {
                if (!Board.boundingBox && !mainJFrame.kai_shi && !Wifi.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var4) {
                        Logger.getLogger(Wifi.class.getName()).log(Level.SEVERE, null, var4);
                    }

                    if (!Wifi.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException var3) {
                            Logger.getLogger("WIFI").log(Level.SEVERE, null, var3);
                        }

                        if (!Wifi.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                            Wifi.this.close();
                            return;
                        }
                    }
                }

                try {
                    Thread.sleep(6000L);
                } catch (InterruptedException var2) {
                    Logger.getLogger("WIFI").log(Level.SEVERE, null, var2);
                }
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();
    }

    public boolean connect(byte[] data, int chao) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return false;
        } else {
            this.data_w = data;
            Wifi.WriteRead writeRead = new Wifi.WriteRead();
            writeRead.start();
            this.recv_count = 0;
            this.zhu_dong = true;

            while (this.recv_count < 1 && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                }
            }

            synchronized (this.recv_lock) {
                this.zhu_dong = false;
                if (this.recv_count < 1) {
                    return false;
                } else if (this.recv[0] == 9) {
                    return true;
                } else {
                    this.recv_count = 0;
                    return false;
                }
            }
        }
    }

    public boolean xie2(byte[] data, int chao) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return false;
        } else {
            this.data_w = data;
            Wifi.WriteRead writeRead = new Wifi.WriteRead();
            writeRead.start();
            this.recv_count = 0;
            this.zhu_dong = true;

            while (this.recv_count < 1 && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                }
            }

            synchronized (this.recv_lock) {
                this.zhu_dong = false;
                if (this.recv_count < 1) {
                    return false;
                } else if (this.recv[0] == 9) {
                    return true;
                } else {
                    this.recv_count = 0;
                    return false;
                }
            }
        }
    }

    public byte[] version(byte[] data, int chao) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return null;
        } else {
            this.data_w = data;
            Wifi.WriteRead writeRead = new Wifi.WriteRead();
            writeRead.start();
            this.recv_count = 0;
            this.zhu_dong = true;

            while (this.recv_count < 3 && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }
            }

            synchronized (this.recv_lock) {
                this.zhu_dong = false;
                if (this.recv_count < 3) {
                    this.recv_count = 0;
                    return null;
                } else {
                    byte[] fh = new byte[]{this.recv[0], this.recv[1], this.recv[2]};
                    this.recv_count = 0;
                    return fh;
                }
            }
        }
    }

    public boolean kaishi(byte[] data, int chao) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return false;
        } else {
            this.data_w = data;
            Wifi.WriteRead writeRead = new Wifi.WriteRead();
            writeRead.start();
            this.recv_count = 0;
            this.zhu_dong = true;

            while (this.recv_count < 4 && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                }
            }

            synchronized (this.recv_lock) {
                this.zhu_dong = false;
                if (this.recv_count < 4) {
                    return false;
                } else if (this.recv[0] == -1 && this.recv[1] == -1 && this.recv[2] == -1 && this.recv[3] == -2) {
                    return true;
                } else {
                    this.recv_count = 0;
                    return false;
                }
            }
        }
    }

    public boolean xie_shuju(byte[] data, int chao) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return false;
        } else {
            this.data_w = data;
            Wifi.WriteRead writeRead = new Wifi.WriteRead();
            writeRead.start();
            this.recv_count = 0;
            this.zhu_dong = true;

            while (this.recv_count < 1 && m++ <= chao) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                }
            }

            synchronized (this.recv_lock) {
                this.zhu_dong = false;
                if (this.recv_count < 1) {
                    return false;
                } else if (this.recv[0] == 9) {
                    return true;
                } else {
                    this.recv_count = 0;
                    return false;
                }
            }
        }
    }

    private void fw_kai() {
        new Thread(() -> {
            try {
                Wifi.this.serverSocket = new ServerSocket(12346);
                Wifi.this.socket = Wifi.this.serverSocket.accept();
                Wifi.this.bis = new BufferedInputStream(Wifi.this.socket.getInputStream());
                Wifi.this.bos = new BufferedOutputStream(Wifi.this.socket.getOutputStream());
                Wifi.this.readThread = Wifi.this.new ReadThread();
                Wifi.this.readThread.start();
                Wifi.this.connected = true;
                if (Wifi.this.bt != null) {
                    int count = 0;

                    do {
                        if (count++ > 3) {
                            Wifi.this.close();
                            return;
                        }

                        Thread.sleep(500L);
                    } while (!Wifi.this.connect(new byte[]{10, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100));

                    byte[] fhx = Wifi.this.version(new byte[]{-1, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100);
                    if (fhx != null) {
                        Wifi.this.board.version(fhx, 2);
                    }

                    Thread.sleep(500L);
                    Wifi.this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi.png")));
                    int rg2 = Wifi.this.rg.getValue() * 2;
                    int jd = Wifi.this.fbl.getSelectedIndex();
                    Board.resolution = 0.05D + (double) Wifi.this.fbl.getSelectedIndex() * 0.0125D;
                    Wifi.this.board.di_tu();
                    Wifi.this.connect(new byte[]{40, 0, 11, (byte) rg2, (byte) jd, 0, 0, 0, 0, 0, 0}, 200);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Wifi.this.connected = false;
                if (Wifi.this.bt != null) {
                    Wifi.this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
                }
            }
        }).start();
        new Thread(() -> {
            while (!Wifi.this.connected) {
                try {
                    Wifi.this.udp = new DatagramSocket();
                    Wifi.this.serverAddress = InetAddress.getByName("255.255.255.255");
                    byte[] data = ("IPjiakuo\"" + Wifi.this.qu_ip() + "\",12346\r\n").getBytes();
                    byte[] data2 = new byte[data.length + 3];
                    data2[0] = 2;
                    data2[1] = (byte) (data2.length >> 8);
                    data2[2] = (byte) data2.length;

                    for (int i = 0; i < data.length; ++i) {
                        data2[i + 3] = data[i];
                    }

                    DatagramPacket packet = new DatagramPacket(data2, data2.length);
                    packet.setSocketAddress(new InetSocketAddress("255.255.255.255", 12345));
                    Wifi.this.udp.setBroadcast(true);
                    Wifi.this.udp.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    String qu_ip() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var3) {
            Logger.getLogger("WIFI").log(Level.SEVERE, null, var3);
        }
        return ip;
    }

    private String intToIp(int i) {
        return (i & 255) + "." + (i >> 8 & 255) + "." + (i >> 16 & 255) + "." + (i >> 24 & 255);
    }

    public void close() {
        this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
        this.connected = false;
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

        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException var3) {
                Logger.getLogger("WIFI").log(Level.SEVERE, null, var3);
            }

            this.serverSocket = null;
        }

        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException var2) {
                Logger.getLogger("WIFI").log(Level.SEVERE, null, var2);
            }

            this.socket = null;
        }

        if (this.window.wifi != null) {
            this.window.wifi = null;
        }

    }

    private class WriteRead extends Thread {
        private WriteRead() {
        }

        public void run() {
            if (!Wifi.this.occupied) {
                Wifi.this.occupied = true;
                Wifi.this.xin_tiao = 0;

                try {
                    Wifi.this.bos.write(Wifi.this.data_w);
                    Wifi.this.bos.flush();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }

                Wifi.this.occupied = false;
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
                    if (Wifi.this.bis != null) {
                        count = Wifi.this.bis.available();
                    }

                    if (count > 0) {
                        int i;
                        if (Wifi.this.zhu_dong) {
                            synchronized (Wifi.this.recv_lock) {
                                i = Wifi.this.bis.read(Wifi.this.data_r);

                                for (int ix = 0; ix < i; ++ix) {
                                    Wifi.this.recv[Wifi.this.recv_count++] = Wifi.this.data_r[ix];
                                }
                            }
                        } else {
                            if (Wifi.this.socket.isClosed()) {
                                return;
                            }

                            int f = Wifi.this.bis.read(Wifi.this.data_r);

                            for (i = 0; i < f; ++i) {
                                Wifi.this.recv2[Wifi.this.recv2_count++] = Wifi.this.data_r[i];
                            }

                            if (Wifi.this.recv2_count > 3) {
                                Wifi.this.recv2_count = 0;
                                if (Wifi.this.recv2[0] == -1 && Wifi.this.recv2[1] == -1 && Wifi.this.recv2[2] == 0 && Wifi.this.window != null && (Wifi.this.window.com_isOpened || Wifi.this.connected)) {
                                    mainJFrame.kai_shi2 = true;
                                    Wifi.this.jdt.setValue(Wifi.this.recv2[3]);
                                    Wifi.this.jdt.setVisible(true);
                                    mainJFrame.kai_shi = true;
                                    mainJFrame.timeout = 0;
                                }
                            } else {
                                Wifi.this.ret_code = f;
                                Wifi.this.xin_tiao = 0;
                            }
                        }
                    }

                    Thread.sleep(10L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // $FF: synthetic method
        ReadThread(Object x1) {
            this();
        }
    }
}
