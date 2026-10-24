package domain;

import java.util.List;

/**
 * A wheel that copies its left neighbour. When it spins, if there is a wheel
 * in the cell to its left, it takes the symbol that wheel is showing instead
 * of advancing. If there is no wheel to its left, it spins like a normal
 * wheel. A locked Lefty does not move.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Lefty extends Wheel
{
    private List<Wheel> wheels;

    /**
     * Creates a Lefty wheel at the given cell.
     * @param pos position (cell) of the wheel.
     * @param wheels the machine's wheels, used to find the left neighbour.
     */
    public Lefty(int pos, List<Wheel> wheels){
        super(pos);
        this.wheels=wheels;
    }

    /**
     * Copies the symbol of the wheel immediately to its left. If that cell is
     * empty, it does nothing.
     */
    @Override
    public void spin(){
        if(isLocked){
            return;
        }
        Wheel left=findLeft();
        if(left!=null){
            copy(left);
        }
    }

    /**
     * Copies the symbol of the wheel immediately to its left, whatever the
     * number of steps. If that cell is empty, it does nothing.
     * @param steps number of steps to rotate.
     */
    @Override
    public void spin(int steps){
        if(isLocked || steps==0){
            return;
        }
        Wheel left=findLeft();
        if(left!=null){
            copy(left);
        }
    }

    /**
     * Shows the same symbol the given wheel is showing.
     * @param left the wheel to copy.
     */
    private void copy(Wheel left){
        String color=left.currentSymbol();
        if(color!=null){
            placeSymbol(color);
        }
    }

    /**
     * Finds the wheel in the cell just to the left of this one.
     * @return that wheel, or null if the cell is empty.
     */
    private Wheel findLeft(){
        for(Wheel w:wheels){
            if(w.getPosition()==position-1){
                return w;
            }
        }
        return null;
    }

    /**
     * Returns the frame color of a lefty wheel.
     * @return "cyan".
     */
    @Override
    protected String frameColor(){
        return "cyan";
    }
}
