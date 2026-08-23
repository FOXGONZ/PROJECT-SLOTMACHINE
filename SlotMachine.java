import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Slot machine (SlotMachine).
 * 
 * The machine is organized by wheels (Wheel) placed on a grid. This first
 * version builds the machine body and manages adding and removing wheels.
 * 
 * @author : Juan Diego Zorro Gonzalez.
 * @author : Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachine
{
    private Rectangle Body_SlotMachine;
    private Rectangle Tube_Horizontal;
    private Rectangle Tube_Vertical;
    private Circle Handle;
    private Rectangle winnerLight;
    private Rectangle Base;
    private ArrayList<Wheel> wheels;
    private boolean isVisible;
    private boolean lastActionOk;

    /**
     * Creates the slot machine with its visual representation.
     */
    public SlotMachine(){
        wheels=new ArrayList<Wheel>();
        isVisible=false;
        lastActionOk=true;
        createSlotMachine();
    }

    /**
     * Builds the visual representation of the machine body.
     */
    private void createSlotMachine(){
        Body_SlotMachine=new Rectangle();
        Body_SlotMachine.changeColor("gray");
        Body_SlotMachine.changeSize(430,900);
        Body_SlotMachine.moveHorizontal(20);
        Body_SlotMachine.moveVertical(150);

        winnerLight=new Rectangle();
        winnerLight.changeColor("gray");
        winnerLight.changeSize(40,900);
        winnerLight.moveHorizontal(20);
        winnerLight.moveVertical(160);

        Tube_Horizontal=new Rectangle();
        Tube_Horizontal.changeColor("lightGray");
        Tube_Horizontal.changeSize(30,120);
        Tube_Horizontal.moveHorizontal(920);
        Tube_Horizontal.moveVertical(330);

        Tube_Vertical=new Rectangle();
        Tube_Vertical.changeColor("lightGray");
        Tube_Vertical.changeSize(120,30);
        Tube_Vertical.moveHorizontal(1010);
        Tube_Vertical.moveVertical(240);

        Handle=new Circle();
        Handle.changeColor("red");
        Handle.changeSize(60);
        Handle.moveHorizontal(1045);
        Handle.moveVertical(200);

        Base=new Rectangle();
        Base.changeColor("grey");
        Base.changeSize(40,970);
        Base.moveVertical(580);
    }

    /**
     * Adds a wheel at the given position (1..30). Out-of-range values are
     * clamped. If a wheel already exists at that position, nothing is done.
     * @param pos position (cell) of the new wheel.
     */
    public void addWheel(int pos){
        if(pos<1){
            addWheel(1);
            return;
        }
        if(pos>30){
            addWheel(30);
            return;
        }
        if(!isActionOk(findWheel(pos)!=null,"Ya existe una rueda en la posicion "+pos)){
            return;
        }
        Wheel w=new Wheel(pos);
        wheels.add(w);
        if(isVisible){
            w.makeVisible();
        }
        lastActionOk=true;
    }

    /**
     * Removes the wheel at the given position.
     * @param pos position (cell) of the wheel to remove.
     */
    public void delWheel(int pos){
        Wheel w=findWheel(pos);
        if(!isActionOk(w==null,"No existe una rueda en la posicion "+pos)){
            return;
        }
        w.makeInvisible();
        wheels.remove(w);
        lastActionOk=true;
    }

    /**
     * Makes the machine and all its wheels visible.
     */
    public void makeVisible(){
        isVisible=true;
        Body_SlotMachine.makeVisible();
        winnerLight.makeVisible();
        Tube_Horizontal.makeVisible();
        Tube_Vertical.makeVisible();
        Handle.makeVisible();
        Base.makeVisible();
        for(Wheel w:wheels){
            w.makeVisible();
        }
        lastActionOk=true;
    }

    /**
     * Makes the machine and all its wheels invisible.
     */
    public void makeInvisible(){
        for(Wheel w:wheels){
            w.makeInvisible();
        }
        Handle.makeInvisible();
        Tube_Vertical.makeInvisible();
        Tube_Horizontal.makeInvisible();
        winnerLight.makeInvisible();
        Body_SlotMachine.makeInvisible();
        isVisible=false;
        lastActionOk=true;
    }

    /**
     * Tells whether the last performed action was successful.
     * @return true if the last action was successful; false otherwise.
     */
    public boolean ok(){
        return lastActionOk;
    }

    /**
     * Finds the wheel at a given position.
     * @param pos position searched.
     * @return the wheel found, or null if it does not exist.
     */
    private Wheel findWheel(int pos){
        for(Wheel w:wheels){
            if(w.getPosition()==pos){
                return w;
            }
        }
        return null;
    }

    /**
     * Checks whether an action is valid. If not, it shows a message through
     * JOptionPane only if the simulator is visible.
     * @param invalid condition that invalidates the action.
     * @param message message to show if the action is not valid.
     * @return true if the action can be performed; false otherwise.
     */
    private boolean isActionOk(boolean invalid, String message){
        if(invalid){
            lastActionOk=false;
            if(isVisible){
                JOptionPane.showMessageDialog(null,message);
            }
            return false;
        }
        return true;
    }
}
