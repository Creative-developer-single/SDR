package com.example.sdr.Core.ProjectManager.Simulation;

import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;

public class Simulator {
    public final int MAX_SIMULATION_CYCLES = 1000;
    public final int SIMLUATION_MODE_LIMITED_CYCLES = 0;
    public final int SIMLUATION_MODE_INTERACTIVE = 1;

    //Components
    private LogicGraphScheduler scheduler;
    private LogicDirectedGraph graph;

    //Simluation Mode
    private int simulationMode;
    private int simulationCycles;

    public Simulator(){
        scheduler = null;
        graph = null;
        simulationMode = SIMLUATION_MODE_LIMITED_CYCLES;
        simulationCycles = 1;
    }

    public void setSimluatorMode(int mode){
        simulationMode = mode;
    }

    /*
     * @brief Set the number of simulation cycles
     * @param cycles Number of simulation cycles
     */
    public void setSimulationCycles(int cycles){
        simulationCycles = cycles;
    }

    public void setLoicGraph(LogicDirectedGraph graph){
        this.graph = graph;
    }

    public void setScheduler(LogicGraphScheduler scheduler){
        this.scheduler = scheduler;
    }

    public void Simluation(){
        scheduler.addNodesToNodeQueue(graph.getNodes());
        scheduler.generateScheduleOrder();
        switch(simulationMode){
            case SIMLUATION_MODE_LIMITED_CYCLES:
                for(int i = 1; i <= simulationCycles; i++)
                {
                    scheduler.runTheScheduler();
                }
                break;
            default:
                break;
        }
        scheduler.printScheduleOrder();
        scheduler.printTerminalNodes();
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
