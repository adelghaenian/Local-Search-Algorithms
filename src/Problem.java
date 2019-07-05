import java.util.List;

public abstract class Problem {
    public abstract State getinitalstate();
    public abstract List<State> getNeighbors(State state);
    public abstract double getEvaluation(State state);
    public abstract void createRandomInitialState();
    public abstract State[] crossOverTwoState(State s1,State s2,int cut);
    public abstract void mutateState(State state,Problem problem,double rate);
}
