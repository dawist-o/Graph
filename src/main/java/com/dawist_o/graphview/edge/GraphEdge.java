package com.dawist_o.graphview.edge;


import com.dawist_o.graphview.labels.LabelledNode;
import com.dawist_o.graphview.style.StylableNode;
import com.dawist_o.model.Edge;

public interface GraphEdge<E,V> extends StylableNode, LabelledNode {
    void attachArrow(EdgeArrow arrow);
    EdgeArrow getAttachedArrow();

    Edge<E,V> getEdge();
}
