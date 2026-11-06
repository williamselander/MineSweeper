package presentation;

import com.sun.source.tree.NewClassTree;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
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
    private ToggleGroup difficulty = new ToggleGroup();

    private final GridPane gridPane = new GridPane();
    public BoardView(
            BiConsumer<Integer, Integer> onSquareClick,
            BiConsumer<Integer, Integer> onFlagClick,
            Runnable onRestartGame
    ){
        this.onSquareClick = onSquareClick;
        this.onFlagClick = onFlagClick;
        this.onRestartGame = onRestartGame;
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 20, 20, 20));
        this.setStyle("-fx-background-color: #bdbdbd; -fx-border-width: 5px; -fx-border-color: white #7b7b7b #7b7b7b white");
        HBox infoBox = createInfoBox();
        MenuBar settings =createSettings();
        HBox top = new HBox(2);
        top.setStyle("-fx-background-image: url('assets/top.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 100% 100%");
        top.setMinHeight(40);
        top.setPrefHeight(40);
        top.setMaxHeight(40);
        this.getChildren().addAll(top,settings,infoBox,gridPane);
        createGrid();
    }


    public MenuBar createSettings() {
        MenuItem newGame = new MenuItem("New");
        newGame.setOnAction(event -> onRestartGame.run());

        RadioMenuItem beginner = new RadioMenuItem("Beginner");
        RadioMenuItem intermediate = new RadioMenuItem("Intermediate");
        RadioMenuItem expert = new RadioMenuItem("Expert");

        beginner.setToggleGroup(difficulty);
        intermediate.setToggleGroup(difficulty);
        expert.setToggleGroup(difficulty);
        intermediate.setSelected(true);

        MenuItem bestTimes = new MenuItem("Best Times");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());

        Menu game = new Menu("Game");
        game.getItems().addAll(
                newGame,
                new SeparatorMenuItem(),
                beginner,
                intermediate,
                expert,
                new SeparatorMenuItem(),
                bestTimes,
                exit
        );

        Menu help = new Menu("Help");


        MenuItem firsHelp = new MenuItem("1. Öppna en ruta med vänsterklick för att börja");
        MenuItem secondHelp = new MenuItem("2. Rutor med siffror har så många minor runt sig");
        MenuItem thirdHelp = new MenuItem("3. Flagga rutorna som du tror har en mina med högerklick");
        MenuItem fourthHelp = new MenuItem("4. Ifall du klickar på en ruta med mina har du förlorat och smileyn blir ledsen");
        MenuItem fifthHelp = new MenuItem("5. Du har vunnit när alla rutor förutom dom flaggade är klickade,\n och du har markerat rätt rutor med minor. Smileyn kommer bli cool");
        MenuItem lastHelp = new MenuItem("6. Starta om spelet antingen i Game eller genom att klicka på smileyn");

        help.getItems().addAll(
                firsHelp,
                secondHelp,
                thirdHelp,
                fourthHelp,
                fifthHelp,
                lastHelp
        );
        return new MenuBar(game, help);
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
        if (hasWon) {
            createWinSmiley();
        }
        //Loopa igenom nya board
        loopThroughBoard(board);
    }

    private void loopThroughBoard(Square[][] board){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length ; col++) {
                Square square = board[row][col];
                if (hasLost) {
                    if (square.isBlown()) {
                        setSquareAsBlown(squareViews[row][col]);
                    } else if (square.isHasMine()) {
                        setSquareAsMine(squareViews[row][col]);
                    } else if (square.isOpen()) {
                        setSquareClickedStyle(squareViews[row][col], square.getMinesAround());
                    } else setSquareStandardStyle(squareViews[row][col]);
                }
                if (!hasLost) {
                    if (square.isFlagged()) {
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
    }

    private HBox createInfoBox(){
        HBox box = new HBox(10);
        box.setStyle("""
                -fx-border-width: 5px;

                -fx-border-color:  white #7b7b7b #7b7b7b white;
                """);
        smiley = createSmiley();
        HBox countBox = createMineCounter();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(countBox, smiley);
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
            hasLost = false;
            hasWon = false;
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

    private void createWinSmiley(){
        smiley.setStyle("""
                         -fx-background-color: #bdbdbd;
                         -fx-background-image: url('assets/win.gif');
                         -fx-background-repeat: no-repeat;
                         -fx-background-position: center;
                          -fx-background-size: 62px 62px;
                          -fx-border-width: 3px;
                          -fx-border-color: white #7b7b7b #7b7b7b white;
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

    private void setSquareAsMine(StackPane square) {
        square.setStyle("""
                        -fx-background-color: #bdbdbd;
                         -fx-border-color: white #7b7b7b #7b7b7b white;
                          -fx-border-width: 3px;
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

        int rows = 16;
        int cols = 16;

        switch (difficulty.getSelectedToggle().toString()) {
            case "Beginner" -> {
                rows = 9;
                cols = 9;
            }
            case "Expert" -> {
                rows = 30;
                cols = 16;
            }
            default -> {
                rows = 16;
                cols = 16;
            }
        }

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
                    if (event.getButton() == MouseButton.PRIMARY) {
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
