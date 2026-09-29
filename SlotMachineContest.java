import java.util.ArrayList;

/**
 * Solves the ICPC marathon problem (Slot Machine) using a SlotMachine as a
 * tool. It can only rotate wheels and ask how many distinct symbols are shown,
 * just like in the problem.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachineContest
{
    private SlotMachine machine;

    /**
     * Default constructor.
     */
    public SlotMachineContest(){
    }

    /**
     * Returns the last machine the contest created and solved.
     * @return the machine used in the last solve or simulate.
     */
    public SlotMachine getMachine(){
        return machine;
    }

    /**
     * Solves a machine of n wheels and n symbols without showing it and returns
     * the actions {wheel, steps} that lead to the jackpot.
     * @param n number of wheels and symbols.
     * @return the actions applied, each one a pair {wheel, steps}.
     */
    public int[][] solve(int n){
        if(n<1){
            n=1;
        }
        if(n>50){
            n=50;
        }
        machine=new SlotMachine(n);
        machine.makeInvisible();
        return play(machine,n);
    }

    /**
     * Simulates the solution of a machine of n wheels and n symbols showing it,
     * so the moves can be seen.
     * @param n number of wheels and symbols.
     */
    public void simulate(int n){
        if(n<1){
            n=1;
        }
        if(n>50){
            n=50;
        }
        machine=new SlotMachine(n);
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
    private int[][] play(SlotMachine m, int n){
        ArrayList<int[]> actions=new ArrayList<int[]>();
        separateSymbols(m,n,actions);
        int[] next=wheelOrder(m,n);
        alignWheels(m,n,next,actions);
        return actions.toArray(new int[0][]);
    }

    /**
     * Rotates each wheel to the position where the most distinct symbols are
     * visible, so that at the end the n wheels show different symbols.
     * @param m the machine.
     * @param n number of wheels.
     * @param actions list where the applied actions are recorded.
     */
    private void separateSymbols(SlotMachine m, int n, ArrayList<int[]> actions){
        for(int i=1;i<=n;i++){
            int best=0;
            int bestDistinct=m.distinctSymbols();
            for(int s=1;s<n;s++){
                m.spin(i,1);
                if(m.distinctSymbols()>bestDistinct){
                    bestDistinct=m.distinctSymbols();
                    best=s;
                }
            }
            m.spin(i,best-(n-1));
            if(best!=0){
                actions.add(new int[]{i,best});
            }
        }
    }

    /**
     * Finds the cyclic order of the wheels. It rotates one wheel one step
     * forward and another one step backward; if the number of distinct symbols
     * stays at n, the second wheel was one position ahead of the first.
     * @param m the machine.
     * @param n number of wheels.
     * @return next[a] = the wheel that follows wheel a in the cycle.
     */
    private int[] wheelOrder(SlotMachine m, int n){
        int[] next=new int[n+1];
        for(int a=1;a<=n;a++){
            next[a]=-1;
            for(int b=1;b<=n;b++){
                if(a==b){
                    continue;
                }
                m.spin(a,1);
                m.spin(b,-1);
                boolean same=(m.distinctSymbols()==n);
                m.spin(b,1);
                m.spin(a,-1);
                if(same){
                    next[a]=b;
                    break;
                }
            }
        }
        return next;
    }

    /**
     * Walks the cyclic order from wheel 1 and rotates each wheel backward by its
     * distance, leaving them all on wheel 1's symbol.
     * @param m the machine.
     * @param n number of wheels.
     * @param next the cyclic order from wheelOrder.
     * @param actions list where the applied actions are recorded.
     */
    private void alignWheels(SlotMachine m, int n, int[] next, ArrayList<int[]> actions){
        boolean[] done=new boolean[n+1];
        done[1]=true;
        int node=next[1];
        int shift=1;
        while(node!=-1 && node!=1 && !done[node]){
            m.spin(node,-shift);
            actions.add(new int[]{node,-shift});
            done[node]=true;
            node=next[node];
            shift++;
        }
    }
}