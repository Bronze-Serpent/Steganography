package hidersTest;


import hiders.*;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static hidersTest.RegularHidersTests.*;
import static hidersTest.RegularHidersTests.REGULAR_INF;
import static hidersTest.TestUtils.makeImageCopy;


public class BruyndonckxHiderTest
{

    private final MaskSecurityHider HIDER = new BruyndonckxHider(8);
    public static final List<Long> REGULAR_INF_MASKS = Stream.generate(() -> ThreadLocalRandom.current().nextLong())
            .limit((long) REGULAR_INF.length * 8 * 2)
            .toList();


    @Test(expected = HiderSizeException.class)
    public void unplaceableInf() throws HiderSizeException
    {
        byte[] inf = new byte[2];
        List<Long> infMasks = Stream.generate(() -> ThreadLocalRandom.current().nextLong())
                .limit((long) inf.length * 8 * 2)
                .toList();

        BufferedImage imgWithMsg = makeImageCopy(THE_SMALLEST_PIC);

        HIDER.hideInf(imgWithMsg, inf, infMasks);
    }



    @Test
    public void isInfCanBePlaced()
    {
        Assert.assertTrue(HIDER.willTheInfFit(REGULAR_PIC, REGULAR_INF));
    }


    @Test
    public void isInfCanNOTBePlaced()
    {
        byte[] inf = new byte[12_000];

        Assert.assertFalse(HIDER.willTheInfFit(REGULAR_PIC, inf));
    }


    @Test
    public void HideTakenOut()
    {
        try
        {
            BufferedImage imgWithMsg = makeImageCopy(PIC_1);

            HIDER.hideInf(imgWithMsg, REGULAR_INF, REGULAR_INF_MASKS);

            byte[] takenOutInf = HIDER.takeOutInf(imgWithMsg, REGULAR_INF_MASKS, REGULAR_INF.length);

            Assert.assertEquals(REGULAR_INF.length, takenOutInf.length);
            Assert.assertTrue(TestUtils.calcNumOfErr(REGULAR_INF, takenOutInf) * 1.0 / takenOutInf.length < 0.02);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }
}
