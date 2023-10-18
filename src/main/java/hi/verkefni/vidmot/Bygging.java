package hi.verkefni.vidmot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Bygging extends ImageView implements Leikhlutur{
    @FXML
    private Bygging fxBygging;
    private int OFFSET = 3;

    public Bygging() {
        lesa("bygging-view.fxml");
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
     * Bygging færist um OFFSET upp leikborðið.
     */
    public void afram() {
        fxBygging.relocate(fxBygging.getLayoutX() - OFFSET, fxBygging.getLayoutY());
    }

    /**
     * Bygging færist um OFFSET upp leikborðið,
     * ásamt því að færast þvert yfir leikborð.
     */
    public void levelTwoAfram() {
        fxBygging.relocate(fxBygging.getLayoutX() - OFFSET, fxBygging.getLayoutY());
        fxBygging.relocate(fxBygging.getLayoutX(), fxBygging.getLayoutY() - OFFSET);
    }

    /**
     * Bygging færist um OFFSET upp leikborðið.
     */
    public void levelThreeAfram() {
        fxBygging.relocate(fxBygging.getLayoutX() - OFFSET, fxBygging.getLayoutY());
    }

    /**
     * Setter fyrir offset sem leyfir því að stækka og þar með auka hraðann á pöllum
     * @param OFFSET
     */
    public void setOFFSET(int OFFSET) {
        this.OFFSET = OFFSET;
    }
}
