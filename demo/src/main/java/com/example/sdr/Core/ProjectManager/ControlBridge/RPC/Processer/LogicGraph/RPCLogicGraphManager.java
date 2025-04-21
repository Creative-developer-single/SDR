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

    public void RPCCall(JSONObject object){
        try{
            String Command = object.getString("Command");
            JSONObject args = object.getJSONObject("args");
            
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

                //Update the Edge
                JSONArray edges = args.getJSONArray("Edges");
                for(int j=0;j<edges.length();j++){
                    JSONObject edge = edges.getJSONObject(j);
                    String edgeID = edge.getString("EdgeID");
                    
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
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public RPCProcesser getProcesser() {
        return processer;
    }
    
}
