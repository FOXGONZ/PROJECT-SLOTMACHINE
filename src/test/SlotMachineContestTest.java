package test;

import domain.SlotMachineContest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Unit tests for SlotMachineContest: the solvers solve and solveFast.
 * The machines are solved without showing them, so no windows appear.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachineContestTest
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

    // ---------- solve ----------

    /**
     * The actions returned by solve should use wheels in the range 1..n.
     */
    @Test
    public void shouldReturnActionsWithinWheelRange() {
        int[][] actions = SlotMachineContest.solve(7);
        for (int[] action : actions) {
            assertTrue(action[0] >= 1 && action[0] <= 7);
        }
    }

    /**
     * The reported actions should never be an empty movement (zero steps).
     */
    @Test
    public void shouldNotReportAnEmptyMovement() {
        int[][] actions = SlotMachineContest.solve(n);
        for (int[] action : actions) {
            assertNotEquals(0, action[1]);
        }
    }

    /**
     * The number of actions should stay under the problem's limit.
     */
    @Test
    public void shouldReturnActionsUnderTheLimit() {
        int[][] actions = SlotMachineContest.solve(12);
        assertTrue(actions.length < 10000);
    }

    /**
     * Solve should work for several sizes, always returning a non-null result.
     */
    @Test
    public void shouldSolveDifferentSizes() {
        assertNotNull(SlotMachineContest.solve(3));
        assertNotNull(SlotMachineContest.solve(25));
        assertNotNull(SlotMachineContest.solve(50));
    }

    // ---------- limits and properties of the solutions ----------

    /**
     * solve keeps n between 1 and 50: a machine of 1 wheel needs no actions.
     */
    @Test
    public void accordingZGBCShouldSolveClampingSize()
    {
        assertEquals(0, SlotMachineContest.solve(0).length);
        assertEquals(0, SlotMachineContest.solve(-4).length);
    }

    /**
     * solve only uses positive steps smaller than n.
     */
    @Test
    public void accordingZGBCShouldSolveWithPositiveSteps()
    {
        for(int size = 2; size <= 8; size++){
            for(int[] action : SlotMachineContest.solve(size)){
                assertTrue(action[0] >= 1 && action[0] <= size);
                assertTrue(action[1] >= 1 && action[1] < size);
            }
        }
    }

    /**
     * solveFast moves each wheel at most once, with positive steps smaller than n.
     */
    @Test
    public void accordingZGBCShouldSolveFastMovingEachWheelOnce()
    {
        for(int size = 1; size <= 10; size++){
            int[][] actions = SlotMachineContest.solveFast(size);
            assertTrue(actions.length <= size);
            Set<Integer> used = new HashSet<Integer>();
            for(int[] action : actions){
                assertTrue(action[0] >= 1 && action[0] <= size);
                assertTrue(action[1] >= 1 && action[1] < size);
                assertTrue(used.add(action[0]));
            }
        }
    }

    /**
     * solveFast keeps n between 1 and 50.
     */
    @Test
    public void accordingZGBCShouldSolveFastClampingSize()
    {
        assertEquals(0, SlotMachineContest.solveFast(0).length);
        assertEquals(0, SlotMachineContest.solveFast(-2).length);
        assertTrue(SlotMachineContest.solveFast(70).length <= 50);
    }
}
