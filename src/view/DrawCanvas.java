package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

public class DrawCanvas implements Observer {

    private Canvas canvas;
    private GraphicsContext gc;


    public DrawCanvas() {

    }

    private void drawShapes(float bands[]) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.web("#d59a63"));
        double barWidth = width / bands.length;
        for (int i = 0; i < bands.length; i++) {
            int SCALING = 20;
            gc.fillRect(i * barWidth, height / 2 - bands[i] * SCALING, barWidth, bands[i] * SCALING * 2);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof float[]) {
            drawShapes((float[]) o);
        }
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }
}
