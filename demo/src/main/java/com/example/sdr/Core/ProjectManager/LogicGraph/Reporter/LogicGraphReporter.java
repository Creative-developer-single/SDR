package com.example.sdr.Core.ProjectManager.LogicGraph.Reporter;

import java.util.List;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicGraphReporter {
    //Reported Nodes and edges
    private List<LogicNode> nodes;
    private List<LogicEdge> edges;

    //set the nodes and edges
    public void setNodes(List<LogicNode> nodes){
        this.nodes = nodes;
    }

    public void setEdges(List<LogicEdge> edges){
        this.edges = edges;
    }

    //get the nodes and edges
    public List<LogicNode> getNodes(){
        return nodes;
    }

    public List<LogicEdge> getEdges(){
        return edges;
    }

    public LogicGraphReporter(List<LogicNode> nodes, List<LogicEdge> edges){
        this.nodes = nodes;
        this.edges = edges;
    }

    public LogicGraphReporter(){
        this.nodes = null;
        this.edges = null;
    }
}
