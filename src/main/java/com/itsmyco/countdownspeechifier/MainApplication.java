package com.itsmyco.countdownspeechifier;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 671, 600);
        stage.setTitle("Countdown Speechifier");
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();

        Platform.setImplicitExit(false);
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        MainController.executor.shutdownNow();
        Platform.exit();
    }
}