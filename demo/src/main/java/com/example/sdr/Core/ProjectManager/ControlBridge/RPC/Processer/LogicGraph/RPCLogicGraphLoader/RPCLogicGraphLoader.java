package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraphLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class RPCLogicGraphLoader {
    RPCProcesser processer;
    LogicGraphManager manager;

    public RPCLogicGraphLoader(RPCProcesser processer) {
        this.processer = processer;
        this.manager = processer.getProjectManager().getLogicGraphManager();
    }

    /*
     * RPC调用方法
     */
    // RPC调用某个节点数据
    public SDRData[] RPCGetNodeData(Integer nodeID,Integer index,Integer length) {
        // 从ID中查找节点
        LogicNode node = manager.getGraphInstance().getNodeManager().getFinder().findNodeById(nodeID);

        if (node == null) {
            throw new IllegalArgumentException("Node with ID " + nodeID + " not found");
        }

        SDRData[] data = manager.getReportInstance().getNodeData(node,index);
        if (index < 0 || index > data.length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for node data length " + data.length);
        }
        if (length <= 0 || length > data.length) {
            throw new IllegalArgumentException("Invalid length " + length + " for node data starting at index " + index);
        }

        return data;
    }

    // RPC调用节点数组的数据
    public SDRData[][] RPCGetNodeByArray(JSONArray nodes){
        try{
            // 先检查是否合法
            if (nodes == null || nodes.length() == 0) {
                throw new IllegalArgumentException("Node array cannot be null or empty");
            }

            // 提取最长的长度
            int length = 0;
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);
                if (!node.has("Length")) {
                    throw new IllegalArgumentException("Node at index " + i + " does not have a Length property");
                }
                int nodeLength = node.getInt("Length");
                if (nodeLength > length) {
                    length = nodeLength;
                }
            }

            SDRData[][] result = SDRDataUtils.createComplexMatrix(nodes.length(), length, 0, 0);

            // 遍历节点，依次提取数据
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);
                Integer nodeID = node.getInt("ID");
                Integer index = node.getInt("Index");
                int Modulelength = nodes.getJSONObject(i).getInt("Length");

                // 获取节点数据
                result[i] = RPCGetNodeData(nodeID, index, Modulelength);
            }

            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
