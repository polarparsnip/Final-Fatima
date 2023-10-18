package hi.verkefni.vidmot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Leikbord extends Pane implements Leikhlutur {
    @FXML
    private Hero hero;
    @FXML
    private Bygging b1, b2, b3, b4, b5, b6;

    ObservableList<Bygging> fxByggingar = FXCollections.observableArrayList();
    private GameController sc;

    private boolean DUO = true;

    private int xxx;

    private int yyy;

    public Leikbord() {
        lesa("leikbord-view.fxml");
        fxByggingar.add(b1);
        fxByggingar.add(b2);
        fxByggingar.add(b3);
        fxByggingar.add(b4);

        fxByggingar.add(b5);
        fxByggingar.add(b6);

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
            sc.leikLokid();
        }
    }

    /**
     * Athugar hvort hero sé á botni leikborðs, ef svo þá er kallað
     * leiklokid fall til að enda leik, svo skilað true, annars false.
     */
    public boolean isHeroOnEdge() {
        if (hero.getLayoutY() >= this.getHeight()-hero.getFitWidth() || hero.getLayoutY() <= 0) {
            sc.leikLokid();
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
        if(sc.getLeikur().counterProperty().getValue() == 800) {
            sc.leikKlarad();
        }
        if (!isHeroOnEdge()) {
            hero.afram();
        }
    }

    /**
     * Færir allar byggingar áfram um offset mikla lengd og athugar alltaf hvort hero
     * sé í snertingu. Offset á byggingu er svo hækkað um 1 í hverju stökki af 160.
     */
    public void aframByggingar() {
        for (Bygging B : fxByggingar) {
            B.setOFFSET(sc.getLeikur().counterProperty().getValue() / 160 + 3);

            athugaHeroAByggingu(B);

            if (B.getLayoutX() <= 0) {

                if (DUO) {
                    xxx = (int) (Math.random() * (this.getWidth() - B.getFitWidth()));
                    yyy = (int) (Math.random() * (this.getHeight() - B.getFitHeight()));
                    DUO = false;
                    B.relocate(this.getWidth(), yyy-210);

                } else {
                    B.relocate(this.getWidth(), yyy+210);
                    DUO = true;
                }
            }
            B.afram();
        }
    }

    /**
     * Byrjar nýjan leik með því að setja byggingar á staðsetnignar af handahófi
     * og hero efst á skjá.
     */
    public void nyrLeikur() {
        int place = 0;
        boolean DUO = true;
        int yy = 0;
        for (Bygging B : fxByggingar) {
            if (DUO) {
                yy = (int) (Math.random() * (455 - B.getFitHeight()));
                DUO = false;
                B.relocate(place + 600, yy-210);
            } else {
                B.relocate(place + 600, yy+210);
                DUO = true;
                place += 210;
            }
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
