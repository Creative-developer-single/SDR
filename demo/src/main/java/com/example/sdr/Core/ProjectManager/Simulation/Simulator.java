package com.example.sdr.Core.ProjectManager.Simulation;

import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;

public class Simulator {
    public final int MAX_SIMULATION_CYCLES = 1000;
    public final int SIMLUATION_MODE_LIMITED_CYCLES = 0;
    public final int SIMLUATION_MODE_INTERACTIVE = 1;

    private LogicGraphManager manager;

    //Simluation Mode
    private int simulationMode;
    private int simulationCycles;

    public Simulator(){
        manager = null;
        simulationMode = SIMLUATION_MODE_LIMITED_CYCLES;
        simulationCycles = 1;
    }

    public void setLogicGraphManager(LogicGraphManager manager){
        this.manager = manager;
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

    public void Simluation(){
        manager.createScheduler();
        switch(simulationMode){
            case SIMLUATION_MODE_LIMITED_CYCLES:
                for(int i = 1; i <= simulationCycles; i++)
                {
                    manager.runScheduler();
                }
                break;
            default:
                break;
        }
        manager.updateReporter();
        manager.getReportInstance().printReportedNodes();
        manager.getReportInstance().printReportedEdges();
    }

    public void DebugSimulation(){
        Simluation();
    }
    
}
