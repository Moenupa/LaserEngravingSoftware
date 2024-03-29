package engraver.control;

import engraver.Main;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

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
                byte[] data = SerialPortUtil.readData(this.com);
                switch (this.terminate_type) {
                    case 0 -> {
                        this.ret = true;
                        this.ret_val = data;
                    }
                    case 2 -> {
                        // add ret_val -> terminate buffer
                        this.terminate_count += data.length;
                        for (byte b : data) {
                            this.terminate_buffer.add(b);
                        }

                        // if count >= 3, mv first 3 -> this.ret_val
                        if (this.terminate_count >= 3) {
                            this.ret_val = new byte[3];
                            IntStream.range(0, 3).forEach(i -> this.ret_val[i] = this.terminate_buffer.get(i));
                            this.ret = true;
                        }
                    }
                    case 3 -> {
                        // add ret_val -> terminate buffer
                        for (byte b : data) {
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
                            Main.engraveStarted = true;
                            Main.engraveFinished = true;
                            this.terminate_buffer.clear();
                        }
                        System.out.println(data.length);
                        Main.timeout = 0;
                    }
                }
            } catch (Exception e) {
                Logger.getLogger("COM").log(Level.SEVERE, null, e);
            }
        }
    };
    SerialPortEventListener terminate = (event) -> {
        if (event.getEventType() != 1)
            return;

        synchronized (this) {
            try {
                if (this.com != null) {
                    byte[] data = SerialPortUtil.readData(this.com);
                    byte[] ret = new byte[this.ret_code];

                    System.arraycopy(data, 0, ret, 0, ret.length);

                    if (ret.length == 4) {
                        if (ret[0] == -1 && ret[1] == -1 && ret[2] == 0) {
                            this.jdt.setValue(ret[3]);
                            this.jdt.setVisible(true);
                            Main.engraveStarted = true;
                            Main.engraveFinished = true;
                            Main.timeout = 0;
                        } else {
                            if (ret[0] == -1 && ret[1] == -1 && ret[2] == -1 && ret[3] == -1) {
                                this.jdt.setValue(0);
                                this.jdt.setVisible(false);
                                Main.engraveStarted = false;
                                Main.engraveFinished = false;
                                Main.timeout = 0;
                            } else {
                                this.ret = true;
                                this.ret_val = ret;
                            }
                        }
                    } else {
                        this.ret = true;
                        this.ret_val = ret;
                    }
                }
            } catch (Exception e) {
                Logger.getLogger("COM").log(Level.SEVERE, null, e);
            }
        }
    };

    public Com(SerialPort port) {
        this.terminate_buffer.add((byte) 1);
        synchronized (this) {
            this.com = port;
            try {
                String osName = System.getProperties().getProperty("os.name");
                if (osName.contains("Win")) {
                    SerialPortUtil.setListenerToSerialPort(this.com, this.terminate_win);
                } else {
                    SerialPortUtil.setListenerToSerialPort(this.com, this.terminate);
                }
            } catch (Exception e) {
                Logger.getLogger("COM").log(Level.SEVERE, null, e);
            }
        }
    }

    public void close() {
        if (this.com != null) this.com.close();
    }

    /**
     * Read firmware version from serial port
     * Return codes:
     * <pre>
     * ret_code=3: version
     * ret_val   : byte[3]</pre>
     *
     * @return true if success
     */
    public boolean read_version() {
        final Object lock = new Object();
        this.terminateWith(2, 0);
        this.returnWith(3, false);

        SerialPortUtil.sendData(this.com, new byte[]{-1, 0, 4, 0});
        new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i < 200; i++) {
                    if (this.ret) {
                        if (this.ret_val.length != 3)
                            this.ret = false;
                        break;
                    }

                    try {
                        Thread.sleep(10L);
                    } catch (Exception e) {
                        Logger.getLogger("COM").log(Level.SEVERE, null, e);
                    }
                }
            }
        }).start();

        try {
            Thread.sleep(100L);
        } catch (Exception e) {
            Logger.getLogger("COM").log(Level.SEVERE, null, e);
        }

        synchronized (lock) {
            return this.ret;
        }
    }

    /**
     * Send data to the serial port.
     * Return codes:
     * <pre>
     * ret_code=1: lost
     * ret_code=8: fail
     * ret_code=9: success
     * else      : unknown</pre>
     *
     * @param data    data to send
     * @param timeout timeout
     * @return true if successful
     */
    public boolean send(byte[] data, final int timeout) {
        final Object lock = new Object();
        this.terminateWith(0, 0);
        this.returnWith(1, false);

        SerialPortUtil.sendData(this.com, data);
        new Thread(() -> {
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
                        Logger.getLogger("COM").log(Level.SEVERE, null, e);
                    }
                }
            }
        }).start();

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            Logger.getLogger("COM").log(Level.SEVERE, null, e);
        }

        synchronized (lock) {
            return this.ret;
        }
    }

    /**
     * send message to serial port
     * tells machine to go offline
     *
     * @param data    message
     * @param timeout timeout
     * @return true
     */
    public boolean send_offline(byte[] data, int timeout) {
        this.terminateWith(3, 0);
        this.returnWith(4, false);
        SerialPortUtil.sendData(this.com, data);
        return true;
    }

    public void terminateWith(int type, int count) {
        this.terminate_type = type;
        this.terminate_count = count;
        this.terminate_buffer.clear();
    }

    public void returnWith(int ret_code, boolean ret) {
        this.ret = ret;
        this.ret_code = ret_code;
    }
}
