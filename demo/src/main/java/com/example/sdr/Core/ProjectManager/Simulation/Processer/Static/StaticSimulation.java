package com.example.sdr.Core.ProjectManager.Simulation.Processer.Static;

import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;
import com.example.sdr.Core.ProjectManager.Simulation.Processer.SimulationProcesser;

public class StaticSimulation {
    //Instance for Processor
    private SimulationProcesser processer;

    //Properties Instance
    private SimulationProperties properties;

    //Static Properties
    private int simulationCycle;
    private int currentCycle = 0;

    private void syncStatus(){
        simulationCycle = properties.simulationCycle;
        currentCycle = properties.currentCycle;
    }

    private void updateStatus(){
        properties.simulationCycle = simulationCycle;
        properties.currentCycle = currentCycle;
    }

    public void runSimulation(){
        syncStatus();
        if(currentCycle <= simulationCycle){
            processer.getSimulator().getProjectManager().getLogicGraphManager().runScheduler();
            currentCycle++;
        }
        updateStatus();
    }

    //Constructor
    public StaticSimulation(SimulationProcesser processer,SimulationProperties properties) {
        this.processer = processer;
        this.properties = properties;
    }
}
