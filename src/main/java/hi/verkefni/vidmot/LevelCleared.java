package hi.verkefni.vidmot;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;

import java.io.File;

public class LevelCleared {
    @FXML
    public TextField clearedText;
    @FXML
    public Button continueButton;
    @FXML
    private TextArea storyText;
    MenuController MENU = (MenuController) ViewSwitcher.lookup(View.MENU);
    SettingsController settings = (SettingsController) ViewSwitcher.lookup(View.SETTINGS);
    GameController gc = (GameController) ViewSwitcher.lookup(View.GAMELEVELONE);
    GameLevelThreeController gltc = (GameLevelThreeController) ViewSwitcher.lookup(View.GAMELEVELTHREE);
    AudioClip BUTTONSELECT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-menu-select-sound-effect.mp3").toURI().toString());

    private View NEXTLEVEL = View.GAMELEVELONE;

    /**
     * Skiptir yfir í leik og byrjar leik þegar ýtt er á "Spila" takkann.
     */
    public void spilaHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        MENU.getPROLOGUEMUSIC().stop();
        MENU.getMENUMUSIC().stop();
        ViewSwitcher.switchTo(NEXTLEVEL);
        if(NEXTLEVEL == View.GAMELEVELONE) {
            GameController sc = (GameController) ViewSwitcher.lookup(NEXTLEVEL);
            sc.nyrLeikur();
        } else if(NEXTLEVEL == View.GAMELEVELTWO){
            GameLevelTwoController sc = (GameLevelTwoController) ViewSwitcher.lookup(NEXTLEVEL);
            sc.nyrLeikur();
        } else if(NEXTLEVEL == View.GAMELEVELTHREE){
            GameLevelThreeController sc = (GameLevelThreeController) ViewSwitcher.lookup(NEXTLEVEL);
            sc.nyrLeikur();
        }


    }

    /**
     * Skiptir yfir í menu ef ýtt var á þann takka.
     */
    public void menuHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        if (gc != null) {
            gc.getVICTORYSOUND().stop();
        }
        if (gltc != null) {
            gltc.getFINALVICTORYSOUND().stop();
        }
        MENU.getPROLOGUEMUSIC().stop();
        if(settings == null || settings.getMusic()) {
            MENU.getMENUMUSIC().play();
        }
        ViewSwitcher.switchTo(View.MENU);
    }

    /**
     * Lokar forriti ef notandi vill hætta.
     */
    public void hættaHandler(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Setter fyrir næsta level svo hægt sé að halda afrám milli borða.
     * @param nextLevel
     */
    public void setNextLevel(View nextLevel) {
        NEXTLEVEL = nextLevel;
    }

    /**
     * Setur texta fyrir söguna eftir að fyrsta borðið er klárað.
     * @param text
     */
    public void LevelOneCleared(String text) {
        storyText.setText(text);
    }

    /**
     * Setter fyrir header texta.
     * @param text
     */
    public void setClearedText(String text) {
        clearedText.setText(text);
    }

    /**
     * Stjórnar sýnileika á halda áfram takka.
     * @param visibility
     */
    public void ContinueButtonVisibility(Boolean visibility) {
        continueButton.setVisible(visibility);
    }
}