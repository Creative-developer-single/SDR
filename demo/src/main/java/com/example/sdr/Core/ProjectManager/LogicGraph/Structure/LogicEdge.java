package com.example.sdr.Core.ProjectManager.LogicGraph.Structure;

public class LogicEdge {
    private LogicNode node1;
    private LogicNode node2;

    private int node1DataIndex;
    private int node2DataIndex;

    public LogicEdge(LogicNode node1, LogicNode node2){
        this.node1 = node1;
        this.node2 = node2;
        this.node1DataIndex = 0;
        this.node2DataIndex = 0;
    }

    public LogicEdge(LogicNode node1,int index1,LogicNode node2,int index2)
    {
        this.node1 = node1;
        this.node2 = node2;
        this.node1DataIndex = index1;
        this.node2DataIndex = index2;
    }

    public LogicNode getNode1() {
        return node1;
    }

    public LogicNode getNode2() {
        return node2;
    }

    public int getNode1DataIndex() {
        return node1DataIndex;
    }

    public int getNode2DataIndex() {
        return node2DataIndex;
    }
}
