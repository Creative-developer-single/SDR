package com.example.sdr.Core.ProjectManager.LogicGraph;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.LogicGraph.Loader.LogicGraphLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Reporter.LogicGraphReporter;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;

public class LogicGraphManager {
    //Graph Structure
    private LogicGraphStructureManager graph;

    //Graph Scheduler
    private LogicGraphScheduler scheduler;

    //Graph Structer Loader
    private LogicGraphLoader loader;

    //Graph Reporter
    private LogicGraphReporter reporter;

    //Load the JSON Format Graph
    public void loadGraphFromJSON(String jsonPath){
        loader.setJSONPath(jsonPath);
        loader.LoadFromJSON();
    }

    public void loadGraphFromJSON(JSONObject object){
        loader.setJSONObject(object);
        loader.createFromJSONObject();
    }

    //Create the Scheduler
    public void createScheduler(){
        clearScheduler();
        scheduler.addNodesToNodeQueue(graph.getNodes());
        scheduler.generateScheduleOrder();
    }

    public void clearScheduler(){
        scheduler.clearScheduler();
        graph.resetNodes();
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


    public LogicGraphStructureManager getGraphInstance(){
        return graph;
    }

    public LogicGraphScheduler getSchedulerInstance(){
        return scheduler;
    }

    public LogicGraphReporter getReportInstance(){
        return reporter;
    }

    //Get the Reported Nodes and Edges
    public void getReportedNode(){
        reporter.getNodes();
    }

    public void getReportedEdges(){
        reporter.getEdges();
    }

    public LogicGraphManager(){
        graph = new LogicGraphStructureManager();
        scheduler = new LogicGraphScheduler(this);
        loader = new LogicGraphLoader(this);
        reporter = new LogicGraphReporter();
    }
}
