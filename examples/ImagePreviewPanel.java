package examples;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImagePreviewPanel extends JPanel implements PropertyChangeListener {
    private int width;
    private int height;
    private BufferedImage image;
    private final Color bg;

    public ImagePreviewPanel() {
        this.setPreferredSize(new Dimension(155, -1));
        this.bg = this.getBackground();
    }

    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        if (propertyName.equals("SelectedFileChangedProperty")) {
            File selection = (File) e.getNewValue();
            if (selection == null) {
                return;
            }

            String name = selection.getAbsolutePath().toLowerCase();
            if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".bmp") || name.endsWith(".png")) {
                try {
                    this.image = ImageIO.read(selection);
                    this.scaleImage();
                    this.repaint();
                } catch (IOException var6) {
                    Logger.getLogger(ImagePreviewPanel.class.getName()).log(Level.SEVERE, null, var6);
                }
            }
        }

    }

    private void scaleImage() {
        this.width = this.image.getWidth(this);
        this.height = this.image.getHeight(this);
        double ratio;
        if (this.width >= this.height) {
            ratio = 150.0D / (double) this.width;
            this.width = 150;
            this.height = (int) ((double) this.height * ratio);
        } else if (this.getHeight() > 150) {
            ratio = 150.0D / (double) this.height;
            this.height = 150;
            this.width = (int) ((double) this.width * ratio);
        } else {
            ratio = (double) this.getHeight() / (double) this.height;
            this.height = this.getHeight();
            this.width = (int) ((double) this.width * ratio);
        }

    }

    public void paintComponent(Graphics g) {
        g.setColor(this.bg);
        g.fillRect(0, 0, 155, this.getHeight());
        g.drawImage(this.image, this.getWidth() / 2 - this.width / 2 + 5, this.getHeight() / 2 - this.height / 2, this.width, this.height, this);
    }

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        chooser.setAccessory(preview);
        chooser.addPropertyChangeListener(preview);
        chooser.showOpenDialog(null);
    }
}
