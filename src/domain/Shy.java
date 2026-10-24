package domain;

/**
 * A shy symbol. Each time it lands in the window of its wheel it changes its
 * mind: if it was showing it hides, and if it was hidden it shows up again.
 * While it is hidden the wheel shows nothing, so it does not count for a
 * jackpot and a lefty wheel has nothing to copy from it.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Shy extends Symbol
{
    private boolean hidden=false;

    /**
     * Creates a shy symbol of a given color, centered at (cx, cy). It starts
     * showing.
     * @param color color of the symbol (also identifies it).
     * @param cx x coordinate of the symbol center.
     * @param cy y coordinate of the symbol center.
     */
    public Shy(String color, int cx, int cy){
        super(color,cx,cy);
    }

    /**
     * Returns the type of this symbol.
     * @return "shy".
     */
    @Override
    public String getType(){
        return "shy";
    }

    /**
     * Draws the symbol, unless it is hiding.
     */
    @Override
    public void makeVisible(){
        if(!hidden){
            figure.makeVisible();
        }
    }

    /**
     * Hides or shows up: it switches between hiding and showing. When it
     * starts hiding its figure is erased; when it shows up again, the wheel
     * draws it.
     */
    @Override
    public void turn(){
        hidden=!hidden;
        if(hidden){
            figure.makeInvisible();
        }
    }

    /**
     * Tells whether the symbol is showing.
     * @return false while it is hiding; true otherwise.
     */
    @Override
    public boolean isShown(){
        return !hidden;
    }
}
