package presentation;

import model.Square;
import service.BoardService;

public class Controller {
    private final BoardService service;
    private final BoardView boardView;

    public Controller(BoardService service){
        this.service = service;
        this.boardView = new BoardView(this::handleSquareClick, this::handleFlagClick, this::getSquare, this::isBoardActive, this::isSquareFlagged);
    }

    private void startGame(int row, int col){
        service.createGame(row, col);
    }

    // Den första man klickar på får ej vara en mina.
    // Rutan får ej öppnas om den är flaggad
    // Börja sätta ut spelplanen här kanske???
    public Square[][] handleSquareClick(int row, int col){
        if (service.getOpenedSquares() == 0) {
            startGame(row, col);
            service.openSquare(row, col);
        } else service.openSquare(row, col);
        return getBoard();
    }

    public void handleFlagClick(int row, int col){
        service.flagSquare(row, col);
    }

    public Square getSquare(int row, int col){return service.getSquare(row,col);}

    public BoardView getBoardView(){return boardView;}

    private Square[][] getBoard(){return service.getBoard();}

    public boolean isBoardActive(){return service.isActive();}

    public boolean isSquareFlagged(int row, int col){return service.isSquareFlagged(row, col);}



}
