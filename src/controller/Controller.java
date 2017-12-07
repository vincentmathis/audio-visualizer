package controller;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MP3Player;
import view.DrawCanvas;

import java.io.File;
import java.util.Observer;

public class Controller implements Observer {

    private final MP3Player player;
    private DrawCanvas drawCanvas;
    private Stage stage;
    private File lastFile;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox slowCheckBox;
    @FXML
    private Slider spacingSlider;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Pane canvasPane;


    public Controller() {
        this.player = new MP3Player();
    }

    @FXML
    public void initialize() {
        this.drawCanvas = new DrawCanvas(canvas);
        this.player.addObserver(drawCanvas);
        this.player.addObserver(this);
        // TODO only scales one direction
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
        spacingSlider.valueProperty().addListener((observable, oldValue, newValue) -> player.setSteps(newValue.intValue()));

        progressBar.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                Bounds bounds = progressBar.getLayoutBounds();
                double mouseX = event.getX();
                double percent = (mouseX) / (bounds.getMaxX() - bounds.getMinX());
                player.setPosition(percent);
            }
        });
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

    @Override
    public void update(java.util.Observable o, Object arg) {
        if (arg instanceof Double) {
            progressBar.setProgress((double) arg);
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
