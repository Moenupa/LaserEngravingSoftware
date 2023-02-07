package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Geng_xin {
    private static String openFile(String filePath) {
        String ee = new String();

        try {
            URL url = new URL(filePath);
            URLConnection urlconn = url.openConnection();
            urlconn.connect();
            HttpURLConnection httpconn = (HttpURLConnection) urlconn;
            int HttpResult = httpconn.getResponseCode();
            if (HttpResult != 200) {
                System.out.print("无法连接到");
            } else {
                int filesize = urlconn.getContentLength();
                InputStreamReader isReader = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer buffer = new StringBuffer();

                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    buffer.append(line);
                    buffer.append("\r\n");
                }

                System.out.print(buffer.toString());
                ee = buffer.toString();
            }
        } catch (FileNotFoundException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        return ee;
    }

    private static void browse2(String url) throws Exception {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }

    }

    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 != null && version2 != null) {
            String[] versionArray1 = version1.split("\\.");
            String[] versionArray2 = version2.split("\\.");
            int idx = 0;
            int minLength = Math.min(versionArray1.length, versionArray2.length);

            int diff;
            for (diff = 0; idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0 && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0; ++idx) {
            }

            diff = diff != 0 ? diff : versionArray1.length - versionArray2.length;
            return diff;
        } else {
            throw new Exception("compareVersion error:illegal params.");
        }
    }

    public static void geng_xin() {
        Runnable runnable2 = new Runnable() {
            public void run() {
                String gx = Geng_xin.openFile("http://jiakuo25.0594.bftii.com/geng_xin.txt");
                String[] strArr = gx.split("\r\n");
                if (strArr.length > 1 && strArr[0].equals("1")) {
                    try {
                        if (Geng_xin.compareVersion(strArr[1].toUpperCase(), mainJFrame.software_version.toUpperCase()) > 0) {
                            int n = JOptionPane.showConfirmDialog((Component) null, mainJFrame.str_outdated, "", 0);
                            if (n == 0) {
                                try {
                                    Properties props = System.getProperties();
                                    String osName = props.getProperty("os.name");
                                    if (osName.contains("Win")) {
                                        Geng_xin.browse2("http://jiakuo25.0594.bftii.com/Laser_java_win.zip");
                                    } else {
                                        Geng_xin.browse2("http://jiakuo25.0594.bftii.com/Laser_java_mac.zip");
                                    }
                                } catch (TooManyListenersException var6) {
                                    Logger.getLogger(Com.class.getName()).log(Level.SEVERE, (String) null, var6);
                                }
                            }
                        }
                    } catch (Exception var7) {
                        Logger.getLogger(Geng_xin.class.getName()).log(Level.SEVERE, (String) null, var7);
                    }
                }

            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();
    }
}
