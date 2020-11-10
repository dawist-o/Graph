package com.dawist_o.graphview.edge;


import com.dawist_o.graphview.labels.LabelledNode;
import com.dawist_o.graphview.style.StylableNode;

public interface GraphEdge extends StylableNode, LabelledNode {
    void attachArrow(EdgeArrow arrow);

    EdgeArrow getAttachedArrow();
}
