/**
 * Created by yifan on 6/7/2016.
 */
public class Node {
    public int x;
    public int y;
    public int headIndex;

    public Node() {
        this.headIndex = -1;
    }

    @Override
    public String toString() {
        return "Node [x=" + x + ", y=" + y + "]";
    }

}
