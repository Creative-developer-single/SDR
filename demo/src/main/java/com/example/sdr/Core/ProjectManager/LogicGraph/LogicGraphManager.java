package com.example.sdr.Core.ProjectManager.LogicGraph;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.LogicGraph.Loader.LogicGraphLoader;
import com.example.sdr.Core.ProjectManager.LogicGraph.Reporter.LogicGraphReporter;
import com.example.sdr.Core.ProjectManager.LogicGraph.Schedule.LogicGraphScheduler;
import com.example.sdr.Core.ProjectManager.LogicGraph.Structure.LogicGraphStructureManager;

public class LogicGraphManager {
    //Graph Structure
    private LogicGraphStructureManager graph;

    //Graph Scheduler
    private LogicGraphScheduler scheduler;

    //Graph Structer Loader
    private LogicGraphLoader loader;

    //Graph Reporter
    private LogicGraphReporter reporter;

    //Load the JSON Format Graph
    public void loadGraphFromJSON(String jsonPath){
        // 先清除现有图
        graph.clearGraph();
        loader.setJSONPath(jsonPath);
        loader.createFromJSONObject();
    }

    public void loadGraphFromJSON(JSONObject object){
        // 先清除现有图
        graph.clearGraph();
        loader.setJSONObject(object);
        loader.createFromJSONObject();
    }

    /*
     * 函数名称：resetGraph
     * 描述：重置图结构，重新加载调度信息
     */
    public void resetGraph(){
        clearScheduler();
        createScheduler();
    }

    /*
     * Function Name: createScheduler
     * Description: 为调度器创建调度信息
     */
    public void createScheduler(){
        // 先清除一边调度器保证无干扰
        clearScheduler();

        // 重新从结构体中添加节点和边
        scheduler.addNodesToNodeQueue(graph.getNodes());

        // 生成调度顺序，这里关键在于节点已经绑定边信息了
        scheduler.generateScheduleOrder();
    }

    /*
     * 函数名称: clearScheduler
     * 描述: 清除调度器，重置节点信息
     * 注意: 这个函数会清除调度器中的所有信息，包括节点和边的入度和出度信息等
     *       调用后需要重新创建调度器
     *       这个函数不会清除图结构中的节点和边，只是清除调度器中的信息
     */
    public void clearScheduler(){
        //清除调度器
        scheduler.clearScheduler();

        // 重置节点，包括节点中存储的入度和出度信息等
        graph.resetNodes();
    }

    //Run a Schedule Cycle
    public void runScheduler(){
        scheduler.runTheScheduler();
    }

    //Update the Reporter
    public void updateReporter(){
        reporter.setNodes(graph.getNodes());
        reporter.setEdges(graph.getEdges());
    }

    // refresh All the Components
    public void refreshAllComponents(){
        graph.refreshComponents();
    }

    public LogicGraphStructureManager getGraphInstance(){
        return graph;
    }

    public LogicGraphScheduler getSchedulerInstance(){
        return scheduler;
    }

    public LogicGraphReporter getReportInstance(){
        return reporter;
    }

    //Get the Reported Nodes and Edges
    public void getReportedNode(){
        reporter.getNodes();
    }

    public void getReportedEdges(){
        reporter.getEdges();
    }

    public LogicGraphManager(){
        graph = new LogicGraphStructureManager();
        scheduler = new LogicGraphScheduler(this);
        loader = new LogicGraphLoader(this);
        reporter = new LogicGraphReporter();
    }
}
