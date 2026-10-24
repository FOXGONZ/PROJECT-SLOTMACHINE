package domain;

/**
 * A rebel wheel: it cannot be locked, swapped or deleted. Apart from that it
 * behaves like a normal wheel. Its frame is orange.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Rebel extends Wheel
{
    /**
     * Creates a rebel wheel at the given cell.
     * @param pos position (cell) of the wheel.
     */
    public Rebel(int pos){
        super(pos);
    }

    /**
     * Refuses to be locked: it does nothing, so the wheel keeps spinning.
     */
    @Override
    public void lock(){
        // a rebel wheel ignores the lock on purpose
    }

    /**
     * Tells whether the wheel can take part in a swap.
     * @return false: a rebel wheel never moves to another cell.
     */
    @Override
    public boolean canSwap(){
        return false;
    }

    /**
     * Tells whether the wheel can be deleted from the machine.
     * @return false: a rebel wheel cannot be deleted.
     */
    @Override
    public boolean remove(){
        return false;
    }

    /**
     * Returns the frame color of a rebel wheel.
     * @return "orange".
     */
    @Override
    protected String frameColor(){
        return "orange";
    }
}
