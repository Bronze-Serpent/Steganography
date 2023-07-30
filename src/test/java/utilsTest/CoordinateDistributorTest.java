package utilsTest;


import org.junit.Assert;
import org.junit.Test;
import utils.Coordinate;
import utils.CoordinateDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoordinateDistributorTest
{

    @Test
    public void shouldHaveZeroElem()
    {
        List<Coordinate> coordinates = Stream.generate(() -> new Coordinate(ThreadLocalRandom.current().nextInt(0, 100),
                ThreadLocalRandom.current().nextInt(0, 100)))
                .limit(30)
                .toList();
        int mask = 0;
        Coordinate[] emptyCoordArr = new Coordinate[0];
        Assert.assertArrayEquals(emptyCoordArr,
                CoordinateDistributor.elementsByMask(coordinates, mask).toArray(new Coordinate[0]));
    }


    @Test
    public void shouldSeparateElem()
    {
        List<Coordinate> coordinates = Stream.generate(() -> new Coordinate(ThreadLocalRandom.current().nextInt(0, 100),
                        ThreadLocalRandom.current().nextInt(0, 100)))
                .limit(30)
                .collect(Collectors.toList());
        int mask = 179; //10110011

        List<List<Coordinate>> splitCoordinates = CoordinateDistributor.splitIntoTwoGroupsByMask(coordinates, mask);

        List<Coordinate> firstGroup = new ArrayList<>();
        firstGroup.add(coordinates.get(0));
        firstGroup.add(coordinates.get(1));
        firstGroup.add(coordinates.get(4));
        firstGroup.add(coordinates.get(5));
        firstGroup.add(coordinates.get(7));
        coordinates.removeAll(firstGroup); // second group

        Assert.assertArrayEquals(splitCoordinates.get(0).toArray(new Coordinate[0]), firstGroup.toArray(new Coordinate[0]));
        Assert.assertArrayEquals(splitCoordinates.get(1).toArray(new Coordinate[0]), coordinates.toArray(new Coordinate[0]));
    }
}
