package examples.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BPoint {
    public int x;
    public int y;

    public BPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BPoint(BPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public static boolean isPoint(int rgb) {
        return (rgb & 0xff0000) == 0;
    }

    public static double getDistance(BPoint p1, BPoint p2) {
        return getDistance(p1.x, p2.x, p1.y, p2.y);
    }

    public static double getDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static BPoint getNearestPoint(BPoint src, BufferedImage image) {
        List<BPoint> points = new ArrayList<>();
        int width = image.getWidth(), height = image.getHeight();
        int sideMaxLen = Math.max(width, height);

        for (int len = 1; len < sideMaxLen; ++len) {
            int x1 = Math.max(src.x - len, 0);
            int x2 = Math.min(src.x + len, width);
            int y1 = Math.max(src.y - len, 0);
            int y2 = Math.min(src.y + len, height);

            for (int x = x1; x < x2; x++) {
                if (isPoint(image.getRGB(x, y1)))
                    points.add(new BPoint(x, y1));
                if (isPoint(image.getRGB(x, y2)))
                    points.add(new BPoint(x, y2));
            }
            for (int y = y1; y < y2; y++) {
                if (isPoint(image.getRGB(x1, y)))
                    points.add(new BPoint(x1, y));
                if (isPoint(image.getRGB(x2, y)))
                    points.add(new BPoint(x2, y));
            }

            if (points.size() > 0) {
                BPoint nearest = points.get(0);
                for (var p : points) {
                    if (getDistance(src, p) < getDistance(src, nearest))
                        nearest = p;
                }
                return nearest;
            }
        }

        return new BPoint(50000, 0);
    }

    static boolean isNeighbor(BPoint a, BPoint b) {
        return Math.abs(a.x - b.x) <= 2 && Math.abs(a.y - b.y) <= 2;
    }
}
