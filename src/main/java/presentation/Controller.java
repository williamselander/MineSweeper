package presentation;

import model.Board;
import model.Square;


public class Controller {
    private final BoardView boardView;
    private final Board board;

    public Controller(Board board){
        this.board = board;
        this.boardView = new BoardView(this::handleSquareClick, this::handleFlagClick, this::handleRestartGame);
    }

    private void startGame(int row, int col){
        board.createBoard(row, col);
    }

    // Den första man klickar på får ej vara en mina.
    // Rutan får ej öppnas om den är flaggad
    // Börja sätta ut spelplanen här kanske???
    public void handleSquareClick(int row, int col){
        if (board.getOpenedSquares() == 0) {
            startGame(row, col);
        }
        board.reveal(row, col);
        board.hasPlayerWon();
        boardView.updateBoard(board.getBoard(), board.isActive(), board.getTotalMinesForCounter(), board.isHasWon(), board.isHasLost());
        }


    public void handleFlagClick(int row, int col){
        board.flagSquare(row, col);
        boardView.updateBoard(board.getBoard(), board.isActive(), board.getTotalMinesForCounter(), board.isHasWon(), board.isHasLost());
    }

    public void handleRestartGame(){
        board.clearOpenedSquares();
        board.setHasWonAsFalse();
        boardView.createGrid();
    }

    public BoardView getBoardView(){return boardView;}





}
