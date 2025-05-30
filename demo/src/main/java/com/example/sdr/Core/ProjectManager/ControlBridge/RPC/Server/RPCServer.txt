package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.RPCManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;

public class RPCServer {
    public final int MAIN_PORT = 9000;
    public final int DATA_PORT = 9001;

    private int mainPort = MAIN_PORT;

    //Generate the Socket
    private ServerSocket mainSocket;
    private ServerSocket dataSocket;

    //RPCManager Instance
    private RPCManager manager;

    //RPC Processer Instance
    private RPCProcesser processer;

    private void handleControlSocket(Socket socket) {
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            StringBuilder sb = new StringBuilder();
            int prev = -1, curr;
    
            while ((curr = isr.read()) != -1) {
                sb.append((char) curr);
    
                if (prev == 'E' && curr == 'D') {
                    // 去掉末尾的 \r\n
                    String inputLine = sb.toString().substring(0, sb.length() - 2);
                    System.out.println("Received Message: " + inputLine);
    
                    // 调用 processer
                    processer.AddRPCFrameToQueue(inputLine);
                    writer.println("ACK from the Server: " + inputLine);
    
                    // 清空 StringBuilder 准备下一行
                    sb.setLength(0);
                    prev = -1;
                    continue;
                }
    
                prev = curr;
            }
        } catch (Exception e) {
            System.out.println("Control Socket Connection Failed");
            e.printStackTrace();
        }
    }
    

    //Generate the mainListener
    private void mainListener(){
       try{
            mainSocket = new ServerSocket(mainPort);
            System.out.println("Control Socket Created Port at " + MAIN_PORT); 
            while(true){
                Socket socket = mainSocket.accept();
                System.out.println("Control Socket Connection Established");

                new Thread(()->handleControlSocket(socket)).start();
            }
        }catch(Exception e){
            System.out.println("Control Socket Connection Failed");
        }
    }
    
    public void Start(){
        //Start the Server
        new Thread(() -> mainListener()).start();
    }

    public RPCServer(){
        mainSocket = null;
        dataSocket = null;
    }

    public RPCServer(int mainPort,RPCManager manager){
        this.mainPort = mainPort;
        mainSocket = null;
        dataSocket = null;
        this.manager = manager;
        this.processer = manager.getProcesser();
    }

    public static void main(String[] args){
        RPCServer server = new RPCServer();
        server.Start();
    }
}
