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

    /*
     * Function Name: clearGraph
     * Description: 清除整个图，包括节点和边，之后需要重新加载图信息
     */
    public void clearGraph(){
        // 删除所有节点和边
        nodes.clear();
        edges.clear();
        rootNode = null;
    }

    public void refreshComponents(){
        for (LogicNode node : nodes) {
            node.getComponent().refreshComponent();
        }
    }


    // 重置节点，但不清除节点，用于重新运行现有的仿真图
    public void resetNodes(){
        //reset all nodes
        for (LogicNode node : nodes) {
            node.reset();
        }

        //reBinding the edges
        for(LogicEdge edge : edges){
            LogicNode node1 = edge.getNode1();
            LogicNode node2 = edge.getNode2();
            node1.addNextEdge(edge);
            node2.addPrevEdge(edge);
        }
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
