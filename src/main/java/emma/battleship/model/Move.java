package emma.battleship.model;

public class Move {
    private Integer row;
    private Integer column;

    //checks if move is within board and is an open square
    public Boolean checkValid(Board board) {
        return row > 9 || column > 9 || !board.get(row)[column].equals(".");
    }

    //getters and setters
    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }
}
