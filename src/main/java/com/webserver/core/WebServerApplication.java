package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServerApplication {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    public WebServerApplication(){
        try {
            System.out.println("正在启动服务器");
            serverSocket = new ServerSocket(8089);
            threadPool = Executors.newFixedThreadPool(50);
            System.out.println("服务器已启动");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void start() {
        while (true) {
            try {
                System.out.println("等待客户连接");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户已连接");
                ClientHandler c = new ClientHandler(socket);
                threadPool.execute(c);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
         WebServerApplication w = new WebServerApplication();
         w.start();
    }

}
