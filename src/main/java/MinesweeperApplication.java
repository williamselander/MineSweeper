import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import presentation.Controller;

import java.util.Objects;


public class MinesweeperApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();

        BorderPane root = new BorderPane();
        BorderPane desktop = createBox();
        VBox desktopsIcons = createIcons();

        StackPane gameArea = new StackPane(controller.getBoardView());
        controller.getBoardView().setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        desktop.setCenter(gameArea);
        desktop.setLeft(desktopsIcons);
        BorderPane.setAlignment(desktopsIcons, Pos.TOP_LEFT);
        BorderPane.setMargin(desktopsIcons, new Insets(80,20,20,20));
        root.setCenter(desktop);
        root.setBottom(createBottom());

        Scene scene = new Scene(root, 1200, 1200);

        desktopsIcons.visibleProperty().bind(
                scene.widthProperty().greaterThan(1300)
        );
        desktopsIcons.managedProperty().bind(
                desktopsIcons.visibleProperty()
        );

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static VBox createIcons(){
        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);
        box.setFillWidth(false);
        box.getChildren().addAll(
                createIcon("paint.png"),
                createIcon("RecycleBin.png"),
                createIcon("myComputer.png"),
                createIcon("neighbour.png")
        );
        return box;
    }

    private static Region createIcon(String fileName) {
        Region icon = new Region();
        icon.setMinSize(68, 68);
        icon.setPrefSize(68, 68);
        icon.setMaxSize(68, 68);
        Image image = new Image(Objects.requireNonNull(
                MinesweeperApplication.class.getResource("/assets/" + fileName)
        ).toExternalForm());
        icon.setBackground(new Background(new BackgroundImage(
                image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
        )));
        return icon;
    }
    private static BorderPane createBox() {
        BorderPane box = new BorderPane();
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

