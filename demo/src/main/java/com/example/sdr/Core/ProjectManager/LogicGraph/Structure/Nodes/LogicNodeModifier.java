package com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Nodes;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.Components.Tools.ComponentCreator;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicNodeModifier {
    private LogicNodeManager nodeManager;

    public LogicNodeModifier(LogicNodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    //Modify the LogicNode by Object
    private void updateNodeByJSONObject(LogicNode node, JSONObject object){
        String ID = object.getString("ID");
        
        //Update the ID
        node.setID(ID);
        
        //Update the Component
        ComponentCreator creator = new ComponentCreator();
        Object component = creator.createComponentByClassName(object);
        node.setComponent((BaseComponent)component);
    }

    //Create the LogicNode by Object
    public void createLogicNodeByObject(JSONObject object) {
        String ID = object.getString("ID");
        
        //Update the Component
        ComponentCreator creator = new ComponentCreator();
        Object component = creator.createComponentByClassName(object);
        
        LogicNode node = new LogicNode((BaseComponent)component, ID);
        
        //Add the LogicNode
        nodeManager.getNodes().add(node);
    }

    //Modify the LogicNode by Object
    public void modifyLogicNodeByObject(LogicNode node, JSONObject object) {
        LogicNode targetNode = nodeManager.getFinder().findNodeByObject(node);
        if (targetNode != null) {
            updateNodeByJSONObject(targetNode, object);
        }
    }

    //Add a Empty LogicNode
    public void addDefinedLogicNode(LogicNode node) {
        if(nodeManager.getFinder().findNodeByObject(node) != null){
            System.out.println("Node already exists");
            return;
        }
        nodeManager.getNodes().add(node);
    }

    //Delete a LogicNode
    public void deleteLogicNode(String ID){
        LogicNode node = nodeManager.getFinder().findNodeById(ID);
        if (node != null) {
            nodeManager.getNodes().remove(node);
            System.out.println("Node deleted");
        } else {
            System.out.println("Node not found");
        }
    }

    //Delete a LogicNode by Object
    public void deleteLogicNode(LogicNode node){
        LogicNode targetNode = nodeManager.getFinder().findNodeByObject(node);
        if (targetNode != null) {
            nodeManager.getNodes().remove(targetNode);
            System.out.println("Node deleted");
        } else {
            System.out.println("Node not found");
        }
    }
}
