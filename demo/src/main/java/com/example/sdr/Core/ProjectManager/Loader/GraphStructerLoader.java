package com.example.sdr.Core.ProjectManager.Loader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class GraphStructerLoader {
    //Json File Path
    private String jsonPath;

    //Map Store
    HashMap<String, LogicNode> nodeMap = new HashMap<>();

    public GraphStructerLoader(String jsonPath){
        this.jsonPath = jsonPath;
    }

    private LogicNode createNode(String ID,String ComponentType,String ComponentID,int blockLength){
        switch(ComponentType){
            case "BaseComponent":
                //Create the BaseComponent
                BaseComponent component = new BaseComponent(blockLength, blockLength,ComponentID);
                return new LogicNode(component, ID);
            default:
                return null;
        }
    }

    public void LoadFromJSON(){
        //Load the JSON File
        try{
            //Decode the JSON File
            String content = new String(Files.readAllBytes(Paths.get(jsonPath)));
            JSONObject jsonObject = new JSONObject(content);

            //Decode the Nodes
            //Decode the NodesArray
            JSONArray nodes = jsonObject.getJSONArray(content);
            
            //For Each Node
            for(int i=0;i<nodes.length();i++)
            {
                //Get the node object
                JSONObject node = nodes.getJSONObject(i);

                //Get the Node ID
                String id = node.getString("ID");
                String componentType = node.getString("ComponentType");
                String componentID = node.getString("ComponentID");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
