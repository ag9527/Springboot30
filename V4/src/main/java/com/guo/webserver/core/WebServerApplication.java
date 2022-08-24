package com.guo.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServerApplication {
    private ServerSocket serverSocket;
    public WebServerApplication(){
        try {
            System.out.println("正在启动服务器");
            serverSocket = new ServerSocket(8090);
            System.out.println("服务已启动");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void start(){

        try {
            while (true) {
                System.out.println("等待连接、、、、、、、、、、、");
                Socket socket = serverSocket.accept();
                System.out.println("已连接");
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
     public static void main(String[] args) {
    WebServerApplication w = new WebServerApplication();
    w.start();
    }
}
