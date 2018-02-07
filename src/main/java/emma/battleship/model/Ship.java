package emma.battleship.model;

public class Ship {

    private Integer start1;
    private Integer start2;
    private Integer end1;
    private Integer end2;
    private Integer shipLength;

    public Boolean checkValid(String[][] board) {
        if (start1 > 9 || start2 > 9 || end1 > 9 || end2 > 9){
            return false;
        }

        for (String[] row : board) {
            for (String cell : row) {
                if (cell.equals(shipLength.toString())) {
                    return false;
                }
            }
        }

        if (start1 != end1 && start2 != end2) {
            return false;
        }
        if ((start1 - end1 + start2 - end2) != shipLength - 1 &&
                (end1 - start1 + end2 - start2) != shipLength - 1){
            return false;
        }
        return true;
    }


    public Integer getShipLength() {
        return shipLength;
    }

    public void setShipLength(Integer shipLength) {
        this.shipLength = shipLength;
    }

    public Integer getStart1() {
        return start1;
    }

    public void setStart1(Integer start1) {
        this.start1 = start1;
    }

    public Integer getStart2() {
        return start2;
    }

    public void setStart2(Integer start2) {
        this.start2 = start2;
    }

    public Integer getEnd1() {
        return end1;
    }

    public void setEnd1(Integer end1) {
        this.end1 = end1;
    }

    public Integer getEnd2() {
        return end2;
    }

    public void setEnd2(Integer end2) {
        this.end2 = end2;
    }
}
