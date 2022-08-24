package com.webserver.http;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.controller.UserController;
import com.webserver.x1.ClientHandler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URISyntaxException;

public class DispatcherServlet {
    private static File dir;
    private static File staticDir;

    static {
        try {
            dir = new File(ClientHandler.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(dir, "static");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private static DispatcherServlet instance = new DispatcherServlet();


    private DispatcherServlet(){
    }
   public  void service(HttpServletResponse response,HttpServletRequest request){
        String path = request.getRequestURI();
       Method method = HandlerMapping.getMethod(path);
       if(method!=null){
           try {
               method.invoke(method.getDeclaringClass().newInstance(),
                       request,response);
           } catch (IllegalAccessException e) {
               throw new RuntimeException(e);
           } catch (InvocationTargetException e) {
               throw new RuntimeException(e);
           } catch (InstantiationException e) {
               throw new RuntimeException(e);
           }
           return;
       }
       File file = new File(staticDir, path);
           if (file.isFile()) {
               response.setContentFile(file);
           } else {
               response.setStatusCode(400);
               response.setStatusReason("NotFound");
               file = new File(staticDir, "root/404.html");
               response.setContentFile(file);
           }
       }
    public static DispatcherServlet getInstance(){
        return instance;
    }
}
