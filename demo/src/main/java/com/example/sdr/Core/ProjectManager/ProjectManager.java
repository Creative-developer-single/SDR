package com.example.sdr.Core.ProjectManager;

import com.example.sdr.Core.ProjectManager.Loader.GraphStructerLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class ProjectManager {
    //JSON path
    private String ProjectPropertiesJSONPath;
    private String GraphStructJSONPath;
    
    //Loader
    GraphStructerLoader loader;

    //LogicDirectedGraph
    LogicDirectedGraph graph;

    //LogicDirectedScheduler
    LogicGraphScheduler scheduler;

    //Simulator
    Simulator simulator;

    public ProjectManager(){
        loader = new GraphStructerLoader();
        graph = new LogicDirectedGraph();
        scheduler = new LogicGraphScheduler();
        simulator = new Simulator();
    }
}
