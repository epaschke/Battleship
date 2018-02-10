package emma.battleship.service;

import emma.battleship.model.Board;

public class BoardWrapper {
    private Board board;
    private Boolean success;
    private Integer whoseTurn;
    private String errorMessage;
    private Boolean gameOver;
    private Integer winner;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Integer getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(Integer whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }
}
