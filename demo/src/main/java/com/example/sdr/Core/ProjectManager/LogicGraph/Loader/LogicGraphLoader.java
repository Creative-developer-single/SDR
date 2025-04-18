package com.example.sdr.Core.ProjectManager.LogicGraph.Loader;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.python.netty.handler.codec.json.JsonObjectDecoder;

import com.example.sdr.Core.Components.Tools.PropertyModifier.AutoPropertyModifier;
import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.Components.Arithmetic.*;
import com.example.sdr.Core.ProjectManager.Components.Base.*;
import com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer.*;
import com.example.sdr.Core.ProjectManager.Components.Source.SignalGenerator;
import com.example.sdr.Core.ProjectManager.Components.Tools.ComponentCreator;
import com.example.sdr.Core.ProjectManager.Components.Virtual.GeneralBridgeComponent;
import com.example.sdr.Core.ProjectManager.LogicGraph.LogicGraphManager;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.*;

public class LogicGraphLoader {
    //Json File Path
    private String jsonPath;

    //LogicDirectedGraph
    private LogicGraphManager manager;

    //Load from the JSONObject
    private JSONObject graphObject;

    //Map Store
    HashMap<String, LogicNode> nodeMap = new HashMap<>();

    public LogicGraphLoader(){
        this.jsonPath = null;
        this.manager = null;
    }

    public LogicGraphLoader(LogicGraphManager manager){
        this.jsonPath = null;
        this.manager = manager;
    }

    public LogicGraphLoader(String jsonPath,LogicGraphManager manager){
        this.jsonPath = jsonPath;
        this.manager = manager;
    }

    public void setJSONPath(String jsonPath){
        this.jsonPath = jsonPath;
    }

    public void setJSONObject(JSONObject jsonObject){
        this.graphObject = jsonObject;
    }

    private LogicNode createNode(int BlockLength,int SampleRate,String ID,String ComponentType,String ComponentID){
        switch(ComponentType){
            case "BaseComponent":
                //Create the BaseComponent
                BaseComponent component = new BaseComponent(BlockLength, 1,1,ComponentID);
                return new LogicNode(component, ID);
            case "BasicALU":
                //Create the BasicALU
                BasicALU basicALU = new BasicALU(BlockLength,1,1,ComponentID);
                return new LogicNode(basicALU, ID);
            case "NoLinear":
                //Create the NoLinear
                NoLinear noLinear = new NoLinear(BlockLength, 1,1,ComponentID);
                return new LogicNode(noLinear, ID);
            case "SinglePortBuffer":
                //Create the SinglePortBuffer
                SinglePortBuffer singlePortBuffer = new SinglePortBuffer(BlockLength, BlockLength,ComponentID);
                return new LogicNode(singlePortBuffer, ID);
            case "SignalGenerator":
                //Create the SignalGenerator
                SignalGenerator signalGenerator = new SignalGenerator(BlockLength,SampleRate,ID);
                return new LogicNode(signalGenerator, ID);
            case "GeneralBridgeComponent":
                //Create the GeneralBridgeComponent
                GeneralBridgeComponent generalBridgeComponent = new GeneralBridgeComponent(BlockLength, 1,1,ComponentID);
                return new LogicNode(generalBridgeComponent, ID);
            default:
                return null;
        }
    }

    public void createFromJSONObject(){
        try{
            LogicGraphStructureManager graph = manager.getGraphInstance();

            JSONArray nodes = graphObject.getJSONArray("Nodes");

            ComponentCreator creator = new ComponentCreator();

            //For Each Node
            for(int i=0;i<nodes.length();i++)
            {
                JSONObject node = nodes.getJSONObject(i);

                Object object = creator.createComponentByClassName(node);

                LogicNode newNode = new LogicNode((BaseComponent)object, node.getString("ID"));

                graph.addNode(newNode);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void LoadFromJSONObject(){
        try{
            LogicGraphStructureManager graph = manager.getGraphInstance();

            int blockLength = graphObject.getInt("BlockLength");
            int SampleRate = graphObject.getInt("SampleRate");

            //Decode the Nodes
            //Decode the NodesArray
            JSONArray nodes = graphObject.getJSONArray("Nodes");
            
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

                //Get the Component Binded By the Node
                BaseComponent component = (BaseComponent) logicNode.getComponent();

                //Modify the Special Parameters
                JSONObject componentSettings = node.getJSONObject("ComponentSettings");

                if(componentType.equals("GeneralBridgeComponent")){
                    ((GeneralBridgeComponent)component).setComponentConfig(componentSettings);
                }else
                {
                    AutoPropertyModifier.setPropertiesFromJson(component, componentSettings);
                }
                
                //Add the Node to the Graph
                graph.addNode(logicNode);
            }

            //Decode the Edges
            //Decode the EdgesArray
            JSONArray edges = graphObject.getJSONArray("Edges");

            //For Each Edge
            for(int i=0;i<edges.length();i++)
            {
                //Get the Edge Object
                JSONObject edge = edges.getJSONObject(i);

                //Get the Edge ID
                String edgeID = edge.getString("EdgeID");

                //Get the Start and End Node ID
                String sourceID = edge.getString("StartNodeID");
                String destinationID = edge.getString("EndNodeID");

                //Get the Start and End Edge Index
                int startEdgeIndex = edge.getInt("StartEdgeIndex");
                int endEdgeIndex = edge.getInt("EndEdgeIndex");

                LogicNode startNode = graph.getNodeManager().getFinder().findNodeById(destinationID);
                LogicNode endNode = graph.getNodeManager().getFinder().findNodeById(destinationID);

                //Add the Edge
                graph.addEdge(edgeID, startNode, startEdgeIndex,endNode,endEdgeIndex);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void LoadFromJSON(){
        //Load the JSON File
        try{
            LogicGraphStructureManager graph = manager.getGraphInstance();

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

                //Get the Component Binded By the Node
                BaseComponent component = (BaseComponent) logicNode.getComponent();

                //Modify the Special Parameters
                JSONObject componentSettings = node.getJSONObject("ComponentSettings");
                AutoPropertyModifier.setPropertiesFromJson(component, componentSettings);

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
                String edgeID = edge.getString("EdgeID");

                //Get the Start and End Node ID
                String sourceID = edge.getString("StartNodeID");
                String destinationID = edge.getString("EndNodeID");

                //Get the Start and End Edge Index
                int startEdgeIndex = edge.getInt("StartEdgeIndex");
                int endEdgeIndex = edge.getInt("EndEdgeIndex");

                //Get the Start and End Node By ID
                LogicNode startNode = graph.getNodeManager().getFinder().findNodeById(destinationID);
                LogicNode endNode = graph.getNodeManager().getFinder().findNodeById(destinationID);

                //Add the Edge
                graph.addEdge(edgeID, startNode, startEdgeIndex,endNode,endEdgeIndex);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setLogicGraphManager(LogicGraphManager manager){
        this.manager = manager;
    }
}
