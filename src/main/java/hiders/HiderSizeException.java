package hiders;

/**
 * This exception serves to indicate a situation in which the size of the information that is embedded or retrieved
 * by this method is greater than the size of the container for this method
 */
public class HiderSizeException extends Exception
{
    private final int infSize;
    private final int contSize;


    public HiderSizeException(int infSize, int contSize)
    {
        super(String.format("An attempt to hide or extract %d bits with a container having %d bits for this hiding method",
                infSize, contSize));

        this.infSize = infSize;
        this.contSize = contSize;
    }

    public int getInfSize() { return infSize; }

    public int getContSize() { return contSize; }
}
