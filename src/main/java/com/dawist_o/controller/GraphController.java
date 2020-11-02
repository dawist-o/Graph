package com.dawist_o.controller;

import com.dawist_o.model.Graph;
import com.dawist_o.model.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class GraphController {

    @FXML
    private AnchorPane vertexField;
    @FXML
    private TextField newVertexTextField;

    @FXML
    private ComboBox<String> sourceVertexBox;

    @FXML
    private ComboBox<String> receivingVertexBox;
    
    @FXML
    private TextField edgeField;

    private Graph graph;

    @FXML
    void showShortestWay(){
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;

        graph.showShortestWay(sourceVertexBox.getValue(),receivingVertexBox.getValue());
    }

    @FXML
    void onConnectButtonPressed(ActionEvent event) {
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;

        graph.addEdge(sourceVertexBox.getValue(), receivingVertexBox.getValue(),Integer.parseInt(edgeField.getText()));

        System.out.println(graph.getAdjecentVertices());
    }
    

    @FXML
    void onAddNewVertexButtonPressed(ActionEvent event) {
        String vValue = newVertexTextField.getText().trim();

        if (graph.getAdjecentVertices().containsKey(new Vertex(vValue))) return;

        graph.addVertex(vValue);
        sourceVertexBox.getItems().add(vValue);
        receivingVertexBox.getItems().add(vValue);
    }


    @FXML
    void initialize() {
        graph = new Graph();
        newVertexTextField.setText("0");
    }
}
