package hi.verkefni.vidmot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Pane(), 600, 500);
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.MENU);
        stage.setTitle("Final Fatima");
        URL IconImgURL = GameController.class.getResource("myndir/pepsi-max-jetpack1.png");
        stage.getIcons().add(new Image(IconImgURL.toString()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}