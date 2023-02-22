package engraver.control;

import engraver.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.Desktop.Action;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Update {
    private static String getURLContents(String urlStr) throws Exception {
        try {
            URL url = new URL(urlStr);
            Scanner s = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();

            while (s.hasNext()) {
                sb.append(s.next());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void browse2(String url) throws Exception {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }
    }

    public static int compareVersion(String v1, String v2) throws Exception {
        if (v1 == null || v2 == null)
            throw new Exception("compareVersion error:illegal params.");

        String[] v1Arr = v1.split("\\.");
        String[] v2Arr = v2.split("\\.");
        int i = 0, diff = 0;
        while (
                i < Math.min(v1Arr.length, v2Arr.length)
                && (diff = v1Arr[i].length() - v2Arr[i].length()) == 0
                && (diff = v1Arr[i].compareTo(v2Arr[i])) == 0
        ) {
            ++i;
        }

        diff = diff != 0 ? diff : v1Arr.length - v2Arr.length;
        return diff;
    }

    public static void update() {
        new Thread(() -> {
            try {
                String latest = getURLContents("http://jiakuo25.0594.bftii.com/geng_xin.txt");
                String[] latestVer = latest.split("\r\n");
                if (latestVer.length <= 1 || !latestVer[0].equals("1")) {
                    return;
                }
                if (
                        Update.compareVersion(
                                latestVer[1].toUpperCase(),
                                Main.software_version.toUpperCase()
                        ) <= 0
                ) {
                    return;
                }
                int n = JOptionPane.showConfirmDialog(null, Main.str_outdated, "", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    String osName = System.getProperties().getProperty("os.name");
                    if (osName.contains("Win"))
                        Update.browse2("http://jiakuo25.0594.bftii.com/Laser_java_win.zip");
                    else
                        Update.browse2("http://jiakuo25.0594.bftii.com/Laser_java_mac.zip");
                }
            } catch (Exception e) {
                Logger.getLogger("Update").log(Level.SEVERE, null, e);
            }
        }).start();
    }
}
