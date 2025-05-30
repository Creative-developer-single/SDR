package com.example.sdr.Core.ProjectManager.LogicGraph.Schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.*;

public class LogicGraphScheduler {
    private LogicGraphManager manager;

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

    public LogicGraphScheduler(LogicGraphManager manager){
        this.manager = manager;
        nodeQueue = new LinkedList<LogicNode>();
        edgeStack = new Stack<LogicEdge>();
        terminalNodes = new Stack<LogicNode>();
    }

    public void addNodesToNodeQueue(List<LogicNode> nodes){
        for (LogicNode node : nodes) {
            nodeQueue.add(node);
        }
    }

    public void clearScheduler(){
        nodeQueue.clear();
        edgeStack.clear();
        terminalNodes.clear();
    }

    public void runTheScheduler(){
        //Order is Stored in the Stack
        for(LogicEdge edge : edgeStack){
            //Calculate the Data From Node1
            edge.getNode1().getComponent().Calculate();
            double[] ans = edge.getNode1().getComponent().getAns(0);

            //Set the Data to Node2
            edge.getNode2().getComponent().setOperationParams(ans,edge.getNode2DataIndex());

            //Print the ans
            for(int i = 0; i < ans.length; i++){
                //System.out.println("Node1: " + edge.getNode1().getId() + " -> Node2: " + edge.getNode2().getId() + " : " + ans[i]);
            }

            //Check if Node2 is the last Node
            if(edge.getNode2().getNextEdgesCount() == 0){
                terminalNodes.push(edge.getNode2());
            }
        }
    }

    //debug used
    public void printScheduleOrder(){
        System.out.println("Info: Print Schedule Order");
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
        System.out.println("Info: Print Terminal Nodes");
        for(LogicNode node : terminalNodes){
            System.out.println(node.getId());
            MatFileWriter writer = new MatFileWriter();
            try {
                MLDouble mlDouble = new MLDouble(String.valueOf(node.getClass()) + String.valueOf(node.getId()), node.getComponent().getAns(0), node.getComponent().getAns(0).length);
                ArrayList list = new ArrayList();
                list.add(mlDouble);
                writer.write("/home/chengzirui/workspace/Java/learning/SDR/demo/src/main/resources/dataOutput" + node.getId() + ".mat", list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printAllBufferNodes(){
        List<LogicNode> nodes = manager.getGraphInstance().getNodes();
        MatFileWriter writer = new MatFileWriter();
        ArrayList list = new ArrayList();
        System.out.println("Info: Print All Buffer Nodes");
        for(LogicNode node: nodes){
            System.out.println(node.getClass().getName());
            if(node.getComponent().getClass().getName() =="com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer.SinglePortBuffer"){ 
                try{
                    MLDouble mlDouble = new MLDouble(String.valueOf(node.getClass()) + String.valueOf(node.getId()), node.getComponent().getAns(0), node.getComponent().getAns(0).length);
                    System.out.println("Write the Data");
                    list.add(mlDouble);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        try{
            writer.write("/home/chengzirui/workspace/Java/learning/SDR/demo/src/main/resources/dataOutput.mat", list);
        }catch (Exception e){
            e.printStackTrace();
        }
       
    }

    public void generateScheduleOrder() {
        // Topological Sort
        while (!nodeQueue.isEmpty()) {
            LogicNode node = nodeQueue.peek();  // 仅获取队头节点，不出队
    
            if (node.getUnVisitedInDegrees() == 0) {
                nodeQueue.poll();  // 现在才真正出队
                for (LogicEdge edge : node.getNextEdges()) {
                    if (edge != null) {
                        edgeStack.push(edge);
                        //if(edge.getNode1().getUnVisitedOutDegrees() > 0)
                        edge.getNode1().decrementUnVisitedOutDegrees();
                        //if(edge.getNode2().getUnVisitedInDegrees() > 0)
                        edge.getNode2().decrementUnVisitedInDegrees();
                    }
                }
            } else {
                // 如果入度不为 0，跳过当前节点，继续检查队列中下一个节点
                nodeQueue.add(nodeQueue.poll());  // 将当前节点移到队尾，避免死循环
            }
        }
    }
    
    
}
