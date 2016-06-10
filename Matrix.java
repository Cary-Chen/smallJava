/**
 * Created by Cary on 2016/6/10.
 */
public class Matrix {
    private int row;
    private int column;

    public int getRow() {
        return row;
    }
    public Matrix(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }
}
