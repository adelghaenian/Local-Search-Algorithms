import java.util.*;

public class Genetic {
    public static State solveGenetic(List<State> currentChromosomes, Problem p, int population, int iterationLimit,int cuts,double mutationRate){
        //termination condition
        if (iterationLimit==0){
            currentChromosomes.sort(Comparator.comparingDouble(p::getEvaluation).reversed());
            State bestState = currentChromosomes.get(0);
            return bestState;
        }

        State bestState = currentChromosomes.get(0);
        State averageState = currentChromosomes.get(currentChromosomes.size()/2);
        State worstState = currentChromosomes.get(currentChromosomes.size()-1);

        System.out.println("best Eval: "+ p.getEvaluation(bestState) );
        System.out.println("average Eval: "+ p.getEvaluation(averageState) );
        System.out.println("worst Eval: "+ p.getEvaluation(worstState) );
        System.out.println();

        List<State> selectedStates = new ArrayList<>();
        //Random collection is a collector that picks one state by random due to its evaluation
        // (choosing possibility has related to its evaluation)
        RandomCollection<State> rc = new RandomCollection<>();

        for(State s:currentChromosomes){
            rc.add(p.getEvaluation(s),s);
        }

        //select new population
        for (int i=0;i<population;i++){
            selectedStates.add(rc.next());
        }
        //Caution !! population must greater than 2
        List<State> crossOveredStated = crossOver(selectedStates,p,cuts);

        //lets mutate some chromosomes !
        List<State> mutatedStates = new ArrayList<>(crossOveredStated);
        for (State s : mutatedStates){
            p.mutateState(s,p,mutationRate);
        }

        currentChromosomes.addAll(mutatedStates);

        return solveGenetic(currentChromosomes,p,population,iterationLimit-1,cuts,mutationRate);
    }

    private static List<State> crossOver(List<State> selectedStates,Problem p,int cuts) {
        List<State> crossOvered = new ArrayList<>();
        for (int i=1;i<selectedStates.size();i=i+2){
            State s1 = selectedStates.get(i);
            State s2 = selectedStates.get(i-1);
            State[] crossOverStates = p.crossOverTwoState(s1,s2,cuts);
            crossOvered.add(crossOverStates[0]);
            crossOvered.add(crossOverStates[1]);
        }
        return crossOvered;
    }
    public static class RandomCollection<E> {
        private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
        private final Random random;
        private double total = 0;

        public RandomCollection() {
            this(new Random());
        }

        public RandomCollection(Random random) {
            this.random = random;
        }

        public RandomCollection<E> add(double weight, E result) {
            if (weight <= 0) return this;
            total += weight;
            map.put(total, result);
            return this;
        }

        public E next() {
            double value = random.nextDouble() * total;
            return map.higherEntry(value).getValue();
        }
    }
}
