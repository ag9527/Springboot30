package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpServletResponse {
    private ByteArrayOutputStream out;
    private Socket socket;
    private int statusCode = 200;
    private String statusReason = "OK";
    private File contentFile;
    private Map<String,String> headers = new HashMap<>();
    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }
    public void response() throws IOException {
        sendBefore();
       sendStatusLine();
       sendHeaders();
       sentContent();
    }
    private void  sendBefore(){
        if(out!= null){
           addHeader("Content-Length",out.size()+"");
        }
    }
    private void sendStatusLine() throws IOException {
         line("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }
    private void sendHeaders() throws IOException {
        Set<Map.Entry<String,String>> entry = headers.entrySet();
        for(Map.Entry<String,String> entry1 : entry){
            String name = entry1.getKey();
            String value = entry1.getValue();
            line(name+": "+value);
        }
        line("");

    }
    private void sentContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        if(this.out!=null){
            byte[] data = this.out.toByteArray();
            out.write(data);
        }
        else if(contentFile!=null) {
             FileInputStream fis = new FileInputStream(contentFile);
             byte[] data = new byte[(int) contentFile.length()];
             int len;
             while ((len = fis.read(data)) != -1) {
                 out.write(data, 0, len);
             }
         }

    }

    private void line(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] bytes = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(bytes);
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
            String contentType = Files.probeContentType(contentFile.toPath());
            if(contentType != null){
                addHeader("Content-Type",contentType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addHeader("Content-Length",contentFile.length()+"");
    }
    public void addHeader(String name,String value){
        headers.put(name,value);
    }
    public void sendRedirect(String path){
        statusCode = 302;
        statusReason = "MovedTemporarily";
        addHeader("Location",path);
    }
    public OutputStream getOutStream(){
        if(out == null){
           out = new ByteArrayOutputStream();
        }
        return out;
    }
    public PrintWriter getWriter(){
      return new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                               getOutStream(),StandardCharsets.UTF_8
                        )
                ),true
        );

    }
    public void setContentType(String mime){
        addHeader("contentType",mime);
    }
}
