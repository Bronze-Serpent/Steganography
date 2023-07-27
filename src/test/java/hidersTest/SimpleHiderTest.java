package hidersTest;

import hiders.Hider;
import hiders.HiderSizeException;
import hiders.SimpleHider;
import org.junit.Assert;
import org.junit.Test;
import utils.Channel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static hidersTest.ImgUtils.makeImageCopy;


public class SimpleHiderTest
{

    private static final byte[] INF = ("Hello! This is my text, which i want to hide from everyone in my special picture." +
            "There may be various secrets, messages and various other interesting things," +
            "even the fact of transmission of which you would like to hide.").getBytes();


    @Test
    public void allChannelsWithRandomQ()
    {
        File file = new File(getClass().getClassLoader().getResource("pictures/regular_pic.jpg").getFile());
        try
        {
            byte[] takenOutInf = hideTakeOutInf(file, INF, List.of(Channel.values()), 2);

            Assert.assertEquals(INF.length, takenOutInf.length);
            Assert.assertTrue(calcNumOfErr(INF, takenOutInf) * 1.0 / takenOutInf.length < 0.01);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }


    @Test
    public void allChannelsHideTakenOut()
    {
        File file = new File(getClass().getClassLoader().getResource("pictures/dark_pic.jpg").getFile());
        try
        {
            byte[] takenOutInf = hideTakeOutInf(file, INF, List.of(Channel.values()), 2);

            Assert.assertEquals(INF.length, takenOutInf.length);
            Assert.assertTrue(calcNumOfErr(INF, takenOutInf) * 1.0 / takenOutInf.length < 0.01);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }


    @Test
    public void oneChannelHideTakenOut()
    {
        File file = new File(getClass().getClassLoader().getResource("pictures/pic_1.jpg").getFile());
        try
        {
            byte[] takenOutInf = hideTakeOutInf(file, INF, List.of(Channel.BLUE), 2);

            Assert.assertEquals(INF.length, takenOutInf.length);
            Assert.assertTrue(calcNumOfErr(INF, takenOutInf) * 1.0 / takenOutInf.length < 0.01);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }


    private byte[] hideTakeOutInf(File file, byte[] inf,  List<Channel> usedChannels, int qInByte) throws IOException, HiderSizeException
    {
        BufferedImage img = ImageIO.read(file);
        BufferedImage imgWithMsg = makeImageCopy(img);
        Hider hider = new SimpleHider(usedChannels, qInByte);

        hider.hideInf(imgWithMsg, inf);

        return hider.takeOutInf(imgWithMsg, inf.length);
    }


    private int calcNumOfErr(byte[] arr1, byte[] arr2)
    {
        int numOfErr = 0;
        for (int i = 0; i < arr1.length; i++)
            if (arr1[i] != arr2[i])
                numOfErr++;

        return numOfErr;
    }
}
