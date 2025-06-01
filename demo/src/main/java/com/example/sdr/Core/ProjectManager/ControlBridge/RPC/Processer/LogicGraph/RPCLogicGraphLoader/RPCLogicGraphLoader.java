package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.LogicGraph.RPCLogicGraphLoader;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public double[] RPCGetNodeData(Integer nodeID,Integer index,Integer length) {
        // 从ID中查找节点
        LogicNode node = manager.getGraphInstance().getNodeManager().getFinder().findNodeById(nodeID);

        if (node == null) {
            throw new IllegalArgumentException("Node with ID " + nodeID + " not found");
        }

        double[] data = manager.getReportInstance().getNodeData(node,index);
        if (index < 0 || index >= data.length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for node data length " + data.length);
        }
        if (length <= 0 || length > data.length) {
            throw new IllegalArgumentException("Invalid length " + length + " for node data starting at index " + index);
        }

        // 返回指定索引和长度的数据
        double[] result = new double[length];
        System.arraycopy(data, 0, result, 0, length);
        return result;
    }

    // RPC调用节点数组的数据
    public double[][] RPCGetNodeByArray(JSONArray nodes){
        try{
            // 先检查是否合法
            if (nodes == null || nodes.length() == 0) {
                throw new IllegalArgumentException("Node array cannot be null or empty");
            }

            // 规定前端数组长度一致
            // 提取第一个节点的长度作为数组初始化长度
            int length = nodes.getJSONObject(0).getInt("Length");

            double[][] result = new double[nodes.length()][length];

            // 遍历节点，依次提取数据
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);
                Integer nodeID = node.getInt("ID");
                Integer index = node.getInt("Index");

                // 获取节点数据
                result[i] = RPCGetNodeData(nodeID, index, length);
            }

            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
