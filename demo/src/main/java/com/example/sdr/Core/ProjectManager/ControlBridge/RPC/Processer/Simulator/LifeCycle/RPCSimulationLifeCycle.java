package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator.LifeCycle;

import org.json.JSONObject;

import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator.RPCSimulatorManager;

public class RPCSimulationLifeCycle {
    // 获取RPCSimulationManager实例
    private RPCSimulatorManager rpcSimulatorManager;

    // 改变生命周期
    public void ModifySimulationStatus(JSONObject object){
        try{
            String status = object.getString("Status");
            switch(status) {
                case "Start":
                    StartSimulation();
                    break;
                case "Stop":
                    StopSimulation();
                    break;
                default:
                    System.out.println("Invalid status command: " + status);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error modifying simulation status: " + e.getMessage());
        }
    }

    public void StartSimulation() {
        rpcSimulatorManager.getSimulator().startSimulation();
    }

    public void StopSimulation() {
        rpcSimulatorManager.getSimulator().stopSimulation();
    }
    

    public RPCSimulationLifeCycle(RPCSimulatorManager manager) {
        this.rpcSimulatorManager = manager;
    }
}
