package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
private static Map<String, Method> mapping = new HashMap<>();
static {
    initMapping();
}
private static void initMapping(){

    try {
        File dir = new File(HandlerMapping.class.getClassLoader().
                getResource(".").toURI());
        File controllerDir = new File(dir,"/com/webserver/controller");
        File[] subs = controllerDir.listFiles(f->f.getName().endsWith(".class"));
        System.out.println(Arrays.toString(subs));
        for (File sub :subs){
            String fileName = sub.getName();
            String className = fileName.substring(0, fileName.indexOf("."));
            System.out.println(className);
            Class cls = Class.forName("com.webserver.controller."+className);
            if (cls.isAnnotationPresent(Controller.class)){
                Method[] methods = cls.getDeclaredMethods();
                System.out.println(methods);
                for (Method method : methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){

                        RequestMapping rqm = method.getAnnotation(RequestMapping.class);
                        String value = rqm.value();
                           mapping.put(value,method);

                        }
                    }
                }
            }
    } catch (Exception e)

    {
        e.printStackTrace();

    }
}
public  static Method getMethod(String path){
    return mapping.get(path);
}
}
