package com.dawist_o;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com.dawist_o/graph.fxml"));
        System.out.println(loader.getLocation());
        Parent root = loader.load();
        this.primaryStage=stage;
        primaryStage.setTitle("Graph");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
