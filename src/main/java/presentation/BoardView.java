package presentation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import model.Square;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BoardView extends VBox {
    private final BiConsumer<Integer, Integer> onSquareClick;
    private final BiConsumer<Integer, Integer> onFlagClick;
    private final Supplier<Boolean> isActive;
    private final BiFunction<Integer, Integer, Boolean> isSquareFlagged;


    private final GridPane gridPane = new GridPane();
    private boolean flaggedClicked = false;

    public BoardView(
            BiConsumer<Integer, Integer> onSquareClick,
            BiConsumer<Integer, Integer> onFlagClick,
            Supplier<Boolean> isActive,
            BiFunction<Integer, Integer, Boolean> isSquareFlagged
    ){
        this.onSquareClick = onSquareClick;
        this.onFlagClick = onFlagClick;
        this.isActive = isActive;
        this.isSquareFlagged = isSquareFlagged;
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40, 20, 20, 20));
        this.setStyle("-fx-background-color: #bdbdbd; -fx-border-width: 4px; -fx-border-color: #808080 #808080 #808080 #808080");
        HBox infoBox = createInfoBox();


        this.getChildren().addAll(infoBox,gridPane);
        createGrid();
    }

    public void updateBoard(Square[][] board) {

        //Loopa igenom nya board
        for (Square[] row : board) {
            for (Square col : row) {





            }


        }


    }

    private HBox createInfoBox(){
        HBox box = new HBox(10);
        HBox flag = createFlag();
        HBox smiley = createSmiley();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(flag, smiley);
        return box;
    }

    private HBox createSmiley(){
        HBox smiley = new HBox();
        smiley.setAlignment(Pos.CENTER);
        smiley.setMinSize(60,60);
        smiley.setPadding(new Insets(6));
        smiley.setPrefSize(60,60);
        if (isActive.get()) {
            createHappySmiley(smiley);
        } else createSadSmiley(smiley);
        return smiley;
    }

    private void createHappySmiley(HBox box) {
        box.setStyle("""
                         -fx-background-color: #bdbdbd;
                         -fx-background-image: url('assets/happy.png');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: contain;
                          -fx-border-width: 3px;
                          -fx-border-color: white #7b7b7b #7b7b7b white;
                         """);
    }
    private void createSadSmiley(HBox box) {
        box.setStyle("""
                         -fx-background-color: #bdbdbd;
                         -fx-background-image: url('assets/happy.png');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: contain;
                          -fx-border-width: 3px;
                          -fx-border-color: white #7b7b7b #7b7b7b white;
                         """);

    }

    private HBox createFlag(){
        HBox flag = new HBox();
        flag.setAlignment(Pos.CENTER);
        flag.setMinSize(60,60);
        flag.setPrefSize(60,60);
        setFlagStyleNotClicked(flag);
        flag.setCursor(Cursor.HAND);
        flag.setOnMouseClicked(event -> {
            // Kan inte flagga innan vi har börjat
            if (Boolean.FALSE.equals(isActive.get())) {
                return;
            }
            flipFlag();
            if (!flaggedClicked) {
                setFlagStyleNotClicked(flag);
                return;
            }
            setFlagStyleClicked(flag);
        });

        return flag;
    }

    private void flipFlag(){
        this.flaggedClicked = !flaggedClicked;
    }

    private void setFlagStyleNotClicked(HBox flag) {
        flag.setStyle("""
                         -fx-background-color: #bdbdbd;
                         -fx-background-image: url('assets/Flag.png');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: contain;
                          -fx-border-width: 3px;
                          -fx-border-color: white #7b7b7b #7b7b7b white;
                         """);
    }

    private void setFlagStyleClicked(HBox flag) {
        flag.setStyle("""
                         -fx-background-color: #a0a0a0;
                         -fx-background-image: url('assets/Flag.png');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: contain;
                          -fx-border-width: 3px;
                          -fx-border-color:  #7b7b7b white  white #7b7b7b;
                         """);
    }

    private void setSquareStandardStyle(StackPane square) {
        square.setStyle("-fx-background-color: #bdbdbd;-fx-border-width: 3px ;-fx-border-color: white #7b7b7b #7b7b7b white;");
    }

    private void setSquareClickedStyle(StackPane square){
        square.setStyle("-fx-background-color: #b6b6b6; -fx-border-color: #8b8b8b; -fx-border-width: 2px");
    }


    private void createGrid(){
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col  < 16 ; col++) {
                int currentRow = row;
                int currentCol = col;
                StackPane square = new StackPane();

                square.setPrefSize(40,40);
                setSquareStandardStyle(square);
                square.setCursor(Cursor.HAND);

                square.setOnMouseClicked(event -> {

                    // Om ej flagga klickat
                    if (!flaggedClicked) {
                        // Om spelet ej har startat
                        System.out.println("Spelet har startat?: " + Boolean.FALSE.equals(isActive.get()));
                        if (Boolean.FALSE.equals(isActive.get())) {
                            onSquareClick.accept(currentRow, currentCol);
                            setSquareClickedStyle(square);
                            return;
                        }
                        // Om spelet har startat
                        System.out.println("Spelet har startat: " + isActive.get());

                        // Om den är flaggad kan den ej aktiveras
                        System.out.println("Är den flaggad?: " + isSquareFlagged.apply(currentRow, currentCol));
                        if (isSquareFlagged.apply(currentRow, currentCol)) {
                            System.out.println("Flaggad!");
                            return;
                        }
                        onSquareClick.accept(currentRow, currentCol);
                        setSquareClickedStyle(square);
                        return;
                    }

                    // Om flagga är klickad

                        // Om den har en flagga
                        if (Boolean.TRUE.equals(isSquareFlagged.apply(currentRow, currentCol))) {
                            setSquareStandardStyle(square);
                            onFlagClick.accept(currentRow, currentCol); // Flickar den så att den ej har en flagga
                            return;
                        }
                        // Om den ej har en flagga
                        square.setStyle("""
                        -fx-background-color: #bdbdbd;
                        -fx-border-color: white #7b7b7b #7b7b7b white;
                        -fx-border-width: 3px;
                        -fx-background-image: url("assets/Flag.png");
                        -fx-background-repeat: no-repeat;
                        -fx-background-position: center;
                        -fx-background-size: contain;
                        """);
                        onFlagClick.accept(currentRow, currentCol);
                });
                gridPane.add(square, col, row);
            }

        }


    }

}
