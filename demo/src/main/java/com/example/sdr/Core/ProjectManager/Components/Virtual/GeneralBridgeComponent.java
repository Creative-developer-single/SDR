package com.example.sdr.Core.ProjectManager.Components.Virtual;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

import com.example.sdr.Core.Components.Tools.PropertyExporter.PropertyJSONExporter;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class GeneralBridgeComponent extends BaseComponent{
    public static final String PYTHON_SERVER_ADDRESS = "172.27.234.221";
    //public static final String PYTHON_SERVER_ADDRESS = "10.29.232.171";
    public static final String PYTHON_SERVER_PORT = "19000";

    //TCP Client
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    //Python Server Address
    private String pythonServerAddress;
    private String pythonServerPort;

    //Component Properties
    private String componentName;
    private JSONObject componentConfig;

    private void createTCPClient(){
        try{
            clientSocket = new Socket(pythonServerAddress, Integer.parseInt(pythonServerPort));
            System.out.println("Connected to Python server at " + pythonServerAddress + ":" + pythonServerPort);

            // Get the OutputStream
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            //Get the InputStream
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.out.println("Error creating TCP client: " + e.getMessage());
        }
    }

    /*
     * Method: openTCPConnection
     * Description: Opens a TCP connection to the Python server.
     * Parameters: None
     * Returns: Boolean - true if the connection is open, false otherwise.
     * Throws: IOException - if an error occurs while opening the connection.
     * Note: This method checks if the clientSocket is null or closed before creating a new TCP client.
     */
    private void openTCPConnection(){
        try{
            if(clientSocket == null || clientSocket.isClosed()){
                createTCPClient();
            }
        }catch(Exception e){
            System.out.println("Error opening TCP connection: " + e.getMessage());
        }
    }

    /*
     * Method: closeTCPConnection
     * Description: Closes the TCP connection to the Python server.
     * Parameters: None
     * Returns: None
     * Throws: IOException - if an error occurs while closing the connection.
     * Note: This method checks if the clientSocket is not null and not closed before attempting to close it.
     */
    private void closeTCPConnection(){
        try{
            if(clientSocket != null && !clientSocket.isClosed()){
                clientSocket.close();
                System.out.println("TCP connection closed.");
            }
        }catch(Exception e){
            System.out.println("Error closing TCP connection: " + e.getMessage());
        }
    }

    /*
     * Method: checkTCPConnection
     * Description: Checks if the TCP connection to the Python server is open.
     * Parameters: None
     * Returns: Boolean - true if the connection is open, false otherwise.
     * Throws: IOException - if an error occurs while checking the connection.
     * Note: This method checks if the clientSocket is not null and not closed before returning true.
     */
    private Boolean checkTCPConnection(){
        try{
            if(clientSocket != null && !clientSocket.isClosed()){
                return true;
            }
        }catch(Exception e){
            System.out.println("Error checking TCP connection: " + e.getMessage());
        }
        return false;
    }

    private void loadConfig(){
        if(componentConfig != null){
            try{
                componentConfig.put("CmdType", "load_config");
                componentConfig.put("ModuleName", componentName);
                componentConfig.put("input_channels_num", inputCount);
                componentConfig.put("block_size",blockLength);

                String sendStr = "CMDE"+componentConfig.toString(2)+"\r\n\r\n"+"DATAPAYLOAD";
            
                if(checkTCPConnection()){
                    writer.println(sendStr);
                    writer.flush();
                    System.out.println("Loaded config sent to Python server.");
                }else{
                    //If TCP connection is not open, create a new one
                    openTCPConnection();
                    writer.println(sendStr);
                    writer.flush();
                    System.out.println("Loaded config sent to Python server.");
                }
            }catch(Exception e){
                System.out.println("Error loading config: " + e.getMessage());
            }
        }
    }

    public void setOperationParams(double[] data,int index)
    {
        op_in[index] = data;
        load_data(data,index);
    }

    private void load_data(double data[],int index){
        //Build the Data Str
        String dataStr = "[";
        for(int i=0;i<blockLength-1;i++)
        {
            String tmp = String.valueOf(data[i]);
            dataStr = dataStr + tmp +',';
        }
        dataStr = dataStr + String.valueOf(data[blockLength-1]) + "]";

        String sendStr = "CMDE";
        componentConfig.put("CmdType","load_data");
        sendStr = sendStr + componentConfig.toString(2);
        sendStr = sendStr + "\r\n\r\n" + dataStr;

        //Send to the Server
        try{
            if(checkTCPConnection())
            {
                writer.println(sendStr);
                writer.flush();
                System.out.println("Send: " + sendStr);
                System.out.println("Loaded config sent to Python server.");
            }else{
                //If TCP connection is not open, create a new one
                openTCPConnection();
                writer.println(sendStr);
                writer.flush();
                System.out.println("Send: " + sendStr);
                System.out.println("Loaded config sent to Python server.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void Calculate(){
        //Build the Cmd Str
        String cmdStr = "CMDE";
        componentConfig.put("CmdType","process");
        cmdStr = cmdStr + componentConfig.toString(2);
        cmdStr = cmdStr + "\r\n\r\n" + "DATAPAYLOAD";

        //Send to the Server
        try{
            if(checkTCPConnection())
            {
                writer.println(cmdStr);
                writer.flush();
                System.out.println("Send: " + cmdStr);
                System.out.println("Loaded config sent to Python server.");
            }else{
                //If TCP connection is not open, create a new one
                openTCPConnection();
                writer.println(cmdStr);
                writer.flush();
                System.out.println("Send: " + cmdStr);
                System.out.println("Loaded config sent to Python server.");
            }
        }catch(Exception e){
            System.out.println("Error sending command: " + e.getMessage());
        }
    }

    public double[] createTestData(){
        //Create a test data
        double[] testData = new double[blockLength];
        for(int i=0;i<blockLength;i++){
            testData[i] = Math.random();
        }

        //Set the test data
        setOperationParams(testData, 0);
        System.out.println("Test data created and set.");
        return testData;
    }

    private void sendLoadedConfig(){
        try{
            //Get the Loaded Config from Class
            JSONObject loadedConfig = PropertyJSONExporter.exportToJson(this);

            //Add the Ctrl Frame
            loadedConfig.put("CmdType", "load_config");

            if(checkTCPConnection()){
                writer.println(loadedConfig.toString(2));
                writer.flush();
                System.out.println("Loaded config sent to Python server.");
            }else{
                //If TCP connection is not open, create a new one
                openTCPConnection();
                writer.println(loadedConfig.toString(2));
                writer.flush();
                System.out.println("Loaded config sent to Python server.");
            }
        }catch(Exception e){
            System.out.println("Error sending loaded config: " + e.getMessage());
        }
    }

    public void setComponentConfig(JSONObject config){
        this.componentConfig = config;
    }

    public GeneralBridgeComponent(int blockLength, int inputCount, String ID) {
        super(blockLength, inputCount, ID);
        this.pythonServerAddress = PYTHON_SERVER_ADDRESS;
        this.pythonServerPort = PYTHON_SERVER_PORT;
        this.componentName = ID;

        //createTCPClient();
    }

    public GeneralBridgeComponent(int blockLength,int inputCount,String ID,JSONObject config){
        super(blockLength, inputCount, ID);
        this.pythonServerAddress = PYTHON_SERVER_ADDRESS;
        this.pythonServerPort = PYTHON_SERVER_PORT;
        this.componentName = ID;
        this.componentConfig = config;

        //createTCPClient();
    }
    public static void main(String[] args) {
        GeneralBridgeComponent bridgeComponent = new GeneralBridgeComponent(100, 1, "AGC");
        
        //Create a sample config
        JSONObject config = new JSONObject();
        config.put("CmdType", "load_config");
        config.put("ModuleName", "AGC");
        config.put("input_channels_num", 1);
        config.put("output_channels_num", 1);
        config.put("block_size", 100);
        config.put("current_input_channel_index", 0);
        config.put("current_output_channel_index", 0);
        config.put("level",1);
        config.put("max_gain",20);
        config.put("win_length", 100);

        //Set the config
        bridgeComponent.setComponentConfig(config);

        try{
            //Load the config
            bridgeComponent.loadConfig();
            Thread.sleep(1000);
            double[] data =  bridgeComponent.createTestData();
            bridgeComponent.setOperationParams(data, 0);
            Thread.sleep(1000);
            bridgeComponent.Calculate();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        
        //JSONObject loadedConfig = PropertyJSONExporter.exportToJson(bridgeComponent);
        //loadedConfig.put("CmdType", "load_config");

        //System.out.println(loadedConfig.toString(2));
    }   
}
