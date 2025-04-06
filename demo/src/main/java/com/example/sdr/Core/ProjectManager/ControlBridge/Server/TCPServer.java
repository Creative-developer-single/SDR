package com.example.sdr.Core.ProjectManager.ControlBridge.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public final int CONTROL_PORT = 9000;
    public final int DATA_PORT = 9001;

    //Generate the Socket
    private ServerSocket contSocket;
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

    //Generate the ControlListener
    private void ControlListener(){
       try{
            contSocket = new ServerSocket(CONTROL_PORT);
            System.out.println("Control Socket Created Port at " + CONTROL_PORT); 
            while(true){
                Socket socket = contSocket.accept();
                System.out.println("Control Socket Connection Established");

                new Thread(()->handleControlSocket(socket)).start();
            }
        }catch(Exception e){
            System.out.println("Control Socket Connection Failed");
        }
    }
    
    private void Start(){
        //Start the Server
        new Thread(() -> ControlListener()).start();
    }

    public TCPServer(){
        contSocket = null;
        dataSocket = null;
    }

    public static void main(String[] args){
        TCPServer server = new TCPServer();
        server.Start();
    }
}
