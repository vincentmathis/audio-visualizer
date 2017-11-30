package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MP3Player;
import view.DrawCanvas;

import java.io.File;

public class Controller {

    private Stage stage;
    private File lastFile;
    private MP3Player player;
    private DrawCanvas drawCanvas;
    @FXML
    private Canvas canvas;

    public Controller() {
        this.player = new MP3Player();
        this.drawCanvas = new DrawCanvas(player);
    }

    @FXML
    public void initialize() {
        // TODO scaling
        //drawCanvas.widthProperty().bind(drawCanvasPane.widthProperty());
        //drawCanvas.heightProperty().bind(drawCanvasPane.heightProperty());
        drawCanvas.setCanvas(canvas);
    }

    @FXML
    void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Audio File");
        if (lastFile != null) {
            fileChooser.setInitialDirectory(new File(lastFile.getParent()));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            lastFile = selectedFile;
            player.play(selectedFile.toString());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
