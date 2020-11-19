package com.dawist_o.model;


import com.dawist_o.exceptions.EmptyGraphException;

import java.util.*;

public class DGraph<V, E extends Comparable<E>> implements Graph<V, E> {
    private final Map<V, Vertex<V>> vertices;
    private final Map<E, Edge<E, V>> edges;
    private List<List<Edge<E, V>>> allPaths_Edges;

    public DGraph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: \n");
        vertices.values().forEach(v -> sb.append(v.toString()).append(",\t"));
        sb.append("\n Edges: \n");
        edges.values().forEach(e -> sb.append("\t").append(e.toString()).append("\n"));
        return sb.toString();
    }

    public List<List<Edge<E, V>>> getAllPaths(V source, V receiver) {
        allPaths_Edges = new LinkedList<>();
        Map<Edge<E, V>, Boolean> isVisited = new HashMap<>();
        for (Edge<E, V> e : edges.values()) {
            isVisited.put(e, false);
        }
        //check all paths from source vertex`
        List<Edge<E, V>> pathList = new LinkedList<>();
        outboundEdges(vertices.get(source)).forEach(outEdge -> {
            pathList.clear();
            pathList.add(outEdge);
            getAllPathsRecursive(outEdge, receiver, isVisited, pathList);
        });
        allPaths_Edges.sort((o1, o2) -> {
            Integer firstListSum = o1.stream().mapToInt(edge -> Integer.parseInt(edge.element().toString())).sum();
            Integer secondListSum = o2.stream().mapToInt(edge -> Integer.parseInt(edge.element().toString())).sum();
            return firstListSum.compareTo(secondListSum);
        });
        return allPaths_Edges;
    }

    @Override
    public Vertex<V> getGraphCenter() {
        if (vertices.isEmpty())
            throw new EmptyGraphException("This graph doesn't contains any vertices");
        Map<Vertex<V>, Integer> eccentricityMap = new HashMap<>();
        vertices.values().forEach(value -> eccentricityMap.put(value, 0));
        //count eccentricity for all vertices
        for (Vertex<V> inV : vertices.values()) {
            for (Vertex<V> outV : vertices.values()) {
                List<List<Edge<E, V>>> paths = getAllPaths(outV.element(), inV.element());
                if (!paths.isEmpty()) {
                    int distBetweenOutAndIn = paths.get(0).stream()
                            .mapToInt(edge -> Integer.parseInt(edge.element().toString())).sum();
                    if (eccentricityMap.get(inV) <= distBetweenOutAndIn)
                        eccentricityMap.put(inV, distBetweenOutAndIn);
                }
            }
        }
        //center vertex = vertex with minimal eccentricity
        Vertex<V> center = null;
        int minEccentricity = Integer.MAX_VALUE;
        for (Vertex<V> v : eccentricityMap.keySet()) {
            if (eccentricityMap.get(v) < minEccentricity && eccentricityMap.get(v) != 0) {
                center = v;
                minEccentricity = eccentricityMap.get(v);
            }
        }
        return center;
    }

    private void getAllPathsRecursive(Edge<E, V> source, V receiver,
                                      Map<Edge<E, V>, Boolean> isVisited, List<Edge<E, V>> localPathList) {

        if (source.vertices().get(1).element().equals(receiver)) {
            allPaths_Edges.add(new ArrayList<>(localPathList));
            // if match found then no need to traverse more till depth
            return;
        }
        // Mark the current node as visited
        isVisited.put(source, true);
        // Recur for all the vertices adjacent to current vertex
        for (Edge<E, V> currentEdge : outboundEdges(source.vertices().get(1))) {
            if (!isVisited.get(currentEdge)) {
                localPathList.add(currentEdge);
                getAllPathsRecursive(currentEdge, receiver, isVisited, localPathList);
                localPathList.remove(currentEdge);
            }
        }
        // Mark the current node as unvisited for other paths
        isVisited.put(source, false);
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return vertices.values();
    }

    @Override
    public Collection<Edge<E, V>> edges() {
        return edges.values();
    }

    private Collection<Vertex<V>> getAdjacentVertices(Vertex<V> outV) {
        List<Vertex<V>> adjVertices = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {
            if (((GEdge<E, V>) edge).outVertex == outV) {
                adjVertices.add(((GEdge<E, V>) edge).inVertex);
            }
        }
        return adjVertices;
    }


    @Override
    public void insertVertex(V value) {
        vertices.put(value, new GVertex<>(value));
    }

    @Override
    public void removeVertex(V vertex) {
        GVertex<V> removeV = new GVertex<>(vertex);
        Collection<Edge<E, V>> incidentEdges = incidentEdges(removeV);
        incidentEdges.addAll(outboundEdges(removeV));
        for (Edge<E, V> edge : incidentEdges) {
            edges.remove(edge.element());
        }
        vertices.remove(vertex);
    }

    public List<Edge<E, V>> outboundEdges(Vertex<V> outbound) {
        List<Edge<E, V>> outboundEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {
            if (((GEdge<E, V>) edge).outVertex.equals(outbound)) {
                outboundEdges.add(edge);
            }
        }
        return outboundEdges;
    }

    public List<Edge<E, V>> inboundEdges(Vertex<V> inbound) {
        List<Edge<E, V>> inboundEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {
            if (((GEdge<E, V>) edge).outVertex.equals(inbound)) {
                inboundEdges.add(edge);
            }
        }
        return inboundEdges;
    }

    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) {
        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {
            if (((GEdge<E, V>) edge).contains(v)) {
                incidentEdges.add(edge);
            }
        }
        return incidentEdges;
    }

    private GVertex<V> vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.element().equals(vElement)) {
                return (GVertex<V>) v;
            }
        }
        return null;
    }

    @Override
    public void insertEdge(V outVertex, V inVertex, E edgeValue) {
        GVertex<V> out = vertexOf(outVertex);
        GVertex<V> in = vertexOf(inVertex);
        edges.put(edgeValue, new GEdge<E, V>(out, in, edgeValue));
    }

    @Override
    public void removeEdge(E edge) {
        edges.remove(edge);
    }

    @Override
    public int verticesCount() {
        return vertices.size();
    }

    @Override
    public int edgesCount() {
        return edges.size();
    }

    private class GEdge<E extends Comparable<E>, V> implements Edge<E, V>, Comparable<E> {
        E edgeValue;
        Vertex<V> inVertex;
        Vertex<V> outVertex;

        public GEdge(Vertex<V> outVertex, Vertex<V> inVertex, E edgeValue) {
            this.edgeValue = edgeValue;
            //    this.edgeValue = outVertex.element().toString() + inVertex.element().toString() + edgeValue.toString();
            this.inVertex = inVertex;
            this.outVertex = outVertex;
        }

        boolean contains(Vertex<V> v) {
            return inVertex == v || outVertex == v;
        }

        @Override
        public E element() {
            return edgeValue;
        }

        @Override
        public String toString() {
            return "Edge (" + edgeValue + ") = out " + outVertex + ", in " + inVertex;
        }

        @Override
        public List<Vertex<V>> vertices() {
            return List.of(outVertex, inVertex);
        }

        @Override
        public int compareTo(E o) {
            return edgeValue.compareTo(o);
        }
    }

    private class GVertex<V> implements Vertex<V> {
        V value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GVertex<V> gVertex = (GVertex<V>) o;
            return Objects.equals(value, gVertex.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        public GVertex(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + this.value + "}";
        }

        @Override
        public V element() {
            return this.value;
        }
    }
}
