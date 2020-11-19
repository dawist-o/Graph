package com.dawist_o.graphview.placementutilities;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Point2D;

import static javafx.beans.binding.Bindings.createDoubleBinding;

public class PlacementManager {
    public static Point2D rotate(Point2D point, Point2D center, double degree) {
        double angle = Math.toRadians(degree);

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        Point2D result = point.subtract(center);

        Point2D rotated = new Point2D(result.getX() * cos - result.getY() * sin,
                result.getX() * sin + result.getY() * cos);

        return rotated.add(center);
    }

    public static DoubleBinding atan2(final ObservableDoubleValue y, final ObservableDoubleValue x) {
        return createDoubleBinding(() -> Math.atan2(y.get(), x.get()), y, x);
    }

    public static DoubleBinding toDegrees(final ObservableDoubleValue angrad) {
        return createDoubleBinding(() -> Math.toDegrees(angrad.get()), angrad);
    }
}
