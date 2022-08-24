package com.guo.webserver.core;

import com.guo.webserver.http.HttpServletRequest;
import com.guo.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{

    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;

    }
    @Override
    public void run() {
        try {
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);

            DispatcherServlet.getInstance().service(request,response);
            response.response();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
