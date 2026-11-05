package presentation;

import com.sun.source.tree.NewClassTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.Square;

import java.util.function.BiConsumer;

public class BoardView extends VBox {
    private final BiConsumer<Integer, Integer> onSquareClick;
    private final BiConsumer<Integer, Integer> onFlagClick;
    private final Runnable onRestartGame;
    private StackPane[][] squareViews = new StackPane[16][16];
    private boolean hasWon = false;
    private boolean hasLost = false;
    private boolean isActive = false;
    private Label counter = new Label("0");
    private HBox smiley = new HBox();

    private final GridPane gridPane = new GridPane();
    private boolean flaggedClicked = false;

    public BoardView(
            BiConsumer<Integer, Integer> onSquareClick,
            BiConsumer<Integer, Integer> onFlagClick,
            Runnable onRestartGame
    ){
        this.onSquareClick = onSquareClick;
        this.onFlagClick = onFlagClick;
        this.onRestartGame = onRestartGame;
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40, 20, 20, 20));
        this.setStyle("-fx-background-color: #bdbdbd; -fx-border-width: 4px; -fx-border-color: #808080 #808080 #808080 #808080");
        HBox infoBox = createInfoBox();
        this.getChildren().addAll(infoBox,gridPane);
        createGrid();
    }

    public void updateBoard(Square[][] board, boolean isActive, int totalMines, boolean hasWon, boolean hasLost) {
        this.isActive = isActive;
        this.hasWon = hasWon;
        this.hasLost = hasLost;
        updateCounter(totalMines);
        if (isActive) {
            createHappySmiley();
        } else {
            createSadSmiley();
        }

        //Loopa igenom nya board
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length ; col++) {
                Square square = board[row][col];
                if (square.isBlown()) {
                    setSquareAsBlown(squareViews[row][col]);
                } else if (square.isFlagged()) {
                    squareViews[row][col].setStyle("""
                        -fx-background-color: #bdbdbd;
                        -fx-border-color: white #7b7b7b #7b7b7b white;
                        -fx-border-width: 3px;
                        -fx-background-image: url("assets/Flag.png");
                        -fx-background-repeat: no-repeat;
                        -fx-background-position: center;
                        -fx-background-size: contain;
                        """);
                } else if (square.isOpen()) {
                    if (square.isHasMine()) {
                        setSquareWithMineStyle(squareViews[row][col]);
                    } else {
                        setSquareClickedStyle(squareViews[row][col], square.getMinesAround());
                    }
                }
                else {
                    setSquareStandardStyle(squareViews[row][col]);
                }
            }
        }
    }

    private HBox createInfoBox(){
        HBox box = new HBox(10);
        HBox flag = createFlag();
        smiley = createSmiley();
        HBox countBox = createMineCounter();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(countBox,flag, smiley);
        return box;
    }

    private void updateCounter(int count){
        counter.setText(String.valueOf(count));
    }

    private HBox createMineCounter(){
        HBox countBox = new HBox();
        countBox.setAlignment(Pos.CENTER);
        countBox.setMinSize(120,60);
        countBox.setPrefSize(120,60);
        countBox.setStyle(" -fx-background-color: #bdbdbd; -fx-border-width: 3px; -fx-border-color: white #7b7b7b #7b7b7b white;");
        counter.getStyleClass().add("mine-counter");
        countBox.getChildren().add(counter);
        return countBox;
    }


    private HBox createSmiley(){
        smiley.setAlignment(Pos.CENTER);
        smiley.setMinSize(60,60);
        smiley.setPadding(new Insets(6));
        smiley.setPrefSize(60,60);
        createHappySmiley();
        smiley.setCursor(Cursor.HAND);

        // Vid restart
        smiley.setOnMouseClicked(event -> {
            flaggedClicked = false;
            hasLost = false;
            createHappySmiley();
            counter.setText("0");
            isActive = true;
            onRestartGame.run();
        });
        return smiley;
    }

    private void createHappySmiley() {
        smiley.setStyle("""
                         -fx-background-color: #bdbdbd;
                         -fx-background-image: url('assets/happy.png');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: 42px 42px;
                          -fx-border-width: 3px;
                          -fx-border-color: white #7b7b7b #7b7b7b white;
                         """);
    }
    private void createSadSmiley() {
        smiley.setStyle("""
                         -fx-background-color: #bdbdbd;
                         -fx-background-image: url('assets/sad.png');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: 62px 62px;
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
            if (!isActive) {
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

    private void setSquareClickedStyle(StackPane square, int minesAround){

        if (minesAround != 0) {
            Label minesLabel = new Label(String.valueOf(minesAround));
            switch (minesAround) {
                case 1 -> minesLabel.getStyleClass().add("mines-1");
                case 2 -> minesLabel.getStyleClass().add("mines-2");
                case 3 -> minesLabel.getStyleClass().add("mines-3");
                case 4 -> minesLabel.getStyleClass().add("mines-4");
                case 5 -> minesLabel.getStyleClass().add("mines-5");
                default -> {
                    return;
                }
            }
            square.getChildren().add(minesLabel);
        }
        square.setStyle("-fx-background-color: #b6b6b6; -fx-border-color: #8b8b8b; -fx-border-width: 2px");
    }

    private void setSquareAsBlown(StackPane square) {
        square.setStyle("""
                        -fx-background-color: #d50b06;
                         -fx-border-color: #8b8b8b;
                          -fx-border-width: 2px;
                          -fx-background-image: url("assets/mine.png");
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                         -fx-background-size: contain;
                          """);
    }

    private void setSquareWithMineStyle(StackPane square) {
        square.setStyle("""
                        -fx-background-color: #b6b6b6;
                         -fx-border-color: #8b8b8b;
                         -fx-border-width: 2px;
                         -fx-background-image: url("assets/mine.png");
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                         -fx-background-size: contain;
                        """);
    }


    public void createGrid(){
        gridPane.getChildren().clear();
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col  < 16 ; col++) {
                int currentRow = row;
                int currentCol = col;
                StackPane square = new StackPane();

                square.setPrefSize(40,40);
                setSquareStandardStyle(square);
                square.setCursor(Cursor.HAND);

                square.setOnMouseClicked(event -> {
                    if (hasLost) {
                        return;
                    }
                    // Om ej flagga klickat
                    if (!flaggedClicked) {
                        onSquareClick.accept(currentRow, currentCol);
                        return;
                    }
                    // Om flagga är klickad
                        onFlagClick.accept(currentRow, currentCol);
                });

                squareViews[row][col] = square;
                gridPane.add(square, col, row);
            }

        }


    }

}
