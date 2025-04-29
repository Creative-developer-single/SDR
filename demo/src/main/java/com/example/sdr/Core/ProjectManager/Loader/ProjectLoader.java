package com.example.sdr.Core.ProjectManager.Loader;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ProjectManager;

public class ProjectLoader {
    String ProjectPropertiesJSONPath;

    //ProjectManager
    ProjectManager manager;

    public ProjectLoader(String ProjectPropertiesJSONPath){
        this.ProjectPropertiesJSONPath = ProjectPropertiesJSONPath;
        manager = null;
    }

    public ProjectLoader(ProjectManager manager){
        this.ProjectPropertiesJSONPath = null;
        this.manager = manager;
    }

    public ProjectLoader(){
        this.ProjectPropertiesJSONPath = null;
        this.manager = null;
    }

    public void setProjectManager(ProjectManager manager){
        this.manager = manager;
    }

    public void setProjectPropertiesJSONPath(String ProjectPropertiesJSONPath){
        this.ProjectPropertiesJSONPath = ProjectPropertiesJSONPath;
    }

    public void readJSONFile(){
        try{
            String content = new String(Files.readAllBytes(Paths.get(ProjectPropertiesJSONPath)));
            JSONObject jsonObject = new JSONObject(content);

            manager.getLogicGraphManager().loadGraphFromJSON(jsonObject.getJSONObject("LogicGraph"));
            manager.getProjectPropertiesManager().getProjectPropertiesSetter().LoadProperties(jsonObject.getJSONObject("ProjectProperties"));
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
