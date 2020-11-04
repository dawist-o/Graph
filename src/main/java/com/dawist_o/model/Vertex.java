package com.dawist_o.model;

import java.util.Comparator;
import java.util.Objects;

public class Vertex implements Comparable<Vertex> {
    private String value;
    private int weight = 1;

    public String getValue() {
        return value;
    }

    public Vertex(String value) {
        this.value = value;
    }

    public Vertex(String value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "{" + value + "," + weight + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(value, vertex.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(this.weight, o.weight);
    }
}
