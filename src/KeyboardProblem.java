
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyboardProblem extends Problem {
    String letters;

    public KeyboardProblem(String letters){
        this.letters = letters;
    }

    @Override
    public State getinitalstate() {
        return new KeyboardState(letters);
    }

    @Override
    public List<State> getNeighbors(State state) {
        KeyboardState ks = (KeyboardState) state;

        List<String> neighborsSTR = getAllNeighborsString(ks.letters);

        List<State> neighbors = new ArrayList<>();

        for (String s : neighborsSTR)
            neighbors.add(new KeyboardState(s));

        return neighbors;
    }

    @Override
    public double getEvaluation(State state) {
        KeyboardState keyboardState = (KeyboardState) state;
        String letters = keyboardState.letters;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String leftSideLetters = letters.substring(0,13);
        int[] alphabetPosition = new int[26]; //-1 means left side , +1 means right side of keyboard
        for (int i=0;i<alphabet.length();i++){
            if (leftSideLetters.contains(alphabet.charAt(i) + ""))
                alphabetPosition[i]=-1;
            else
                alphabetPosition[i]=1;
        }
        int evalOfBalance = alphabetPosition[alphabet.indexOf('e')] + alphabetPosition[alphabet.indexOf('t')] +alphabetPosition[alphabet.indexOf('a')]
                +alphabetPosition[alphabet.indexOf('i')] +alphabetPosition[alphabet.indexOf('n')] +alphabetPosition[alphabet.indexOf('o')] +alphabetPosition[alphabet.indexOf('s')] +
                alphabetPosition[alphabet.indexOf('h')] +alphabetPosition[alphabet.indexOf('r')] ;

        evalOfBalance = -1*Math.abs(evalOfBalance);

        int evalOfTwoCommon = 0;
        if (alphabetPosition[alphabet.indexOf('t')] + alphabetPosition[alphabet.indexOf('h')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('e')] + alphabetPosition[alphabet.indexOf('r')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('o')] + alphabetPosition[alphabet.indexOf('n')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('a')] + alphabetPosition[alphabet.indexOf('n')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('r')] + alphabetPosition[alphabet.indexOf('h')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('h')] + alphabetPosition[alphabet.indexOf('e')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('i')] + alphabetPosition[alphabet.indexOf('n')]==0)
            evalOfTwoCommon+=2;
        if (alphabetPosition[alphabet.indexOf('e')] + alphabetPosition[alphabet.indexOf('d')]==0)
            evalOfTwoCommon+=2;

        int sumEval = evalOfBalance+evalOfTwoCommon+ 27; //plus 27 to be sure that evaluation is positive
        if (checkDuplicate(letters))return -1; // keyboard has duplicate characters return -1 (then not choose it)
        return sumEval;
    }
    @Override
    public void createRandomInitialState() {
        letters = LetterTable.shuffleString("abcdefghijklmnopqrstuvwxyz");
    }

    @Override
    public State[] crossOverTwoState(State s1, State s2, int cut) {
        if (cut==0)
            return new State[]{s1,s2};

        Random random = new Random();

        KeyboardState ks1 =(KeyboardState) s1,ks2=(KeyboardState) s2;

        String newLetters1 = "";
        String newLetters2 = "";

        int crossoverPos = random.nextInt(ks1.letters.length());
        for (int i=0;i<crossoverPos;i++){
            newLetters1+=ks1.letters.charAt(i);
            newLetters2+=ks2.letters.charAt(i);
        }
        for (int i=crossoverPos;i<ks1.letters.length();i++){
            newLetters1+=ks2.letters.charAt(i);
            newLetters2+=ks1.letters.charAt(i);
        }

        KeyboardState newKS1 = new KeyboardState(newLetters1);
        KeyboardState newKS2 = new KeyboardState(newLetters2);
        if (getEvaluation(s1)==-1)
            return crossOverTwoState(s1,s2,cut-1); // if has duplicate do not use them !
        return crossOverTwoState(newKS1,newKS2,cut-1);
    }

    @Override
    public void mutateState(State state, Problem problem, double rate) {
        Random random = new Random();
        double rnd = random.nextDouble();
        if(rate > rnd){
            List<State> ns = getNeighbors(state);
            State s = ns.get(random.nextInt(ns.size()));
            KeyboardState ks = (KeyboardState) s;
            KeyboardState kstate = (KeyboardState) state;
            ((KeyboardState) state).letters = ks.letters;
        }
        return;
    }
    public static boolean checkDuplicate(String s) {
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j < s.length(); j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> getAllNeighborsString(String s){
        //this function returns any swap of two letters in String of s.
        List<String> neighborsString = new ArrayList<>();
        for (int i=0;i<s.length()-1;i++){
            for(int j=i+1;j<s.length();j++){
                char c1 = s.charAt(i);
                char c2 = s.charAt(j);
                neighborsString.add(s.substring(0,i) + c2 + s.substring(i+1,j) + c1 + s.substring(j+1,s.length()));
            }
        }
        return  neighborsString;
    }

}
