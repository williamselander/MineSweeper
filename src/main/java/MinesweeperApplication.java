import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Board;
import presentation.Controller;


public class MinesweeperApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Board board = new Board();
        Controller controller = new Controller(board);

        GridPane box = createBox();
        box.getChildren().addAll(controller.getBoardView());

        Scene scene = new Scene(box, 800, 800);

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
}
