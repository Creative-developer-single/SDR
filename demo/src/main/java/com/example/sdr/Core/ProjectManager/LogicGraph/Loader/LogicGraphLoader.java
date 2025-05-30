package com.example.sdr.Core.ProjectManager.LogicGraph.Loader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.Components.Base.*;
import com.example.sdr.Core.ProjectManager.Components.Tools.ComponentCreator;
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


    public JSONObject getGraphObject() {
        return graphObject;
    }

    public void setJSONPath(String path){
        this.jsonPath = path;
        // 从JSON文件中读取内容
        try {
            String content = new String(Files.readAllBytes(Paths.get(jsonPath)));
            this.graphObject = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setJSONObject(JSONObject jsonObject){
        this.graphObject = jsonObject;
    }

    /*
     * 函数名称：createFromJSONObject
     * 描述：从JSON对象中创建逻辑图结构
     */
    public void createFromJSONObject(){
        try{
            LogicGraphStructureManager graph = manager.getGraphInstance();

            JSONArray nodes = graphObject.getJSONArray("Nodes");
            JSONArray edges = graphObject.getJSONArray("Edges");

            ComponentCreator creator = new ComponentCreator();

            //For Each Node
            for(int i=0;i<nodes.length();i++)
            {
                JSONObject node = nodes.getJSONObject(i);

                Object object = creator.createComponentByClassName(node);

                LogicNode newNode = new LogicNode((BaseComponent)object, node.getInt("ID"));

                graph.getNodeManager().getNodes().add(newNode);
            }

            //for Each Edge
            for(int i=0;i<edges.length();i++)
            {
                JSONObject edge = edges.getJSONObject(i);

                //Get the Start and End Node ID
                Integer ID = edge.getInt("EdgeID");
                Integer edgeID = ID;
                
                Integer sourceID = edge.getInt("StartNodeID");
                Integer destinationID = edge.getInt("EndNodeID");

                //Get the Start and End Edge Index
                int startEdgeIndex = edge.getInt("StartEdgeIndex");
                int endEdgeIndex = edge.getInt("EndEdgeIndex");

                LogicNode startNode = graph.getNodeManager().getFinder().findNodeById(sourceID);
                LogicNode endNode = graph.getNodeManager().getFinder().findNodeById(destinationID);

                //Add the Edge
                graph.getEdgeManager().getModifier().addLogicEdge(edgeID, startNode, startEdgeIndex,endNode,endEdgeIndex);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void setLogicGraphManager(LogicGraphManager manager){
        this.manager = manager;
    }
}
