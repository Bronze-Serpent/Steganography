package hidersTest;


import hiders.Hider;
import hiders.HiderSizeException;
import hiders.KochZhaoHider;
import org.junit.Assert;
import org.junit.Test;
import utils.Channel;

import java.util.List;

import static hidersTest.RegularHidersTests.*;
import static hidersTest.RegularHidersTests.REGULAR_INF;

public class KochZhaoHiderTest
{
    private final Hider HIDER = new KochZhaoHider(8, 12, Channel.BLUE, List.of(3, 4), 1234);


    @Test(expected = HiderSizeException.class)
    public void unplaceableInf() throws HiderSizeException
    {
        byte[] inf = new byte[2];

        RegularHidersTests.unplaceableInf(HIDER, THE_SMALLEST_PIC, inf);
    }


    @Test
    public void isInfCanBePlaced()
    {
        Assert.assertTrue(RegularHidersTests.isInfCanBePlaced(HIDER, REGULAR_PIC, REGULAR_INF));
    }


    @Test
    public void isInfCanNOTBePlaced()
    {
        byte[] inf = new byte[1500];

        Assert.assertFalse(RegularHidersTests.isInfCanBePlaced(HIDER, REGULAR_PIC, inf));
    }


    @Test
    public void HideTakenOut()
    {
        try
        {
            byte[] takenOutInf = RegularHidersTests.hideTakenOutInf(HIDER,
                    DARK_PIC_2, REGULAR_INF);

            Assert.assertEquals(REGULAR_INF.length, takenOutInf.length);
            Assert.assertTrue(TestUtils.calcNumOfErr(REGULAR_INF, takenOutInf) * 1.0 / takenOutInf.length < 0.02);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }
}
