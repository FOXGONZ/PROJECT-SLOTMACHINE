/**
 * A symbol of the slot machine.
 * 
 * Reuses the components of the shapes project: a symbol is represented
 * as a colored circle (the color is also the "name" of the symbol, so
 * different symbols always have different colors).
 * 
 * @author : Juan Diego Zorro Gonzalez.
 * @author : Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class Symbol
{
    private Circle figure;
    private String color;

    private static final int SIZE=28;    // circle diameter
    private static final int START_X=20; // initial x of the Circle in shapes
    private static final int START_Y=15; // initial y of the Circle in shapes

    /**
     * Creates a symbol of a given color, centered at (cx, cy).
     * @param color color of the symbol (also identifies it).
     * @param cx x coordinate of the symbol center.
     * @param cy y coordinate of the symbol center.
     */
    public Symbol(String color, int cx, int cy){
        this.color=color;
        figure=new Circle();
        figure.changeSize(SIZE);
        figure.changeColor(color);
        figure.moveHorizontal((cx+70-SIZE/2)-START_X);
        figure.moveVertical((cy+20-SIZE/2)-START_Y);
    }

    /**
     * Makes the symbol visible.
     */
    public void makeVisible(){
        figure.makeVisible();
    }

    /**
     * Makes the symbol invisible.
     */
    public void makeInvisible(){
        figure.makeInvisible();
    }

    /**
     * Returns the color/name of the symbol.
     * @return color of the symbol.
     */
    public String getColor(){
        return color;
    }
}
