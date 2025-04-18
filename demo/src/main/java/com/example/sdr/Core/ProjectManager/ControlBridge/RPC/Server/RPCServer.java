package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {
    public final int MAIN_PORT = 9000;
    public final int DATA_PORT = 9001;


    private int mainPort = MAIN_PORT;

    //Generate the Socket
    private ServerSocket mainSocket;
    private ServerSocket dataSocket;

    private void handleControlSocket(Socket socket){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);

            String inputLine = reader.readLine();
            while(inputLine != null){
                inputLine = reader.readLine();
                System.out.println("Received Message: " + inputLine);
                writer.println("ACK from the Server: "+inputLine);
            }
        }catch(Exception e){
            System.out.println("Control Socket Connection Failed");
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
    
    private void Start(){
        //Start the Server
        new Thread(() -> mainListener()).start();
    }

    public RPCServer(){
        mainSocket = null;
        dataSocket = null;
    }

    public RPCServer(int mainPort){
        this.mainPort = mainPort;
        mainSocket = null;
        dataSocket = null;
    }

    public static void main(String[] args){
        RPCServer server = new RPCServer();
        server.Start();
    }
}
