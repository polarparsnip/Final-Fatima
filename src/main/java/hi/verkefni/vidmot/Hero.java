package hi.verkefni.vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;

public class Hero extends ImageView implements Leikhlutur {

    private int counter = 0;

    private int OFFSET = 10;

    URL secondImgURL = GameController.class.getResource("myndir/pepsi-max-jetpack2.png");
    URL imgURL = GameController.class.getResource("myndir/pepsi-max-jetpack1.png");
    Image EXHAUST = new Image(secondImgURL.toString());
    Image NONEXHAUST = new Image(imgURL.toString());

    public Hero() {
        lesa("hero-view.fxml");
    }

    /**
     * Lesa inn útlit úr fxml skrá
     * @param fxmlSkra nafn á fxml skrá
     */
    protected void lesa(String fxmlSkra) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlSkra));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Lætur hero falla til jörðu eftir að counter fer í 0,
     * breytir svo mynd á hero í útgáfu án útblásturs.
     */
    public void afram() {
        if (counter == 0) {
            setFitHeight(60);
            setImage(NONEXHAUST);
            setLayoutY((int)(getLayoutY() + OFFSET));
        }

        if (counter != 0) {
            counter--;
        }
    }

    /**
     * Lætur hero fjúga upp þegar ýtt er á flugtakka og breytir mynd í útgáfu
     * með útblástri, counter er svo settur í 5 þannig hero byrjar ekki að strax
     * að falla til jörðu.
     * Kveikir svo líka á hljóði fyrir útblástur.
     */
    public void fljuga() {
        setFitHeight(70);
        setImage(EXHAUST);
        counter = 5;
        setLayoutY((int)(getLayoutY() - 1.2*OFFSET));

    }
}
