module Graph {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports com.dawist_o.controllers;
    opens com.dawist_o.controllers;

    exports com.dawist_o.graphview;
    opens com.dawist_o.graphview;

    exports com.dawist_o.model;
    opens com.dawist_o.model;

    exports com.dawist_o;
    opens com.dawist_o;

}