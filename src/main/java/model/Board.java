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

    private void setStatusAsNotStarted(){
        this.status = GameStatus.NOT_STARTED;
    }

    private void setStatusAsWon(){
        this.status = GameStatus.WON;
    }

    public void setStatusAsLost(){
        this.status = GameStatus.LOST;
    }

    private void setStatusAsActive(){
        this.status = GameStatus.ACTIVE;
    }

    private int getTotalSquares(){
        return gameBoard.length * gameBoard[0].length;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void checkWinCondition(){
        // ifall totala rutor - antalet minor = opened squares har man vunnit.
        if (getTotalSquares() - getTotalMines() == openedSquares && !getStatus().equals(GameStatus.LOST)) {
            setStatusAsWon();
        }
    }

    private void setAsBlown(int row, int col) {
        gameBoard[row][col].setAsBlown();
        setStatusAsLost();
    }


    public void createBoard(int row, int col) {
        // Skapar första array
        createFirstArray();
        createMines(row, col);
        setMinesAround();
        setStatusAsActive();
    }

    private void createFirstArray(){
        for (int row = 0; row < gameBoard.length ; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                gameBoard[row][col] = new Square(row, col, false);
            }
        }
    }

    public Square[][] getBoard(){
        Square[][] copy = gameBoard.clone();
        for (int i = 0; i < gameBoard.length; i++) {
            copy[i] = gameBoard[i].clone();
        }
        return copy;
    }

    private void setMinesAround(){
        for (int row = 0; row < gameBoard.length ; row++) {
            for (int col = 0; col < gameBoard[row].length ; col++) {
                var adjacentSquares = getAdjacentSquares(row, col);
                gameBoard[row][col].setMinesAround(Math.toIntExact(findMinesAround(adjacentSquares)));
            }
        }
    }

    private long findMinesAround(List<Square> adjacentSquares) {
        return adjacentSquares.stream().filter(Square::hasMine).count();
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

    private void createMines(int rowClicked, int colClicked){
        int totalSquares = getTotalSquares();
        int totalMines;
        switch (totalSquares) {
            // Beginner ger 10 % minor
            case 81 -> totalMines = (int) (totalSquares * 0.10);
            // Expert ger 18%
            case 480 -> totalMines = (int) (totalSquares * 0.18);
            // Default och intermediate ger 13%
            default -> totalMines = (int) (totalSquares * 0.13);
        }
        int minesCreated = 0;
        while (minesCreated < totalMines) {
            int row = random.nextInt(gameBoard.length);
            int col = random.nextInt(gameBoard[row].length);
            Square square = gameBoard[row][col];
            if (square.hasMine() || (row == rowClicked && col == colClicked)) {
                continue;
            }
            square.setHasMine();
            minesCreated += 1;
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

        if (square.hasMine()) {
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


    private int getTotalMines(){
        int totalMines = 0;
        for (Square[] row: gameBoard) {
            for (Square col: row) {
                if (col.hasMine()) {
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

    public void flagSquare(int row, int col) {
        if (getStatus() != GameStatus.ACTIVE) {
            return;
        }
        if (!gameBoard[row][col].isFlagged() && getTotalMinesForCounter() - 1 < 0) {
                return;
            }
        Square square = gameBoard[row][col];
        square.toggleFlag();
    }
}
