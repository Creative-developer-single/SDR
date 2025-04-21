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

    public LogicEdgeManager getEdgeManager(){
        return edgeManager;
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
