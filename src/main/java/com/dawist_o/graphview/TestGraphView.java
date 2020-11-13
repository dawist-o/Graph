package com.dawist_o.graphview;

import com.dawist_o.graphview.placementstrategies.CirclePlacementStrategy;
import com.dawist_o.graphview.placementstrategies.PlacementStrategy;
import com.dawist_o.model.DGraph;
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
        Graph<String, String> g = build_flower_graph();

        //SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        PlacementStrategy strategy = new CirclePlacementStrategy();
        GraphView<String,String> graphView = new GraphView<>(g, strategy);

        pane = new BorderPane();
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

    private Graph<String, String> build_flower_graph() {

        Graph<String, String> g = new DGraph<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");

        g.insertEdge("A", "B", "1");
        g.insertEdge("A", "B", "2");
        g.insertEdge("B", "C", "3");
        g.insertEdge("B", "C", "5");
        g.insertEdge("A", "C", "7");

/*        g.insertEdge("A", "E", "4");
        g.insertEdge("A", "F", "5");
        g.insertEdge("A", "G", "6");*/

        g.getAllPaths("A","С").forEach(System.out::println);
        return g;
    }

    public static void updateGraphView(DGraph<String,String> g) {
        PlacementStrategy strategy = new CirclePlacementStrategy();
        GraphView<String,String> graphView = new GraphView<>(g, strategy);
        graphView.setPrefWidth(400);
        graphView.setMinWidth(400);
        pane.setCenter(graphView);
        graphView.init();
    }
}
