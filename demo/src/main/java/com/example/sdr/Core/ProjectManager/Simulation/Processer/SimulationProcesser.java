package com.example.sdr.Core.ProjectManager.Simulation.Processer;

import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;
import com.example.sdr.Core.ProjectManager.Simulation.Processer.Static.StaticSimulation;

public class SimulationProcesser {
    // Simulator Instance
    private Simulator simulator;

    // Properties
    private SimulationProperties simulationProperties;

    // Static Simulation Instance
    private StaticSimulation staticSimulation;

    // Last simulation timestamp (ms)
    private long lastSimulationTime = 0;

    public Simulator getSimulator() {
        return simulator;
    }

    public SimulationProcesser(Simulator simulator, SimulationProperties properties) {
        // Constructor
        this.simulator = simulator;
        this.simulationProperties = properties;
        this.staticSimulation = new StaticSimulation(this, properties);
    }

    public void updateSimulationState() {
        // Check SimulationProperties state
        switch (simulator.getSimulationStatus()) {
            case "Running":
                // When starting, reset lastSimulationTime so first step runs immediately
                lastSimulationTime = System.currentTimeMillis();
                break;
            case "Stopped":
                // No action needed
                break;
            default:
                // Other states - can extend if needed
                break;
        }
    }

    public void handleSimulation() {
        switch (simulationProperties.simulationMode) {
            case "Static":
                long currentTime = System.currentTimeMillis();
                long timeStepMs = (long) (simulationProperties.simulationTimeStep * 1000); // sec -> ms

                if (currentTime - lastSimulationTime >= timeStepMs) {
                    staticSimulation.runSimulation();
                    lastSimulationTime = currentTime;
                    System.out.println("a cycle");
                } else {
                    // Skip this cycle
                    // Optional: System.out.println("Static mode waiting for next timestep...");
                }
                break;

            case "Interactive":
                // TODO: Add Interactive mode logic if needed
                break;

            default:
                break;
        }
    }

    // Optional: Manual one-step run for Static
    public void handleStaticSimulation() {
        staticSimulation.runSimulation();
        lastSimulationTime = System.currentTimeMillis();  // Update last sim time
    }
}
