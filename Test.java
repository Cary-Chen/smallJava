import java.util.ArrayList;

public class Test {

    private static Node[] mNodes;

    public static void main(String[] argvs) {
        mNodes = new Node[3];
        addNode();
        System.out.print("node x " + mNodes[1].x + " y:" + mNodes[1].y);
    }

    private static void addNode() {
        Node node = new Node();
        node.x = 1;
        node.y = 2;
        mNodes[1] = node;
    }
}
