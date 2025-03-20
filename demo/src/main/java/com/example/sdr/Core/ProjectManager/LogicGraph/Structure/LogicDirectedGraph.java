package com.example.sdr.Core.ProjectManager.LogicGraph.Structure;

import java.util.ArrayList;
import java.util.List;

public class LogicDirectedGraph {
    private LogicNode rootNode;
    private LogicNode currentNode;

    private List<LogicNode> nodes;
    private List<LogicEdge> edges;

    public LogicDirectedGraph(){
        rootNode = null;
        currentNode = null;
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public LogicNode findNodeByID(String id){
        for (LogicNode logicNode : nodes) {
            if(logicNode.getId().equals(id)){
                return logicNode;
            }
        }
        return null;
    }

    public LogicNode findNodeByObject(LogicNode node){
        for (LogicNode logicNode : nodes) {
            
            if(logicNode != null && logicNode.equals(node)){
                return logicNode;
            }
        }
        return null;
    }


    //Add a Node, But doesn't have edges
    public void addNode(LogicNode node)
    {
        if(findNodeByObject(node) == null){
            nodes.add(node);
        }else{
            System.out.println("Node already exists");
        }
    }

    //Add an edge between two nodes
    public void addEdge(LogicNode node1, LogicNode node2){
        if(findNodeByObject(node1) != null && findNodeByObject(node2) != null){
            LogicEdge edge = new LogicEdge(node1, node2);
            node1.addNextEdge(edge);
            node2.addPrevEdge(edge);
        }else{
            System.out.println("Node not found");
        }
    }

    //Add the Node With Channel Index
    public void addEdgeWithIndex(LogicNode node1,int index1,LogicNode node2,int index2)
    {
        if(findNodeByObject(node1) != null && findNodeByObject(node2) != null){
            LogicEdge edge = new LogicEdge(node1,index1,node2,index2);
            node1.addNextEdge(edge);
            node2.addPrevEdge(edge);
            edges.add(edge);
        }
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
