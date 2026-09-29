import java.util.ArrayList;

/**
 * Soluciona el problema I de la maraton (Slot Machine) usando una SlotMachine
 * como herramienta. Solo puede girar ruedas y preguntar cuantos simbolos
 * distintos se ven, igual que en el problema.
 *
 * @author Juan Diego Zorro Gonzalez.
 * @author Ruben Felipe Bustos Carabante.
 * @version 1.0
 */
public class SlotMachineContest
{
    public SlotMachineContest(){
    }

    /**
     * Resuelve una maquina de n ruedas y n simbolos sin mostrarla y devuelve
     * las acciones {rueda, pasos}.
     * @param n numero de ruedas y simbolos.
     * @return acciones aplicadas.
     */
    public int[][] solve(int n){
        SlotMachine machine=new SlotMachine(n);
        machine.makeInvisible();
        return play(machine,n);
    }

    /**
     * Simula la solucion mostrando la maquina.
     * @param n numero de ruedas y simbolos.
     */
    public void simulate(int n){
        SlotMachine machine=new SlotMachine(n);
        machine.makeVisible();
        play(machine,n);
    }

    private int[][] play(SlotMachine m, int n){
        ArrayList<int[]> actions=new ArrayList<int[]>();
        separateSymbols(m,n,actions);
        int[] next=wheelOrder(m,n);
        return actions.toArray(new int[0][]);
    }

    /**
     * Gira cada rueda a la posicion donde se ven mas simbolos distintos.
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
     * Halla el orden ciclico de las ruedas comparando pares por rotaciones
     * opuestas.
     * @return next[a] = rueda que sigue a la rueda a.
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
}
