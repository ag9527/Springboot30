package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpServletResponse {
    private int statusCode = 200;
    private String statusReason = "OK";
    private File contentFile;
    private Socket socket;
    Map<String,String> headers = new HashMap<>();
    public HttpServletResponse(Socket socket){
        this.socket = socket;

    }
    public void response() throws IOException {
        sendStatusLine();
        sendHeaders();
        sendContent();
    }
   public void sendStatusLine() throws IOException {
        line("HTTP/1.1"+statusCode+" "+statusReason+"");
    }
  public void sendHeaders() throws IOException {
       Set<Map.Entry<String,String>> entry = headers.entrySet();
       for (Map.Entry<String ,String> e:entry){
           String name = e.getKey();
           String value = e.getValue();
           line(name+": "+value);
       }

        line("");

    }

    public void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(contentFile);
        byte[] data = new byte[(int)contentFile.length()];
        int len;
        while ((len = fis.read(data))!=-1){
            out.write(data,0,len);
        }

    }
    private void line(String line ) throws IOException {
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
            String contentType = Files.probeContentType(contentFile.toPath());
            if (contentType!=null){
                addHandler("Content-Type",contentType);
            }
            addHandler("Content-Length",contentFile.length()+"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHeaders(String name) {
        return headers.get(name);
    }
   public void addHandler(String name,String value){
        headers.put(name,value);
   }

}
