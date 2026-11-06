package presentation;

import model.Board;
import model.Square;


public class Controller {
    private final BoardView boardView;
    private Board board;

    public Controller(){

        this.boardView = new BoardView(this::handleSquareClick, this::handleFlagClick, this::handleRestartGame);
    }

    private void startGame(int row, int col, int rows, int cols){
        this.board = new Board(new Square[rows][cols]);
        board.createBoard(row, col);
    }

    // Den första man klickar på får ej vara en mina.
    // Rutan får ej öppnas om den är flaggad
    // Börja sätta ut spelplanen här kanske???
    public void handleSquareClick(int row, int col){
        if (board == null) {
            startGame(
                    row,col, boardView.getRowsBoard(), boardView.getColsBoard()
            );
        }

        if (board.getOpenedSquares() == 0) {
            startGame(row, col, boardView.getRowsBoard(), boardView.getColsBoard());
        }
        board.reveal(row, col);
        board.hasPlayerWon();
        boardView.updateBoard(board.getBoard(), board.isActive(), board.getTotalMinesForCounter(), board.isHasWon(), board.isHasLost());
        }


    public void handleFlagClick(int row, int col){
        if (board == null) {
            return;
        }
        board.flagSquare(row, col);
        boardView.updateBoard(board.getBoard(), board.isActive(), board.getTotalMinesForCounter(), board.isHasWon(), board.isHasLost());
    }

    public void handleRestartGame(){
        board = null;
        boardView.createGrid();
    }

    public BoardView getBoardView(){return boardView;}





}
