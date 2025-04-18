package com.example.sdr.Core.ProjectManager.LogicGraph.Structure;

import java.util.ArrayList;
import java.util.List;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Edges.LogicEdgeManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Nodes.LogicNodeManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Nodes.LogicNodeModifier;

public class LogicGraphStructureManager {
    //LogicNodeManagers
    private LogicNodeManager nodeManager;

    private LogicEdgeManager edgeManager;

    private LogicNode rootNode;
    private LogicNode currentNode;

    private List<LogicNode> nodes;
    private List<LogicEdge> edges;

    public LogicGraphStructureManager(){
        rootNode = null;
        currentNode = null;
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        nodeManager = new LogicNodeManager(this);
        edgeManager = new LogicEdgeManager(this);
    }

    public LogicNodeManager getNodeManager(){
        return nodeManager;
    }

    //Add a Node, But doesn't have edges
    public void addNode(LogicNode node)
    {
        nodeManager.getModifier().addDefinedLogicNode(node);
    }

    public void addEdge(String ID,LogicNode node1,LogicNode node2)
    {
        edgeManager.getModifier().addLogicEdge(ID, node1, node2);
    }

    //With Index
    public void addEdge(String ID,LogicNode node1,int startEdgeIndex,LogicNode node2,int endEdgeIndex)
    {
        edgeManager.getModifier().addLogicEdgeByIndex(ID, node1, startEdgeIndex, node2, endEdgeIndex);
    }

    public LogicNode getRootNode(){
        return rootNode;
    }

    public List<LogicNode> getNodes(){
        return nodes;
    }

    public List<LogicEdge> getEdges(){
        return edges;
    }

    public void PrintNodes(){
        for (LogicNode logicNode : nodes) {
            System.out.println(logicNode.getId());
        }
    }
    public void PrintEdges(){
        for(LogicEdge edge : edges){
            System.out.println(edge.getNode1().getId() + "->" + edge.getNode2().getId());
        }
    }
}
