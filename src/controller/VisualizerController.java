package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.MP3Player;

import java.util.Observable;
import java.util.Observer;

public class VisualizerController implements Observer {

    private MP3Player player;
    @FXML
    private Pane centerPane;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;


    public void initPlayer(MP3Player player) {
        if (this.player != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.player = player;
        this.player.addObserver(this);
    }

    @FXML
    public void initialize() {
        this.gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(centerPane.widthProperty());
        canvas.heightProperty().bind(centerPane.heightProperty());
    }

    private void drawShapes(float bands[]) {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double scale = height / 100;
            // TODO doesn't clear after using step slider
            gc.clearRect(0, 0, width, height);
            gc.setFill(Color.web("#d59a63"));
            double barWidth = width / bands.length;
            for (int i = 0; i < bands.length; i++) {
                gc.fillRect(i * barWidth, height / 2 - bands[i] * scale, barWidth, bands[i] * scale * 2);
            }
        });

    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof float[]) {
            drawShapes((float[]) o);
        }
    }
}
