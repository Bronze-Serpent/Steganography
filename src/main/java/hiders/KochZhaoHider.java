package hiders;


import utils.*;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static utils.DataStructureUtils.*;


public class KochZhaoHider implements Hider
{
    /**
     * side of a pixel square used to hide 1 bit of information
     */
    private final int BLOCK_SIZE;

    /**
     * channel used to hide information
     */
    private final Channel USE_CHANNEL;

    /**
     * parameter affecting the bit embedding strength
     */
    private final int EPS;

    /**
     * dct coefficients used to embed information bit
     */
    private final List<Integer> USE_COEFF;

    /**
     * hiding key. Used to determine the order of blocks to embed.
     */
    private final int KEY;


    public KochZhaoHider(int blockSize, int eps, Channel channel, List<Integer> useCoeff, int key)
    {
        this.BLOCK_SIZE = blockSize;
        this.EPS = eps;
        this.USE_CHANNEL = channel;
        this.USE_COEFF = useCoeff;
        this.KEY = key;
    }


    @Override
    public BufferedImage hideInf(BufferedImage stegoContainer, byte[] inf) throws HiderSizeException
    {
        byte[] preparedInf = ByteDistributor.distributeBitsBy(inf, 1);
        List<List<Coordinate>> wholeBlocks = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), BLOCK_SIZE);

        if (inf.length * 8 >= wholeBlocks.size())
            throw new HiderSizeException(inf.length * 8, wholeBlocks.size() - 1);

        Queue<Integer> hidingOrder = shuffleQueue(KEY, createQueue(wholeBlocks.size(), 0, (i) -> i + 1));

        for (byte b : preparedInf)
        {
            // TODO: 22.05.2023 why "null" doesn't work here?
            // The warning can be suppressed because the check above is performed and
            // the queue is created with the guaranteed required size.
            @SuppressWarnings("all")
            List<Coordinate> block = wholeBlocks.get(hidingOrder.poll());
            int[] blockBlueVal = block.stream()
                    .mapToInt(coordinate -> (stegoContainer.getRGB(coordinate.x(), coordinate.y())) & 0xFF)
                    .toArray();

            double[] dctCoeff = twoDimArrToSingle(MathUtils.dct8X8(singleArrayToTwoDimBy8(blockBlueVal)));

            if (b == 0)
            {
                if (Math.abs(dctCoeff[USE_COEFF.get(0)]) - Math.abs(dctCoeff[USE_COEFF.get(1)]) < EPS) {
                    double newVal = Math.abs(dctCoeff[USE_COEFF.get(1)]) + EPS;
                    dctCoeff[USE_COEFF.get(0)] = dctCoeff[USE_COEFF.get(0)] >= 0 ? newVal : -1 * newVal;
                }
            }
            else
            {
                if (Math.abs(dctCoeff[USE_COEFF.get(0)]) - Math.abs(dctCoeff[USE_COEFF.get(1)]) > -EPS) {
                    double newVal = Math.abs(dctCoeff[USE_COEFF.get(0)]) + EPS;
                    dctCoeff[USE_COEFF.get(1)] = dctCoeff[USE_COEFF.get(1)] >= 0 ? newVal : -1 * newVal;
                }
            }
            int[] changedBlue = MathUtils.roundToBorders(0, 255,
                    twoDimArrToSingle(MathUtils.idct8X8(singleArrayToTwoDimBy8(dctCoeff))));
            ColorUtils.writeNewColors(stegoContainer, block, Arrays.stream(changedBlue).boxed().toList(), USE_CHANNEL);
        }

        return stegoContainer;
    }


    @Override
    public byte[] takeOutInf(BufferedImage stegoContainer, int bytesQuantity) throws HiderSizeException
    {
        byte[] readBites = new byte[bytesQuantity * 8];
        List<List<Coordinate>> wholeBlocks = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), BLOCK_SIZE);


        if (bytesQuantity >= wholeBlocks.size())
            throw new HiderSizeException(bytesQuantity, wholeBlocks.size() - 1);

        Queue<Integer> readingOrder = shuffleQueue(KEY, createQueue(wholeBlocks.size(), 0, (i) -> i + 1));

        for (int i = 0; i < readBites.length; i++)
        {
            // The warning can be suppressed because the check above is performed and
            // the queue is created with the guaranteed required size.
            @SuppressWarnings("all")
            int[] blockBlueVal = wholeBlocks.get(readingOrder.poll()).stream()
                    .mapToInt(coordinate -> (stegoContainer.getRGB(coordinate.x(), coordinate.y())) & 0xFF)
                    .toArray();

            double[] dctCoeff = twoDimArrToSingle(MathUtils.dct8X8(singleArrayToTwoDimBy8(blockBlueVal)));

            if (Math.abs(dctCoeff[USE_COEFF.get(0)]) > Math.abs(dctCoeff[USE_COEFF.get(1)]))
                readBites[i] = 0;
            else
                readBites[i] = 1;
        }

        return ByteDistributor.collectBitsBy(readBites, 1);
    }


    @Override
    public boolean willTheInfFit(BufferedImage stegoContainer, byte[] inf)
    {
        List<List<Coordinate>> wholeBlocks = MathUtils.breakIntoWholeBlocks(stegoContainer.getWidth(),
                stegoContainer.getHeight(), BLOCK_SIZE);

        return wholeBlocks.size() - 1 > inf.length * 8;
    }

}
