package com.example.sdr.Core.ProjectManager.LogicGraph.Schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.*;

public class LogicGraphScheduler {
    //Node Queue
    private Queue<LogicNode> nodeQueue;

    //Edge Stack
    private Stack<LogicEdge> edgeStack;

    //Terminal Nodes Stack
    private Stack<LogicNode> terminalNodes;

    public LogicGraphScheduler(){
        nodeQueue = new LinkedList<LogicNode>();
        edgeStack = new Stack<LogicEdge>();
        terminalNodes = new Stack<LogicNode>();
    }

    public void addNodesToNodeQueue(List<LogicNode> nodes){
        for (LogicNode node : nodes) {
            nodeQueue.add(node);
        }
    }

    public void runTheScheduler(){
        //Order is Stored in the Stack
        for(LogicEdge edge : edgeStack){
            //Calculate the Data From Node1
            edge.getNode1().getComponent().Calculate();
            double[] ans = edge.getNode1().getComponent().getAns();

            //Set the Data to Node2
            edge.getNode2().getComponent().setOperationParams(ans,edge.getNode2DataIndex());

            //Check if Node2 is the last Node
            if(edge.getNode2().getNextEdgesCount() == 0){
                terminalNodes.push(edge.getNode2());
            }
        }
    }

    //debug used
    public void printScheduleOrder(){
        System.out.println(edgeStack.size());
        for(LogicEdge edge : edgeStack){
            System.out.println(edge.getNode1().getId() + " -> " + edge.getNode2().getId());
        }
    }

    //debug used
    public void clearDegress(){
        for(LogicNode node : nodeQueue){
            if(node != null){
                node.resetDegrees();
            }
        }
    }

    //debug used
    public void printTerminalNodes(){
        for(LogicNode node : terminalNodes){
            System.out.println(node.getId());
            MatFileWriter writer = new MatFileWriter();
            try {
                MLDouble mlDouble = new MLDouble(node.getId(), node.getComponent().getAns(), node.getComponent().getAns().length);
                ArrayList list = new ArrayList();
                list.add(mlDouble);
                writer.write("/home/chengzirui/workspace/Java/learning/SDR/demo/src/main/resources/dataOutput" + node.getId() + ".mat", list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generateScheduleOrder()
    {
        //Topological Sort
        while(!nodeQueue.isEmpty()){
            LogicNode node = nodeQueue.poll();
            if(node.getUnVisitedInDegrees() == 0){
                for (LogicEdge edge : node.getNextEdges()) {
                    if(edge != null)
                    {
                        edgeStack.push(edge);
                        edge.getNode1().decrementUnVisitedOutDegrees();
                        edge.getNode2().decrementUnVisitedInDegrees();
                    }
                }
            }
        }
    }
}
