package com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Edges;

import java.util.List;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Nodes.LogicNodeModifier;

public class LogicEdgeManager {
    private LogicGraphStructureManager manager;

    private LogicEdgeFinder finder;

    private LogicEdgeModifier modifier;

    private List<LogicEdge> edges;

    public LogicEdgeModifier getModifier() {
        return new LogicEdgeModifier(this);
    }
    
    public LogicEdgeFinder getFinder() {
        return finder;
    }

    public List<LogicEdge> getEdges() {
        return edges;
    }

    public LogicGraphStructureManager getManager() {
        return manager;
    }

    public LogicEdgeManager(LogicGraphStructureManager manager) {
        this.manager = manager;
        finder = new LogicEdgeFinder(this);
        edges = manager.getEdges();
    }
}
