package com.example.sdr.Core.ProjectManager.LogicGraph.Structure;

import java.util.HashMap;
import java.util.Map;

public class LogicGraphGlobalDefinitions {
    private Map<String,String> globalDefinitons;

    public LogicGraphGlobalDefinitions(){
        globalDefinitons = new HashMap<>();
    }

    public void addGlobalDefinition(String key,String value){
        globalDefinitons.put(key,value);
    }

    public String getGlobalDefinition(String key){
        return globalDefinitons.get(key);
    }
}
