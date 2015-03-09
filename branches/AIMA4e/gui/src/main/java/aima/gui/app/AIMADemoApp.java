package aima.gui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Ciaran O'Reilly
 */
public class AIMADemoApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("aimademoapp.fxml"));
        primaryStage.setTitle("AIMA4e-Java Demos");
        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("aima/gui/app/aimademoapp.css");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
