package controller;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.DrawCanvas;

import java.io.File;

public class Controller {

    public static final int BANDS = 256;
    @FXML
    AnchorPane canvasPane;

    private MediaPlayer audioPlayer;
    @FXML
    private DrawCanvas drawCanvas;
    private GraphicsContext gc;
    private Stage stage;
    private File lastFile;

    @FXML
    public void initialize() {
        drawCanvas = new DrawCanvas();
        // TODO scaling
//        drawCanvas.widthProperty().bind(drawCanvasPane.widthProperty());
//        drawCanvas.heightProperty().bind(drawCanvasPane.heightProperty());
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
            if (audioPlayer != null) {
                if (audioPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                    audioPlayer.stop();
                }
            }
            lastFile = selectedFile;
            String audioURI = selectedFile.toURI().toString();
            Media media = new Media(audioURI);
            audioPlayer = new MediaPlayer(media);
            audioPlayer.setAudioSpectrumListener(drawCanvas);
            audioPlayer.setAudioSpectrumInterval(0.02);
            audioPlayer.setAudioSpectrumNumBands(BANDS);
            audioPlayer.setCycleCount(Timeline.INDEFINITE);
            drawCanvas.setAudioPlayer(audioPlayer);
            audioPlayer.play();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
