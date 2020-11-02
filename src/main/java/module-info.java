module Graph {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports com.dawist_o.controller;
    opens com.dawist_o.controller;
    exports com.dawist_o;
    opens com.dawist_o;

}