package view;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AudioVisualizer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AudioVisualizer.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(stage);
        root.getStylesheets().add("styles/style.css");
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        stage.setScene(new Scene(root));
        stage.setTitle("Audio Visualizer");
        stage.show();
    }

    // TODO stop playing on close

}
