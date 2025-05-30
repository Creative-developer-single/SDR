package com.example.sdr.Core.ProjectManager;

public class ProjectManagerTaskHandler {
    // ProjectManager Instance
    private ProjectManager projectManager;

    public ProjectManagerTaskHandler(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void handleTask(){
        SimulationTask();
    }

    private void SimulationTask(){
        String status = projectManager.getSimulator().getSimulationStatus();
        switch(status){
            case "Starting":
                projectManager.getSimulator().resetSimulation();
                projectManager.getSimulator().startSimulation();
                break;
            case "Running":
                projectManager.getSimulator().runSimulation();
                break;
            case "Paused":
                break;
            case "Stopped":
                projectManager.getSimulator().resetSimulation();
                //System.out.println("Simulation Stopped");
                break;
            default:
                break;
        }
    }
}
