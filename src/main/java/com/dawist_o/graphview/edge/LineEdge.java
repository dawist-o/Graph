package com.dawist_o.graphview.edge;

import com.dawist_o.graphview.labels.LabelNode;
import com.dawist_o.graphview.labels.LabelledNode;
import com.dawist_o.graphview.placementutilities.PlacementManager;
import com.dawist_o.graphview.style.StyleProxy;
import com.dawist_o.graphview.vertex.VertexNode;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class LineEdge extends Line implements GraphEdge {

    private final VertexNode inVertex;
    private final VertexNode outVertex;
    private final StyleProxy styleProxy;
    private EdgeArrow arrow;
    private LabelNode labelNode;

    public LineEdge(VertexNode inVertex, VertexNode outVertex) {
        this.inVertex = inVertex;
        this.outVertex = outVertex;

        styleProxy = new StyleProxy(this);
        styleProxy.addStyleClass("edge");

        this.startXProperty().bind(outVertex.centerXProperty());
        this.startYProperty().bind(outVertex.centerYProperty());
        this.endXProperty().bind(inVertex.centerXProperty());
        this.endYProperty().bind(inVertex.centerYProperty());

    }


    @Override
    public void attachArrow(EdgeArrow arrow) {
        this.arrow = arrow;

        /* attach arrow to line's endpoint */
        arrow.translateXProperty().bind(endXProperty());
        arrow.translateYProperty().bind(endYProperty());

        /* rotate arrow around itself based on this line's angle */
        Rotate rotation = new Rotate();
        rotation.pivotXProperty().bind(translateXProperty());
        rotation.pivotYProperty().bind(translateYProperty());
        rotation.angleProperty().bind(PlacementManager.toDegrees(
                PlacementManager.atan2(endYProperty().subtract(startYProperty()),
                        endXProperty().subtract(startXProperty()))
        ));

        arrow.getTransforms().add(rotation);

        /* add translation transform to put the arrow touching the circle's bounds */
        Translate t = new Translate(-outVertex.getRadius(), 0);
        arrow.getTransforms().add(t);

    }

    @Override
    public EdgeArrow getAttachedArrow() {
        return arrow;
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }

    @Override
    public void attachLabel(LabelNode label) {
        this.labelNode = label;
        label.xProperty().bind(startXProperty().add(endXProperty()).divide(2).subtract(label.getLayoutBounds().getWidth() / 2));
        label.yProperty().bind(startYProperty().add(endYProperty()).divide(2).add(label.getLayoutBounds().getHeight() / 1.5));
    }

    @Override
    public LabelNode getLabel() {
        return this.labelNode;
    }
}
