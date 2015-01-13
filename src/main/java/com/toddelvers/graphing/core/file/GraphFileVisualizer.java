package com.toddelvers.graphing.core.file;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GraphFileVisualizer {
	private static final Logger log = Logger.getLogger(GraphFileVisualizer.class);

	public void visualizeWithZGRViewer(File graphFile) throws IOException {
		writeZGRViewerConfigFile();
		openWithZGRViewer(graphFile);
	}

	// This manually writes ZGRViewer's configuration file so you don't have to use the GUI to do it
	private void writeZGRViewerConfigFile() throws IOException {
		File configFile = new File(System.getenv("USERPROFILE"), ".zgrviewer");
		FileUtils.writeStringToFile(configFile, generateZGRViewerConfigFileContents(), false);
		
		log.debug("ZGRViewer config file written to [" + configFile.getAbsolutePath() +"]");
	}

	public static String generateZGRViewerConfigFileContents() throws FileNotFoundException {
		String winGraphVizBinDir = GraphFileLocator.determineAbsoluteFilePathFor("ZGRViewer/target/WinGraphviz/bin");
		return new StringBuilder()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
				.append("<zgrv:config xmlns:zgrv=\"http://zvtm.sourceforge.net/zgrviewer\">\n")
				.append("\t<zgrv:directories>\n")
				.append("\t\t<zgrv:tmpDir value=\"true\">").append(System.getenv("TEMP")).append("</zgrv:tmpDir>\n")
				.append("\t\t<zgrv:graphDir>graphs</zgrv:graphDir>\n")
				.append("\t\t<zgrv:dot>").append(winGraphVizBinDir).append("\\dot.exe</zgrv:dot>\n")
				.append("\t\t<zgrv:neato>").append(winGraphVizBinDir).append("\\neato.exe</zgrv:neato>\n")
				.append("\t\t<zgrv:circo>").append(winGraphVizBinDir).append("\\circo.exe</zgrv:circo>\n")
				.append("\t\t<zgrv:twopi>").append(winGraphVizBinDir).append("\\twopi.exe</zgrv:twopi>\n")
				.append("\t\t<zgrv:graphvizFontDir/>\n")
				.append("\t</zgrv:directories>\n")
				.append("\t<zgrv:webBrowser autoDetect=\"true\" options=\"\" path=\"\"/>\n")
				.append("\t<zgrv:proxy enable=\"false\" host=\"\" port=\"80\"/>\n")
				.append("\t<zgrv:preferences antialiasing=\"true\" cmdL_options=\"\"")
				.append("\t\tdynaspot=\"false\" highlightColor=\"-65536\" magFactor=\"2.0\"")
				.append("\t\tsaveWindowLayout=\"false\" sdZoom=\"false\" sdZoomFactor=\"2\" silent=\"true\"/>\n")
				.append("\t<zgrv:plugins/>\n")
				.append("\t<zgrv:commandLines/>\n")
				.append("</zgrv:config>")
				.toString();
	}

	private void openWithZGRViewer(File graphFile) throws IOException {
		log.info("Opening [" + graphFile.getAbsolutePath() + "] with ZGRViewer.");
		log.info("(Note: If ZGRViewer opens to a blank window, simply run the application again.)");
		
		String zgrviewerJarPath = GraphFileLocator.determineAbsoluteFilePathFor("ZGRViewer/target/zgrviewer-0.8.2.jar");
		String command = String.format("java -jar %s %s", zgrviewerJarPath, graphFile.getAbsolutePath());
		Runtime.getRuntime().exec(command);
	}
}
