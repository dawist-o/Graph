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
    private final Graph<V, E> g;
    private final PlacementStrategy<V> strategy;
    private final Map<Vertex<V>, VertexNode<V>> vertices;
    private final Map<Edge<E, V>, GraphEdge> edges;
    private final Map<Pair<VertexNode<V>, VertexNode<V>>, Integer> pairs;

    public Graph<V, E> getG() {
        return g;
    }

    public GraphView(Graph<V, E> g, PlacementStrategy<V> strategy) {
        this.g = g;
        this.strategy = strategy;
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        pairs = new HashMap<>();
        loadStylesheet(null);
        initNodes();
      //  vertices.get(g.getGraphCenter()).setStyle("-fx-fill:#f5b461;" +
        //        "-fx-stroke:#ec524b");
    }

    public void initPaths(V source, V reciever) {
        for (Edge<E, V> edge : g.getAllPaths(source, reciever).get(0)) {
            edges.get(edge).setStyle("-fx-stroke:#fddb3a");
        }
    }

    private void initNodes() {
        if (g == null || g.verticesCount() == 0)
            return;
        //putting all vertices into vertexNodes
        for (Vertex<V> vertex : g.vertices()) {
            VertexNode<V> vertexNode = new VertexNode<>(vertex, 0, 0, 15, true);
            addVertex(vertexNode);
        }
        //creates edges between vertices
        for (Vertex<V> outboundVertex : vertices.keySet()) {
            Iterable<Edge<E, V>> outboundEdges = g.outboundEdges(outboundVertex);
            for (Edge<E, V> outEdge : outboundEdges) {
                VertexNode<V> inboundVertex = vertices.get(outEdge.vertices().get(1));
                GraphEdge graphEdge = createEdge(outEdge, inboundVertex, vertices.get(outboundVertex));
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

    private GraphEdge createEdge(Edge<E, V> outEdge, VertexNode<V> in, VertexNode<V> out) {
        int edgesBetween = 0;
        Integer count = pairs.get(new Pair<>(in, out));
        if (count != null)
            edgesBetween = count;

        GraphEdge edge;
        if (in == out || getTotalEdgesBetween(in.getVertexValue(), out.getVertexValue()) > 1)
            edge = new CurveEdge(outEdge, in, out, edgesBetween);
        else
            edge = new LineEdge(outEdge, in, out);

        pairs.put(new Pair<>(in, out), ++edgesBetween);
        return edge;
    }

    private void addEdge(GraphEdge gEdge, Edge<E, V> edge) {
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
        for (Edge<E, V> edge : g.edges()) {
            if (edge.vertices().get(0) == v && edge.vertices().get(1) == u
                    || edge.vertices().get(0) == u && edge.vertices().get(1) == v) {
                count++;
            }
        }
        return count;
    }

    public void init() {
        if (strategy != null) {
            strategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.g, this.vertices.values());
        } else {
            new RandomPlacementStrategy<V>()
                    .place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.g, this.vertices.values());
        }
    }

    public synchronized void update() {
        Platform.runLater(() -> {
            removeNodes();
            insertNodes();
            init();
        });
    }

    private void insertNodes() {
        Collection<Vertex<V>> unplotted = unplottedVertices();
        for (Vertex<V> un : unplotted) {
            VertexNode<V> newNode = new VertexNode<>(un, 100, 100, 15, true);
            addVertex(newNode);
        }
    }

    private Collection<Vertex<V>> unplottedVertices() {
        Collection<Vertex<V>> unplotted = new LinkedList<>();
        for (Vertex<V> v : g.vertices()) {
            if (!vertices.containsKey(v))
                unplotted.add(v);
        }
        return unplotted;
    }

    public void removeNodes() {
        Collection<Vertex<V>> removedVertices = removedVertices();
        Collection<GraphEdge> edgesValues = new LinkedList<>(edges.values());

        Set<VertexNode<V>> verticesToRemove = new HashSet<>();
        Set<GraphEdge> edgesToRemove = new HashSet<>();

        for (Vertex<V> v : removedVertices) {
            for (GraphEdge e : edgesValues) {
                if (e.getEdge().vertices().get(0) == v
                        || e.getEdge().vertices().get(1) == v)
                    edgesToRemove.add(e);
            }
            VertexNode<V> toRemove = vertices.get(v);
            verticesToRemove.add(toRemove);
        }

        for (GraphEdge e : edgesToRemove) {
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
        Collection<Edge<E, V>> gEdges = g.edges();
        Collection<GraphEdge> plotted = edges.values();

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

    private void removeEdge(GraphEdge e) {
        this.getChildren().remove((Node) e);

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
        Collection<Vertex<V>> graphV = g.vertices();
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
}
