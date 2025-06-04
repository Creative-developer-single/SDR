package com.example.sdr.Core.ProjectManager.Components.Virtual;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.json.JSONObject;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.Components.Tools.ArrayConverter.JSONToDouble;
import com.example.sdr.Core.Components.Tools.PropertyExporter.PropertyJSONExporter;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class GeneralBridgeComponent extends BaseComponent{
    public static final String PYTHON_SERVER_ADDRESS = "172.27.234.221";
    //public static final String PYTHON_SERVER_ADDRESS = "10.29.232.171";
    public static final String PYTHON_SERVER_PORT = "19000";
    public static final int WAIT_TIME = 5;
    public static final int SOCKET_BUFFER = 64*1024;

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

    private void createTCPClient() {
    try {
        // 1. 创建Socket并设置底层缓冲区
        clientSocket = new Socket(pythonServerAddress, Integer.parseInt(pythonServerPort));
        // 设置Socket底层接收/发送缓冲区（操作系统级）
        clientSocket.setReceiveBufferSize(SOCKET_BUFFER); // 64KB接收缓冲区[1,2](@ref)
        clientSocket.setSendBufferSize(SOCKET_BUFFER);    // 64KB发送缓冲区[1,2](@ref)
        
        // 2. 设置读取超时（防止无限阻塞）
        clientSocket.setSoTimeout(5000); // 5秒超时[1,6](@ref)

        // 3. 包装缓冲流并设置应用层缓冲区
        // 输出流：BufferedOutputStream + PrintWriter
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(
            clientSocket.getOutputStream(), SOCKET_BUFFER // 16KB应用层发送缓冲区
        );
        writer = new PrintWriter(bufferedOutput, true); // 自动刷新

        // 输入流：BufferedInputStream + BufferedReader
        BufferedInputStream bufferedInput = new BufferedInputStream(
            clientSocket.getInputStream(), SOCKET_BUFFER // 16KB应用层接收缓冲区
        );
        reader = new BufferedReader(new InputStreamReader(bufferedInput));

        System.out.println("Connected to Python server at " + pythonServerAddress + ":" + pythonServerPort);
    } catch (Exception e) {
        System.out.println("Error creating TCP client: " + e.getMessage());
        closeTCPConnection(); // 确保失败时关闭连接
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

    private String buildCmdString(String cmdType){
        String cmdStr = "CMDE";

        componentConfig.put("CmdType",cmdType);
        //componentConfig.put("ModuleName", componentName);
        componentConfig.put("input_channels_num", inputCount);
        componentConfig.put("output_channels_num", 1);
        componentConfig.put("current_input_channel_index", 0);
        componentConfig.put("current_output_channel_index", 0);
        componentConfig.put("block_size",blockLength);

        cmdStr = cmdStr + componentConfig.toString(2);

        if(cmdType != "load_data"){
            cmdStr = cmdStr + "\r\n\r\n" + "DATAPAYLOAD";
        }else{
            cmdStr = cmdStr + "\r\n\r\n";
        }
        return cmdStr;
    }

    private void sendStrToServer(String sendStr){
        try{
            if(checkTCPConnection()){
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
            System.out.println("Error sending command: " + e.getMessage());
        }
    }

    private boolean sendStrToServerWithReply(String sendStr, int timeoutMillis) {
        try {
            // 发送指令部分（复用原有逻辑）
            if (!checkTCPConnection()) {
                openTCPConnection();
            }
            writer.println(sendStr);
            writer.flush();
            System.out.println("Send: " + sendStr);
    
            // 设置固定读取超时
            Socket socket = this.clientSocket;  // 假设 socket 是类成员变量
            int originalTimeout = socket.getSoTimeout();
            socket.setSoTimeout(timeoutMillis); // 设置总超时时间
    
            try {
                // 单次读取等待（阻塞直到收到数据或超时）
                String response = reader.readLine();
                
                // 处理连接关闭
                if (response == null) {
                    System.out.println("Connection closed by server");
                    return false;
                }
                
                // 检查目标响应
                if ("OK".equals(response.trim())) {
                    System.out.println("Received OK from server");
                    return true;
                } else {
                    System.out.println("Unexpected response: " + response);
                    return false;
                }
            } finally {
                // 恢复原始超时设置
                socket.setSoTimeout(originalTimeout);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout waiting for reply after " + timeoutMillis + "ms");
            return false;
        } catch (IOException e) {
            System.out.println("Communication error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private String sendAndReceiveFromServer(String sendStr){
        try{
            sendStrToServer(sendStr);
            String receiveStr = reader.readLine();
            return receiveStr;
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private void loadConfig(){
        try{
            String cmdStr =  buildCmdString("load_config");
            sendStrToServerWithReply(cmdStr,WAIT_TIME);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void load_data(SDRData[] data,int index){
        try{
            String cmdStr = buildCmdString("load_data");

            //Build the Data Str
            String dataStr = "[";
            for(int i=0;i<blockLength-1;i++)
            {
                String tmp = String.format("%.4f", data[i]);
                dataStr = dataStr + tmp +',';
            }
            dataStr = dataStr + String.valueOf(data[blockLength-1]) + "]";

            cmdStr = cmdStr + dataStr;
            sendStrToServerWithReply(cmdStr,WAIT_TIME);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void setOperationParams(SDRData[] data,int index)
    {
        op_in[index] = data;
        load_data(data,index);
    }

    private void process(){
        try{
            String cmdStr = buildCmdString("process");
            sendStrToServerWithReply(cmdStr,WAIT_TIME);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Calculate(){
        process();
    }

    public double[] createTestData(){
        //Create a test data
        double[] testData = new double[blockLength];
        for(int i=0;i<blockLength;i++){
            testData[i] = Math.random();
        }

        //Set the test data
        setOperationParams(SDRDataUtils.fromDoubleArray(testData), 0);
        System.out.println("Test data created and set.");
        return testData;
    }

    private void result(){
        try{
            String sendStr = buildCmdString("result");
            String data = sendAndReceiveFromServer(sendStr);

            ans[0] = SDRDataUtils.fromDoubleArray(JSONToDouble.getDoubleFromJSONString(data));
            for(int i=0;i<blockLength;i++){
                System.out.println("Result: " + ans[i]);
            }
            System.out.println("Result received from Python server.");
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void setComponentConfig(JSONObject config){
        this.componentConfig = config;

        loadConfig();
    }


    public GeneralBridgeComponent(int blockLength, int inputCount, int outputCount,String ID) {
        super(blockLength, inputCount,outputCount, ID);
        this.pythonServerAddress = PYTHON_SERVER_ADDRESS;
        this.pythonServerPort = PYTHON_SERVER_PORT;
        this.componentName = ID;

        createTCPClient();
    }

    public GeneralBridgeComponent(int blockLength,int inputCount,String ID,JSONObject config){
        super(blockLength, inputCount,1, ID);
        this.pythonServerAddress = PYTHON_SERVER_ADDRESS;
        this.pythonServerPort = PYTHON_SERVER_PORT;
        this.componentName = ID;
        this.componentConfig = config;

        createTCPClient();
    }
    public static void main(String[] args) {
        GeneralBridgeComponent bridgeComponent = new GeneralBridgeComponent(100, 1, 1,"AGC");
        
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
            bridgeComponent.setOperationParams(SDRDataUtils.fromDoubleArray(data), 0);
            Thread.sleep(1000);
            bridgeComponent.Calculate();
            Thread.sleep(1000);
            System.out.println("Preparing for Ans...");
            bridgeComponent.result();
            
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        
    }   
}
