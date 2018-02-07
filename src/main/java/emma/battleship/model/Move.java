package emma.battleship.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Move {
    @JsonProperty("row")
    private Integer row;
    @JsonProperty("column")
    private Integer column;

    public Boolean checkValid(String[][] board) {
        if (row > 9 || column > 9) {
            return false;
        }

        if (! board[row][column].equals(".")){
            return false;
        }

        return true;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }
}
