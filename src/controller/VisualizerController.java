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
            gc.clearRect(0, 0, width, height);
            gc.setFill(Color.web("#b37ccf"));
            double a = 360.0 / bands.length;
            for (int i = 0; i < bands.length; i++) {
                double xCoords[] = {width / 2 + Math.cos(Math.toRadians(i * a)) * 100, width / 2 + Math.cos(Math.toRadians((i + 1) * a)) * 100, width / 2 + Math.cos(Math.toRadians(i * a)) * 100 + bands[i], width / 2 + Math.cos(Math.toRadians((i + 1) * a)) * 100 + bands[i]};
                double yCoords[] = {height/2 - Math.sin(Math.toRadians(i * a)) * 100, height/2 - Math.sin(Math.toRadians((i + 1) * a)) * 100, height/2 - Math.sin(Math.toRadians(i * a)) * 100 + bands[i], height/2 - Math.sin(Math.toRadians((i + 1) * a)) *100 + bands[i]};

                gc.fillPolygon(xCoords, yCoords, 4);
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
