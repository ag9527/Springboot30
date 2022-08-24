package com.guo.webserver.core;

import com.guo.webserver.http.HttpServletRequest;
import com.guo.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class DispatcherServlet {
    private static File dir;
    private static File staticDir;
    static {
        try {
            dir = new File(ClientHandler.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(dir,"static");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public  static DispatcherServlet instance = new DispatcherServlet();
    private DispatcherServlet(){}
    public static DispatcherServlet  getInstance(){
        return instance;
    }
    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getUri();
        System.out.println(path);
        File file = new File(staticDir,path);
        if (file.isFile()) {
            response.setContentFile(file);
            response.addHandler("Content-Type"," text/html");
            response.addHandler("Content-Length",file.length()+" ");

        }else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir,"root/404.html");
            response.setContentFile(file);
            response.addHandler("Content-Type"," text/html");
            response.addHandler("Content-Length",file.length()+" ");
        }
    }
}
