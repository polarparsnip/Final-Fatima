package hi.verkefni.vidmot;

import hi.verkefni.vinnsla.Leikur;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;

public class GameController {
    @FXML
    private Leikbord fxLeikbord;
    @FXML
    private Rectangle progressBar;
    AudioClip BUTTONSELECT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-menu-select-sound-effect.mp3").toURI().toString());
    MenuController MENU = (MenuController) ViewSwitcher.lookup(View.MENU);

    private View CURRENTLEVEL = View.GAMELEVELONE;
    private View NEXTVIEW = View.LEVELCLEAREDSCREEN;
    private String LEVELFINISHEDTEXT = "Fatima has managed to fly by the lumbering \n" +
            "skyscrapers using her flying equipment without any harm. \n" +
            "\n" +
            "Thanks to this she has gained enough \n" +
            "confidence to continue to her next objective,\n" +
            "acquiring a weapon strong enough to use \n" +
            "against the villain terrifying the city.\n" +
            "\n" +
            "If you wish to continue with Fatima on her \n" +
            "journey, click  \"Continue\".\n" +
            "\n" +
            "The goal of the next level is to reach the building containing the weapon,\n" +
            "but due to Pepsi-man growing stronger, the buildings have started to move!";

    Media song = new Media(new File("src/main/resources/hi/verkefni/vidmot/media/Retro_Platforming.mp3").toURI().toString());
    MediaPlayer music = new MediaPlayer(song);

    AudioClip GAMEOVERSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-over-8bit.mp3").toURI().toString());
    AudioClip VICTORYSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/8-bit-victory-non-copyright-sound-effect.mp3").toURI().toString());

    SettingsController settings = (SettingsController) ViewSwitcher.lookup(View.SETTINGS);

    private Leikur leikur;

    private Timeline t; // Tímalína fyrir Animation á leiknum

    public static final int INTERVAL = 50;

    public Region content;

    DoubleProperty xPosition = new SimpleDoubleProperty(0);

    Timeline timeline;

    EventHandler<KeyEvent> anyKey = (event) -> {
        try {
            if (String.valueOf(event.getCode()).equals("UP")) {
                fxLeikbord.getHero().fljuga();
            }
        } catch (NullPointerException e) {
            event.consume();
        }
    };

    /**
     * Setur upp Animation fyrir leikinn og setur upp leikjalykkjuna.
     * Setur svo upp "Progress bar" til að sýna hversu mikið leikmaður er
     * búinn með af borðinu, og svo er counter settur í gang svo hægt er
     * að mæla hversu mikið leikmaður er búinn með af borði.
     */
    public void hefjaLeik() {
        KeyFrame k = new KeyFrame(Duration.millis(INTERVAL),
                e -> {
                    fxLeikbord.aframByggingar();
                    fxLeikbord.afram();
                    statusBar(800, 0.395);
                    leikur.haekkaCounter();
                });
        t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);   // leikurinn leikur endalaust
        music.setCycleCount(MediaPlayer.INDEFINITE); //tónlist spilar endalaust
    }

    /**
     * Hækkar progressbar til að sýna stöðu í leik
     */
    private void statusBar(double goal, double increment) {
        if(leikur.counterProperty().getValue() < goal) {
            progressBar.setWidth(progressBar.getWidth()+increment);
        }
    }

    /**
     * Leik er lokið.
     * Tónlist er stoppuð og tímalina stoppuð.
     * Svo er fjarlægður event filter og skipt yfir í game over senu.
     */
    public void leikLokid() {
        music.stop();
        if(settings == null || settings.getMusic()) {
            GAMEOVERSOUND.play();
        }
        t.stop();
        fxLeikbord.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        GameOver.setCURRENTLEVEL(CURRENTLEVEL);
        ViewSwitcher.switchTo(View.GAMEOVER);
    }

    /**
     * Borð var klárað.
     * Tónlist er stoppuð og tímalina stoppuð.
     * Svo er fjarlægður event filter og skipt yfir í næstu senu.
     */
    public void leikKlarad() {
        music.stop();
        if(settings == null || settings.getMusic()) {
            VICTORYSOUND.play();
        }
        t.stop();
        fxLeikbord.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        Levels.enableLevelTwoButton();
        ViewSwitcher.switchTo(NEXTVIEW);

        LevelCleared sc = (LevelCleared) ViewSwitcher.lookup(NEXTVIEW);
        sc.setNextLevel(View.GAMELEVELTWO);
        sc.LevelOneCleared(LEVELFINISHEDTEXT);
        sc.setClearedText("Level cleared");
        sc.ContinueButtonVisibility(true);
    }

    /**
     * Stillir upp nýjum leik og byrjar hann
     */
    public void nyrLeikur() {
        GAMEOVERSOUND.stop();
        VICTORYSOUND.stop();
        progressBar.setWidth(0);
        leikur.leikLokid();
        ViewSwitcher.getScene().addEventFilter(KeyEvent.ANY, anyKey);
        fxLeikbord.nyrLeikur();
        timeline.play();
        t.play();
        if(settings == null || settings.getMusic()) {
            music.play();
        }
    }

    /**
     * getter fyrir leik svo hægt sé að vinna með stigin
     */
    public Leikur getLeikur() {
        return leikur;
    }

    /**
     * Return takki sem skiptir um view aftur yfir í menu
     */
    @FXML
    protected void menuHandler() {
        music.stop();
        t.stop();
        timeline.stop();
        BUTTONSELECT.play();
        fxLeikbord.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        ViewSwitcher.switchTo(View.MENU);
        if(settings == null || settings.getMusic()) {
            MENU.getMENUMUSIC().play();
        }
    }

    /**
     * Færir til bakgrunnsmyndir til að búa til bakgrunns hreyfimyndir.
     */
    public void setBackgroundPositions(Region region, double xPosition) {
        String style = "-fx-background-position: " +
                "left " + xPosition/5 + "px bottom," +
                "left " + xPosition/4 + "px bottom," +
                "left " + xPosition/3 + "px bottom," +
                "left " + xPosition/2 + "px bottom," +
                "left " + xPosition/1 + "px bottom;";
        region.setStyle(style);
    }

    /**
     * Getter fyrir VICTORYSOUND.
     * @return victory tónlist fyrir sigur
     */
    public AudioClip getVICTORYSOUND() {
        return VICTORYSOUND;
    }

    /**
     * Getter fyrir GAMEOVERSOUND.
     * @return game over tónlist fyrir tap
     */
    public AudioClip getGAMEOVERSOUND() {
        return GAMEOVERSOUND;
    }

    /**
     * Getter fyrir tímalínu t, svo hægt sé að hafa áhrif á hana utan frá.
     * @return tímalína t
     */
    public Timeline getT() {
        return t;
    }

    /**
     * Getter fyrir tímalínu timeline, svo hægt sé að hafa áhrif á hana utan frá.
     * @return tímalína timeline
     */
    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * Upphafsstillir vinnsluna, setur upp controls og animation
     * og lætur leiksvæðið vita hvaða controller hann á að hafa samband við
     */
    public void initialize() {
        music.setVolume(music.getVolume() -0.7);
        xPosition.addListener((observable, oldValue, newValue) -> setBackgroundPositions(fxLeikbord, xPosition.get()));
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(xPosition, 0)),
                new KeyFrame(Duration.seconds(200), new KeyValue(xPosition, -15000))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        leikur = new Leikur();
        fxLeikbord.setController(this);
        hefjaLeik();
    }
}