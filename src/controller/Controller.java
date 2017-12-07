package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MP3Player;
import view.DrawCanvas;

import java.io.File;
import java.util.Observer;

public class Controller implements Observer {

    private final MP3Player player;
    private final DrawCanvas drawCanvas;
    private Stage stage;
    private File lastFile;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox slowCheckBox;
    @FXML
    private Slider spacingSlider;
    @FXML
    private Slider seekSlider;
    @FXML private AnchorPane canvasPane;
    private long lastPressed;


    public Controller() {
        this.player = new MP3Player();
        this.drawCanvas = new DrawCanvas();
        this.player.addObserver(drawCanvas);
        this.player.addObserver(this);
    }

    @FXML
    public void initialize() {
        // TODO only scales one direction
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
        drawCanvas.setCanvas(canvas);
        spacingSlider.valueProperty().addListener((observable, oldValue, newValue) -> player.setSteps(newValue.intValue()));
        seekSlider.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> lastPressed = System.currentTimeMillis());
        seekSlider.valueProperty().addListener(observable -> {
            if(seekSlider.isValueChanging() || (System.currentTimeMillis() - lastPressed) < 200) {
                player.setPosition(seekSlider.getValue());
            }
        });
    }

    @FXML
    void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MP3 File");
        if (lastFile != null) {
            fileChooser.setInitialDirectory(new File(lastFile.getParent()));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            lastFile = selectedFile;
            player.play(selectedFile.toString());
        }
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if(arg instanceof Double){
            seekSlider.setValue((double) arg);
        }
    }

    @FXML
    public void setSlowShrink() {
        player.setSlowShrink(slowCheckBox.isSelected());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
