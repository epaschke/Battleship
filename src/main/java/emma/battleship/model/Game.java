package emma.battleship.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Board board1Attack;
    @ManyToOne
    private Board board1Player;
    @ManyToOne
    private Board board2Attack;;
    @ManyToOne
    private Board board2Player;
    private Boolean gameOver;
    private Boolean player1;
    private Boolean player2;
    private Integer whoseTurn;
    private Integer winner;

    public Game() {
        this.gameOver = false;
        this.player1 = false;
        this.player2 = false;
        this.whoseTurn = 0;
    }

    public Boolean checkStartValid() {
        Map<String, Integer> hash1 = new HashMap<>();
        Map<String, Integer> hash2 = new HashMap<>();
        Boolean valid = true;

        if (whoseTurn == 0) {
            for (int i = 0; i < 10; i++) {
                String[] row = board1Player.get(i);
                for (String cell : row) {
                    if (!cell.equals(".")) {
                        if (!hash1.containsKey(cell)) hash1.put(cell, 0);
                        hash1.put(cell, hash1.get(cell) + 1);
                    }
                }
            }

            for (int i = 0; i < 10; i++) {
                String[] row = board2Player.get(i);
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
        for (int i = 0; i < 10; i++) {
            String[] row = board1Player.get(i);
            for (String cell : row) {
                if (!cell.equals(".") && !cell.equals("o") && !cell.equals("x")) over1 = false;
            }
        }

        for (int i = 0; i < 10; i++) {
            String[] row = board2Player.get(i);
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Board getBoard1Attack() {
        return board1Attack;
    }

    public void setBoard1Attack(Board board1Attack) {
        this.board1Attack = board1Attack;
    }

    public Board getBoard2Attack() {
        return board2Attack;
    }

    public void setBoard2Attack(Board board2Attack) {
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

    public Board getBoard1Player() {
        return board1Player;
    }

    public void setBoard1Player(Board board1Player) {
        this.board1Player = board1Player;
    }

    public Board getBoard2Player() {
        return board2Player;
    }

    public void setBoard2Player(Board board2Player) {
        this.board2Player = board2Player;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }
}
