package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MP3Player;
import view.DrawCanvas;

import java.io.File;

public class Controller {

    private final MP3Player player;
    private final DrawCanvas drawCanvas;
    private Stage stage;
    private File lastFile;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox slowCheckBox;
    @FXML
    private Slider spacingSlider;

    public Controller() {
        this.player = new MP3Player();
        this.drawCanvas = new DrawCanvas();
        this.player.addObserver(drawCanvas);
    }

    @FXML
    public void initialize() {
        // TODO scaling
        //drawCanvas.widthProperty().bind(drawCanvasPane.widthProperty());
        //drawCanvas.heightProperty().bind(drawCanvasPane.heightProperty());
        drawCanvas.setCanvas(canvas);
        spacingSlider.valueProperty().addListener((observable, oldValue, newValue) -> player.setSteps(newValue.intValue()));
    }

    @FXML
    void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MP3 File");
        if (lastFile != null) {
            fileChooser.setInitialDirectory(new File(lastFile.getParent()));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            lastFile = selectedFile;
            player.play(selectedFile.toString());
        }
    }

    @FXML
    public void setSlowShrink() {
        player.setSlowShrink(slowCheckBox.isSelected());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
