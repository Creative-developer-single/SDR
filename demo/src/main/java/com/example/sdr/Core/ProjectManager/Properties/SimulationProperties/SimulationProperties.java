package com.example.sdr.Core.ProjectManager.Properties.SimulationProperties;

import org.json.JSONObject;

public class SimulationProperties {
    public static final int MAX_SIMULATION_LENGTH = 60;
    public static final int SIMULATION_MODE_LIMITED_CYCLES = 0;
    public static final int SIMULATION_MODE_INTERACTIVE = 1;

    //Simulation Properties
    public String simulationMode;
    public double simulationTime;

    //Load From JSONObject
    public void loadFromJSON(JSONObject object) {
        simulationMode = object.getString("SimulationMode");
        simulationTime = object.getDouble("SimulationTime");
    }

    public SimulationProperties(){
        simulationMode = "LimitedCycles";
        simulationTime = 1.0;
    }
    
}
