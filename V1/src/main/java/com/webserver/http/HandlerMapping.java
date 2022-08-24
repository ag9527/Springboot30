package com.webserver.http;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static Map<String, Method> mapping = new HashMap<>();
    static {
        initMapping();
    }
    private static void initMapping(){
        try {
            File dir = new File(HandlerMapping.class.getClassLoader().getResource(".").toURI());
            File controller = new File(dir,"/com/webserver/controller");
            File[] subs = controller.listFiles(f->f.getName().endsWith(".class"));
            for (File sub:subs){
                String classname = sub.getName().substring(0,sub.getName().indexOf("."));
                Class cls = Class.forName("com.webserver.controller."+classname);
                if(cls.isAnnotationPresent(Controller.class)){
                    Object obj = cls.newInstance();
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods){
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            mapping.put(rm.value(),method);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }
    public static Method getMethod(String path){
        return mapping.get(path);
    }
}
