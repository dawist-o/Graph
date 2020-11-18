package com.dawist_o.controllers;

import com.dawist_o.model.DGraph;
import com.dawist_o.model.Graph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class GraphController {

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

    private Graph<String, String> DGraph;

    @FXML
    void onRemoveEdgePressed(ActionEvent event) {
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null) return;
        //DGraph.removeEdge();
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
    void update(ActionEvent event) {
        MainController.graphView.getG();
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
        MainController.graphView.getG().insertVertex(vValue);
    }

    @FXML
    void initialize() {
        DGraph = new DGraph<>();
    }
}
