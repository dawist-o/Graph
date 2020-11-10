package com.dawist_o;

import com.dawist_o.controller.GraphController;
import com.dawist_o.graphview.GraphView;
import com.dawist_o.graphview.placementstrategies.CirclePlacementStrategy;
import com.dawist_o.graphview.placementstrategies.PlacementStrategy;
import com.dawist_o.model.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

//TODO
/*
 * кратчайший путь
 * самый длинный путь
 * все пути между вершинами по возрастанию
 * центр графа
 * */

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage primaryStage;
    private static BorderPane pane;

    @Override
    public void start(Stage stage) throws Exception {
        pane = new BorderPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com.dawist_o/graph_menu.fxml"));
        AnchorPane root = loader.load();
        pane.setRight(root);

        updateGraphView(null);

        primaryStage = stage;
        primaryStage.setTitle("Graph");
        primaryStage.setScene(new Scene(pane, 670, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void updateGraphView(Graph g) {
        PlacementStrategy strategy = new CirclePlacementStrategy();
        GraphView graphView = new GraphView(g, strategy);
        graphView.setPrefWidth(400);
        graphView.setMinWidth(400);
        pane.setCenter(graphView);
        graphView.init();
    }

}
