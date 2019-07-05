import java.util.List;
import java.util.Random;

class Simulated_Annealing {
    public static int viewedStates =0;
    public static int expandedStates =0;
    static State solve(State s, Problem p, double temperature, double coolingRate)  {
        Random random = new Random();

        //Termination condition
        if (temperature<0.1){
            System.out.println("-----Solved By Simulated Annealing Algorithm-----");
            System.out.println("Evaluation : " +(int) p.getEvaluation(s));
            System.out.println("VisitedNodes : " +viewedStates);
            System.out.println("ExpandedStates : " +expandedStates);
            return s;
        }

        List<State> neighbors = p.getNeighbors(s);

        viewedStates+=neighbors.size();

        State selectedNeighbor = neighbors.get(random.nextInt(neighbors.size()));
        double possibility = Math.exp((p.getEvaluation(selectedNeighbor) - p.getEvaluation(s)) / temperature);
        //cool down !
        temperature *= 1-coolingRate;
        //expand
        expandedStates++;
        if (p.getEvaluation(selectedNeighbor)>p.getEvaluation(s)){
            //if is better then go to it !
           return solve(selectedNeighbor,p,temperature,coolingRate);
        }
        //if is not better then go to it with possibility !
        else if (possibility < random.nextDouble()){
            return solve(selectedNeighbor,p,temperature,coolingRate);
        }
        return solve(s,p,temperature,coolingRate);
    }
}
