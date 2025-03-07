package com.example.sdr.Core.ProjectManager.Simulation;

import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;

public class Simulator {
    private LogicGraphScheduler scheduler;
    private LogicDirectedGraph graph;

    public Simulator(){
        scheduler = null;
        graph = null;
    }

    public void setLoicGraph(LogicDirectedGraph graph){
        this.graph = graph;
    }

    public void setScheduler(LogicGraphScheduler scheduler){
        this.scheduler = scheduler;
    }

    public void DebugSimulation(){
        scheduler.addNodesToNodeQueue(graph.getNodes());
        scheduler.generateScheduleOrder();
        for(int i = 1; i <= 2; i++)
        {
            scheduler.runTheScheduler();
        }
        //scheduler.runTheScheduler();
        scheduler.printScheduleOrder();
        scheduler.printTerminalNodes();
    }
    
}
