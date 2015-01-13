package com.toddelvers.graphing.core.file;

import com.toddelvers.graphing.core.file.mapping.GraphDataToDOTExporterTransformer;
import com.toddelvers.graphing.core.pojos.GraphLayoutEngine;
import com.toddelvers.graphing.core.pojos.WeightedEdge;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.io.*;

public class GraphFileWriter {
	private static final Logger log = Logger.getLogger(GraphFileWriter.class);

	private GraphDataToDOTExporterTransformer graphDataToDOTExporterTransformer = new GraphDataToDOTExporterTransformer();
	private File destinationDirectory = new File(System.getenv("TEMP"));		// Default - overwrite with .usingDestinationDirectory()
	private GraphLayoutEngine graphLayoutEngine = GraphLayoutEngine.SFDP;		// Default - overwrite with .usingLayoutEngine()

    /**
     * Overrides the default graph layout engine (SFDP) with the given layout engine.
     *
     * @param graphLayoutEngine the graph layout engine to use in place of the SFDP default layout engine
     * @return the GraphFileWriter this graph layout engine was set to
     */
	public GraphFileWriter usingLayoutEngine(GraphLayoutEngine graphLayoutEngine) {
		this.graphLayoutEngine = graphLayoutEngine;
		return this;
	}

    /**
     * Overrides the default destination directory (system temp. directory) with the given directory.
     *
     * @param destinationDirectory the new directory to place all output files in
     * @return the GraphFileWriter this destination directory was set to
     * @throws IOException if the specified destination directory couldn't be created or already exists as a file
     */
	public GraphFileWriter usingDestinationDirectory(File destinationDirectory) throws IOException {
		destinationDirectory.mkdir();
		if (!destinationDirectory.isDirectory()) {
			throw new IOException("Location to copy graph to must be a directory.");
		} else {
			this.destinationDirectory = destinationDirectory;
		}
		
		return this;
	}

    /**
     * Writes a directed, weighted graph to an SVG file in the destination directory
     * with a randomly generated filename.
     *
     * <br />
     *
     * This is done by first writing a DOT file with attributes specified by the class's
     * GraphDataToDOTExporterMapper.  Then the DOT file is converted to an SVG file via a
     * command line call to 'dot.exe' in the ZGRViewer directory.  (The DOT files are
     * converted to SVG for better visualization algrothims for larger data sets).
     *
     * @param graph the graph to be written to file as an SVG file
     * @return a handle to the SVG file generated in the destination directory
     * @throws IOException if there is trouble writing the DOT file, reading the DOT file or writing the SVG file.
     */
	public File writeGraphToFile(DefaultDirectedWeightedGraph<String, WeightedEdge> graph) throws IOException {
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
		DOTExporter<String, WeightedEdge> dotExporter = graphDataToDOTExporterTransformer.transform();
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
