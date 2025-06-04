package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

public class RPCSimulatorBridge {
    //RPCSimulator Instance
    private RPCSimulatorManager rpcSimulator;

    private volatile double rpcSimulatorState = 0.0;
    private volatile double rpcSimulatorTime = 0.0;
    private volatile double rpcSimulatorSampleRate = 0;
    private volatile double rpcSimulatorBlockLength = 0;

    private volatile double rpcSimulatorTimeStep = 0.0;

    private Map<String,Double> simulationProperties;

    private void StoreParamsInMap(){
        simulationProperties.put("RPCSimulatorState", rpcSimulatorState);
        simulationProperties.put("RPCSimulatorTime", rpcSimulatorTime);
        simulationProperties.put("RPCSimulatorSampleRate", rpcSimulatorSampleRate);
        simulationProperties.put("RPCSimulatorBlockLength", rpcSimulatorBlockLength);
        simulationProperties.put("RPCSimulatorTimeStep", rpcSimulatorTimeStep);
    }

    public Map<String,Double> getSimulationProperties() {
        return simulationProperties;
    }

    public void ModifySimulationProperties(JSONObject object){
        this.rpcSimulatorState = object.getDouble("RPCSimulatorState");
        this.rpcSimulatorTime = object.getDouble("RPCSimulatorTime");
        this.rpcSimulatorSampleRate = object.getInt("RPCSimulatorSampleRate");
        this.rpcSimulatorBlockLength = object.getInt("RPCSimulatorBlockLength");

        this.rpcSimulatorTimeStep = object.getDouble("RPCSimulatorTimeStep");

        StoreParamsInMap();
    }

    public RPCSimulatorBridge(RPCSimulatorManager rpcSimulator) {
        this.rpcSimulator = rpcSimulator;
        this.simulationProperties = new ConcurrentHashMap<>();
    }
}
