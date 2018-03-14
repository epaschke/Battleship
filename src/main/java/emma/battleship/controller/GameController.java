package emma.battleship.controller;

import emma.battleship.dao.BoardDao;
import emma.battleship.dao.GameDao;
import emma.battleship.model.Board;
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
import java.util.List;


@RestController
public class GameController {
    private final GameDao gameDao;
    private final BoardDao boardDao;

    @Autowired
    public GameController(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    //player requests to join game
    @RequestMapping("/joinGame")
    public GameWrapper joinGame(){
        GameWrapper gameWrapper = new GameWrapper();
        List<Game> games = gameDao.findAll();
        switch(games.size()){
            //if there is a game in the database
            case 1:
                Game gamePresent = games.get(0);
                if (gamePresent.getPlayer1() && gamePresent.getPlayer2()){
                    gameWrapper.setSuccess(false);
                    gameWrapper.setErrorMessage("Game full.");
                } else if (!gamePresent.getPlayer2()){
                    gamePresent.setPlayer2(true);
                    gameDao.save(gamePresent);

                    gameWrapper.setPlayer(2);
                    gameWrapper.setSuccess(true);
                }
                break;
            //if there are no games in the database
            case 0:
                Game newGame = new Game();
                newGame.setPlayer1(true);
                newGame.setBoard1Player(boardDao.save(new Board()));
                newGame.setBoard2Player(boardDao.save(new Board()));
                newGame.setBoard1Attack(boardDao.save(new Board()));
                newGame.setBoard2Attack(boardDao.save(new Board()));
                gameDao.save(newGame);

                gameWrapper.setPlayer(1);
                gameWrapper.setSuccess(true);
                break;
            //if there is an error and there is more than 1 game in the database
            default:
                for (Game g : games){
                    gameDao.delete(g.getId());
                }
                gameWrapper.setSuccess(false);
                gameWrapper.setErrorMessage("Game has been cleared. Try again.");
        }
        return gameWrapper;
    }

    //player requests to set ship with player number in params & class Ship in body
    @PostMapping(value = "/setShip/{player}", consumes = MediaType.ALL_VALUE)
    public BoardWrapper setShip(@RequestBody Ship ship,
                               @PathVariable("player") Integer player) {
        //convert Ship class to integer array
        Integer[] start = new Integer[] { ship.getStart1(), ship.getStart2() };
        Integer[] end = new Integer[] { ship.getEnd1(), ship.getEnd2() };
        BoardWrapper boardWrapper = new BoardWrapper();
        try {
            Game gamePresent = gameDao.findById(1);
            //assign player's board based on player number sent
            Board playerGame = player == 1 ? gamePresent.getBoard1Player() : gamePresent.getBoard2Player();

            //if ship coordinates are outside of the board or otherwise invalid
            if (!ship.checkValid(playerGame)) {
                boardWrapper.setSuccess(false);
                boardWrapper.setErrorMessage("Invalid ship placement.");
                return boardWrapper;
            }

            Boolean valid = true;
            Integer shipLength = ship.getShipLength();

            //check content of all cells between start and end
            if (start[0] == end[0]) { // same row
                int lesser = start[1] < end[1] ? start[1] : end[1];
                int greater = start[1] < end[1] ? end[1] : start[1];
                for (int i = lesser; i <= greater; i++) {
                    String[] gotten = playerGame.get(start[0]);
                    //if the selected cell is not empty
                    if (!gotten[i].equals(".")) valid = false;
                    else {
                        gotten[i] = shipLength.toString();
                        playerGame.set(start[0], gotten);
                    }
                }
            } else if (start[1] == end[1]) { // same column
                int lesser = start[0] < end[0] ? start[0] : end[0];
                int greater = start[0] < end[0] ? end[0] : start[0];
                for (int i = lesser; i <= greater; i++) {
                    String[] gotten = playerGame.get(i);
                    if (! gotten[start[1]].equals(".")) valid = false;
                    else {
                        gotten[start[1]] = shipLength.toString();
                        playerGame.set(i, gotten);
                    }
                }
            }

            //if invalid, do not save game and return success is false
            if (!valid) {
                boardWrapper.setErrorMessage("Overlaps previous ship.");

                boardWrapper.setSuccess(false);
                return boardWrapper;
            }

            //if valid, save game and return success
            boardDao.save(playerGame);

            boardWrapper.setSuccess(true);
            gamePresent.checkStartValid();
            boardWrapper.setBoard(playerGame);

            gameDao.save(gamePresent);
        }
        catch(Exception e){
            boardWrapper.setErrorMessage(e.getMessage());
            boardWrapper.setSuccess(false);
        }
        return boardWrapper;
    }

    //player requests a move with player number in params and class Move in body
    @PostMapping(value = "/move/{player}", consumes = MediaType.ALL_VALUE)
    public MoveWrapper move(@PathVariable("player") Integer player,
                            @RequestBody Move move) {
        MoveWrapper moveWrapper = new MoveWrapper();
        Game gamePresent = gameDao.findById(1);
        try {
            //if game is over
            if (gamePresent.getGameOver()) {
                moveWrapper.setErrorMessage("Game is over.");
                moveWrapper.setSuccess(false);
                return moveWrapper;
            }
            //if game hasn't started
            if (gamePresent.getWhoseTurn() == 0) {
                moveWrapper.setErrorMessage("Game hasn't started.");
                moveWrapper.setSuccess(false);
                return moveWrapper;
            }
            //if it's not the player's turn
            if (gamePresent.getWhoseTurn() != player) {
                moveWrapper.setErrorMessage("It's not your turn.");
                moveWrapper.setSuccess(false);
                return moveWrapper;
            }

            Board playerAttack = player == 1 ? gamePresent.getBoard1Attack() : gamePresent.getBoard2Attack();
            Board opponentBoard = player == 1 ? gamePresent.getBoard2Player() : gamePresent.getBoard1Player();

            //if move is off board or in a place that's already been guessed
            if (!move.checkValid(playerAttack)) {
                moveWrapper.setSuccess(false);
                moveWrapper.setErrorMessage("Invalid move.");
                return moveWrapper;
            }

            Integer row = move.getRow();
            Integer column = move.getColumn();

            String[] opponentRow = opponentBoard.get(row);
            String[] playerRow = playerAttack.get(row);

            //check if move is valid or invalid
            if (opponentRow[column].equals(".")) {
                opponentRow[column] = "o";
                opponentBoard.set(row, opponentRow);
                playerRow[column] = "o";
                playerAttack.set(row, playerRow);
                moveWrapper.setCorrectHit(false);
            } else {
                opponentRow[column] = "x";
                opponentBoard.set(row, opponentRow);
                playerRow[column] = "x";
                playerAttack.set(row, playerRow);
                moveWrapper.setCorrectHit(true);
            }

            boardDao.save(playerAttack);
            boardDao.save(opponentBoard);

            //set game state to other player's turn
            gamePresent.setWhoseTurn(player == 1 ? 2 : 1);
            //check if game is over
            moveWrapper.setGameOver(gamePresent.checkEndGame());
            gameDao.save(gamePresent);
            moveWrapper.setSuccess(true);
        }
        catch(Exception e){
            moveWrapper.setErrorMessage(e.getMessage());
            moveWrapper.setSuccess(false);
        }

        return moveWrapper;
    }

    //player requests to leave game
    @RequestMapping("/leaveGame")
    public GameWrapper leaveGame() {
        GameWrapper gameWrapper = new GameWrapper();
        try {
            //remove the game from the database
            gameDao.delete(1);
            gameWrapper.setSuccess(true);
        }
        catch(Exception e){
            gameWrapper.setErrorMessage(e.getMessage());
        }
        return gameWrapper;
    }

    //returns player's board, takes player number as param
    @RequestMapping("/getChanges/{player}")
    public BoardWrapper getChanges(@PathVariable("player") Integer player) {
        BoardWrapper boardWrapper = new BoardWrapper();
        try {
            Game gamePresent = gameDao.findById(1);
            Board board = player == 1 ? gamePresent.getBoard2Attack() : gamePresent.getBoard1Attack();
            boardWrapper.setBoard(board);
            boardWrapper.setSuccess(true);
            boardWrapper.setGameOver(gamePresent.getGameOver());
            boardWrapper.setWhoseTurn(gamePresent.getWhoseTurn());
            // if game is over, set winner
            if (gamePresent.getGameOver()) {
                boardWrapper.setWinner(gamePresent.getWinner());
            }
            //save game
            gameDao.save(gamePresent);
        }
        catch(Exception e){
            boardWrapper.setSuccess(false);
            boardWrapper.setErrorMessage(e.getMessage());
        }
        return boardWrapper;
    }
}
