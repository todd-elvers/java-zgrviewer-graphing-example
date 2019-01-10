package com.toddelvers.graphing.core;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;

import com.toddelvers.graphing.core.pojos.WeightedEdge;

import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {
    private static Logger log = Logger.getLogger(GraphGenerator.class);
    private DefaultDirectedWeightedGraph<String, WeightedEdge> graph;

    public DefaultDirectedWeightedGraph<String, WeightedEdge> generateGraph(Multimap<String, String> map) {
        resetGraph();
        addUniqueVertices(map);
        addEdgesBetweenVertices(map);

        log.info("Graph created with [" + graph.vertexSet().size() + "] vertices and [" + graph.edgeSet().size() + "] edges.");
        return graph;
    }

    private void resetGraph() {
        graph = new DefaultDirectedWeightedGraph<String, WeightedEdge>(WeightedEdge.class);
    }

    private void addUniqueVertices(Multimap<String, String> map) {
        for (String uniqueStartingVertex : map.keySet()) {
            log.debug("Adding starting vertex: (" + uniqueStartingVertex + ")");
            graph.addVertex(uniqueStartingVertex);
        }

        final Set<String> uniqueEndingVertices = HashMultiset.create(map.values()).elementSet();
        for (String endingVertex : new ArrayList<String>(uniqueEndingVertices)) {
            log.debug("Adding ending vertex: (" + endingVertex + ")");
            graph.addVertex(endingVertex);
        }
    }

    private void addEdgesBetweenVertices(Multimap<String, String> map) {
        for (String keyVertex : map.keySet()) {
            for (String valueVertex : map.get(keyVertex)) {
                log.debug("Adding edge: (" + keyVertex + ")-->(" + valueVertex + ")");

                // Generates a pseudo-random edge weight between 0-100
                final int edgeWeight = new Random().nextInt(100);
                graph.addEdge(keyVertex, valueVertex, new WeightedEdge(edgeWeight));
            }
        }
    }
}
