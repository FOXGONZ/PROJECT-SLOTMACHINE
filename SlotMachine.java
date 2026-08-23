import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Slot machine (SlotMachine).
 * 
 * The machine is organized by wheels (Wheel). Symbols are shared: when a
 * symbol is added or removed, the change is applied to ALL wheels, so every
 * wheel shares the same catalog of possible symbols.
 * 
 * The main class coordinates and delegates the concrete work to each wheel:
 * adding/removing symbols, spinning, placing a specific symbol, etc.
 * 
 * A spin advances each wheel one position to the next symbol.
 * spin() spins all wheels; spin(int) spins only the wheel at that position.
 * 
 * @author : Juan Diego Zorro Gonzalez.
 * @author : Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachine
{
    private int Max_x=1500;
    private int Max_y=750;

    private Rectangle Body_SlotMachine;
    private Rectangle Tube_Horizontal;
    private Rectangle Tube_Vertical;
    private Circle Handle;
    private Rectangle winnerLight;
    private Rectangle Base;
    private String[] symbols;
    private ArrayList<Wheel> wheels;
    private boolean isVisible;
    private boolean lastActionOk;

    /**
     * Creates the slot machine with its visual representation.
     */
    public SlotMachine(){
        wheels=new ArrayList<Wheel>();
        symbols=new String[0];
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
     * Adds a wheel at the given position.
     * If a wheel already exists at that position, the action is not performed.
     * @param pos position (cell) of the new wheel.
     */
    public void addWheel(int pos){
        if(pos<1){
            addWheel(1);
            return;
        }
        if(pos>50){
            addWheel(50);
            return;
        }
        if(!isActionOk(findWheel(pos)!=null,"Ya existe una rueda en la posicion "+(pos+1))){
            return;
        }
        Wheel w=new Wheel(pos);
        for(int i=0;i<symbols.length;i++){
            if(symbols[i]!=null){
                w.setSymbol(i,symbols[i]);
            }
        }
        wheels.add(w);
        if(isVisible){
            w.makeVisible();
        }
        lastActionOk=true;
    }

    /**
     * Removes the wheel at the given position.
     * If no wheel exists at that position, the action is not performed.
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
     * Adds a symbol (color) to ALL wheels of the machine.
     * If the color is not valid or the symbol already exists, nothing is done.
     * @param pos reference position (does not alter the shared catalog).
     * @param color color of the symbol to add.
     */
    public void addSymbol(int pos, String color){
        if(!isActionOk(!isValidColor(color),"Color de simbolo no valido: "+color)){
            return;
        }
        if(!isActionOk(contains(color),"El simbolo '"+color+"' ya existe")){
            return;
        }
        if(!isActionOk(pos<1,"La posicion debe ser mayor o igual a 1")){
            return;
        }
        int index=pos-1;
        if(!isActionOk(isSlotTaken(index),"La posicion "+pos+" ya esta ocupada")){
            return;
        }
        symbols=ensureSize(symbols,index+1);
        symbols[index]=color;
        for(Wheel w:wheels){
            w.setSymbol(index,color);
        }
        lastActionOk=true;
    }

    /**
     * Removes a symbol (color) from ALL wheels of the machine.
     * If the symbol does not exist, the action is not performed.
     * @param symbol color of the symbol to remove.
     */
    public void delSymbol(String symbol){
        if(!isActionOk(!contains(symbol),"El simbolo '"+symbol+"' no existe")){
            return;
        }
        for(int i=0;i<symbols.length;i++){
            if(symbol.equals(symbols[i])){
                symbols[i]=null;
            }
        }
        for(Wheel w:wheels){
            w.delSymbol(symbol);
        }
        lastActionOk=true;
    }

    /**
     * Fixes a specific symbol as visible in a given wheel.
     * @param wheel position of the wheel.
     * @param symbol color of the symbol to show in that wheel.
     */
    public void placeSymbol(int wheel, String symbol){
        Wheel w=findWheel(wheel);
        if(!isActionOk(w==null,"No existe una rueda en la posicion "+wheel)){
            return;
        }
        if(!isActionOk(!contains(symbol),"El simbolo '"+symbol+"' no existe")){
            return;
        }
        w.placeSymbol(symbol);
        lastActionOk=true;
    }

    /**
     * Spins a specific wheel one position to the next symbol.
     * @param wheel position of the wheel to spin.
     */
    public void spin(int wheel){
        Wheel w=findWheel(wheel);
        if(!isActionOk(w==null,"No existe una rueda en la posicion "+wheel)){
            return;
        }
        HandleOff();
        w.spin();
        showJackpotState();
        lastActionOk=true;
    }

    /**
     * Spins ALL wheels one position to the next symbol.
     */
    public void spin(){
        if(!isActionOk(wheels.isEmpty(),"No hay ruedas para girar")){
            return;
        }
        HandleOff();
        for(Wheel w:wheels){
            w.spin();
        }
        showJackpotState();
        lastActionOk=true;
    }

    /**
     * Returns the shared symbol catalog of the machine.
     * @return array with the available symbols.
     */
    public String[] symbols(){
        return symbols.clone();
    }

    /**
     * Returns the number of distinct symbols in the catalog.
     * @return number of distinct symbols.
     */
    public int distinctSymbols(){
        int count=0;
        for(String s:symbols){
            if(s!=null){
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the current configuration: the visible symbol on each wheel,
     * ordered by the wheels' positions.
     * @return array with the visible symbol of each wheel.
     */
    public String[] configuration(){
        String[] config=new String[wheels.size()];
        int i=0;
        for(Wheel w:orderedWheels()){
            config[i]=w.currentSymbol();
            i++;
        }
        return config;
    }

    /**
     * Tells whether the machine is in a winning state (jackpot): every wheel
     * shows the same symbol and there is at least one wheel with a symbol.
     * @return true if it is a winning state; false otherwise.
     */
    public boolean isJackpot(){
        if(wheels.isEmpty()){
            return false;
        }
        String first=wheels.get(0).currentSymbol();
        if(first==null){
            return false;
        }
        for(Wheel w:wheels){
            if(!first.equals(w.currentSymbol())){
                return false;
            }
        }
        return true;
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
        showJackpotState();
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
     * Closes the simulator.
     */
    public void exit(){
        makeInvisible();
        System.exit(0);
    }

    /**
     * Tells whether the last performed action was successful.
     * @return true if the last action was successful; false otherwise.
     */
    public boolean ok(){
        return lastActionOk;
    }

    /**
     * Pauses execution for a number of milliseconds, used for visualization.
     * @param millis milliseconds to wait.
     */
    private void pause(int millis){
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
        }
    }

    /**
     * Updates the visual light according to the winning state of the machine.
     */
    private void showJackpotState(){
        if(isJackpot()){
            for(int i=3;i>0;i--){
                winnerLight.changeColor("yellow");
                pause(120);
                winnerLight.changeColor("red");
                pause(120);
                winnerLight.changeColor("green");
                pause(120);
                winnerLight.changeColor("cyan");
                pause(120);
            }
            winnerLight.changeColor("yellow");
        }else{
            winnerLight.changeColor("gray");
        }
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
     * Returns the wheels ordered by their position (cell).
     * @return list of wheels ordered by ascending position.
     */
    private ArrayList<Wheel> orderedWheels(){
        ArrayList<Wheel> ordered=new ArrayList<Wheel>(wheels);
        ordered.sort((a,b)->Integer.compare(a.getPosition(),b.getPosition()));
        return ordered;
    }

    /**
     * Tells whether the shared catalog contains a symbol.
     * @param symbol symbol searched.
     * @return true if it exists; false otherwise.
     */
    private boolean contains(String symbol){
        for(String s:symbols){
            if(s!=null&&s.equals(symbol)){
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether the fixed slot at the given index is already taken.
     * A slot is taken if it exists in the array and holds a non-null value.
     * @param index slot index (base 0).
     * @return true if the slot holds a symbol; false if empty or out of range.
     */
    private boolean isSlotTaken(int index){
        return index>=0&&index<symbols.length&&symbols[index]!=null;
    }

    /**
     * Returns an array at least newSize long, copying the original values and
     * leaving new positions as null (empty slots). If the array is already big
     * enough, the same array is returned unchanged.
     * @param base original array.
     * @param newSize minimum required size.
     * @return array with at least newSize length.
     */
    private String[] ensureSize(String[] base, int newSize){
        if(base.length>=newSize){
            return base;
        }
        String[] result=new String[newSize];
        System.arraycopy(base,0,result,0,base.length);
        return result;
    }

    /**
     * Checks whether an action is valid. If not, it shows a message through
     * JOptionPane only if the simulator is visible.
     * @param invalid condition that invalidates the action (true=not valid).
     * @param message message to show to the user if the action is not valid.
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

    /**
     * Tells whether a color is valid (supported by the shapes canvas).
     * @param color color to validate.
     * @return true if the color is valid; false otherwise.
     */
    private boolean isValidColor(String color){
        String[] valid={"red","blue","green","yellow","magenta",
                        "orange","pink","cyan","black","white"};
        for(String v:valid){
            if(v.equals(color)){
                return true;
            }
        }
        return false;
    }

    /**
     * Shows how the handle goes down each time a spin is performed.
     */
    private void HandleOff(){
        Tube_Vertical.moveVertical(100);
        Handle.moveVertical(220);
        pause(500);
        Tube_Vertical.moveVertical(-100);
        Handle.moveVertical(-220);
    }
}
