package hi.verkefni.vidmot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class LeikbordLevelTwo extends Pane implements Leikhlutur {
    @FXML
    private Hero hero;
    @FXML
    private Bygging b1, b2, b3;

    ObservableList<Bygging> fxByggingar = FXCollections.observableArrayList();
    private GameController sc;

    URL imgURL = GameController.class.getResource("myndir/t_skyscraper.png");
    Image BUILDING = new Image(imgURL.toString());

    ImageView goal;

    URL GUNIMGURL = GameController.class.getResource("myndir/gun.png");
    Image GUN = new Image(GUNIMGURL.toString());
    ImageView HEROGUN = new ImageView(GUN);

    Node gunNode;

    public LeikbordLevelTwo() {
        lesa("leikbord-level-two-view.fxml");
        fxByggingar.add(b1);
        fxByggingar.add(b2);
        fxByggingar.add(b3);

        HEROGUN.setFitWidth(120);
        HEROGUN.setFitHeight(40);
        gunNode = HEROGUN;
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
        getChildren().remove(goal);
        getChildren().remove(gunNode);
        if(lost) {
            sc.leikLokid();
        } else {
            sc.leikKlarad();
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
            goal = new ImageView(BUILDING);
            goal.setFitHeight(270);
            goal.setFitWidth(60);
            goal.setLayoutX(700);
            goal.setLayoutY(200);
            getChildren().add(goal);

            gunNode.setLayoutX(goal.getLayoutX()-25);
            gunNode.setLayoutY(goal.getLayoutY()-30);
            getChildren().add(gunNode);
        }
        if(sc.getLeikur().counterProperty().getValue() > 600) {
            goal.relocate(goal.getLayoutX()-5, goal.getLayoutY());
            gunNode.setLayoutX(goal.getLayoutX()-25);
            gunNode.setLayoutY(goal.getLayoutY()-30);

            if (hero.getBoundsInParent().intersects(goal.getBoundsInParent())) {
                gameOver(false);
            }
            if (goal.getLayoutX() < -goal.getFitWidth()) {
                gameOver(true);
            }
        }
        if (!isHeroOnEdge()) {
            hero.afram();
        }
    }

    /**
     * Færir allar byggingar áfram um offset mikla lengd og athugar alltaf hvort hero
     * sé í snertingu. Offset á byggingu er svo hækkað um 1 í hverju stökki af 200.
     */
    public void aframByggingar() {
        for (Bygging B : fxByggingar) {
            B.setOFFSET(sc.getLeikur().counterProperty().getValue() / 200 + 4);

            athugaHeroAByggingu(B);
            if (sc.getLeikur().counterProperty().getValue() <= 600) {
                if (B.getLayoutX() <= 0-B.getFitWidth()) {
                    int y = (int) (Math.random() * (this.getHeight() - B.getFitHeight()));
                    B.relocate(this.getWidth(), y);
                }

                if (B.getLayoutY() <= -B.getFitHeight()) {
                    int z = (int) (Math.random() * (this.getWidth() - B.getFitWidth()));
                    int q = (int) (Math.random() * (this.getHeight() - B.getFitHeight()));

                    B.relocate(B.getLayoutX(), this.getHeight());

                }
            }


            B.levelTwoAfram();

        }
    }

    /**
     * Byrjar nýjan leik með því að setja byggingar á staðsetnignar af handahófi
     * og hero efst á skjá.
     */
    public void nyrLeikur() {
        int place = 0;
        for (Bygging B : fxByggingar) {
            B.setFitHeight(170);
            int x = (int) (Math.random() * (600 - B.getFitWidth()));
            int y = (int) (Math.random() * (455 - B.getFitHeight()));
            B.relocate(place+600, y);
            place += 210;
        }
        hero.relocate(100, 5);
    }

    /**
     * Setur controller fyrir leikborð sem sc
     * @param sc
     */
    public void setController(GameController sc) {
        this.sc = sc;
    }
}