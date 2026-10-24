package test;

import domain.SlotMachine;
import domain.SlotMachineContest;

/**
 * Acceptance tests for cycle 3 of the SlotMachine: the marathon solver
 * (SlotMachineContest).
 *
 * This is NOT a JUnit class: each public method is one acceptance test that you
 * run from the BlueJ object bench. Every test shows the solver's machine on
 * screen and moves it with pauses so you can watch it start scrambled and end
 * on a single symbol (the jackpot light turns on). The method runAll() plays
 * every test in a row.
 *
 * Each test asks the solver for a plan with solve(n) and replays it on a
 * visible machine of the same size. SlotMachine(int n) always starts in the
 * same scrambled configuration, so a fresh machine of size n begins exactly
 * like the one the solver worked on, and replaying the actions reproduces the
 * win on screen.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class AcceptanceCycle3Test
{
    /**
     * Plays every cycle 3 acceptance test one after another.
     */
    public void runAll()
    {
        accordingZGBCShouldSolveASmallMachine();
        accordingZGBCShouldSolveAMediumMachine();
        accordingZGBCShouldSolveTheLargestMachine();
        accordingZGBCShouldSolveSeveralSizes();
    }

    /**
     * Solves a small machine (n = 3). You should first see the solver run live
     * through simulate, and then see its plan replayed step by step until all
     * three wheels show the same symbol with the jackpot light on.
     */
    public void accordingZGBCShouldSolveASmallMachine()
    {
        int n = 3;
        SlotMachineContest.simulate(n);
        int[][] plan = SlotMachineContest.solve(n);
        replayAndShow(plan, n);
    }

    /**
     * Solves a medium machine (n = 6) and replays its plan step by step. You
     * should see the six wheels start on different symbols and end all equal.
     */
    public void accordingZGBCShouldSolveAMediumMachine()
    {
        int n = 6;
        int[][] plan = SlotMachineContest.solve(n);
        replayAndShow(plan, n);
    }

    /**
     * Solves the largest allowed machine (n = 50) off screen (too many steps to
     * watch one by one) and then shows the final solved machine. You should see
     * all fifty wheels already lined up on the same symbol with the jackpot
     * light on.
     */
    public void accordingZGBCShouldSolveTheLargestMachine()
    {
        int n = 50;
        int[][] plan = SlotMachineContest.solve(n);

        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        for (int[] action : plan) {
            machine.spin(action[0], action[1]);
        }
        machine.makeVisible();
        pause(2000);
        machine.makeInvisible();
    }

    /**
     * Solves machines of several sizes (10 and 25) and shows each solved
     * machine for a moment. You should see each one appear already lined up on
     * a single symbol with the jackpot light on.
     */
    public void accordingZGBCShouldSolveSeveralSizes()
    {
        int[] sizes = {10, 25};
        for (int n : sizes) {
            int[][] plan = SlotMachineContest.solve(n);
            SlotMachine machine = new SlotMachine(n);
            machine.makeInvisible();
            for (int[] action : plan) {
                machine.spin(action[0], action[1]);
            }
            machine.makeVisible();
            pause(1500);
            machine.makeInvisible();
        }
    }

    /**
     * Replays a plan on a visible machine of n wheels, one action at a time
     * with pauses, so the solution can be watched. It ends by leaving the
     * solved machine on screen for a moment before hiding it.
     * @param plan actions {wheel, steps} returned by the solver.
     * @param n number of wheels and symbols.
     */
    private void replayAndShow(int[][] plan, int n)
    {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
        pause(800);
        for (int[] action : plan) {
            machine.spin(action[0], action[1]);
            pause(400);
        }
        pause(1500);
        machine.makeInvisible();
    }

    /**
     * Waits the given number of milliseconds so the movements can be watched.
     * @param millis milliseconds to wait.
     */
    private void pause(int millis)
    {
        try {
            Thread.sleep(millis);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs every cycle 3 acceptance test from VS Code (Run button).
     * @param args not used.
     */
    public static void main(String[] args)
    {
        new AcceptanceCycle3Test().runAll();
    }
}
