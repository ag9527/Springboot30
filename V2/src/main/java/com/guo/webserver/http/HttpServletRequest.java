package com.guo.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    private Socket socket;
    private String method;
    private String uri;
    private String protocol;
    private String requestURI;
    private String queryString;
    Map<String,String> parameters = new HashMap<>();
   private Map<String ,String> headers= new HashMap<>();
    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }
    private void  parseRequestLine() throws IOException, EmptyRequestException {
        String line = redLine();
        if(line.isEmpty()){
            throw new EmptyRequestException("空的");
        }
        System.out.println(line);
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];
        System.out.println(method+"<"+uri+"<"+protocol);
        parseURI();

    }
    private void parseURI(){
        String[] data = uri.split("\\?");
        requestURI = data[0];
        if(data.length>1){
            queryString = data[1];
            String[] l = queryString.split("&");
            for (String arr : l){
                String[]  a = arr.split("=",2);
                parameters.put(a[0],a[1]);
            }
        }

    }
    private void parseHeaders() throws IOException {
        String line;
        while (!(line = redLine()).isEmpty()){
            String[] header = line.split(":\\s");
            headers.put(header[0],header[1]);
            System.out.println(line);
        }
        System.out.println(headers);


    }
    private void parseContent(){
    }
    private String redLine() throws IOException {
        InputStream in = socket.getInputStream();
        int d;//行
        char pre = 'a',cur = 'a';
        StringBuilder builder = new StringBuilder();
        while ((d=in.read())!=-1){
            cur = (char)d;
            if(pre==13&&cur==10){
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
