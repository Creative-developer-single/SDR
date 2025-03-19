package com.example.sdr.Debug.JsonLoader;

import com.example.sdr.Core.Components.Tools.GeneralResourceFinder;
import com.example.sdr.Core.ProjectManager.Loader.GraphStructerLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;

public class LogicGraphJsonLoaderMain {

    public static void main(String[] args){
        //Load the JSON File
        GeneralResourceFinder finder = new GeneralResourceFinder();
        String jsonPath = finder.getFilePath("/LogicGraph/JSON/TestGraph1.json");
        //Create the LogicDirectedGraph
        LogicDirectedGraph graph = new LogicDirectedGraph();

        //Create the GraphStructerLoader
        GraphStructerLoader loader = new GraphStructerLoader(jsonPath);
        loader.setLogicDirectedGraph(graph);
        loader.LoadFromJSON();

        //Print the Graph
        graph.PrintNodes();
        graph.PrintEdges();
    }   
}
