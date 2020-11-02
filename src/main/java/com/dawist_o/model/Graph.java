package com.dawist_o.model;

import java.util.*;

public class Graph {
    private Map<Vertex,List<Vertex>> adjecentVertices;

    public Graph() {
        this.adjecentVertices = new HashMap<>();
    }

    /**
     * Adds new Vertex without edges
     *
     * @param value value of vertex
     **/
    public void addVertex(String value) {
        adjecentVertices.put(new Vertex(value),new ArrayList<>());
    }

    public Map<Vertex, List<Vertex>> getAdjecentVertices() {
        return adjecentVertices;
    }

    public void showShortestWay(String value1,String value2){
        
    }

    /**
     * Deletes vertices and edges, that contains it
     *
     * @param value value of vertex for remove
     */
    public void removeVertex(String value) {
        Vertex vertexForRemove = new Vertex(value);
        adjecentVertices.values().forEach(v->v.remove(vertexForRemove));
        adjecentVertices.remove(vertexForRemove);
    }

    /**
     * Adds edge from first vertex to second with weight
     *
     * @param value1 value of first vertex
     * @param value2 value of edge vertex
     * @param weight weight of edge between first and second vertices
     */
    public void addEdge(String value1, String value2, int weight) {
        Vertex v1 = new Vertex(value1);
        Vertex v2 = new Vertex(value2, weight);
        adjecentVertices.get(v1).add(v2);
    }

    /**
     * Adds edge from first vertex to second with default weight(1)
     *
     * @param value1 value of first vertex
     * @param value2 value of edge vertex
     */
    public void addEdge(String value1, String value2) {
        Vertex v1 = new Vertex(value1);
        Vertex v2 = new Vertex(value2);
        adjecentVertices.get(v1).add(v2);
    }

}
