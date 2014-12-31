package com.toddelvers.graphing;

import com.google.common.collect.Multimap;
import com.toddelvers.graphing.core.GraphGenerator;
import com.toddelvers.graphing.core.data.GraphDataGenerator;
import com.toddelvers.graphing.core.file.GraphFileVisualizer;
import com.toddelvers.graphing.core.file.GraphFileWriter;
import com.toddelvers.graphing.core.pojos.GraphLayoutEngine;
import com.toddelvers.graphing.core.pojos.WeightedEdge;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.io.File;

public class Startup {
    private static File GRAPHS_FOLDER_ON_DESKTOP = new File(System.getenv("USERPROFILE"), "Desktop/graphs");

    public static void main(String[] args) throws Exception {
        Multimap<String, String> dataToGraph = GraphDataGenerator.generateGraphData();
        DefaultDirectedWeightedGraph<String, WeightedEdge> graph = new GraphGenerator().generateGraph(dataToGraph);

        File graphFile = new GraphFileWriter()
                .usingLayoutEngine(GraphLayoutEngine.CIRCO)            // Change this to try different graph node display layouts
                .usingDestinationDirectory(GRAPHS_FOLDER_ON_DESKTOP)
                .writeGraphToFile(graph);

        new GraphFileVisualizer().visualizeWithZGRViewer(graphFile);
    }

}
