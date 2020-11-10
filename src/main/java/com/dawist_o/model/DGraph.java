package com.dawist_o.model;


import java.util.*;

public class DGraph<V extends Comparable<V>, E> implements Graph<V, E> {
    private final Map<V, Vertex<V>> vertices;
    private final Map<E, Edge<E, V>> edges;
    private List<List<Vertex<V>>> allPaths;

    public DGraph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("Graph with %d vertices and %d edges:\n", verticesCount(), edgesCount())
        );
        sb.append("Vertices: \n");
        vertices.values().forEach(v->sb.append("\t").append(v.toString()).append("\n"));
        sb.append("\n Edges: \n");
        edges.values().forEach(e->sb.append("\t").append(e.toString()).append("\n"));
        return sb.toString();
    }

    public List<List<Vertex<V>>> getAllPaths(V source, V receiver) {
        allPaths = new ArrayList<>();
        Map<V, Boolean> isVisited = new HashMap<>();
        for (Vertex<V> v : vertices.values()) {
            isVisited.put(v.element(), false);
        }
        ArrayList<Vertex<V>> pathList = new ArrayList<>();
        pathList.add(vertices.get(source));
        // Call recursive utility
        getAllPathsRecursive(source, receiver, isVisited, pathList);
        return allPaths;
    }

    private void getAllPathsRecursive(V source, V receiver,
                                      Map<V, Boolean> isVisited, List<Vertex<V>> localPathList) {
        if (source.equals(receiver)) {
            allPaths.add(new ArrayList<>(localPathList));
            // if match found then no need to traverse more till depth
            return;
        }
        // Mark the current node as visited
        isVisited.put(source, true);
        // Recur for all the vertices adjacent to current vertex
        for (Vertex<V> currentVertex : getAdjacentVertices(vertices.get(source))) {
            if (!isVisited.get(currentVertex.element())) {
                localPathList.add(currentVertex);
                getAllPathsRecursive(currentVertex.element(), receiver, isVisited, localPathList);
                localPathList.remove(currentVertex);
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
    }

    public Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) {
        List<Edge<E, V>> outboundEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {
            if (((GEdge<E, V>) edge).outVertex.equals(outbound)) {
                outboundEdges.add(edge);
            }
        }
        return outboundEdges;
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

    private class GEdge<E, V extends Comparable<V>> implements Edge<E, V> {
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
    }

    private class GVertex<V extends Comparable<V>> implements Vertex<V> {
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
    /*
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Vertex, List<Vertex>> entry : adjacentVertices.entrySet()) {
            sb.append(entry.getKey()).append(": ");
            for (Vertex adjV : entry.getValue()) {
                sb.append(adjV).append(',');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    *//**
     * Adds new Vertex without edges
     *
     * @param value value of vertex
     **//*
    public void addVertex(String value) {
        adjacentVertices.put(new Vertex(value), new ArrayList<>());
    }

    public Map<Vertex, List<Vertex>> getAdjacentVertices() {
        return adjacentVertices;
    }

    private final Map<Vertex, List<Vertex>> adjacentVertices;
    private Set<Vertex> settled;
    private PriorityQueue<Vertex> pq;
    private Map<Vertex, Integer> dist;

    public int getShortestPathBetween(String value1, String value2) {
        //Map of all nodes with path for them
        dist = new LinkedHashMap<>();
        for (Vertex ver : adjacentVertices.keySet()) {
            dist.put(ver, Integer.MAX_VALUE);
        }
        dist.put(new Vertex(value1), 0);
        //adding source node into PriorityQueue
        pq = new PriorityQueue<>();
        pq.add(getEntryByKey(adjacentVertices, new Vertex(value1)).getKey());
        //HashSet for settled nodes
        settled = new HashSet<>();
        //while haven't visited all nodes and pq isn't empty
        while (settled.size() != adjacentVertices.size() && !pq.isEmpty()) {
            //getting current node
            Vertex currentNode = pq.remove();
            //current node is visited, so adds it into settled
            settled.add(currentNode);
            //checking all adjacency vertices
            ifThereAreShorterPaths(getEntryByKey(adjacentVertices, currentNode));
        }
        return dist.get(new Vertex(value2));
    }

    private void ifThereAreShorterPaths(Map.Entry<Vertex, List<Vertex>> vertexEntry) {
        int edgeDistance;
        int newDistance;
        //checking all adjacency nodes (neighbours)
        for (Vertex neighbour : vertexEntry.getValue()) {
            //if node isn't visited
            if (!settled.contains(neighbour)) {
                edgeDistance = neighbour.getWeight();
                newDistance = dist.get(vertexEntry.getKey()) + edgeDistance;
                //if this way is shorter than the previous one
                if (newDistance < dist.get(neighbour)) {
                    dist.put(neighbour, newDistance);
                }
                pq.add(neighbour);
            }
        }
    }

    public Vertex getGraphCenter() {
        Map<Vertex, Integer> eccentricityMap = new LinkedHashMap<>();
        for (Vertex ver : adjacentVertices.keySet()) {
            eccentricityMap.put(ver, 0);
        }
        for (Map.Entry<Vertex, List<Vertex>> external_entry : adjacentVertices.entrySet()) {
            for (Map.Entry<Vertex, List<Vertex>> inner_entry : adjacentVertices.entrySet()) {
                getShortestPathBetween(external_entry.getKey().getValue(), inner_entry.getKey().getValue());
                for (Map.Entry<Vertex, Integer> distance : dist.entrySet()) {
                    if (distance.getValue() != Integer.MAX_VALUE
                            && eccentricityMap.get(distance.getKey()) <= distance.getValue()) {
                        eccentricityMap.put(distance.getKey(), distance.getValue());
                    }
                }
            }
        }
        Vertex center = null;
        int minEx = Integer.MAX_VALUE;
        for (Map.Entry<Vertex, Integer> ex : eccentricityMap.entrySet()) {
            if (minEx > ex.getValue() && ex.getValue() != 0) {
                center = ex.getKey();
                minEx = ex.getValue();
            }
        }
        return center;
    }

    private List<List<Vertex>> allPaths;

    public List<List<Vertex>> getAllPaths(String source, String receiver) {
        allPaths = new ArrayList<>();
        Map<Vertex, Boolean> isVisited = new HashMap<>();
        for (Vertex v : adjacentVertices.keySet()) {
            isVisited.put(v, false);
        }
        ArrayList<Vertex> pathList = new ArrayList<>();
        pathList.add(getEntryByKey(adjacentVertices, new Vertex(source)).getKey());
        // Call recursive utility
        getAllPathsRecursive(source, receiver, isVisited, pathList);

        allPaths.sort(Comparator.comparing(cPath -> cPath.stream().mapToInt(Vertex::getWeight).sum()));

        return allPaths;
    }

    private void getAllPathsRecursive(String source, String receiver,
                                      Map<Vertex, Boolean> isVisited, List<Vertex> localPathList) {
        if (source.equals(receiver)) {
            allPaths.add(new ArrayList<>(localPathList));
            // if match found then no need to traverse more till depth
            return;
        }
        // Mark the current node as visited
        isVisited.put(new Vertex(source), true);
        // Recur for all the vertices adjacent to current vertex
        for (Vertex currentVertex : adjacentVertices.get(new Vertex(source))) {
            if (!isVisited.get(currentVertex)) {
                localPathList.add(currentVertex);
                getAllPathsRecursive(currentVertex.getValue(), receiver, isVisited, localPathList);
                localPathList.remove(currentVertex);
            }
        }
        // Mark the current node as unvisited for other paths
        isVisited.put(new Vertex(source), false);
    }


    *//**
     * returns entry with K key from Map<K,V> map
     *
     * @param map class that implements interface Map
     * @param key key for entry
     * @param <K> type of key
     * @param <V> type of value
     *//*
    private static <K, V> Map.Entry<K, V> getEntryByKey(Map<K, V> map, K key) {
        return map.entrySet()
                .stream()
                .filter(kvEntry -> kvEntry.getKey().equals(key)).findFirst().get();
    }

    public void removeEdge(String vertex, String edge) {
        adjacentVertices.get(new Vertex(vertex)).remove(new Vertex(edge));
    }

    *//**
     * Deletes vertices and edges, that contains it
     *
     * @param value value of vertex for remove
     *//*
    public void removeVertex(String value) {
        Vertex vertexForRemove = new Vertex(value);
        adjacentVertices.values().forEach(v -> v.remove(vertexForRemove));
        adjacentVertices.remove(vertexForRemove);
    }

    *//**
     * Adds edge from first vertex to second with weight
     *
     * @param value1 value of first vertex
     * @param value2 value of edge vertex
     * @param weight weight of edge between first and second vertices
     *//*
    public void addEdge(String value1, String value2, int weight) {
        Vertex v1 = new Vertex(value1);
        Vertex v2 = new Vertex(value2, weight);
        adjacentVertices.get(v1).add(v2);
    }

    *//**
     * Adds edge from first vertex to second with default weight(1)
     *
     * @param value1 value of first vertex
     * @param value2 value of edge vertex
     *//*
    public void addEdge(String value1, String value2) {
        Vertex v1 = new Vertex(value1);
        Vertex v2 = new Vertex(value2);
        adjacentVertices.get(v1).add(v2);
    }

    private void fillForTest() {
        this.adjacentVertices.clear();
        //E : 4(D)
        adjacentVertices.put(new Vertex("E")
                , Collections.singletonList(
                        new Vertex("D", 4)));
        //F :
        adjacentVertices.put(new Vertex("F")
                , new ArrayList<>());
        //C : 3(E)
        adjacentVertices.put(new Vertex("C")
                , Collections.singletonList(
                        new Vertex("E", 3)));
        //D : 11(F)
        adjacentVertices.put(new Vertex("D")
                , Collections.singletonList(
                        new Vertex("F", 11)));
        //A : 4(B) 2(C)
        adjacentVertices.put(new Vertex("A")
                , Arrays.asList(
                        new Vertex("A", 3),
                        new Vertex("B", 4),
                        new Vertex("B", 14),
                        new Vertex("C", 2),
                        new Vertex("F", 2)));
        //B : 5(C) 10(D)
        adjacentVertices.put(new Vertex("B")
                , Arrays.asList(
                        new Vertex("C", 5),
                        new Vertex("D", 10)));
    }*/
}
