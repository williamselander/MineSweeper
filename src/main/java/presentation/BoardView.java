package presentation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;
import model.GameStatus;
import model.Square;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class BoardView extends VBox {
    private final BiConsumer<Integer, Integer> onSquareClick;
    private final BiConsumer<Integer, Integer> onFlagClick;
    private final Runnable onRestartGame;
    private final Runnable onLoss;
    private StackPane[][] squareViews;
    private GameStatus gameStatus = GameStatus.NOT_STARTED;
    private final Label counter = new Label("0");
    private final Timeline timeline = new Timeline();
    private int maxSeconds = 300;
    private final Label timer = new Label("05:00");
    private boolean timerStarted = false;
    private boolean timerApplied = true;
    private HBox smiley = new HBox();
    private final ToggleGroup difficulty = new ToggleGroup();
    private final ToggleGroup timerSettings = new ToggleGroup();
    private int rowsBoard;
    private int colsBoard;
    private final GridPane gridPane = new GridPane();

    public BoardView(
            BiConsumer<Integer, Integer> onSquareClick,
            BiConsumer<Integer, Integer> onFlagClick,
            Runnable onRestartGame,
            Runnable onLoss
    ){
        this.onSquareClick = onSquareClick;
        this.onFlagClick = onFlagClick;
        this.onRestartGame = onRestartGame;
        this.onLoss = onLoss;
        this.getStyleClass().add("main-box");
        HBox infoBox = createInfoBox();
        MenuBar settings = createSettings();
        HBox top = createTop();
        gridPane.setAlignment(Pos.CENTER);
        this.getChildren().addAll(top,settings,infoBox,gridPane);
        createGrid();
    }

    private void setMaxSeconds(int seconds) {this.maxSeconds = seconds;}

    private HBox createTop(){
        HBox top = new HBox(5);
        top.getStyleClass().add("top-box");
        Region mine = createTitleIcon("mine.png", 40,40);
        Label text = new Label("Minesweeper");
        text.getStyleClass().add("top-text");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox threeIcons = new HBox(3);
        threeIcons.setAlignment(Pos.CENTER);
        threeIcons.getChildren().addAll(
                createTitleIcon("minimi.png", 22,22),
                            createTitleIcon("maximi.png", 22,22),
                            createTitleIcon("kryss.png", 22,22)
        );
        top.getChildren().addAll(mine, text, spacer ,threeIcons);
        return top;
    }

    private Region createTitleIcon(String fileName, int width, int height) {
        Region icon = new Region();
        icon.setMinSize(width,height);
        icon.setPrefSize(width,height);
        icon.setMaxSize(width,height);

        icon.setStyle("""
        -fx-background-image: url('assets/%s');
        -fx-background-repeat: no-repeat;
        -fx-background-position: center;
        -fx-background-size: contain;
        """.formatted(fileName));
        if (fileName.equals("kryss.png")) {
            icon.setCursor(Cursor.HAND);
            icon.setOnMouseClicked(event -> Platform.exit());
        }
        return icon;
    }

    private RadioMenuItem createDifficultyOptions(String text){
        RadioMenuItem option = new RadioMenuItem(text);
        option.setToggleGroup(difficulty);
        option.setOnAction(event -> onRestartGame.run());
        return option;
    }

    private RadioMenuItem createTimerOptions(String text) {
        RadioMenuItem option = new RadioMenuItem(text);
        option.setToggleGroup(timerSettings);
        if (text.equals("Ingen timer")) {
            option.setOnAction(event -> {
                timerApplied = false;
                onRestartGame.run();
            });
            return option;
        }
        option.setOnAction(event -> {
            timerApplied = true;
            onRestartGame.run();
        });
        return option;
    }

    private MenuBar createSettings() {
        MenuItem newGame = new MenuItem("New");
        newGame.setOnAction(event -> onRestartGame.run());

        RadioMenuItem beginner = createDifficultyOptions("Nybörjare");
        RadioMenuItem intermediate = createDifficultyOptions("Mellannivå");
        RadioMenuItem expert = createDifficultyOptions("Expert");

        RadioMenuItem noTimer = createTimerOptions("Ingen timer");
        RadioMenuItem standard = createTimerOptions("Standard");

        intermediate.setSelected(true);
        standard.setSelected(true);

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
                noTimer,
                standard,
                new SeparatorMenuItem(),
                exit
        );

        Menu help = createHelpOptions();
        return new MenuBar(game, help);
    }

    private Menu createHelpOptions(){
        Menu help = new Menu("Help");
        MenuItem firsHelp = new MenuItem("1. Öppna en ruta med vänsterklick för att börja");
        MenuItem secondHelp = new MenuItem("2. Rutor med siffror har så många minor runt sig");
        MenuItem thirdHelp = new MenuItem("3. Flagga rutorna som du tror har en mina med högerklick");
        MenuItem fourthHelp = new MenuItem("4. Ifall du klickar på en ruta med mina eller att tiden går ut har du förlorat ");
        MenuItem fifthHelp = new MenuItem("5. Du har vunnit när alla rutor utom minor har öppnats,\n Smileyn kommer bli cool!");
        MenuItem lastHelp = new MenuItem("6. Starta om spelet antingen i Game eller genom att klicka på smileyn");
        help.getItems().addAll(
                firsHelp,
                secondHelp,
                thirdHelp,
                fourthHelp,
                fifthHelp,
                lastHelp
        );
        return help;
    }

    public void updateBoard(Square[][] board, GameStatus gameStatus, int totalMines) {
        this.gameStatus = gameStatus;

        if (!timerApplied) {
            timer.setText("---");
        }
        switch (gameStatus) {
            case GameStatus.WON -> {
                timeline.stop();
                updateCounter("Win!");
            }
            case GameStatus.LOST -> {
                timeline.stop();
                updateCounter("Lost!");
            }
            default -> updateCounter(String.valueOf(totalMines));

        }
        if (gameStatus == GameStatus.ACTIVE) {
            createHappySmiley();
        } else {
            createSadSmiley();
        }
        if (gameStatus == GameStatus.WON) {
            createWinSmiley();
        }
        //Loopa igenom nya board
        loopThroughBoard(board);
    }

    private void renderSquareAfterLoss(Square square, int row, int col){
        if (square.isBlown()) {
            setSquareAsBlown(squareViews[row][col]);
        } else if (square.hasMine()) {
            setSquareAsMine(squareViews[row][col]);
        } else if (square.isOpen()) {
            setSquareClickedStyle(squareViews[row][col], square.getMinesAround());
        } else setSquareStandardStyle(squareViews[row][col]);
    }

    private void renderSquareDuringGame(Square square, int row, int col) {
        if (square.isFlagged()) {
            squareViews[row][col].getStyleClass().setAll("square-with-flag");
        } else if (square.isOpen()) {
            if (square.hasMine()) {
                setSquareWithMineStyle(squareViews[row][col]);
            } else {
                setSquareClickedStyle(squareViews[row][col], square.getMinesAround());
            }
        }
        else {
            setSquareStandardStyle(squareViews[row][col]);
        }
    }

    private void loopThroughBoard(Square[][] board){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length ; col++) {
                Square square = board[row][col];
                if (gameStatus == GameStatus.LOST) {
                    renderSquareAfterLoss(square, row, col);
                }
                if (gameStatus != GameStatus.LOST) {
                    renderSquareDuringGame(square, row, col);
                }
            }
        }
    }

    private HBox createInfoBox(){
        HBox box = new HBox(10);
        box.getStyleClass().add("info-box");
        smiley = createSmiley();

        HBox countBox = createCounter(120);
        countBox.getChildren().add(counter);
        counter.getStyleClass().add("mine-counter");

        HBox timerBox = createCounter(140);
        timerBox.getChildren().add(timer);
        timer.getStyleClass().add("timer");

        box.getChildren().addAll(countBox, smiley, timerBox);
        return box;
    }

    private void createTimeline(){
        AtomicInteger elapsedSeconds = new AtomicInteger(0);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(false);
        KeyFrame oneSecond = new KeyFrame(Duration.seconds(1), event -> {
            int current = elapsedSeconds.incrementAndGet();
            if (current >= maxSeconds ) {
                timeline.stop();
                onLoss.run();
                setTimerText(0,0);
                updateCounter("Lost!");
                createSadSmiley();
                return;
            }
            setTimerText(maxSeconds, current);
        });
        timeline.getKeyFrames().add(oneSecond);
        timeline.play();
    }

    private void setTimerText(int maxSeconds, int currentSeconds){
        int remainingSeconds = maxSeconds-currentSeconds;
        if (remainingSeconds < 0 ) {
            remainingSeconds = 0;
        }
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateCounter(String value){
        counter.setText(value);
    }

    private HBox createCounter(int width){
        HBox countBox = new HBox();
        countBox.getStyleClass().add("count-box");
        countBox.setMinSize(width,60);
        countBox.setPrefSize(width,60);
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
            createHappySmiley();
            counter.setText("0");
            onRestartGame.run();
        });
        return smiley;
    }

    private void createHappySmiley() {
        smiley.getStyleClass().setAll("smiley-happy");
    }
    private void createSadSmiley() {
        smiley.getStyleClass().setAll("smiley-sad");
    }
    private void createWinSmiley(){
        smiley.getStyleClass().setAll("smiley-win");
    }

    private void setSquareStandardStyle(StackPane square) {
        square.getStyleClass().setAll("square-standard");
    }

    private void setSquareClickedStyle(StackPane square, int minesAround){
        square.getChildren().clear();
        if (minesAround != 0) {
            Label minesLabel = new Label(String.valueOf(minesAround));
            switch (minesAround) {
                case 1 -> minesLabel.getStyleClass().add("mines-1");
                case 2 -> minesLabel.getStyleClass().add("mines-2");
                case 3 -> minesLabel.getStyleClass().add("mines-3");
                case 4 -> minesLabel.getStyleClass().add("mines-4");
                case 5 -> minesLabel.getStyleClass().add("mines-5");
                case 6 -> minesLabel.getStyleClass().add("mines-6");
                case 7 -> minesLabel.getStyleClass().add("mines-7");
                case 8 -> minesLabel.getStyleClass().add("mines-8");
                default -> {
                    return;
                }
            }
            square.getChildren().add(minesLabel);
        }
        square.getStyleClass().setAll("square-clicked");
    }

    private void setSquareAsBlown(StackPane square) {
        square.setStyle("");
        square.getStyleClass().setAll("square-blown");
    }

    private void setSquareAsMine(StackPane square) {
        square.getStyleClass().setAll("square-as-mine");
    }

    private void setSquareWithMineStyle(StackPane square) {
        square.getStyleClass().setAll("square-with-mine");
    }

    public int getRowsBoard(){
        return this.rowsBoard;
    }

    public int getColsBoard(){
        return this.colsBoard;
    }

    private void resetBoardView(){
        updateCounter("33");
        timeline.stop();
        timeline.getKeyFrames().clear();
        timerStarted = false;
        this.gameStatus = GameStatus.NOT_STARTED;
        createHappySmiley();
        gridPane.getChildren().clear();
    }

    private void applySelectedDifficulty(){
        RadioMenuItem selected = (RadioMenuItem) difficulty.getSelectedToggle();
        switch (selected.getText()) {
            case "Nybörjare" -> {
                this.rowsBoard = 9;
                this.colsBoard = 9;
                setMaxSeconds(300);
                updateCounter("8");
            }
            case "Expert" -> {
                this.rowsBoard = 16;
                this.colsBoard = 30;
                setMaxSeconds(420);
                updateCounter("86");
            }
            default -> {
                this.rowsBoard = 16;
                this.colsBoard = 16;
                setMaxSeconds(300);
                updateCounter("33");
            }
        }
    }

    public void createGrid(){
        resetBoardView();
        applySelectedDifficulty();
        if (timerApplied) {
            setTimerText(maxSeconds, 0);
        } else this.timer.setText("---");
        squareViews = new StackPane[rowsBoard][colsBoard];
        createSquares();
    }

    private void createSquares(){
        for (int row = 0; row < rowsBoard; row++) {
            for (int col = 0; col < colsBoard ; col++) {
                int currentRow = row;
                int currentCol = col;
                StackPane square = new StackPane();
                square.setPrefSize(40,40);
                setSquareStandardStyle(square);
                square.setCursor(Cursor.HAND);

                square.setOnMouseClicked(event -> {
                    if (gameStatus == GameStatus.LOST) {
                        return;
                    }
                    // Om ej flagga klickat
                    if (event.getButton() == MouseButton.PRIMARY) {
                        // Ifall användaren spelar med timer på och den ej har satts på ännu
                        if (!timerStarted && timerApplied) {
                            createTimeline(); // sätter på timern
                            timerStarted = true;
                        }
                        onSquareClick.accept(currentRow, currentCol);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        // Om flagga är klickad
                        onFlagClick.accept(currentRow, currentCol);
                    }
                });
                squareViews[row][col] = square;
                gridPane.add(square, col, row);
            }
        }
    }


}
