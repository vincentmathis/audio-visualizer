package view;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class DrawCanvas implements AudioSpectrumListener {

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private float[] buffer = new float[Controller.BANDS];
    private MediaPlayer audioPlayer;

    public DrawCanvas() {
        this.gc = canvas.getGraphicsContext2D();
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for (int i = 0; i < magnitudes.length; i++) {
            if (magnitudes[i] - audioPlayer.getAudioSpectrumThreshold() >= buffer[i]) {
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
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.web("#d59a63"));
        double barWidth = width / buffer.length;
        for (int i = 0; i < buffer.length; i++) {
            gc.fillRect(i * barWidth, height / 2 - buffer[i] * 3, barWidth, buffer[i] * 3 * 2);
        }
    }

    public void setAudioPlayer(MediaPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }
}
