package emma.battleship.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Game {

    private String[][] board1Attack;
    private String[][] board1Player;
    private String[][] board2Attack;
    private String[][] board2Player;
    private Boolean gameOver;
    private Boolean player1;
    private Boolean player2;
    private Integer whoseTurn;
    private Integer winner;

    public Game() {
        this.gameOver = false;
        this.board1Attack = new String[10][10];
        for (String[] row : board1Attack) Arrays.fill(row, ".");
        this.board2Attack = new String[10][10];
        for (String[] row : board2Attack) Arrays.fill(row, ".");
        this.board1Player = new String[10][10];
        for (String[] row : board1Player) Arrays.fill(row, ".");
        this.board2Player = new String[10][10];
        for (String[] row : board2Player) Arrays.fill(row, ".");
        this.player1 = false;
        this.player2 = false;
        this.whoseTurn = 0;
    }

    public Boolean checkStartValid() {
        Map<String, Integer> hash1 = new HashMap<>();
        Map<String, Integer> hash2 = new HashMap<>();
        Boolean valid = true;

        if (whoseTurn == 0) {
            for (String[] row : board1Player) {
                for (String cell : row) {
                    if (!cell.equals(".")) {
                        if (!hash1.containsKey(cell)) hash1.put(cell, 0);
                        hash1.put(cell, hash1.get(cell) + 1);
                    }
                }
            }

            for (String[] row : board2Player) {
                for (String cell : row) {
                    if (!cell.equals(".")) {
                        if (!hash2.containsKey(cell)) hash2.put(cell, 0);
                        hash2.put(cell, hash2.get(cell) + 1);
                    }
                }
            }

            for (int i = 1; i <= 5; i++){
                if (!hash1.containsKey(Integer.toString(i))) valid = false;
                else if (hash1.get(Integer.toString(i)) != i) valid = false;

                if (!hash2.containsKey(Integer.toString(i))) valid = false;
                else if (hash2.get(Integer.toString(i)) != i) valid = false;
            }
            if (valid) this.whoseTurn = 1;
        }

        return valid;
    }

    public Boolean checkEndGame() {
        boolean over1 = true;
        boolean over2 = true;
        for (String[] row : board1Player) {
            for (String cell : row) {
                if (!cell.equals(".") && !cell.equals("o") && !cell.equals("x")) over1 = false;
            }
        }

        for (String[] row : board2Player) {
            for (String cell : row) {
                if (!cell.equals(".") && !cell.equals("o") && !cell.equals("x")) over2 = false;

            }
        }

        if (over1 || over2) {
            gameOver = true;
            winner = over1 ? 2 : 1;
        }
        return gameOver;
    }

    public String[][] getBoard1Attack() {
        return board1Attack;
    }

    public void setBoard1Attack(String[][] board1Attack) {
        this.board1Attack = board1Attack;
    }

    public String[][] getBoard2Attack() {
        return board2Attack;
    }

    public void setBoard2Attack(String[][] board2Attack) {
        this.board2Attack = board2Attack;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Boolean getPlayer1() {
        return player1;
    }

    public void setPlayer1(Boolean player1) {
        this.player1 = player1;
    }

    public Boolean getPlayer2() {
        return player2;
    }

    public void setPlayer2(Boolean player2) {
        this.player2 = player2;
    }

    public Integer getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(Integer whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public String[][] getBoard1Player() {
        return board1Player;
    }

    public void setBoard1Player(String[][] board1Player) {
        this.board1Player = board1Player;
    }

    public String[][] getBoard2Player() {
        return board2Player;
    }

    public void setBoard2Player(String[][] board2Player) {
        this.board2Player = board2Player;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }
}
