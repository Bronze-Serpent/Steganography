package utils;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ColorUtils
{

    private static final double[] COLOR_RATIO = new double[]{0.299, 0.587, 0.114};


    public static double calcBrightness(Color c)
    {
        return COLOR_RATIO[0] * c.getRed() + COLOR_RATIO[1] * c.getGreen() + COLOR_RATIO[2] * c.getBlue();
    }


    public static double calcAvgChannelValInArea(BufferedImage img, Channel channel, final int x, final int y, int q)
    {
        int sumChannels = 0;

        for (int i = 0, currY = y + q; i <= q * 2; i++)
        {
            if (currY != y)
            {
                if (isCoordinateAvailable(img, x, currY))
                    sumChannels += getChannelVal(img.getRGB(x, currY), channel);
            }
            else
                {
                    for (int j = 0, currX = x - q; j <= q * 2; j++)
                    {
                        if (isCoordinateAvailable(img, currX, currY))
                            sumChannels += getChannelVal(img.getRGB(currX, currY), channel);

                        currX++;
                    }
                }
            currY--;
        }

        return Math.round(sumChannels / (4.0 * q + 1));
    }


    public static int getChannelVal(int rgb, Channel channel)
    {
        return switch (channel) {
            case RED -> (rgb >>> 16) & 0xFF;
            case GREEN -> (rgb >>> 8) & 0xFF;
            case BLUE -> rgb & 0xFF;
        };
    }


    private static boolean isCoordinateAvailable(BufferedImage img, int x, int y)
    {
        return x > -1 && x < img.getWidth() && y > -1 && y < img.getHeight();
    }
}
