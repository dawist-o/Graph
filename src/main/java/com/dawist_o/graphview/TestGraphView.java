package com.dawist_o.graphview;

import com.dawist_o.graphview.placementstrategies.CirclePlacementStrategy;
import com.dawist_o.graphview.placementstrategies.PlacementStrategy;
import com.dawist_o.model.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestGraphView extends Application {
    private static BorderPane pane;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage ignored) throws Exception {
        Graph g=new Graph();

        //SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        PlacementStrategy strategy = new CirclePlacementStrategy();
        GraphView graphView = new GraphView(g, strategy);

        pane=new BorderPane();
        pane.setCenter(graphView);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com.dawist_o/graph_menu.fxml"));
        AnchorPane root = loader.load();
        pane.setRight(root);

        Scene scene = new Scene(pane, 900, 600);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        graphView.init();
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
