package utils;

import java.util.Arrays;


public class ByteDistributor
{

    /**
     * Distributes information bits from array originalArr to a new array of qInByte bits per byte.
     * The order of the bits does not change.
     *
     * @param originalArr an array whose bits need to be split by qInByte pieces into each byte of the new array
     * @param qInByte the number of information bits that should be in each element of the new array
     * @return an array, each element of which contains only qInByte information bits, and the rest are filled with 0
     */
    public static byte[] distributeBitsBy(byte[] originalArr, int qInByte)
    {
        if (qInByte > 8 || qInByte < 1)
            throw new IllegalArgumentException("invalid parameter value 'qInByte'");

        byte[] arr = Arrays.copyOf(originalArr, originalArr.length);
        byte[] distributedBits = new byte[(int) (Math.ceil(arr.length * 8.0 / qInByte))];

        int bitsInArrBlock = 8;
        int arrI = 0;
        for (int i = 0; i < distributedBits.length; i++)
        {
            if (bitsInArrBlock >= qInByte)
            {
                distributedBits[i] = getFirstBits(arr[arrI], qInByte);
                bitsInArrBlock -= qInByte;
                arr[arrI] = (byte) (arr[arrI] << qInByte);
            }
            else
            {
                int numAddBits = qInByte - bitsInArrBlock;
                distributedBits[i] = getFirstBits(arr[arrI++], bitsInArrBlock);

                if (arrI == arr.length)
                    return distributedBits;

                distributedBits[i] = (byte) (distributedBits[i] << numAddBits);
                distributedBits[i] = (byte) (distributedBits[i] | getFirstBits(arr[arrI], numAddBits));

                arr[arrI] = (byte) (arr[arrI] << numAddBits);
                bitsInArrBlock = 8 - numAddBits;
            }
        }

        return distributedBits;
    }


    /**
     * Concatenates the bits from originalArr into a new array.
     *
     * The method works on the assumption that the sum of all significant bits is a multiple of 8
     * so that they can be packed into whole bytes.
     * Using this assumption, the size of the new array will be calculated
     * by the formula: Math.floor(infArr.length * qInByte / 8.0)).
     * The order of the bits does not change.
     * To fill the space in the last byte of the new array, bits are taken from the end of the last byte in originalArr
     * in the size necessary to fill the full new byte.
     * @param originalArr byte array which contains information bits
     * @param qInByte number of information bits in the element of originalArr
     * @return an array in which each bit in the array element is informational
     */
    public static byte[] collectBitsBy(byte[] originalArr, int qInByte)
    {
        if (qInByte > 8 || qInByte < 1)
            throw new IllegalArgumentException("invalid parameter value 'qInByte'");

        byte[] infArr = Arrays.copyOf(originalArr, originalArr.length);
        byte[] collectedBits = new byte[(int) (Math.floor(infArr.length * qInByte / 8.0))];

        int collArrI = 0;
        int emptySp = 8;
        for (int infArrI = 0; infArrI < infArr.length; )
        {
            if (emptySp >= qInByte)
            {
                emptySp -= qInByte;
                collectedBits[collArrI] = (byte) (collectedBits[collArrI] | (getLastBits(infArr[infArrI++], qInByte) << emptySp));
            }
            else
            {
                if (infArrI != infArr.length - 1)
                {
                    collectedBits[collArrI] = (byte) (collectedBits[collArrI] | getFirstBits(infArr[infArrI], 8 - qInByte + emptySp));

                    int numRemainingBits = qInByte - emptySp;
                    emptySp = 8 - numRemainingBits;
                    collectedBits[++collArrI] = (byte) ((collectedBits[collArrI] | (getLastBits(infArr[infArrI++], numRemainingBits) << emptySp)));
                }
                else if (emptySp != 0)
                    collectedBits[collArrI] = (byte) (collectedBits[collArrI] | getLastBits(infArr[infArrI++], emptySp));
                else
                    collectedBits[++collArrI] = (byte) (collectedBits[collArrI] | getLastBits(infArr[infArrI++], qInByte));
            }
        }

        return collectedBits;
    }


    // Method returns q bits from src. The count of the number of copied bits goes from high to low bits.
    // The copied bits are stored in the lower bits of the return value. The order of the bits does not change.
    private static byte getFirstBits(byte src, int q)
    {
        return (byte) (Byte.toUnsignedInt(ByteBitmask.fromNum(q, true).apply(src)) >>> (8 - q));
    }


    // Method returns q bits from src. The count of the number of copied bits goes from low to high bits.
    // The order of the bits does not change.
    private static byte getLastBits(byte src, int q)
    {
        return (byte) (Byte.toUnsignedInt(ByteBitmask.fromNum(q, false).apply(src)));
    }
}
