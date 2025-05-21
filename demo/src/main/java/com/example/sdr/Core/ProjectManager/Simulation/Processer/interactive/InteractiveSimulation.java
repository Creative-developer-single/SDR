package com.example.sdr.Core.ProjectManager.Simulation.Processer.interactive;

import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;
import com.example.sdr.Core.ProjectManager.Simulation.Processer.SimulationProcesser;

public class InteractiveSimulation {
    //Instance for Processer
    private SimulationProcesser processer;

    //Properties Instance
    private SimulationProperties properties;

    //Interactive Properties
    private double simulationTime;
    private double simulationTimeStep;
    private int simulationCycle;
    private int currentCycle;

    //Timer
    long currentTime;
    long lastTime;

    public void syncStatus(){
        simulationTime = properties.simulationTime;
        simulationTimeStep = properties.simulationTimeStep;
        currentCycle = properties.currentCycle;
    }

    public void updateStatus(){
        properties.simulationTime = simulationTime;
        properties.simulationTimeStep = simulationTimeStep;
    
        properties.simulationCycle = (int) Math.round(simulationTime / simulationTimeStep);
        properties.currentCycle = currentCycle;
    }

    public void runSimulation(){
        syncStatus();
        //Check if the Timer Triggered
        currentTime = System.currentTimeMillis();
        if(currentTime - lastTime >= simulationTimeStep  * 1000){
            if(currentCycle <= simulationCycle){
                processer.getSimulator().getProjectManager().getLogicGraphManager().runScheduler();
                currentCycle++;
            }
            lastTime = currentTime;
        }

        updateStatus();
    }

    public InteractiveSimulation(SimulationProcesser processer,SimulationProperties properties) {
        this.processer = processer;
        this.properties = properties;
        syncStatus();
    }

    
}
