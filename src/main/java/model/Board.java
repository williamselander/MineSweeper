package model;

import java.util.Random;

public class Board {
    private Square[][] board = new Square[16][16];
    private final Random random = new Random();
    private boolean hasWon;

    public Board() {
        this.hasWon = false;
    }

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

        int startRow = Math.max(0, row -1);
        int endRow = Math.min(board.length - 1, row + 1);

        int startCol = Math.max(0, col -1);
        int endCol = Math.min(board[0].length - 1, col + 1);

        for (int r = startRow; r <= endRow ; r++) {
            for (int c = startCol; c <= endCol ; c++) {
                // Tar inte sig själv
                if (r == row && c == col) {
                    continue;
                }
                if (board[r][c].isHasMine()) {
                    minesAround++;
                }
            }
        }
        return minesAround;
    }

    private boolean hasMine(){
        // 10% chans för en mina
        return random.nextDouble() < 0.10;
    }

    public boolean isHasWon(){return hasWon;}

    public void openSquare(int row, int col) {
        Square squareToOpen = board[row][col];
        squareToOpen.setAsOpen();
    }






}
