package view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Observable;
import java.util.Observer;

public class DrawCanvas implements Observer {

    private Canvas canvas;
    private GraphicsContext gc;

    public DrawCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
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
