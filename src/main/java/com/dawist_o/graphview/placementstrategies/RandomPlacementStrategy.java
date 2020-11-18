package com.dawist_o.graphview.placementstrategies;

import com.dawist_o.graphview.vertex.VertexNode;
import com.dawist_o.model.Graph;

import java.util.Collection;
import java.util.Random;

public class RandomPlacementStrategy<V> implements PlacementStrategy<V> {


    @Override
    public void place(double width, double height, Graph theDGraph, Collection<VertexNode<V>> vertices) {
        Random r = new Random();
        for (VertexNode<V> v : vertices) {
            double x = r.nextDouble() * width;
            double y = r.nextDouble() * height;
            System.out.println("x = " + x + " y = " + y);
            v.setPosition(x, y);
        }
    }
}
