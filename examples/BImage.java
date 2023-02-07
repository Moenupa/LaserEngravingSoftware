package examples;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.Arrays;

public class BImage {
    public static BufferedImage zoomImage(BufferedImage im, double resizeTimes) {
        BufferedImage result = null;

        try {
            int width = im.getWidth();
            int height = im.getHeight();
            int toWidth = (int) ((double) width * resizeTimes);
            int toHeight = (int) ((double) height * resizeTimes);
            result = new BufferedImage(toWidth, toHeight, 2);
            result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, 4), 0, 0, null);
        } catch (Exception var8) {
            System.out.println("创建缩略图发生异常" + var8.getMessage());
        }

        return result;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        return (((alpha << 8) + red << 8) + green << 8) + blue;
    }

    public static BufferedImage greyScale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int color = image.getRGB(i, j);
                int r = color >> 16 & 255;
                int g = color >> 8 & 255;
                int b = color & 255;
                int gray = (int) ((double) (r + g + b) / 3.0D);
                grayImage.setRGB(i, j, colorToRGB(0, gray, gray, gray));
            }
        }

        return grayImage;
    }

    public static int limitBound(int p) {
        if (p > 255) return 255;
        return Math.max(p, 0);
    }

    public static BufferedImage convertGreyImgByFloyd(BufferedImage img, int zhi) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);
        int[] gray = new int[height * width];

        int p1, p2;
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                p1 = pixels[width * h + w];
                p2 = ((p1 & 0xff0000) >> 16) + (128 - zhi);
                gray[width * h + w] = limitBound(p2);
            }
        }
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                p1 = gray[width * h + w];
                if (p1 >= 128) {
                    pixels[width * h + w] = 0x1ffffff;
                    p2 = p1 - 255;
                } else {
                    pixels[width * h + w] = -16777216;
                    p2 = p1;
                }

                if (w < width - 1 && h < height - 1) {
                    gray[width * h + w + 1] += 3 * p2 / 8;
                    gray[width * (h + 1) + w] += 3 * p2 / 8;
                    gray[width * (h + 1) + w + 1] += p2 / 4;
                } else if (w == width - 1 && h < height - 1) {
                    gray[width * (h + 1) + w] += 3 * p2 / 8;
                } else if (w < width - 1 && h == height - 1) {
                    gray[width * h + w + 1] += p2 / 4;
                }
            }
        }

        BufferedImage mBitmap = new BufferedImage(width, height, 2);
        mBitmap.setRGB(0, 0, width, height, pixels, 0, width);
        return mBitmap;
    }

    public static BufferedImage blackAndWhite(BufferedImage bb, int zhi) {
        BufferedImage nb = bb.getSubimage(0, 0, bb.getWidth(), bb.getHeight());
        int[] pixels = new int[bb.getWidth() * bb.getHeight()];
        nb.getRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels, 0, nb.getWidth());

        for (int i = 0; i < pixels.length; ++i) {
            int clr = pixels[i];
            int red = (clr & 16711680) >> 16;
            if (red < zhi) {
                pixels[i] = -16777216;
            } else {
                pixels[i] = 33554431;
            }
        }

        BufferedImage mBitmap = new BufferedImage(nb.getWidth(), nb.getHeight(), 2);
        mBitmap.setRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels, 0, nb.getWidth());
        return mBitmap;
    }

    public static BufferedImage fanse(BufferedImage bb) {
        BufferedImage nb = bb.getSubimage(0, 0, bb.getWidth(), bb.getHeight());
        int[] pixels = new int[bb.getWidth() * bb.getHeight()];
        nb.getRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels, 0, nb.getWidth());

        for (int i = 0; i < pixels.length; ++i) {
            int clr = pixels[i];
            int red = (clr & 16711680) >> 16;
            if (red == 255) {
                pixels[i] = -16777216;
            } else {
                pixels[i] = 33554431;
            }
        }

        BufferedImage mBitmap = new BufferedImage(nb.getWidth(), nb.getHeight(), 2);
        mBitmap.setRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels, 0, nb.getWidth());
        return mBitmap;
    }

    public static BufferedImage mirror_x(BufferedImage bb) {
        BufferedImage nb = bb.getSubimage(0, 0, bb.getWidth(), bb.getHeight());
        int[] pixels = new int[bb.getWidth() * bb.getHeight()];
        int[] pixels2 = new int[bb.getWidth() * bb.getHeight()];
        nb.getRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels, 0, nb.getWidth());
        int k = nb.getWidth();
        int g = nb.getHeight();

        for (int i = 0; i < g; ++i) {
            for (int j = 0; j < k; ++j) {
                pixels2[i * k + j] = pixels[i * k + (k - j - 1)];
            }
        }

        BufferedImage mBitmap = new BufferedImage(nb.getWidth(), nb.getHeight(), 2);
        mBitmap.setRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels2, 0, nb.getWidth());
        return mBitmap;
    }

    public static BufferedImage mirror_y(BufferedImage bb) {
        BufferedImage nb = bb.getSubimage(0, 0, bb.getWidth(), bb.getHeight());
        int[] pixels = new int[bb.getWidth() * bb.getHeight()];
        int[] pixels2 = new int[bb.getWidth() * bb.getHeight()];
        nb.getRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels, 0, nb.getWidth());
        int k = nb.getWidth();
        int g = nb.getHeight();

        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < g; ++j) {
                pixels2[j * k + i] = pixels[(g - j - 1) * k + i];
            }
        }

        BufferedImage mBitmap = new BufferedImage(nb.getWidth(), nb.getHeight(), 2);
        mBitmap.setRGB(0, 0, nb.getWidth(), nb.getHeight(), pixels2, 0, nb.getWidth());
        return mBitmap;
    }

    public static BufferedImage qu_lunkuo(BufferedImage img, int zhi) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        int[] pixels2 = new int[(width + 2) * (height + 2)];
        Arrays.fill(pixels2, 16777215);
        img.getRGB(0, 0, width, height, pixels, 0, width);
        int[] gray = new int[(height + 2) * (width + 2)];

        int i;
        int j;
        for (i = 0; i < height + 2; ++i) {
            for (j = 0; j < width + 2; ++j) {
                if (i != 0 && j != 0 && j != width + 1 && i != height + 1) {
                    int grey = pixels[width * (i - 1) + j - 1];
                    int red = (grey & 16711680) >> 16;
                    if (red > zhi) {
                        gray[(width + 2) * i + j] = 16777215;
                    } else {
                        gray[(width + 2) * i + j] = -16777216;
                    }
                } else {
                    gray[(width + 2) * i + j] = 16777215;
                }
            }
        }

        height += 2;
        width += 2;

        for (i = 1; i < height; ++i) {
            for (j = 1; j < width; ++j) {
                if (gray[width * i + j] != gray[width * i + (j - 1)]) {
                    if (gray[width * i + j] == -16777216) {
                        pixels2[width * i + j] = -16777216;
                    } else {
                        pixels2[width * i + (j - 1)] = -16777216;
                    }
                }

                if (gray[width * i + j] != gray[width * (i - 1) + j]) {
                    if (gray[width * i + j] == -16777216) {
                        pixels2[width * i + j] = -16777216;
                    } else {
                        pixels2[width * (i - 1) + j] = -16777216;
                    }
                }
            }
        }

        BufferedImage mBitmap = new BufferedImage(width, height, 2);
        mBitmap.setRGB(0, 0, width, height, pixels2, 0, width);
        return mBitmap;
    }

    static int[] getGray(int[] pixels, int width, int height) {
        int[] gray = new int[width * height];

        for (int i = 0; i < width - 1; ++i) {
            for (int j = 0; j < height - 1; ++j) {
                int index = width * j + i;
                int rgba = pixels[index];
                int g = ((rgba & 0xff0000) >> 16) * 3 + ((rgba & '\uff00') >> 8) * 6 + (rgba & 255);
                gray[index] = g / 10;
            }
        }

        return gray;
    }

    static int[] getInverse(int[] gray) {
        int[] inverse = new int[gray.length];
        int i = 0;

        for (int size = gray.length; i < size; ++i) {
            inverse[i] = 255 - gray[i];
        }

        return inverse;
    }

    static int[] guassBlur(int[] inverse, int width, int height) {
        int[] guassBlur = new int[inverse.length];

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int temp = width * j + i;
                if (i != 0 && i != width - 1 && j != 0 && j != height - 1) {
                    int i0 = width * (j - 1) + (i - 1);
                    int i1 = width * (j - 1) + i;
                    int i2 = width * (j - 1) + i + 1;
                    int i3 = width * j + (i - 1);
                    int i4 = width * j + i;
                    int i5 = width * j + i + 1;
                    int i6 = width * (j + 1) + (i - 1);
                    int i7 = width * (j + 1) + i;
                    int i8 = width * (j + 1) + i + 1;
                    int sum = inverse[i0] + 2 * inverse[i1] + inverse[i2] + 2 * inverse[i3] + 4 * inverse[i4] + 2 * inverse[i5] + inverse[i6] + 2 * inverse[i7] + inverse[i8];
                    sum /= 16;
                    guassBlur[temp] = sum;
                } else {
                    guassBlur[temp] = 0;
                }
            }
        }

        return guassBlur;
    }

    static int[] deceasecolorCompound(int[] guassBlur, int[] gray, int width, int height) {
        int[] output = new int[guassBlur.length];

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int index = j * width + i;
                int b = guassBlur[index];
                int a = gray[index];
                int temp = a + a * b / (256 - b);
                float ex = (float) (temp * temp) / 255.0F / 255.0F;
                temp = (int) ((float) temp * ex);
                a = Math.min(temp, 255);
                output[index] = a;
            }
        }

        return output;
    }

    static BufferedImage create(int[] pixels, int[] output, int width, int height) {
        int i = 0;

        for (int size = pixels.length; i < size; ++i) {
            int gray = output[i];
            int pixel = pixels[i] & -16777216 | gray << 16 | gray << 8 | gray;
            output[i] = pixel;
        }

        BufferedImage mBitmap = new BufferedImage(width, height, 2);
        mBitmap.setRGB(0, 0, width, height, output, 0, width);
        return mBitmap;
    }

    public static BufferedImage su_miao(BufferedImage bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getRGB(0, 0, width, height, pixels, 0, width);
        int[] gray = getGray(pixels, width, height);
        int[] inverse = getInverse(gray);
        int[] guassBlur = guassBlur(inverse, width, height);
        int[] output = deceasecolorCompound(guassBlur, gray, width, height);
        return create(pixels, output, width, height);
    }

    public static BufferedImage su_miao2(BufferedImage old) {
        BufferedImage b1 = discolor(old);
        b1 = invert(b1);
        float[][] matric = gaussian2DKernel(3, 3.0F);
        b1 = convolution(b1, matric);
        b1 = deceaseColorCompound(old, b1);
        ColorSpace cs1 = ColorSpace.getInstance(1003);
        ColorConvertOp op1 = new ColorConvertOp(cs1, null);
        BufferedImage b2 = new BufferedImage(old.getWidth(), old.getHeight(), 1);
        op1.filter(b1, b2);
        return b2;
    }

    public static BufferedImage discolor(BufferedImage sourceImage) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        BufferedImage retImage = new BufferedImage(width, height, 2);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int color1 = sourceImage.getRGB(i, j);
                int a1 = color1 >> 24 & 255;
                int r1 = color1 >> 16 & 255;
                int g1 = color1 >> 8 & 255;
                int b1 = color1 & 255;
                double sumR;
                double sumG;
                double sumB;
                sumR = sumG = sumB = (double) r1 * 0.299D + (double) g1 * 0.587D + (double) b1 * 0.114D;
                int result = (int) (double) a1 << 24 | (int) sumR << 16 | (int) sumG << 8 | (int) sumB;
                retImage.setRGB(i, j, result);
            }
        }

        return retImage;
    }

    public static BufferedImage invert(BufferedImage sourceImage) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        BufferedImage retImage = new BufferedImage(width, height, 2);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int color1 = sourceImage.getRGB(i, j);
                int a1 = color1 >> 24 & 255;
                int r1 = color1 >> 16 & 255;
                int g1 = color1 >> 8 & 255;
                int b1 = color1 & 255;
                int r = 255 - r1;
                int g = 255 - g1;
                int b = 255 - b1;

                retImage.setRGB(i, j, limitBound(a1 << 24 | r << 16 | g << 8 | b));
            }
        }

        return retImage;
    }

    public static BufferedImage deceaseColorCompound(BufferedImage sourceImage, BufferedImage targetImage) {
        int width = Math.max(sourceImage.getWidth(), targetImage.getWidth());
        int height = Math.max(sourceImage.getHeight(), targetImage.getHeight());
        BufferedImage retImage = new BufferedImage(width, height, 2);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (i < sourceImage.getWidth() && j < sourceImage.getHeight()) {
                    if (i < targetImage.getWidth() && j < targetImage.getHeight()) {
                        int color1 = sourceImage.getRGB(i, j);
                        int color2 = targetImage.getRGB(i, j);
                        int a1 = color1 >> 24 & 255;
                        int r1 = color1 >> 16 & 255;
                        int g1 = color1 >> 8 & 255;
                        int b1 = color1 & 255;
                        int a2 = color2 >> 24 & 255;
                        int r2 = color2 >> 16 & 255;
                        int g2 = color2 >> 8 & 255;
                        int b2 = color2 & 255;
                        int a = deceaseColorChannel(a1, a2);
                        int r = deceaseColorChannel(r1, r2);
                        int g = deceaseColorChannel(g1, g2);
                        int b = deceaseColorChannel(b1, b2);
                        int result = a << 24 | r << 16 | g << 8 | b;
                        retImage.setRGB(i, j, result);
                    } else {
                        retImage.setRGB(i, j, sourceImage.getRGB(i, j));
                    }
                } else if (i < targetImage.getWidth() && j < targetImage.getHeight()) {
                    retImage.setRGB(i, j, targetImage.getRGB(i, j));
                } else {
                    retImage.setRGB(i, j, 0);
                }
            }
        }

        return retImage;
    }

    private static int deceaseColorChannel(int source, int target) {
        int result = source + source * target / (256 - target);
        return Math.min(result, 255);
    }

    public static BufferedImage convolution(BufferedImage image, float[][] kernel) {
        int width = image.getWidth();
        int height = image.getHeight();
        int radius = kernel.length / 2;
        BufferedImage retImage = new BufferedImage(width, height, 2);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                double sumA = 0.0D;
                double sumR = 0.0D;
                double sumG = 0.0D;
                double sumB = 0.0D;

                int x;
                for (x = i - radius; x <= i + radius; ++x) {
                    for (int y = j - radius; y <= j + radius; ++y) {
                        int posX = x < 0 ? 0 : (x >= width ? width - 1 : x);
                        int posY = y < 0 ? 0 : (y >= height ? height - 1 : y);
                        int color = image.getRGB(posX, posY);
                        int a = color >> 24 & 255;
                        int r = color >> 16 & 255;
                        int g = color >> 8 & 255;
                        int b = color & 255;
                        int kelX = x - i + radius;
                        int kelY = y - j + radius;
                        sumA += kernel[kelX][kelY] * (float) a;
                        sumR += kernel[kelX][kelY] * (float) r;
                        sumG += kernel[kelX][kelY] * (float) g;
                        sumB += kernel[kelX][kelY] * (float) b;
                    }
                }

                x = (int) sumA << 24 | (int) sumR << 16 | (int) sumG << 8 | (int) sumB;
                retImage.setRGB(i, j, x);
            }
        }

        return retImage;
    }

    public static float[][] gaussian2DKernel(int radius, float sigma) {
        int length = 2 * radius;
        float[][] matric = new float[length + 1][length + 1];
        float sigmaSquare2 = 2.0F * sigma * sigma;
        float sum = 0.0F;

        int x;
        int y;
        for (x = -radius; x <= radius; ++x) {
            for (y = -radius; y <= radius; ++y) {
                matric[radius + x][radius + y] = (float) (Math.pow(2.718281828459045D, (float) (-(x * x + y * y)) / sigmaSquare2) / (3.141592653589793D * (double) sigmaSquare2));
                sum += matric[radius + x][radius + y];
            }
        }

        for (x = 0; x < length; ++x) {
            for (y = 0; y < length; ++y) {
                matric[x][y] /= sum;
            }
        }

        return matric;
    }
}
