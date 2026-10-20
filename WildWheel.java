import java.util.ArrayList;

/**
 * A wild wheel: it matches any symbol. When the machine asks which symbol it
 * shows, it answers with the symbol of the first normal (non-wild) wheel, so
 * it never breaks a jackpot. Its window still shows its own symbol, like the
 * picture on a joker card. If every wheel is wild, it answers with its own
 * symbol.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class WildWheel extends Wheel
{
    private ArrayList<Wheel> wheels;

    /**
     * Creates a wild wheel at the given cell.
     * @param pos position (cell) of the wheel.
     * @param wheels the machine's wheels, used to find the symbol to match.
     */
    public WildWheel(int pos, ArrayList<Wheel> wheels){
        super(pos);
        this.wheels=wheels;
    }

    /**
     * Returns the symbol this wheel counts as: the symbol shown by the first
     * wheel that is not wild, or its own symbol if all wheels are wild.
     * @return the symbol it matches, or null if it has no symbols.
     */
    @Override
    public String currentSymbol(){
        for(Wheel w:wheels){
            if(!(w instanceof WildWheel)){
                return w.currentSymbol();
            }
        }
        return super.currentSymbol();
    }

    /**
     * Returns the frame color of a wild wheel.
     * @return "magenta".
     */
    @Override
    protected String frameColor(){
        return "magenta";
    }
}
