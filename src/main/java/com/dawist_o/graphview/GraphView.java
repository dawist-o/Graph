package com.dawist_o.graphview;

import com.dawist_o.graphview.edge.CurveEdge;
import com.dawist_o.graphview.edge.EdgeArrow;
import com.dawist_o.graphview.edge.GraphEdge;
import com.dawist_o.graphview.edge.LineEdge;
import com.dawist_o.graphview.labels.LabelNode;
import com.dawist_o.graphview.placementstrategies.PlacementStrategy;
import com.dawist_o.graphview.placementstrategies.RandomPlacementStrategy;
import com.dawist_o.graphview.vertex.VertexNode;
import com.dawist_o.model.Edge;
import com.dawist_o.model.Graph;
import com.dawist_o.model.Vertex;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;


public class GraphView<V, E> extends Pane {
    private final Graph<V, E> graph;
    private final PlacementStrategy<V, E> strategy;
    private final Map<Vertex<V>, VertexNode<V>> vertices;
    private final Map<Edge<E, V>, GraphEdge<E, V>> edges;
    private final Map<Pair<VertexNode<V>, VertexNode<V>>, Integer> pairs;

    public Graph<V, E> getGraph() {
        return graph;
    }

    public GraphView(Graph<V, E> g, PlacementStrategy<V, E> strategy) {
        this.graph = g;
        this.strategy = strategy;
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        pairs = new HashMap<>();
        loadStylesheet(null);
        initNodes();
    }

    private void initNodes() {
        if (graph == null || graph.verticesCount() == 0)
            return;
        //putting all vertices into vertexNodes
        for (Vertex<V> vertex : graph.vertices()) {
            VertexNode<V> vertexNode = new VertexNode<>(vertex, 0, 0, 15, true);
            addVertex(vertexNode);
        }
        //creates edges between vertices
        for (Vertex<V> outboundVertex : vertices.keySet()) {
            Iterable<Edge<E, V>> outboundEdges = graph.outboundEdges(outboundVertex);
            for (Edge<E, V> outEdge : outboundEdges) {
                VertexNode<V> inboundVertex = vertices.get(outEdge.vertices().get(1));
                GraphEdge<E, V> graphEdge = createEdge(outEdge, inboundVertex, vertices.get(outboundVertex));
                addEdge(graphEdge, outEdge);
            }
        }
    }

    private void addVertex(VertexNode<V> vertexNode) {
        vertices.put(vertexNode.getVertexValue(), vertexNode);
        LabelNode labelNode = new LabelNode(vertexNode.getVertexValue().element().toString());
        vertexNode.attachLabel(labelNode);
        this.getChildren().addAll(vertexNode, labelNode);
    }

    private GraphEdge<E, V> createEdge(Edge<E, V> outEdge, VertexNode<V> in, VertexNode<V> out) {
        int edgesBetween = 0;
        Integer count = pairs.get(new Pair<>(in, out));
        if (count != null)
            edgesBetween = count;

        GraphEdge<E, V> edge;
        if (in == out || getTotalEdgesBetween(in.getVertexValue(), out.getVertexValue()) > 1)
            edge = new CurveEdge<>(outEdge, in, out, edgesBetween);
        else
            edge = new LineEdge<>(outEdge, in, out);

        pairs.put(new Pair<>(in, out), ++edgesBetween);
        return edge;
    }

    private void addEdge(GraphEdge<E, V> gEdge, Edge<E, V> edge) {
        String edgeValue = String.valueOf(edge.element());
        Tooltip t = new Tooltip(edgeValue);
        t.setShowDelay(new Duration(2));
        Tooltip.install((Node) gEdge, t);

        LabelNode label = new LabelNode(String.valueOf(edge.element()));
        gEdge.attachLabel(label);
        this.getChildren().add(label);

        EdgeArrow arrow = new EdgeArrow();
        gEdge.attachArrow(arrow);
        this.getChildren().add(arrow);
        edges.put(edge, gEdge);
        this.getChildren().add(0, (Node) gEdge);
    }

    private int getTotalEdgesBetween(Vertex<V> v, Vertex<V> u) {
        int count = 0;
        for (Edge<E, V> edge : graph.edges()) {
            if (edge.vertices().get(0) == v && edge.vertices().get(1) == u
                    || edge.vertices().get(0) == u && edge.vertices().get(1) == v) {
                count++;
            }
        }
        return count;
    }

    public void init() {
        if (strategy != null) {
            strategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.graph, this.vertices.values());
        } else {
            new RandomPlacementStrategy<V, E>()
                    .place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.graph, this.vertices.values());
        }
    }

    public synchronized void update() {
        Platform.runLater(() -> {
            removeNodes();
            insertNodes();
            updateCenterVertex();
        });
    }

    private void updateCenterVertex() {
        if(graph.verticesCount()==0) return;

        vertices.values().forEach(v->v.setStyleClass("vertex"));

        Vertex<V> center = graph.getGraphCenter();
        if(center!=null)
            vertices.get(center).setStyleClass("center-vertex");
    }

    private Bounds getPlacementBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE,
                maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        if (vertices.size() == 0) return new BoundingBox(0, 0, getWidth(), getHeight());

        for (VertexNode<V> v : vertices.values()) {
            minX = Math.min(minX, v.getCenterX());
            minY = Math.min(minY, v.getCenterY());
            maxX = Math.max(maxX, v.getCenterX());
            maxY = Math.max(maxY, v.getCenterY());
        }

        return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }

    private void insertNodes() {
        Collection<Vertex<V>> unplottedVertices = unplottedVertices();
        List<VertexNode<V>> newVertices = null;

        Bounds placementBounds = getPlacementBounds();
        double mx = placementBounds.getMinX() + placementBounds.getWidth() / 2.0;
        double my = placementBounds.getMinY() + placementBounds.getHeight() / 2.0;

        if (!unplottedVertices.isEmpty()) {
            newVertices = new LinkedList<>();

            for (Vertex<V> v : unplottedVertices) {
                double x, y;
                Collection<Edge<E, V>> incidentEdges = graph.incidentEdges(v);

                if (incidentEdges.isEmpty()) {
                    x = mx;
                    y = my;
                } else {
                    x = 100;
                    y = 100;
                }
                VertexNode<V> newVertex = new VertexNode<>(v, x, y, 15, true);
                newVertices.add(newVertex);
                vertices.put(v, newVertex);
            }
        }

        Collection<Edge<E, V>> unplottedEdges = unplottedEdges();
        if (!unplottedEdges.isEmpty()) {
            for (Edge<E, V> edge : unplottedEdges) {

                List<Vertex<V>> edgeVertices = edge.vertices();
                Vertex<V> u = edgeVertices.get(0); //outbound
                Vertex<V> v = edgeVertices.get(1); //inbound

                VertexNode<V> graphVertexOut = vertices.get(u);
                VertexNode<V> graphVertexIn = vertices.get(v);

                if (graphVertexIn == null || graphVertexOut == null) {
                    continue;
                }

                GraphEdge<E, V> graphEdge = createEdge(edge, graphVertexIn, graphVertexOut);

                EdgeArrow arrow = new EdgeArrow();
                graphEdge.attachArrow(arrow);
                this.getChildren().add(arrow);

                addEdge(graphEdge, edge);
            }
        }

        if (newVertices != null) {
            for (VertexNode<V> node : newVertices) {
                addVertex(node);
            }
        }
    }

    private Collection<Edge<E, V>> unplottedEdges() {
        Collection<Edge<E, V>> unplotted = new LinkedList<>();
        for (Edge<E, V> e : graph.edges()) {
            if (!edges.containsKey(e))
                unplotted.add(e);
        }
        return unplotted;
    }

    private Collection<Vertex<V>> unplottedVertices() {
        Collection<Vertex<V>> unplotted = new LinkedList<>();
        for (Vertex<V> v : graph.vertices()) {
            if (!vertices.containsKey(v))
                unplotted.add(v);
        }
        return unplotted;
    }

    public void removeNodes() {
        //remove vertices and all edges contains them
        Collection<Vertex<V>> removedVertices = removedVertices();

        Set<VertexNode<V>> verticesToRemove = new HashSet<>();
        Set<GraphEdge<E, V>> edgesToRemove = new HashSet<>();

        for (Vertex<V> v : removedVertices) {
            for (GraphEdge<E, V> e : edges.values()) {
                if (e.getEdge().vertices().get(0) == v
                        || e.getEdge().vertices().get(1) == v)
                    edgesToRemove.add(e);
            }
            VertexNode<V> toRemove = vertices.get(v);
            verticesToRemove.add(toRemove);
        }

        for (GraphEdge<E, V> e : edgesToRemove) {
            edges.remove(e.getEdge());
            removeEdge(e);
        }

        for (VertexNode<V> v : verticesToRemove) {
            vertices.remove(v.getVertexValue());
            removeVertex(v);
        }

        Collection<Edge<E, V>> removedEdges = removedEdges();
        for (Edge<E, V> e : removedEdges) {
            GraphEdge<E, V> eNode = edges.get(e);
            edges.remove(e);
            removeEdge(eNode);
        }

    }

    private Collection<Edge<E, V>> removedEdges() {
        List<Edge<E, V>> removedE = new LinkedList<>();
        Collection<Edge<E, V>> gEdges = graph.edges();
        Collection<GraphEdge<E, V>> plotted = edges.values();

        for (GraphEdge<E, V> e : plotted) {
            if (!gEdges.contains(e.getEdge()))
                removedE.add(e.getEdge());
        }

        return removedE;
    }

    private void removeVertex(VertexNode<V> v) {
        this.getChildren().remove(v);
        LabelNode label = v.getLabel();
        if (label != null)
            this.getChildren().remove(label);
    }

    private void removeEdge(GraphEdge<E, V> e) {
        this.getChildren().remove(e);

        EdgeArrow attachedArrow = e.getAttachedArrow();
        if (attachedArrow != null) {
            this.getChildren().remove(attachedArrow);
        }

        LabelNode attachedLabel = e.getLabel();
        if (attachedLabel != null) {
            this.getChildren().remove(attachedLabel);
        }
    }


    private Collection<Vertex<V>> removedVertices() {
        List<Vertex<V>> removedV = new LinkedList<>();
        Collection<Vertex<V>> graphV = graph.vertices();
        Collection<VertexNode<V>> plotted = vertices.values();

        for (VertexNode<V> v : plotted) {
            if (!graphV.contains(v.getVertexValue()))
                removedV.add(v.getVertexValue());
        }

        return removedV;
    }

    private void loadStylesheet(URI cssFile) {
        try {
            String css;
            if (cssFile != null) {
                css = cssFile.toURL().toExternalForm();
            } else {
                File f = new File("smartgraph.css");
                css = f.toURI().toURL().toExternalForm();
            }
            getStylesheets().add(css);
            this.getStyleClass().add("graph");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public void highlightPaths(V sourceV, V receiveV) {
        Platform.runLater(() -> {
            edges.values().forEach(edgeNode -> {
                edgeNode.setStyleClass("edge");
                edgeNode.getAttachedArrow().setStyleClass("arrow");
            });
            List<List<Edge<E, V>>> allPaths = graph.getAllPaths(sourceV, receiveV);
            System.out.println(allPaths);
            if (allPaths.size() != 0) {
                if (allPaths.size() == 2) {
                    for (Edge<E, V> edge : allPaths.get(1)) {
                        edges.get(edge).setStyle("-fx-stroke: #EA2B1F");//longest
                        edges.get(edge).getAttachedArrow().setStyle("-fx-stroke: #EA2B1F");
                    }
                } else if (allPaths.size() > 2) {
                    for (int i = 1; i <= allPaths.size() - 2; i++) {
                        for (Edge<E, V> edge : allPaths.get(i)) {
                            edges.get(edge).setStyle("-fx-stroke: #ffe05d");//default
                            edges.get(edge).getAttachedArrow().setStyle("-fx-stroke: #ffe05d");
                        }
                    }
                    for (Edge<E, V> edge : allPaths.get(allPaths.size() - 1)) {
                        edges.get(edge).setStyle("-fx-stroke: #EA2B1F");//longest
                        edges.get(edge).getAttachedArrow().setStyle("-fx-stroke: #EA2B1F");
                    }
                }
                for (Edge<E, V> edge : allPaths.get(0)) {
                    edges.get(edge).setStyle("-fx-stroke: #32e0c4");//shortest
                    edges.get(edge).getAttachedArrow().setStyle("-fx-stroke: #32e0c4");
                }
            }
        });
    }
}
