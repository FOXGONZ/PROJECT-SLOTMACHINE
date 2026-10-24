package test;

import domain.SlotMachine;

/**
 * Acceptance tests for cycle 1 of the SlotMachine.
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
public class AcceptanceCycle1Test
{
    /**
     * Plays every cycle 1 acceptance test one after another.
     */
    public void runAll()
    {
        accordingZGBCShouldBuildSpinAndWin();
        accordingZGBCShouldRejectInvalidBuildOperations();
        accordingZGBCShouldTearDownAndRebuild();
        accordingZGBCShouldShareCatalogAcrossWheels();
        accordingZGBCShouldSpinAllAndKeepAlignment();
    }

    /**
     * Builds a three-wheel machine, breaks its aligned start with a single
     * spin, then places every wheel on the same symbol so the winning light
     * turns on. You should see: three wheels appear, one wheel change on its
     * own, and finally all three show green with the jackpot light.
     */
    public void accordingZGBCShouldBuildSpinAndWin()
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
        pause(800);

        machine.spin(1);
        pause(800);

        machine.placeSymbol(1, "green");
        machine.placeSymbol(2, "green");
        machine.placeSymbol(3, "green");
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Tries three invalid operations and then a valid one. You should see that
     * nothing changes on the three rejected attempts (a second wheel in cell 1,
     * an invalid color and a duplicated symbol), and that only the valid red
     * symbol finally appears on the wheel.
     */
    public void accordingZGBCShouldRejectInvalidBuildOperations()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(1);
        pause(600);

        machine.addSymbol(1, "notAColor");
        pause(600);

        machine.addSymbol(1, "red");
        pause(600);
        machine.addSymbol(2, "red");
        pause(1200);

        machine.makeInvisible();
    }

    /**
     * Tears the catalog and the wheels down and rebuilds the machine. You
     * should see the machine empty out completely and then come back with a
     * single green wheel showing the jackpot light.
     */
    public void accordingZGBCShouldTearDownAndRebuild()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        pause(800);

        machine.delSymbol("red");
        machine.delWheel(2);
        machine.delWheel(1);
        pause(900);

        machine.addWheel(1);
        machine.addSymbol(1, "green");
        machine.placeSymbol(1, "green");
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Shows that the symbol catalog is shared by every wheel, including a hex
     * color. You should see all three wheels able to show the same hex symbol
     * #FF00AA at once, turning on the jackpot light.
     */
    public void accordingZGBCShouldShareCatalogAcrossWheels()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "#FF00AA");
        pause(800);

        machine.placeSymbol(1, "#FF00AA");
        machine.placeSymbol(2, "#FF00AA");
        machine.placeSymbol(3, "#FF00AA");
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Places two wheels on the same symbol (a jackpot) and then spins all the
     * wheels once. You should see both wheels advance together and the machine
     * stay aligned, so the jackpot light remains on.
     */
    public void accordingZGBCShouldSpinAllAndKeepAlignment()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        pause(800);

        machine.spin();
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
     * Runs every cycle 1 acceptance test from VS Code (Run button).
     * @param args not used.
     */
    public static void main(String[] args)
    {
        new AcceptanceCycle1Test().runAll();
    }
}
