package com.guo.webserver.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpServletResponse {
    private int statusCode = 200;
    private String statusReason = "OK";
    private File contentFile;
    private Socket socket;
    private ByteArrayOutputStream out;
    Map<String,String> headers = new HashMap<>();
    public HttpServletResponse(Socket socket){
 this.socket = socket;
    }
    public void response() throws IOException {
        sendBefore();
        sendStatusLine();
        sendHeaders();;
        sendContent();

    }
    private void sendBefore(){
        if(out != null){
            addHandlers("Content-Length",out.size()+"");
        }
    }
    //解析请求行
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }
    //响应消息头
    private void sendHeaders() throws IOException {
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for (Map.Entry<String,String> e : entrySet){
            String name = e.getKey();
            String value = e.getValue();
            println(name+": "+value);
        }
        println("");
    }
    //发送响应正文
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        if(this.out!=null){
            byte[] bytes = this.out.toByteArray();
            out.write(bytes);
        }
        else if (contentFile != null) {

            FileInputStream fis = new FileInputStream(contentFile);
            byte[] bytes = new byte[1024 * 10];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
        }
    }
    //方法读取行
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
        try {
            String contentType = Files.probeContentType(contentFile.toPath());//各种类型的文件
            if(contentType != null){
                addHandlers("ContentType",contentType);
            }
            addHandlers("Content-Length",contentFile.length()+"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   public void addHandlers(String name,String value){
        headers.put(name,value);
    }
    public  void  sendRedirect(String path){
       statusCode = 302;
       setStatusReason("MovedTemporarily");
       addHandlers("Location",path);
    }
    public OutputStream getOutputStream(){
        if(out == null){
            out = new ByteArrayOutputStream();
        }
        return out;
    }
    public PrintWriter getWriter(){
        return new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                getOutputStream(),StandardCharsets.UTF_8
                        )
                )
                ,true
        );
    }
    public void setContentType(String mime){
        addHandlers("Content-Type",mime);
    }
}
