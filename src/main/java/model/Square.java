package model;

public class Square {
    private final int row;
    private final int col;
    private boolean hasMine;
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

    public void setAsOpen(){
        this.isOpen = true;
    }

    public void setFlagged(){
        if (isOpen) {
            return;
        }
        this.isFlagged = !isFlagged;
    }

    public void setAsBlown(){
        this.blown = true;
    }

    public boolean isBlown(){
        return this.blown;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isHasMine() {
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
