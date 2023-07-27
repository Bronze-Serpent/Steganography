package utilsTest;

import org.junit.Assert;
import org.junit.Test;
import utils.ByteBitmask;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class BitMaskTest
{

    @Test
    public void applyFirstMaskTest()
    {
        byte num = -1; // eight 1

        for (byte i = 0, valShouldBe = 0; i < 9; i++)
        {
            ByteBitmask byteBitmask = ByteBitmask.fromNum(i, true);
            byte value = byteBitmask.apply(num);

            Assert.assertEquals(valShouldBe, value);

            valShouldBe = (byte) ((valShouldBe >>> 1) | (byte) -128);
        }
    }


    @Test
    public void applyLastMaskTest()
    {
        byte num = -1; // eight 1

        for (byte i = 0, valShouldBe = 0; i < 9; i++)
        {
            ByteBitmask byteBitmask = ByteBitmask.fromNum(i, false);
            byte value = byteBitmask.apply(num);

            Assert.assertEquals(valShouldBe, value);

            valShouldBe = (byte) ((valShouldBe << 1) | 1);
        }
    }


    @Test
    public void workLastBitmasks()
    {
        for (byte i = 0, mask = 0; i < 9; i++)
        {
            ByteBitmask byteBitmask = ByteBitmask.fromNum(i, false);
            Assert.assertEquals(mask, byteBitmask.getMask());

            mask = (byte) ((mask << 1) | 1);
        }
    }


    @Test
    public void workFirstBitmasks()
    {
        for (byte i = 0, mask = 0; i < 9; i++)
        {
            ByteBitmask byteBitmask = ByteBitmask.fromNum(i, true);
            Assert.assertEquals(mask, byteBitmask.getMask());

            mask = (byte) ((mask >>> 1) | (byte) -128);
        }
    }


    @Test
    public void getNumFromEnum()
    {
        List<Integer> rangeOfNumBitmasks = IntStream.range(0, 9).boxed().toList();

        List<Integer> firstBitmasks = Arrays.stream(ByteBitmask.values())
                .filter(ByteBitmask::isFirst)
                .map(ByteBitmask::getNum)
                .toList();

        List<Integer> lastBitmasks = Arrays.stream(ByteBitmask.values())
                .filter(ByteBitmask::isFirst)
                .map(ByteBitmask::getNum)
                .toList();

        Assert.assertEquals(rangeOfNumBitmasks, firstBitmasks);
        Assert.assertEquals(rangeOfNumBitmasks, lastBitmasks);
    }
}
