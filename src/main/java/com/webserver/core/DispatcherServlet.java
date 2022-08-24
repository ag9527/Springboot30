package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;

public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();

    private DispatcherServlet() {
    }

    private static File dir;
    private static File staticDir;

    static {
        //定位环境变量ClassPath(类加载路径)中"."的位置
        //在IDEA中执行项目时,类加载路径是从target/classes开始的
        try {
            dir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位target/classes/static目录
            staticDir = new File(dir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static DispatcherServlet getInstance() {
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestUri();
        System.out.println("抽象路径" + path);


         Method method =  HandlerMapping.getMethod(path);
         if (method != null){
             try {
                 method.invoke(method.getDeclaringClass().newInstance(),request,response);
                      return;
             } catch (Exception e) {
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
