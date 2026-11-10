package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private final Square[][] gameBoard;
    private final Random random = new Random();
    private int openedSquares = 0;
    private GameStatus status;


    public Board(Square[][] gameBoard) {
        setStatusAsNotStarted();
        this.gameBoard = gameBoard;
    }

    public void setStatusAsNotStarted(){
        this.status = GameStatus.NOT_STARTED;
    }

    public void setStatusAsWon(){
        this.status = GameStatus.WON;
    }

    public void setStatusAsLost(){
        this.status = GameStatus.LOST;
    }

    public void setStatusAsActive(){
        this.status = GameStatus.ACTIVE;
    }

    private int getTotalSquares(){
        return gameBoard.length * gameBoard[0].length;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void hasPlayerWon(){
        // ifall totala rutor - antalet minor = opened squares har man vunnit.
        if (getTotalSquares() - getTotalMines() == openedSquares && !getStatus().equals(GameStatus.LOST)) {
            setStatusAsWon();
        }
    }

    public void setAsBlown(int row, int col) {
        gameBoard[row][col].setAsBlown();
        setStatusAsLost();
    }


    public void createBoard(int row, int col) {
        // Skapar första array
        createFirstArray(row, col);
        setMinesAround();
        setStatusAsActive();
    }

    private void createFirstArray(int rowClicked, int colClicked){
        for (int row = 0; row < gameBoard.length ; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                boolean hasMine;
                // Ifall det är koordinaten där användaren klickat, bir det ej en mina.
                if (row == rowClicked && col == colClicked) {
                    hasMine = false;
                } else hasMine = hasMine();
                gameBoard[row][col] = new Square(row, col, hasMine);
            }
        }
    }

    public Square[][] getBoard(){return gameBoard.clone();}

    private void setMinesAround(){
        for (int row = 0; row < gameBoard.length ; row++) {
            for (int col = 0; col < gameBoard[row].length ; col++) {
                var adjacentSquares = getAdjacentSquares(row, col);
                gameBoard[row][col].setMinesAround(Math.toIntExact(findMinesAround(adjacentSquares)));
            }
        }
    }

    private Long findMinesAround(List<Square> adjacentSquares) {
        return adjacentSquares.stream().filter(Square::isHasMine).count();
    }

    private List<Square> getAdjacentSquares(int row, int col){
        List<Square> adjacentSquares = new ArrayList<>();

        int startRow = Math.max(0, row -1);
        int endRow = Math.min(gameBoard.length - 1, row + 1);

        int startCol = Math.max(0, col -1);
        int endCol = Math.min(gameBoard[0].length - 1, col + 1);

        for (int r = startRow; r <= endRow ; r++) {
            for (int c = startCol; c <= endCol ; c++) {
                // Tar inte sig själv
                if (r == row && c == col) {
                    continue;
                }
                adjacentSquares.add(gameBoard[r][c]);
            }
        }
        return adjacentSquares;
    }

    private boolean hasMine(){
        int totalSquares = getTotalSquares();

        switch (totalSquares) {
            // Beginner ger 10 % för mina
            case 81 -> {
                return random.nextDouble() < 0.10;
            }
            // Expert ger 20%
            case 480 -> {
                return random.nextDouble() < 0.20;
            }
            // Default och intermediate ger 13%
            default -> {
                return random.nextDouble() < 0.13;
            }
        }
    }

    public void reveal(int row, int col) {

        if (!getStatus().equals(GameStatus.ACTIVE)) {
            return;
        }
        Square square = gameBoard[row][col];

        if (square.isOpen() || square.isFlagged()) {
            return;
        }

        square.setAsOpen();
        openedSquares++;

        if (square.isHasMine()) {
            setAsBlown(row, col);
            return;
        }

        if (square.getMinesAround() == 0) {
            List<Square> adjacentSquares = getAdjacentSquares(row, col);

            for (Square adjacent: adjacentSquares) {
                reveal(adjacent.getRow(), adjacent.getCol());
            }
        }
    }


    public int getTotalMines(){
        int totalMines = 0;
        for (Square[] row: gameBoard) {
            for (Square col: row) {
                if (col.isHasMine()) {
                    totalMines++;
                }
            }
        }
        return totalMines;
    }

    public int getTotalMinesForCounter(){
        return getTotalMines() - getTotalFlaggedMines();
    }

    private int getTotalFlaggedMines(){
        int totalFlagged = 0;
        for (Square[] row: gameBoard) {
            for (Square col: row) {
                if (col.isFlagged()) {
                    totalFlagged++;
                }
            }
        }
        return totalFlagged;
    }

    public int getOpenedSquares(){return openedSquares;}

    public void flagSquare(int row, int col) {
        if (getStatus() != GameStatus.ACTIVE) {
            return;
        }
        if (!gameBoard[row][col].isFlagged() && getTotalMinesForCounter() - 1 < 0) {
                return;
            }
        Square square = gameBoard[row][col];
        square.setFlagged();
    }
}
