package com.dawist_o.model;


import java.util.*;

public class Graph {
    public Graph() {
        this.adjacentVertices = new HashMap<>();
    }

    /**
     * Adds new Vertex without edges
     *
     * @param value value of vertex
     **/
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
        //fillForTest();
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
        fillForTest();
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
            System.out.println(ex.getKey().getValue() + ex.getValue());
            if (minEx > ex.getValue()) {
                center = ex.getKey();
                minEx = ex.getValue();
            }
        }
        return center;
    }

    private List<List<Vertex>> allPaths;

    public List<List<Vertex>> getAllPaths(String source, String receiver) {
        fillForTest();
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
        Collections.reverse(allPaths);

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


    /**
     * returns entry with K key from Map<K,V> map
     *
     * @param map class that implements interface Map
     * @param key key for entry
     * @param <K> type of key
     * @param <V> type of value
     */
    private static <K, V> Map.Entry<K, V> getEntryByKey(Map<K, V> map, K key) {
        return map.entrySet()
                .stream()
                .filter(kvEntry -> kvEntry.getKey().equals(key)).findFirst().get();
    }

    /**
     * Deletes vertices and edges, that contains it
     *
     * @param value value of vertex for remove
     */
    public void removeVertex(String value) {
        Vertex vertexForRemove = new Vertex(value);
        adjacentVertices.values().forEach(v -> v.remove(vertexForRemove));
        adjacentVertices.remove(vertexForRemove);
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
        adjacentVertices.get(v1).add(v2);
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
                        new Vertex("B", 4),
                        new Vertex("C", 2),
                        new Vertex("F", 2)));
        //B : 5(C) 10(D)
        adjacentVertices.put(new Vertex("B")
                , Arrays.asList(
                        new Vertex("C", 5),
                        new Vertex("D", 10)));
    }
}
