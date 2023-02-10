package engraver.control;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialPortUtil {
    static final Object lock = new Object();

    /**
     * Get all serial ports
     *
     * @return a list of all available ports
     */
    public static List<String> getSerialPortList() {
        List<String> systemPorts = new ArrayList<>();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            String portName = ((CommPortIdentifier) portList.nextElement()).getName();
            systemPorts.add(portName);
        }

        return systemPorts;
    }

    public static SerialPort openSerialPort(String serialPortName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        SerialPortParameter parameter = new SerialPortParameter(serialPortName);
        return openSerialPort(parameter);
    }

    public static SerialPort openSerialPort(String serialPortName, int baudRate) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        SerialPortParameter parameter = new SerialPortParameter(serialPortName, baudRate);
        return openSerialPort(parameter);
    }

    public static SerialPort openSerialPort(String serialPortName, int baudRate, int timeout) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        SerialPortParameter parameter = new SerialPortParameter(serialPortName, baudRate);
        return openSerialPort(parameter, timeout);
    }

    public static SerialPort openSerialPort(SerialPortParameter parameter) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        return openSerialPort(parameter, 2000);
    }

    public static SerialPort openSerialPort(SerialPortParameter parameter, int timeout) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(parameter.getSerialPortName());
        CommPort commPort = portIdentifier.open(parameter.getSerialPortName(), timeout);
        if (commPort instanceof SerialPort) {
            SerialPort serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(parameter.getBaudRate(), parameter.getDataBits(), parameter.getStopBits(), parameter.getParity());
            System.out.println("开启串口成功，串口名称：" + parameter.getSerialPortName());
            return serialPort;
        } else {
            throw new NoSuchPortException();
        }
    }

    public static void closeSerialPort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            System.out.println("关闭了串口：" + serialPort.getName());
        }

    }

    /**
     * send data to serial port
     *
     * @param serialPort port object
     * @param data bytes to send
     */
    public static void sendData(SerialPort serialPort, byte[] data) {
        synchronized (lock) {
            try {
                OutputStream os = serialPort.getOutputStream();
                os.write(data);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read data from serial port
     *
     * @param serialPort port object
     * @return data in bytes
     */
    public static byte[] readData(SerialPort serialPort) {
        byte[] bytes = null;
        try {
            InputStream is = serialPort.getInputStream();
            int buffLength;
            do {
                buffLength = is.available();
                bytes = new byte[buffLength];
                is.read(bytes);
            } while (buffLength != 0);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * read a line end by <code>\r</code> or <code>\uffff</code>
     *
     * @param serialPort port object
     * @return data in string
     */
    public static String readLine(SerialPort serialPort) {
        StringBuilder sb = new StringBuilder();
        try {
            char c;

            InputStream is = serialPort.getInputStream();
            do {
                c = (char) is.read();
                if (c == '\uffff')
                    break;
                sb.append(c);
            } while (c != '\r');
            is.close();
        } catch (IOException e) {
            Logger.getLogger("SerialPortUtil").log(Level.SEVERE, null, e);
        }
        return sb.toString();
    }

    public static void setListenerToSerialPort(SerialPort serialPort, SerialPortEventListener listener) throws TooManyListenersException {
        serialPort.addEventListener(listener);
        serialPort.notifyOnDataAvailable(true);
        serialPort.notifyOnBreakInterrupt(true);
    }
}
