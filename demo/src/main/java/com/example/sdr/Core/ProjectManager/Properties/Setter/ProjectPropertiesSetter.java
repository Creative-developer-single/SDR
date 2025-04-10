package com.example.sdr.Core.ProjectManager.Properties.Setter;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.Properties.ProjectPropertiesManager;

public class ProjectPropertiesSetter {
    //ProjectPropertiesManager
    private ProjectPropertiesManager projectPropertiesManager;

    //JSON Format Properties
    private JSONObject jsonObject;

    public void LoadProperties(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        //Load the Global Properties
        JSONObject globalPropertiesObject = jsonObject.getJSONObject("GlobalProperties");
        projectPropertiesManager.globalProperties.LoadFromJSON(globalPropertiesObject);

        //Load the Graph Properties
        JSONObject graphPropertiesObject = jsonObject.getJSONObject("GraphProperties");
        projectPropertiesManager.graphProperties.LoadFromJSON(graphPropertiesObject);

        //Load the Settings Properties
        JSONObject settingsPropertiesObject = jsonObject.getJSONObject("SettingsProperties");
        projectPropertiesManager.settingsProperties.LoadFromJSON(settingsPropertiesObject);

        //Load the Simulation Properties
        JSONObject simulationPropertiesObject = jsonObject.getJSONObject("SimulationProperties");
        projectPropertiesManager.simulationProperties.loadFromJSON(simulationPropertiesObject);
    }


    public ProjectPropertiesSetter(ProjectPropertiesManager projectPropertiesManager) {
        this.projectPropertiesManager = projectPropertiesManager;
    }


}
