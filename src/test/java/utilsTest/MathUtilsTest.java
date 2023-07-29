package utilsTest;


import org.junit.Assert;
import org.junit.Test;
import utils.MathUtils;

import static utils.MathUtils.breakIntoWholeBlocks;


public class MathUtilsTest
{

    private final double[] MULTIPLIERS = new double[]{0.5, 0.25, 0.25};


    @Test
    public void shouldSet3ElemToTheTop()
    {
        int[] values = new int[]{242, 250, 253};
        int[] increasedValues = new int[]{255, 255, 255};
        Assert.assertArrayEquals(increasedValues, MathUtils.increaseTheAvgOfElem(values, MULTIPLIERS, 10));
    }


    @Test
    public void shouldIncrTwoElem()
    {
        int[] values = new int[]{144, 255, 201};
        int[] increasedValues = new int[]{157, 255, 214};
        Assert.assertArrayEquals(increasedValues, MathUtils.increaseTheAvgOfElem(values, MULTIPLIERS, 10));

        int[] values_2 = new int[]{255, 124, 201};
        int[] increasedValues_2 = new int[]{255, 139, 216};
        Assert.assertArrayEquals(increasedValues_2, MathUtils.increaseTheAvgOfElem(values_2, MULTIPLIERS, 10));
    }


    @Test
    public void shouldIncrOneElem()
    {
        int[] values = new int[]{144, 250, 253};
        int[] increasedValues = new int[]{158, 255, 255};
        Assert.assertArrayEquals(increasedValues, MathUtils.increaseTheAvgOfElem(values, MULTIPLIERS, 10));

        int[] values_2 = new int[]{250, 250, 240};
        int[] increasedValues_2 = new int[]{255, 255, 254};
        Assert.assertArrayEquals(increasedValues_2, MathUtils.increaseTheAvgOfElem(values_2, MULTIPLIERS, 10));
    }


    @Test
    public void shouldIncrThreeElem()
    {
        int[] values = new int[]{144, 102, 201};
        int[] increasedValues = new int[]{154, 112, 211};

        Assert.assertArrayEquals(increasedValues, MathUtils.increaseTheAvgOfElem(values, MULTIPLIERS, 10));
    }


    @Test
    public void shouldReallocate3Incr()
    {
        int[] values = new int[]{144, 102, 250};
        int[] increasedValues = new int[]{156, 114, 255};
        Assert.assertArrayEquals(increasedValues, MathUtils.increaseTheAvgOfElem(values, MULTIPLIERS, 10));

        int[] values_2 = new int[]{250, 102, 144};
        int[] increasedValues_2 = new int[]{255, 115, 157};
        Assert.assertArrayEquals(increasedValues_2, MathUtils.increaseTheAvgOfElem(values_2, MULTIPLIERS, 10));
    }


    @Test
    public void shouldHaveZeroBlocks()
    {
        Assert.assertEquals(0, breakIntoWholeBlocks(8, 7, 8).size());
        Assert.assertEquals(0, breakIntoWholeBlocks(7, 8, 8).size());
        Assert.assertEquals(0, breakIntoWholeBlocks(0, 0, 8).size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldHaveException()
    {
        Assert.assertEquals(0, breakIntoWholeBlocks(16, 18, 0).size());
    }


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
