package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private final Square[][] gameBoard = new Square[16][16];
    private final Random random = new Random();
    private boolean hasWon;
    private int openedSquares = 0;
    private boolean isActive = false;

    public Board() {
        this.hasWon = false;
    }

    public void setAsWon(){
        this.hasWon = true;
        this.isActive = false;
    }

    public boolean isActive(){return isActive;}
    public void setAsActive(){this.isActive = true;}

    public void createBoard(int row, int col) {
        // Skapar första array
        createFirstArray(row, col);
        setMinesAround();
        setAsActive();

        System.out.println(isActive);
    }

    private void createFirstArray(int rowClicked, int colClicked){
        for (int row = 0; row < 16 ; row++) {
            for (int col = 0; col < 16; col++) {
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
        for (int row = 0; row < 16 ; row++) {
            for (int col = 0; col < 16 ; col++) {
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
        // 7% chans för en mina
        return random.nextDouble() < 0.07;
    }


    public void reveal(int row, int col) {
        Square square = gameBoard[row][col];
        if (square.isOpen() || square.isFlagged()) {
            return;
        }
        square.setAsOpen();
        if (square.getMinesAround() == 0) {
            List<Square> adjacentSquares = getAdjacentSquares(row, col);

            for (Square adjacent: adjacentSquares) {
                reveal(adjacent.getRow(), adjacent.getCol());
            }
        }
    }

    public boolean isHasWon(){return hasWon;}

    public int getTotalMines(){
        int totalMines = 0;
        for (Square[] row: gameBoard) {
            for (Square col: row) {
                if (col.isHasMine()) {
                    totalMines++;
                }
            }
        }
        return totalMines - getTotalFlaggedMines();
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

    public void openSquare(int row, int col) {
        Square squareToOpen = gameBoard[row][col];
        // Om den redan är öppnad eller flaggad
        if (squareToOpen.isOpen() || squareToOpen.isFlagged()) {
            return;
        }
        squareToOpen.setAsOpen();
        openedSquares++;
    }

    public int getOpenedSquares(){return openedSquares;}

    public void flagSquare(int row, int col) {
        Square square = gameBoard[row][col];
        square.setFlagged();
    }




}
