package pw.jcollado.sobelfilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;


public class Sobel {
    public static Bitmap Sobel(Bitmap image) throws URISyntaxException {

        //String filename = uri;

        //File file = new File(new URI(filename));
        //Log.i("uri",uri);
        //Bitmap image = BitmapFactory.decodeFile(uri);

        int x = image.getWidth();
        int y = image.getHeight();

        Bitmap image_output = Bitmap.createBitmap(x,y, Bitmap.Config.RGB_565);


        int[][] edgeColors = new int[x][y];
        int maxGradient = -1;

        int[][] gxarr = { { -1, 0, 1 }, { -2, 0 , 2}, { -1, 0 , 1} };
        int[][] gyarr = { { -1, -2, -1 }, { 0, 0 , 0}, { 1, 2 , 1} };


        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                int[][] image_pixels = { { image.getPixel(i - 1, j - 1), image.getPixel(i - 1, j), image.getPixel(i - 1, j + 1) },
                                         { image.getPixel(i,j - 1), image.getPixel(i, j) , image.getPixel(i, j + 1)},
                                        { image.getPixel(i + 1, j - 1), image.getPixel(i + 1, j) , image.getPixel(i + 1, j + 1)} };
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

                int c = Color.rgb(edgeColor,edgeColor,edgeColor);

                image_output.setPixel(i, j, c);
            }
        }

        System.out.println("max : " + maxGradient);
        System.out.println("Finished");
        return image_output;

    }

    public static int  getGrayScale(int rgb) {


        Color c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            c = Color.valueOf(rgb);

        int red = (int) (c.red() * 0.2126);
        int green = (int) (c.green() * 0.7152);
        int blue = (int) (c.blue() * 0.0722);


        return red + green + blue;
        }
        return 0;
    }
}