package com.dawist_o.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.dawist_o.model.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class GraphController {

    @FXML
    private TextField newVertexTF;

    @FXML
    private ComboBox<String> sourceVertexBox;

    @FXML
    private ComboBox<String> receivingVertexBox;

    @FXML
    private TextField edgeWeightField;

    @FXML
    private ComboBox<String> removeVertexBox;

    @FXML
    private ComboBox<String> pathFrom;

    @FXML
    private ComboBox<String> pathTo;

    @FXML
    private ComboBox<String> removeEdgeBox;

    public static String sourceV;
    public static String receiveV;

    @FXML
    public void initialize(){
        pathFrom.getSelectionModel().selectedItemProperty().addListener((options,oldValue, newValue)->{
            sourceV=newValue;
        });
        pathTo.getSelectionModel().selectedItemProperty().addListener((options,oldValue, newValue)->{
            receiveV=newValue;
        });
    }

    @FXML
    void onAddNewVertexButtonPressed(ActionEvent event) {
        String newV = newVertexTF.getText().trim();

        if (newV.isEmpty() || newV.isBlank()) return;

        MainController.graphView.getGraph().insertVertex(newV);

        removeVertexBox.getItems().add(newV);

        sourceVertexBox.getItems().add(newV);
        receivingVertexBox.getItems().add(newV);

        pathFrom.getItems().add(newV);
        pathTo.getItems().add(newV);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @FXML
    void onCreateEdgeButtonPressed(ActionEvent event) {
        String weight = edgeWeightField.getText().trim();
        if (sourceVertexBox.getValue() == null || receivingVertexBox.getValue() == null
                || !isNumeric(weight) || removeEdgeBox.getItems().contains(weight)) return;

        MainController.graphView.getGraph().insertEdge(sourceVertexBox.getValue(),
                receivingVertexBox.getValue(), weight);

        removeEdgeBox.getItems().add(weight);
    }

    @FXML
    void onRemoveEdgePressed(ActionEvent event) {
        if (removeEdgeBox.getValue() == null) return;

        MainController.graphView.getGraph().removeEdge(removeEdgeBox.getValue());
        removeEdgeBox.getItems().remove(removeEdgeBox.getValue());
    }

    @FXML
    void onRemoveVertexPressed(ActionEvent event) {
        if (removeVertexBox.getValue() == null) return;

        String vertexToRemove = removeVertexBox.getValue().trim();

        MainController.graphView.getGraph().removeVertex(vertexToRemove);

        sourceVertexBox.getItems().remove(vertexToRemove);
        receivingVertexBox.getItems().remove(vertexToRemove);

        pathFrom.getItems().remove(vertexToRemove);
        pathTo.getItems().remove(vertexToRemove);

        removeVertexBox.getItems().remove(vertexToRemove);
    }
}
