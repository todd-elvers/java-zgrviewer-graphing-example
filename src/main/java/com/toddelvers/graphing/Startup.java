package com.toddelvers.graphing;

import com.toddelvers.graphing.core.GraphGenerator;
import com.toddelvers.graphing.core.file.GraphFileWriter;
import com.toddelvers.graphing.core.file.ZGRViewerWrapper;
import com.toddelvers.graphing.core.pojos.GraphData;
import com.toddelvers.graphing.core.pojos.GraphLayoutEngine;
import com.toddelvers.graphing.core.pojos.WeightedEdge;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.io.File;
import java.io.IOException;

public class Startup {
    private static final File GRAPHS_FOLDER_ON_DESKTOP = new File(System.getenv("USERPROFILE"), "Desktop/graphs");

    public static void main(String[] args) throws IOException {
        DefaultDirectedWeightedGraph<String, WeightedEdge> graph = new GraphGenerator().generateGraph(GraphData.generateTestGraphData());

        File graphFile = new GraphFileWriter()
                .usingLayoutEngine(GraphLayoutEngine.CIRCO)            // Change this to try different graph node display layouts
                .usingDestinationDirectory(GRAPHS_FOLDER_ON_DESKTOP)
                .writeGraphToFile(graph);

        new ZGRViewerWrapper().visualizeWithZGRViewer(graphFile);
    }

}
