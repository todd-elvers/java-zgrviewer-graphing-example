package com.toddelvers.graphing.core.pojos;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GraphData {

    public static Multimap<String, String> generateTestGraphData() {
        final Multimap<String, String> graphData = ArrayListMultimap.create();

        graphData.put("Video Games", "Company Of Heros 2");
        graphData.put("Video Games", "Chivalry");
        graphData.put("Video Games", "League of Legends");
        graphData.put("Video Games", "Heros Of Newerth");
        graphData.put("Video Games", "XCOM");

        graphData.put("Location", "Iowa City");
        graphData.put("Location", "Columbia");

        graphData.put("Work", "Software Developer");
        graphData.put("Work", "Acidemia");
        graphData.put("Work", "Sales");
        graphData.put("Work", "Construction");
        graphData.put("Work", "Secretary");

        graphData.put("People", "Bryce");
        graphData.put("People", "Todd");

        graphData.put("Bryce", "Software Developer");
        graphData.put("Bryce", "Company Of Heros 2");
        graphData.put("Bryce", "Iowa City");
        graphData.put("Bryce", "Chivalry");
        graphData.put("Bryce", "League of Legends");
        graphData.put("Bryce", "XCOM");

        graphData.put("Todd", "Columbia");
        graphData.put("Todd", "Chivalry");
        graphData.put("Todd", "Heros Of Newerth");
        graphData.put("Todd", "Company Of Heros 2");

        return graphData;
    }
}
