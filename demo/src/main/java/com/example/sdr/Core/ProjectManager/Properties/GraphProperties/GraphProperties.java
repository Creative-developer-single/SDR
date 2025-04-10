package com.example.sdr.Core.ProjectManager.Properties.GraphProperties;

import org.json.JSONObject;

public class GraphProperties {
    //Graph Properties
    public String graphType;
    public String graphName;

    //Load From JSON
    public void LoadFromJSON(JSONObject object){
        //Load the Graph Properties
        this.graphType = object.getString("GraphType");
        this.graphName = object.getString("GraphName");
    }

    //Graph Properties Constructor
    public GraphProperties() {
        this.graphType = "LogicGraph";
        this.graphName = "LogicGraph";
    }

}
