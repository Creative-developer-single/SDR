package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer;

import java.nio.ByteBuffer;
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

    private BlockingQueue<ByteBuffer> rpcFrameQueue;

    public ProjectManager getProjectManager() {
        return manager;
    }

    public void AddRPCFrameToQueue(ByteBuffer rpcFrame){
        try {
            rpcFrameQueue.put(rpcFrame);
        } catch (InterruptedException e) {
            System.out.println("Error adding RPC frame to queue: " + e.getMessage());
        }
    }

    public void processRPCFrame(){
        if(rpcFrameQueue.isEmpty()){
            //System.out.println("RPC Frame Queue is empty");
            return;
        }
        System.out.println("Processing RPC Frame from Queue");
        HandleRPCCall(rpcFrameQueue.poll());
    }

    public void HandleRPCCall(ByteBuffer sourceData){
        try{
            //第一步，检查帧类型，检查前四个字节是DATA还是CTRL
            //排除异常
            if (sourceData == null || sourceData.remaining() < 4) {
                System.out.println("Invalid RPC Frame: " + sourceData);
                return;
            }
            byte[] header = new byte[4];
            sourceData.get(header); // 获取前四个字节
            
            //检查帧类型
            String frameType = new String(header);

            //如果不是CTRL或者DATA，直接返回
            if (!frameType.equals("CTRL") && !frameType.equals("DATA")) {
                System.out.println("Invalid RPC Frame Type: " + frameType);
                return;
            }

            //当前版本中，前端只发送CTRL帧
            if(frameType.equals("CTRL")){
                // 去掉前四个字节，然后转换为JSON对象
                byte[] dataBytes = new byte[sourceData.remaining()];
                sourceData.get(dataBytes);
                String dataString = new String(dataBytes, "UTF-8");
                System.out.println("Received RPC Frame: " + dataString);

                // 将字符串转换为JSON对象
                JSONObject ctrlFrame = new JSONObject(dataString);
                JSONObject payload = ctrlFrame.getJSONObject("command");
                Integer rpcID = ctrlFrame.getInt("id");//

                JSONObject rpcFrame = payload.getJSONObject("RPCFrame");
                String targetModule = rpcFrame.getString("TargetModule");

                switch(targetModule){
                    case "LogicGraph":
                        rpcLogicGraphManager.RPCCall(rpcFrame,rpcID);

                        // 回复响应
                        JSONObject response = new JSONObject();

                        JSONObject ReplyFrame = new JSONObject();
                        ReplyFrame.put("FrameID",rpcID);
                        ReplyFrame.put("ReplyType","string");
                        ReplyFrame.put("Reply","success");
                        
                        response.put("RPCFrame", ReplyFrame);

                        manager.getRPCManager().SendReply("CTRL"+response.toString());
                        break;
                    default:
                        System.out.println("Not available in this process");
                        break;
                }
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
