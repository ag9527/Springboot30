package com.guo.webserver.core;

import com.guo.webserver.annotations.Controller;
import com.guo.webserver.annotations.RequestMapping;
import com.guo.webserver.controller.UserController;
import com.guo.webserver.http.HttpServletRequest;
import com.guo.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;

public class DispatcherServlet {
    private static DispatcherServlet instance =new DispatcherServlet();
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
    private DispatcherServlet(){

    }
    public static DispatcherServlet getInstance(){
        return instance;
    }
    public void service(HttpServletRequest request, HttpServletResponse response)  {
        String path = request.getRequestURI();
        System.out.println(path);
        Method method = HandlerMapping.getMethod(path);
        if(method!=null){
            try {
                method.invoke(method.getDeclaringClass().newInstance(),request,response);
                return;
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        File file = new File(staticDir, path);

        if (file.isFile()) {
            response.setContentFile(file);
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "root/404.html");
            response.setContentFile(file);

    }
    }

}
