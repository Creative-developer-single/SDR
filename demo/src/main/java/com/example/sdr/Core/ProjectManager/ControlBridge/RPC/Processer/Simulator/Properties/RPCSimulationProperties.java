package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator.Properties;

import org.json.JSONObject;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator.RPCSimulatorManager;

public class RPCSimulationProperties {
    // 获取RPCSimulationManager实例
    private RPCSimulatorManager rpcSimulatorManager;
    // 设置Simulation参数
    public void setSimulationParameter(JSONObject object){
        rpcSimulatorManager.getSimulator().getSimulationProperties().loadFromJSON(object);
        System.out.println("Simulation properties updated: " + object.toString());
    }

    // 构造函数，传入RPCProcesser实例
    public RPCSimulationProperties(RPCSimulatorManager processer) {
        this.rpcSimulatorManager = processer;
    }
}
