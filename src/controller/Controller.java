package controller;

import javafx.animation.Timeline;
import javafx.fxml.FXML;import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller implements AudioSpectrumListener {

    private final int BANDS = 256;
    private float[] buffer = new float[BANDS];
    private MediaPlayer audioPlayer;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;
    private File lastFile;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
    }

    @FXML
    void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Audio File");
        if(lastFile != null){
            fileChooser.setInitialDirectory(new File(lastFile.getParent()));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            lastFile = selectedFile;
            String audioURI = selectedFile.toURI().toString();
            Media media = new Media(audioURI);
            audioPlayer = new MediaPlayer(media);
            audioPlayer.setAudioSpectrumListener(this);
            audioPlayer.setAudioSpectrumInterval(0.02);
            audioPlayer.setAudioSpectrumNumBands(BANDS);
            audioPlayer.setCycleCount(Timeline.INDEFINITE);
            audioPlayer.play();
        }
    }


    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for (int i = 0; i < magnitudes.length; i++) {
            if(magnitudes[i] - audioPlayer.getAudioSpectrumThreshold() >= buffer[i]) {
                buffer[i] = magnitudes[i] - audioPlayer.getAudioSpectrumThreshold();
            } else {
                buffer[i] -= 0.25;
            }
        }
        drawShapes();
    }

    private void drawShapes() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0,0, width, height);
        gc.setFill(Color.web("#d59a63"));
        double barWidth = width / buffer.length;
        for (int i = 0; i < buffer.length; i++) {
            gc.fillRect(i*barWidth, height / 2 - buffer[i]*3, barWidth, buffer[i]*3*2);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
