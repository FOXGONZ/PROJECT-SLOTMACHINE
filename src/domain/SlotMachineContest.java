package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves the ICPC marathon problem (Slot Machine) using a SlotMachine as a
 * tool. It can only rotate wheels and ask how many distinct symbols are shown,
 * just like in the problem.
 *
 * The strategy has three phases. First it separates the symbols so every wheel
 * shows a different one. Then it finds the cyclic order of the wheels. Finally
 * it aligns every wheel on the same symbol to reach the jackpot. Two small
 * optimizations keep the number of rotations well under the problem's limit:
 * the separation phase stops a wheel as soon as all symbols are distinct, and
 * the ordering phase skips wheels that are already known to be someone's
 * successor.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public final class SlotMachineContest
{
    /**
     * The contest is only a group of static methods, so it is not created.
     */
    private SlotMachineContest(){
        // utility class: it is never instantiated
    }

    /**
     * Solves a machine of n wheels and n symbols without showing it and returns
     * the actions {wheel, steps} that lead to the jackpot.
     * @param n number of wheels and symbols.
     * @return the actions applied, each one a pair {wheel, steps}.
     */
    public static int[][] solve(int n){
        if(n<1){
            n=1;
        }
        if(n>50){
            n=50;
        }
        SlotMachine machine=new SlotMachine(n);
        machine.makeInvisible();
        return play(machine,n);
    }

    /**
     * Simulates the solution of a machine of n wheels and n symbols showing it,
     * so the moves can be seen.
     * @param n number of wheels and symbols.
     */
    public static void simulate(int n){
        if(n<1){
            n=1;
        }
        if(n>50){
            n=50;
        }
        SlotMachine machine=new SlotMachine(n);
        machine.makeVisible();
        play(machine,n);
    }

    /**
     * Runs the strategy on the machine and returns the actions. Wheels are
     * numbered 1..n. It first makes all symbols distinct, then finds the order
     * of the wheels, and finally aligns them on the same symbol.
     * @param m the machine to solve.
     * @param n number of wheels.
     * @return the actions {wheel, steps} performed.
     */
    private static int[][] play(SlotMachine m, int n){
        List<int[]> actions=new ArrayList<int[]>();
        separateSymbols(m,n,actions);
        int[] next=wheelOrder(m,n);
        alignWheels(m,n,next,actions);
        return actions.toArray(new int[0][]);
    }

    /**
     * Rotates each wheel to the position where the most distinct symbols are
     * visible, so that at the end the n wheels show different symbols.
     * Optimization: as soon as all n symbols are already distinct the search for
     * a wheel stops early, because it cannot improve any further.
     * @param m the machine.
     * @param n number of wheels.
     * @param actions list where the applied actions are recorded.
     */
    private static void separateSymbols(SlotMachine m, int n, List<int[]> actions){
        for(int i=1;i<=n;i++){
            int best=0;
            int bestDistinct=m.distinctSymbols();
            if(bestDistinct==n){
                continue;
            }
            int acc=0;
            for(int s=1;s<n;s++){
                m.spin(i,1);
                acc++;
                if(m.distinctSymbols()>bestDistinct){
                    bestDistinct=m.distinctSymbols();
                    best=s;
                }
                if(bestDistinct==n){
                    break;
                }
            }
            m.spin(i,best-acc);
            if(best!=0){
                actions.add(new int[]{i,best});
            }
        }
    }

    /**
     * Finds the cyclic order of the wheels. It rotates one wheel one step
     * forward and another one step backward; if the number of distinct symbols
     * stays at n, the second wheel was one position ahead of the first, so it is
     * its successor. Optimization: a wheel that is already the successor of some
     * other wheel is skipped, since each wheel has a single predecessor.
     * @param m the machine.
     * @param n number of wheels.
     * @return next[a] = the wheel that follows wheel a in the cycle.
     */
    private static int[] wheelOrder(SlotMachine m, int n){
        int[] next=new int[n+1];
        boolean[] used=new boolean[n+1];
        for(int a=1;a<=n;a++){
            next[a]=-1;
            for(int b=1;b<=n;b++){
                if(a==b || used[b]){
                    continue;
                }
                m.spin(a,1);
                m.spin(b,-1);
                boolean same=m.distinctSymbols()==n;
                m.spin(b,1);
                m.spin(a,-1);
                if(same){
                    next[a]=b;
                    used[b]=true;
                    break;
                }
            }
        }
        return next;
    }

    /**
     * Walks the cyclic order from wheel 1 and rotates each wheel forward by the
     * complement of its distance, leaving them all on wheel 1's symbol.
     * @param m the machine.
     * @param n number of wheels.
     * @param next the cyclic order from wheelOrder.
     * @param actions list where the applied actions are recorded.
     */
    private static void alignWheels(SlotMachine m, int n, int[] next, List<int[]> actions){
        boolean[] done=new boolean[n+1];
        done[1]=true;
        int node=next[1];
        int shift=1;
        while(node!=-1 && node!=1 && !done[node]){
            int move=n-shift;
            m.spin(node,move);
            if(move!=0){
                actions.add(new int[]{node,move});
            }
            done[node]=true;
            node=next[node];
            shift++;
        }
    }

    /**
     * Fast and simple solver. Solves a machine of n wheels and n symbols without
     * showing it and without any animation, and returns the actions
     * {wheel, steps} that lead to the jackpot. It needs at most one move per
     * wheel.
     * @param n number of wheels and symbols.
     * @return the actions applied, each one a pair {wheel, steps}. Wheels that
     *         were already on the chosen symbol need no action and are not listed.
     */
    public static int[][] solveFast(int n){
        if(n<1){
            n=1;
        }
        if(n>50){
            n=50;
        }
        SlotMachine machine=new SlotMachine(n);
        machine.makeInvisible();
        return playFast(machine,false);
    }

    /**
     * Fast and simple solver, visual version. Solves a machine of n wheels and
     * n symbols showing it, with pauses, so the moves can be seen. It needs at
     * most one move per wheel.
     * @param n number of wheels and symbols.
     */
    public static void simulateFast(int n){
        if(n<1){
            n=1;
        }
        if(n>50){
            n=50;
        }
        SlotMachine machine=new SlotMachine(n);
        machine.makeVisible();
        playFast(machine,true);
    }

    /**
     * Runs the fast strategy on the machine and returns the actions. Unlike
     * play, it is not restricted to spinning and counting distinct symbols: it
     * reads the symbols the wheels show, picks the symbol that needs the fewest
     * total steps to line every wheel up, and spins each wheel straight to it.
     * It assumes the wheels are numbered 1..n and that none of them is locked.
     * Pauses are only made when show is true; the machine itself only animates
     * when it is visible.
     * @param machine the machine to solve.
     * @param show true to pause between moves so they can be watched.
     * @return the actions {wheel, steps} performed.
     */
    private static int[][] playFast(SlotMachine machine, boolean show){
        if(show){
            pause(800);
        }

        String[] catalog=machine.symbols();
        String[] shown=machine.configuration();

        List<Integer> occupied=new ArrayList<Integer>();
        for(int i=0;i<catalog.length;i++){
            if(catalog[i]!=null){
                occupied.add(i);
            }
        }
        int k=occupied.size();
        if(shown.length==0 || k==0){
            return new int[0][];
        }

        // slot[w] = place of the symbol shown by wheel w among the occupied slots
        int[] slot=new int[shown.length];
        for(int w=0;w<shown.length;w++){
            slot[w]=-1;
            for(int i=0;i<k;i++){
                if(catalog[occupied.get(i)].equals(shown[w])){
                    slot[w]=i;
                }
            }
            if(slot[w]==-1){
                return new int[0][];
            }
        }

        // pick the target symbol that needs the fewest steps in total
        int target=0;
        int bestTotal=Integer.MAX_VALUE;
        for(int t=0;t<k;t++){
            int total=0;
            for(int w=0;w<shown.length;w++){
                total+=(t-slot[w]+k)%k;
            }
            if(total<bestTotal){
                bestTotal=total;
                target=t;
            }
        }

        // spin each wheel straight to the target
        List<int[]> actions=new ArrayList<int[]>();
        for(int w=0;w<shown.length;w++){
            int steps=(target-slot[w]+k)%k;
            if(steps>0){
                machine.spin(w+1,steps);
                actions.add(new int[]{w+1,steps});
                if(show){
                    pause(400);
                }
            }
        }
        if(show){
            pause(1000);
        }
        return actions.toArray(new int[0][]);
    }

    /**
     * Waits the given number of milliseconds so the movements can be watched.
     * @param millis milliseconds to wait.
     */
    private static void pause(int millis){
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}