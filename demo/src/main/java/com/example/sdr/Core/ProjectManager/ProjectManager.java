package com.example.sdr.Core.ProjectManager;

import com.example.sdr.Core.Components.Tools.GeneralResourceFinder;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.RPCManager;
import com.example.sdr.Core.ProjectManager.Loader.ProjectLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.Properties.ProjectPropertiesManager;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class ProjectManager {
    //JSON path
    private String ProjectPropertiesJSONPath;
    private String GraphStructJSONPath;

    RPCManager rpcManager;

    //ProjectLoader
    ProjectLoader projectLoader;
    
    //ProjectSettingsLoader
    ProjectPropertiesManager projectPropertiesManager;

    //LogicGrphManager
    LogicGraphManager manager;

    //Simulator
    Simulator simulator;

    public void loadFromJSON(String ProjectPropertiesJSONPath){
        GeneralResourceFinder finder = new GeneralResourceFinder();
        projectLoader.setProjectPropertiesJSONPath(finder.getFilePath(ProjectPropertiesJSONPath));
        projectLoader.readJSONFile();
    }

    public ProjectPropertiesManager getProjectPropertiesManager(){
        return projectPropertiesManager;
    }

    public LogicGraphManager getLogicGraphManager(){
        return manager;
    }

    public void StartRPC(){
        rpcManager.StartRPC();
    }

    public ProjectManager(){
        projectPropertiesManager = new ProjectPropertiesManager(this);
        projectLoader = new ProjectLoader(this);
        manager = new LogicGraphManager();
        simulator = new Simulator(this);
        rpcManager = new RPCManager(this);
    }

    public static void TestForRPC(){
        ProjectManager projectManager = new ProjectManager();
        projectManager.loadFromJSON("/ProjectSettings/JSON/ProjectSettings3.json");

        projectManager.StartRPC();
    }

    public static void main(String[] args){
        /*
        ProjectManager projectManager = new ProjectManager();
        projectManager.loadFromJSON("/ProjectSettings/JSON/ProjectSettings3.json");

        projectManager.getLogicGraphManager().getGraphInstance().PrintNodes();
        projectManager.getLogicGraphManager().getGraphInstance().PrintEdges();

        //projectManager.simulator.Simluation();
        */
        TestForRPC();
    }
}
