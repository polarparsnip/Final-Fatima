module hi.verkefni.hopverkefni {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens hi.verkefni.vidmot to javafx.fxml, javafx.media;
    exports hi.verkefni.vidmot;
}