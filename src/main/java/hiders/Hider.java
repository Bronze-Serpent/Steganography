package hiders;

import java.awt.image.BufferedImage;

/**
 * Describes a type that can store the passed information into the bits of the stegocontainer - picture.
 *
 * stegocont - is a picture, parts of the bits in which will be replaced with bits of information
 * inf - is the information that will be hidden in the container
 *
 * throws HiderSizeException if the size of the information that is embedded or retrieved
 * is greater than the size of the container for this method
 */
public interface Hider
{

    /**
     * Embeds bits from inf into the given stegocontainer
     */
    BufferedImage hideInf(BufferedImage stegocont, byte[] inf) throws HiderSizeException;


    /**
     * Extracts the specified number of bytes from the container.
     *
     * @param bytesQuantity the number of bytes to be read in the specified way from the stegocontainer
     * @return extracted bytes
     */
    byte[] takeOutInf(BufferedImage stegocont, int bytesQuantity) throws HiderSizeException;


    /**
     *  Checks if a particular Hider can embed an inf into a stegocont
     * @return true if information can be embedded
     */
    boolean willTheInfFit(BufferedImage stegocont, byte[] inf);
}
