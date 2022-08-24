package com.guo.webserver.core;

import com.guo.webserver.annotations.Controller;
import com.guo.webserver.annotations.RequestMapping;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
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
            File dir = new File(HandlerMapping.class.getClassLoader().getResource(".").toURI());
            File controller = new File(dir,"/com/guo/webserver/controller");
            File[] subs = controller.listFiles(f->f.getName().endsWith(".class"));
            System.out.println(Arrays.toString(subs));
            for (File sub : subs){
                String className = sub.getName().substring(0,sub.getName().indexOf("."));
                System.out.println(className);
                Class cls = Class.forName("com.guo.webserver.controller."+className);
                if(cls.isAnnotationPresent(Controller.class)){
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method :methods){
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String value = rm.value();
                            mapping.put(value,method);

                            }
                        }

                    }
                }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Method getMethod(String path){
        return mapping.get(path);
    }

    public static void main(String[] args) {
        Method method = mapping.get("/regUser");
        //通过方法对象可以获取其所属的类的类对象
        Class cls = method.getDeclaringClass();
        System.out.println(cls);
        System.out.println(method);
    }
}
