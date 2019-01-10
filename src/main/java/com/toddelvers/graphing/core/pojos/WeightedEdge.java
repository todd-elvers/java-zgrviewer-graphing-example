package com.toddelvers.graphing.core.pojos;

public class WeightedEdge extends org.jgrapht.graph.DefaultEdge implements Comparable<WeightedEdge> {
    private static final long serialVersionUID = 229708706467350994L;
    private int weight;

    public WeightedEdge(final int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedEdge o) {
        return this.getWeight() - o.getWeight();
    }
}
