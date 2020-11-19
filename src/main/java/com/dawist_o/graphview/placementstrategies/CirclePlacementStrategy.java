package com.dawist_o.graphview.placementstrategies;

import com.dawist_o.graphview.placementutilities.PlacementManager;
import com.dawist_o.graphview.vertex.VertexNode;
import com.dawist_o.model.Graph;
import javafx.geometry.Point2D;

import java.util.Collection;

public class CirclePlacementStrategy<V,E> implements PlacementStrategy<V,E> {
    @Override
    public void place(double width, double height, Graph<V,E> theDGraph, Collection<VertexNode<V>> vertices) {
        Point2D center = new Point2D(width / 2, height / 2);
        double verticesCount = vertices.size();
        double degreePerVertex = 360 / verticesCount;


        boolean first = true;
        Point2D p = null;
        for (VertexNode<V> vertex : vertices) {
            //place first node in north
            if (first) {
                //verifiy smaller width and height.
                if (width > height)
                    p = new Point2D(center.getX(),
                            center.getY() - height / 2 + vertex.getRadius() * 2);
                else
                    p = new Point2D(center.getX(),
                            center.getY() - width / 2 + vertex.getRadius() * 2);
                first = false;
            } else {
                p = PlacementManager.rotate(p, center, degreePerVertex);
            }

            vertex.setPosition(p.getX(), p.getY());

        }
    }
}
