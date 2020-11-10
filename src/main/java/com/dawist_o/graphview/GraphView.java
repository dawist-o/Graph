package com.dawist_o.graphview;

import com.dawist_o.graphview.edge.CurveEdge;
import com.dawist_o.graphview.edge.EdgeArrow;
import com.dawist_o.graphview.edge.GraphEdge;
import com.dawist_o.graphview.edge.LineEdge;
import com.dawist_o.graphview.labels.LabelNode;
import com.dawist_o.graphview.labels.LabelledNode;
import com.dawist_o.graphview.placementstrategies.PlacementStrategy;
import com.dawist_o.graphview.placementstrategies.RandomPlacementStrategy;
import com.dawist_o.graphview.vertex.VertexNode;
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

public class GraphView extends Pane {
    private final Graph g;
    private final PlacementStrategy strategy;
    private Map<Vertex, VertexNode> vertexNodes;

    public GraphView(Graph g, PlacementStrategy strategy) {
        this.g = g;
        this.strategy = strategy;
        this.vertexNodes = new HashMap<>();
        loadStylesheet(null);
        initNodes();
    }

    private void initNodes() {
        if(g==null ||  g.getAdjacentVertices().isEmpty())
            return;
        //putting all vertices into vertexNodes
        for (Map.Entry<Vertex, List<Vertex>> entry : g.getAdjacentVertices().entrySet()) {
            VertexNode vertexNode = new VertexNode(entry.getKey(), 0, 0, 15, true);
            vertexNodes.put(entry.getKey(), vertexNode);
            LabelNode labelNode = new LabelNode(entry.getKey().getValue());
            vertexNode.attachLabel(labelNode);
            this.getChildren().addAll(vertexNode, labelNode);
        }
        //creates edges between vertices

        for (Map.Entry<Vertex, List<Vertex>> entry : g.getAdjacentVertices().entrySet()) {
            VertexNode outbound = vertexNodes.get(entry.getKey());
            for (Vertex inV : entry.getValue()) {
                VertexNode inbound = vertexNodes.get(inV);
                GraphEdge edge = createEdge(inbound, outbound, inV.getWeight());
                this.getChildren().add(0, (Node) edge);
            }
        }
    }

    private int getTotalEdgesBetween(VertexNode inNode, VertexNode outNode) {
        int count=0;
        Vertex in=inNode.getVertexValue();
        Vertex out=outNode.getVertexValue();
        for (Vertex v: g.getAdjacentVertices().get(out)){
            if(in.equals(v)){
                count++;
            }
        }
        return count;
    }

    private GraphEdge createEdge(VertexNode in, VertexNode out, int weight) {
        GraphEdge edge;
        if (in == out || getTotalEdgesBetween(in, out) > 1)
            edge = new CurveEdge(in, out);
        else
            edge = new LineEdge(in, out);

        String edgeValue = String.valueOf(weight);
        Tooltip t = new Tooltip(edgeValue);
        t.setShowDelay(new Duration(2));
        Tooltip.install((Node) edge, t);

        LabelNode label=new LabelNode(String.valueOf(weight));
        edge.attachLabel(label);
        this.getChildren().add(label);

        EdgeArrow arrow = new EdgeArrow();
        edge.attachArrow(arrow);
        this.getChildren().add(arrow);
        return edge;
    }

    public void init() {
        if (strategy != null) {
            strategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.g, this.vertexNodes.values());
        } else {
            new RandomPlacementStrategy()
                    .place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), this.g, this.vertexNodes.values());
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
            //     Logger.getLogger(SmartGraphPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
