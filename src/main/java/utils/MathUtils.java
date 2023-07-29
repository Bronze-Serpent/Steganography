package utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


public class MathUtils
{

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


    //If you try to draw a circle with so many squares, nothing will come of it.
    // However, when choosing an area to hide, the circles are just that.
    public static int numOfSquaresInACircle(int rad)
    {
        return (rad + numOfSquaresInAQuarter(rad)) * 4;
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


    private static int numOfSquaresInAQuarter(int rad)
    {
        return Stream.iterate(rad, r -> r - 1).limit(rad).reduce(0, Integer::sum);
    }

}
