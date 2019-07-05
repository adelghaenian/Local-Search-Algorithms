import java.util.*;

public class ColorGraphProblem extends Problem {

    private List<int[]> edges ;
    private int nodesCount;
    private int maxColors;
    State initialState;

    ColorGraphProblem(List<int[]> edges, int nodesCount, int maxColors) {
        this.edges= edges;
        this.nodesCount = nodesCount;
        this.maxColors=maxColors;
    }

    @Override
    public State getinitalstate() {
        return initialState;
    }

    @Override
    public List<State> getNeighbors(State state) {
        ColorGraphState colorGraphState = (ColorGraphState)state;
        List<State> neighbors = new ArrayList<>();
        //consider colorGraphState then a neighbor is colorGraphState with one changed node color
        for (int i=0;i<nodesCount;i++){
            for (int c=0;c<maxColors;c++){
                //check new color is not equal to previous color
                if (colorGraphState.colors.get(i)!=c){
                    ColorGraphState s = new ColorGraphState(colorGraphState.colors);
                    s.setColor(i,c);
                    neighbors.add(s);
                }
            }
        }
        return neighbors;
    }


    @Override
    public double getEvaluation(State state) {
    //Evaluation = sum of two adjacent nodes that are not same color
        ColorGraphState colorGraphState = (ColorGraphState)state;
        int diffrentColors=0;
        for (int[] e:edges){
            if (colorGraphState.colors.get(e[0])!=colorGraphState.colors.get(e[1]))
                diffrentColors++;
        }
        return diffrentColors;
    }


    @Override
    public void createRandomInitialState() {
    //colorize randomly nodes !

        List<Integer> colors = new ArrayList<>();
        Random rand = new Random();
        for (int i=0;i<nodesCount;i++){
            int randomColor = rand.nextInt(maxColors);
            colors.add(randomColor);
        }
        this.initialState = new ColorGraphState(colors);
    }

    @Override
    public State[] crossOverTwoState(State s1, State s2,int cut) {
        //termination condition
        if (cut==0)
            return new State[]{s1,s2};

        Random random = new Random();

        ColorGraphState cs1=(ColorGraphState)s1,cs2=(ColorGraphState)s2;

        List<Integer> newColors1 = new ArrayList<>(),newColors2 = new ArrayList<>();

        //Do crossover at crossoverIndex
        int crossoverPos = random.nextInt(cs1.colors.size());
        for (int i=0;i<crossoverPos;i++){
            newColors1.add(cs1.colors.get(i));
            newColors2.add(cs2.colors.get(i));
        }
        for (int i=crossoverPos;i<cs1.colors.size();i++){
            newColors1.add(cs2.colors.get(i));
            newColors2.add(cs1.colors.get(i));
        }

        ColorGraphState outputState1 =  new ColorGraphState(new ArrayList<>(newColors1));
        ColorGraphState outputState2 =  new ColorGraphState(new ArrayList<>(newColors2));

        return crossOverTwoState(outputState1,outputState2,cut-1);
    }

    @Override
    public void mutateState(State state,Problem problem, double rate) {
    //change one node color randomly !

        Random rnd = new Random();

        ColorGraphProblem colorGraphProblem = (ColorGraphProblem) problem;
        ColorGraphState colorGraphState = (ColorGraphState) state;

        List<Integer> colors = colorGraphState.colors;

        for(int i=0;i<colors.size();i++){
          double random = rnd.nextDouble();
            if (rate > random){
                int randomColor = rnd.nextInt(colorGraphProblem.maxColors);
                colorGraphState.setColor(i,randomColor);
            }
        }
    }

}
