package hi.verkefni.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;

import java.io.File;

public class SettingsController {
    @FXML
    private Button musicButton;
    @FXML
    private Button sfxButton;

    AudioClip BUTTONSELECT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-menu-select-sound-effect.mp3").toURI().toString());
    AudioClip CLICK = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/click.mp3").toURI().toString());
    MenuController MENU = (MenuController) ViewSwitcher.lookup(View.MENU);
    private Boolean music = true;
    private Boolean sfx = true;

    public Boolean getMusic() {
        return music;
    }

    public Boolean getSfx() {
        return sfx;
    }

    /**
     * Handler fyrir takka sem sér um að kveikja á tónlist í leiknum.
     * @param actionEvent
     */
    public void enableMusicHandler(ActionEvent actionEvent) {
        CLICK.play();
        MENU.getMENUMUSIC().play();
        music = true;
        musicButton.setText("Disable music");
        musicButton.setOnAction(this::disableMusicHandler);
    }

    /**
     * Handler fyrir takka sem sér um að slökkva á tónlist í leiknum.
     * @param actionEvent
     */
    public void disableMusicHandler(ActionEvent actionEvent) {
        CLICK.play();
        MENU.getMENUMUSIC().stop();
        music = false;
        musicButton.setText("Enable music");
        musicButton.setOnAction(this::enableMusicHandler);
    }

    /**
     * Handler fyrir takk sem sér um að kveikja á sfx hljóðum í leiknum.
     * @param actionEvent
     */
    public void enableSfxHandler(ActionEvent actionEvent) {
        CLICK.play();
        sfx = true;
        sfxButton.setText("Disable sfx");
        sfxButton.setOnAction(this::disableSfxHandler);
    }

    /**
     * Handler fyrir takka sem sér um að slökkva á sfx hljóðum í leiknum.
     * @param actionEvent
     */
    public void disableSfxHandler(ActionEvent actionEvent) {
        CLICK.play();
        sfx = false;
        sfxButton.setText("Enable sfx");
        sfxButton.setOnAction(this::enableSfxHandler);
    }

    /**
     * Skiptir yfir í menu.
     */
    public void menuHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        ViewSwitcher.switchTo(View.MENU);
    }
}
