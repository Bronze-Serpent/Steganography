package utilsTest;


import org.junit.Assert;
import org.junit.Test;
import utils.MathUtils;


public class MathUtilsTest
{

    @Test
    public void zeroRadCircle()
    {
        Assert.assertEquals(0, MathUtils.numOfSquaresInACircle(0));
    }


    @Test
    public void numOfSquaresInACircle()
    {
        Assert.assertEquals(80, MathUtils.numOfSquaresInACircle(5));
    }
}
