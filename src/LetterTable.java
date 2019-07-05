import java.util.*;

public class LetterTable extends Problem{
    private int n,m;
    private char [][] table;
    private String usedLeters ="";
    List<char[]> dictionary;

    public LetterTable(int n,int m,char[][] table,List<char[]> dictionary){
        this.m=m;
        this.n=n;
        this.dictionary = dictionary;
        this.table = new char[n][m];
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                this.table[i][j] = table[i][j];
                usedLeters+=table[i][j];
            }
        }
    }

    @Override
    public State getinitalstate() {
        return new LetterTableState(n,m,table);
    }

    @Override
    public List<State> getNeighbors(State state) {

        List<State> neighbors = new ArrayList<>();

        List<String> stringNeigbors = getAllNeighborsString(usedLeters);

        for (String s : stringNeigbors){
            LetterTableState lts = new LetterTableState(n,m,s);
            neighbors.add(lts);
        }

        return neighbors;
    }

    @Override
    public double getEvaluation(State state) {
            LetterTableState letterTableState = (LetterTableState) state;
            int evalSum =0;
            for (int k=0;k<dictionary.size();k++){
                char[] word = dictionary.get(k);
                for (int i=0;i<n;i++){
                    for (int j=0;j<m;j++){
                        if (letterTableState.table[i][j]==word[0]){
                            int eval = findWord(letterTableState.table,n,m,i,j,word,1);
                            evalSum+= eval;
                        }
                    }
                }
            }
            return evalSum;
        }

    private static int findWord(char[][] table, int n, int m, int i, int j, char[] word, int index) {
        if (index == word.length)
            return index;

        if ( j+1< m && table[i][j+1]==word[index]){
            return findWord(table,n,m,i,j+1,word,index+1);
        }
        if ( i+1<n  &&  table[i+1][j]==word[index]){
            return findWord(table,n,m,i+1,j,word,index+1);
        }
        if ( j-1>=0  &&  table[i][j-1]==word[index] ){
            return findWord(table,n,m,i,j-1,word,index+1);

        }
        if ( i-1>=0 && table[i-1][j]==word[index] ){
            return findWord(table,n,m,i-1,j,word,index+1);
        }
        if (index==1)return 0;
        return index;
    }


    @Override
    public void createRandomInitialState() {
        String newLetters = shuffleString(usedLeters);
        LetterTableState letterTableState = new LetterTableState(n,m,newLetters);
        this.usedLeters = newLetters;
        this.table = letterTableState.table;
    }

    @Override
    public State[] crossOverTwoState(State s1, State s2, int cut) {
        if (cut==0)
            return new State[]{s1,s2};

        Random random = new Random();

        LetterTableState lts1 =(LetterTableState) s1,lts2=(LetterTableState) s2;

        String newLetters1 = "";
        String newLetters2 = "";

        int crossoverPos = random.nextInt(lts1.toString(n,m).length());
        for (int i=0;i<crossoverPos;i++){
            newLetters1+=lts1.toString(n,m).charAt(i);
            newLetters2+=lts2.toString(n,m).charAt(i);
        }
        for (int i=crossoverPos;i<lts1.toString(n,m).length();i++){
            newLetters1+=lts2.toString(n,m).charAt(i);
            newLetters2+=lts1.toString(n,m).charAt(i);
        }

        LetterTableState newLTS1 = new LetterTableState(n,m,newLetters1);
        LetterTableState newLTS2 = new LetterTableState(n,m,newLetters2);

        return crossOverTwoState(newLTS1,newLTS2,cut-1);
    }

    @Override
    public void mutateState(State state, Problem problem, double rate) {
        Random random = new Random();

        LetterTableState letterTableState = (LetterTableState)state;
        String ltsString = letterTableState.toString(n,m);

        for (int i=0;i<ltsString.length();i++){
            double d = random.nextDouble();
            if(d < rate){
                for (int j=0;j<ltsString.length();j++){
                    double dd = random.nextDouble();
                    if(dd < rate){
                        char c1 = ltsString.charAt(i);
                        char c2 = ltsString.charAt(j);
                        String newString = ltsString.substring(0,i)+c2+ltsString.substring(i+1,j)+c1+ltsString.substring(j+1,ltsString.length());
                        letterTableState.table = new LetterTableState(n,m,newString).table;
                        return;
                    }
                }
            }
        }
    }

    public static List<String> getAllNeighborsString(String s){

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
    public static String shuffleString(String string)
    {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }

}
