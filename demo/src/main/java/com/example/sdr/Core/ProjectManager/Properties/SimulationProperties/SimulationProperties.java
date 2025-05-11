package com.example.sdr.Core.ProjectManager.Properties.SimulationProperties;

import org.json.JSONObject;

public class SimulationProperties {
    public static final int MAX_SIMULATION_LENGTH = 60;
    public static final int SIMULATION_MODE_LIMITED_CYCLES = 0;
    public static final int SIMULATION_MODE_INTERACTIVE = 1;

    //Simulation Properties
    public String simulationMode;

    //Static Simulation
    public int simulationCycle;
    public int currentCycle = 0;

    //Dynamic Simulation
    public double simulationTime;
    public double simulationTimeStep;
    public double simulationSampleRate;
    public double simulationBlockLength;

    //Load From JSONObject
    public void loadFromJSON(JSONObject object) {
        simulationMode = object.getString("SimulationMode");
        simulationTime = object.getDouble("SimulationTime");
    }

    public SimulationProperties(){
        simulationMode = "LimitedCycles";
        
        simulationCycle = 1000;
        currentCycle = 0;

        simulationTime = 1.0;
        simulationTimeStep = 0.01;
        simulationSampleRate = 100000;
        simulationBlockLength = 1024;
    }
    
}
