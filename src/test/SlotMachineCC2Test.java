package test;

import domain.SlotMachine;

/**
 * Big flow (demonstration) tests for SlotMachine.
 *
 * These tests run long visual sessions with many wheels and many symbols.
 * They do not use assertions: their purpose is to exercise a large part of
 * the behavior on screen, with pauses between steps so the whole flow can be
 * watched while it runs.
 *
 * @author  Juan Diego Zorro Gonzalez
 * @author  Ruben Felipe Bustos Carabante
 * @version 1.0
 */
public class SlotMachineCC2Test
{
    /**
     * Pauses execution so each step of the visual flow can be seen.
     */
    private void pause(int millis)
    {
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Big flow 1: fills a large machine with many wheels and many symbols,
     * then spins everything several times and forces different configurations.
     * No assertions: it is meant to be watched.
     */
    public void bigFlowManyWheelsAndSymbols()
    {
        String[] palette = {"red","blue","green","yellow","magenta",
                            "orange","pink","white","black"};

        SlotMachine game = new SlotMachine();
        game.makeVisible();
        pause(500);

        // create many wheels (two full rows of the grid)
        for(int pos=1; pos<=20; pos++){
            game.addWheel(pos);
            pause(80);
        }
        pause(400);

        // add many symbols; each one is applied to all wheels
        for(int i=0; i<palette.length; i++){
            game.addSymbol(i+1, palette[i]);
            pause(150);
        }
        pause(500);

        // spin all wheels several times to see the movement
        for(int i=0; i<6; i++){
            game.spin();
            pause(300);
        }
        pause(400);

        // build a configuration for all 20 wheels and set it at once
        String[] config = new String[20];
        for(int i=0; i<20; i++){
            config[i] = palette[i % palette.length];
        }
        game.spin(config);
        pause(700);

        // now force a full jackpot: every wheel on the same symbol
        String[] allSame = new String[20];
        for(int i=0; i<20; i++){
            allSame[i] = "red";
        }
        game.spin(allSame);
        pause(900);

        game.makeInvisible();
    }

    /**
     * Big flow 2: exercises lock, unlock, swap and stepped spins over a large
     * machine with many wheels and symbols. No assertions: watch the behavior.
     */
    public void bigFlowLockSwapAndStepsMassive()
    {
        String[] palette = {"red","blue","green","yellow","magenta",
                            "orange","pink","cyan"};

        SlotMachine game = new SlotMachine();
        game.makeVisible();
        pause(500);

        // create a full first row plus part of the second
        for(int pos=1; pos<=15; pos++){
            game.addWheel(pos);
            pause(80);
        }
        pause(300);

        // load the symbols into every wheel
        for(int i=0; i<palette.length; i++){
            game.addSymbol(i+1, palette[i]);
            pause(150);
        }
        pause(400);

        // lock the odd wheels so they stay still
        for(int pos=1; pos<=15; pos+=2){
            game.lock(pos);
            pause(100);
        }
        pause(300);

        // spin all: only the unlocked (even) wheels should move
        for(int i=0; i<4; i++){
            game.spin();
            pause(300);
        }
        pause(400);

        // unlock everything again
        for(int pos=1; pos<=15; pos+=2){
            game.unLock(pos);
            pause(100);
        }
        pause(300);

        // stepped spins on several wheels, different number of steps each
        for(int pos=1; pos<=10; pos++){
            game.spin(pos, pos);
            pause(200);
        }
        pause(400);

        // swap wheels in pairs to see them trade places
        for(int pos=1; pos+1<=14; pos+=2){
            game.swap(pos, pos+1);
            pause(250);
        }
        pause(700);

        game.makeInvisible();
    }

    /**
     * Big flow 3: triggers many invalid actions one after another so all the
     * error messages (JOptionPane) can be seen while the machine is visible.
     * No assertions: it is meant to be watched. After each invalid action the
     * machine keeps working normally.
     */
    public void bigFlowOnlyErrors()
    {
        SlotMachine game = new SlotMachine();
        game.makeVisible();
        pause(600);

        // adding a symbol with no wheels yet -> error
        game.addSymbol(1, "red");
        pause(600);

        // spinning with no wheels -> error
        game.spin();
        pause(600);

        // deleting a wheel that does not exist -> error
        game.delWheel(5);
        pause(600);

        // now create a couple of wheels to keep going
        game.addWheel(1);
        game.addWheel(2);
        pause(400);

        // adding a wheel where one already exists -> error
        game.addWheel(1);
        pause(600);

        // adding an invalid color -> error
        game.addSymbol(1, "notacolor");
        pause(600);

        // add a valid symbol, then try to reuse the same slot -> error
        game.addSymbol(1, "red");
        pause(300);
        game.addSymbol(1, "blue");
        pause(600);

        // deleting a symbol that does not exist -> error
        game.delSymbol("green");
        pause(600);

        // placing a symbol that does not exist -> error
        game.placeSymbol(1, "green");
        pause(600);

        // spinning a wheel that does not exist -> error
        game.spin(9);
        pause(600);

        // stepped spin on a missing wheel -> error
        game.spin(9, 3);
        pause(600);

        // locking / unlocking a missing wheel -> error
        game.lock(7);
        pause(600);
        game.unLock(7);
        pause(600);

        // swapping with a missing wheel -> error
        game.swap(1, 8);
        pause(600);

        // swapping when a wheel is locked -> error
        game.lock(1);
        pause(300);
        game.swap(1, 2);
        pause(600);
        game.unLock(1);
        pause(300);

        // configuration array with the wrong size -> error
        game.spin(new String[]{"red"});
        pause(600);

        // configuration array asking for a symbol a wheel does not have -> error
        game.spin(new String[]{"green", "green"});
        pause(600);

    }

    /**
     * Runs the three big flows from VS Code (Run button).
     * @param args not used.
     */
    public static void main(String[] args)
    {
        SlotMachineCC2Test flows = new SlotMachineCC2Test();
        flows.bigFlowManyWheelsAndSymbols();
        flows.bigFlowLockSwapAndStepsMassive();
        flows.bigFlowOnlyErrors();
    }
}
