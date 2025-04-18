package com.example.sdr.Core.ProjectManager.Simulation;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;
import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;

public class Simulator {
    public final int MAX_SIMULATION_CYCLES = 1000;
    public final int SIMLUATION_MODE_LIMITED_CYCLES = 0;
    public final int SIMLUATION_MODE_INTERACTIVE = 1;

    private ProjectManager manager;

    //Simluation Mode
    private String simulationMode;
    private int simulationCycles;

    //Simulation Time
    private double simulationTime;
    private double simulationSampleRate;
    private double simulationBlockLength;

    public Simulator(){
        manager = null;
        simulationMode = "LimitedCycles";
        simulationCycles = 1;
    }

    public Simulator(ProjectManager manager){
        this.manager = manager;
        simulationMode = "LimitedCycles";
        simulationCycles = 1;
    }

    public void UpdateState(){
        SimulationProperties simulationProperties = manager.getProjectPropertiesManager().simulationProperties;
        simulationMode = simulationProperties.simulationMode;
        simulationTime = simulationProperties.simulationTime;
    }

    public void setSimluatorMode(String mode){
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
        LogicGraphManager logicGraphManager = manager.getLogicGraphManager();
        logicGraphManager.createScheduler();
        switch(simulationMode){
            case "LimitedCycles":
                for(int i = 1; i <= simulationCycles; i++)
                {
                    logicGraphManager.runScheduler();
                }
                break;
            default:
                break;
        }
        logicGraphManager.updateReporter();
        logicGraphManager.getReportInstance().printReportedNodes();
        logicGraphManager.getReportInstance().printReportedEdges();
    }

    public void DebugSimulation(){
        Simluation();
    }
    
}
