package emma.battleship.service;

public class MoveWrapper {
    private Boolean success;
    private String errorMessage;
    private Boolean correctHit;
    private Boolean gameOver;

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

    public Boolean getCorrectHit() {
        return correctHit;
    }

    public void setCorrectHit(Boolean correctHit) {
        this.correctHit = correctHit;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }
}
