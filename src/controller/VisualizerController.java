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
    private boolean bars = true;

    private double brightness = 0.8;
    private double saturation = 0.5;

    private MP3Player player;
    @FXML
    private Pane centerPane;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    private float bands[];


    public void initPlayer(MP3Player player) {
        if (this.player != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.player = player;
        this.player.addObserver(this);
    }

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(centerPane.widthProperty());
        canvas.heightProperty().bind(centerPane.heightProperty());
    }


    private void drawCircleBars() {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double minBounds = width < height ? width : height;
            int radius = (int) (minBounds / 4);
            double scale = minBounds / 30;
            double angle = 360.0 / (bands.length - 1);

            gc.clearRect(0, 0, width, height);

            for (int i = 0; i < bands.length - 1; i += 2) {
                double band = Math.log(bands[i] + 1) * scale;
                double xCenter = width / 2;
                double yCenter = height / 2;
                double xDist1 = Math.sin(Math.toRadians(i * angle));
                double xDist2 = Math.sin(Math.toRadians((i + 2) * angle - angle / 2));
                double yDist1 = Math.cos(Math.toRadians(i * angle));
                double yDist2 = Math.cos(Math.toRadians((i + 2) * angle - angle / 2));
                double x1 = xCenter + xDist1 * radius;
                double x2 = xCenter + xDist2 * radius;
                double x3 = xCenter + xDist2 * (radius + band);
                double x4 = xCenter + xDist1 * (radius + band);
                double y1 = yCenter - yDist1 * radius;
                double y2 = yCenter - yDist2 * radius;
                double y3 = yCenter - yDist2 * (radius + band);
                double y4 = yCenter - yDist1 * (radius + band);

                double xCoords[] = {x1, x2, x3, x4};
                double yCoords[] = {y1, y2, y3, y4};

                gc.setFill(Color.hsb(i * angle, saturation, brightness));
                gc.fillPolygon(xCoords, yCoords, 4);
            }
        });

    }

    private void drawCircleLine() {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double xCenter = width / 2;
            double yCenter = height / 2;
            double minBounds = width < height ? width : height;
            int radius = (int) (minBounds / 4);
            double scale = minBounds / 30;
            double angle = 360.0 / bands.length;

            gc.clearRect(0, 0, width, height);
            gc.setLineWidth(scale / 10);

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

                gc.setStroke(Color.hsb(i * angle, saturation, brightness));
                gc.strokeLine(xCoord1, yCoord1, xCoord2, yCoord2);
            }
        });
    }

    private void drawStraightBars() {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double yCenter = height / 2;
            double minBounds = width < height ? width : height;
            double scale = minBounds / 30;
            double barWidth = width / (bands.length - 1);
            double angle = 360.0 / (bands.length - 1);

            gc.clearRect(0, 0, width, height);

            for (int i = 0; i < bands.length - 1; i += 2) {
                double band = Math.log(bands[i] + 1) * scale;
                gc.setFill(Color.hsb(i * angle, saturation, brightness));
                gc.fillRect(barWidth * i, yCenter - band, barWidth, band * 2);
            }
        });
    }

    private void drawStraightLine() {
        Platform.runLater(() -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double yCenter = height / 2;
            double minBounds = width < height ? width : height;
            double scale = minBounds / 30;
            double barWidth = width / (bands.length - 1);
            double angle = 360.0 / (bands.length - 1);

            gc.clearRect(0, 0, width, height);
            gc.setLineWidth(scale / 10);

            for (int i = 0; i < bands.length - 1; i++) {
                double band1 = Math.log(bands[i] + 1) * scale;
                double band2 = Math.log(bands[i + 1] + 1) * scale;

                gc.setStroke(Color.hsb(i * angle, saturation, brightness));
                gc.strokeLine(barWidth * i, yCenter - band1, barWidth * (i + 1), yCenter - band2);
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        if (saturation > 0.5) saturation -= 0.02;
        if (brightness > 0.8) brightness -= 0.02;

        bands = player.getBands();

        if (circle) {
            if (bars) drawCircleBars();
            else drawCircleLine();
        } else {
            if (bars) drawStraightBars();
            else drawStraightLine();
        }

        if (player.isBeat()) {
            saturation = 1.0;
            brightness = 1.0;
        }
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    public void setBars(boolean bars) {
        this.bars = bars;
    }


}
