package test;

import domain.SlotMachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the base methods of SlotMachine (cycle 1).
 *
 * The machine is created and made invisible in setUp, so no windows or
 * JOptionPane dialogs appear while the tests run. Each test is named with the
 * should / shouldNot pattern and checks the logical result through ok(),
 * configuration(), symbols(), distinctSymbols() and isJackpot().
 *
 * @author  Juan Diego Zorro Gonzalez
 * @author  Ruben Felipe Bustos Carabante
 * @version 1.0
 */
public class SlotMachineC1Test
{
    private SlotMachine machine;

    /**
     * Sets up the fixture before each test: a fresh, invisible machine so no
     * windows or dialogs pop up during the run.
     */
    @BeforeEach
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

    /**
     * Tears down the fixture after each test.
     */
    @AfterEach
    public void tearDown()
    {
        machine = null;
    }

    // ---------- addWheel ----------

    /**
     * A wheel should be added at a valid position, and the configuration
     * should then have one wheel.
     */
    @Test
    public void accordingZGBCShouldAddWheelAtValidPosition()
    {   
        assertEquals(0, machine.configuration().length);
        machine.addWheel(1);
        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    /**
     * A second wheel should not be added at a position that is already taken,
     * so the count stays at one.
     */
    @Test
    public void accordingZGBCShouldNotAddWheelAtTakenPosition()
    {
        assertEquals(0, machine.configuration().length);
        machine.addWheel(1);
        machine.addWheel(1);
        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    // ---------- delWheel ----------

    /**
     * An existing wheel should be removed, leaving the configuration empty.
     */
    @Test
    public void accordingZGBCShouldDeleteExistingWheel()
    {
        machine.addWheel(1);
        machine.delWheel(1);
        assertTrue(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /**
     * Deleting a wheel that does not exist should fail.
     */
    @Test
    public void accordingZGBCShouldNotDeleteMissingWheel()
    {
        machine.delWheel(9);
        assertFalse(machine.ok());
    }

    // ---------- addSymbol ----------

    /**
     * A valid symbol should be added to the wheel and become visible on it.
     */
    @Test
    public void accordingZGBCShouldAddSymbolToWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * A symbol should not be added when there are no wheels to hold it.
     */
    @Test
    public void accordingZGBCShouldNotAddSymbolWithoutWheels()
    {
        machine.addSymbol(1, "red");
        assertFalse(machine.ok());
    }

    /**
     * An invalid color should not be accepted as a symbol.
     */
    @Test
    public void accordingZGBCShouldNotAddInvalidColor()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "notacolor");
        assertFalse(machine.ok());
    }

    // ---------- delSymbol ----------

    /**
     * An existing symbol should be removed from the machine.
     */
    @Test
    public void accordingZGBCShouldDeleteExistingSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.delSymbol("red");
        assertTrue(machine.ok());
    }

    /**
     * A symbol that does not exist should not be removed.
     */
    @Test
    public void accordingZGBCShouldNotDeleteMissingSymbol()
    {
        machine.addWheel(1);
        machine.delSymbol("green");
        assertFalse(machine.ok());
    }

    // ---------- placeSymbol ----------

    /**
     * An existing symbol should be placed as the visible one on a wheel.
     */
    @Test
    public void accordingZGBCShouldPlaceSymbolOnWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "blue");
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Placing a symbol that does not exist should fail.
     */
    @Test
    public void accordingZGBCShouldNotPlaceMissingSymbol()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "green");
        assertFalse(machine.ok());
    }

    // ---------- spin(wheel) ----------

    /**
     * Spinning an existing wheel should advance it to the next symbol.
     */
    @Test
    public void accordingZGBCShouldSpinOneWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String before = machine.configuration()[0];
        machine.spin(1);
        assertNotEquals(before, machine.configuration()[0]);
    }

    /**
     * Spinning a wheel that does not exist should fail.
     */
    @Test
    public void accordingZGBCShouldNotSpinMissingWheel()
    {
        machine.spin(9);
        assertFalse(machine.ok());
    }

    // ---------- spin() ----------

    /**
     * A global spin should advance every wheel of the machine at once.
     */
    @Test
    public void accordingZGBCShouldSpinAllWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String[] before = machine.configuration().clone();
        machine.spin();
        assertNotEquals(before[0], machine.configuration()[0]);
        assertNotEquals(before[1], machine.configuration()[1]);
    }

    /**
     * A global spin should fail when there are no wheels to spin.
     */
    @Test
    public void accordingZGBCShouldNotSpinAllWithoutWheels()
    {
        machine.spin();
        assertFalse(machine.ok());
    }

    // ---------- symbols / distinctSymbols ----------

    /**
     * The machine should report the shared catalog: its distinct count and the
     * colors in order.
     */
    @Test
    public void accordingZGBCShouldReturnSharedSymbols()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        assertEquals(1, machine.distinctSymbols());
        assertEquals("red", machine.symbols()[0]);
        assertEquals("blue", machine.symbols()[1]);
    }

    // ---------- configuration ----------

    /**
     * The configuration should have one entry per wheel.
     */
    @Test
    public void accordingZGBCShouldReturnConfigurationForEachWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        assertEquals(2, machine.configuration().length);
    }

    // ---------- isJackpot ----------

    /**
     * The machine should report a jackpot when all wheels show the same symbol.
     */
    @Test
    public void accordingZGBCShouldDetectJackpot()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        assertTrue(machine.isJackpot());
    }

    /**
     * The machine should not report a jackpot when the wheels show different
     * symbols.
     */
    @Test
    public void accordingZGBCShouldNotDetectJackpotWhenDifferent()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(2, "blue");
        assertFalse(machine.isJackpot());
    }

    // ---------- ok ----------

    /**
     * ok() should report success right after a valid action.
     */
    @Test
    public void accordingZGBCShouldReportOkAfterValidAction()
    {
        machine.addWheel(1);
        assertTrue(machine.ok());
    }

    // ---------- exit ----------
    // WARNING: exit() calls System.exit, which closes the whole program and
    // makes BlueJ show "VM restarted". That is the CORRECT result of exit(),
    // not a bug. This test is left COMMENTED so the other tests can run and
    // report normally. To test exit(), remove the /* and */ below and run it;
    // BlueJ will restart the VM, which proves exit() worked.
    /*
    @Test
    public void zzzAccordingZGBCShouldExitTheSimulator()
    {
        machine.addWheel(1);
        machine.exit();
    }
    */

    // ---------- more cases of cycle 1 ----------

    /**
     * An empty machine has no symbols, no configuration and no jackpot.
     */
    @Test
    public void accordingZGBCShouldHaveNothingWhenEmpty()
    {
        assertEquals(0, machine.symbols().length);
        assertEquals(0, machine.configuration().length);
        assertEquals(0, machine.distinctSymbols());
        assertFalse(machine.isJackpot());
        assertTrue(machine.ok());
    }

    /**
     * A machine whose wheels have no symbols shows nothing and has no jackpot.
     */
    @Test
    public void accordingZGBCShouldNotHaveJackpotWithoutSymbols()
    {
        machine.addWheel(1);
        assertNull(machine.configuration()[0]);
        assertFalse(machine.isJackpot());
    }

    /**
     * Positions below 1 go to 1 and positions above 50 go to 50.
     */
    @Test
    public void accordingZGBCShouldClampWheelPositions()
    {
        machine.addWheel(0);
        machine.addWheel(99);
        assertEquals(2, machine.configuration().length);
        machine.delWheel(1);
        assertTrue(machine.ok());
        machine.delWheel(50);
        assertTrue(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /**
     * Hexadecimal colors and the named colors of the shapes are accepted.
     */
    @Test
    public void accordingZGBCShouldAcceptHexAndNamedColors()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "#a1B2c3");
        assertTrue(machine.ok());
        machine.addSymbol(2, "lightGray");
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"#a1B2c3", "lightGray"}, machine.symbols());
    }

    /**
     * A null color is not valid.
     */
    @Test
    public void accordingZGBCShouldNotAddNullColor()
    {
        machine.addWheel(1);
        machine.addSymbol(1, null);
        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    /**
     * The same symbol cannot be added twice.
     */
    @Test
    public void accordingZGBCShouldNotAddRepeatedSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red");
        assertFalse(machine.ok());
        assertEquals(1, machine.symbols().length);
    }

    /**
     * A symbol position must be 1 or more.
     */
    @Test
    public void accordingZGBCShouldNotAddSymbolBelowPositionOne()
    {
        machine.addWheel(1);
        machine.addSymbol(0, "red");
        assertFalse(machine.ok());
    }

    /**
     * Two symbols cannot share the same slot.
     */
    @Test
    public void accordingZGBCShouldNotAddSymbolInTakenSlot()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * A symbol cannot be placed on a wheel that does not exist.
     */
    @Test
    public void accordingZGBCShouldNotPlaceSymbolOnMissingWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(9, "red");
        assertFalse(machine.ok());
    }

    /**
     * Deleting the symbol a wheel is showing moves it to the next symbol.
     */
    @Test
    public void accordingZGBCShouldMoveToNextSymbolWhenCurrentIsDeleted()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.delSymbol("red");
        assertEquals("blue", machine.configuration()[0]);
        machine.delSymbol("blue");
        assertNull(machine.configuration()[0]);
    }

    /**
     * Deleting a symbol leaves its slot empty.
     */
    @Test
    public void accordingZGBCShouldLeaveEmptySlotWhenSymbolIsDeleted()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.delSymbol("blue");
        assertArrayEquals(new String[]{"red", null}, machine.symbols());
    }

    /**
     * distinctSymbols counts the different symbols shown by the wheels.
     */
    @Test
    public void accordingZGBCShouldCountDistinctSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        assertEquals(1, machine.distinctSymbols());
        machine.spin(2);
        assertEquals(2, machine.distinctSymbols());
    }

}