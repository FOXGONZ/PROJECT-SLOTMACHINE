package test;

import domain.SlotMachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for the cycle 3: the random SlotMachine(int n) constructor.
 * The solver is tested in SlotMachineContestTest. The machine is created invisible so no windows or
 * dialogs appear during the tests.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachineC3Test
{
    private int n;

    @BeforeEach
    public void setUp()
    {
        n = new Random().nextInt(10) + 1;
    }

    @AfterEach
    public void tearDown()
    {
        n = 0;
    }

    // ---------- constructor SlotMachine(int n) ----------

    /**
     * The machine should be created with as many wheels as asked for.
     */
    @Test
    public void shouldCreateAMachineWithAsManyWheelsAsAsked() {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        assertEquals(n, machine.configuration().length);
    }

    /**
     * The machine should have as many symbols as wheels.
     */
    @Test
    public void shouldCreateAMachineWithAsManySymbolsAsWheels() {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        assertEquals(n, machine.symbols().length);
    }

    /**
     * All the symbols of the machine should be different colors.
     */
    @Test
    public void shouldCreateAMachineWithDifferentColors() {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        Set<String> colors = new HashSet<>(Arrays.asList(machine.symbols()));
        assertEquals(n, colors.size());
    }

    /**
     * The machine should not start already showing a jackpot.
     */
    @Test
    public void shouldNotStartAsJackpot() {
        SlotMachine machine = new SlotMachine(6);
        machine.makeInvisible();
        assertFalse(machine.isJackpot());
    }

    /**
     * A size lower than one should be adjusted to one wheel.
     */
    @Test
    public void shouldAdjustASizeLowerThanOne() {
        SlotMachine machine = new SlotMachine(0);
        machine.makeInvisible();
        assertEquals(1, machine.configuration().length);
    }

    /**
     * A negative size should be adjusted to one wheel.
     */
    @Test
    public void shouldAdjustANegativeSize() {
        SlotMachine machine = new SlotMachine(-5);
        machine.makeInvisible();
        assertEquals(1, machine.configuration().length);
    }

    /**
     * A size greater than the maximum should be adjusted to fifty wheels.
     */
    @Test
    public void shouldAdjustASizeGreaterThanTheMaximum() {
        SlotMachine machine = new SlotMachine(50 + 10);
        machine.makeInvisible();
        assertEquals(50, machine.configuration().length);
    }
}
