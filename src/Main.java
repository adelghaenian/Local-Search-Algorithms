import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //coloringGraphConfig();
        //letterTableConfig();
        keyboardLettersConfig();
    }
    private static void keyboardLettersConfig() {
        KeyboardProblem keyboardProblem = new KeyboardProblem("abcdefghijklmnopqrstuvwxyz");
        Scanner scanner = new Scanner(System.in);
        List<State> states = new ArrayList<>();
        System.out.println("Enter initial population count : ");
        int initialPopulation = scanner.nextInt();
        for (int i=0;i<initialPopulation;i++){
            keyboardProblem.createRandomInitialState();
            KeyboardState ks = new KeyboardState(keyboardProblem.letters);
            states.add(ks);
            System.out.println("chromosome "+ i + ":     "+ ks.letters.substring(0,13) + "    |    " + ks.letters.substring(13,26) + "    eval : " + keyboardProblem.getEvaluation(ks));
        }
        System.out.println("enter population:");
        int population = scanner.nextInt();
        System.out.println("enter iterationLimit:");
        int iterationLimit = scanner.nextInt();
        System.out.println("enter cuts count in each crossover:");
        int cuts = scanner.nextInt();
        System.out.println("enter mutation rate:");
        double mutationRate = scanner.nextDouble();
        System.out.println("-----------Solution of Genetic----------");
        KeyboardState solve = (KeyboardState) Genetic.solveGenetic(states,keyboardProblem,population,iterationLimit,cuts,mutationRate);
        System.out.println(solve.letters.substring(0,13) + "    |    " + solve.letters.substring(13,26) + "    eval : " + keyboardProblem.getEvaluation(solve));
    }
    private static void letterTableConfig() {
        Scanner sc = new Scanner(System.in);
        int n,m;
        System.out.println("Enter table Rows And Column size : n m ");
        n=sc.nextInt();
        m=sc.nextInt();
        char[][] table = new char[n][m];
        System.out.println("Enter table (with a space between each two characters)");
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                table[i][j]=sc.next().charAt(0);
            }
        }
        System.out.println("How many words is in dictionary ? :");
        int wordsCount =  sc.nextInt();
        List<char[]> dictionary = new ArrayList<>();
        System.out.println("enter dictionary words ");
        for (int i=0;i<wordsCount;i++)
        {
            String word = sc.next();
            dictionary.add(word.toCharArray());
        }
        System.out.println("enter Temperature and CoolingRate : ");

        double tmp = sc.nextDouble();
        double coolingrate = sc.nextDouble();
        LetterTable letterTable = new LetterTable(n,m,table,dictionary);
        LetterTableState solved = (LetterTableState) Simulated_Annealing.solve(letterTable.getinitalstate(),letterTable,tmp,coolingrate);
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                System.out.print(solved.table[i][j] + " ");
            }
            System.out.println();
        }
        solved = (LetterTableState) HillClimbing.randomRestartSolve(letterTable.getinitalstate(),letterTable,100,null,-1);
        System.out.println("----Solution by RandomRestart HC :-----");
        System.out.println("eval :" + letterTable.getEvaluation(solved));
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                System.out.print(solved.table[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void coloringGraphConfig() {
        int nodesCount,edgesCount,a,b;
        List<int[]> edges = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("enter number of nodes:");
        nodesCount=sc.nextInt();
        System.out.println("enter number of edges:");
        edgesCount=sc.nextInt();
        System.out.println("enter edges ( a--->b ) : ");
        for (int i=0;i<edgesCount;i++){
            a=sc.nextInt();
            b=sc.nextInt();
            int[] edge = new int[]{a-1,b-1};
            edges.add(edge);
        }
        System.out.println("Enter MaxColors  : ");
        int maxColors = sc.nextInt();
        ColorGraphProblem colorGraphProblem = new ColorGraphProblem(edges,nodesCount,maxColors);
        colorGraphProblem.createRandomInitialState();
        State solved = HillClimbing.simpleSolve(colorGraphProblem.initialState,colorGraphProblem);
        List<Integer> colors = ((ColorGraphState)solved).colors;
        System.out.println("----Solution by simple HillClimbing-----");
        System.out.println("Visited Nodes: " + HillClimbing.visitedNodes);
        System.out.println("Expanded Nodes " + HillClimbing.expandedNodes);
        System.out.println("Evaluation : " + colorGraphProblem.getEvaluation(solved));
        printColors(colors);

        //reset the counters !
        HillClimbing.expandedNodes=0;
        HillClimbing.visitedNodes=0;

        solved = HillClimbing.randomRestartSolve(colorGraphProblem.initialState,colorGraphProblem,1000,null,-1);
        colors = ((ColorGraphState)solved).colors;
        System.out.println("----Solution by RandomRestart HillClimbing-----");
        System.out.println("Visited Nodes: " + HillClimbing.visitedNodes);
        System.out.println("Expanded Nodes " + HillClimbing.expandedNodes);
        System.out.println("Evaluation : " + colorGraphProblem.getEvaluation(solved));
        printColors(colors);

        //reset the counters !
        HillClimbing.expandedNodes=0;
        HillClimbing.visitedNodes=0;

        solved = HillClimbing.stochasticSolve(colorGraphProblem.initialState,colorGraphProblem);
        colors = ((ColorGraphState)solved).colors;
        System.out.println("----Solution by Stochastic HillClimbing-----");
        System.out.println("Visited Nodes: " + HillClimbing.visitedNodes);
        System.out.println("Expanded Nodes " + HillClimbing.expandedNodes);
        System.out.println("Evaluation : " + colorGraphProblem.getEvaluation(solved));
        printColors(colors);


        HillClimbing.expandedNodes=0;
        HillClimbing.visitedNodes=0;

        solved = HillClimbing.firstChoiceSolve(colorGraphProblem.initialState,colorGraphProblem);
        colors = ((ColorGraphState)solved).colors;
        System.out.println("----Solution by FirstChoice HillClimbing-----");
        System.out.println("Visited Nodes: " + HillClimbing.visitedNodes);
        System.out.println("Expanded Nodes " + HillClimbing.expandedNodes);
        System.out.println("Evaluation : " + colorGraphProblem.getEvaluation(solved));
        printColors(colors);
    }
    private static void printColors(List<Integer> colors) {
        String leftAlignFormat = "| %-15s | %-4d |%n";
        System.out.format("+-----------------+------+%n");
        System.out.format("|      Node       | color|%n");
        System.out.format("+-----------------+------+%n");
        for (int i = 0; i < colors.size(); i++) {
            System.out.format(leftAlignFormat, "# " + (i+1), colors.get(i)+1);
        }
        System.out.format("+-----------------+------+%n");
    }
}