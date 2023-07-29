package hidersTest;

import hiders.HiderSizeException;
import hiders.Hider;
import hiders.SimpleHider;
import org.junit.Assert;
import org.junit.Test;
import utils.Channel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static hidersTest.RegularHidersTests.*;


public class SimpleHiderTest
{

    @Test(expected = HiderSizeException.class)
    public void unplaceableInf() throws HiderSizeException
    {
        Hider hider = new SimpleHider(Collections.singletonList(Channel.BLUE), 1);
        byte[] inf = new byte[25];

        RegularHidersTests.unplaceableInf(hider, THE_SMALLEST_PIC, inf);
    }


    @Test
    public void isInfCanBePlaced()
    {
        Hider hider = new SimpleHider(Collections.singletonList(Channel.BLUE), 1);

        Assert.assertTrue(RegularHidersTests.isInfCanBePlaced(hider, REGULAR_PIC, REGULAR_INF));
    }


    @Test
    public void isInfCanNOTBePlaced()
    {
        Hider hider = new SimpleHider(Collections.singletonList(Channel.BLUE), 1);
        byte[] inf = new byte[25];

        Assert.assertFalse(RegularHidersTests.isInfCanBePlaced(hider, THE_SMALLEST_PIC, inf));
    }


    @Test
    public void allChannelsWithRandomQ()
    {
        Hider hider = new SimpleHider(List.of(Channel.values()), ThreadLocalRandom.current().nextInt(0, 9));
        try
        {
            byte[] takenOutInf = RegularHidersTests.hideTakenOutInf(hider,
                    REGULAR_PIC, REGULAR_INF);

            Assert.assertEquals(REGULAR_INF.length, takenOutInf.length);
            Assert.assertTrue(TestUtils.calcNumOfErr(REGULAR_INF, takenOutInf) * 1.0 / takenOutInf.length < 0.01);
        }catch (HiderSizeException e) {
            Assert.fail();
        }
    }


    @Test
    public void allChannelsHideTakenOut()
    {
        Hider hider = new SimpleHider(List.of(Channel.values()), 2);
        try
        {
            byte[] takenOutInf = RegularHidersTests.hideTakenOutInf(hider,
                    REGULAR_PIC, REGULAR_INF);

            Assert.assertEquals(REGULAR_INF.length, takenOutInf.length);
            Assert.assertTrue(TestUtils.calcNumOfErr(REGULAR_INF, takenOutInf) * 1.0 / takenOutInf.length < 0.01);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }


    @Test
    public void oneChannelHideTakenOut()
    {
        Hider hider = new SimpleHider(Collections.singletonList(Channel.BLUE), 2);
        try
        {
            byte[] takenOutInf = RegularHidersTests.hideTakenOutInf(hider,
                    REGULAR_PIC, REGULAR_INF);

            Assert.assertEquals(REGULAR_INF.length, takenOutInf.length);
            Assert.assertTrue(TestUtils.calcNumOfErr(REGULAR_INF, takenOutInf) * 1.0 / takenOutInf.length < 0.01);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }

}
