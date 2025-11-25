package com.taxtelecom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.h2.tools.Server;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // H2 Database Web Console
        Server webServer = org.h2.tools.Server.createWebServer(
            "-web", "-webAllowOthers", "-webPort", "8082"
        ).start();
        Parent root = FXMLLoader.load(getClass().getResource("/main-view.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Менеджер документов");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}