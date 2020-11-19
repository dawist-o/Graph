package com.dawist_o.graphview.edge;

import com.dawist_o.graphview.style.StylableNode;
import com.dawist_o.graphview.style.StyleProxy;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class EdgeArrow extends Path implements StylableNode {
    private final StyleProxy styleProxy;

    public EdgeArrow() {
        getElements().add(new MoveTo(0, 0));
        getElements().add(new LineTo(-5 , 5));
        getElements().add(new MoveTo(0, 0));
        getElements().add(new LineTo(-5, -5));

        styleProxy = new StyleProxy(this);
        styleProxy.setStyleClass("arrow");
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }
}
