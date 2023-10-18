package hi.verkefni.vidmot;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.media.AudioClip;

import java.io.File;

public class GameOver {
    AudioClip BUTTONSELECT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-menu-select-sound-effect.mp3").toURI().toString());
    SettingsController settings = (SettingsController) ViewSwitcher.lookup(View.SETTINGS);
    MenuController MENU = (MenuController) ViewSwitcher.lookup(View.MENU);
    GameController gc = (GameController) ViewSwitcher.lookup(View.GAMELEVELONE);
    GameController gltc = (GameController) ViewSwitcher.lookup(View.GAMELEVELTWO);
    GameController glthc = (GameController) ViewSwitcher.lookup(View.GAMELEVELTHREE);

    private static View CURRENTLEVEL;

    /**
     * Skiptir yfir í leik og byrjar leik þegar ýtt er á "Spila" takkann.
     */
    public void spilaHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        MENU.getMENUMUSIC().stop();
        ViewSwitcher.switchTo(CURRENTLEVEL);
        GameController sc = (GameController) ViewSwitcher.lookup(CURRENTLEVEL);
        sc.nyrLeikur();
    }

    /**
     * Skiptir yfir í menu.
     */
    public void menuHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        if(gc != null) {
            gc.getGAMEOVERSOUND().stop();
        } else if (gltc != null) {
            gltc.getGAMEOVERSOUND().stop();
        } else if (glthc != null) {
            glthc.getGAMEOVERSOUND().stop();
        }
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
     * Setter fyrir CURRENTLEVEL, eða núverandi borð,
     * svo hægt sé að byrja aftur eftir tap.
     * @param view
     */
    public static void setCURRENTLEVEL(View view) {
        CURRENTLEVEL = view;
    }
}
