package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;

public class RPCLogicGraph {
    RPCProcesser processer;
    LogicGraphManager manager;

    public RPCLogicGraph(RPCProcesser processer) {
        this.processer = processer;
        this.manager = processer.getProjectManager().getLogicGraphManager();
    }

    //ModifyLogicNode By ID
    public void RPCModifyLogicNode(JSONObject object) {
        String ID = object.getString("ID");
        manager.getGraphInstance().getNodeManager().modifyLogicNode(ID, object);
        System.out.println("SuccessFul to Excute the RPC");
    }

    //AddLogicNode RPC
    public void RPCCreateLogicNode(JSONObject object){
        manager.getGraphInstance().getNodeManager().getModifier().createLogicNodeByObject(object);
        System.out.println("SuccessFul to Excute the RPC");
    }

    //DeleteLogicNode RPC
    public void RPCDeleteLogicNode(JSONObject object){
        String ID = object.getString("ID");
        manager.getGraphInstance().getNodeManager().getModifier().deleteLogicNode(ID);
        System.out.println("SuccessFul to Excute the RPC");
    }

    //ModifyLogicEdge RPC
    public void RPCModifyLogicEdge(JSONObject object){
        String ID = object.getString("ID");
        manager.getGraphInstance().getEdgeManager().getModifier().modifyLogicEdge(object);
        System.out.println("SuccessFul to Excute the RPC");
    }

    //AddLogicEdge RPC
    public void RPCAddLogicEdge(JSONObject object){
        String ID = object.getString("ID");
        manager.getGraphInstance().getEdgeManager().getModifier().addLogicEdge(object);
        System.out.println("SuccessFul to Excute the RPC");
    }

    public void RPCDeleteLogicEdge(JSONObject object){
        String ID = object.getString("ID");
        manager.getGraphInstance().getEdgeManager().getModifier().deleteLogicEdge(object);
        System.out.println("SuccessFul to Excute the RPC");
    }
}
