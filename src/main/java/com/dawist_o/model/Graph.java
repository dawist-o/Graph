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


    private Map<Vertex, List<Vertex>> adjacentVertices;
    private Set<Vertex> settled;
    private PriorityQueue<Vertex> pq;
    private Map<Vertex, Integer> dist;

    public void getShortestWayBetween(String value1, String value2) {
        //        fillForTest();
        //Map of all nodes with path for them
        dist = new LinkedHashMap<>();
        for (Vertex ver : adjacentVertices.keySet()) {
            dist.put(ver, Integer.MAX_VALUE);
        }
        dist.put(new Vertex(value1), 0);
        //adding source node into PriorityQueue
        pq = new PriorityQueue<>();
        pq.add(getEntry(adjacentVertices, new Vertex(value1)).getKey());
        //HashSet for settled nodes
        settled = new HashSet<>();
        //while haven't visited all nodes and pq isn't empty
        while (settled.size() != adjacentVertices.size() && !pq.isEmpty()) {
            //getting current node
            Vertex currentNode = pq.remove();
            //current node is visited, so adds it into settled
            settled.add(currentNode);
            //checking all adjacency vertices
            ifThereAreShorterWays(getEntry(adjacentVertices, currentNode));
        }
        if (dist.get(new Vertex(value2)) == Integer.MAX_VALUE) {
            System.out.printf("There are no way from %s vertex to %s\n", value1, value2);
        } else {
            System.out.printf("Shortest distance from %s to %s = %d\n", value1, value2, dist.get(new Vertex(value2)));
        }
    }

    private void ifThereAreShorterWays(Map.Entry<Vertex, List<Vertex>> vertexEntry) {
        int edgeDistance = -1;
        int newDistance = -1;
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

    public void getLongestWayBetween(String value1, String value2) {
        //fillForTest();
        //Map of all nodes with path for them
        dist = new LinkedHashMap<>();
        for (Vertex ver : adjacentVertices.keySet()) {
            dist.put(ver, Integer.MIN_VALUE);
        }
        dist.put(new Vertex(value1), 0);
        //adding source node into PriorityQueue
        pq = new PriorityQueue<>();
        pq.add(getEntry(adjacentVertices, new Vertex(value1)).getKey());
        //HashSet for settled nodes
        settled = new HashSet<>();
        //while haven't visited all nodes and pq isn't empty
        while (settled.size() != adjacentVertices.size() && !pq.isEmpty()) {
            //getting current node
            Vertex currentNode = pq.remove();
            //current node is visited, so adds it into settled
            settled.add(currentNode);
            //checking all adjacency vertices
            ifThereAreLongerWays(getEntry(adjacentVertices, currentNode));
        }
/*        if (dist.get(new Vertex(value2)) == Integer.MIN_VALUE) {
            System.out.printf("There are no way from %s vertex to %s\n", value1, value2);
        } else {
            System.out.printf("Longest distance from %s to %s = %d\n", value1, value2, dist.get(new Vertex(value2)));
        }*/
    }

    private void ifThereAreLongerWays(Map.Entry<Vertex, List<Vertex>> vertexEntry) {
        int edgeDistance = -1;
        int newDistance = -1;
        //checking all adjacency nodes (neighbours)
        for (Vertex neighbour : vertexEntry.getValue()) {
            //if node isn't visited
            if (!settled.contains(neighbour)) {
                edgeDistance = neighbour.getWeight();
                newDistance = dist.get(vertexEntry.getKey()) + edgeDistance;
                //if this way is shorter than the previous one
                if (newDistance > dist.get(neighbour)) {
                    dist.put(neighbour, newDistance);
                }
                pq.add(neighbour);
            }
        }
    }

    public void getGraphCenter() {
        fillForTest();
        Map<Vertex, Integer> excentrycityMap = new LinkedHashMap<>();
        for (Vertex ver : adjacentVertices.keySet()) {
            excentrycityMap.put(ver, 0);
        }
        for (Map.Entry<Vertex, List<Vertex>> external_entry : adjacentVertices.entrySet()) {
            for (Map.Entry<Vertex, List<Vertex>> inner_entry : adjacentVertices.entrySet()) {
                getShortestWayBetween(external_entry.getKey().getValue(), inner_entry.getKey().getValue());
                for (Map.Entry<Vertex, Integer> distance : dist.entrySet()) {
                    if (distance.getValue() != Integer.MAX_VALUE
                            && excentrycityMap.get(distance.getKey()) <= distance.getValue()) {
                        excentrycityMap.put(distance.getKey(), distance.getValue());
                    }
                }
            }
        }
        Vertex center = null;
        int minEx = Integer.MAX_VALUE;
        for (Map.Entry<Vertex, Integer> ex : excentrycityMap.entrySet()) {
            System.out.println(ex.getKey().getValue() + ex.getValue());
            if (minEx > ex.getValue()) {
                center = ex.getKey();
                minEx = ex.getValue();
            }
        }
        System.out.println(center.getValue() + minEx);
    }


    private static <K, V> Map.Entry<K, V> getEntry(Map<K, V> map, K key) {
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
