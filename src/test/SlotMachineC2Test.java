package test;

import domain.SlotMachine;
import domain.Wheel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SlotMachineC2Test
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

    /**
     * Loads five colors (red, blue, green, yellow, magenta) into slots 1..5,
     * so every wheel shares the same catalog. Used by tests that need several
     * symbols to move a wheel a precise number of steps.
     */
    private void loadFiveSymbols()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addSymbol(4, "yellow");
        machine.addSymbol(5, "magenta");
    }

    // ---------- swap ----------

    @Test
    public void accordingZGBCShouldSwapTwoWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(2, "blue");
        String pos1Before = machine.configuration()[0];
        String pos2Before = machine.configuration()[1];
        machine.swap(1, 2);
        assertTrue(machine.ok());
        assertEquals(pos1Before, machine.configuration()[1]);
        assertEquals(pos2Before, machine.configuration()[0]);
    }

    @Test
    public void accordingZGBCShouldNotSwapMissingWheel()
    {
        machine.addWheel(1);
        machine.swap(1, 8);
        assertFalse(machine.ok());
    }

    @Test
    public void accordingZGBCShouldNotSwapLockedWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(1);
        machine.swap(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void accordingZGBCShouldSwapMovingBothWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        loadFiveSymbols();
        machine.placeSymbol(1, "green");
        machine.placeSymbol(2, "magenta");
        machine.swap(1, 2);
        assertTrue(machine.ok());
        assertEquals("magenta", machine.configuration()[0]);
        assertEquals("green", machine.configuration()[1]);
    }

    // ---------- lock ----------

    @Test
    public void accordingZGBCShouldLockAndNotSpinWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        loadFiveSymbols();
        machine.lock(1);
        String lockedBefore = machine.configuration()[0];
        String freeBefore = machine.configuration()[1];
        machine.spin(1);
        machine.spin(2);
        // the locked wheel stays, the free one moves
        assertEquals(lockedBefore, machine.configuration()[0]);
        assertNotEquals(freeBefore, machine.configuration()[1]);
    }

    @Test
    public void accordingZGBCShouldNotLockMissingWheel()
    {
        machine.lock(5);
        assertFalse(machine.ok());
    }

    // ---------- unlock ----------

    @Test
    public void accordingZGBCShouldUnlockAndSpinWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        loadFiveSymbols();
        machine.lock(1);
        machine.unLock(1);
        String before = machine.configuration()[0];
        machine.spin(1);
        assertNotEquals(before, machine.configuration()[0]);
    }

    @Test
    public void accordingZGBCShouldNotUnlockMissingWheel()
    {
        machine.unLock(5);
        assertFalse(machine.ok());
    }

    // ---------- spin(wheel, steps) ----------

    @Test
    public void accordingZGBCShouldSpinSeveralStepsAndWrapAround()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String before = machine.configuration()[0];
        machine.spin(1, 2);
        assertEquals(before, machine.configuration()[0]);
    }

    @Test
    public void accordingZGBCShouldSpinStepsToPreciseSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        loadFiveSymbols();
        String wheel2Before = machine.configuration()[1];
        // red -> blue -> green (two steps)
        machine.spin(1, 2);
        assertEquals("green", machine.configuration()[0]);
        // the other wheel must NOT move
        assertEquals(wheel2Before, machine.configuration()[1]);
    }

    @Test
    public void accordingZGBCShouldSpinStepsAdvanceWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String before = machine.configuration()[0];
        machine.spin(1, 1);
        assertNotEquals(before, machine.configuration()[0]);
    }

    @Test
    public void accordingZGBCShouldNotSpinStepsOnMissingWheel()
    {
        machine.spin(9, 3);
        assertFalse(machine.ok());
    }

    @Test
    public void accordingZGBCShouldNotSpinStepsOnLockedWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.lock(1);
        String before = machine.configuration()[0];
        machine.spin(1, 3);
        assertEquals(before, machine.configuration()[0]);
    }

    @Test
    public void accordingZGBCShouldSpinBothWheelsStepsToDifferentPositions()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        loadFiveSymbols();
        machine.spin(1, 2);   // red -> blue -> green
        machine.spin(2, 4);   // red -> ... -> magenta
        assertEquals("green", machine.configuration()[0]);
        assertEquals("magenta", machine.configuration()[1]);
        assertNotEquals(machine.configuration()[0], machine.configuration()[1]);
    }

    // ---------- spin() global ----------

    @Test
    public void accordingZGBCShouldSpinAllWheelsAtOnce()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String[] before = machine.configuration().clone();
        machine.spin();
        // every wheel must have advanced
        assertNotEquals(before[0], machine.configuration()[0]);
        assertNotEquals(before[1], machine.configuration()[1]);
        assertNotEquals(before[2], machine.configuration()[2]);
    }

    @Test
    public void accordingZGBCShouldNotSpinAllWithoutWheels()
    {
        machine.spin();
        assertFalse(machine.ok());
    }

    @Test
    public void accordingZGBCShouldSpinAllButNotLockedWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.lock(1);
        String lockedBefore = machine.configuration()[0];
        String freeBefore = machine.configuration()[1];
        machine.spin();
        // locked wheel stays, the rest move
        assertEquals(lockedBefore, machine.configuration()[0]);
        assertNotEquals(freeBefore, machine.configuration()[1]);
    }

    // ---------- spin(String[]) ----------

    @Test
    public void accordingZGBCShouldPositionWheelsWithSymbolArray()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(new String[]{"blue", "red"});
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
    }

    @Test
    public void accordingZGBCShouldPositionBothWheelsToDifferentSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        loadFiveSymbols();
        machine.spin(new String[]{"green", "magenta"});
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
        assertEquals("magenta", machine.configuration()[1]);
        assertNotEquals(machine.configuration()[0], machine.configuration()[1]);
    }

    @Test
    public void accordingZGBCShouldSkipLockedWheelInSymbolArray()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(new String[]{"blue", "red"});
        assertEquals("red", machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
    }

    @Test
    public void accordingZGBCShouldNotPositionWithWrongArraySize()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.spin(new String[]{"red"});
        assertFalse(machine.ok());
    }

    @Test
    public void accordingZGBCShouldNotPositionWithUnknownSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.spin(new String[]{"green"});
        assertFalse(machine.ok());
    }

    // ---------- more cases of cycle 2 ----------

    /**
     * A swap is not valid when the second wheel does not exist.
     */
    @Test
    public void accordingZGBCShouldNotSwapWhenSecondWheelIsMissing()
    {
        machine.addWheel(1);
        machine.swap(1, 9);
        assertFalse(machine.ok());
    }

    /**
     * A swap is not valid when the second wheel is locked.
     */
    @Test
    public void accordingZGBCShouldNotSwapWithLockedSecondWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(2);
        machine.swap(1, 2);
        assertFalse(machine.ok());
        machine.unLock(2);
        machine.swap(1, 2);
        assertTrue(machine.ok());
    }

    /**
     * Positioning with a null array is not valid.
     */
    @Test
    public void accordingZGBCShouldNotPositionWithNullArray()
    {
        machine.addWheel(1);
        loadFiveSymbols();
        machine.spin((String[]) null);
        assertFalse(machine.ok());
    }

    /**
     * Negative steps spin the wheel backward and wrap around.
     */
    @Test
    public void accordingZGBCShouldSpinStepsBackward()
    {
        machine.addWheel(1);
        loadFiveSymbols();
        machine.spin(1, -1);
        assertEquals("magenta", machine.configuration()[0]);
        machine.spin(1, -2);
        assertEquals("green", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * Zero steps leave the wheel where it is.
     */
    @Test
    public void accordingZGBCShouldNotMoveWithZeroSteps()
    {
        machine.addWheel(1);
        loadFiveSymbols();
        machine.spin(1, 0);
        assertEquals("red", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * A wheel tells whether it is locked and whether it can move or swap.
     */
    @Test
    public void accordingZGBCShouldTellIfWheelCanMove()
    {
        Wheel wheel = new Wheel(4);
        assertEquals(4, wheel.getPosition());
        assertFalse(wheel.isLocked());
        assertTrue(wheel.canMove());
        assertTrue(wheel.canSwap());
        assertTrue(wheel.remove());
        wheel.lock();
        assertTrue(wheel.isLocked());
        assertFalse(wheel.canMove());
        assertFalse(wheel.canSwap());
        wheel.unLock();
        assertFalse(wheel.isLocked());
        assertTrue(wheel.canMove());
    }

    /**
     * A locked wheel does not change its symbol even when one is placed.
     */
    @Test
    public void accordingZGBCShouldNotPlaceSymbolOnLockedWheel()
    {
        Wheel wheel = new Wheel(1);
        wheel.setSymbol(0, "red");
        wheel.setSymbol(1, "blue");
        wheel.lock();
        wheel.placeSymbol("blue");
        wheel.spin();
        wheel.spin(1);
        assertEquals("red", wheel.currentSymbol());
    }

    /**
     * A moved wheel keeps its symbols and the symbol it was showing.
     */
    @Test
    public void accordingZGBCShouldKeepSymbolsWhenWheelIsMoved()
    {
        Wheel wheel = new Wheel(1);
        wheel.setSymbol(0, "red");
        wheel.setSymbol(2, "blue");
        wheel.spin();
        wheel.moveTo(12);
        assertEquals(12, wheel.getPosition());
        assertEquals("blue", wheel.currentSymbol());
        assertArrayEquals(new String[]{"red", null, "blue"}, wheel.getSymbolsColor());
    }

    /**
     * A wheel without symbols, or whose symbols were all removed, cannot spin.
     */
    @Test
    public void accordingZGBCShouldNotSpinWheelWithoutSymbols()
    {
        Wheel wheel = new Wheel(1);
        wheel.spin();
        wheel.spin(2);
        assertNull(wheel.currentSymbol());
        wheel.setSymbol(0, "red");
        wheel.delSymbol("red");
        wheel.spin();
        wheel.spin(2);
        wheel.spin(-2);
        assertNull(wheel.currentSymbol());
    }

    /**
     * A wheel ignores unknown symbols and spins over empty slots.
     */
    @Test
    public void accordingZGBCShouldIgnoreUnknownSymbolsInWheel()
    {
        Wheel wheel = new Wheel(1);
        wheel.setSymbol(0, "red");
        wheel.setSymbol(2, "blue");
        wheel.delSymbol("pink");
        wheel.placeSymbol("pink");
        assertEquals("red", wheel.currentSymbol());
        assertFalse(wheel.hasColor("pink"));
        assertTrue(wheel.hasColor("blue"));
        wheel.spin(-1);
        assertEquals("blue", wheel.currentSymbol());
        wheel.delSymbol("red");
        assertEquals("blue", wheel.currentSymbol());
    }


}