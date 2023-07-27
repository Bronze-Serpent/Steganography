package utilsTest;

import org.junit.Assert;
import org.junit.Test;
import utils.ByteBitmask;
import utils.ByteDistributor;

import java.util.concurrent.ThreadLocalRandom;


public class ByteDistributorTest
{

    @Test
    public void splitThenCollectBits()
    {
        byte[] arr = new byte[]{-12, 127, 1, -24, 75, 0};
        int qInByte = 3;

        byte[] splitArr = ByteDistributor.distributeBitsBy(arr, qInByte);
        byte[] collectedArr = ByteDistributor.collectBitsBy(splitArr, qInByte);

        Assert.assertArrayEquals(arr, collectedArr);
    }


    @Test
    public void splittingEachByteIntoByteArray()
    {
        byte[] arr = new byte[]{(byte) ThreadLocalRandom.current().nextInt(128)};
        int qInByte = 3;

        byte[] splitArr = ByteDistributor.distributeBitsBy(arr, qInByte);

        Assert.assertEquals((int) (Math.ceil(arr.length * 8.0 / qInByte)), splitArr.length);

        byte[] splitArrWithAppliedMask = splitArr.clone();
        ByteBitmask mask = ByteBitmask.fromNum(qInByte, false);
        for (int i = 0; i < splitArrWithAppliedMask.length; i++)
            splitArrWithAppliedMask[i] = mask.apply(splitArrWithAppliedMask[i]);

        Assert.assertArrayEquals(splitArrWithAppliedMask, splitArr);
    }


    @Test
    public void collectingBitsIntoAWholeBytes()
    {
        int qInByte = 2;
        int upBoundForElem = ByteBitmask.fromNum(qInByte, false).getMask();
        byte[] arr = new byte[]{(byte) ThreadLocalRandom.current().nextInt(upBoundForElem),
                (byte) ThreadLocalRandom.current().nextInt(upBoundForElem),
                (byte) ThreadLocalRandom.current().nextInt(upBoundForElem),
                (byte) ThreadLocalRandom.current().nextInt(upBoundForElem)};

        byte[] collectedArr = ByteDistributor.collectBitsBy(arr, qInByte);

        Assert.assertEquals((int) (Math.floor(arr.length * qInByte / 8.0)), collectedArr.length);

        byte[] collectedArrWithAppliedMask = collectedArr.clone();
        for (int i = 0; i < collectedArr.length; i++)
            collectedArrWithAppliedMask[i] = ByteBitmask.LAST_EIGHT.apply(collectedArrWithAppliedMask[i]);

        Assert.assertArrayEquals(collectedArr, collectedArrWithAppliedMask);
    }


    @Test(expected = IllegalArgumentException.class)
    public void invalidBitNumberLowValue()
    {
        byte[] arr = new byte[]{(byte) ThreadLocalRandom.current().nextInt(128)};
        int qInByte = 0;

        byte[] splitArr = ByteDistributor.distributeBitsBy(arr, qInByte);
    }


    @Test(expected = IllegalArgumentException.class)
    public void invalidBitNumberUpValue()
    {
        byte[] arr = new byte[]{(byte) ThreadLocalRandom.current().nextInt(128)};
        int qInByte = 9;

        byte[] splitArr = ByteDistributor.distributeBitsBy(arr, qInByte);
    }
}
