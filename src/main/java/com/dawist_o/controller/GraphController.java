package com.dawist_o.controller;

import com.dawist_o.model.Graph;
import com.dawist_o.model.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.List;

public class GraphController {

    @FXML
    private AnchorPane vertexField;
    @FXML
    private Text pathText;

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

    private Graph graph;

    @FXML
    void onRemoveEdgePressed(ActionEvent event) {
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;
        System.out.println(graph);
        graph.removeEdge(sourceVertexBox.getValue(), receivingVertexBox.getValue());
        System.out.println(graph);
    }

    @FXML
    void onRemoveVertexPressed(ActionEvent event) {
        if (removeVertexBox.getValue() == null) return;

        graph.removeVertex(removeVertexBox.getValue());
        sourceVertexBox.getItems().remove(removeVertexBox.getValue());
        receivingVertexBox.getItems().remove(removeVertexBox.getValue());
        removeVertexBox.getItems().remove(removeVertexBox.getValue());
    }

    @FXML
    void showGraphProperties(ActionEvent event) {
        if (graph.getAdjacentVertices().isEmpty() ||
                sourceVertexBox.getValue() == null && receivingVertexBox.getValue() == null) return;

        List<List<Vertex>> allPaths = graph.getAllPaths(sourceVertexBox.getValue(), receivingVertexBox.getValue());
        fillShortestPathTF(allPaths);
        fillLongestPathTF(allPaths);
        fillAllPathsTA(allPaths);
        graphCenterTF.setText(graph.getGraphCenter().getValue());
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
                    shortestPath.append(v.getValue());
                    isFirst = false;
                } else
                    shortestPath.append(" -").append('(').append(v.getWeight()).append(")-> ").append(v.getValue());
            }
        } catch (NullPointerException e) {
            return "There are no paths between this vertices";
        }
        return shortestPath.toString();
    }

    @FXML
    void onConnectButtonPressed(ActionEvent event) {
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;

        if (isNumeric(edgeWeightField.getText()))
            graph.addEdge(sourceVertexBox.getValue(), receivingVertexBox.getValue(), Integer.parseInt(edgeWeightField.getText()));
        else
            graph.addEdge(sourceVertexBox.getValue(), receivingVertexBox.getValue());

        System.out.println(graph);
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    @FXML
    void onAddNewVertexButtonPressed(ActionEvent event) {
        String vValue = newVertexTextField.getText().trim();

        if (graph.getAdjacentVertices().containsKey(new Vertex(vValue))) return;

        graph.addVertex(vValue);
        sourceVertexBox.getItems().add(vValue);
        receivingVertexBox.getItems().add(vValue);
        removeVertexBox.getItems().add(vValue);
    }

    @FXML
    void initialize() {
        graph = new Graph();
        newVertexTextField.setText("0");
    }
}
