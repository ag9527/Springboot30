package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServerApplication {
    private ServerSocket serverSocket;
    public WebServerApplication(){
        try {
            System.out.println("服务器正在启动");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务器已启动");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            while (true){
            System.out.println("等待客户连接");
            Socket socket = serverSocket.accept();
            System.out.println("客户已连接");
            ClientHandler handler = new ClientHandler(socket);
            Thread t = new Thread(handler);
            t.start();}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        WebServerApplication webServerApplication = new WebServerApplication();
        webServerApplication.start();
    }
}
