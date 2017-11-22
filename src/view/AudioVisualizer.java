package view;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;


public class AudioVisualizer extends Application implements AudioSpectrumListener {

    private final int WIDTH = 400;
    private final int HEIGHT = 300;
    private final int BANDS = 128;
    private float[] buffer = new float[BANDS];
    private static MediaPlayer audioPlayer;
    private GraphicsContext gc;
    private File lastFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Button openFile = new Button("Open");
        openFile.setOnAction(event -> openFile(stage));

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        BorderPane pane = new BorderPane();
        pane.setBottom(openFile);
        pane.setCenter(canvas);

        stage.setScene(new Scene(pane));
        stage.setTitle("Audio Visualizer");
        stage.show();
    }

    private void openFile(Stage stage) {
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
        drawShapes(gc, buffer);
    }

    private void drawShapes(GraphicsContext gc, float[] mags) {
        gc.clearRect(0,0, WIDTH, HEIGHT);
        gc.setFill(Color.BLACK);
        int width = WIDTH / mags.length;
        for (int i = 0; i < mags.length; i++) {
            gc.fillRect(i*width, HEIGHT / 2 - mags[i]*3, width, mags[i]*3*2);
        }
    }
}
