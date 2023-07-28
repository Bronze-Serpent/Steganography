package utils;


import java.util.stream.Stream;


public class MathUtils
{
    //If you try to draw a circle with so many squares, nothing will come of it.
    // However, when choosing an area to hide, the circles are just that.
    public static int numOfSquaresInACircle(int rad)
    {
        return (rad + numOfSquaresInAQuarter(rad)) * 4;
    }


    private static int numOfSquaresInAQuarter(int rad)
    {
        return Stream.iterate(rad, r -> r - 1).limit(rad).reduce(0, Integer::sum);
    }

}
