package view;

import controller.ControlsController;
import controller.VisualizerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.MP3Player;


public class AudioVisualizer extends Application {

    private MP3Player player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        root.getStyleClass().addAll("border-pane");

        FXMLLoader visualizerLoader = new FXMLLoader(getClass().getResource("visualizer.fxml"));
        root.setCenter(visualizerLoader.load());
        VisualizerController visualizerController = visualizerLoader.getController();

        FXMLLoader controlsLoader = new FXMLLoader(getClass().getResource("controls.fxml"));
        root.setBottom(controlsLoader.load());
        ControlsController controlsController = controlsLoader.getController();

        player = new MP3Player();
        visualizerController.initPlayer(player);
        controlsController.initPlayer(player);

        root.getStylesheets().add("styles/style.css");
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        stage.setScene(new Scene(root, 1280, 720));
        stage.setTitle("Audio Visualizer");
        stage.show();
    }


    // TODO player is null but minim keeps running after closing
    @Override
    public void stop() throws Exception {
        player.stop();
    }
}
