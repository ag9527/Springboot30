package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.Socket;
import java.net.URISyntaxException;

public class DispatcherServlet {

    private Socket socket;
    private static File dir;
    private static File staticDir;
    static {
        try {
            dir = new File(DispatcherServlet.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(dir,"static");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private static DispatcherServlet instance = new DispatcherServlet();
    private DispatcherServlet(){

    }
    public  void  service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getUri();
        System.out.println(path);
        File file = new File(staticDir,path);
        if (file.isFile()){
            response.setContentFile(file);

        }
        else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "root/404.html");
            response.setContentFile(file);
        }

    }
    public static DispatcherServlet getInstance(){
        return instance;
    }
}
