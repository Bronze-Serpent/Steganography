package utils;


import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStructureUtils
{

    public static <T> Queue<T> createQueue(int size, T seed, UnaryOperator<T> oper)
    {
        return Stream.iterate(seed, oper)
                .limit(size).collect(Collectors.toCollection(LinkedList::new));
    }


    public static <T> Queue<T> shuffleQueue(int rndSeed, Queue<T> queue)
    {
        //Suppress because cast is guaranteed to success. Object arr is empty
        @SuppressWarnings("unchecked")
        T[] arr = queue.toArray((T[]) new Object[0]);

        Random rnd = new Random(rndSeed);
        for(int i = 0; i < arr.length / 3; i++)
        {
            int idx = rnd.nextInt(arr.length);
            T temp = arr[idx];
            arr[idx] = arr[i];
            arr[i] = temp;
        }

        return new LinkedList<>(Arrays.asList(arr));
    }


    public static double[] twoDimArrToSingle(double[][] arr)
    {
        double[] concatArr = new double[arr.length * arr[0].length];

        int counter = 0;
        for (double[] subArr : arr)
            for (double elem : subArr)
                concatArr[counter++] = elem;

        return concatArr;
    }


    public static double[][] singleArrayToTwoDimBy8(double[] arr)
    {
        double[][] splitArr = new double[(int) Math.ceil(arr.length / 8.0)][8];

        int rowCounter = 0;
        int clmCounter = 0;
        for (double e : arr)
        {
            splitArr[rowCounter][clmCounter] = e;
            clmCounter++;

            if(clmCounter == splitArr[0].length)
            {
                rowCounter++;
                clmCounter = 0;
            }
        }

        return splitArr;
    }


    public static int[][] singleArrayToTwoDimBy8(int[] arr)
    {
        int[][] splitArr = new int[(int) Math.ceil(arr.length / 8.0)][8];

        int rowCounter = 0;
        int clmCounter = 0;
        for (int e : arr)
        {
            splitArr[rowCounter][clmCounter] = e;
            clmCounter++;

            if(clmCounter == splitArr[0].length)
            {
                rowCounter++;
                clmCounter = 0;
            }
        }

        return splitArr;
    }
}
