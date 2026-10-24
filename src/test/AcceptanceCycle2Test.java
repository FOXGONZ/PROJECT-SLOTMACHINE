package test;

import domain.SlotMachine;

/**
 * Acceptance tests for cycle 2 of the SlotMachine.
 *
 * This is NOT a JUnit class: each public method is one acceptance test that you
 * run from the BlueJ object bench. Every test shows the machine on screen and
 * moves it with pauses so you can watch the scenario and judge for yourself
 * whether it behaved correctly. The method runAll() plays every test in a row.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class AcceptanceCycle2Test
{
    /**
     * Plays every cycle 2 acceptance test one after another.
     */
    public void runAll()
    {
        accordingZGBCShouldProtectAndReleaseALockedWheel();
        accordingZGBCShouldSwapWheelsAndRejectLockedSwap();
        accordingZGBCShouldPositionWithArrayAndSkipLocked();
        accordingZGBCShouldStepWheelAndWrapAround();
        accordingZGBCShouldSwapThenLineUpAJackpot();
    }

    /**
     * Locks wheel 1 on red and then attacks it with a full spin, a multi-step
     * spin and a symbol array. You should see wheel 1 stay on red through all
     * three attempts, and only start moving after it is unlocked.
     */
    public void accordingZGBCShouldProtectAndReleaseALockedWheel()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        pause(700);

        machine.spin();
        pause(700);

        machine.spin(1, 3);
        pause(700);

        machine.spin(new String[]{"blue", "red"});
        pause(700);

        machine.unLock(1);
        machine.spin(1);
        pause(1200);

        machine.makeInvisible();
    }

    /**
     * Swaps two wheels and then tries to swap again with one of them locked.
     * You should see red and cyan exchange cells on the first swap, and nothing
     * move on the second swap because wheel 1 is locked.
     */
    public void accordingZGBCShouldSwapWheelsAndRejectLockedSwap()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "cyan");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "cyan");
        pause(700);

        machine.swap(1, 2);
        pause(900);

        machine.lock(1);
        machine.swap(1, 2);
        pause(1200);

        machine.makeInvisible();
    }

    /**
     * Positions three wheels with a symbol array, then locks the middle wheel
     * and sends another array. You should see the first array place blue,
     * green, red; then wheel 2 stay put while the others follow the second
     * array. The wrong-sized and unknown-symbol arrays at the end should move
     * nothing.
     */
    public void accordingZGBCShouldPositionWithArrayAndSkipLocked()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        pause(700);

        machine.spin(new String[]{"blue", "green", "red"});
        pause(900);

        machine.placeSymbol(2, "blue");
        machine.lock(2);
        machine.spin(new String[]{"green", "red", "blue"});
        pause(900);

        machine.unLock(2);
        machine.spin(new String[]{"red", "blue"});
        pause(600);

        machine.spin(new String[]{"red", "pink", "green"});
        pause(1200);

        machine.makeInvisible();
    }

    /**
     * Steps a single wheel over a two-symbol catalog. You should see the wheel
     * return to its starting symbol after two steps, move to the other symbol
     * after one more step, and nothing happen when a missing wheel is stepped.
     */
    public void accordingZGBCShouldStepWheelAndWrapAround()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addSymbol(1, "orange");
        machine.addSymbol(2, "pink");
        pause(700);

        machine.spin(1, 2);
        pause(800);

        machine.spin(1, 1);
        pause(800);

        machine.spin(9, 1);
        pause(1200);

        machine.makeInvisible();
    }

    /**
     * Starts with two wheels showing different symbols, swaps them and then
     * lines them up on the same symbol. You should see red and yellow exchange
     * cells and then both become red, turning on the jackpot light.
     */
    public void accordingZGBCShouldSwapThenLineUpAJackpot()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "yellow");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "yellow");
        pause(700);

        machine.swap(1, 2);
        pause(900);

        machine.placeSymbol(1, "red");
        pause(1200);

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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs every cycle 2 acceptance test from VS Code (Run button).
     * @param args not used.
     */
    public static void main(String[] args)
    {
        new AcceptanceCycle2Test().runAll();
    }
}
