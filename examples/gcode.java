package examples;

class gcode {
   static double fen_bian_lv = 0.05D;
   static double x_double = 0.0D;
   static double y_double = 0.0D;
   static int bu = 8;
   static gcode.Point[] zhixian = new gcode.Point[0];

   static double[] qu_dian_double(String str) {
      double[] dou = new double[]{0.0D, 0.0D};
      int xw = str.indexOf("X");
      int kw = str.indexOf(" ");
      double ddd = Double.valueOf(str.substring(xw + 1, kw - xw - 1));
      dou[0] = ddd;
      xw = str.indexOf("Y", 0);
      kw = str.indexOf(" ", xw);
      if (kw <= 0) {
         kw = str.length();
      }

      String ss = str.substring(xw + 1, kw - xw - 1);
      int f = ss.indexOf("F", 0);
      if (f >= 1) {
         ss = str.substring(xw + 1, f - 1);
      }

      dou[1] = Double.valueOf(ss);
      return dou;
   }

   static gcode.Point[] jiaru(gcode.Point[] sz, gcode.Point cy) {
      gcode.Point[] fanhui = new gcode.Point[sz.length + 1];

      for(int i = 0; i < sz.length; ++i) {
         fanhui[i] = sz[i];
      }

      fanhui[sz.length] = cy;
      return fanhui;
   }

   class Point {
      int x = 0;
      int y = 0;
   }
}
