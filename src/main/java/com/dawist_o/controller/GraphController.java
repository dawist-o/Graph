package com.dawist_o.controller;

import com.dawist_o.model.DGraph;
import com.dawist_o.model.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

import static com.dawist_o.Main.updateGraphView;

public class GraphController {


    @FXML
    private TextField shortestPathTF;

    @FXML
    private TextField longestPathTF;

    @FXML
    private TextArea allPathsTA;

    @FXML
    private TextField graphCenterTF;

    @FXML
    private TextField newVertexTextField;

    @FXML
    private ComboBox<String> sourceVertexBox;

    @FXML
    private ComboBox<String> receivingVertexBox;

    @FXML
    private ComboBox<String> removeVertexBox;

    @FXML
    private TextField edgeWeightField;

    private DGraph<String, String> DGraph;

    @FXML
    void onRemoveEdgePressed(ActionEvent event) {
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;
        System.out.println(DGraph);
        //DGraph.removeEdge();
        System.out.println(DGraph);
    }

    @FXML
    void onRemoveVertexPressed(ActionEvent event) {
        if (removeVertexBox.getValue() == null) return;

        DGraph.removeVertex(removeVertexBox.getValue());
        sourceVertexBox.getItems().remove(removeVertexBox.getValue());
        receivingVertexBox.getItems().remove(removeVertexBox.getValue());
        removeVertexBox.getItems().remove(removeVertexBox.getValue());
    }

    @FXML
    void showGraphProperties(ActionEvent event) {
/*        if (graph.getAdjacentVertices().isEmpty() ||
                sourceVertexBox.getValue() == null && receivingVertexBox.getValue() == null) return;

        List<List<Vertex>> allPaths = graph.getAllPaths(sourceVertexBox.getValue(), receivingVertexBox.getValue());
        if(allPaths.isEmpty())
            return;
        fillShortestPathTF(allPaths);
        fillLongestPathTF(allPaths);
        fillAllPathsTA(allPaths);
        graphCenterTF.setText(graph.getGraphCenter().getValue());*/
        updateGraphView(DGraph);
    }

    private void fillAllPathsTA(List<List<Vertex>> allPaths) {
        StringBuilder stringBuilder = new StringBuilder();
        for (List<Vertex> path : allPaths) {
            stringBuilder.append(getPathAsString(path)).append('\n');
        }
        allPathsTA.setText(stringBuilder.toString());
    }

    private void fillLongestPathTF(List<List<Vertex>> allPaths) {
        longestPathTF.setText(getPathAsString(allPaths.get(allPaths.size() - 1)));
    }

    private void fillShortestPathTF(List<List<Vertex>> allPaths) {
        shortestPathTF.setText(getPathAsString(allPaths.get(0)));
    }

    private String getPathAsString(List<Vertex> path) {
        StringBuilder shortestPath = new StringBuilder();
        boolean isFirst = true;
        try {
            for (Vertex v : path) {
                if (isFirst) {
                    shortestPath.append(v);
                    isFirst = false;
                } else
                    shortestPath.append(" -").append('(').append(v).append(")-> ").append(v);
            }
        } catch (NullPointerException e) {
            return "There are no paths between this vertices";
        }
        return shortestPath.toString();
    }

    @FXML
    void onConnectButtonPressed(ActionEvent event) {
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;

        DGraph.insertEdge(sourceVertexBox.getValue(), receivingVertexBox.getValue(), edgeWeightField.getText());

        System.out.println(DGraph);
    }

    @FXML
    void onAddNewVertexButtonPressed(ActionEvent event) {
        String vValue = newVertexTextField.getText().trim();

/*        if (DGraph.getAdjacentVertices().containsKey(new Vertex(vValue))) return;
        DGraph.addVertex(vValue);
        sourceVertexBox.getItems().add(vValue);
        receivingVertexBox.getItems().add(vValue);
        removeVertexBox.getItems().add(vValue);*/
    }

    @FXML
    void initialize() {
        DGraph = new DGraph<>();
        newVertexTextField.setText("0");
        sourceVertexBox.setValue("A");
        receivingVertexBox.setValue("F");
    }
}
