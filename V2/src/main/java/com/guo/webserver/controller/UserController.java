package com.guo.webserver.controller;

import com.guo.webserver.annotations.Controller;
import com.guo.webserver.annotations.RequestMapping;
import com.guo.webserver.entity.User;
import com.guo.webserver.http.HttpServletRequest;
import com.guo.webserver.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private static File userDir;
    static {
        userDir = new File("users");
        if (!userDir.exists()){
            userDir.mkdirs();
        }
    }
@RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response){
        String name  = request.getParameters("username");
        String password = request.getParameters("password");
        String nickname = request.getParameters("nickname");
        String ageStr = request.getParameters("age");
        if (name==null||name.isEmpty()||
        password==null||password.isEmpty()||
        nickname==null||nickname.isEmpty()||
        ageStr==null||ageStr.isEmpty()||!ageStr.matches("[0-9]+")){
            response.sendRedirect("reg_info_error.html");
            return;
        }
        int age = Integer.parseInt(ageStr);
        User user = new User(name,password,nickname,age);
        File file =  new File(userDir,name+".obj");
        if(file.exists()){
            response.sendRedirect("have_user.html");
            return;
        }

        try(   FileOutputStream fos = new FileOutputStream(file);
               ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(user);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect("reg_success.html");
    }
    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始登录");
      String username = request.getParameters("username");
      String password = request.getParameters("password");

      if(username==null||username.isEmpty()||password==null||password.isEmpty()){
          response.sendRedirect("login_fail.html");
          return;
      }
      File file = new File(userDir,username+".obj");
      if(!(file.exists())){
          response.sendRedirect("login_info_error.html");
          return;
      }
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);) {
            User user = (User)ois.readObject();

            if(user.getPassword().equals(password)){
                response.sendRedirect("login_success.html");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @RequestMapping("/userList")
    public void userList(HttpServletRequest request,HttpServletResponse response){
        List<User> userList = new ArrayList<>();
        System.out.println("阿萨德啊");
        File[] subs = userDir.listFiles(f->f.getName().endsWith(".obj"));
        for (File sub :subs){
            try ( FileInputStream fis = new FileInputStream(sub);
                  ObjectInputStream ois = new ObjectInputStream(fis)){
                User user = (User) ois.readObject();
                userList.add(user);

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
           response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<title>用户列表</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<h1>用户列表</h1>");
            pw.println("<table border=\"1\">");
            pw.println("<tr>");
            pw.println("<td>用户名</td>");
            pw.println("<td>密码</td>");
            pw.println("<td>昵称</td>");
            pw.println("<td>年龄</td>");
            pw.println("<td>操作</td>");
            pw.println("</tr>");

            for(User user : userList) {
                pw.println("<tr>");
                pw.println("<td>"+user.getName()+"</td>");
                pw.println("<td>"+user.getPassword()+"</td>");
                pw.println("<td>"+user.getNickname()+"</td>");
                pw.println("<td>"+user.getAge()+"</td>");
                pw.println("<td><a href='/deleteUser?username="+ user.getName() +"'>删除</a></td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");


    }
}
