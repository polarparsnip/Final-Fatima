package hi.verkefni.vidmot;

import hi.verkefni.vinnsla.Leikur;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import javafx.scene.media.AudioClip;

public class GameLevelThreeController extends GameController{
    @FXML
    private LeikbordLevelThree fxLeikbordThree;
    @FXML
    private Rectangle progressBar;

    Media song = new Media(new File("src/main/resources/hi/verkefni/vidmot/media/TITAN - 8 Bit (Chiptune) cover.mp3").toURI().toString());
    MediaPlayer music = new MediaPlayer(song);
    AudioClip FINALVICTORYSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/spiderman 8bit--victory.mp3").toURI().toString());
    private View CURRENTLEVEL = View.GAMELEVELTHREE;
    private View NEXTVIEW = View.LEVELCLEAREDSCREEN;
    private String LEVELFINISHEDTEXT = "Fatima has defeated the Pepsi-man!!! How great is that??\n" +
            "after Pepsi-man was defeated, the gravity came back to\n" +
            "the city and all of the buildings came crashing down,\n" +
            "solving the overpopulation problem along with the no \n" +
            "gravity problem!\n" +
            "\n" +
            "Now she can give back the gun to the police officers,\n" +
            "she is renting a smaller apartment and is planning to start\n" +
            "working in the Pepsi fctory near her house to make sure \n" +
            "something like this never happens again!";

    private Leikur leikur;

    private Timeline t; // Tímalína fyrir Animation á leiknum

    public static final int INTERVAL = 50;

    public Region content;

    DoubleProperty xPosition = new SimpleDoubleProperty(0);

    private Timeline timeline;

    KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    EventHandler<KeyEvent> anyKey = (event) -> {
        try {
            if (String.valueOf(event.getCode()).equals("UP")) {
                fxLeikbordThree.getHero().fljuga();
            }
            if (String.valueOf(event.getCode()).equals("S")) {
                fxLeikbordThree.shoot();
            }

            if (keyCodeCombination.match(event)) {
                if(leikur.counterProperty().getValue() > 650) {
                    fxLeikbordThree.adminKill();
                }
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
                    fxLeikbordThree.aframByggingar();
                    fxLeikbordThree.afram();
                    statusBar(625, 0.5056);
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
        t.stop();
        if(settings == null || settings.getMusic()) {
            GAMEOVERSOUND.play();
        }
        fxLeikbordThree.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
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
            FINALVICTORYSOUND.play();
        }
        t.stop();
        ViewSwitcher.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        Levels.enableLevelThreeButton();
        ViewSwitcher.switchTo(NEXTVIEW);

        LevelCleared sc = (LevelCleared) ViewSwitcher.lookup(NEXTVIEW);
        sc.LevelOneCleared(LEVELFINISHEDTEXT);
        sc.setClearedText("Victory");
        sc.ContinueButtonVisibility(false);
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
        fxLeikbordThree.nyrLeikur();
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
        FINALVICTORYSOUND.stop();
        t.stop();
        timeline.stop();
        BUTTONSELECT.play();
        fxLeikbordThree.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        ViewSwitcher.switchTo(View.MENU);
        if(settings == null || settings.getMusic()) {
            MENU.getMENUMUSIC().play();
        }
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
     * Getter fyrir FINALVICTORYSOUND, svo hægt sé að hafa áhrif á hana utan frá.
     * @return victory tónlist fyrir sigur
     */
    public AudioClip getFINALVICTORYSOUND() {
        return FINALVICTORYSOUND;
    }

    /**
     * Upphafsstillir vinnsluna, setur upp controls og animation
     * og lætur leiksvæðið vita hvaða controller hann á að hafa samband við
     */
    public void initialize() {
        music.setVolume(music.getVolume() -0.7);
        xPosition.addListener((observable, oldValue, newValue) -> setBackgroundPositions(fxLeikbordThree, xPosition.get()));
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(xPosition, 0)),
                new KeyFrame(Duration.seconds(200), new KeyValue(xPosition, -15000))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        leikur = new Leikur();
        fxLeikbordThree.setController(this);
        hefjaLeik();
    }
}
