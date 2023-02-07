package examples;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Com {
    public JProgressBar jdt;

    SerialPort com;

    public byte[] ret_val = new byte[]{0, 0, 0, 0};
    int ret_code = 0;
    boolean ret = false;

    public int terminate_type = 0;
    public int terminate_count = 0;
    public List<Byte> terminate_buffer = new ArrayList<>();

    SerialPortEventListener terminate_win = (event) -> {
        if (event.getEventType() != 1)
            return;

        synchronized (this) {
            try {
                if (this.com == null) return;

                byte[] ret_val = SerialPortUtil.readData(this.com);
                switch (this.terminate_type) {
                    case 0:
                        this.ret = true;
                        this.ret_val = ret_val;
                        return;
                    case 2:
                        // add ret_val -> terminate buffer
                        this.terminate_count += ret_val.length;
                        for (byte b : ret_val) {
                            this.terminate_buffer.add(b);
                        }

                        // if count >= 3, mv first 3 -> this.ret_val
                        if (this.terminate_count >= 3) {
                            this.ret_val = new byte[3];
                            for (int i = 0; i < 3; ++i) {
                                this.ret_val[i] = this.terminate_buffer.get(i);
                            }
                            this.ret = true;
                        }
                        return;
                    case 3:
                        // add ret_val -> terminate buffer
                        for (byte b : ret_val) {
                            this.terminate_buffer.add(b);
                        }

                        int size = this.terminate_buffer.size();
                        if (
                                size > 3
                                        && this.terminate_buffer.get(size - 4) == -1
                                        && this.terminate_buffer.get(size - 3) == -1
                                        && this.terminate_buffer.get(size - 2) == 0
                        ) {
                            this.jdt.setValue(this.terminate_buffer.get(size - 1));
                            this.jdt.setVisible(true);
                            mainJFrame.kai_shi = true;
                            mainJFrame.kai_shi2 = true;
                            this.terminate_buffer.clear();
                        }

                        System.out.println(ret_val.length);
                        mainJFrame.timeout = 0;
                        return;
                    case 1:
                    default:
                        return;

                }
            } catch (Exception e) {
                Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                return;
            }
        }

    };
    SerialPortEventListener terminate = (event) -> {
        if (event.getEventType() == 1) {
            synchronized (this) {
                try {
                    if (this.com != null) {
                        byte[] fh2 = SerialPortUtil.readData(this.com);
                        byte[] fh = new byte[this.ret_code];

                        System.arraycopy(fh2, 0, fh, 0, fh.length);

                        if (fh.length == 4) {
                            if (fh[0] == -1 && fh[1] == -1 && fh[2] == 0) {
                                this.jdt.setValue(fh[3]);
                                this.jdt.setVisible(true);
                                mainJFrame.kai_shi = true;
                                mainJFrame.kai_shi2 = true;
                                mainJFrame.timeout = 0;
                            } else {
                                if (fh[0] == -1 && fh[1] == -1 && fh[2] == -1 && fh[3] == -1) {
                                    this.jdt.setValue(0);
                                    this.jdt.setVisible(false);
                                    mainJFrame.kai_shi = false;
                                    mainJFrame.kai_shi2 = false;
                                    mainJFrame.timeout = 0;
                                } else {
                                    this.ret = true;
                                    this.ret_val = fh;
                                }
                            }
                        } else {
                            this.ret = true;
                            this.ret_val = fh;
                        }
                        return;
                    }
                } catch (Exception e) {
                    Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                    return;
                }

            }
        }
    };

    public Com(SerialPort c) {
        this.terminate_buffer.add((byte) 1);
        synchronized (this) {
            this.com = c;

            try {
                Properties props = System.getProperties();
                String osName = props.getProperty("os.name");
                if (osName.contains("Win")) {
                    SerialPortUtil.setListenerToSerialPort(this.com, this.terminate_win);
                } else {
                    SerialPortUtil.setListenerToSerialPort(this.com, this.terminate);
                }
            } catch (TooManyListenersException e) {
                Logger.getLogger("COM").log(Level.SEVERE, null, e);
            }

        }
    }

    public void close() {
        if (this.com != null) this.com.close();
    }

    public boolean read_version() {
        final Object lock = new Object();
        this.ret_code = 3;
        this.terminate_count = 0;
        this.terminate_type = 2;
        this.terminate_buffer.clear();
        this.ret = false;

        SerialPortUtil.sendData(this.com, new byte[]{-1, 0, 4, 0});
        Thread t = new Thread(() -> {
            synchronized (lock) {
                for (int i = 200; i > 0; --i) {
                    if (this.ret) {
                        if (this.ret_val.length != 3)
                            this.ret = false;
                        break;
                    }

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                    }
                }

            }
        });
        t.start();

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }

        synchronized (lock) {
            return this.ret;
        }
    }

    public boolean send(byte[] bytes, final int timeout) {
        final Object lock = new Object();
        this.ret_code = 1;
        this.terminate_count = 0;
        this.terminate_type = 0;
        this.terminate_buffer.clear();

        this.ret = false;
        SerialPortUtil.sendData(this.com, bytes);
        Thread t = new Thread(() -> {
            synchronized (lock) {
                for (int i = timeout * 100; i > 0; --i) {
                    if (this.ret) {
                        if (this.ret_val[0] == 9)
                            break;
                        else if (this.ret_val[0] == 8) {
                            Com.this.ret = false;
                            break;
                        }
                    }

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
                    }
                }
            }
        });
        t.start();

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            Logger.getLogger("MAIN").log(Level.SEVERE, null, e);
        }

        synchronized (lock) {
            return this.ret;
        }
    }

    public boolean fa_song_fe(byte[] bytes, int timeout) {
        this.ret_code = 4;
        this.terminate_count = 0;
        this.terminate_type = 3;
        this.terminate_buffer.clear();
        this.ret = false;
        SerialPortUtil.sendData(this.com, bytes);
        return true;
    }

    public boolean send_settings(byte[] bytes, int timeout) {
        this.ret_code = 4;
        this.ret = false;
        this.terminate_count = 0;
        this.terminate_type = 3;
        this.terminate_buffer.clear();
        SerialPortUtil.sendData(this.com, bytes);
        return true;
    }
}
