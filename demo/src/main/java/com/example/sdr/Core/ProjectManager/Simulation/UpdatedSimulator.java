package com.example.sdr.Core.ProjectManager.Simulation;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;

public class UpdatedSimulator {
    //Instance for the ProjectManager
    private ProjectManager manager;

    //Loop Thread Instance
    private Thread loopThread;

    private String simulationStatus = "Stopped";

    public void setSimulationStatus(String status) {
        this.simulationStatus = status;
    }

    public LogicGraphManager getLogicGraphManager() {
        return manager.getLogicGraphManager();
    }

    public UpdatedSimulator(ProjectManager manager) {
        this.manager = manager;
    }
}
