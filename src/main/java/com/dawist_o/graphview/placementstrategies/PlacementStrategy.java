package com.dawist_o.graphview.placementstrategies;


import com.dawist_o.graphview.vertex.VertexNode;
import com.dawist_o.model.DGraph;
import com.dawist_o.model.Graph;

import java.util.Collection;

public interface PlacementStrategy<V> {

    void place(double width, double height, Graph theDGraph, Collection<VertexNode<V>> vertices);
}
