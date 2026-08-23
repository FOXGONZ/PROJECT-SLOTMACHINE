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
    private ArrayList<String> symbols;   // symbols available in this wheel
    private int position;                // cell of the wheel inside the grid
    private int current;                 // index of the currently visible symbol
    private boolean isVisible;

    private Rectangle body;              // frame/body of the wheel
    private Rectangle window;            // window where the symbol is shown
    private Symbol symbol;               // figure of the visible symbol

    private int xBase;                   // x corner of the wheel body
    private int yBase;                   // y corner of the wheel body

    private static final int COLS=10;    // grid columns
    private static final int WIDTH=48;   // wheel body width
    private static final int HEIGHT=56;  // wheel body height
    private static final int GAP_X=8;    // horizontal gap between wheels
    private static final int GAP_Y=10;   // vertical gap between wheels
    private static final int ORIGIN_X=150; // left margin of the grid
    private static final int ORIGIN_Y=190; // top margin of the grid

    /**
     * Creates a wheel at the given cell.
     * @param pos position (cell) of the wheel inside the grid.
     */
    public Wheel(int pos){
        position=pos;
        symbols=new ArrayList<String>();
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

        symbol=null;
    }

    /**
     * Makes the wheel and its current symbol visible.
     */
    public void makeVisible(){
        isVisible=true;
        body.makeVisible();
        window.makeVisible();
        refreshSymbol();
    }

    /**
     * Makes the wheel and its current symbol invisible.
     */
    public void makeInvisible(){
        if(symbol!=null){
            symbol.makeInvisible();
        }
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
        current=next;
        refreshSymbol();
    }

    /**
     * Returns the wheel slots, including empty ones as null.
     * @return array with the names (colors) of the symbols, null where empty.
     */
    public String[] symbols(){
        return symbols.toArray(new String[0]);
    }

    /**
     * Returns the symbol (color) currently visible in the window.
     * @return name of the visible symbol, or null if the wheel is empty.
     */
    public String currentSymbol(){
        if(current<0||current>=symbols.size()){
            return null;
        }
        return symbols.get(current);
    }

    /**
     * Sets a symbol in the fixed slot at the given index, growing the slot
     * list with empty slots (null) if needed. If the wheel had no visible
     * symbol yet, this slot becomes the visible one.
     * @param index fixed slot index (base 0).
     * @param symbolColor color/name of the symbol to place.
     */
    public void setSymbol(int index, String symbolColor){
        while(symbols.size()<=index){
            symbols.add(null);
        }
        symbols.set(index,symbolColor);
        if(current<0||symbols.get(current)==null){
            current=index;
        }
        refreshSymbol();
    }

    /**
     * Removes a symbol from the wheel, leaving its fixed slot empty (null).
     * If the removed symbol was the visible one, the window moves to the next
     * occupied slot, or shows nothing if the wheel becomes empty.
     * @param symbolColor color/name of the symbol to remove.
     */
    public void delSymbol(String symbolColor){
        int idx=symbols.indexOf(symbolColor);
        if(!isActionOk(idx<0)){
            return;
        }
        symbols.set(idx,null);
        if(current==idx){
            current=nextOccupied(idx);
        }
        refreshSymbol();
    }

    /**
     * Places (fixes) a given symbol as the visible one.
     * @param symbolColor color/name of the symbol to show.
     */
    public void placeSymbol(String symbolColor){
        int idx=symbols.indexOf(symbolColor);
        if(!isActionOk(idx<0)){
            return;
        }
        current=idx;
        refreshSymbol();
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
     * Redraws the visible symbol in the wheel window.
     */
    private void refreshSymbol(){
        if(symbol!=null){
            symbol.makeInvisible();
            symbol=null;
        }
        if(current<0||current>=symbols.size()||symbols.get(current)==null){
            return;
        }
        int cx=xBase+WIDTH/2;
        int cy=yBase+HEIGHT/2;
        symbol=new Symbol(symbols.get(current),cx,cy);
        if(isVisible){
            symbol.makeVisible();
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
}
