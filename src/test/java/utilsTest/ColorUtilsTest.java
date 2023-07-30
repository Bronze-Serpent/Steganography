package utilsTest;


import hidersTest.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import utils.Channel;
import utils.ColorUtils;
import utils.Coordinate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Stream;

import static hidersTest.RegularHidersTests.REGULAR_PIC;


public class ColorUtilsTest
{

    @Test
    public void shouldWriteColors()
    {
        BufferedImage imgCopy = TestUtils.makeImageCopy(REGULAR_PIC);
        List<Coordinate> block = List.of(new Coordinate(0, 0), new Coordinate(1, 0),
                new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 2));
        List<Integer> colorValues = Stream.generate(() -> 0).limit(block.size()).toList();
        Channel ch = Channel.BLUE;

        ColorUtils.writeNewColors(imgCopy, block, colorValues, ch);

        if (ColorUtils.getChannelVal(imgCopy.getRGB(0, 0), ch) != 0 || ColorUtils.getChannelVal(imgCopy.getRGB(1, 0), ch) != 0 ||
            ColorUtils.getChannelVal(imgCopy.getRGB(1, 1), ch) != 0 || ColorUtils.getChannelVal(imgCopy.getRGB(2, 1), ch) != 0 ||
                ColorUtils.getChannelVal(imgCopy.getRGB(2, 2), ch) != 0)
            Assert.fail();
    }


    @Test
    public void getChannelValTest()
    {
        int rgb = 43567890;
        Color color = new Color(rgb);

        Assert.assertEquals(color.getBlue(), ColorUtils.getChannelVal(rgb, Channel.BLUE));
        Assert.assertEquals(color.getRed(), ColorUtils.getChannelVal(rgb, Channel.RED));
        Assert.assertEquals(color.getGreen(), ColorUtils.getChannelVal(rgb, Channel.GREEN));
    }


    @Test
    public void calcAvgChannelValInArea()
    {
        Assert.assertEquals(134, Math.round(ColorUtils.calcAvgChannelValInArea(REGULAR_PIC, Channel.BLUE, 5, 5, 0)));
        Assert.assertEquals(134, Math.round(ColorUtils.calcAvgChannelValInArea(REGULAR_PIC, Channel.BLUE, 5, 5, 1)));
        Assert.assertEquals(133, Math.round(ColorUtils.calcAvgChannelValInArea(REGULAR_PIC, Channel.BLUE, 5, 5, 2)));
    }
}
