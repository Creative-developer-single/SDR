package com.example.sdr.Core.ProjectManager.Simulation.interactive;

import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class InteractiveSimulation {
    private Simulator simulator;

    private double simulationTime;
    private double simulationSampleRate;
    private double simulationBlockLength;


    public InteractiveSimulation(Simulator simulator) {
        this.simulator = simulator;
    }

    
}
