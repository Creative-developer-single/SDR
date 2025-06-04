package com.example.sdr.Core.ProjectManager.Simulation;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;
import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;
import com.example.sdr.Core.ProjectManager.Simulation.Processer.SimulationProcesser;

public class Simulator {
    public final int MAX_SIMULATION_CYCLES = 1000;
    public final int SIMLUATION_MODE_LIMITED_CYCLES = 0;
    public final int SIMLUATION_MODE_INTERACTIVE = 1;

    //Instance
    private ProjectManager manager;

    //Properties
    private SimulationProperties simulationProperties;

    //Processer
    private SimulationProcesser processer;

    //LifeCycle
    // Available status: Stopped, Starting,Running, Paused, Stopping
    private String simulationStatus = "Stopped";

    public Simulator(){
        manager = null;
    }

    public Simulator(ProjectManager manager){
        this.manager = manager;
        this.simulationProperties = manager.getProjectPropertiesManager().simulationProperties;
        this.processer = new SimulationProcesser(this, simulationProperties);
    }

    public ProjectManager getProjectManager(){
        return manager;
    }

    public String getSimulationStatus(){
        return simulationStatus;
    }

    /*
     * @brief Get the Simulation Properties
     * @param None
     * @return SimulationProperties
     */
    public SimulationProperties getSimulationProperties(){
        return simulationProperties;
    }

    /*
     * @brief Reset the simulation
     * @param None
     */
    public void resetSimulation(){
        // Reset the variables
        simulationProperties.currentCycle = 0;

        LogicGraphManager logicGraphManager = manager.getLogicGraphManager();
        logicGraphManager.clearScheduler();
    }

    /*
     * @brief Start the simulation
     * @param None
     */
    public void startSimulation(){
        LogicGraphManager logicGraphManager = manager.getLogicGraphManager();
        logicGraphManager.createScheduler();
        simulationStatus = "Running";
     }

    public void runSimulation(){
        if(simulationProperties.currentCycle < simulationProperties.simulationCycle){
            processer.handleSimulation();
        }else{
            simulationStatus = "Stopped";
        }
    }

    public void stopSimulation(){
        LogicGraphManager logicGraphManager = manager.getLogicGraphManager();
        logicGraphManager.clearScheduler();
        simulationStatus = "Stopped";
    } 

    public void Simluation(){
        LogicGraphManager logicGraphManager = manager.getLogicGraphManager();
        logicGraphManager.createScheduler();
        switch(simulationProperties.simulationMode){
            case "Static":
                for(int i = 1; i <= simulationProperties.simulationCycle; i++)
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
        logicGraphManager.getSchedulerInstance().printAllBufferNodes();
    }

    public void DebugSimulation(){
        Simluation();
    }
    
}
