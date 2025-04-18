package com.example.sdr.Debug.JsonLoader;

import com.example.sdr.Core.Components.Tools.GeneralResourceFinder;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Loader.LogicGraphLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class LogicGraphJsonLoaderMain {

    public static void main(String[] args){
        /* 
        //Load the JSON File
        GeneralResourceFinder finder = new GeneralResourceFinder();
        String jsonPath = finder.getFilePath("/LogicGraph/JSON/TestGraph1.json");
        //Create the LogicDirectedGraph
        LogicGraphManager manager = new LogicGraphManager();
        manager.loadGraphFromJSON(jsonPath);

        //Print the Graph
        manager.getGraphInstance().PrintEdges();
        manager.getGraphInstance().PrintNodes();

        //Generate the Simulator
        Simulator simulator = new Simulator();

        //Bind the Simulator with graph and scheduler
        simulator.setLogicGraphManager(manager);

        //Run the Simulator
        simulator.Simluation();
        */
    }   
}
