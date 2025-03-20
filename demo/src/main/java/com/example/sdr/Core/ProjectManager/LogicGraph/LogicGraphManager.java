package com.example.sdr.Core.ProjectManager.LogicGraph;

import com.example.sdr.Core.ProjectManager.Loader.GraphStructerLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Reporter.LogicGraphReporter;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;

public class LogicGraphManager {
    //Graph Structure
    private LogicDirectedGraph graph;

    //Graph Scheduler
    private LogicGraphScheduler scheduler;

    //Graph Structer Loader
    private GraphStructerLoader loader;

    //Graph Reporter
    private LogicGraphReporter reporter;

    //Load the JSON Format Graph
    public void loadGraphFromJSON(String jsonPath){
        loader.setJSONPath(jsonPath);
        loader.setLogicDirectedGraph(graph);
        loader.LoadFromJSON();
    }

    //Create the Scheduler
    public void createScheduler(){
        scheduler.addNodesToNodeQueue(graph.getNodes());
        scheduler.generateScheduleOrder();
    }

    //Run a Schedule Cycle
    public void runScheduler(){
        scheduler.runTheScheduler();
    }

    //Update the Reporter
    public void updateReporter(){
        reporter.setNodes(graph.getNodes());
        reporter.setEdges(graph.getEdges());
    }

    //Get the Reported Nodes and Edges
    public void getReportedNode(){
        reporter.getNodes();
    }

    public void getReportedEdges(){
        reporter.getEdges();
    }

    public LogicGraphManager(){
        graph = new LogicDirectedGraph();
        scheduler = new LogicGraphScheduler();
        loader = new GraphStructerLoader();
        reporter = new LogicGraphReporter();
    }
}
