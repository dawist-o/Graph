package com.dawist_o.model;

import java.util.List;

public interface Edge<E, V extends Comparable<V>> {
    E element();
    List<Vertex<V>> vertices();

}
