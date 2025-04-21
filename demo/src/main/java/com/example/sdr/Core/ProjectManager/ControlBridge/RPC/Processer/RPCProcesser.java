package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraph;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraphManager;

public class RPCProcesser {
    private ProjectManager manager;

    private RPCLogicGraphManager rpcLogicGraphManager;

    public ProjectManager getProjectManager() {
        return manager;
    }

    

    public void HandleRPCCall(String sourceData){
        try{
            JSONObject ctrlFrame = new JSONObject(sourceData);
            JSONArray rpcFrameArray = ctrlFrame.getJSONArray("RPCFrameArray");

            JSONObject rpcFrameIndexZero = rpcFrameArray.getJSONObject(0);
            JSONObject rpcFrame = rpcFrameIndexZero.getJSONObject("RPCFrame");
            String targetModule = rpcFrame.getString("TargetModule");

            switch(targetModule){
                case "LogicGraph":
                    rpcLogicGraphManager.RPCCall(rpcFrame);
                    break;
                default:
                    System.out.println("Not available in this process");
                    break;
            }
        }catch(Exception e){
            System.out.println("RPC Processer Error: " + e.getMessage());
        }
    }
    
    public RPCProcesser(ProjectManager manager) {
        this.manager = manager;
        this.rpcLogicGraphManager = new RPCLogicGraphManager(this);
    }
}
