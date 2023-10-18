package hi.verkefni.vidmot;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

import java.io.File;

public class Levels {
    @FXML
    private Button levelTwoButton;
    @FXML
    private Button levelThreeButton;
    @FXML
    private PasswordField fxLevelsCode;
    MenuController MENU = (MenuController) ViewSwitcher.lookup(View.MENU);

    AudioClip BUTTONSELECT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-menu-select-sound-effect.mp3").toURI().toString());
    AudioClip CLICK = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/click.mp3").toURI().toString());

    private static boolean levelOneCleared = false;
    private static boolean levelTwoCleared = false;

    EventHandler<KeyEvent> enterKey = (event) -> {
        try {
            if (String.valueOf(event.getCode()).equals("ENTER")) {
                levelCodeUnlocker();
            }
        } catch (NullPointerException e) {
            event.consume();
        }
    };

    /**
     * Athugar hvaða borð hafa verið kláruð og sýnir svo
     * viðeigandi play takka fyrir borð sem hægt er að spila
     */
    public void checkLevelsCleared() {
        if (levelOneCleared) {
            levelTwoButton.setDisable(false);
        }
        if (levelTwoCleared) {
            levelThreeButton.setDisable(false);
        }
    }

    /**
     * Takki sem leyfir leikmanni að byrja að spila borð nr 1.
     */
    public void levelOneHandler(ActionEvent actionEvent) {
        CLICK.play();
        MENU.getMENUMUSIC().stop();
        ViewSwitcher.switchTo(View.GAMELEVELONE);
        GameController sc = (GameController) ViewSwitcher.lookup(View.GAMELEVELONE);
        sc.nyrLeikur();
    }

    /**
     * Takki sem leyfir leikmanni að byrja á borði nr 2 hafi hann verið
     * búinn að klára borð 1.
     */
    public void levelTwoHandler(ActionEvent actionEvent) {
        CLICK.play();
        MENU.getMENUMUSIC().stop();
        ViewSwitcher.switchTo(View.GAMELEVELTWO);
        GameController sc = (GameController) ViewSwitcher.lookup(View.GAMELEVELTWO);
        sc.nyrLeikur();
    }

    /**
     * Takki sem leyfir leikmanni að byrja á borði nr 3 hafi hann verið
     * búinn að klára borð 1 og 2.
     */
    public void levelThreeHandler(ActionEvent actionEvent) {
        CLICK.play();
        MENU.getMENUMUSIC().stop();
        ViewSwitcher.switchTo(View.GAMELEVELTHREE);
        GameLevelThreeController sc = (GameLevelThreeController) ViewSwitcher.lookup(View.GAMELEVELTHREE);
        sc.nyrLeikur();
    }

    /**
     * Skiptir yfir í menu.
     */
    public void menuHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        ViewSwitcher.switchTo(View.MENU);
    }

    /**
     * Skiptir booelan breytunni fyrir level 2 í levels yfir í true
     * þannig að þegar level 1 er klárað, þá er hægt að spila level 2
     * án þess að fara aftur í gegnum level 1.
     */
    public static void enableLevelTwoButton() {
        levelOneCleared = true;
    }

    /**
     * Skiptir booelan breytunni fyrir level 3 í levels yfir í true
     * þannig að þegar level 2 er klárað, þá er hægt að spila level 3
     * án þess að fara aftur í gegnum level 1 og 2.
     */
    public static void enableLevelThreeButton() {
        levelTwoCleared = true;
    }

    /**
     * Handler sem sæer um að aflæsa borð ef notandi er með rétta kóðann.
     */
    public void levelCodeUnlocker() {
        CharSequence passwordChars = fxLevelsCode.getCharacters();
        if(passwordChars.toString().equals("fatima")) {
            CLICK.play();
            levelTwoButton.setDisable(false);
            levelThreeButton.setDisable(false);
            levelOneCleared = true;
            levelTwoCleared = true;
            fxLevelsCode.clear();
        }
    }

    /**
     * Initialize sem keyrir hvenær sem levels er opnað,
     * kallar á fall sem athugar hvaða borð leikmaður getur spilað.
     */
    public void initialize() {

        checkLevelsCleared();

        ViewSwitcher.getScene().addEventFilter(KeyEvent.KEY_PRESSED, enterKey);
    }

}
