package hiders;

import java.awt.image.BufferedImage;
import java.util.List;


/**
 * Describes a type that can hide passed information in the bits of an image stegocontainer,
 * using a stegokey represented by masks to hide the information.
 *
 * stegocont - is a picture, parts of the bits in which will be replaced with bits of information
 * inf - is the information that will be hidden in the container
 * masks - а set of constants used to sort pixels into different zones within a block. Used to provide privacy.
 *
 * throws HiderSizeException if the size of the information that is embedded or retrieved
 * is greater than the size of the container for this method
 */
public interface MaskSecurityHider
{

    /**
     * Embeds bits from inf into the given stegocontainer
     */
    BufferedImage hideInf(BufferedImage stegoContainer, byte[] inf, List<Long> masks) throws HiderSizeException;


    /**
     * Extracts the specified number of bytes from the container.
     *
     * @param bytesQuantity the number of bytes to be read in the specified way from the stegocontainer
     * @return extracted bytes
     */
    byte[] takeOutInf(BufferedImage stegoContainer, List<Long> masks, int bytesQuantity) throws HiderSizeException;


    /**
     *  Checks if a particular Hider can embed an inf into a stegocont
     * @return true if information can be embedded
     */
    boolean willTheInfFit(BufferedImage stegocont, byte[] inf);
}
