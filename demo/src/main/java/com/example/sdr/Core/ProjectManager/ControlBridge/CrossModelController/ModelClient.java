package com.example.sdr.Core.ProjectManager.ControlBridge.CrossModelController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.python.antlr.base.mod;

public class ModelClient {
    //ProcessBuilder Instance
    private ProcessBuilder processBuilder;
    private Process process;

    //Python Server Path
    private String pythonServerPath;
    private String pythonServerHTTPAddress;
    private String pythonServerHTTPPort;

    //MultiThread Support
    private Thread outputThread;

    public ModelClient(){
        processBuilder = null;
        process = null;
        pythonServerPath = null;
        pythonServerHTTPAddress = null;
        pythonServerHTTPPort = null;
    }

    public void setPythonServerPath(String path){
        this.pythonServerPath = path;
    }

    public void StartPythonServer() throws IOException{
        processBuilder = new ProcessBuilder("python3","-u",pythonServerPath);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();

        outputThread = new Thread(()->{
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while((line = reader.readLine()) != null){
                    System.out.println(line);
                }
            }catch(Exception e){
                System.out.println("Python Server Failed to Start");
                e.printStackTrace();
            }
        });

        outputThread.start();

        //Add a Shutdown Listener
        new Thread(()->{
            try{
                int exitCode = process.waitFor();
                System.out.println("Python Server Exited with Code: " + exitCode);
                outputThread.interrupt();
            }catch(Exception e){
                System.out.println("Python Server Failed to Start");
                e.printStackTrace();
            }
        }).start();
    }

    public void stopPythonServer(){
        if(process != null && process.isAlive()){
            process.destroy();
            System.out.println("Python Server Stopped");
        }
    }

    public static void main(String[] args) {
        ModelClient modelClient = new ModelClient();
        modelClient.setPythonServerPath("/home/chengzirui/workspace/PyCharmProject/SDR/main.py");

        try {
            modelClient.StartPythonServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
