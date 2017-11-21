package view;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;


public class AudioVisualizer extends Application implements AudioSpectrumListener {

    private static MediaPlayer audioPlayer;
    // TODO get URI of mp3
    //private static final String AUDIO_URI = new File("mpthreetest.mp3").toURI().toString();
    private final String AUDIO_URI = System.getProperty("demo.audio.url","http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");
    private final int WIDTH = 400;
    private final int HEIGHT = 300;
    private final int BANDS = 128;
    private GraphicsContext gc;
    private float[] buffer = new float[BANDS];

    public AudioVisualizer() {
        Media media = new Media(AUDIO_URI);
        audioPlayer = new MediaPlayer(media);
        audioPlayer.setAudioSpectrumListener(this);
        audioPlayer.setAudioSpectrumInterval(0.02);
        audioPlayer.setAudioSpectrumNumBands(BANDS);
        audioPlayer.setCycleCount(Timeline.INDEFINITE);
	}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        audioPlayer.play();

        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.setTitle("Audio Visualizer");
        stage.show();
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
