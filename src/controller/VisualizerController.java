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
            double a = 360.0 / (bands.length / 4);
            for (int i = 0; i < bands.length - 4; i += 4) {
                double avg = (bands[i] + bands[i + 1] + bands[i + 2] + bands[i + 3]) / 4;
                if(avg < 1.0) continue;
                double xCenter = width / 2;
                double yCenter = height / 2;
                double xDist1 = Math.sin(Math.toRadians(i * a));
                double xDist2 = Math.sin(Math.toRadians((i + 4) * a));
                double yDist1 = Math.cos(Math.toRadians(i * a));
                double yDist2 = Math.cos(Math.toRadians((i + 4) * a));
                double x1 = xCenter + xDist1 * 100;
                double x2 = xCenter + xDist2 * 100;
                double x3 = xCenter + xDist2 * 100 * avg;
                double x4 = xCenter + xDist1 * 100 * avg;
                double y1 = yCenter - yDist1 * 100;
                double y2 = yCenter - yDist2 * 100;
                double y3 = yCenter - yDist2 * 100 * avg;
                double y4 = yCenter - yDist1 * 100 * avg;

                double xCoords[] = {x1, x2, x3, x4};
                double yCoords[] = {y1, y2, y3, y4};
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
