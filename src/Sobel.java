import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sobel {
    public static void main(String args[]) throws IOException {

        String filename = "/Users/jcolladosp/projects/SobelFilter/src/lenna.png";

        File file = new File(filename);
        BufferedImage image = ImageIO.read(file);

        int x = image.getWidth();
        int y = image.getHeight();

        int[][] edgeColors = new int[x][y];
        int maxGradient = -1;

        int[][] gxarr = { { -1, 0, 1 }, { -2, 0 , 2}, { -1, 0 , 1} };
        int[][] gyarr = { { -1, -2, -1 }, { 0, 0 , 0}, { 1, 2 , 1} };


        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                int[][] image_pixels = { { image.getRGB(i - 1, j - 1), image.getRGB(i - 1, j), image.getRGB(i - 1, j + 1) },
                                         { image.getRGB(i,j - 1), image.getRGB(i, j) , image.getRGB(i, j + 1)},
                                        { image.getRGB(i + 1, j - 1), image.getRGB(i + 1, j) , image.getRGB(i + 1, j + 1)} };
                int gx = 0;
                int gy = 0;

                for (int a = 0; a < gxarr.length; a++) {
                    for (int b = 0; b < image_pixels.length; b++) {

                        gx += gxarr[a][b] * getGrayScale(image_pixels[a][b]);
                        gy += gyarr[a][b] * getGrayScale(image_pixels[a][b]);

                    }
                }

                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                System.out.println("color : " + edgeColor);

                Color c = new Color(edgeColor,edgeColor,edgeColor);

                image.setRGB(i, j, c.getRGB());
            }
        }

        File outputfile = new File("sobel.png");
        ImageIO.write(image, "png", outputfile);

        System.out.println("max : " + maxGradient);
        System.out.println("Finished");
    }

    public static int  getGrayScale(int rgb) {


        Color c = new Color(rgb);
        int red = (int) (c.getRed() * 0.2126);
        int green = (int) (c.getGreen() * 0.7152);
        int blue = (int) (c.getBlue() * 0.0722);


        return red + green + blue;
    }
}