package com.guo.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpServletResponse {
    private Socket socket;
    private int statusCode = 200;
    private  String statusReason = "OK";
    private File contentFile;
    Map<String,String> headers = new HashMap<>();
    public HttpServletResponse(Socket socket){
        this.socket = socket;

    }
    public void response() throws IOException {
        sendStatusLine();
        sendHeaders();
        sendContent();
    }
    private void sendStatusLine() throws IOException {
     println("HTTP/1.1"+""+statusCode+""+statusReason);
    }
    private void sendHeaders() throws IOException {
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for (Map.Entry<String,String> entry :entrySet){
            String name = entry.getKey();
            String value = entry.getValue();
            println(name+": "+value);
        }   
        println("");


    }
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(contentFile);
        byte[] buf = new byte[1024 * 10];
        int len;
        while ((len = fis.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }
    private void println(String line) throws IOException {
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
    }
    public void addHandler(String name,String value){
        headers.put(name,value);
    }
}
