package com.example.sdr.Core.ProjectManager.Simulation.Processer;

import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;
import com.example.sdr.Core.ProjectManager.Simulation.UpdatedSimulator;
import com.example.sdr.Core.ProjectManager.Simulation.Processer.Static.StaticSimulation;

public class SimulationProcesser {
    //Simulator Instance
    private Simulator simulator;

    //Properties
    private SimulationProperties simulationProperties;

    //Static Simulation Instance
    private StaticSimulation staticSimulation;

    public Simulator getSimulator() {
        return simulator;
    }

    public SimulationProcesser(Simulator simulator,SimulationProperties properties) {
        // Constructor
        this.simulator = simulator;
        this.simulationProperties = properties;
        this.staticSimulation = new StaticSimulation(this, properties);
    }

    public void handleSimulation(){
        switch(simulationProperties.simulationMode){
            case "Static":
                // Static Simulation
                staticSimulation.runSimulation();   
                break;
            case "Interactive":
                break;
            default:
                break;
        }
    }

    public void handleStaticSimulation(){
        staticSimulation.runSimulation();
    }
}
