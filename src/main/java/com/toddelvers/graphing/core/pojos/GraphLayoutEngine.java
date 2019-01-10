package com.toddelvers.graphing.core.pojos;

public enum GraphLayoutEngine {
    DOT("dot"),        //filter for drawing directed graphs
    NEATO("neato"),        //filter for drawing undirected graphs
    CIRCO("circo"),        //filter for circular layout of graphs
    TWOPI("twopi"),        //filter for radial layouts of graphs
    FDP("fdp"),            //filter for drawing undirected graphs
    SFDP("sfdp");        //filter for drawing large undirected graphs

    private String name;

    GraphLayoutEngine(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
