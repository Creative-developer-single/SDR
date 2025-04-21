package com.example.sdr.Core.ProjectManager.ControlBridge.RPC;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Server.RPCServer;

public class RPCManager {
    //ProjectManager Instance
    private ProjectManager projectManager;

    //RPC Server Instance
    private RPCServer rpcServer;

    //RPC Processer Instance
    private RPCProcesser processer;

    public void StartRPC(){
        //Start the RPC Server
        rpcServer.Start();
    }

    public RPCProcesser getProcesser(){
        return processer;
    }

    public RPCManager(ProjectManager manager){
        this.projectManager = manager;
        this.processer = new RPCProcesser(manager);
        this.rpcServer = new RPCServer(9000,this);
    }
}
