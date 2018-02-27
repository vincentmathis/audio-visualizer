package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.MP3Player;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ControlsController implements Observer {

    private MP3Player player;
    private VisualizerController vc;
    private File lastFile;

    @FXML
    private Pane bottomPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider circleToggle;
    @FXML
    private Slider barsToggle;


    public void initPlayer(MP3Player player) {
        if (this.player != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.player = player;
        this.player.addObserver(this);
    }

    public void initVisualizer(VisualizerController vc) {
        if (this.vc != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.vc = vc;
    }

    @FXML
    void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MP3 File");
        if (lastFile != null) {
            fileChooser.setInitialDirectory(new File(lastFile.getParent()));
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(bottomPane.getScene().getWindow());
        if (selectedFile != null) {
            lastFile = selectedFile;
            player.play(selectedFile.toString());
        }
    }

    @FXML
    public void initialize() {
        circleToggle.valueProperty().addListener(((observable, oldValue, newValue) -> vc.setCircle(newValue.intValue() == 0)));
        barsToggle.valueProperty().addListener(((observable, oldValue, newValue) -> vc.setBars(newValue.intValue() == 0)));
        progressBar.setOnMouseClicked(event -> player.setPosition(event.getX() / progressBar.getWidth()));
    }

    @Override
    public void update(Observable o, Object arg) {
        progressBar.setProgress(player.getProgess());
    }
}
