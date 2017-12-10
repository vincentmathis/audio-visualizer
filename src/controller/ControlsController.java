package controller;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.MP3Player;

import java.io.File;
import java.util.Observer;

public class ControlsController implements Observer {

    private MP3Player player;
    private File lastFile;

    @FXML
    private Pane bottomPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider spacingSlider;
    @FXML
    private CheckBox slowCheckBox;

    public void initPlayer(MP3Player player) {
        if (this.player != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.player = player;
        this.player.addObserver(this);
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
        File selectedFile = fileChooser.showOpenDialog(bottomPane.getScene().getWindow());
        if (selectedFile != null) {
            lastFile = selectedFile;
            player.play(selectedFile.toString());
        }
    }

    @FXML
    public void initialize() {
        spacingSlider.valueProperty().addListener((observable, oldValue, newValue) -> player.setSteps(newValue.intValue()));
        progressBar.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Bounds bounds = progressBar.getLayoutBounds();
                double mouseX = event.getX();
                double percent = (mouseX) / (bounds.getMaxX() - bounds.getMinX());
                player.setPosition(percent);
            }
        });
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
}
