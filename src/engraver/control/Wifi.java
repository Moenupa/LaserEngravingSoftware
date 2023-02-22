package engraver.control;

import engraver.Main;
import engraver.model.Board;

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

    public byte[] ret_val = null;
    public int ret_code = 0;
    public boolean connected = false;
    public boolean occupied = false;
    public ServerSocket serverSocket = null;
    Socket socket;

    public JButton btn_wificonnect = null;
    public Board board = null;
    public JComboBox<String> sd_accuracy = null;
    public JSlider sd_weak_light = null;
    public JProgressBar jdt = null;
    public Main window = null;

    private final Object lock = new Object();
    boolean listeningForResponse = true;
    byte[] buff = new byte[100];
    int buff_len = 0;

    public Wifi() {
        this.open();
    }

    public boolean connect(byte[] data, int timeout) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return false;
        } else {
            buff_len = 0;
            new WriteThread(data).start();
            this.listeningForResponse = true;

            while (buff_len < 1 && m++ <= timeout) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lock) {
                this.listeningForResponse = false;
                if (buff_len < 1) {
                    return false;
                } else if (this.buff[0] == 9) {
                    return true;
                } else {
                    buff_len = 0;
                    return false;
                }
            }
        }
    }

    public boolean send(byte[] data, int timeout) {
        this.ret_val = null;
        int m = 0;

        if (!this.connected) return false;

        new WriteThread(data).start();
        buff_len = 0;
        this.listeningForResponse = true;

        while (buff_len < 1 && m++ <= timeout) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (lock) {
            this.listeningForResponse = false;
            if (buff_len < 1) {
                return false;
            } else if (this.buff[0] == 9) {
                return true;
            } else {
                buff_len = 0;
                return false;
            }
        }
    }

    public byte[] version(byte[] data, int timeout) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return null;
        } else {
            new WriteThread(data).start();
            new ReadThread().start();
            buff_len = 0;
            this.listeningForResponse = true;

            while (buff_len < 3 && m++ <= timeout) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lock) {
                this.listeningForResponse = false;
                if (buff_len < 3) {
                    buff_len = 0;
                    return null;
                } else {
                    byte[] ret = new byte[]{this.buff[0], this.buff[1], this.buff[2]};
                    buff_len = 0;
                    return ret;
                }
            }
        }
    }

    public boolean kaishi(byte[] data, int timeout) {
        this.ret_val = null;
        int m = 0;
        if (!this.connected) {
            return false;
        } else {
            new WriteThread(data).start();
            buff_len = 0;
            this.listeningForResponse = true;

            while (buff_len < 4 && m++ <= timeout) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lock) {
                this.listeningForResponse = false;
                if (buff_len < 4) {
                    return false;
                } else if (this.buff[0] == -1 && this.buff[1] == -1 && this.buff[2] == -1 && this.buff[3] == -2) {
                    return true;
                } else {
                    buff_len = 0;
                    return false;
                }
            }
        }
    }

    private void open() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12346);
                socket = serverSocket.accept();
                bis = new BufferedInputStream(socket.getInputStream());
                bos = new BufferedOutputStream(socket.getOutputStream());
                new ReadThread().start();
                connected = true;
                if (btn_wificonnect != null) {
                    int count = 0;

                    do {
                        if (count++ > 3) {
                            close();
                            return;
                        }

                        Thread.sleep(500L);
                    } while (!connect(new byte[] {10, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100));

                    byte[] ret = version(new byte[] {-1, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100);
                    if (ret != null) {
                        board.version(ret[2], 2);
                    }

                    Thread.sleep(500L);
                    btn_wificonnect.setIcon(new ImageIcon(this.getClass().getResource("/res/wifi.png")));
                    int weak_light = sd_weak_light.getValue() * 2;
                    int accuracy = sd_accuracy.getSelectedIndex();
                    Board.RESOLUTION = 0.05D + accuracy * 0.0125D;
                    board.boardSetup();
                    connect(new byte[] {40, 0, 11, (byte) weak_light, (byte) accuracy, 0, 0, 0, 0, 0, 0}, 200);
                }
            } catch (Exception e) {
                e.printStackTrace();
                connected = false;
                if (btn_wificonnect != null) {
                    btn_wificonnect.setIcon(new ImageIcon(this.getClass().getResource("/res/wifi2.png")));
                }
            }
        }).start();
        new Thread(() -> {
            while (!connected) {
                try {
                    udp = new DatagramSocket();
                    serverAddress = InetAddress.getByName("255.255.255.255");
                    byte[] data = ("IPjiakuo\"" + getIP() + "\",12346\r\n").getBytes();
                    byte[] data2 = new byte[data.length + 3];
                    data2[0] = 2;
                    data2[1] = (byte) (data2.length >> 8);
                    data2[2] = (byte) data2.length;

                    System.arraycopy(data, 0, data2, 3, data.length);

                    DatagramPacket packet = new DatagramPacket(data2, data2.length);
                    packet.setSocketAddress(new InetSocketAddress("255.255.255.255", 12345));
                    udp.setBroadcast(true);
                    udp.send(packet);

                    Thread.sleep(2000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    String getIP() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            Logger.getLogger("WIFI").log(Level.SEVERE, null, e);
        }
        return ip;
    }

    public void close() {
        this.btn_wificonnect.setIcon(new ImageIcon(this.getClass().getResource("/res/wifi2.png")));
        this.connected = false;
        this.bis = null;
        this.bos = null;
        this.readThread = null;
        this.udp = null;
        this.window.wifi = null;

        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                Logger.getLogger("WIFI").log(Level.SEVERE, null, e);
            }

            this.serverSocket = null;
        }

        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                Logger.getLogger("WIFI").log(Level.SEVERE, null, e);
            }

            this.socket = null;
        }
    }

    private class WriteThread extends Thread {
        byte[] bytes;

        WriteThread(byte[] bytes) {
            this.bytes = bytes;
        }

        public void run() {
            try {
                if (!occupied) {
                    occupied = true;
                    bos.write(this.bytes);
                    bos.flush();
                    occupied = false;
                } else {
                    Thread.sleep(500L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadThread extends Thread {
        private ReadThread() {
        }

        public void run() {
            while (true) {
                try {
                    if (bis != null && bis.available() > 0) {
                        if (listeningForResponse) {
                            synchronized (lock) {
                                buff_len = bis.read(buff);
                            }
                            return;
                        }
                        if (socket.isClosed()) {
                            return;
                        }

                        buff_len = bis.read(buff);

                        if (buff_len <= 3) {
                            ret_code = buff_len;
                            return;
                        }

                        buff_len = 0;
                        if (buff[0] == -1 && buff[1] == -1 && buff[2] == 0 && window != null && connected) {
                            Main.engraveFinished = true;
                            jdt.setValue(buff[3]);
                            jdt.setVisible(true);
                            Main.engraveStarted = true;
                            Main.timeout = 0;
                        }
                    }
                    Thread.sleep(10L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
