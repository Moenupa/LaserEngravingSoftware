package examples;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;
import javax.swing.JSlider;

public class Wang {
   BufferedInputStream bis;
   BufferedOutputStream bos;
   Wang.ReadThread readThread;
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
   public Hua_ban hb = null;
   public JComboBox fbl = null;
   public JSlider rg = null;
   public JProgressBar jdt = null;
   public mainJFrame win = null;
   byte[] jie_shou = new byte[100];
   int jie_shou_len = 0;
   boolean zhu_dong = true;
   Object jie_shou_suo = new Object();
   byte[] jie_shou2 = new byte[100];
   int jie_shou_len2 = 0;

   public Wang() {
      this.fw_kai();
   }

   public void xin_tiao() {
      Runnable runnable2 = new Runnable() {
         public void run() {
            while(true) {
               if (!Hua_ban.kuang && !mainJFrame.kai_shi && !Wang.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var4) {
                     Logger.getLogger(Wang.class.getName()).log(Level.SEVERE, (String)null, var4);
                  }

                  if (!Wang.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                     try {
                        Thread.sleep(100L);
                     } catch (InterruptedException var3) {
                        Logger.getLogger(Wang.class.getName()).log(Level.SEVERE, (String)null, var3);
                     }

                     if (!Wang.this.xie2(new byte[]{11, 0, 4, 0}, 100)) {
                        Wang.this.guan_bi();
                        return;
                     }
                  }
               }

               try {
                  Thread.sleep(6000L);
               } catch (InterruptedException var2) {
                  Logger.getLogger(Wang.class.getName()).log(Level.SEVERE, (String)null, var2);
               }
            }
         }
      };
      Thread thread2 = new Thread(runnable2);
      thread2.start();
   }

   public boolean lianjie(byte[] data, int chao) {
      this.fanhui = null;
      int m = 0;
      if (!this.lian_jie) {
         return false;
      } else {
         this.data_w = data;
         Wang.WriteRead writeRead = new Wang.WriteRead();
         writeRead.start();
         this.jie_shou_len = 0;
         this.zhu_dong = true;

         while(this.jie_shou_len < 1 && m++ <= chao) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }

         synchronized(this.jie_shou_suo) {
            this.zhu_dong = false;
            if (this.jie_shou_len < 1) {
               return false;
            } else if (this.jie_shou[0] == 9) {
               return true;
            } else {
               this.jie_shou_len = 0;
               return false;
            }
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
         Wang.WriteRead writeRead = new Wang.WriteRead();
         writeRead.start();
         this.jie_shou_len = 0;
         this.zhu_dong = true;

         while(this.jie_shou_len < 1 && m++ <= chao) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }

         synchronized(this.jie_shou_suo) {
            this.zhu_dong = false;
            if (this.jie_shou_len < 1) {
               return false;
            } else if (this.jie_shou[0] == 9) {
               return true;
            } else {
               this.jie_shou_len = 0;
               return false;
            }
         }
      }
   }

   public byte[] ban_ben(byte[] data, int chao) {
      this.fanhui = null;
      int m = 0;
      if (!this.lian_jie) {
         return null;
      } else {
         this.data_w = data;
         Wang.WriteRead writeRead = new Wang.WriteRead();
         writeRead.start();
         this.jie_shou_len = 0;
         this.zhu_dong = true;

         while(this.jie_shou_len < 3 && m++ <= chao) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var9) {
               var9.printStackTrace();
            }
         }

         synchronized(this.jie_shou_suo) {
            this.zhu_dong = false;
            if (this.jie_shou_len < 3) {
               this.jie_shou_len = 0;
               return null;
            } else {
               byte[] fh = new byte[]{this.jie_shou[0], this.jie_shou[1], this.jie_shou[2]};
               this.jie_shou_len = 0;
               return fh;
            }
         }
      }
   }

   public boolean kaishi(byte[] data, int chao) {
      this.fanhui = null;
      int m = 0;
      if (!this.lian_jie) {
         return false;
      } else {
         this.data_w = data;
         Wang.WriteRead writeRead = new Wang.WriteRead();
         writeRead.start();
         this.jie_shou_len = 0;
         this.zhu_dong = true;

         while(this.jie_shou_len < 4 && m++ <= chao) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }

         synchronized(this.jie_shou_suo) {
            this.zhu_dong = false;
            if (this.jie_shou_len < 4) {
               return false;
            } else if (this.jie_shou[0] == -1 && this.jie_shou[1] == -1 && this.jie_shou[2] == -1 && this.jie_shou[3] == -2) {
               return true;
            } else {
               this.jie_shou_len = 0;
               return false;
            }
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
         Wang.WriteRead writeRead = new Wang.WriteRead();
         writeRead.start();
         this.jie_shou_len = 0;
         this.zhu_dong = true;

         while(this.jie_shou_len < 1 && m++ <= chao) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }

         synchronized(this.jie_shou_suo) {
            this.zhu_dong = false;
            if (this.jie_shou_len < 1) {
               return false;
            } else if (this.jie_shou[0] == 9) {
               return true;
            } else {
               this.jie_shou_len = 0;
               return false;
            }
         }
      }
   }

   private void fw_kai() {
      (new Thread(new Runnable() {
         public void run() {
            try {
               Wang.this.fu_wu = new ServerSocket(12346);
               Wang.this.ke_hu = Wang.this.fu_wu.accept();
               Wang.this.bis = new BufferedInputStream(Wang.this.ke_hu.getInputStream());
               Wang.this.bos = new BufferedOutputStream(Wang.this.ke_hu.getOutputStream());
               Wang.this.readThread = Wang.this.new ReadThread();
               Wang.this.readThread.start();
               Wang.this.lian_jie = true;
               if (Wang.this.bt != null) {
                  byte[] fh = null;
                  int var2 = 0;

                  do {
                     if (var2++ > 3) {
                        Wang.this.guan_bi();
                        return;
                     }

                     Thread.sleep(500L);
                  } while(!Wang.this.lianjie(new byte[]{10, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100));

                  byte[] fhx = Wang.this.ban_ben(new byte[]{-1, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0}, 100);
                  if (fhx != null) {
                     Wang.this.hb.ban_ben(fhx, 2);
                  }

                  Thread.sleep(500L);
                  Wang.this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi.png")));
                  int rg2 = Wang.this.rg.getValue() * 2;
                  int jd = Wang.this.fbl.getSelectedIndex();
                  Hua_ban.fen_bian_lv = 0.05D + (double)Wang.this.fbl.getSelectedIndex() * 0.0125D;
                  Wang.this.hb.di_tu();
                  Wang.this.lianjie(new byte[]{40, 0, 11, (byte)rg2, (byte)jd, 0, 0, 0, 0, 0, 0}, 200);
               }
            } catch (Exception var6) {
               var6.printStackTrace();
               Wang.this.lian_jie = false;
               if (Wang.this.bt != null) {
                  Wang.this.bt.setIcon(new ImageIcon(this.getClass().getResource("/tu/wifi2.png")));
               }
            }

         }
      })).start();
      (new Thread(new Runnable() {
         public void run() {
            while(!Wang.this.lian_jie) {
               try {
                  Wang.this.udp = new DatagramSocket();
                  Wang.this.serverAddress = InetAddress.getByName("255.255.255.255");
                  byte[] data = ("IPjiakuo\"" + Wang.this.qu_ip() + "\",12346\r\n").getBytes();
                  byte[] data2 = new byte[data.length + 3];
                  data2[0] = 2;
                  data2[1] = (byte)(data2.length >> 8);
                  data2[2] = (byte)data2.length;

                  for(int i = 0; i < data.length; ++i) {
                     data2[i + 3] = data[i];
                  }

                  DatagramPacket packet = new DatagramPacket(data2, data2.length);
                  packet.setSocketAddress(new InetSocketAddress("255.255.255.255", 12345));
                  Wang.this.udp.setBroadcast(true);
                  Wang.this.udp.send(packet);
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
         Logger.getLogger(Wang.class.getName()).log(Level.SEVERE, (String)null, var3);
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
            Logger.getLogger(Wang.class.getName()).log(Level.SEVERE, (String)null, var3);
         }

         this.fu_wu = null;
      }

      if (this.ke_hu != null) {
         try {
            this.ke_hu.close();
         } catch (IOException var2) {
            Logger.getLogger(Wang.class.getName()).log(Level.SEVERE, (String)null, var2);
         }

         this.ke_hu = null;
      }

      if (this.win.wang != null) {
         this.win.wang = null;
      }

   }

   private class WriteRead extends Thread {
      private WriteRead() {
      }

      public void run() {
         if (!Wang.this.mang) {
            Wang.this.mang = true;
            Wang.this.xin_tiao = 0;

            try {
               Wang.this.bos.write(Wang.this.data_w);
               Wang.this.bos.flush();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

            Wang.this.mang = false;
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
         while(true) {
            try {
               int count = 0;
               if (Wang.this.bis != null) {
                  count = Wang.this.bis.available();
               }

               if (count > 0) {
                  int i;
                  if (Wang.this.zhu_dong) {
                     synchronized(Wang.this.jie_shou_suo) {
                        i = Wang.this.bis.read(Wang.this.data_r);

                        for(int ix = 0; ix < i; ++ix) {
                           Wang.this.jie_shou[Wang.this.jie_shou_len++] = Wang.this.data_r[ix];
                        }
                     }
                  } else {
                     if (Wang.this.ke_hu.isClosed()) {
                        return;
                     }

                     int f = Wang.this.bis.read(Wang.this.data_r);

                     for(i = 0; i < f; ++i) {
                        Wang.this.jie_shou2[Wang.this.jie_shou_len2++] = Wang.this.data_r[i];
                     }

                     if (Wang.this.jie_shou_len2 > 3) {
                        Wang.this.jie_shou_len2 = 0;
                        if (Wang.this.jie_shou2[0] == -1 && Wang.this.jie_shou2[1] == -1 && Wang.this.jie_shou2[2] == 0 && Wang.this.win != null && (Wang.this.win.com_dakai || Wang.this.lian_jie)) {
                           mainJFrame var10000 = Wang.this.win;
                           mainJFrame.kai_shi2 = true;
                           Wang.this.jdt.setValue(Wang.this.jie_shou2[3]);
                           Wang.this.jdt.setVisible(true);
                           mainJFrame.kai_shi = true;
                           var10000 = Wang.this.win;
                           mainJFrame.chaoshi = 0;
                        }
                     } else {
                        Wang.this.fan_hui_ma = f;
                        Wang.this.xin_tiao = 0;
                     }
                  }
               }

               Thread.sleep(10L);
            } catch (IOException var7) {
               var7.printStackTrace();
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }
      }

      // $FF: synthetic method
      ReadThread(Object x1) {
         this();
      }
   }
}
