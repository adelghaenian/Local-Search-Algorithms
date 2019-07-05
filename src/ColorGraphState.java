import java.util.ArrayList;
import java.util.List;

public class ColorGraphState extends State {
    List<Integer> colors ;

    public ColorGraphState(List<Integer> colors) {
        this.colors = new ArrayList<>(colors);
    }
    public void setColor(int pos,int color) {
        colors.add(pos,color);
        colors.remove(pos+1);
    }
}
