package test;

import domain.Ephemeral;
import domain.Lefty;
import domain.SlotMachine;
import domain.SymbolFactory;
import domain.Symbol;
import domain.Wheel;
import domain.WheelFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for cycle 4 of the SlotMachine: typed wheels (normal, rebel,
 * lefty, wild) and typed symbols (normal, shy, ephemeral).
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachineC4Test
{
    private SlotMachine machine;

    @BeforeEach
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

    @AfterEach
    public void tearDown()
    {
        machine = null;
    }

    // ---------- addWheel(type, pos) ----------

    /**
     * Every valid wheel type can be created.
     */
    @Test
    public void accordingZGBCShouldAddWheelsOfEveryType()
    {
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.addWheel("lefty", 3);
        machine.addWheel("wild", 4);
        assertTrue(machine.ok());
        assertEquals(4, machine.configuration().length);
    }

    /**
     * An unknown wheel type is rejected and no wheel is added.
     */
    @Test
    public void accordingZGBCShouldNotAddWheelOfUnknownType()
    {
        machine.addWheel("dragon", 1);
        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /**
     * A typed wheel cannot be added in a cell that is already taken.
     */
    @Test
    public void accordingZGBCShouldNotAddTypedWheelInTakenCell()
    {
        machine.addWheel(1);
        machine.addWheel("rebel", 1);
        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    /**
     * The old addWheel(pos) still works and creates a normal wheel.
     */
    @Test
    public void accordingZGBCShouldKeepOldAddWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    // ---------- addSymbol(type, pos, color) ----------

    /**
     * Every valid symbol type can be added.
     */
    @Test
    public void accordingZGBCShouldAddSymbolsOfEveryType()
    {
        machine.addWheel(1);
        machine.addSymbol("normal", 1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("ephemeral", 3, "green");
        assertTrue(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    /**
     * An unknown symbol type is rejected and no symbol is added.
     */
    @Test
    public void accordingZGBCShouldNotAddSymbolOfUnknownType()
    {
        machine.addWheel(1);
        machine.addSymbol("ghost", 1, "red");
        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    /**
     * A typed symbol with an invalid color is rejected.
     */
    @Test
    public void accordingZGBCShouldNotAddTypedSymbolWithInvalidColor()
    {
        machine.addWheel(1);
        machine.addSymbol("shy", 1, "notAColor");
        assertFalse(machine.ok());
    }

    /**
     * A wheel added after the symbols inherits the catalog.
     */
    @Test
    public void accordingZGBCShouldInheritCatalogInNewWheel()
    {
        machine.addWheel(1);
        machine.addSymbol("shy", 1, "red");
        machine.addSymbol("ephemeral", 2, "blue");
        machine.addWheel("rebel", 2);
        machine.placeSymbol(2, "blue");
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    // ---------- symbol types inside a wheel ----------

    /**
     * A wheel creates each symbol with the requested type.
     */
    @Test
    public void accordingZGBCShouldCreateSymbolsWithTheirType()
    {
        Wheel wheel = new Wheel(1);
        wheel.setSymbol(0, "red", "normal");
        wheel.setSymbol(1, "blue", "shy");
        wheel.setSymbol(2, "green", "ephemeral");
        assertEquals("normal", wheel.getSymbolsType()[0]);
        assertEquals("shy", wheel.getSymbolsType()[1]);
        assertEquals("ephemeral", wheel.getSymbolsType()[2]);
    }

    /**
     * Moving a wheel (as swap does) keeps the type of its symbols.
     */
    @Test
    public void accordingZGBCShouldKeepSymbolTypesWhenMoving()
    {
        Wheel wheel = new Wheel(1);
        wheel.setSymbol(0, "red", "shy");
        wheel.setSymbol(1, "blue", "ephemeral");
        wheel.moveTo(5);
        assertEquals(5, wheel.getPosition());
        assertEquals("shy", wheel.getSymbolsType()[0]);
        assertEquals("ephemeral", wheel.getSymbolsType()[1]);
    }

    /**
     * An ephemeral symbol gets smaller each time it turns, without changing
     * the size of other symbols.
     */
    @Test
    public void accordingZGBCShouldShrinkEphemeralWhenTurning()
    {
        Ephemeral ephemeral = new Ephemeral("red", 0, 0);
        Symbol normal = new Symbol("blue", 0, 0);
        int before = ephemeral.getSize();
        ephemeral.turn();
        assertTrue(ephemeral.getSize() < before);
        assertEquals(before, normal.getSize());
    }

    /**
     * An ephemeral symbol never shrinks below a positive size.
     */
    @Test
    public void accordingZGBCShouldNotShrinkEphemeralForever()
    {
        Ephemeral ephemeral = new Ephemeral("red", 0, 0);
        for (int i = 0; i < 100; i++) {
            ephemeral.turn();
        }
        assertTrue(ephemeral.getSize() > 0);
    }

    // ---------- Rebel ----------

    /**
     * A rebel wheel cannot be locked: it keeps spinning after lock.
     */
    @Test
    public void accordingZGBCShouldNotLockRebel()
    {
        machine.addWheel("rebel", 1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.lock(1);
        String before = machine.configuration()[0];
        machine.spin(1);
        assertNotEquals(before, machine.configuration()[0]);
    }

    /**
     * A rebel wheel cannot be deleted.
     */
    @Test
    public void accordingZGBCShouldNotDeleteRebel()
    {
        machine.addWheel("rebel", 1);
        machine.delWheel(1);
        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    /**
     * A rebel wheel cannot be swapped, and neither wheel moves.
     */
    @Test
    public void accordingZGBCShouldNotSwapRebel()
    {
        machine.addWheel("rebel", 1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * A normal wheel can still be deleted when there is a rebel.
     */
    @Test
    public void accordingZGBCShouldDeleteNormalNextToRebel()
    {
        machine.addWheel("rebel", 1);
        machine.addWheel(2);
        machine.delWheel(2);
        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    // ---------- Lefty ----------

    /**
     * A lefty wheel copies the symbol of the wheel to its left when it spins.
     */
    @Test
    public void accordingZGBCShouldCopyLeftWithLefty()
    {
        machine.addWheel(4);
        machine.addWheel("lefty", 5);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(4, "green");
        machine.spin(5);
        assertEquals("green", machine.configuration()[1]);
    }

    /**
     * A lefty wheel also copies when it spins several steps.
     */
    @Test
    public void accordingZGBCShouldCopyLeftWithLeftySteps()
    {
        machine.addWheel(4);
        machine.addWheel("lefty", 5);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(4, "blue");
        machine.spin(5, 4);
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * A lefty wheel does nothing if the cell immediately to its left is
     * empty, even if there is a wheel further to the left.
     */
    @Test
    public void accordingZGBCShouldNotMoveLeftyWithoutLeftWheel()
    {
        machine.addWheel(3);
        machine.addWheel("lefty", 5);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(3, "blue");
        String before = machine.configuration()[1];
        machine.spin(5);
        machine.spin(5, 2);
        assertEquals(before, machine.configuration()[1]);
    }

    /**
     * A locked lefty wheel does not copy its left neighbour.
     */
    @Test
    public void accordingZGBCShouldNotCopyWithLockedLefty()
    {
        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "blue");
        machine.lock(2);
        machine.spin(2);
        assertEquals("red", machine.configuration()[1]);
    }

    /**
     * A lefty wheel finds its left neighbour by position, not by the order in
     * which the wheels were created.
     */
    @Test
    public void accordingZGBCShouldFindLeftByPositionWithLefty()
    {
        machine.addWheel("lefty", 3);
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "green");
        machine.placeSymbol(2, "blue");
        machine.spin(3);
        assertEquals("blue", machine.configuration()[2]);
    }

    // ---------- WildWheel ----------

    /**
     * A wild wheel matches any symbol, so it does not break a jackpot.
     */
    @Test
    public void accordingZGBCShouldWinWithWildWheel()
    {
        machine.addWheel(1);
        machine.addWheel("wild", 2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "green");
        machine.placeSymbol(3, "red");
        assertTrue(machine.isJackpot());
    }

    /**
     * A wild wheel does not make different normal wheels a jackpot.
     */
    @Test
    public void accordingZGBCShouldNotWinWithWildWheelAndDifferentOthers()
    {
        machine.addWheel(1);
        machine.addWheel("wild", 2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(3, "blue");
        assertFalse(machine.isJackpot());
    }

    /**
     * A wild wheel keeps matching after it spins.
     */
    @Test
    public void accordingZGBCShouldKeepMatchingAfterWildSpins()
    {
        machine.addWheel(1);
        machine.addWheel("wild", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "blue");
        machine.spin(2);
        machine.spin(2, 2);
        assertTrue(machine.isJackpot());
    }

    /**
     * If every wheel is wild, it is a jackpot only when all of them show the
     * same symbol.
     */
    @Test
    public void accordingZGBCShouldCompareOwnSymbolsWhenAllWild()
    {
        machine.addWheel("wild", 1);
        machine.addWheel("wild", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.isJackpot());
        machine.placeSymbol(2, "blue");
        assertFalse(machine.isJackpot());
    }

    // ---------- mixed wheels and symbols ----------

    /**
     * A shy symbol hides when its wheel lands on it, and shows up again the
     * next time the wheel lands on it.
     */
    @Test
    public void accordingZGBCShouldHideShyWhenItsWheelLandsOnIt()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.spin(1);
        assertNull(machine.configuration()[0]);
        machine.spin(1);
        assertEquals("red", machine.configuration()[0]);
        machine.spin(1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * A hidden shy symbol breaks the jackpot, and when it shows up again the
     * jackpot can come back.
     */
    @Test
    public void accordingZGBCShouldBreakJackpotWithHiddenShy()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        assertTrue(machine.isJackpot());
        machine.spin();
        assertFalse(machine.isJackpot());
        machine.spin();
        assertTrue(machine.isJackpot());
        machine.spin();
        assertTrue(machine.isJackpot());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Two shy symbols, a normal one and a lefty wheel: while the wheel on the
     * left shows a hidden shy symbol, the lefty has nothing to copy.
     */
    @Test
    public void accordingZGBCShouldNotCopyHiddenShyWithLefty()
    {
        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("shy", 3, "green");
        machine.spin(1);
        machine.spin(2);
        assertNull(machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
        machine.spin(1);
        machine.spin(2);
        assertNull(machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
    }

    /**
     * Two shy symbols, a normal one and a lefty wheel: once the shy symbol on
     * the left shows up again, the lefty copies it and there is a jackpot.
     */
    @Test
    public void accordingZGBCShouldCopyShownShyWithLefty()
    {
        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("shy", 3, "green");
        machine.spin(1);
        machine.spin(1);
        machine.spin(1);
        machine.spin(1);
        assertEquals("blue", machine.configuration()[0]);
        machine.spin(2);
        assertEquals("blue", machine.configuration()[1]);
        assertTrue(machine.isJackpot());
    }

    /**
     * An ephemeral symbol shrinks each time its wheel lands on it, and only
     * the one in that wheel shrinks.
     */
    @Test
    public void accordingZGBCShouldShrinkEphemeralWhenItsWheelLandsOnIt()
    {
        Wheel wheel = new Wheel(1);
        Wheel other = new Wheel(2);
        wheel.setSymbol(0, "red", "normal");
        wheel.setSymbol(1, "blue", "ephemeral");
        other.setSymbol(0, "red", "normal");
        other.setSymbol(1, "blue", "ephemeral");
        int start = wheel.getSymbol(1).getSize();
        wheel.spin();
        int once = wheel.getSymbol(1).getSize();
        wheel.spin();
        wheel.spin();
        int twice = wheel.getSymbol(1).getSize();
        assertTrue(once < start);
        assertTrue(twice < once);
        assertEquals(start, other.getSymbol(1).getSize());
    }

    /**
     * A locked wheel does not move, so its symbols do not turn.
     */
    @Test
    public void accordingZGBCShouldNotTurnSymbolsOfLockedWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.lock(1);
        machine.spin(1);
        assertEquals("red", machine.configuration()[0]);
        machine.unLock(1);
        machine.spin(1);
        assertNull(machine.configuration()[0]);
    }

    /**
     * Placing a symbol is not spinning: a shy symbol placed in the window
     * does not turn, so it keeps the state it had.
     */
    @Test
    public void accordingZGBCShouldNotTurnWhenPlacingASymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.placeSymbol(1, "blue");
        assertEquals("blue", machine.configuration()[0]);
        machine.placeSymbol(1, "red");
        machine.spin(1);
        assertNull(machine.configuration()[0]);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        assertNull(machine.configuration()[0]);
    }

    /**
     * Spinning several steps turns every symbol the wheel lands on along the
     * way, not only the last one.
     */
    @Test
    public void accordingZGBCShouldTurnEverySymbolItPassesOver()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol(3, "green");
        machine.spin(1, 2);
        assertEquals("green", machine.configuration()[0]);
        machine.spin(1, 2);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * A wild wheel keeps matching even when the shy symbol in its own window
     * hides.
     */
    @Test
    public void accordingZGBCShouldKeepWildMatchingWhenItsShyHides()
    {
        machine.addWheel(1);
        machine.addWheel("wild", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.spin(2);
        assertTrue(machine.isJackpot());
    }

    /**
     * A wild wheel cannot save the jackpot when the normal wheel shows a
     * hidden shy symbol.
     */
    @Test
    public void accordingZGBCShouldNotWinWhenNormalWheelHidesNextToWild()
    {
        machine.addWheel(1);
        machine.addWheel("wild", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.spin(1);
        assertFalse(machine.isJackpot());
    }

    /**
     * A rebel wheel ignores the lock, so it lands on a shy symbol and the shy
     * symbol hides.
     */
    @Test
    public void accordingZGBCShouldHideShyOnRebelEvenAfterLock()
    {
        machine.addWheel("rebel", 1);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.lock(1);
        machine.spin(1);
        assertNull(machine.configuration()[0]);
    }

    // ---------- more cases of cycle 4: each class on its own ----------

    /**
     * A normal symbol keeps its color and type, is always shown and does
     * not change when it turns.
     */
    @Test
    public void accordingZGBCShouldKeepNormalSymbolWhenTurned()
    {
        Symbol symbol = new Symbol("red", 100, 100);
        assertEquals("red", symbol.getColor());
        assertEquals("normal", symbol.getType());
        symbol.turn();
        assertTrue(symbol.isShown());
        assertEquals(28, symbol.getSize());
    }

    /**
     * A wheel keeps the type of every symbol, also with empty slots.
     */
    @Test
    public void accordingZGBCShouldKeepTypesWithEmptySlots()
    {
        Wheel wheel = new Wheel(1);
        wheel.setSymbol(0, "red", "shy");
        wheel.setSymbol(2, "blue", "ephemeral");
        assertArrayEquals(new String[]{"shy", null, "ephemeral"}, wheel.getSymbolsType());
        assertFalse(wheel.isWild());
    }

    /**
     * A lefty wheel does nothing with zero steps or without a left wheel.
     */
    @Test
    public void accordingZGBCShouldNotCopyWithZeroStepsOrWithoutLeft()
    {
        List<Wheel> list = new ArrayList<Wheel>();
        Wheel left = new Wheel(1);
        Lefty lefty = new Lefty(2, list);
        Lefty alone = new Lefty(7, list);
        list.add(left);
        list.add(lefty);
        list.add(alone);
        for(Wheel w : list){
            w.setSymbol(0, "red");
            w.setSymbol(1, "blue");
        }
        left.spin();
        lefty.spin(0);
        assertEquals("red", lefty.currentSymbol());
        alone.spin();
        alone.spin(3);
        assertEquals("red", alone.currentSymbol());
        lefty.spin(-1);
        assertEquals("blue", lefty.currentSymbol());
    }

    // ---------- factories ----------

    /**
     * The wheel factory knows the four wheel types and creates the right one.
     */
    @Test
    public void accordingZGBCShouldCreateWheelsWithTheFactory()
    {
        List<Wheel> list = new ArrayList<Wheel>();
        assertTrue(WheelFactory.isValidType("lefty"));
        assertFalse(WheelFactory.isValidType("dragon"));
        assertTrue(WheelFactory.create("wild", 1, list).isWild());
        assertFalse(WheelFactory.create("rebel", 2, list).canSwap());
        assertTrue(WheelFactory.create("normal", 3, list).canSwap());
        assertEquals(4, WheelFactory.create("lefty", 4, list).getPosition());
    }

    /**
     * The symbol factory knows the three symbol types and creates the right one.
     */
    @Test
    public void accordingZGBCShouldCreateSymbolsWithTheFactory()
    {
        assertTrue(SymbolFactory.isValidType("shy"));
        assertFalse(SymbolFactory.isValidType("ghost"));
        assertEquals("normal", SymbolFactory.create("normal", "red", 0, 0).getType());
        assertEquals("shy", SymbolFactory.create("shy", "blue", 0, 0).getType());
        assertEquals("ephemeral", SymbolFactory.create("ephemeral", "green", 0, 0).getType());
    }
}
