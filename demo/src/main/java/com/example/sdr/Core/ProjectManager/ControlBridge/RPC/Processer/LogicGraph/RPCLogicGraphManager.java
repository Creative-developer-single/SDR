package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.Components.Tools.BytesTools.BytesConcat;
import com.example.sdr.Core.Components.Tools.Converter.DoubleToBytes.DoubleToBytes;
import com.example.sdr.Core.Components.Tools.Converter.IntToBytes.IntToBytes;
import com.example.sdr.Core.Components.Tools.Converter.StringToBytes.StringToBytes;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraphLoader.RPCLogicGraphLoader;

public class RPCLogicGraphManager {
    // RPCProcesser Instance
    private RPCProcesser processer;

    private RPCLogicGraph rpcLogicGraph;

    private RPCLogicGraphLoader rpcLogicGraphLoader;

    public RPCLogicGraphManager(RPCProcesser processer) {
        this.processer = processer;
        this.rpcLogicGraph = new RPCLogicGraph(processer);
        this.rpcLogicGraphLoader = new RPCLogicGraphLoader(processer);
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

    /*
     * 处理节点数据相关RPC调用
     */
    public void RPCNodeData(JSONObject object,Integer rpcID){
        try{
            String Command = object.getString("Command");
            JSONObject args = object.getJSONObject("Args");
            JSONArray nodes = args.getJSONArray("Nodes");

            // 检查命令类型
            switch(Command){
                case "RPCGetNodeAns":
                    SDRData [][] result = rpcLogicGraphLoader.RPCGetNodeByArray(nodes);

                    // 创建RPC返回帧
                    byte[] response = StringToBytes.stringToBytes("DATA");
                    // 创建FrameID
                    byte [] frameID = IntToBytes.intToBytes(rpcID);
                    // 创建模块长度
                    byte[] moduleLength = IntToBytes.intToBytes(nodes.length());
                    
                    // 拼接到response
                    response = BytesConcat.concat(response,frameID,moduleLength);
                    
                    // 遍历结果数组
                    for (int i = 0; i < result.length;i++){
                        // 创建模块ID
                        byte[] moduleID = IntToBytes.intToBytes(nodes.getJSONObject(i).getInt("ID"));

                        // 创建模块数据长度
                        byte[] moduleDataLength = IntToBytes.intToBytes(result[i].length);

                        // 创建模块数据
                        byte[] moduleData = SDRDataUtils.toByteArray(result[i]);
                        // 拼接到modulesData
                        response = BytesConcat.concat(response, moduleID, moduleDataLength,moduleData);
                    }

                    // 调用RPCProcesser的SendReply方法发送响应
                    processer.getProjectManager().getRPCManager().SendReply(response);
            }


            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void RPCCall(JSONObject object, Integer rpcID){
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
                case "RPCGetNodeAns":
                    RPCNodeData(object, rpcID);
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
