package com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Server;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.RPCManager;
import com.example.sdr.Core.ProjectManager.ControlBridge.RPC.Processer.RPCProcesser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class RPCWebSocketServer {

    private int port = 9002;
    private RPCManager manager;
    private RPCProcesser processer;

    private InnerWebSocketServer server;

    // 当前唯一前端连接（AtomicReference 确保线程安全）
    private AtomicReference<WebSocket> currentConnection = new AtomicReference<>(null);

    // 内部 WebSocket Server
    private class InnerWebSocketServer extends WebSocketServer {

        public InnerWebSocketServer(int port) {
            super(new InetSocketAddress(port));
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            System.out.println("WebSocket connected: " + conn.getRemoteSocketAddress());

            // 保存当前连接
            currentConnection.set(conn);

            // 发送欢迎消息
            conn.send("Welcome to WebSocket RPC Server!");
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("WebSocket disconnected: " + reason);

            // 清空当前连接
            currentConnection.compareAndSet(conn, null);
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // 忽略 String 消息，或者打印警告
            System.out.println("Warning: Received unexpected TEXT message, ignoring.");
        }

        @Override
        public void onMessage(WebSocket conn, ByteBuffer message) {
            System.out.println("Received WebSocket message: " + message);

            // 调用 processer 处理 RPC 帧
            if (processer != null) {
                processer.AddRPCFrameToQueue(message);
            }

            // 回 ACK
            //conn.send("ACK: " + message);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            System.err.println("WebSocket error: " + ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("WebSocket RPC Server started at ws://localhost:" + port + "/");
            setConnectionLostTimeout(0);
            setConnectionLostTimeout(100);
        }
    }

    // 构造函数
    public RPCWebSocketServer() {
        this.port = 9002;
    }

    public RPCWebSocketServer(int port, RPCManager manager) {
        this.port = port;
        this.manager = manager;
        this.processer = manager.getProcesser();
    }

    // Start 方法
    public void Start() {
        server = new InnerWebSocketServer(port);
        server.setReuseAddr(true);
        server.start();
    }

    // 主动发送消息给前端
    public void sendToFrontend(String message) {
        WebSocket conn = currentConnection.get();
        if (conn != null && conn.isOpen()) {
            conn.send(message);
            System.out.println("Sent to frontend: " + message);
        } else {
            System.out.println("No active WebSocket connection to frontend.");
        }
    }

    // 测试 main
    public static void main(String[] args) {
        RPCManager manager = new RPCManager(new ProjectManager());
        RPCWebSocketServer server = new RPCWebSocketServer(9100, manager);
        server.Start();
    }
}
