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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;

public class GameLevelTwoController extends GameController{
    @FXML
    private LeikbordLevelTwo fxLeikbordTwo;

    @FXML
    private Rectangle progressBar;

    Media song = new Media(new File("src/main/resources/hi/verkefni/vidmot/media/A_Bit_Of_Hope_-_David_Fesliyan.mp3").toURI().toString());
    MediaPlayer music = new MediaPlayer(song);

    private View CURRENTLEVEL = View.GAMELEVELTWO;
    private View NEXTVIEW = View.LEVELCLEAREDSCREEN;
    private String LEVELFINISHEDTEXT = "Fatima has managed to acquire the weapon she needs to\n" +
            "continue to her final and main objective, to confront and\n" +
            "defeat the dreaded villain Pepsiman, who only gets \n" +
            "more powerful by the second.\n" +
            "\n" +
            "If you wish to continue with Fatima on the final steps of \n" +
            "her journey to defeat Pepsiman, click  \"Continue\".\n" +
            "\n" +
            "In this next level you have the ability to shoot by using the S-key,\n" +
            "The goal is to reach Pepsi-man and then take him down with your new\n" +
            "weapon. But this time he has mastered his powers and is throwing\n" +
            "buildings right in your direction!";

    private Leikur leikur;

    private Timeline t; // Tímalína fyrir Animation á leiknum

    public static final int INTERVAL = 50;

    public Region content;

    DoubleProperty xPosition = new SimpleDoubleProperty(0);

    Timeline timeline;

    EventHandler<KeyEvent> anyKey = (event) -> {
        try {
            if (String.valueOf(event.getCode()).equals("UP")) {
                fxLeikbordTwo.getHero().fljuga();
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
                    fxLeikbordTwo.aframByggingar();
                    fxLeikbordTwo.afram();
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
        if(settings == null || settings.getMusic()) {
            GAMEOVERSOUND.play();
        }
        t.stop();
        fxLeikbordTwo.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
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
        fxLeikbordTwo.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        Levels.enableLevelThreeButton();
        ViewSwitcher.switchTo(NEXTVIEW);

        LevelCleared sc = (LevelCleared) ViewSwitcher.lookup(NEXTVIEW);
        sc.setNextLevel(View.GAMELEVELTHREE);
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
        fxLeikbordTwo.nyrLeikur();
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
        fxLeikbordTwo.getScene().removeEventFilter(KeyEvent.ANY, anyKey);
        ViewSwitcher.switchTo(View.MENU);
        if(settings == null || settings.getMusic()) {
            MENU.getMENUMUSIC().play();
        }
    }

    /**
     * Upphafsstillir vinnsluna, setur upp controls og animation
     * og lætur leiksvæðið vita hvaða controller hann á að hafa samband við
     */
    public void initialize() {
        music.setVolume(music.getVolume() -0.8);
        xPosition.addListener((observable, oldValue, newValue) -> setBackgroundPositions(fxLeikbordTwo, xPosition.get()));
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(xPosition, 0)),
                new KeyFrame(Duration.seconds(200), new KeyValue(xPosition, -15000))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        leikur = new Leikur();
        fxLeikbordTwo.setController(this);
        hefjaLeik();
    }
}