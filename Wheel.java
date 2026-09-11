import java.util.ArrayList;

/**
 * A wheel (Wheel) of the slot machine.
 * 
 * Each wheel has a position inside the machine and shows a window with one
 * visible symbol at a given moment. The position is interpreted as a cell
 * of a grid of COLS columns: the column is (pos-1)%COLS and the row is
 * (pos-1)/COLS. All wheels share the same catalog of possible symbols; what
 * changes between them is which one is in the window.
 * 
 * The spin() method advances the wheel one position to the next symbol.
 * 
 * @author : Juan Diego Zorro Gonzalez.
 * @author : Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Wheel
{
    private ArrayList<Symbol> symbols;   // symbols available in this wheel
    private int position;                // cell of the wheel inside the grid
    private int current;                 // index of the currently visible symbol
    private boolean isVisible;

    private Rectangle body;              // frame/body of the wheel
    private Rectangle window;            // window where the symbol is shown

    private int xBase;                   // x corner of the wheel body
    private int yBase;                   // y corner of the wheel body

    private static final int COLS=10;    // grid columns
    private static final int WIDTH=48;   // wheel body width
    private static final int HEIGHT=56;  // wheel body height
    private static final int GAP_X=8;    // horizontal gap between wheels
    private static final int GAP_Y=10;   // vertical gap between wheels
    private static final int ORIGIN_X=150; // left margin of the grid
    private static final int ORIGIN_Y=190; // top margin of the grid
    
    private boolean isLocked=false;

    /**
     * Creates a wheel at the given cell.
     * @param pos position (cell) of the wheel inside the grid.
     */
    public Wheel(int pos){
        position=pos;
        symbols=new ArrayList<Symbol>();
        current=-1;
        isVisible=false;
        createWheel(pos);
    }

    /**
     * Builds the visual representation of the wheel from its cell.
     * @param pos position (cell) of the wheel.
     */
    private void createWheel(int pos){
        int col=(pos-1)%COLS;
        int row=(pos-1)/COLS;
        xBase=ORIGIN_X+col*(WIDTH+GAP_X);
        yBase=ORIGIN_Y+row*(HEIGHT+GAP_Y);

        body=new Rectangle();
        body.changeColor("lightGray");
        body.changeSize(HEIGHT,WIDTH);
        body.moveHorizontal(xBase);
        body.moveVertical(yBase+10);

        window=new Rectangle();
        window.changeColor("white");
        window.changeSize(HEIGHT-12,WIDTH-12);
        window.moveHorizontal(xBase+6);
        window.moveVertical(yBase+16);
    }

    /**
     * Makes the wheel and its current symbol visible.
     */
    public void makeVisible(){
        isVisible=true;
        body.makeVisible();
        window.makeVisible();
        showCurrent();
    }

    /**
     * Makes the wheel and its current symbol invisible.
     */
    public void makeInvisible(){
        hideCurrent();
        window.makeInvisible();
        body.makeInvisible();
        isVisible=false;
    }

    /**
     * Spins the wheel one position: advances to the next occupied slot,
     * skipping empty ones. If there are no symbols, it does nothing.
     */
    public void spin(){
        int next=nextOccupied(current);
        if(next<0){
            return;
        }
        hideCurrent();
        current=next;
        showCurrent();
    }

    /**
     * Returns the wheel slots, including empty ones as null.
     * @return array with the names (colors) of the symbols, null where empty.
     */
    /**
     * Returns the colors of the wheel slots, including empty ones as null.
     * This lets the SlotMachine read the shared catalog from a wheel.
     * @return array with the colors of the symbols, null where the slot is empty.
     */
    public String[] getSymbolsColor(){
        String[] colors=new String[symbols.size()];
        for(int i=0;i<symbols.size();i++){
            colors[i]=symbols.get(i)==null?null:symbols.get(i).getColor();
        }
        return colors;
    }

    /**
     * Returns the symbol (color) currently visible in the window.
     * @return name of the visible symbol, or null if the wheel is empty.
     */
    public String currentSymbol(){
        if(current<0||current>=symbols.size()||symbols.get(current)==null){
            return null;
        }
        return symbols.get(current).getColor();
    }

    /**
     * Sets a symbol in the fixed slot at the given index, growing the slot
     * list with empty slots (null) if needed. The Symbol figure is created
     * right away, but only shown if this slot becomes (or already is) the
     * visible one. If the wheel had no visible symbol yet, this slot becomes
     * the visible one.
     * @param index fixed slot index (base 0).
     * @param symbolColor color/name of the symbol to place.
     */
    public void setSymbol(int index, String symbolColor){
        while(symbols.size()<=index){
            symbols.add(null);
        }
        int cx=xBase+WIDTH/2;
        int cy=yBase+HEIGHT/2;
        symbols.set(index,new Symbol(symbolColor,cx,cy));
        if(current<0||symbols.get(current)==null){
            current=index;
            showCurrent();
        }
    }

    /**
     * Removes a symbol from the wheel, leaving its fixed slot empty (null).
     * If the removed symbol was the visible one, the window moves to the next
     * occupied slot, or shows nothing if the wheel becomes empty.
     * @param symbolColor color/name of the symbol to remove.
     */
    public void delSymbol(String symbolColor){
        int idx=indexOfColor(symbolColor);
        if(!isActionOk(idx<0)){
            return;
        }
        symbols.get(idx).makeInvisible();
        symbols.set(idx,null);
        if(idx==current){
            current=nextOccupied(idx);
            showCurrent();
        }
    }

    /**
     * Places (fixes) a given symbol as the visible one.
     * @param symbolColor color/name of the symbol to show.
     */
    public void placeSymbol(String symbolColor){
        int idx=indexOfColor(symbolColor);
        if(!isActionOk(idx<0)){
            return;
        }
        hideCurrent();
        current=idx;
        showCurrent();
    }

    /**
     * Finds the slot index whose symbol has the given color.
     * @param symbolColor color/name to search for.
     * @return index of the matching slot, or -1 if none matches.
     */
    private int indexOfColor(String symbolColor){
        for(int i=0;i<symbols.size();i++){
            Symbol s=symbols.get(i);
            if(s!=null&&s.getColor().equals(symbolColor)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the next occupied slot after the given one, wrapping around.
     * @param from slot to start searching after.
     * @return index of the next occupied slot, or -1 if none is occupied.
     */
    private int nextOccupied(int from){
        int n=symbols.size();
        if(n==0){
            return -1;
        }
        for(int step=1;step<=n;step++){
            int i=((from+step)%n+n)%n;
            if(symbols.get(i)!=null){
                return i;
            }
        }
        return -1;
    }

    /**
     * Hides the currently visible symbol figure, if any.
     */
    private void hideCurrent(){
        if(current>=0&&current<symbols.size()&&symbols.get(current)!=null){
            symbols.get(current).makeInvisible();
        }
    }

    /**
     * Shows the currently visible symbol figure, only if the wheel itself
     * is visible.
     */
    private void showCurrent(){
        if(isVisible&&current>=0&&current<symbols.size()&&symbols.get(current)!=null){
            symbols.get(current).makeVisible();
        }
    }

    /**
     * Tells whether an action is valid (pattern used by the original design).
     * @param validation condition that invalidates the action (true=not valid).
     * @return true if the action can be performed; false otherwise.
     */
    private boolean isActionOk(boolean validation){
        return !validation;
    }

    /**
     * Returns the cell (position) of the wheel.
     * @return position of the wheel.
     */
    public int getPosition(){
        return position;
    }
    public boolean isLocked(){
        return isLocked;
    }
    public void lock(){
        isLocked=true;
    }
    public void unLock(){
        isLocked=false;
    }
    /**
     * Moves the wheel to a new grid position, redrawing its body, window and
     * current symbol at the coordinates of that new cell.
     * @param newPos new position (cell) of the wheel.
     */
    /**
     * Moves the wheel to a new grid position, redrawing its body, window and
     * all its symbols at the coordinates of the new cell.
     * @param newPos new position (cell) of the wheel.
     */
    public void moveTo(int newPos){
        boolean wasVisible=isVisible;
        String[] colors=getSymbolsColor();
        int savedCurrent=current;
        makeInvisible();
        position=newPos;
        createWheel(newPos);
        symbols=new ArrayList<Symbol>();
        current=-1;
        for(int i=0;i<colors.length;i++){
            if(colors[i]!=null){
                setSymbol(i,colors[i]);
            }
        }
        current=savedCurrent;
        if(wasVisible){
            makeVisible();
        }
    }
}