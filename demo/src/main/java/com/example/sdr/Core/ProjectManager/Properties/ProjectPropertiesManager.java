package com.example.sdr.Core.ProjectManager.Properties;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.Loader.ProjectLoader;
import com.example.sdr.Core.ProjectManager.Properties.GlobalProperties.GlobalProperties;
import com.example.sdr.Core.ProjectManager.Properties.GraphProperties.GraphProperties;
import com.example.sdr.Core.ProjectManager.Properties.Setter.ProjectPropertiesSetter;
import com.example.sdr.Core.ProjectManager.Properties.SettingsProperties.SettingsProperties;
import com.example.sdr.Core.ProjectManager.Properties.SimulationProperties.SimulationProperties;

public class ProjectPropertiesManager {
    //ProjectManager
    private ProjectManager projectManager;

    //Global Properties
    public GlobalProperties globalProperties;

    //Graph Properties
    public GraphProperties graphProperties;

    //Settings Properties
    public SettingsProperties settingsProperties;

    //Simulation Properties
    public SimulationProperties simulationProperties;

    //Properties Setter
    private ProjectPropertiesSetter projectPropertiesSetter;
    

    public ProjectPropertiesSetter getProjectPropertiesSetter() {
        return projectPropertiesSetter;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public ProjectPropertiesManager(){
        globalProperties = new GlobalProperties();
        graphProperties = new GraphProperties();
        settingsProperties = new SettingsProperties();
        simulationProperties = new SimulationProperties();
        projectPropertiesSetter = new ProjectPropertiesSetter(this);
        this.projectManager = null;
    }

    public ProjectPropertiesManager(ProjectManager manager){
        this.projectManager = manager;
        globalProperties = new GlobalProperties();
        graphProperties = new GraphProperties();
        settingsProperties = new SettingsProperties();
        simulationProperties = new SimulationProperties();
        projectPropertiesSetter = new ProjectPropertiesSetter(this);
    }
}
