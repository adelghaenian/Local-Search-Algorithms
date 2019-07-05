public class LetterTableState extends State {
    char[][] table;
    LetterTableState(int n,int m,char [][] t){
        this.table = new char[n][m];
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                table[i][j] = t[i][j];
            }
        }
    }
    LetterTableState(int n,int m,String letters){
        this.table = new char[n][m];
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                table[i][j] = letters.charAt(i*m + j);
            }
        }
    }

    public String toString(int n,int m) {
        String s ="";
        for (int i=0;i<n;i++)
            for (int j=0;j<m;j++)
                s+=table[i][j];
        return s;
    }
}
