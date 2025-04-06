package com.example.sdr.Core.ProjectManager.Loader;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ProjectManager;

public class ProjectPropertiesLoader {
    String ProjectPropertiesJSONPath;

    //ProjectManager
    ProjectManager manager;

    //Call the SubModule Loader
    GraphStructerLoader graphStructerLoader;

    public ProjectPropertiesLoader(String ProjectPropertiesJSONPath){
        this.ProjectPropertiesJSONPath = ProjectPropertiesJSONPath;
        graphStructerLoader = new GraphStructerLoader();
    }

    public ProjectPropertiesLoader(){
        this.ProjectPropertiesJSONPath = null;
        graphStructerLoader = new GraphStructerLoader();
    }

    public void setProjectManager(ProjectManager manager){
        this.manager = manager;
    }

    public void setProjectPropertiesJSONPath(String ProjectPropertiesJSONPath){
        this.ProjectPropertiesJSONPath = ProjectPropertiesJSONPath;
    }

    public void LoadProjectProperties(){
        try{
            //Load the Project Properties
            String content = new String(Files.readAllBytes(Paths.get(ProjectPropertiesJSONPath)));
            JSONObject jsonObject = new JSONObject(content);

            //Get the Project Properties
            String ProjectName = jsonObject.getString("ProjectName");
            String ProjectOwner = jsonObject.getString("ProjectOwner");
            String LastModifyTime = jsonObject.getString("LastModifyTime");

            JSONObject projectSettings = jsonObject.getJSONObject("ProjectSettings");
            JSONObject logicGraphStructure = projectSettings.getJSONObject("LogicGraph");

            //Bind the Graph
            graphStructerLoader.setLogicDirectedGraph(manager.getLogicGraphManager().getGraphInstance());

            graphStructerLoader.setJSONObject(logicGraphStructure);
            graphStructerLoader.LoadFromJSONObject();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
