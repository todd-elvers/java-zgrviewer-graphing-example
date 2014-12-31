package com.toddelvers.graphing.core.file;

import com.toddelvers.graphing.core.data.GraphDataMapper;
import com.toddelvers.graphing.core.pojos.GraphLayoutEngine;
import com.toddelvers.graphing.core.pojos.WeightedEdge;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.io.*;

public class GraphFileWriter {
	private static Logger log = Logger.getLogger(GraphFileWriter.class);

	private GraphDataMapper graphDataMapper = new GraphDataMapper();
	private File destinationDirectory = new File(System.getenv("TEMP"));		// Default - overwrite with .usingDestinationDirectory()
	private GraphLayoutEngine graphLayoutEngine = GraphLayoutEngine.SFDP;		// Default - overwrite with .usingLayoutEngine()

	public GraphFileWriter usingLayoutEngine(GraphLayoutEngine graphLayoutEngine) {
		this.graphLayoutEngine = graphLayoutEngine;
		return this;
	}

	public GraphFileWriter usingDestinationDirectory(File destinationDirectory) throws IOException {
		destinationDirectory.mkdir();
		if (!destinationDirectory.isDirectory()) {
			throw new IOException("Location to copy graph to must be a directory.");
		} else {
			this.destinationDirectory = destinationDirectory;
		}
		
		return this;
	}

	public File writeGraphToFile(DefaultDirectedWeightedGraph<String, WeightedEdge> graph) throws Exception {
		// To be able to customize how the graph is displayed,
		// we must first apply customizations to the graph's DOT format
		File dotTempFile = createDotFile(graph);

		// Then convert the DOT format and it's customizations to SVG
		// (SVG is better at visualizing/organizing large groups of data)
		File svgTempFile = convertDotToSvg(dotTempFile);

		return svgTempFile;
	}

	private File createDotFile(DefaultDirectedWeightedGraph<String, WeightedEdge> graph) throws IOException {
		String filename = "Graph_" + RandomStringUtils.randomAlphanumeric(4) + ".dot";
		File dotFile = new File(destinationDirectory, filename);

		// Write graph contents to DOT file
		Writer writer = new BufferedWriter(new FileWriter(dotFile));
		DOTExporter<String, WeightedEdge> dotExporter = graphDataMapper.getContentMapper();
		dotExporter.export(writer, graph);
		writer.close();

		log.info("DOT file written to [" + dotFile.getAbsolutePath() + "]");
		return dotFile;
	}

	private File convertDotToSvg(File dotFile) throws IOException {
		File svgFile = new File(dotFile.getParentFile(), dotFile.getName().replace(".dot", ".svg"));
		String dotExePath = GraphFileLocator.determineAbsoluteFilePathFor("ZGRViewer/target/WinGraphviz/bin/dot.exe");

		log.debug("File path for dot.exe: [" + dotExePath + "]");
		log.debug("SVG graph display format: [" + graphLayoutEngine + "]");

		String command = String.format("%s -T svg -K %s -o %s %s", dotExePath, graphLayoutEngine, svgFile.getAbsolutePath(), dotFile.getAbsolutePath());
		Runtime.getRuntime().exec(command);

		log.info("DOT file converted to SVG file at [" + svgFile.getAbsolutePath() + "]");
		return svgFile;
	}

}
