package com.dawist_o.graphview.edge;

import com.dawist_o.graphview.labels.LabelNode;
import com.dawist_o.graphview.placementutilities.PlacementManager;
import com.dawist_o.graphview.style.StyleProxy;
import com.dawist_o.graphview.vertex.VertexNode;
import com.dawist_o.model.Edge;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CurveEdge<E,V> extends CubicCurve implements GraphEdge<E,V> {


    private final VertexNode<V> inVertex;
    private final VertexNode<V> outVertex;
    private final StyleProxy styleProxy;
    private EdgeArrow arrow;
    private LabelNode labelNode;
    private final Edge<E,V> edge;

    private final int MAX_ANGLE=20;
    private double randomAngleFactor =0;

    public CurveEdge(Edge<E,V> edge, VertexNode<V> inVertex, VertexNode<V> outVertex) {
        this(edge,inVertex,outVertex,0);
    }

    public CurveEdge(Edge<E,V> edge,VertexNode<V> inVertex, VertexNode<V> outVertex, int edgesBetween) {
        this.inVertex = inVertex;
        this.outVertex = outVertex;
        this.edge=edge;

        styleProxy = new StyleProxy(this);
        styleProxy.addStyleClass("edge");

        this.startXProperty().bind(outVertex.centerXProperty());
        this.startYProperty().bind(outVertex.centerYProperty());
        this.endXProperty().bind(inVertex.centerXProperty());
        this.endYProperty().bind(inVertex.centerYProperty());

        randomAngleFactor = edgesBetween==0 ? 0 : 1.0 / edgesBetween ;
        addListeners();
        update();
    }

    private void update(){
        //self loop
        if(inVertex==outVertex){
            double midpointX1 = outVertex.getCenterX() - inVertex.getRadius() * 5;
            double midpointY1 = outVertex.getCenterY() - inVertex.getRadius() * 2;

            double midpointX2 = outVertex.getCenterX() + inVertex.getRadius() * 5;
            double midpointY2 = outVertex.getCenterY() - inVertex.getRadius() * 2;

            setControlX1(midpointX1);
            setControlY1(midpointY1);
            setControlX2(midpointX2);
            setControlY2(midpointY2);
        }else{
            double midpointX = (outVertex.getCenterX() + inVertex.getCenterX()) / 2;
            double midpointY = (outVertex.getCenterY() + inVertex.getCenterY()) / 2;

            Point2D midpoint = new Point2D(midpointX, midpointY);

            Point2D startpoint = new Point2D(inVertex.getCenterX(), inVertex.getCenterY());
            Point2D endpoint = new Point2D(outVertex.getCenterX(), outVertex.getCenterY());

            double angle = MAX_ANGLE;

            double distance = startpoint.distance(endpoint);

            angle = angle - (distance / 1500 * angle);

            midpoint = PlacementManager.rotate(midpoint, startpoint,
                    (-angle) + randomAngleFactor * (angle - (-angle)));

            setControlX1(midpoint.getX());
            setControlY1(midpoint.getY());
            setControlX2(midpoint.getX());
            setControlY2(midpoint.getY());

        }
    }

    private void addListeners() {
        this.startXProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> update());
        this.startYProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> update());
        this.endXProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> update());
        this.endYProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> update());
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
                PlacementManager.atan2(endYProperty().subtract(controlY2Property()),
                        endXProperty().subtract(controlX2Property()))
        ));

        arrow.getTransforms().add(rotation);

        /* add translation transform to put the arrow touching the circle's bounds */
        Translate t = new Translate(-outVertex.getRadius(), 0);
        arrow.getTransforms().add(t);
    }

    @Override
    public EdgeArrow getAttachedArrow() {
        return this.arrow;
    }

    @Override
    public Edge<E,V> getEdge() {
        return this.edge;
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
        label.xProperty().bind(controlX1Property().add(controlX2Property()).divide(2).subtract(label.getLayoutBounds().getWidth() / 2));
        label.yProperty().bind(controlY1Property().add(controlY2Property()).divide(2).add(label.getLayoutBounds().getHeight() / 2));
    }

    @Override
    public LabelNode getLabel() {
        return labelNode;
    }
}
