package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;

public class RPCLogicGraphManager {
    // RPCProcesser Instance
    private RPCProcesser processer;

    private RPCLogicGraph rpcLogicGraph;

    public RPCLogicGraphManager(RPCProcesser processer) {
        this.processer = processer;
        this.rpcLogicGraph = new RPCLogicGraph(processer);
    }

    public void RPCLoadLogicGraph(JSONObject object){
        // 提取逻辑图数据
        JSONObject logicGraph = object.getJSONObject("Args");
        rpcLogicGraph.RPCLoadLogicGraph(logicGraph);
    }

    public void RPCManageLogicNode(JSONObject object){
        try{
            String Command = object.getString("Command");
            JSONObject args = object.getJSONObject("Args");
            
            JSONArray nodes = args.getJSONArray("Nodes");
            
            for(int i=0;i<nodes.length();i++){
                JSONObject node = nodes.getJSONObject(i);
                String ID = node.getString("ID");
                
                //Update the Node
                switch(Command){
                    case "RPCModifyLogicNode":
                        rpcLogicGraph.RPCModifyLogicNode(node);
                        break;
                    case "RPCCreateLogicNode":
                        rpcLogicGraph.RPCCreateLogicNode(node);
                        break;
                    case "RPCDeleteLogicNode":
                        rpcLogicGraph.RPCDeleteLogicNode(node);
                        break;
                    default:
                        System.out.println("Not available in this process");
                        break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void RPCManageLogicEdge(JSONObject object){
        try{
            String Command = object.getString("Command");
            JSONObject args = object.getJSONObject("Args");
            
            JSONArray edges = args.getJSONArray("Edges");
            
            for(int i=0;i<edges.length();i++){
                JSONObject edge = edges.getJSONObject(i);
                String ID = edge.getString("ID");
                
                //Update the Edge
                switch(Command){
                    case "RPCModifyLogicEdge":
                        rpcLogicGraph.RPCModifyLogicEdge(edge);
                        break;
                    case "RPCAddLogicEdge":
                        rpcLogicGraph.RPCAddLogicEdge(edge);
                        break;
                    case "RPCDeleteLogicEdge":
                        rpcLogicGraph.RPCDeleteLogicEdge(edge);
                        break;
                    default:
                        System.out.println("Not available in this process");
                        break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void RPCCall(JSONObject object){
        try{
            // 获取命令类型
            String Command = object.getString("Command");

            switch(Command){
                case "RPCModifyLogicNode":
                    RPCManageLogicNode(object);
                    break;
                case "RPCCreateLogicNode":
                    RPCManageLogicNode(object);
                    break;
                case "RPCDeleteLogicNode":
                    RPCManageLogicNode(object);
                    break;
                case "RPCModifyLogicEdge":
                    RPCManageLogicEdge(object);
                    break;
                case "RPCAddLogicEdge":
                    RPCManageLogicEdge(object);
                    break;
                case "RPCDeleteLogicEdge":
                    RPCManageLogicEdge(object);
                    break;
                case "RPCLoadLogicGraph":
                    RPCLoadLogicGraph(object);
                    break;
                default:
                    System.out.println("Not available in this process");
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public RPCProcesser getProcesser() {
        return processer;
    }
    
}
