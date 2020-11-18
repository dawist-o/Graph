package com.dawist_o.graphview;

import com.dawist_o.controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestGraphView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainController.stage = stage;
        MainController.setMainStage();
    }
}
