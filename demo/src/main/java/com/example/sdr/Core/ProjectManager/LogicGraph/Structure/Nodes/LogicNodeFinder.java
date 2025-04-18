package com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Nodes;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicNodeFinder {
    //LogicNodeManager Instance
    private LogicNodeManager nodeManager;

    public LogicNodeFinder(LogicNodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    // Find a LogicNode by its ID
    public LogicNode findNodeById(String id) {
        for (LogicNode node : nodeManager.getNodes()) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    //Find a LogicNode by its object
    public LogicNode findNodeByObject(LogicNode node) {
        for (LogicNode logicNode : nodeManager.getNodes()) {
            if (logicNode != null && logicNode.equals(node)) {
                return logicNode;
            }
        }
        return null;
    }
}
