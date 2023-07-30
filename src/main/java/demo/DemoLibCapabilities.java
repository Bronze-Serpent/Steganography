package demo;

import hiders.*;
import utils.Channel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


public class DemoLibCapabilities
{

    private static final byte[] INF = ("Hello! This is my text, which i want to hide from everyone in my special picture." +
            "There may be various secrets, messages and various other interesting things," +
            "even the fact of transmission of which you would like to hide.").getBytes();


    public static void main(String[] args) throws IOException, HiderSizeException
    {

        File file = new File(DemoLibCapabilities.class.getClassLoader().getResource("pictures/pic_1.jpg").getFile());
        BufferedImage initImg = ImageIO.read(file);
        List<Long> masksForINF = Stream.generate(() -> ThreadLocalRandom.current().nextLong())
                .limit((long) INF.length * 8 * 2)
                .toList();

        Map<Class<?> , BufferedImage> hiderPicWithInf = hideUsingAllHiders(initImg, INF, masksForINF, 12345);

        for (Map.Entry<Class<?> , BufferedImage> entry : hiderPicWithInf.entrySet())
        {
            String[] splitHiderName = entry.getKey().getName().split("[.]");
            String filename = String.format("src/main/resources/completed/%sResult.jpg", splitHiderName[splitHiderName.length - 1]);
            ImageIO.write(entry.getValue(), "jpg", new File(filename));
        }
    }


    static Map<Class<?> , BufferedImage> hideUsingAllHiders(BufferedImage img, byte[] inf, List<Long> masks, int key) throws HiderSizeException
    {
        Map<Class<?> , BufferedImage> hiderPicWithInf = new HashMap<>();

        Hider simpleHider = new SimpleHider(Collections.singletonList(Channel.BLUE), 2);
        BufferedImage imageCopy = makeImageCopy(img);
        hiderPicWithInf.put(simpleHider.getClass(), simpleHider.hideInf(imageCopy, inf));
        System.out.printf("%-40s %s%n%n", "SimpleHider  extract information:", new String(simpleHider.takeOutInf(imageCopy, INF.length)));

        Hider cutterHider = new CutterHider(0.6, 2);
        imageCopy = makeImageCopy(img);
        hiderPicWithInf.put(cutterHider.getClass(), cutterHider.hideInf(imageCopy, inf));
        System.out.printf("%-40s %s%n%n", "CutterHider extract information:", new String(cutterHider.takeOutInf(imageCopy, INF.length)));

        MaskSecurityHider bruyndonckxHider = new BruyndonckxHider(8);
        imageCopy = makeImageCopy(img);
        hiderPicWithInf.put(bruyndonckxHider.getClass(), bruyndonckxHider.hideInf(imageCopy, inf, masks));
        System.out.printf("%-40s %s%n%n", "BruyndonckxHider extract information:", new String(bruyndonckxHider.takeOutInf(imageCopy, masks, INF.length)));

        Hider kochHider = new KochZhaoHider(8, 12, Channel.BLUE, List.of(3, 4), 1234);
        imageCopy = makeImageCopy(img);
        hiderPicWithInf.put(kochHider.getClass(), kochHider.hideInf(imageCopy, inf));
        System.out.printf("%-40s %s%n", "KochHider extract information:", new String(kochHider.takeOutInf(imageCopy, INF.length)));

        return hiderPicWithInf;
    }


    private static BufferedImage makeImageCopy(BufferedImage imageToCopy)
    {
        BufferedImage result = new BufferedImage(imageToCopy.getWidth(), imageToCopy.getHeight(), imageToCopy.getType());
        Graphics g = result.getGraphics();
        g.drawImage(imageToCopy, 0, 0, null);
        return result;
    }
}
