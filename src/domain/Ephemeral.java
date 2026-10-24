package domain;

/**
 * An ephemeral symbol. Each time it lands in the window of its wheel it gets
 * smaller, staying centered, until it reaches a minimum size.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Ephemeral extends Symbol
{
    private static final int MIN_SIZE=1;
    private static final int STEP=2;

    /**
     * Creates an ephemeral symbol of a given color, centered at (cx, cy).
     * @param color color of the symbol (also identifies it).
     * @param cx x coordinate of the symbol center.
     * @param cy y coordinate of the symbol center.
     */
    public Ephemeral(String color, int cx, int cy){
        super(color,cx,cy);
    }

    /**
     * Returns the type of this symbol.
     * @return "ephemeral".
     */
    @Override
    public String getType(){
        return "ephemeral";
    }

    /**
     * Shrinks the symbol by STEP pixels, moving it so it stays centered. It
     * does nothing if it would get smaller than MIN_SIZE.
     */
    @Override
    public void turn(){
        if(SIZE-STEP<MIN_SIZE){
            return;
        }
        SIZE=SIZE-STEP;
        figure.changeSize(SIZE);
        figure.moveHorizontal(STEP/2);
        figure.moveVertical(STEP/2);
    }
}
