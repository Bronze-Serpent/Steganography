package utilsTest;


import org.junit.Assert;
import org.junit.Test;
import utils.DataStructureUtils;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class DataStructureUtilsTest
{

    @Test
    public void singleIntToDimBy8()
    {
        int[] singleArr = Stream.generate(() -> ThreadLocalRandom.current().nextInt())
                .limit(20)
                .mapToInt(i->i)
                .toArray();
        int[][] dimArr = DataStructureUtils.singleArrayToTwoDimBy8(singleArr);

        Assert.assertEquals(dimArr.length, (int) Math.ceil(singleArr.length / 8.0));

        int singleArrCount = 0;
        for(int[] subArr : dimArr)
            for (int dimElem : subArr)
            {
                if (dimElem != singleArr[singleArrCount++])
                    Assert.fail();
                if (singleArrCount == singleArr.length)
                    return;
            }
    }


    @Test
    public void singleToSingleBy8()
    {
        double[] singleArr = Stream.generate(() -> ThreadLocalRandom.current().nextDouble())
                .limit(20)
                .mapToDouble(i->i)
                .toArray();

        double[][] dimArr = DataStructureUtils.singleArrayToTwoDimBy8(singleArr);
        double[] reconvertedSingleArr = DataStructureUtils.twoDimArrToSingle(dimArr);

        for(int i = 0; i < singleArr.length; i++)
            if (singleArr[i] != reconvertedSingleArr[i])
                Assert.fail();
    }


    @Test
    public void shouldCreateRndQueue()
    {
        int qSize = 20;
        Queue<Integer> rndQueue = DataStructureUtils.createQueue(qSize, 0, (i) -> i + 1);
        rndQueue = DataStructureUtils.shuffleQueue(12345, rndQueue);

        Assert.assertEquals(qSize, rndQueue.size());
        try
        {
            rndQueue.forEach(Objects::requireNonNull);
        }
        catch (NullPointerException e)
        {
            Assert.fail();
        }

    }
}
