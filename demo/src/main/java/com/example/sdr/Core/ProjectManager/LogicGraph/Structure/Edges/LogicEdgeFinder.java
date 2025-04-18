package com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Edges;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicEdgeFinder {
    private LogicEdgeManager edgeManager;

    public LogicEdgeFinder(LogicEdgeManager edgeManager) {
        this.edgeManager = edgeManager;
    }

    // Find an edge by its start and end nodes
    public LogicEdge findEdge(LogicNode startNode, LogicNode endNode) {
        for (LogicEdge edge : edgeManager.getEdges()) {
            if (edge.getNode1() == startNode && edge.getNode2() == endNode) {
                return edge;
            }
        }
        return null;
    }

    // Find an edge by its index
    public LogicEdge findEdgeByID(String id) {
        for(LogicEdge edge : edgeManager.getEdges()) {
            if (edge.getId().equals(id)) {
                return edge;
            }
        }
        return null;
    }
}
