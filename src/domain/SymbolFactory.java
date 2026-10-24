package domain;

/**
 * Factory of the symbols of the machine (Factory pattern). It is the only
 * place that knows which symbol types exist and which class creates each one:
 * to add a new type of symbol, only this class changes.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public final class SymbolFactory
{
    private static final String[] TYPES={"normal","shy","ephemeral"};

    /**
     * The factory only has static methods, so it is not created.
     */
    private SymbolFactory(){
        // utility class: it is never instantiated
    }

    /**
     * Tells whether a symbol type exists.
     * @param type the type to check.
     * @return true if it is a known symbol type; false otherwise.
     */
    public static boolean isValidType(String type){
        for(String t:TYPES){
            if(t.equals(type)){
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a symbol of the given type.
     * @param type type of symbol: "normal", "shy" or "ephemeral".
     * @param color color of the symbol.
     * @param cx x coordinate of the symbol center.
     * @param cy y coordinate of the symbol center.
     * @return the new symbol; a normal symbol if the type is unknown.
     */
    public static Symbol create(String type, String color, int cx, int cy){
        if("shy".equals(type)){
            return new Shy(color,cx,cy);
        }
        if("ephemeral".equals(type)){
            return new Ephemeral(color,cx,cy);
        }
        return new Symbol(color,cx,cy);
    }
}
