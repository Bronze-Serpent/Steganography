package hidersTest;

import java.awt.*;
import java.awt.image.BufferedImage;


public class TestUtils
{
    public static BufferedImage makeImageCopy(BufferedImage imageToCopy)
    {
        BufferedImage result = new BufferedImage(imageToCopy.getWidth(), imageToCopy.getHeight(), imageToCopy.getType());
        Graphics g = result.getGraphics();
        g.drawImage(imageToCopy, 0, 0, null);
        return result;
    }

    static int calcNumOfErr(byte[] arr1, byte[] arr2)
    {
        int numOfErr = 0;
        for (int i = 0; i < arr1.length; i++)
            if (arr1[i] != arr2[i])
                numOfErr++;

        return numOfErr;
    }
}
