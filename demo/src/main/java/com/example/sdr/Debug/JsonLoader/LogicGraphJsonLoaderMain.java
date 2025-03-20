package com.example.sdr.Debug.JsonLoader;

import com.example.sdr.Core.Components.Tools.GeneralResourceFinder;
import com.example.sdr.Core.ProjectManager.Loader.GraphStructerLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

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

        //Generate the scheduler
        LogicGraphScheduler scheduler = new LogicGraphScheduler();
        
        //Generate the Simulator
        Simulator simulator = new Simulator();

        //Bind the Simulator with graph and scheduler
        simulator.setLoicGraph(graph);
        simulator.setScheduler(scheduler);

        //Run the Simulator
        simulator.Simluation();
    }   
}
