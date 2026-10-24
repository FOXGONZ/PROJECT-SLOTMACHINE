package test;

import domain.SlotMachine;

/**
 * Acceptance tests for cycle 4 of the SlotMachine: typed wheels (normal,
 * rebel, lefty, wild) and typed symbols (normal, shy, ephemeral).
 *
 * This is NOT a JUnit class: each public method is one acceptance test that you
 * run from the BlueJ object bench. Every test shows the machine on screen and
 * moves it with pauses, so you can watch it and decide whether it behaved as
 * described in its comment. When an action is rejected (for example deleting
 * a rebel wheel), the machine shows a message: click OK to continue.
 *
 * Frame colors: normal wheels are light gray, rebel orange, lefty cyan and
 * wild magenta; a locked wheel is dark gray. Each time a wheel lands on a
 * symbol, that symbol turns: a shy symbol hides (or shows up again) and an
 * ephemeral one shrinks. A hidden shy symbol leaves the window empty.
 * The method runAll() plays every test in a row.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class AcceptanceCycle4Test
{
    /**
     * Plays every cycle 4 acceptance test one after another.
     */
    public void runAll()
    {
        accordingZGBCShouldShowRebelResisting();
        accordingZGBCShouldShowLeftyCopying();
        accordingZGBCShouldShowWildWheelMatching();
        accordingZGBCShouldShowTypedSymbols();
        accordingZGBCShouldShowShyWithLefty();
        accordingZGBCShouldShowEphemeralShrinking();
        accordingZGBCShouldShowMixedMachine();
        accordingZGBCShouldPlayCompleteCycle4Session();
    }

    /**
     * A rebel wheel (cell 2) between two normal wheels. You should see:
     * all three on red with the jackpot light on; after locking the rebel its
     * frame stays orange (it does not turn dark) and it still spins to blue,
     * so the light goes off; a message when trying to delete it and another
     * when trying to swap it, with nothing changing; and, for comparison,
     * wheel 3 turning dark when it is locked and not moving when it is spun.
     */
    public void accordingZGBCShouldShowRebelResisting()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel("rebel", 2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");
        pause(1200);

        machine.lock(2);
        pause(800);
        machine.spin(2);
        pause(1200);

        machine.delWheel(2);
        pause(800);
        machine.swap(2, 3);
        pause(800);

        machine.lock(3);
        pause(800);
        machine.spin(3);
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Two lefty wheels: one in cell 4 next to a normal wheel in cell 3, and
     * one in cell 7 with cell 6 empty. You should see: the lefty in cell 4
     * copy green and then blue from cell 3, whether it spins once or several
     * steps; the lefty in cell 7 not move while cell 6 is empty, and copy
     * green as soon as a wheel appears there; and, once the lefty in cell 4
     * is locked (dark frame), it keeps blue even if cell 3 changes to red.
     */
    public void accordingZGBCShouldShowLeftyCopying()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(3);
        machine.addWheel("lefty", 4);
        machine.addWheel("lefty", 7);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        pause(800);

        machine.placeSymbol(3, "green");
        pause(600);
        machine.spin(4);
        pause(1000);

        machine.placeSymbol(3, "blue");
        pause(600);
        machine.spin(4, 3);
        pause(1000);

        machine.spin(7);
        pause(1000);
        machine.addWheel(6);
        machine.placeSymbol(6, "green");
        pause(600);
        machine.spin(7);
        pause(1000);

        machine.lock(4);
        machine.placeSymbol(3, "red");
        pause(600);
        machine.spin(4);
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * A wild wheel (cell 2) between two normal wheels. You should see: the
     * jackpot light on with red, green and red, because the wild wheel matches
     * anything; the light off when wheel 3 changes to blue, even after the
     * wild wheel spins; and the light on again when wheel 1 also shows blue.
     */
    public void accordingZGBCShouldShowWildWheelMatching()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel("wild", 2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "green");
        machine.placeSymbol(3, "red");
        pause(1500);

        machine.placeSymbol(3, "blue");
        pause(1000);
        machine.spin(2);
        pause(800);
        machine.spin(2);
        pause(1000);

        machine.placeSymbol(1, "blue");
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Normal, shy and ephemeral symbols in the same catalog. Here the wheels
     * are only placed, never spun, so no symbol turns. You should see:
     * the three symbols shown by the wheels; a third wheel added afterwards
     * that can also show the shy symbol (it inherits the catalog); and a swap
     * in which every wheel takes its symbols with it to the new cell.
     */
    public void accordingZGBCShouldShowTypedSymbols()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol("normal", 1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("ephemeral", 3, "green");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "green");
        pause(1200);

        machine.addWheel(3);
        machine.placeSymbol(3, "blue");
        pause(1200);

        machine.swap(1, 3);
        pause(1200);
        machine.swap(2, 3);
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Two shy symbols (blue and green), a normal one (red) and a lefty wheel
     * (cell 2, cyan frame) next to a normal wheel (cell 1). You should see:
     * <ol>
     * <li>Both wheels on red with the jackpot light on.</li>
     * <li>Wheel 1 spins onto blue, which hides: its window is empty and the
     *     light goes off.</li>
     * <li>The lefty spins but has nothing to copy, so it stays red.</li>
     * <li>Wheel 1 spins onto green, which also hides: still empty; the lefty
     *     still cannot copy.</li>
     * <li>Wheel 1 spins onto red and the lefty copies it: light on.</li>
     * <li>Wheel 1 spins onto blue, which now shows up again, and the lefty
     *     copies blue: light on again.</li>
     * </ol>
     */
    public void accordingZGBCShouldShowShyWithLefty()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("shy", 3, "green");
        pause(1200);

        machine.spin(1);
        pause(1000);
        machine.spin(2);
        pause(1000);

        machine.spin(1);
        pause(800);
        machine.spin(2);
        pause(1000);

        machine.spin(1);
        pause(800);
        machine.spin(2);
        pause(1200);

        machine.spin(1);
        pause(800);
        machine.spin(2);
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * Two normal wheels with a red symbol and an ephemeral green one. Wheel 2
     * is placed on green without spinning, so its green keeps its size. You
     * should see wheel 1 landing on green again and again, its green circle
     * getting smaller every time, while the green in wheel 2 stays big. Each
     * time both wheels show green the jackpot light turns on, and it goes off
     * when wheel 1 moves to red.
     */
    public void accordingZGBCShouldShowEphemeralShrinking()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol("ephemeral", 2, "green");
        machine.placeSymbol(2, "green");
        pause(1000);

        for (int i = 0; i < 5; i++) {
            machine.spin(1);
            pause(700);
            machine.spin(1);
            pause(400);
        }
        machine.spin(1);
        pause(1500);

        machine.makeInvisible();
    }

    /**
     * A machine mixing every kind: cell 1 normal (gray), cell 2 lefty (cyan),
     * cell 3 wild (magenta) and cell 4 rebel (orange), with a normal red
     * symbol, a shy blue one and an ephemeral green one. You should see:
     * <ol>
     * <li>All four on red with the jackpot light on.</li>
     * <li>Wheel 1 lands on blue, which hides: its window is empty and the
     *     light goes off.</li>
     * <li>The lefty spins but has nothing to copy, so it stays red.</li>
     * <li>Wheel 1 lands on green, which shrinks; the lefty then copies green
     *     (its own green keeps the normal size, because it did not land on it
     *     by spinning).</li>
     * <li>The wild wheel lands on blue, which hides in its window, but the
     *     wild wheel still counts as green.</li>
     * <li>The rebel is locked, its frame stays orange, and it spins onto blue,
     *     which hides.</li>
     * <li>The rebel is placed on green: every wheel counts as green and the
     *     jackpot light turns on.</li>
     * </ol>
     */
    public void accordingZGBCShouldShowMixedMachine()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.addWheel("wild", 3);
        machine.addWheel("rebel", 4);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("ephemeral", 3, "green");
        pause(1500);

        machine.spin(1);
        pause(1000);
        machine.spin(2);
        pause(1000);

        machine.spin(1);
        pause(800);
        machine.spin(2);
        pause(1200);

        machine.spin(3);
        pause(1200);

        machine.lock(4);
        pause(600);
        machine.spin(4);
        pause(1200);

        machine.placeSymbol(4, "green");
        pause(2000);

        machine.makeInvisible();
    }

    /**
     * Complete cycle 4 session with six wheels of every type (1 normal,
     * 2 rebel, 3 lefty, 4 wild, 5 and 6 normal) and symbols of every type.
     * You should see, step by step:
     * <ol>
     * <li>The machine built and already in a jackpot (all wheels start on the
     *     first symbol, red).</li>
     * <li>Most wheels set to blue and the wild one to green; the lefty stays
     *     red, so there is no jackpot.</li>
     * <li>The lefty spins and copies blue from the rebel: jackpot light on,
     *     even though the wild wheel shows green.</li>
     * <li>The rebel is locked but its frame stays orange, and it spins to
     *     green (an ephemeral symbol, which shrinks as it lands): the light
     *     goes off.</li>
     * <li>The lefty copies green from the rebel; then wheels 1, 5 and 6 are
     *     set to green: jackpot light on again.</li>
     * <li>Deleting the rebel shows a message and it stays; swapping it shows
     *     another message and nothing moves.</li>
     * <li>Normal wheel 6 is deleted and disappears; the jackpot remains.</li>
     * <li>Wheel 5 is locked (dark frame) and every wheel spins: wheel 5 keeps
     *     green while the others move to red, so the light goes off.</li>
     * <li>Wheel 5 is unlocked and a symbol array puts every wheel on red
     *     (the wild wheel on blue): jackpot light on to finish.</li>
     * </ol>
     */
    public void accordingZGBCShouldPlayCompleteCycle4Session()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeVisible();
        pause(600);

        // 1. build every wheel type and every symbol type
        machine.addWheel(1);
        machine.addWheel("rebel", 2);
        machine.addWheel("lefty", 3);
        machine.addWheel("wild", 4);
        machine.addWheel(5);
        machine.addSymbol(1, "red");
        machine.addSymbol("shy", 2, "blue");
        machine.addSymbol("ephemeral", 3, "green");
        machine.addWheel(6);
        pause(1500);

        // 2. set the wheels; the lefty is left behind
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(4, "green");
        machine.placeSymbol(5, "blue");
        machine.placeSymbol(6, "blue");
        pause(1200);

        // 3. the lefty copies its left neighbour: jackpot thanks to the wild
        machine.spin(3);
        pause(1500);

        // 4. the rebel refuses the lock and keeps spinning
        machine.lock(2);
        pause(600);
        machine.spin(2);
        pause(1200);

        // 5. the lefty follows the rebel; the normal wheels catch up
        machine.spin(3);
        pause(800);
        machine.placeSymbol(1, "green");
        machine.placeSymbol(5, "green");
        machine.placeSymbol(6, "green");
        pause(1500);

        // 6. the rebel cannot be deleted nor swapped
        machine.delWheel(2);
        pause(800);
        machine.swap(2, 5);
        pause(800);

        // 7. a normal wheel can be deleted
        machine.delWheel(6);
        pause(1200);

        // 8. a locked normal wheel stays while the rest spin
        machine.lock(5);
        pause(600);
        machine.spin();
        pause(1500);

        // 9. back to a jackpot with a symbol array
        machine.unLock(5);
        machine.spin(new String[]{"red", "red", "red", "blue", "red"});
        pause(2000);

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
     * Runs every cycle 4 acceptance test from VS Code (Run button).
     * @param args not used.
     */
    public static void main(String[] args)
    {
        new AcceptanceCycle4Test().runAll();
    }
}
