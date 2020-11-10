package com.dawist_o.model;

import java.util.Collection;
import java.util.List;

public interface Graph<V extends Comparable<V>, E>  {

    void insertVertex(V value);
    void removeVertex(V vertex);

    void insertEdge(V outVertex, V inVertex, E edgeValue);
    void removeEdge(E edge);

    int verticesCount();
    int edgesCount();

    List<List<Vertex<V>>> getAllPaths(V source, V receiver);
    Collection<Vertex<V>> vertices();
    Collection<Edge<E, V>> edges();
    Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound);
    Collection<Edge<E, V>> incidentEdges(Vertex<V> v);
}
