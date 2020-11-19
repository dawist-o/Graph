package com.dawist_o.controllers;

import com.dawist_o.graphview.GraphView;
import com.dawist_o.graphview.placementstrategies.CirclePlacementStrategy;
import com.dawist_o.graphview.placementstrategies.PlacementStrategy;
import com.dawist_o.model.DGraph;
import com.dawist_o.model.Graph;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import static com.dawist_o.controllers.GraphController.receiveV;
import static com.dawist_o.controllers.GraphController.sourceV;

public class MainController {
    public static Stage stage;
    public static GraphView<String, String> graphView;
    private static BorderPane borderPane;

    private static volatile boolean running;

    public static void setMainStage() throws IOException {
        borderPane = new BorderPane();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(com.dawist_o.controllers.GraphController.class.getResource("/com.dawist_o/graph_menu.fxml"));
        AnchorPane root = loader.load();
        borderPane.setRight(root);

        //Graph<String, String> g = build_flower_graph();
        Graph<String, String> g = new DGraph<>();
        PlacementStrategy<String, String> strategy = new CirclePlacementStrategy<>();
        graphView = new GraphView<>(g, strategy);
        borderPane.setCenter(graphView);
        // graphView.initPaths("A", "C");

        Scene scene = new Scene(borderPane, 900, 600);
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();


        stage.setOnCloseRequest((event) -> running = false);

        graphView.init();
        autoUpdate();
    }

    public static void autoUpdate() {
        running = true;
        final int UPDATE_TIME = 500;
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(UPDATE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                graphView.update();
                if(sourceV!=null && receiveV!=null){
                    graphView.highlightPaths(sourceV,receiveV);
                }
            }
        }).start();
    }

    private static Graph<String, String> build_flower_graph() {
        Graph<String, String> g = new DGraph<>();
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");

        g.insertEdge("A", "B", "1");
        g.insertEdge("A", "A", "9");
        g.insertEdge("A", "B", "2");
        g.insertEdge("B", "C", "3");
        g.insertEdge("B", "C", "5");
        g.insertEdge("A", "C", "7");

/*        g.insertEdge("A", "E", "4");
        g.insertEdge("A", "F", "5");
        g.insertEdge("A", "G", "6");*/

        g.getAllPaths("A", "С").forEach(System.out::println);
        System.out.println(g.getGraphCenter());
        return g;
    }
}
