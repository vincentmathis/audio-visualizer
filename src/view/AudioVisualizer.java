package view;


import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class AudioVisualizer extends Application implements AudioSpectrumListener {

    private static MediaPlayer audioPlayer;
    // TODO get URI of mp3
    //private static final String AUDIO_URI = new File("mpthreetest.mp3").toURI().toString();
    private static final String AUDIO_URI = System.getProperty("demo.audio.url","http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");


    public AudioVisualizer() {
        Media media = new Media(AUDIO_URI);
        audioPlayer = new MediaPlayer(media);
	}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        audioPlayer.setAudioSpectrumListener(this);
        audioPlayer.setAudioSpectrumInterval(0.02);
        audioPlayer.setAudioSpectrumNumBands(128);
        audioPlayer.setCycleCount(Timeline.INDEFINITE);
        audioPlayer.play();

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane, 400, 300);

        stage.setTitle("Audio Visualizer");
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        Platform.runLater(() -> {
            for (float magnitude : magnitudes) {
                System.out.println(magnitude);
            }
        });
    }
}
