import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Board;
import presentation.Controller;

import java.util.Objects;


public class MinesweeperApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Board board = new Board();
        Controller controller = new Controller(board);

        BorderPane root = new BorderPane();
        GridPane desktop = createBox();
        desktop.add(controller.getBoardView(), 0,0);
        root.setCenter(desktop);
        root.setBottom(createBottom());

        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static GridPane createBox() {
        GridPane box = new GridPane();
        Image image = new Image("assets/background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO,
                        false,false,true,true
                )
        );
        box.setBackground(new Background(backgroundImage));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private static HBox createBottom(){
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(4));
        box.setStyle("""
        -fx-background-color: #bfb9be;
        -fx-border-color: white transparent transparent transparent;
        -fx-border-width: 2px;
        """);

        HBox startBtn = new HBox();
        startBtn.setPrefSize(90,32);
        startBtn.setMinSize(90,31);
        startBtn.setMaxSize(90,32);
        startBtn.setStyle("-fx-background-image: url('assets/StartButton.jpg'); -fx-background-size: contain; -fx-background-position: center; -fx-background-repeat: no-repeat");
        box.getChildren().addAll(startBtn);
        return box;
    }
}
