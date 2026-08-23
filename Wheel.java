/**
 * A wheel (Wheel) of the slot machine.
 * 
 * Each wheel has a position inside the machine and is drawn as a cell of a
 * grid of COLS columns: the column is (pos-1)%COLS and the row is (pos-1)/COLS.
 * This first version only builds the visual body and window of the wheel.
 * 
 * @author : Juan Diego Zorro Gonzalez.
 * @author : Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Wheel
{
    private int position;                // cell of the wheel inside the grid
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

    /**
     * Creates a wheel at the given cell.
     * @param pos position (cell) of the wheel inside the grid.
     */
    public Wheel(int pos){
        position=pos;
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
     * Makes the wheel visible.
     */
    public void makeVisible(){
        isVisible=true;
        body.makeVisible();
        window.makeVisible();
    }

    /**
     * Makes the wheel invisible.
     */
    public void makeInvisible(){
        window.makeInvisible();
        body.makeInvisible();
        isVisible=false;
    }

    /**
     * Returns the cell (position) of the wheel.
     * @return position of the wheel.
     */
    public int getPosition(){
        return position;
    }
}
