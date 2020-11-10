package com.dawist_o.graphview.placementstrategies;


import com.dawist_o.graphview.vertex.VertexNode;
import com.dawist_o.model.Graph;

import java.util.Collection;

public interface PlacementStrategy {

    void place(double width, double height, Graph theGraph, Collection<VertexNode> vertices);
}
