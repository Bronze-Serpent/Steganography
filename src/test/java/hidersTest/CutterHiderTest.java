package hidersTest;


import hiders.CutterHider;
import hiders.Hider;
import hiders.HiderSizeException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static hidersTest.RegularHidersTests.*;


public class CutterHiderTest
{

    private final Hider HIDER = new CutterHider(0.6, 2);


    @Test(expected = HiderSizeException.class)
    public void unplaceableInf() throws HiderSizeException
    {
        byte[] inf = new byte[2];

        RegularHidersTests.unplaceableInf(HIDER, THE_SMALLEST_PIC, inf);
    }



    @Test
    public void isInfCanBePlaced() throws IOException
    {
        Assert.assertTrue(RegularHidersTests.isInfCanBePlaced(HIDER, REGULAR_PIC, INF));
    }


    @Test
    public void isInfCanNOTBePlaced() throws IOException
    {
        byte[] inf = new byte[5000];

        Assert.assertFalse(RegularHidersTests.isInfCanBePlaced(HIDER, REGULAR_PIC, inf));
    }


    @Test
    public void HideTakenOut()
    {
        try
        {
            byte[] takenOutInf = RegularHidersTests.hideTakenOutInf(HIDER,
                    DARK_PIC_2, INF);

            Assert.assertEquals(INF.length, takenOutInf.length);
            Assert.assertTrue(TestUtils.calcNumOfErr(INF, takenOutInf) * 1.0 / takenOutInf.length < 0.02);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HiderSizeException e) {
            Assert.fail();
        }
    }
}
