package emma.battleship.controller;

import emma.battleship.service.BoardWrapper;
import emma.battleship.model.Move;
import emma.battleship.model.Ship;
import emma.battleship.service.GameWrapper;
import emma.battleship.model.Game;
import emma.battleship.service.MoveWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class GameController {
    private Game gamePresent;

    @Autowired
    public GameController() {
        this.gamePresent = new Game();
    }

    @RequestMapping("/joinGame")
    public GameWrapper joinGame(){
        GameWrapper gameWrapper = new GameWrapper();

        if (!gamePresent.getPlayer1()) {
            gameWrapper.setPlayer(1);
            gamePresent.setPlayer1(true);
            gameWrapper.setSuccess(true);
            return gameWrapper;
        }

        if (!gamePresent.getPlayer2()){
            gameWrapper.setPlayer(2);
            gamePresent.setPlayer2(true);
            gameWrapper.setSuccess(true);
            return gameWrapper;
        }

        gameWrapper.setSuccess(false);
        gameWrapper.setErrorMessage("Game is full");
        return gameWrapper;
    }

    @PostMapping(value = "/setShip/{player}", consumes = MediaType.ALL_VALUE)
    public BoardWrapper setShip(@RequestBody Ship ship,
                               @PathVariable("player") Integer player) {
        Integer[] start = new Integer[] { ship.getStart1(), ship.getStart2() };
        Integer[] end = new Integer[] { ship.getEnd1(), ship.getEnd2() };

        BoardWrapper boardWrapper = new BoardWrapper();
        String[][] tempGame = player == 1 ? gamePresent.getBoard1Player() : gamePresent.getBoard2Player();

        if (!ship.checkValid(tempGame)){
            boardWrapper.setSuccess(false);
            boardWrapper.setErrorMessage("Invalid ship placement.");
            return boardWrapper;
        }

        String[][] playerGame = new String[10][10];
        int countRow = 0;
        for (String[] row : tempGame){
            int countCell = 0;
            for (String cell : row){
                playerGame[countRow][countCell] = cell;
                countCell++;
            }
            countRow++;
        }

        Boolean valid = true;
        Integer shipLength = ship.getShipLength();

        if (start[0] == end[0]) { // same row
            int lesser = start[1] < end[1] ? start[1] : end[1];
            int greater = start[1] < end[1] ? end[1] : start[1];
            for (int i = lesser; i <= greater; i++) {
                if (!playerGame[start[0]][i].equals(".")) valid = false;
                else playerGame[start[0]][i] = shipLength.toString();
            }
        } else if (start[1] == end[1]) { // same column
            int lesser = start[0] < end[0] ? start[0] : end[0];
            int greater = start[0] < end[0] ? end[0] : start[0];
            for (int i = lesser; i <= greater; i++) {
                if (!playerGame[i][start[1]].equals(".")) valid = false;
                else playerGame[i][start[1]] = shipLength.toString();
            }
        }

        if (!valid) {
            boardWrapper.setErrorMessage("Overlaps previous ship.");
            boardWrapper.setSuccess(false);
            return boardWrapper;
        }

        if (player == 1) {
            gamePresent.setBoard1Player(playerGame);
        } else {
            gamePresent.setBoard2Player(playerGame);
        }

        boardWrapper.setSuccess(true);
        gamePresent.checkStartValid();
        boardWrapper.setBoard(playerGame);
        return boardWrapper;
    }

    @PostMapping(value = "/move/{player}", consumes = MediaType.ALL_VALUE)
    public MoveWrapper move(@PathVariable("player") Integer player,
                            @RequestBody Move move,
                            HttpServletRequest request) {
        MoveWrapper moveWrapper = new MoveWrapper();
        if (gamePresent.getGameOver()) {
            moveWrapper.setErrorMessage("Game is over");
            moveWrapper.setSuccess(false);
            return moveWrapper;
        }

        if (gamePresent.getWhoseTurn() != player) {
            moveWrapper.setErrorMessage("It's not your turn");
            moveWrapper.setSuccess(false);
            return moveWrapper;
        }

        String[][] playerAttack = player == 1 ? gamePresent.getBoard1Attack() : gamePresent.getBoard2Attack();
        String[][] opponentBoard = player == 1 ? gamePresent.getBoard2Player() : gamePresent.getBoard1Player();

        if (!move.checkValid(playerAttack)) {
            moveWrapper.setSuccess(false);
            moveWrapper.setErrorMessage("Invalid move.");
            return moveWrapper;
        }

        Integer row = move.getRow();
        Integer column = move.getColumn();

        if (opponentBoard[row][column].equals(".")) {
            opponentBoard[row][column] = "o";
            playerAttack[row][column] = "o";
            moveWrapper.setCorrectHit(false);
        } else {
            opponentBoard[row][column] = "x";
            playerAttack[row][column] = "x";
            moveWrapper.setCorrectHit(true);
        }

        gamePresent.setWhoseTurn(player == 1 ? 2 : 1);
        moveWrapper.setGameOver(gamePresent.checkEndGame());
        moveWrapper.setSuccess(true);

        return moveWrapper;
    }

    @RequestMapping("/leaveGame")
    public GameWrapper leaveGame() {
        GameWrapper gameWrapper = new GameWrapper();
        gamePresent = new Game();
        gameWrapper.setSuccess(true);
        return gameWrapper;
    }

    
    @RequestMapping("/getChanges/{player}")
    public BoardWrapper getChanges(@PathVariable("player") Integer player) {
        BoardWrapper boardWrapper = new BoardWrapper();

        String[][] board = player == 1 ? gamePresent.getBoard2Attack() : gamePresent.getBoard1Attack();
        boardWrapper.setBoard(board);
        boardWrapper.setSuccess(true);
        boardWrapper.setGameOver(gamePresent.getGameOver());
        boardWrapper.setWhoseTurn(gamePresent.getWhoseTurn());
        return boardWrapper;
    }
}
