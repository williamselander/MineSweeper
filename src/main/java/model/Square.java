package model;

public class Square {
    private final int row;
    private final int col;
    private  boolean hasMine;
    private boolean isOpen;
    private boolean isFlagged;
    private int minesAround;
    private boolean blown;


    public Square(int row, int col, boolean hasMine){
        this.row = row;
        this.col = col;
        this.hasMine = hasMine;
        this.isOpen = false;
        this.isFlagged = false;
        this.minesAround = 0;
        this.blown = false;
    }

    void setAsOpen(){
        this.isOpen = true;
    }

    void toggleFlag(){
        if (isOpen) {
            return;
        }
        this.isFlagged = !isFlagged;
    }

    void setHasMine(){
        this.hasMine = true;
    }

    void setAsBlown(){
        this.blown = true;
    }

    public boolean isBlown(){
        return this.blown;
    }

    void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public int getMinesAround() {
        return minesAround;
    }
}
