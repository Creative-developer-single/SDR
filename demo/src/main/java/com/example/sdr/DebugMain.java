package com.example.sdr;

import com.example.sdr.Core.ProjectManager.Components.Arithmetic.BasicALU;
import com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer.SinglePortBuffer;
import com.example.sdr.Core.ProjectManager.Components.Source.SignalGenerator;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicNode;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class DebugMain {
    
    public static void generateDebugNodes(LogicGraphStructureManager graph){
        //Global Definitions
        int blockLength = 4096;
        int bufferLength = 10000;
        int SampleRate = 10000;

        int freqS1 = 100;
        int freqS2 = 1000;

        //Based on a Simple Chain
        SignalGenerator signalGenerator = new SignalGenerator(blockLength,SampleRate, "SignalGenerator1");
        SignalGenerator signalGenerator2 = new SignalGenerator(blockLength,SampleRate, "SignalGenerator2");

        signalGenerator.setFrequency(freqS1);
        signalGenerator.setAmplitude(1);
        signalGenerator.setPhase(0);
        

        signalGenerator2.setFrequency(freqS2);
        signalGenerator2.setAmplitude(1);
        signalGenerator2.setPhase(0);

        //Adder
        BasicALU adder = new BasicALU(blockLength,1,1,"ALU1");

        //Buffer
        SinglePortBuffer buffer = new SinglePortBuffer(blockLength,bufferLength);

        //Create the Nodes, add the Component to the Node
        LogicNode signalGeneratorNode = new LogicNode(signalGenerator,1);
        LogicNode signalGeneratorNode2 = new LogicNode(signalGenerator2,2);
        LogicNode adderNode = new LogicNode(adder,3);
        LogicNode bufferNode = new LogicNode(buffer,4);

        //Add Nodes to the Graph
        //graph.addNode(signalGeneratorNode);
        //graph.addNode(signalGeneratorNode2);
        //graph.addNode(adderNode);
        //graph.addNode(bufferNode);

        //Add Edges
        //graph.addEdge("0", bufferNode, signalGeneratorNode2);
        //graph.addEdgeWithIndex(signalGeneratorNode,0,adderNode,0);
        //graph.addEdgeWithIndex(signalGeneratorNode2,0,adderNode,1);
        //graph.addEdgeWithIndex(adderNode,0,bufferNode,0);
    }

    public static void main(String[] args) {
        /* 
        //Create the graph and the scheduler
        LogicGraphManager manager = new LogicGraphManager();
        Simulator simulator = new Simulator();

        System.out.println("Debug Main");

        //Debug Nodes
        DebugMain.generateDebugNodes(manager.getGraphInstance());

        //Bind the graph and the scheduler
        simulator.setLogicGraphManager(manager);

        //Run the Scheduler
        simulator.DebugSimulation();
        */
    }
}
