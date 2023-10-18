package hi.verkefni.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MenuController {
    @FXML
    public AnchorPane fxMenu;
    @FXML
    private TextField fxMenuText;

    private Media SONG = new Media(new File("src/main/resources/hi/verkefni/vidmot/media/Fatima 8-bit NES arrange.mp3").toURI().toString());
    private MediaPlayer MENUMUSIC = new MediaPlayer(SONG);
    private AudioClip BUTTONSELECT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/game-menu-select-sound-effect.mp3").toURI().toString());
    private AudioClip PROLOGUEMUSIC = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/spiderman-2002-main-title - 8bit--.mp3").toURI().toString());
    public static final int INTERVAL = 50;
    private Timeline timeline;
    private View NEXTVIEW = View.LEVELCLEAREDSCREEN;
    DoubleProperty xPosition = new SimpleDoubleProperty(0);
    private String LEVELPROLOGUETEXT = "Oh no, the city is in panic! skyscrapers are flying and destruction is everywhere. \n" +
            "Apparently a super soldier experiment funded by the pepsi factory has gone wrong. \n" +
            "They fed a man nothing but liters of pepsi everyday to give him superpowers and their \n" +
            "plan worked too well, now he has awakened god level abilities including anti gravity powers, \n" +
            "and he's making the city's skyscrapers rip from the ground and fly all over the place, and his \n" +
            "abilities are only growing stronger by the minute. One worker at the pepsi factory by the \n" +
            "name of Fatima has taken it upon herself to stop this newly awakened menace. She has built \n" +
            "a flying contraption that utilizes the lower state of gravity and pepsi-max to allow her to gain \n" +
            "flight. Using this she intends to acquire a weapon left behind by the helicopter men on one \n" +
            "of the floating buildings in the city. But before she can get to it she has to practice her flying.\n" +

            "Click \"continue\" to follow Fatima at the start of her fateful journey.\n" +
            "Use the UP-arrow key to fly-jump in order to avoid the obstacles.\n" +
            "The progress bar at the bottom always shows your distance from the current objective.";

    /**
     * Skiptir yfir í leik og byrjar leik þegar ýtt er á "Spila" takkann.
     */
    public void spilaHandler(ActionEvent actionEvent) {
        MENUMUSIC.stop();
        BUTTONSELECT.play();

        SettingsController settings = (SettingsController) ViewSwitcher.lookup(View.SETTINGS);

        if(settings == null || settings.getMusic()) {
            PROLOGUEMUSIC.setCycleCount(AudioClip.INDEFINITE);
            PROLOGUEMUSIC.play();
        }
        ViewSwitcher.switchTo(NEXTVIEW);
        LevelCleared sc = (LevelCleared) ViewSwitcher.lookup(NEXTVIEW);
        sc.setNextLevel(View.GAMELEVELONE);
        sc.LevelOneCleared(LEVELPROLOGUETEXT);
        sc.ContinueButtonVisibility(true);
        sc.setClearedText("Prologue");
    }

    /**
     * Skiptir yfir í level selection ef ýtt var á þann takka.
     */
    public void levelsHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        ViewSwitcher.switchTo(View.LEVELS);
    }

    /**
     * Skiptir yfir í leik og byrjar leik þegar ýtt er á "Spila" takkann.
     */
    public void settingsHandler(ActionEvent actionEvent) {
        BUTTONSELECT.play();
        ViewSwitcher.switchTo(View.SETTINGS);
    }

    /**
     * Lokar forriti ef notandi vill hætta.
     */
    public void hættaHandler(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Getter fyrir menu tónlist.
     * @return
     */
    public MediaPlayer getMENUMUSIC() {
        return MENUMUSIC;
    }

    /**
     * Getter fyrir byrjunar tónlist í leik.
     * @return
     */
    public AudioClip getPROLOGUEMUSIC() {
        return PROLOGUEMUSIC;
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
     * Getter fyrir animation timeline
     * @return timeline
     */
    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * Upphafsstillir vinnsluna, setur upp tónlist og animation.
     */
    public void initialize() {
        xPosition.addListener((observable, oldValue, newValue) -> setBackgroundPositions(fxMenu, xPosition.get()));
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(xPosition, 0)),
                new KeyFrame(Duration.seconds(200), new KeyValue(xPosition, -15000)),
                new KeyFrame(Duration.millis(INTERVAL), e -> {
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        MENUMUSIC.setVolume(MENUMUSIC.getVolume()-0.90);
        MENUMUSIC.setCycleCount(MediaPlayer.INDEFINITE);
        MENUMUSIC.play();
    }
}
