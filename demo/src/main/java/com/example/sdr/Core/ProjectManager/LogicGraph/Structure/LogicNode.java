package com.example.sdr.Core.ProjectManager.LogicGraph.Structure;

import java.util.ArrayList;
import java.util.List;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class LogicNode {
    final int MAX_EDGES = 100;

    private String id;
    private BaseComponent component;
    private int nextEdgesPoint;
    private int prevEdgesPoint;
    private List<LogicEdge> nextEdges;
    private List<LogicEdge> prevEdges;

    private int unVisitedInDegrees;
    private int unVisitedOutDegrees;

    public LogicNode(BaseComponent component,String id){
        this.id = id;
        this.component = component;
        this.nextEdges = new ArrayList<LogicEdge>();
        this.prevEdges = new ArrayList<LogicEdge>();
        this.nextEdgesPoint = 0;
        this.prevEdgesPoint = 0;
        this.unVisitedInDegrees = 0;
        this.unVisitedOutDegrees = 0;
    }

    public BaseComponent getComponent(){
        return component;
    }

    //Set the ID
    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

    public void setComponent(BaseComponent component){
        this.component = component;
    }

    public void reset(){
        resetNextEdges();
        resetPrevEdges();
        resetDegrees();
    }

    public void resetPrevEdges(){
        this.prevEdges.clear();
    }

    public void resetNextEdges(){
        this.nextEdges.clear();
    }

    public void resetDegrees(){
        this.unVisitedInDegrees = prevEdges.size();
        this.unVisitedOutDegrees = nextEdges.size();
    }

    public int getUnVisitedInDegrees(){
        return unVisitedInDegrees;
    }

    public int getUnVisitedOutDegrees(){
        return unVisitedOutDegrees;
    }

    public void decrementUnVisitedInDegrees(){
        unVisitedInDegrees--;
    }

    public void decrementUnVisitedOutDegrees(){
        unVisitedOutDegrees--;
    }

    public void incrementUnVisitedInDegrees(){
        unVisitedInDegrees++;
    }

    public void incrementUnVisitedOutDegrees(){
        unVisitedOutDegrees++;
    }

    public int getNextEdgesCount(){
        return nextEdges.size();
    }

    public int getPrevEdgesCount(){
        return prevEdges.size();
    }
   
    //Get the next edge by order
    public LogicEdge getNextEdgeByOrder()
    {
        if(nextEdgesPoint < nextEdges.size()){
            return nextEdges.get(nextEdgesPoint);
        }
        return null;
    }

    private LogicEdge findNextEdge(LogicEdge edge){
        for (int i = 0; i < nextEdges.size(); i++) {
            if(nextEdges.get(i).equals(edge)){
                return nextEdges.get(i);
            }
        }
        return null;   
    }

    private LogicEdge findPrevEdge(LogicEdge edge){
        for (int i = 0; i < prevEdges.size(); i++) {
            if(prevEdges.get(i).equals(edge)){
                return prevEdges.get(i);
            }
        }
        return null;   
    }

    //Add an edge to the node
    public void addNextEdge(LogicEdge edge){
        if(findNextEdge(edge) == null){
            nextEdges.add(edge);
            this.incrementUnVisitedOutDegrees();
        }
    }

    //Add an edge to the node
    public void addPrevEdge(LogicEdge edge)
    {
        if(findPrevEdge(edge) == null){
            prevEdges.add(edge);
            this.incrementUnVisitedInDegrees();
        }
    }

    public List<LogicEdge> getNextEdges(){
        return nextEdges;
    }

    public List<LogicEdge> getPrevEdges(){
        return prevEdges;
    }

    public String getId(){
        return id;
    }

}
