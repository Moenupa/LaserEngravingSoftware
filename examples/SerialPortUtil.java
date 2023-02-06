package examples;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
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
   static Object suo = new Object();

   public static List<String> getSerialPortList() {
      List<String> systemPorts = new ArrayList();
      Enumeration portList = CommPortIdentifier.getPortIdentifiers();

      while(portList.hasMoreElements()) {
         String portName = ((CommPortIdentifier)portList.nextElement()).getName();
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
      return openSerialPort((SerialPortParameter)parameter, 2000);
   }

   public static SerialPort openSerialPort(SerialPortParameter parameter, int timeout) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
      CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(parameter.getSerialPortName());
      CommPort commPort = portIdentifier.open(parameter.getSerialPortName(), timeout);
      if (commPort instanceof SerialPort) {
         SerialPort serialPort = (SerialPort)commPort;
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

   public static void sendData(SerialPort serialPort, byte[] data) {
      synchronized(suo) {
         OutputStream os = null;

         try {
            os = serialPort.getOutputStream();
            os.write(data);
            os.flush();
         } catch (IOException var15) {
            var15.printStackTrace();
         } finally {
            try {
               if (os != null) {
                  os.close();
               }
            } catch (IOException var14) {
               var14.printStackTrace();
            }

         }

      }
   }

   public static byte[] readData(SerialPort serialPort) {
      InputStream is = null;
      byte[] bytes = null;

      try {
         is = serialPort.getInputStream();

         for(int bufflenth = is.available(); bufflenth != 0; bufflenth = is.available()) {
            bytes = new byte[bufflenth];
            is.read(bytes);
         }
      } catch (IOException var12) {
         var12.printStackTrace();
      } finally {
         try {
            if (is != null) {
               is.close();
            }
         } catch (IOException var11) {
            var11.printStackTrace();
         }

      }

      return bytes;
   }

   public static String readLine(SerialPort serialPort) {
      InputStream is = null;
      StringBuilder sb = new StringBuilder();
      Object var3 = null;

      try {
         is = serialPort.getInputStream();
         char bt = false;
         boolean var5 = false;

         char bt;
         do {
            bt = (char)is.read();
            if (bt == '\uffff') {
               break;
            }

            sb.append(bt);
         } while(bt != '\r');
      } catch (IOException var14) {
         Logger.getLogger(SerialPortUtil.class.getName()).log(Level.SEVERE, (String)null, var14);
      } finally {
         try {
            if (is != null) {
               is.close();
            }
         } catch (IOException var13) {
         }

      }

      return sb.toString();
   }

   public static void setListenerToSerialPort(SerialPort serialPort, SerialPortEventListener listener) throws TooManyListenersException {
      serialPort.addEventListener(listener);
      serialPort.notifyOnDataAvailable(true);
      serialPort.notifyOnBreakInterrupt(true);
   }
}
