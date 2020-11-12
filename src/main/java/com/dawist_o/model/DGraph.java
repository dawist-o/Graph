package com.dawist_o.model;


import java.util.*;

public class DGraph<V, E extends Comparable<E>> implements Graph<V, E> {
    private final Map<V, Vertex<V>> vertices;
    private final Map<E, Edge<E, V>> edges;
    private List<List<Vertex<V>>> allPaths;

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

    public List<List<Vertex<V>>> getAllPaths(V source, V receiver) {
        allPaths = new ArrayList<>();
        Map<V, Boolean> isVisited = new HashMap<>();
        for (V v : vertices.keySet()) {
            isVisited.put(v, false);
        }
        List<Vertex<V>> pathList = new LinkedList<>();
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

    public List<Edge<E, V>> outboundEdges(Vertex<V> outbound) {
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
    /*

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
*/
}
