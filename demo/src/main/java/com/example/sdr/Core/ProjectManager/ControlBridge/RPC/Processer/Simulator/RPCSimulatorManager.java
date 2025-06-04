package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator.LifeCycle.RPCSimulationLifeCycle;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator.Properties.RPCSimulationProperties;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class RPCSimulatorManager {
    private RPCProcesser processer;
    private Simulator simulator;

    // 获取RPCSimulatorLifeCycle实例
    private RPCSimulationLifeCycle rpcSimulationLifeCycle;

    // 获取RPCSimulatorProperties实例
    private RPCSimulationProperties rpcSimulationProperties;

    public void RPCStartSimulation(){
        
    }

    public void RPCCall(JSONObject object,Integer rpcID){
        try{
            // 获取调用命令
            String command = object.getString("Command");

            // 获取参数
            JSONObject args = object.getJSONObject("Args");

            // 根据命令执行不同的操作
            switch(command){
                case "RPCModifySimulationStatus":
                    rpcSimulationLifeCycle.ModifySimulationStatus(args);
                    break;
                case "RPCSetSimulationParameter":
                    rpcSimulationProperties.setSimulationParameter(args);
                    break;
                default:
                    System.out.println("Not available in this process");
                    break;
            }

            // 回复响应，特殊响应在switch中处理
            JSONObject response = new JSONObject();
            JSONObject replyFrame = new JSONObject();
            replyFrame.put("FrameID", rpcID);
            replyFrame.put("ReplyType", "string");
            replyFrame.put("Reply", "success");

            response.put("RPCFrame", replyFrame);
            processer.getProjectManager().getRPCManager().SendReply("CTRL" + response.toString());

        }catch(Exception e){
            System.out.println("Simulation RPCCall Error: " + e.getMessage());
        }
    }

    /*
     * @brief 获取Simulator实例
     * @return Simulator
     */
    public Simulator getSimulator() {
        return simulator;
    }

    public RPCSimulatorManager(RPCProcesser processer) {
        this.processer = processer;
        this.simulator = processer.getProjectManager().getSimulator();
        this.rpcSimulationLifeCycle = new RPCSimulationLifeCycle(this);
        this.rpcSimulationProperties = new RPCSimulationProperties(this);
    }
}
