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

    private boolean circle = true;
    private boolean straight;
    private boolean bars = true;
    private boolean line;

    private double BRIGHTNESS = 0.8;
    private double SATURATION = 0.5;

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
        gc.setLineWidth(2);
    }


    private void drawCircleBars(float bands[]) {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double minBounds = width < height ? width : height;
            int radius = (int) (minBounds / 4);
            double scale = minBounds / 30;
            double angle = 360.0 / (bands.length / 2);

            gc.clearRect(0, 0, width, height);


            for (int i = 0; i < bands.length - 2; i += 2) {
                double avg = (bands[i] + bands[i + 1]) / 2;
                avg = Math.log(avg + 1) * scale;
                double xCenter = width / 2;
                double yCenter = height / 2;
                double xDist1 = Math.sin(Math.toRadians(i * angle));
                double xDist2 = Math.sin(Math.toRadians((i + 2) * angle - angle/2));
                double yDist1 = Math.cos(Math.toRadians(i * angle));
                double yDist2 = Math.cos(Math.toRadians((i + 2) * angle - angle/2));
                double x1 = xCenter + xDist1 * radius;
                double x2 = xCenter + xDist2 * radius;
                double x3 = xCenter + xDist2 * (radius + avg);
                double x4 = xCenter + xDist1 * (radius + avg);
                double y1 = yCenter - yDist1 * radius;
                double y2 = yCenter - yDist2 * radius;
                double y3 = yCenter - yDist2 * (radius + avg);
                double y4 = yCenter - yDist1 * (radius + avg);

                double xCoords[] = {x1, x2, x3, x4};
                double yCoords[] = {y1, y2, y3, y4};

                gc.setFill(Color.hsb(i * angle, SATURATION, BRIGHTNESS));
                gc.fillPolygon(xCoords, yCoords, 4);
            }
        });

    }

    private void drawCircleLine(float bands[]) {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double xCenter = width / 2;
            double yCenter = height / 2;
            double minBounds = width < height ? width : height;
            int radius = (int) (minBounds / 4);
            double scale = minBounds / 50;
            double angle = 360.0 / bands.length;

            gc.clearRect(0, 0, width, height);


            for (int i = 0; i < bands.length; i++) {
                double band1 = Math.log(bands[i] + 1) * scale;
                double band2 = Math.log(bands[(i + 1) % bands.length] + 1) * scale;
                double xDist1 = Math.sin(Math.toRadians(i * angle));
                double yDist1 = Math.cos(Math.toRadians(i * angle));
                double xDist2 = Math.sin(Math.toRadians(((i + 1) % bands.length) * angle));
                double yDist2 = Math.cos(Math.toRadians(((i + 1) % bands.length) * angle));
                double xCoord1 = xCenter + xDist1 * (radius + band1);
                double xCoord2 = xCenter + xDist2 * (radius + band2);
                double yCoord1 = yCenter - yDist1 * (radius + band1);
                double yCoord2 = yCenter - yDist2 * (radius + band2);

                gc.setStroke(Color.hsb(i * angle, SATURATION, BRIGHTNESS));
                gc.strokeLine(xCoord1, yCoord1, xCoord2, yCoord2);
            }
        });
    }

    private void drawStraightBars(float bands[]) {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double yCenter = height / 2;
            double minBounds = width < height ? width : height;
            double scale = minBounds / 30;
            double barWidth = width / bands.length;
            double angle = 360.0 / bands.length;

            gc.clearRect(0, 0, width, height);

            for (int i = 0; i < bands.length - 2; i += 2) {
                double band = Math.log(bands[i] + 1) * scale;
                gc.setFill(Color.hsb(i * angle, SATURATION, BRIGHTNESS));
                gc.fillRect(barWidth * i, yCenter - band, barWidth, band);
            }
        });
    }

    private void drawStraightLine(float bands[]) {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double yCenter = height / 2;
            double minBounds = width < height ? width : height;
            double scale = minBounds / 30;
            double barWidth = width / bands.length;
            double angle = 360.0 / bands.length;

            gc.clearRect(0, 0, width, height);

            for (int i = 0; i < bands.length - 1; i++) {
                double band1 = Math.log(bands[i] + 1) * scale;
                double band2 = Math.log(bands[i + 1] + 1) * scale;

                gc.setStroke(Color.hsb(i * angle, SATURATION, BRIGHTNESS));
                gc.strokeLine(barWidth * i, yCenter - band1, barWidth * (i + 1), yCenter - band2);
            }
        });
    }


    @Override
    public void update(Observable observable, Object o) {
        if(SATURATION > 0.5) {
            SATURATION -= 0.02;
        }
        if (o instanceof float[]) {
            if (circle && bars) drawCircleBars((float[]) o);
            else if (circle && line) drawCircleLine((float[]) o);
            else if (straight && bars) drawStraightBars((float[]) o);
            else if (straight && line) drawStraightLine((float[]) o);
        }
        if (o instanceof Boolean) {
            SATURATION = 1.0;
        }
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    public void setStraight(boolean straight) {
        this.straight = straight;
    }

    public void setBars(boolean bars) {
        this.bars = bars;
    }

    public void setLine(boolean line) {
        this.line = line;
    }
}
