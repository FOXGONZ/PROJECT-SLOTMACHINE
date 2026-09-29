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
     * las acciones {rueda, pasos} que llevan al jackpot.
     * @param n numero de ruedas y simbolos.
     * @return acciones aplicadas, cada una es un par {rueda, pasos}.
     */
    public int[][] solve(int n){
        SlotMachine machine=new SlotMachine(n);
        machine.makeInvisible();
        return play(machine,n);
    }

    /**
     * Simula la solucion de una maquina de n ruedas y n simbolos mostrandola,
     * para poder ver los movimientos.
     * @param n numero de ruedas y simbolos.
     */
    public void simulate(int n){
        SlotMachine machine=new SlotMachine(n);
        machine.makeVisible();
        play(machine,n);
    }

    /**
     * Ejecuta la estrategia sobre la maquina y devuelve las acciones. Las
     * ruedas van de 1 a n. Primero deja todos los simbolos distintos, luego
     * halla el orden de las ruedas y por ultimo las alinea en el mismo simbolo.
     * @param m maquina a resolver.
     * @param n numero de ruedas.
     * @return acciones {rueda, pasos} realizadas.
     */
    private int[][] play(SlotMachine m, int n){
        ArrayList<int[]> actions=new ArrayList<int[]>();
        separateSymbols(m,n,actions);
        int[] next=wheelOrder(m,n);
        alignWheels(m,n,next,actions);
        return actions.toArray(new int[0][]);
    }

    /**
     * Gira cada rueda a la posicion donde se ven mas simbolos distintos, de
     * modo que al final las n ruedas muestren simbolos diferentes.
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
     * Halla el orden ciclico de las ruedas. Gira una rueda un paso adelante y
     * otra un paso atras; si se siguen viendo n simbolos distintos, la segunda
     * estaba una posicion adelante de la primera.
     * @return next[a] = rueda que sigue a la rueda a en el ciclo.
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
     * Recorre el orden ciclico desde la rueda 1 y gira cada rueda hacia atras
     * segun su distancia, dejando todas en el simbolo de la rueda 1.
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
