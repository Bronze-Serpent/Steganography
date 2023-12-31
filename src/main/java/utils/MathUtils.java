package utils;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MathUtils
{

    // Hardcoded resources instead of dependency injection, but it's not a math library to provide such dependencies +
    // I'm not good at the math behind these :)
    private static final double[][] COS_SCALING_FACTOR = { {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
            {0.9807853, 0.8314696, 0.5555702, 0.1950903,
                    -0.1950903,-0.5555702,-0.8314696,-0.9807853},
            {0.9238795, 0.3826834,-0.3826834,-0.9238795,
                    -0.9238795,-0.3826834, 0.3826834, 0.9238795},
            {0.8314696,-0.1950903,-0.9807853,-0.5555702,
                    0.5555702, 0.9807853, 0.1950903,-0.8314696},
            {0.7071068,-0.7071068,-0.7071068, 0.7071068,
                    0.7071068,-0.7071068,-0.7071068, 0.7071068},
            {0.5555702,-0.9807853, 0.1950903, 0.8314696,
                    -0.8314696,-0.1950903, 0.9807853,-0.5555702},
            {0.3826834,-0.9238795, 0.9238795,-0.3826834,
                    -0.3826834, 0.9238795,-0.9238795, 0.3826834},
            {0.1950903,-0.5555702, 0.8314696,-0.9807853,
                    0.9807853,-0.8314696, 0.5555702,-0.1950903} };

    private static final double[][] E_SCALING_FACTOR = { {0.125, 0.176777777, 0.176777777, 0.176777777,
            0.176777777, 0.176777777, 0.176777777, 0.176777777},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25}};


    public static int[] roundToBorders(int lowerBound, int upperBound, double[] arr)
    {
        return Arrays.stream(arr)
                .mapToInt(e -> (e <= lowerBound) ? lowerBound :
                        (e >= upperBound) ? upperBound : (int) Math.round(e))
                .toArray();
    }


    public static double[][] dct8X8(int[][] arr)
    {
        if (arr.length != 8 && (Stream.of(arr).filter(a -> a.length == 8).count() == 8))
            throw new IllegalArgumentException("arr must have size 8 X 8, but it has " + arr.length + " X " +
                    Stream.of(arr)
                            .map(a -> String.valueOf(a.length))
                            .collect(Collectors.joining(", ", "{", "}")));

        double[][] dctCoeff = new double[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
            {
                double temp = 0;
                for (int x = 0; x < 8; x++)
                    for (int y = 0; y < 8; y++)
                        temp += COS_SCALING_FACTOR[i][x] * COS_SCALING_FACTOR[j][y] * arr[x][y];

                dctCoeff[i][j] = E_SCALING_FACTOR[i][j] * temp;
            }

        return dctCoeff;
    }


    public static double[][] idct8X8(double[][] dctCoeff)
    {
        if (dctCoeff.length != 8 && (Stream.of(dctCoeff).filter(a -> a.length == 8).count() == 8))
            throw new IllegalArgumentException("dctCoeff must have size 8 X 8, but it has " + dctCoeff.length + " X " +
                    Stream.of(dctCoeff)
                            .map(a -> String.valueOf(a.length))
                            .collect(Collectors.joining(", ", "{", "}")));

        double[][] idctResult = new double[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                for (int x = 0; x < 8; x++)
                    for (int y = 0; y < 8; y++)
                        idctResult[i][j] += dctCoeff[x][y] * COS_SCALING_FACTOR[x][i] * COS_SCALING_FACTOR[y][j] * E_SCALING_FACTOR[x][y];

        return idctResult;
    }


    // Increases the average value of the three elements evenly, by the specified multipliers.
    // It has an upper bound for the element equal to 255
    public static int[] increaseTheAvgOfElem(int[] elements, double[] multipliers, int add)
    {
        int upperBound = 255;
        List<Integer> cannotBeAdded = getNumCannotBeAdded(elements, add, upperBound);

        if (cannotBeAdded.size() == 0)
            return new int[]{elements[0]+ add, elements[1]+ add, elements[2] + add};

        List<Integer> availableIdx = new LinkedList<>();
        Collections.addAll(availableIdx, 0, 1, 2);
        availableIdx.removeAll(cannotBeAdded);
        int[] addedElem = new int[3];

        if (cannotBeAdded.size() == 1)
        {
            int rem = add - (upperBound - elements[cannotBeAdded.get(0)]);
            addedElem[cannotBeAdded.get(0)] = upperBound;

            int recalcAdd = (int) Math.ceil(add + rem * multipliers[cannotBeAdded.get(0)]);
            List<Integer> cannotBeAddedSec = getNumCannotBeAdded(arrWithoutElem(elements, cannotBeAdded), recalcAdd, upperBound);

            if (cannotBeAddedSec.size() == 0)
            {
                addedElem[availableIdx.get(0)] = elements[availableIdx.get(0)] + recalcAdd;
                addedElem[availableIdx.get(1)] = elements[availableIdx.get(1)] + recalcAdd;
            }
            else
            {
                if (cannotBeAddedSec.size() == 1)
                {
                    int secondRem = recalcAdd - (upperBound - elements[cannotBeAddedSec.get(0)]);
                    addedElem[cannotBeAddedSec.get(0)] = upperBound;
                    int secRecalcAdd = (int) Math.ceil(add + rem * multipliers[cannotBeAdded.get(0)]
                            + secondRem * multipliers[cannotBeAddedSec.get(0)]);

                    availableIdx.remove(cannotBeAddedSec.get(0));

                    addedElem[availableIdx.get(0)] = Math.min(addedElem[availableIdx.get(0)] + secRecalcAdd, upperBound);
                }
                else
                {
                    addedElem[availableIdx.get(0)] = upperBound;
                    addedElem[availableIdx.get(1)] = upperBound;
                }
            }
        }
        else
        {
            if (cannotBeAdded.size() == 2)
            {
                int recalcAdd = (int) Math.ceil(add + multipliers[cannotBeAdded.get(0)] * (add - (upperBound - elements[cannotBeAdded.get(0)]))
                        + multipliers[cannotBeAdded.get(1)] * (add - (upperBound - elements[cannotBeAdded.get(1)])));

                addedElem[cannotBeAdded.get(0)] = upperBound;
                addedElem[cannotBeAdded.get(1)] = upperBound;
                addedElem[availableIdx.get(0)] = Math.min(elements[availableIdx.get(0)] + recalcAdd, upperBound);
            }
            else
            {
                addedElem[0] = upperBound;
                addedElem[1] = upperBound;
                addedElem[2] = upperBound;
            }
        }

        return addedElem;
    }


    public static List<List<Coordinate>> breakIntoWholeBlocks(int width, int height, int blockSize)
    {
        if (blockSize < 1)
            throw new IllegalArgumentException("blockSize must be greater than 0");

        List<List<Coordinate>> wholeBlocks = new ArrayList<>((int) (Math.floor(height * 1.0 / blockSize)
                * Math.floor(width * 1.0 / blockSize)));

        for (int currX = 0, currY = 0; ;)
        {
            if (currY + blockSize < height)
            {
                if (currX + blockSize < width)
                {
                    List<Coordinate> block = new ArrayList<>(blockSize * blockSize);
                    for (int y = currY; y < currY + blockSize; y++)
                        for(int x = currX; x < currX + blockSize; x++)
                            block.add(new Coordinate(x, y));

                    wholeBlocks.add(block);
                    currX += blockSize;
                }
                else
                {
                    currY += blockSize;
                    currX = 0;
                }
            }
            else
                break;
        }

        return wholeBlocks;
    }


    private static List<Integer> getNumCannotBeAdded(int[] elements, int add, int upperBound)
    {
        List<Integer> numbers = new LinkedList<>();

        for (int i = 0; i < elements.length; i++)
            if (elements[i] + add > upperBound)
                numbers.add(i);

        return numbers;
    }


    private static int[] arrWithoutElem(int[] arr, List<Integer> idxToExclude)
    {
        int[] newArr = new int[arr.length - idxToExclude.size()];

        for (int i = 0, j = 0; i < arr.length; i++)
            if (! idxToExclude.contains(i))
                newArr[j++] = arr[i];

        return newArr;
    }

}
