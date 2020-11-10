package com.dawist_o.graphview.style;

public interface StylableNode {
    void setStyle(String css);

    void setStyleClass(String cssClass);

    void addStyleClass(String cssClass);

    boolean removeStyleClass(String cssClass);
}
