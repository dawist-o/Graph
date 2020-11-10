package com.dawist_o.graphview.labels;

import com.dawist_o.graphview.style.StylableNode;
import com.dawist_o.graphview.style.StyleProxy;
import javafx.scene.text.Text;

public class LabelNode extends Text implements StylableNode {

    private StyleProxy styleProxy;
    public LabelNode(){
        this(0,0,"");
    }

    public LabelNode(String text) {
        this(0, 0, text);
    }

    public LabelNode(double x, double y, String text) {
        super(x, y, text);
        styleProxy = new StyleProxy(this);
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }
}
