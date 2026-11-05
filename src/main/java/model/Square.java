package model;

public class Square {
    private final int row;
    private final int col;
    private boolean hasMine;
    private boolean isOpen;
    private boolean isFlagged;
    private int minesAround;


    public Square(int row, int col, boolean hasMine){
        this.row = row;
        this.col = col;
        this.hasMine = hasMine;
        this.isOpen = false;
        this.isFlagged = false;
        this.minesAround = 0; // Antalet minor runt är 0 vid instansering
    }

    public void setAsOpen(){
        this.isOpen = true;
        System.out.println("Öppnar");
    }

    public void setFlagged(){
        System.out.println("Innan: " + isFlagged);

        this.isFlagged = !isFlagged;

        System.out.println("Efter: " + isFlagged);
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
