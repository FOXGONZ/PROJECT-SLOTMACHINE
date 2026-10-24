package domain;

import java.util.List;

/**
 * Factory of the wheels of the machine (Factory pattern). It is the only
 * place that knows which wheel types exist and which class creates each one:
 * to add a new type of wheel, only this class changes.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public final class WheelFactory
{
    private static final String[] TYPES={"normal","rebel","lefty","wild"};

    /**
     * The factory only has static methods, so it is not created.
     */
    private WheelFactory(){
        // utility class: it is never instantiated
    }

    /**
     * Tells whether a wheel type exists.
     * @param type the type to check.
     * @return true if it is a known wheel type; false otherwise.
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
     * Creates a wheel of the given type.
     * @param type type of wheel: "normal", "rebel", "lefty" or "wild".
     * @param pos position (cell) of the wheel.
     * @param wheels the wheels of the machine, for the wheels that look at
     *        the others (lefty and wild).
     * @return the new wheel; a normal wheel if the type is unknown.
     */
    public static Wheel create(String type, int pos, List<Wheel> wheels){
        if("rebel".equals(type)){
            return new Rebel(pos);
        }
        if("lefty".equals(type)){
            return new Lefty(pos,wheels);
        }
        if("wild".equals(type)){
            return new WildWheel(pos,wheels);
        }
        return new Wheel(pos);
    }
}
