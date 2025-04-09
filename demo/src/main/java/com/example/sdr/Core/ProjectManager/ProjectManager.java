package com.example.sdr.Core.ProjectManager;

import com.example.sdr.Core.Components.Tools.GeneralResourceFinder;
import com.example.sdr.Core.ProjectManager.Loader.GraphStructerLoader;
import com.example.sdr.Core.ProjectManager.Loader.ProjectPropertiesLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicDirectedGraph;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class ProjectManager {
    //JSON path
    private String ProjectPropertiesJSONPath;
    private String GraphStructJSONPath;
    
    //ProjectSettingsLoader
    ProjectPropertiesLoader projectPropertiesLoader;

    //LogicGrphManager
    LogicGraphManager manager;

    //Simulator
    Simulator simulator;

    public void loadFromJSON(String ProjectPropertiesJSONPath){
        GeneralResourceFinder finder = new GeneralResourceFinder();
        this.ProjectPropertiesJSONPath = finder.getFilePath(ProjectPropertiesJSONPath);
        projectPropertiesLoader.LoadProjectProperties();
    }

    public LogicGraphManager getLogicGraphManager(){
        return manager;
    }

    public ProjectManager(){
        projectPropertiesLoader = new ProjectPropertiesLoader();
        manager = new LogicGraphManager();
        simulator = new Simulator();
    }

    public static void main(String[] args){
        ProjectManager projectManager = new ProjectManager();
        ProjectPropertiesLoader projectPropertiesLoader = new ProjectPropertiesLoader();
        projectManager.simulator.setLogicGraphManager(projectManager.getLogicGraphManager());

        //General Resource Finder
        GeneralResourceFinder finder = new GeneralResourceFinder();
        projectPropertiesLoader.setProjectPropertiesJSONPath(finder.getFilePath("/ProjectSettings/JSON/ProjectSettings2.json"));
        projectPropertiesLoader.setProjectManager(projectManager);
        projectPropertiesLoader.LoadProjectProperties();

        projectManager.getLogicGraphManager().getGraphInstance().PrintNodes();
        projectManager.getLogicGraphManager().getGraphInstance().PrintEdges();

        projectManager.simulator.Simluation();
    }
}
