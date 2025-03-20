package com.example.sdr.Core.ProjectManager.Loader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.Components.Arithmetic.*;
import com.example.sdr.Core.ProjectManager.Components.Base.*;
import com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer.*;
import com.example.sdr.Core.ProjectManager.Components.Source.SignalGenerator;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.*;

public class GraphStructerLoader {
    //Json File Path
    private String jsonPath;

    //LogicDirectedGraph
    private LogicDirectedGraph graph;

    //Map Store
    HashMap<String, LogicNode> nodeMap = new HashMap<>();

    public GraphStructerLoader(String jsonPath){
        this.jsonPath = jsonPath;
    }

    public GraphStructerLoader(){
        this.jsonPath = null;
    }

    public GraphStructerLoader(String jsonPath,LogicDirectedGraph graph){
        this.jsonPath = jsonPath;
        this.graph = graph;
    }

    public void setJSONPath(String jsonPath){
        this.jsonPath = jsonPath;
    }

    private LogicNode createNode(int BlockLength,int SampleRate,String ID,String ComponentType,String ComponentID){
        switch(ComponentType){
            case "BaseComponent":
                //Create the BaseComponent
                BaseComponent component = new BaseComponent(BlockLength, BlockLength,ComponentID);
                return new LogicNode(component, ID);
            case "BasicALU":
                //Create the BasicALU
                BasicALU basicALU = new BasicALU(BlockLength,ComponentID);
                return new LogicNode(basicALU, ID);
            case "NoLinear":
                //Create the NoLinear
                NoLinear noLinear = new NoLinear(BlockLength, BlockLength,ComponentID);
                return new LogicNode(noLinear, ID);
            case "SinglePortBuffer":
                //Create the SinglePortBuffer
                SinglePortBuffer singlePortBuffer = new SinglePortBuffer(BlockLength, BlockLength,ComponentID);
                return new LogicNode(singlePortBuffer, ID);
            case "SignalGenerator":
                //Create the SignalGenerator
                SignalGenerator signalGenerator = new SignalGenerator(BlockLength,SampleRate,ID);
                return new LogicNode(signalGenerator, ID);
            default:
                return null;
        }
    }

    public void LoadFromJSON(){
        //Load the JSON File
        try{
            //Decode the JSON File
            String content = new String(Files.readAllBytes(Paths.get(jsonPath)));
            JSONObject jsonObject = new JSONObject(content);

            //Get the Global Parameters
            int blockLength = jsonObject.getInt("BlockLength");
            int SampleRate = jsonObject.getInt("SampleRate");

            //Decode the Nodes
            //Decode the NodesArray
            JSONArray nodes = jsonObject.getJSONArray("Nodes");
            
            //For Each Node
            for(int i=0;i<nodes.length();i++)
            {
                //Get the node object
                JSONObject node = nodes.getJSONObject(i);

                //Get the Node ID
                String id = node.getString("ID");
                String componentType = node.getString("ComponentType");
                String componentID = String.valueOf(node.getInt("ComponentID"));

                //Create the Node
                LogicNode logicNode = createNode(blockLength, SampleRate, id, componentType, componentID);

                //Add the Node to the Graph
                graph.addNode(logicNode);
            }

            //Decode the Edges
            //Decode the EdgesArray
            JSONArray edges = jsonObject.getJSONArray("Edges");

            //For Each Edge
            for(int i=0;i<edges.length();i++)
            {
                //Get the Edge Object
                JSONObject edge = edges.getJSONObject(i);

                //Get the Start and End Node ID
                String sourceID = edge.getString("StartNodeID");
                String destinationID = edge.getString("EndNodeID");

                //Get the Start and End Edge Index
                int startEdgeIndex = edge.getInt("StartEdgeIndex");
                int endEdgeIndex = edge.getInt("EndEdgeIndex");

                //Get the Start and End Node By ID
                LogicNode startNode = graph.findNodeByID(sourceID);
                LogicNode endNode = graph.findNodeByID(destinationID);

                //Add the Edge
                graph.addEdgeWithIndex(startNode, startEdgeIndex, endNode, endEdgeIndex);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setLogicDirectedGraph(LogicDirectedGraph graph){
        this.graph = graph;
    }
}
