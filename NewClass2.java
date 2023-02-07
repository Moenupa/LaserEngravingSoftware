import examples.BElement;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NewClass2 extends JFrame {
   NewClass2.TextureFillPanel panel = null;

   public NewClass2() {
      this.setTitle("纹理填充特效");
      this.setBounds(100, 100, 400, 300);
      this.setDefaultCloseOperation(3);
      this.setResizable(false);
      this.panel = new NewClass2.TextureFillPanel();
      this.add(this.panel);
   }

   public static void main(String[] args) {
      NewClass2 frame = new NewClass2();
      frame.setVisible(true);
   }

   class TextureFillPanel extends JPanel {
      public void paint(Graphics g) {
         Image image2 = (new ImageIcon(this.getClass().getResource("/tu/3.png"))).getImage();
         int kk = image2.getWidth((ImageObserver)null);
         int gg = image2.getHeight((ImageObserver)null);
         BufferedImage image = new BufferedImage(kk, gg, 1);
         Graphics2D g2 = (Graphics2D)image.getGraphics();
         g2.drawImage(image2, 0, 0, kk, gg, (ImageObserver)null);
         Float rect = new Float(0.0F, 0.0F, (float)kk, (float)gg);
         TexturePaint textPaint = new TexturePaint(image, rect);
         Graphics2D graphics2 = (Graphics2D)g;
         graphics2.setPaint(textPaint);
         new Float(45.0F, 25.0F, 200.0F, 200.0F);
         BElement ty = new BElement();
         ty.path.moveTo(121.0F, 0.0F);
         ty.path.lineTo(149.0F, 93.0F);
         ty.path.lineTo(241.0F, 94.0F);
         ty.path.lineTo(169.0F, 149.0F);
         ty.path.lineTo(196.0F, 241.0F);
         ty.path.lineTo(122.0F, 188.0F);
         ty.path.lineTo(46.0F, 241.0F);
         ty.path.lineTo(72.0F, 149.0F);
         ty.path.lineTo(0.0F, 94.0F);
         ty.path.lineTo(92.0F, 93.0F);
         ty.path.closePath();
         graphics2.fill(ty.path);
      }
   }
}
