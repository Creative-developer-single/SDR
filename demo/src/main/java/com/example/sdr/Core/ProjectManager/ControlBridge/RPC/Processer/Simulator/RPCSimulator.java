package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.Simulator;

import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.Simulation.Simulator;

public class RPCSimulator {
    RPCProcesser processer;
    Simulator simulator;

    public void RPCStartSimulation(){
        
    }

    public RPCSimulator(RPCProcesser processer) {
        this.processer = processer;
        this.simulator = processer.getProjectManager().getSimulator();
    }
}
