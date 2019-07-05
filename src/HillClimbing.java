import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class HillClimbing {
    static int visitedNodes = 0;
    static int expandedNodes= 0;
    public static State simpleSolve(State state,Problem p){
        List<State> neighbors = p.getNeighbors(state);
        visitedNodes+=neighbors.size();

        neighbors.sort(Comparator.comparingDouble(p::getEvaluation).reversed());
        //sort by Evaluation

        State bestNeighbor = neighbors.get(0);
        //get best neighbor

        expandedNodes+=1;

        if(p.getEvaluation(bestNeighbor)>p.getEvaluation(state))
            return simpleSolve(bestNeighbor,p);
        //if best neighbor is better than current state then go to it !

        return state;
        //else return current state :) .
    }

    public static State stochasticSolve(State state,Problem p){

        List<State> neighbors = p.getNeighbors(state);
        visitedNodes+=neighbors.size();
        List<State> betterNeighbors = new ArrayList<>();

        for (State neighbor : neighbors)
            if (p.getEvaluation(neighbor) > p.getEvaluation(state))
                betterNeighbors.add(neighbor);

        if (betterNeighbors.isEmpty())return state;
        //if no better neighbors then return & exit

        for (State neighbor : betterNeighbors){

            double  possibility = 1/ (1 + Math.exp(p.getEvaluation(state)-p.getEvaluation(neighbor)));
            //Sigmoid Possibility = 1/(1+ e^(-delta(V)))

            if (Math.random()<possibility){
                expandedNodes++;
                return stochasticSolve(neighbor,p);
                //go to neighbor with sigmoid possibility
            }
        }
        return stochasticSolve(state,p);
    }

    public static State firstChoiceSolve(State state,Problem p){
        Random random = new Random();
        List<State> neighbors = p.getNeighbors(state);
        visitedNodes+=neighbors.size();

        while (!neighbors.isEmpty()){
            State neighbor = neighbors.remove(random.nextInt(neighbors.size()));
            expandedNodes++;
            // if neighbor is  better , then go to this !
            if (p.getEvaluation(neighbor)>p.getEvaluation(state))
                return firstChoiceSolve(neighbor,p);
        }
        return state;
    }

    public static State randomRestartSolve(State state,Problem p,int iterateLimit,State bestState,double bestEval){
        if (iterateLimit==0)return bestState;

        p.createRandomInitialState();

        State simpleSolve = simpleSolve(state,p);

        if (p.getEvaluation(simpleSolve)>bestEval){
            bestEval = p.getEvaluation(simpleSolve);
            bestState = simpleSolve;
        }

        return randomRestartSolve(state,p,iterateLimit-1,bestState,bestEval);
    }

}
