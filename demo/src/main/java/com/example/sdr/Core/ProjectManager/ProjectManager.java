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

    //ProjectTaskHandler
    private ProjectManagerTaskHandler projectManagerTaskHandler;

    //RPCManager
    private RPCManager rpcManager;

    //ProjectLoader
    private ProjectLoader projectLoader;
    
    //ProjectSettingsLoader
    private ProjectPropertiesManager projectPropertiesManager;

    //LogicGrphManager
    private LogicGraphManager manager;

    //Simulator
    private Simulator simulator;

    public void loadFromJSON(String ProjectPropertiesJSONPath){
        GeneralResourceFinder finder = new GeneralResourceFinder();
        projectLoader.setProjectPropertiesJSONPath(finder.getFilePath(ProjectPropertiesJSONPath));
        projectLoader.readJSONFile();
    }

    public ProjectManagerTaskHandler getProjectManagerTaskHandler(){
        return projectManagerTaskHandler;
    }

    public ProjectPropertiesManager getProjectPropertiesManager(){
        return projectPropertiesManager;
    }

    public LogicGraphManager getLogicGraphManager(){
        return manager;
    }

    public RPCManager getRPCManager(){
        return rpcManager;
    }

    public Simulator getSimulator(){
        return simulator;
    }

    public void StartRPC(){
        rpcManager.StartRPC();
    }

    public void OpenProject(String ProjectPath){
        loadFromJSON(ProjectPath);
    }

    public void UpdateState(){

    }

    public ProjectManager(){
        projectPropertiesManager = new ProjectPropertiesManager(this);
        projectLoader = new ProjectLoader(this);
        manager = new LogicGraphManager();
        simulator = new Simulator(this);
        rpcManager = new RPCManager(this);
        projectManagerTaskHandler = new ProjectManagerTaskHandler(this);
    }

    public static void TestForRPC(){
        ProjectManager projectManager = new ProjectManager();
        projectManager.loadFromJSON("/ProjectSettings/JSON/ProjectSettings3.json");

        projectManager.StartRPC();
    }

    public static void TestForRPCV2(){
        ProjectManager projectManager = new ProjectManager();
        //projectManager.loadFromJSON("/ProjectSettings/JSON/ComplexSignal/ComplexSine.json");
        projectManager.loadFromJSON("/ProjectSettings/JSON/RealSignal/RealSine.json");
        //projectManager.loadFromJSON("/ProjectSettings/JSON/ComplexSignal/ComplexSineWithOscilloscope.json");

        projectManager.getProjectPropertiesManager().simulationProperties.simulationCycle = 1;
        //projectManager.getSimulator().Simluation();
        //projectManager.getSimulator().startSimulation();
        

        projectManager.StartRPC();

        while(true){
            //同步系统状态到前端

            //RPC 调用处理
            projectManager.getRPCManager().getProcesser().processRPCFrame();
            projectManager.getProjectManagerTaskHandler().handleTask();
            if(projectManager.getSimulator().getSimulationStatus().equals("Stopped"))
            {
                System.gc();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void TestForFlow(){
        ProjectManager projectManager = new ProjectManager();
        projectManager.loadFromJSON("/ProjectSettings/JSON/AMProjects.json");
        //projectManager.loadFromJSON("/ProjectSettings/JSON/FMProjects.json");
        //projectManager.loadFromJSON("/ProjectSettings/JSON/ComplexSignal/ComplexSine.json");
        //projectManager.loadFromJSON("/ProjectSettings/JSON/ProjectSettings3.json");

        projectManager.getLogicGraphManager().getGraphInstance().PrintNodes();
        projectManager.getLogicGraphManager().getGraphInstance().PrintEdges();

        projectManager.simulator.Simluation();
    }

    public static void main(String[] args){

        //TestForFlow();
        TestForRPCV2();
        //TestForRPC();
    }
}
