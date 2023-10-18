package hi.verkefni.vidmot;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum View {
    GAMELEVELONE("game-view.fxml"),
    MENU("menu-view.fxml"),
    SETTINGS("settings-view.fxml"),
    LEVELS("levels-view.fxml"),
    GAMEOVER("game-over-view.fxml"),
    LEVELCLEAREDSCREEN("level-cleared-view.fxml"),
    GAMELEVELTWO("game-level-two-view.fxml"),
    GAMELEVELTHREE("game-level-three-view.fxml");


    private final String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
