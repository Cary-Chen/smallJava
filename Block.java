import java.util.ArrayList;

/**
 * Created by yifan on 6/8/2016.
 */
public class Block {
    Node mHead;
    ArrayList<Node> mMember;
    int left, right, top, bottom;

    public Block() {
        mMember = new ArrayList<>();
        left = -1;
        right = -1;
    }
    public void fixBound(Node node) {
        if(node.y < left) {
            left = node.y;
        } else if(node.y > right) {
            right = node.y;
        }
        if (node.x < top) {
            top = node.x;
        } else if(node.x > bottom) {
            bottom = node.x;
        }
    }
}
