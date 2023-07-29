package hiders;

import utils.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class BruyndonckxHider implements MaskSecurityHider
{

    private final int blockSize;


    public BruyndonckxHider(int blockSize)
    {
        this.blockSize = blockSize;
    }


    @Override
    public BufferedImage hideInf(BufferedImage stegoContainer, byte[] inf, List<Long> masks) throws HiderSizeException
    {
        if (masks.size() < inf.length * 8 * 2)
            throw new IllegalArgumentException("masks not provided for all blocks");

        if (!willTheInfFitInTheCont(stegoContainer, inf, blockSize))
        {
            int contSize = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                    stegoContainer.getHeight(), blockSize).size() - 1;

            throw new HiderSizeException(inf.length * 8, contSize);
        }

        byte[] preparedInformation = ByteDistributor.distributeBitsBy(inf, 1);
        List<List<Coordinate>> wholeBlocks = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), blockSize);

        int blockI = 0;
        int maskI = 0;
        for (byte b : preparedInformation)
        {
            List<List<Coordinate>> splitByBrightness = splitIntoTwoGroups(wholeBlocks.get(blockI++));
            List<List<Coordinate>> splitByMask_1 = CoordinateDistributor.splitIntoTwoGroupsByMask(splitByBrightness.get(0), masks.get(maskI++));
            List<List<Coordinate>> splitByMask_2 = CoordinateDistributor.splitIntoTwoGroupsByMask(splitByBrightness.get(1), masks.get(maskI++));

            if (b == 1)
            {
                ColorUtils.makeGroupABrighterThanGroupB(stegoContainer, splitByMask_1.get(0), splitByMask_1.get(1));
                ColorUtils.makeGroupABrighterThanGroupB(stegoContainer, splitByMask_2.get(0), splitByMask_2.get(1));
            }
            else
            {
                ColorUtils.makeGroupABrighterThanGroupB(stegoContainer, splitByMask_1.get(1), splitByMask_1.get(0));
                ColorUtils.makeGroupABrighterThanGroupB(stegoContainer, splitByMask_2.get(1), splitByMask_2.get(0));
            }
        }

        return stegoContainer;
    }


    @Override
    public byte[] takeOutInf(BufferedImage stegoContainer, List<Long> masks, int bytesQuantity) throws HiderSizeException
    {
        if (masks.size() < bytesQuantity * 8 * 2)
            throw new IllegalArgumentException("masks not provided for all blocks");

        int contSize = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), blockSize).size() - 1;
        if (bytesQuantity > contSize)
            throw new HiderSizeException(bytesQuantity, contSize);

        byte[] readBites = new byte[bytesQuantity * 8];
        List<List<Coordinate>> wholeBlocks = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), blockSize);

        for (int maskI = 0, i = 0; i < readBites.length; i++)
        {
            if (isItZeroBruyndonckx(stegoContainer, wholeBlocks.get(i), masks.subList(maskI, maskI + 2)))
                readBites[i] = 0;
            else
                readBites[i] = 1;

            maskI += 2;
        }

        return ByteDistributor.collectBitsBy(readBites, 1);
    }


    @Override
    public boolean willTheInfFit(BufferedImage stegoContainer, byte[] inf)
    {
        return willTheInfFitInTheCont(stegoContainer, inf, blockSize);
    }


    public static boolean willTheInfFitInTheCont(BufferedImage stegoContainer, byte[] inf, int blockSize)
    {
        List<List<Coordinate>> wholeBlocks = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), blockSize);

        return wholeBlocks.size() - 1 > inf.length * 8;
    }


    private static boolean isItZeroBruyndonckx(BufferedImage img, List<Coordinate> block, List<Long> masks)
    {
        List<List<Coordinate>> splitByBrightness = splitIntoTwoGroups(block);
        List<List<Coordinate>> splitByMask_1 =  CoordinateDistributor.splitIntoTwoGroupsByMask(splitByBrightness.get(0), masks.get(0));
        List<List<Coordinate>> splitByMask_2 =  CoordinateDistributor.splitIntoTwoGroupsByMask(splitByBrightness.get(1), masks.get(1));

        return ColorUtils.calcAvgBrightness(img, splitByMask_1.get(1)) - ColorUtils.calcAvgBrightness(img, splitByMask_1.get(0)) > 1E-5 &&
                ColorUtils.calcAvgBrightness(img, splitByMask_2.get(1)) - ColorUtils.calcAvgBrightness(img, splitByMask_2.get(0)) > 1E-5;
    }


    //this method simulates partitioning along the boundary with the maximum slope of the curve
    private static List<List<Coordinate>> splitIntoTwoGroups(List<Coordinate> pixelCoord)
    {
        List<List<Coordinate>> splitPixels = new ArrayList<>(2);
        splitPixels.add(pixelCoord.subList(0, pixelCoord.size() / 2));
        splitPixels.add(pixelCoord.subList(pixelCoord.size() / 2, pixelCoord.size()));

        return splitPixels;
    }
}
