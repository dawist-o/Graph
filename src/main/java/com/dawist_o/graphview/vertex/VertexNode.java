package com.dawist_o.graphview.vertex;

import com.dawist_o.graphview.labels.LabelNode;
import com.dawist_o.graphview.labels.LabelledNode;
import com.dawist_o.graphview.style.StyleProxy;
import com.dawist_o.model.Vertex;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VertexNode<V> extends Circle implements LabelledNode {

    private final Set<VertexNode<V>> adjacentVertices;
    private final Vertex<V> vertexValue;

    private LabelNode labelNode = null;
    private boolean isDragging;

    private final StyleProxy styleProxy;

    public VertexNode(Vertex<V> v, double x, double y, double radius, boolean enableMove) {
        super(x, y, radius);
        this.isDragging = false;

        styleProxy = new StyleProxy(this);
        styleProxy.addStyleClass("vertex");

        this.adjacentVertices = new HashSet<>();
        this.vertexValue = v;
        if (enableMove) {
            enableDrag();
        }
    }

    public void addAdjacentVertices(VertexNode<V> vertexNode) {
        adjacentVertices.add(vertexNode);
    }

    private void enableDrag() {
        final PointVector dragDelta = new PointVector(0, 0);

        setOnMousePressed((MouseEvent mouseEvent) -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getCenterX() - mouseEvent.getX();
                dragDelta.y = getCenterY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
                isDragging = true;

                mouseEvent.consume();
            }

        });

        setOnMouseReleased((MouseEvent mouseEvent) -> {
            getScene().setCursor(Cursor.HAND);
            isDragging = false;

            mouseEvent.consume();
        });

        setOnMouseDragged((MouseEvent mouseEvent) -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                double newX = mouseEvent.getX() + dragDelta.x;
                double x = boundCenterCoordinate(newX, 0, getParent().getLayoutBounds().getWidth());
                setCenterX(x);

                double newY = mouseEvent.getY() + dragDelta.y;
                double y = boundCenterCoordinate(newY, 0, getParent().getLayoutBounds().getHeight());
                setCenterY(y);
                mouseEvent.consume();
            }

        });

        setOnMouseEntered((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        setOnMouseExited((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexNode<V> that = (VertexNode) o;
        return Objects.equals(vertexValue, that.vertexValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexValue);
    }

    //don't let vertex gets outside screen
    private double boundCenterCoordinate(double value, double min, double max) {
        double radius = getRadius();

        if (value < min + radius) {
            return min + radius;
        } else if (value > max - radius) {
            return max - radius;
        } else {
            return value;
        }
    }

    public void setPosition(double x, double y) {
        setCenterX(x);
        setCenterY(y);
    }

    public Vertex<V> getVertexValue() {
        return vertexValue;
    }

    @Override
    public void attachLabel(LabelNode node) {
        this.labelNode = node;
        labelNode.xProperty().bind(centerXProperty().subtract(labelNode.getLayoutBounds().getWidth() / 2.0));
        labelNode.yProperty().bind(centerYProperty().add(getRadius() + labelNode.getLayoutBounds().getHeight()));
    }

    @Override
    public LabelNode getLabel() {
        return labelNode;
    }

    private class PointVector {
        double x, y;

        public PointVector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
