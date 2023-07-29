package hidersTest;


import hiders.Hider;
import hiders.HiderSizeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static hidersTest.TestUtils.makeImageCopy;


public class RegularHidersTests
{

    public static final byte[] REGULAR_INF = ("Hello! This is my text, which i want to hide from everyone in my special picture." +
            "There may be various secrets, messages and various other interesting things," +
            "even the fact of transmission of which you would like to hide.").getBytes();

    public static final BufferedImage REGULAR_PIC;
    public static final BufferedImage THE_SMALLEST_PIC;
    public static final BufferedImage DARK_PIC_2;
    public static final BufferedImage PIC_1;

    static {
        try {
            REGULAR_PIC = ImageIO.read(new File(RegularHidersTests.class.getClassLoader().getResource("pictures/regular_pic.jpg").getFile()));
            THE_SMALLEST_PIC = ImageIO.read(new File(RegularHidersTests.class.getClassLoader().getResource("pictures/the_smallest_pic.jpg").getFile()));
            DARK_PIC_2 = ImageIO.read(new File(RegularHidersTests.class.getClassLoader().getResource("pictures/dark_pic_2.jpg").getFile()));
            PIC_1 = ImageIO.read(new File(RegularHidersTests.class.getClassLoader().getResource("pictures/pic_1.jpg").getFile()));
        } catch (IOException e) {
            throw new RuntimeException("An IOException when initializing images");
        }
    }


    static byte[] hideTakenOutInf(Hider hider, BufferedImage img, byte[] inf) throws HiderSizeException
    {
        BufferedImage imgWithMsg = makeImageCopy(img);

        hider.hideInf(imgWithMsg, inf);

        return hider.takeOutInf(imgWithMsg, inf.length);
    }


    static void unplaceableInf(Hider hider, BufferedImage img, byte[] inf) throws HiderSizeException
    {
        BufferedImage imgWithMsg = makeImageCopy(img);

        hider.hideInf(imgWithMsg, inf);
    }


    static boolean isInfCanBePlaced(Hider hider, BufferedImage img, byte[] inf)
    {
        return hider.willTheInfFit(img, inf);
    }

}
