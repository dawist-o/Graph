module Graph {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports com.dawist_o.controllers;
    opens com.dawist_o.controllers;

    exports com.dawist_o.graphview.placementstrategies;
    opens com.dawist_o.graphview.placementstrategies;

    exports com.dawist_o.graphview.edge;
    opens com.dawist_o.graphview.edge;

    exports com.dawist_o.graphview.vertex;
    opens com.dawist_o.graphview.vertex;

    exports com.dawist_o.graphview;
    opens com.dawist_o.graphview;

    exports com.dawist_o.model;
    opens com.dawist_o.model;

    exports com.dawist_o;
    opens com.dawist_o;

}