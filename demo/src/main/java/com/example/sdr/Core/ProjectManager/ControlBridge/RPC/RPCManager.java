package com.example.sdr.Core.ProjectManager.ControlBridge.RPC;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Server.RPCServer;

public class RPCManager {
    //ProjectManager Instance
    private ProjectManager projectManager;

    //RPC Server Instance
    private RPCServer rpcServer;

    public RPCManager() {
        rpcServer = new RPCServer();
    }
}
