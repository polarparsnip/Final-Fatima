package hi.verkefni.vidmot;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static javafx.scene.shape.StrokeType.INSIDE;

public class LeikbordLevelThree extends Pane implements Leikhlutur {
    @FXML
    private Hero hero;
    @FXML
    private Bygging b1, b2, b3;

    SettingsController settings = (SettingsController) ViewSwitcher.lookup(View.SETTINGS);

    private ArrayList<Node> shots = new ArrayList<Node>();

    private boolean currentlyShooting = false;

    private boolean safety = true;

    ObservableList<Bygging> fxByggingar = FXCollections.observableArrayList();
    private GameController sc;
    private int BUILDINGBASEOFFSET;
    private int VILlAINLIFETOTAL = 20;
    private int VILLAINLIFE = VILlAINLIFETOTAL;
    AudioClip SHOOTSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/12-Gauge-Pump-Action-Shotgun-Far-Gunshot-A-www.fesliyanstudios.com.mp3").toURI().toString());
    AudioClip VILLAINLAUGHSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/Evil Laugh Deep Voice Sound Effect.mp3").toURI().toString());
    AudioClip VILLAINSHOTIMPACT = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/Bullet Hit Sound Effect.mp3").toURI().toString());
    AudioClip VILLAINEXPLOSIONSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/explosion.mp3").toURI().toString());
    AudioClip LASERSOUND = new AudioClip(new File("src/main/resources/hi/verkefni/vidmot/media/Ultra Heavy Laser sound.mp3").toURI().toString());
    private int villainMovement;
    URL VILLAINIMGURL = GameController.class.getResource("myndir/pepsi-man.png");
    Image VILLAIN = new Image(VILLAINIMGURL.toString());
    ImageView VILLAINMAN = new ImageView(VILLAIN);
    URL GUNIMGURL = GameController.class.getResource("myndir/gun.png");
    Image GUN = new Image(GUNIMGURL.toString());
    ImageView HEROGUN = new ImageView(GUN);
    URL BULLETIMGURL = GameController.class.getResource("myndir/bullet.png");
    Image BULLET = new Image(BULLETIMGURL.toString());
    URL EXPLOSIONIMGURL = GameController.class.getResource("myndir/starburst-explosion.png");
    Image EXPLOSION = new Image(EXPLOSIONIMGURL.toString());
    URL GUNIMPACTIMGURL = GameController.class.getResource("myndir/gun-impact.png");
    Image GUNIMPACTIMAGE = new Image(GUNIMPACTIMGURL.toString());
    URL MUZZLEIMGURL = GameController.class.getResource("myndir/muzzle.png");
    Image MUZZLE = new Image(MUZZLEIMGURL.toString());
    ImageView MUZZLEFIRE = new ImageView(MUZZLE);
    Rectangle VILLAINHEALTHBAR = new Rectangle();
    private boolean RELOADING = false;
    Node villainNode;
    Node gunNode;
    Node muzzleNode;
    private boolean adminKillSafety;

    URL LASERIMGURL = GameController.class.getResource("myndir/laser.png");
    Image LASER = new Image(LASERIMGURL.toString());
    ImageView LASERFIRE = new ImageView(LASER);
    Node laserNode;
    private boolean laserShooting = false;
    private boolean laserActive = false;

    public LeikbordLevelThree() {
        lesa("leikbord-level-three-view.fxml");
        b1.setRotate(90);
        fxByggingar.add(b1);

        VILLAINMAN.setFitWidth(80);
        VILLAINMAN.setFitHeight(220);

        VILLAINLAUGHSOUND.setVolume(VILLAINLAUGHSOUND.getVolume() + 0.7);

        VILLAINHEALTHBAR.setHeight(20);
        VILLAINHEALTHBAR.setFill(Paint.valueOf("green"));
        VILLAINHEALTHBAR.setStroke(Paint.valueOf("white"));
        VILLAINHEALTHBAR.setStrokeWidth(2);
        VILLAINHEALTHBAR.setStrokeType(INSIDE);

        HEROGUN.setFitWidth(120);
        HEROGUN.setFitHeight(40);
        gunNode = HEROGUN;
        getChildren().add(gunNode);

        MUZZLEFIRE.setFitWidth(200); //20
        MUZZLEFIRE.setFitHeight(100); //10
        muzzleNode = MUZZLEFIRE;

        LASERFIRE.setFitHeight(20);
        LASERFIRE.setFitWidth(600);
        laserNode = LASERFIRE;

        SHOOTSOUND.setVolume(SHOOTSOUND.getVolume() -0.80);
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
     * Getter fyrir hero á leikborði.
     */
    public Hero getHero() {
        return hero;
    }

    private void gameOver(Boolean lost) {
        VILLAINLAUGHSOUND.stop();
        getChildren().remove(VILLAINHEALTHBAR);

        if(villainNode != null) {
            getChildren().remove(villainNode);
            villainNode = null;
        }
        getChildren().remove(muzzleNode);

        for (Node shot : shots) {
            getChildren().remove(shot);
        }
        shots.clear();

        VILLAINLIFE = VILlAINLIFETOTAL;
        if(lost) {
            sc.leikLokid();
        } else {
            sc.leikKlarad();
        }

        if (laserNode != null) {
            getChildren().remove(laserNode);
            LASERSOUND.stop();
            laserShooting = false;
            laserActive = false;
        }

    }

    /**
     * Athugar hvort hero er í snertingu við byggingu b, og ef svo þá er
     * kallað á fall til að setja hann á bygginguna, svo er hann færður
     * með bygginguna þangað til hún er tekin af honum.
     * Ef hero er ekki í snertingu við byggingu sem hún var á, þá er
     * kallað á fall til að taka hana af byggingu.
     * @param b
     */
    private void athugaHeroAByggingu(Bygging b) {
        if(hero.getBoundsInParent().intersects(b.getBoundsInParent())) {
            gameOver(true);
        }
    }

    /**
     * Athugar hvort hero sé á botni leikborðs, ef svo þá er kallað
     * leiklokid fall til að enda leik, svo skilað true, annars false.
     */
    public boolean isHeroOnEdge() {
        if (hero.getLayoutY() >= this.getHeight()-hero.getFitWidth() || hero.getLayoutY() <= 0) {
            gameOver(true);
            return true;
        }
        return false;
    }

    /**
     * Lætur hero færast niður eins lengi og hún er ekki á byggingu og ekki á botni.
     * Svo ef ákveðnum tíma er náð (800) þá er búið að vinna þetta borð og leikmaður
     * færist áfram í leiknum.
     */
    public void afram() {
        if(sc.getLeikur().counterProperty().getValue() == 600) {
            villainNode = VILLAINMAN;
            villainNode.setLayoutX(700);
            villainNode.setLayoutY(200);
            getChildren().add(villainNode);

            VILLAINHEALTHBAR.setLayoutX(700);
            VILLAINHEALTHBAR.setLayoutY(40);
            getChildren().add(VILLAINHEALTHBAR);

        }
        if(sc.getLeikur().counterProperty().getValue() == 630) {
            if(settings == null || settings.getSfx()) {
                VILLAINLAUGHSOUND.play();
            }
            BUILDINGBASEOFFSET += 6;
        }
        if(sc.getLeikur().counterProperty().getValue() > 600 && sc.getLeikur().counterProperty().getValue() < 650 && villainNode != null) {
            villainNode.relocate(villainNode.getLayoutX()-5, villainNode.getLayoutY());
            VILLAINHEALTHBAR.relocate(VILLAINHEALTHBAR.getLayoutX()-6, VILLAINHEALTHBAR.getLayoutY());
        }
        if(sc.getLeikur().counterProperty().getValue() > 640 && villainNode != null) {
            if(villainMovement <= 50) {
                villainNode.relocate(villainNode.getLayoutX(),villainNode.getLayoutY()-1);
                villainMovement++;
            } else if (villainMovement > 50 && villainMovement <= 100){
                villainNode.relocate(villainNode.getLayoutX(),villainNode.getLayoutY()+1);
                villainMovement++;
            } else {
                villainMovement = 0;
            }
        }

        if (!isHeroOnEdge()) {
            hero.afram();
            gunNode.relocate(hero.getLayoutX()-25, hero.getLayoutY()+25);
            muzzleNode.relocate(hero.getLayoutX()+hero.getBoundsInLocal().getWidth()+35, hero.getLayoutY()-20);
        }
        shooting();

        if(villainNode != null) {
            checkHit();
        }

        if(this.villainNode != null && sc.getLeikur().counterProperty().getValue() >= 750 && sc.getLeikur().counterProperty().getValue() % 250 == 0) {
            if(settings == null || settings.getSfx()) {
                VILLAINLAUGHSOUND.play();
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                laserShoot();
            });
            pause.play();

        }

        if(laserActive) {
            laserShooting();
        }
    }


    public void laserShoot() {
        if(settings == null || settings.getSfx()) {
            LASERSOUND.play();
        }
        laserNode.relocate(villainNode.getLayoutX() - villainNode.getBoundsInLocal().getWidth() -472, villainNode.getLayoutY()+11);
        getChildren().add(laserNode);
        laserActive = true;

        PauseTransition p = new PauseTransition(Duration.millis(50));
        p.setOnFinished(event -> {
            laserShooting = true;
        });
        p.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            getChildren().remove(laserNode);
            LASERSOUND.stop();
            laserShooting = false;
            laserActive = false;
        });
        pause.play();
    }


    public void laserShooting() {
        if(villainNode != null) {
            laserNode.relocate(villainNode.getLayoutX() - villainNode.getBoundsInLocal().getWidth() -472, villainNode.getLayoutY()+11);
        }
        if(laserShooting && hero.getBoundsInParent().intersects(laserNode.getBoundsInParent())) {
            gameOver(true);
        }
    }

    /**
     * Færir allar byggingar áfram um offset mikla lengd og athugar alltaf hvort hero
     * sé í snertingu. Offset á byggingu er svo hækkað um 1 í hverju stökki af 200.
     */
    public void aframByggingar() {
        for (Bygging B : fxByggingar) {
            B.setOFFSET(sc.getLeikur().counterProperty().getValue() / 200 + BUILDINGBASEOFFSET);
            //B.setRotate(B.getRotate()+BUILDINGBASEOFFSET-2);
            athugaHeroAByggingu(B);

            if (B.getLayoutX() <= -B.getFitHeight()) {
                int y = (int) (Math.random() * (this.getHeight() - B.getFitWidth()));
                B.relocate(this.getWidth()+B.getFitWidth(), y-30);
            }
            B.levelThreeAfram();
        }
    }

    /**
     * Byrjar nýjan leik með því að setja byggingar á staðsetnignar af handahófi
     * og hero efst á skjá.
     */
    public void nyrLeikur() {
        int place = 0;
        for (Bygging B : fxByggingar) {
            B.setFitHeight(200);
            int y = (int) (Math.random() * (455 - B.getFitHeight()));
            B.relocate(place+1200, y);
            place += 200;
        }
        hero.relocate(100, 5);
        VILLAINHEALTHBAR.setWidth(150);
        villainMovement = 0;

        //BUILDINGBASEOFFSET = 4;
        BUILDINGBASEOFFSET = 20;

        adminKillSafety = true;
    }

    public void shoot() {
        if(!RELOADING) {
            if(settings == null || settings.getSfx()) {
                SHOOTSOUND.play();
            }
            ImageView bullet = new ImageView(BULLET);
            bullet.setFitHeight(20); //5
            bullet.setFitWidth(40); //10
            Node bulletNode = bullet;
            bulletNode.relocate(hero.getLayoutX()+hero.getBoundsInLocal().getWidth()+35, hero.getLayoutY()+25);
            shots.add(bulletNode);
            getChildren().add(bulletNode);

            getChildren().add(muzzleNode);


            PauseTransition muzzlePause = new PauseTransition(Duration.millis(50));
            muzzlePause.setOnFinished(event -> {
                getChildren().remove(muzzleNode);
            });
            muzzlePause.play();

            RELOADING = true;
            PauseTransition reloadPause = new PauseTransition(Duration.seconds(1));
            reloadPause.setOnFinished(event -> {
                RELOADING = false;
            });
            reloadPause.play();
        }
    }

    private void shooting() {
        for(int i = 0; i < shots.size(); i++) {
            if(shots.get(i).getTranslateX() < 390) {
                shots.get(i).setTranslateX(shots.get(i).getTranslateX() + 10);
            } else {
                getChildren().remove(shots.get(i));
                shots.remove(i);
            }
        }
    }

    private void checkHit() {
        for(int i = 0; i < shots.size(); i++) {
            if(shots.get(i).getBoundsInParent().intersects(villainNode.getBoundsInParent())) {
                if(settings == null || settings.getSfx()) {
                    VILLAINSHOTIMPACT.play();
                }
                ImageView GUNIMPACT = new ImageView(GUNIMPACTIMAGE);
                GUNIMPACT.setFitHeight(60);
                GUNIMPACT.setFitWidth(60);
                GUNIMPACT.relocate(450, shots.get(i).getLayoutY()-20);
                getChildren().add(GUNIMPACT);

                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(event -> {
                    getChildren().remove(GUNIMPACT);
                });
                pause.play();

                getChildren().remove(shots.get(i));
                shots.remove(i);
                VILLAINLIFE--;
                VILLAINHEALTHBAR.setWidth(VILLAINHEALTHBAR.getWidth()-7.5);
                VILLAINHEALTHBAR.setLayoutX(VILLAINHEALTHBAR.getLayoutX()+7.5);
            }

            if(VILLAINLIFE == 0 && safety) {
                villainDefeat();
            }
        }
    }

    private void villainDefeat() {
        if(settings == null || settings.getSfx()) {
            VILLAINEXPLOSIONSOUND.play();
        }
        ImageView explosion = new ImageView(EXPLOSION);
        explosion.setFitHeight(200);
        explosion.setFitWidth(200);
        Node explosionNode = explosion;
        explosionNode.relocate(villainNode.getLayoutX()-50, villainNode.getLayoutY()-20);
        getChildren().add(explosionNode);
        sc.getT().stop();
        sc.getTimeline().stop();
        PauseTransition pause = new PauseTransition(Duration.seconds(1.3));
        pause.setOnFinished(event -> {
            gameOver(false);
            VILLAINEXPLOSIONSOUND.stop();
            getChildren().remove(explosionNode);
        });
        pause.play();
        safety = false;
        //gameOver(false);
    }

    public void adminKill() {
        if(adminKillSafety) {
            VILLAINHEALTHBAR.setWidth(0);
            villainDefeat();
            System.out.println("Admin kill");
            adminKillSafety = false;
        }

    }

    /**
     * Setur controller fyrir leikborð sem sc
     * @param sc
     */
    public void setController(GameController sc) {
        this.sc = sc;
    }
}