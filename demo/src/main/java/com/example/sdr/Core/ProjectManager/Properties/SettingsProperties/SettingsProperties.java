package com.example.sdr.Core.ProjectManager.Properties.SettingsProperties;

import org.json.JSONObject;

public class SettingsProperties {
    //Settings Properties
    public String settingsName;
    public String settingsType;
    public String settingsVersion;
    public String settingsPath;

    public void LoadFromJSON(JSONObject object){
        settingsName = object.getString("SettingsName");
        settingsType = object.getString("SettingsType");
        settingsVersion = object.getString("SettingsVersion");
        settingsPath = object.getString("SettingsPath");
    }

    public SettingsProperties() {
        settingsName = null;
        settingsType = null;
        settingsVersion = null;
        settingsPath = null;
    }

}
