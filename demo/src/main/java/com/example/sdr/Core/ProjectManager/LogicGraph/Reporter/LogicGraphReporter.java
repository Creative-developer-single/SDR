package com.example.sdr.Core.ProjectManager.LogicGraph.Reporter;

import java.util.List;

import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicGraphReporter {
    //Reported Nodes and edges
    private List<LogicNode> nodes;
    private List<LogicEdge> edges;

    /*
     * 获取和设置节点和边数组
     */
    // 设置节点和边数组
    public void setNodes(List<LogicNode> nodes){
        this.nodes = nodes;
    }

    public void setEdges(List<LogicEdge> edges){
        this.edges = edges;
    }
    // 获取节点和边数组
    public List<LogicNode> getNodes(){
        return nodes;
    }

    public List<LogicEdge> getEdges(){
        return edges;
    }


    /*
     * 调试接口：打印节点和边数组
     */
    // 打印节点数组
    public void printReportedNodes(){
        System.out.println("Info: Print Reported Nodes");
        for(LogicNode node : nodes){
            System.out.println(node.getId());
        }
    }
    // 打印边数组
    public void printReportedEdges(){
        System.out.println("Info: Print Reported Edges");
        for(LogicEdge edge : edges){
            System.out.println(edge.getNode1().getId() + " -> " + edge.getNode2().getId());
        }
    }

    /*
     * 获取节点的数据
     * 函数名称：getNodeData
     * 参数：节点对象，数据索引
     */
    public double[] getNodeData(LogicNode node,int index){
        if(node == null || index < 0 || index > node.getComponent().getOutputCount() - 1){
            throw new IllegalArgumentException("Invalid node or index");
        }
        return SDRDataUtils.toDoubleArray(node.getComponent().getAns(index));
    }

    /*
     * 构造函数
     * 用于初始化节点和边数组
     */
    public LogicGraphReporter(List<LogicNode> nodes, List<LogicEdge> edges){
        this.nodes = nodes;
        this.edges = edges;
    }
    // 默认构造函数
    public LogicGraphReporter(){
        this.nodes = null;
        this.edges = null;
    }
}
