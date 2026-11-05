package service;

import model.Board;
import model.Square;

public class BoardService {
    private final Board board;

    public BoardService(Board board) {
        this.board = board;
    }

    // Kan bli att mana använder olika storlekar på arr framåt
    public void createGame(int row, int col){
        board.createBoard(row, col);
    }

    public Square getSquare(int row, int col) {
        return board.getSquare(row, col);
    }

    public void openSquare(int row, int col) {
        board.openSquare(row, col);
    }

    // Flippar flaggan
    public void flagSquare(int row, int col){
        board.flagSquare(row, col);
    }

    public boolean hasWon(){
        return board.isHasWon();
    }

    public boolean isSquareFlagged(int row, int col){
        return board.isSquareFlagged(row, col);
    }

    public boolean isActive(){
        return board.isActive();
    }

    public int getOpenedSquares(){return board.getOpenedSquares();}

    public Square[][] getBoard(){return board.getBoard();}

}
