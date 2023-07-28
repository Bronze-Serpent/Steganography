package utilsTest;


import org.junit.Assert;
import org.junit.Test;
import utils.Channel;
import utils.ColorUtils;

import java.awt.*;

import static hidersTest.RegularHidersTests.REGULAR_PIC;


public class ColorUtilsTest
{

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
