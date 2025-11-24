package com.taxtelecom;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Button helloBtn = new Button("Hello, JavaFX!");
        helloBtn.setOnAction(e -> System.out.println("Hello, JavaFX!"));
        grid.add(helloBtn, 0, 3);

        Scene scene = new Scene(grid, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}