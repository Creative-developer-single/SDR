package com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Nodes;

import java.util.List;

import org.json.JSONObject;


import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicNodeManager {
    //LogicGraph Instance
    private LogicGraphStructureManager manager;

    //LogicNodeFinder Instance
    private LogicNodeFinder finder;

    //LogicNodeModifier Instance
    private LogicNodeModifier modifier;

    //LogicNode List
    private List<LogicNode> nodes;

    //Get the LogicNode Finder
    public LogicNodeFinder getFinder() {
        return finder;
    }

    //Get the LogicNode Modifier
    public LogicNodeModifier getModifier() {
        return modifier;
    }

    public List<LogicNode> getNodes() {
        return nodes;
    }

    //Modify By Node
    public void modifyLogicNode(LogicNode node,JSONObject object){
        modifier.modifyLogicNodeByObject(node, object);
    }

    //Modify By ID
    public void modifyLogicNode(String nodeID, JSONObject object){
        LogicNode node = finder.findNodeById(nodeID);
        if (node != null) {
            modifier.modifyLogicNodeByObject(node, object);
        } else {
            System.out.println("Node not found");
        }
    }

    public LogicNodeManager(LogicGraphStructureManager manager) {
        this.manager = manager;
        nodes = manager.getNodes();
        finder = new LogicNodeFinder(this);
        modifier = new LogicNodeModifier(this);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LogicGraphStructureManager manager = new LogicGraphStructureManager();
        LogicNode node = new LogicNode(new BaseComponent(0, 0, 0),"Test");
        manager.getNodeManager().getNodes().add(node);
        JSONObject object = new JSONObject();
        object.put("ID", "Test2");
        object.put("ComponentType", "Arithmetic.BasicALU");
        object.put("BlockLength", 1024);
        object.put("InputCount", 2);
        object.put("OutputCount", 1);
        object.put("ComponentID", "alu1");
        manager.getNodeManager().modifyLogicNode("Test", object);
        System.out.println(node.getID());
    }
}
