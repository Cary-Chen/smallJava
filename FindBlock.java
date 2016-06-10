import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by yifan on 6/7/2016.
 * Modified by cary on 6/10/2016
 */
public class FindBlock {

    private static int MAX_ROW;
    private static int MAX_COLUMN;
    private static int MAX_BLOCK;
    private static final String FILE_PATH = "C:\\Users\\Cary\\Desktop\\weizhi.txt";

    private static ArrayList<Node> mNodeList;
    private static int[][] mIsNodeArray;
    private static int[][] mHasMarked;
    private static Block[] mBlockArray;
    private static int sumNeededNode;
    //真正的block数量
    private static int mRealBlockNum = 0;

    public static void main(String argvs[]) throws IOException{
        /**
         * for (int i = 0; i < MAX_ROW; i++) { for (int j = 0; j < MAX_COLUMN;
         * j++) { Node node = new Node(); node.x = i; node.y = j;
         * mNodeList.add(node); Random random = new
         * Random(System.currentTimeMillis()); try { Thread.sleep(1); } catch
         * (InterruptedException e) { e.printStackTrace(); } int ran =
         * random.nextInt(3); if (ran <= 1) { mIsNodeArray[i][j] = 0; } else {
         * mIsNodeArray[i][j] = 1; } } }
         */
        BufferedReader bf = new BufferedReader(new FileReader(FILE_PATH));
        //将文件中每个矩阵添加到matirx中，添加的时候要声明每个矩阵的行和列。
        ArrayList<Matrix> matrix = new ArrayList<Matrix>();
        matrix.add(new Matrix(72,88));
        matrix.add(new Matrix(72,88));

        String s;
        String[] strs;

        Node mNode;
        String[] tempArray;
        //读取文件中多个矩阵
        for (int k = 0; k < matrix.size(); k++) {
            int temp = 0;
            int i = 0;
            MAX_ROW = matrix.get(k).getRow();
            MAX_COLUMN = matrix.get(k).getColumn();
            MAX_BLOCK = MAX_COLUMN * MAX_ROW;
            mIsNodeArray = new int[MAX_ROW][MAX_COLUMN];
            mHasMarked = new int[MAX_ROW][MAX_COLUMN];
            mNodeList = new ArrayList<>();
            mBlockArray = new Block[MAX_BLOCK];
            while((temp<matrix.get(k).getRow())&&(s=bf.readLine())!=null){
                temp++;
                //System.out.println(s);

                strs = s.split(" ");
                for (int j = 0; j < strs.length; j++)
                    if ("x+y".equals(strs[j])) {
                        mNode = new Node();
                        mNode.x = -1;
                        mNode.y = -1;
                        mNodeList.add(mNode);
                        mIsNodeArray[i][j] = 0;
                    } else {
                        mNode = new Node();
                        tempArray = strs[j].split("\\+");
                        mNode.x = (Integer.parseInt(tempArray[0])) - i * 4 + i;
                        mNode.y = (Integer.parseInt(tempArray[1])) - j * 4 + j;
                        mNodeList.add(mNode);
                        mIsNodeArray[i][j] = 1;
                        sumNeededNode++;
                    }
                i++;
            }

            processNode();
            System.out.println("");
            System.out.println("");
            splitBlock();
            printHead();
            System.out.println("------------------分割线---------------------");
        }


    }
    private static void processNode() {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COLUMN; j++) {
                if (mIsNodeArray[i][j] == 1) {
                    Node node = findNodeByXY(i, j);
                    if (node.headIndex == -1) {
                        node.headIndex = createBlock();
                        mBlockArray[node.headIndex].mHead = node;
                        mBlockArray[node.headIndex].mMember.add(node);
                        mBlockArray[node.headIndex].left = node.y;
                        mBlockArray[node.headIndex].right = node.y;
                        mBlockArray[node.headIndex].top = node.x;
                        mBlockArray[node.headIndex].bottom = node.x;
                        mHasMarked[i][j] = 1;
                        markNode(i - 1, j, node.headIndex);
                        markNode(i, j + 1, node.headIndex);
                        markNode(i + 1, j, node.headIndex);
                        markNode(i, j - 1, node.headIndex);
                    }
                }
            }
        }
    }

    private static void markNode(int x, int y, int headIndex) {
        if (x == MAX_ROW || y == MAX_COLUMN || x == -1 || y == -1) {
            return;
        }
        Node node = findNodeByXY(x, y);
        if (mHasMarked[x][y] == 1) {
            return;
        }
        mHasMarked[x][y] = 1;
        if (mIsNodeArray[x][y] == 1) {
            node.headIndex = headIndex;
            mBlockArray[headIndex].mMember.add(node);
            mBlockArray[headIndex].fixBound(node);
            markNode(x, y - 1, headIndex);
            markNode(x - 1, y, headIndex);
            markNode(x, y + 1, headIndex);
            markNode(x + 1, y, headIndex);
        }
    }

    private static Node findNodeByXY(int x, int y) {
        for (Node node : mNodeList) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }
        return null;
    }

    private static void printNodes() {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COLUMN; j++) {
                if (mIsNodeArray[i][j] == 1) {
                    System.out.print(i + "+" + j + " ");
                } else {
                    System.out.print("x+y ");
                }
            }
            System.out.println("");

        }
    }

    private static int createBlock() {
        for (int i = 0; i < MAX_BLOCK; i++) {
            if (mBlockArray[i] == null) {
                mBlockArray[i] = new Block();
                return i;
            }
        }
        return -1;
    }

    private static void addBlock(Block block) {
        for (int i = 0; i < MAX_BLOCK; i++) {
            if (mBlockArray[i] == null) {
//                System.out.println("addBlock i" + i);
                mBlockArray[i] = block;
//                for (Node node : mBlockArray[i].mMember) {
//                    System.out.print("(" + node.x + "," + node.y + ") ");
//                }
                break;
            }
        }
    }

    private static void splitBlock() {
//        System.out.println("splitBlock");
        int blockIndex = 0;
        int originalSize = 0;
        for (int i = 0; i < MAX_BLOCK; i++) {
            if (mBlockArray[i] == null) {
                continue;
            }
            originalSize++;
        }
        mRealBlockNum = originalSize;
        for (int z = 0; z < originalSize; z++) {
            Block block = mBlockArray[z];
//            System.out.println("index:" + blockIndex);
            //遍历原始block中的nodes
//            for (Node node : block.mMember) {
//                System.out.print("(" + node.x + "," + node.y + ") ");
//            }
            //打印出block的边界
//            System.out.println(" ");
//            System.out.println("block : " + "left:" + block.left + "right:"
//                    + block.right + "top:" + block.top + "bottom"
//                    + block.bottom);
            int blockNum = 0;
            for (int i = block.left; i <= block.right; i += 4) {
                for (int j = block.top; j <= block.bottom; j += 4) {
//                    System.out.println("splitBlock i: " + i + " j:" + j);
                    if (blockNum == 0) {
                        blockNum++;
                        continue;
                    }
                    Block newBlock = new Block();
                    for (Node node : block.mMember) {
                        if (node.y >= i && node.y < i + 4 && node.x >= j
                                && node.x < j + 4) {
//                            System.out.println(" yifan " + " i:" + i + "j:" + j
//                                    + " x" + node.x + " y" + node.y);
                            newBlock.mMember.add(node);
                        }
                    }
                    if (!newBlock.mMember.isEmpty()) {
//                        for (Node node : newBlock.mMember) {
//                            System.out.print("new Block: " + node.x + " " + node.y);
//                        }
                        addBlock(newBlock);
                        mRealBlockNum++;
                    }
                    blockNum++;
                }
            }
            for (Iterator<Node> it = block.mMember.iterator(); it.hasNext();) {
                Node node = it.next();
                if (node.y >= block.left && node.y < block.left + 4
                        && node.x >= block.top && node.x < block.top + 4) {
                    continue;
                }
                it.remove();
            }
            if(mBlockArray[blockIndex].mMember.isEmpty()) {
                mBlockArray[blockIndex] = null;
                mRealBlockNum--;
            }
            blockIndex++;
        }
    }

    private static void printHead() throws IOException {
        int sumNode = 0;
        FileOutputStream pw = new FileOutputStream(new File("D:/block.txt"),true);
        int tempBlockNum = mRealBlockNum;
//        System.out.println("RealBlockNum:" + mRealBlockNum);
        for (int i = 0; i < mBlockArray.length; i++) {
            if(tempBlockNum == 0) {
                return;
            }
            if (mBlockArray[i] == null) {
                continue;
            }
//            System.out.print("Block" + (mRealBlockNum - tempBlockNum + 1) + ": ");
//            pw.write(("Block" + (mRealBlockNum - tempBlockNum + 1) + ": ").getBytes());
            for (Node node : mBlockArray[i].mMember) {
                System.out.print("(" + node.x *4+ "," + node.y*4 + ") ");
                pw.write((node.x*4 +"+"+node.y*4+" ").getBytes());
                sumNode++;
            }
            pw.write("\r\n".getBytes());
            System.out.println("");
            tempBlockNum--;
//            System.out.println("sumNode:" + sumNode);
        }
        pw.close();

    }

}
