package com.dawist_o.model;

import java.util.Comparator;
import java.util.Objects;

public interface Vertex<V extends Comparable<V>> {
    V element();
}
