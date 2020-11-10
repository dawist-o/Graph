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
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;


public class GraphView<V extends Comparable<V>, E> extends Pane {
    private final Graph<V, E> g;
    private final PlacementStrategy strategy;
    private Map<Vertex<V>, VertexNode> vertexNodes;
    private final Map<Vertex<V>, VertexNode> vertices;
    private final Map<Edge<E, V>, GraphEdge> edges;

    public GraphView(Graph<V, E> g, PlacementStrategy strategy) {
        this.g = g;
        this.strategy = strategy;
        this.vertexNodes = new HashMap<>();
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        loadStylesheet(null);
        initNodes();
    }

    private void initNodes() {
        if (g == null || g.verticesCount() == 0)
            return;
        //putting all vertices into vertexNodes
        for (Vertex<V> vertex : g.vertices()) {
            VertexNode vertexNode = new VertexNode(vertex, 0, 0, 15, true);
            vertexNodes.put(vertex, vertexNode);
            vertices.put(vertex, vertexNode);
            LabelNode labelNode = new LabelNode(vertex.element().toString());
            vertexNode.attachLabel(labelNode);
            this.getChildren().addAll(vertexNode, labelNode);
        }
        //creates edges between vertices
        for (Vertex<V> outboundVertex : vertices.keySet()) {
            Iterable<Edge<E, V>> outboundEdges = g.outboundEdges(outboundVertex);
            for (Edge<E, V> outEdge : outboundEdges) {
                VertexNode inboundVertex = vertices.get(outEdge.vertices().get(1));
                GraphEdge edge = createEdge(inboundVertex, vertices.get(outboundVertex)
                        , outEdge.element());
                this.getChildren().add(0, (Node) edge);
            }
        }
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


    private GraphEdge createEdge(VertexNode in, VertexNode out, E weight) {
        GraphEdge edge;
        if (in == out || getTotalEdgesBetween(in.getVertexValue(), out.getVertexValue()) > 1)
            edge = new CurveEdge(in, out);
        else
            edge = new LineEdge(in, out);

        String edgeValue = String.valueOf(weight);
        Tooltip t = new Tooltip(edgeValue);
        t.setShowDelay(new Duration(2));
        Tooltip.install((Node) edge, t);

        LabelNode label = new LabelNode(String.valueOf(weight));
        edge.attachLabel(label);
        this.getChildren().add(label);

        EdgeArrow arrow = new EdgeArrow();
        edge.attachArrow(arrow);
        this.getChildren().add(arrow);
        return edge;
    }

    public void init() {
        if (strategy != null) {
            strategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.g, this.vertices.values());
        } else {
            new RandomPlacementStrategy()
                    .place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.g, this.vertices.values());
        }
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
