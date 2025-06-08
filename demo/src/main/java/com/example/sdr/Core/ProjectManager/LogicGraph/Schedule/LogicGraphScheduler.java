package com.example.sdr.Core.ProjectManager.LogicGraph.Schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicEdge;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.*;

public class LogicGraphScheduler {
    private LogicGraphManager manager;

    // Node Queue
    private Queue<LogicNode> nodeQueue;

    // Edge Stack
    private Stack<LogicEdge> edgeStack;

    // Terminal Nodes Stack
    private Stack<LogicNode> terminalNodes;

    // Node Schedule List
    private List<LogicNode> nodeScheduleList;

    public LogicGraphScheduler() {
        nodeQueue = new LinkedList<LogicNode>();
        edgeStack = new Stack<LogicEdge>();
        terminalNodes = new Stack<LogicNode>();
    }

    public LogicGraphScheduler(LogicGraphManager manager) {
        this.manager = manager;
        nodeQueue = new LinkedList<LogicNode>();
        edgeStack = new Stack<LogicEdge>();
        terminalNodes = new Stack<LogicNode>();
    }

    public void addNodesToNodeQueue(List<LogicNode> nodes) {
        for (LogicNode node : nodes) {
            nodeQueue.add(node);
        }
    }

    public void clearScheduler() {
        nodeQueue.clear();
        edgeStack.clear();
        terminalNodes.clear();
        if (nodeScheduleList != null) {
            nodeScheduleList.clear();
        }
    }

    public void runSimulationCheck(double perSimulationTime){
        // 运行采样率传播算法
        for (LogicNode node : nodeScheduleList){
            int newSampleRate = node.getComponent().getSampleRate();
            if(node.getComponent().getType() == "SampleRateConverter"){
                // 计算新采样率
                newSampleRate = (int)Math.floor(node.getComponent().getNewSampleRate());
            }

            // 传播到下游节点
            for (LogicEdge edge : node.getNextEdges()){
                if (edge != null){
                    LogicNode targetNode = edge.getNode2();
                    targetNode.getComponent().setSampleRate(newSampleRate);
                }
            }
        }

        // 运行blockLength传播算法
        for (LogicNode node : nodeScheduleList){
            // 直接更新blockLength，只有Driver会检查，其余模块自适应
            if(node.getComponent().getType() == "SampleRateConverter"){
                // 计算新的blockLength
                int newBlockLength = (int)Math.floor(node.getComponent().getNewSampleRate() * perSimulationTime);
                node.getComponent().setBlockLength(newBlockLength);
            }else{
                int newBlockLength = (int)Math.floor(node.getComponent().getSampleRate() * perSimulationTime);
                node.getComponent().setBlockLength(newBlockLength);
            }
        }
    }

    public void runTheScheduler() {
        for (LogicNode node : nodeScheduleList) {
            // 1️⃣ 执行当前节点的计算
            node.getComponent().Calculate();
    
            // 2️⃣ 传递结果到下游节点
            for (LogicEdge edge : node.getNextEdges()) {
                if (edge != null) {
                    // 当前节点的输出
                    SDRData[] ans = node.getComponent().getAns(edge.getNode1DataIndex());
    
                    // 设置到目标节点的输入
                    edge.getNode2().getComponent().setOperationParams(ans, edge.getNode2DataIndex());
                }
            }
    
            // 3️⃣ 如果是终端节点，记录
            if (node.getNextEdgesCount() == 0) {
                terminalNodes.push(node);
            }
        }
    }
    

    public void generateScheduleOrder() {
        nodeScheduleList = new ArrayList<>();

        while (!nodeQueue.isEmpty()) {
            LogicNode node = nodeQueue.peek();

            if (node.getUnVisitedInDegrees() == 0) {
                nodeQueue.poll();
                nodeScheduleList.add(node);

                for (LogicEdge edge : node.getNextEdges()) {
                    if (edge != null) {
                        edgeStack.push(edge); // 保留兼容，供调试/可视化
                        edge.getNode1().decrementUnVisitedOutDegrees();
                        edge.getNode2().decrementUnVisitedInDegrees();
                    }
                }
            } else {
                nodeQueue.add(nodeQueue.poll());
            }
        }
    }

    // Debug: Print Edge Order
    public void printScheduleOrder() {
        System.out.println("Info: Print Schedule Order");
        System.out.println(edgeStack.size());
        for (LogicEdge edge : edgeStack) {
            System.out.println(edge.getNode1().getId() + " -> " + edge.getNode2().getId());
        }
    }

    // Debug: Print Node Order
    public void printNodeScheduleOrder() {
        System.out.println("Info: Print Node Schedule Order");
        for (LogicNode node : nodeScheduleList) {
            System.out.println("Node ID: " + node.getId());
        }
    }

    // Debug: Reset Degrees
    public void clearDegress() {
        for (LogicNode node : nodeQueue) {
            if (node != null) {
                node.resetDegrees();
            }
        }
    }

    // Debug: Print Terminal Nodes
    public void printTerminalNodes() {
        System.out.println("Info: Print Terminal Nodes");
        for (LogicNode node : terminalNodes) {
            System.out.println(node.getId());
            MatFileWriter writer = new MatFileWriter();
            try {
                MLDouble mlDouble = new MLDouble(
                        String.valueOf(node.getClass()) + String.valueOf(node.getId()),
                        SDRDataUtils.toDoubleArray(node.getComponent().getAns(0)),
                        node.getComponent().getAns(0).length
                );
                ArrayList list = new ArrayList();
                list.add(mlDouble);
                writer.write("/home/chengzirui/workspace/Java/learning/SDR/demo/src/main/resources/dataOutput" + node.getId() + ".mat", list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Debug: Print All Buffer Nodes
    public void printAllBufferNodes() {
        List<LogicNode> nodes = manager.getGraphInstance().getNodes();
        MatFileWriter writer = new MatFileWriter();
        ArrayList list = new ArrayList();
        System.out.println("Info: Print All Buffer Nodes");
        for (LogicNode node : nodes) {
            System.out.println(node.getClass().getSimpleName());
            if (node.getComponent().getClass().getName().equals("com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer.SinglePortBuffer")) {
                try {
                    MLDouble mlDouble = new MLDouble(
                            String.valueOf(node.getClass().getSimpleName()) + String.valueOf(node.getId()),
                            SDRDataUtils.toDoubleArray(node.getComponent().getAns(0)),
                            node.getComponent().getAns(0).length
                    );
                    System.out.println("Write the Data");
                    list.add(mlDouble);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.write("/home/chengzirui/workspace/Java/learning/SDR/demo/src/main/resources/dataOutput.mat", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
