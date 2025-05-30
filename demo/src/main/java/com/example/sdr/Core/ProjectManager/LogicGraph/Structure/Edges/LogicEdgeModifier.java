package com.example.sdr.Core.ProjectManager.LogicGraph.Structure.Edges;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;

public class LogicEdgeModifier {
    private LogicEdgeManager edgeManager;

    public void updateLogicEdgeByJSONObject(LogicEdge edge,JSONObject object){
        // Update the edge properties using the provided JSONObject
        try{
            Integer id = object.getInt("EdgeID");
            Integer startNodeID = object.getInt("StartNodeID");
            Integer endNodeID = object.getInt("EndNodeID");
            int startEdgeIndex = object.getInt("StartEdgeIndex");
            int endEdgeIndex = object.getInt("EndEdgeIndex");

            LogicNode startNode = edgeManager.getManager().getNodeManager().getFinder().findNodeById(startNodeID);
            LogicNode endNode = edgeManager.getManager().getNodeManager().getFinder().findNodeById(endNodeID);

            if (startNode != null && endNode != null) {
                edge.modifyEdgeFull(id, startNode, endNode, startEdgeIndex, endEdgeIndex);
            } else {
                System.out.println("Start or End node not found");
            }   
        }catch(Exception e){
            System.out.println("Error in updating edge properties: " + e.getMessage());
        }
    }

    // Modify an edge by its ID
    public void modifyLogicEdge(JSONObject object){
        Integer index = object.getInt("EdgeID");
        LogicEdge edge = edgeManager.getFinder().findEdgeByID(index);
        if (edge != null) {
            updateLogicEdgeByJSONObject(edge, object);
        } else {
            System.out.println("Edge not found");
        }
    }

    public void addLogicEdge(JSONObject object){
        int id = object.getInt("EdgeID");
        Integer startNodeID = object.getInt("StartNodeID");
        Integer endNodeID = object.getInt("EndNodeID");
        int startEdgeIndex = object.getInt("StartEdgeIndex");
        int endEdgeIndex = object.getInt("EndEdgeIndex");

        LogicNode startNode = edgeManager.getManager().getNodeManager().getFinder().findNodeById(startNodeID);
        LogicNode endNode = edgeManager.getManager().getNodeManager().getFinder().findNodeById(endNodeID);

        if (startNode != null && endNode != null) {
            addLogicEdge(id, startNode, startEdgeIndex, endNode, endEdgeIndex);
        } else {
            System.out.println("Start or End node not found");
        }
    }

    public void addLogicEdge(int ID,LogicNode node1,int startEdgeIndex,LogicNode node2,int endEdgeIndex){
        if(edgeManager.getFinder().findEdge(node1, node2) != null){
            System.out.println("Edge already exists");
            return;
        }
        LogicEdge edge = new LogicEdge(node1,startEdgeIndex,node2, endEdgeIndex ,ID);
        edgeManager.getEdges().add(edge);
        node1.addNextEdge(edge);
        node2.addPrevEdge(edge);
    }

    public void deleteLogicEdge(JSONObject object){
        try{
            Integer edgeID = object.getInt("EdgeID");
            LogicEdge edge = edgeManager.getFinder().findEdgeByID(edgeID);
            if (edge != null) {
                edgeManager.getEdges().remove(edge);
                System.out.println("Edge deleted");
            } else {
                System.out.println("Edge not found");
            }
        }catch(Exception e){
            System.out.println("Error in deleting edge: " + e.getMessage());
        }
    }

    public LogicEdgeModifier(LogicEdgeManager edgeManager) {
        this.edgeManager = edgeManager;
    }
}
