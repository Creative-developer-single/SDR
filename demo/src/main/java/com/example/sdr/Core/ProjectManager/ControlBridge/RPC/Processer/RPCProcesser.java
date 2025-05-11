package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraph;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraphManager;

public class RPCProcesser {
    //Instance for the ProjectManager
    private ProjectManager manager;

    //Instance for the RPCLogicGraph
    private RPCLogicGraphManager rpcLogicGraphManager;

    private BlockingQueue<String> rpcFrameQueue;

    public ProjectManager getProjectManager() {
        return manager;
    }

    public void AddRPCFrameToQueue(String rpcFrame){
        try {
            rpcFrameQueue.put(rpcFrame);
        } catch (InterruptedException e) {
            System.out.println("Error adding RPC frame to queue: " + e.getMessage());
        }
    }

    public void processRPCFrame(){
        if(rpcFrameQueue.isEmpty()){
            System.out.println("RPC Frame Queue is empty");
            return;
        }
        HandleRPCCall(rpcFrameQueue.poll());
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
        this.rpcFrameQueue = new LinkedBlockingQueue<>();
    }
}
