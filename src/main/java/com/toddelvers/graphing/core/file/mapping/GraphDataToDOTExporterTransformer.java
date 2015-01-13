package com.toddelvers.graphing.core.file.mapping;

import com.toddelvers.graphing.core.pojos.WeightedEdge;
import org.jgrapht.ext.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is where you edit how particular components of the graph are written to file,
 * which also dictates how those components will get read and visualized by ZGRViewer
 */
public class GraphDataToDOTExporterTransformer {
	private VertexNameProvider<String> vertexIdLabeler;
	private VertexNameProvider<String> vertexMapper;
	private EdgeNameProvider<WeightedEdge> edgeMapper;
	private ComponentAttributeProvider<String> vertexAttributeMapper;
	private ComponentAttributeProvider<WeightedEdge> edgeAttributeMapper;

	public DOTExporter<String, WeightedEdge> transform() {
		initVertexMappers();
		initEdgeMappers();

		return new DOTExporter<String, WeightedEdge>(
				vertexIdLabeler, 
				vertexMapper, 
				edgeMapper, 
				vertexAttributeMapper,
				edgeAttributeMapper
		);
	}

	private void initVertexMappers() {
		final List<String> lightBlueBoxVertices = Arrays.asList("Todd", "Bryce");
		final List<String> greenVertices = Arrays.asList("Video Games", "Home", "Location", "People");
		
		// Define how the vertices maintain uniqueness (here we simply give them a unique integer value)
		// (There are other implementations of VertexNameProvider one could implement here)
		vertexIdLabeler = new IntegerNameProvider<String>();
		
		// Define how a particular vertex is named
		vertexMapper = new VertexNameProvider<String>() {
			public String getVertexName(String vertex) {
				return vertex;
			}
		};
		
		// Define additional attributes to apply to each vertex
		vertexAttributeMapper = new ComponentAttributeProvider<String>() {
			public Map<String, String> getComponentAttributes(String vertex) {
				Map<String, String> attributes = new HashMap<String, String>();

				// If name of vertex is in lightBlueBoxVertices, make the vertex a blue box
				if (lightBlueBoxVertices.contains(vertex)) {
					attributes.put(",shape", "box");
					attributes.put(",color", "lightblue");
					attributes.put(",style", "filled");
				}
				
				// If name of vertex is in greenVertices, fill the inside with the color green
				if(greenVertices.contains(vertex)){
					attributes.put(",color", "green");
					attributes.put(",style", "filled");
				}

				return attributes;
			}
		};

	}
	
	private void initEdgeMappers() {
		// Define how a particular edge is named
		edgeMapper = new EdgeNameProvider<WeightedEdge>() {
			public String getEdgeName(WeightedEdge edge) {
				return String.valueOf(edge.getWeight());
			}
		};

		// Define additional attributes to apply to each edge
		edgeAttributeMapper = new ComponentAttributeProvider<WeightedEdge>() {
			public Map<String, String> getComponentAttributes(WeightedEdge edge) {
				Map<String, String> attributes = new HashMap<String, String>();
				
				// If the weight of the edge is 90 or more, make the edge red
				if (edge.getWeight() >= 90) {
					attributes.put(",color", "red");
				}

				return attributes;
			}
		};
	}
}
