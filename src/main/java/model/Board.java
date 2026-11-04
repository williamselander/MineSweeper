package model;

import java.util.Random;

public class Board {
    private Square[][] board = new Square[16][16];
    private final Random random = new Random();

    public void createBoard() {
        // Skapar första array
        createFirstArray();
        setMinesAround();
    }

    private void createFirstArray(){
        for (int row = 0; row < 16 ; row++) {
            for (int col = 0; col < 16; col++) {
                boolean hasMine = hasMine();
                board[row][col] = new Square(row, col, hasMine);
            }
        }
    }

    public Square[][] getBoard(){return board;}

    private void setMinesAround(){
        for (int row = 0; row < 16 ; row++) {
            for (int col = 0; col < 16 ; col++) {
                board[row][col].setMinesAround(findMinesAround(row, col));
            }
        }
    }

    private int findMinesAround(int row, int col){
        int minesAround = 0;
        int startRow;
        int startCol;
        int endRow;
        int endCol;

        if (row == 0) {
            startRow = row;
        } else startRow = row - 1;

        if (col == 0) {
            startCol = col;
        } else startCol = col - 1;

        endRow = startRow + 2;
        endCol = startCol + 2;

        for (int r = startRow; r < endRow ; r++) {
            for (int c = startCol; c < endCol ; c++) {
                if (board[r][c].isHasMine()) {
                    minesAround++;
                }
            }
        }
        return minesAround;
    }

    private boolean hasMine(){
        // 10% chans för en mina
        return random.nextDouble() > 0.10;
    }

    public void openSquare(int row, int col) {
        Square squareToOpen = board[row][col];
        squareToOpen.setAsOpen();
    }






}
