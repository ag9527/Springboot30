package com.webserver.http;

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
    private Map<String,String> headers = new HashMap<>();
    private String requestURI;
    private String queryString;
  private   Map<String,String> parameters = new HashMap<>();

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();
        if(line.isEmpty()){
            throw  new EmptyRequestException("是空的");
        }
        String[] data = line.split("\\s");

        method = data[0];
        uri = data[1];
        protocol = data[2];
        System.out.println(method+uri+protocol);
    parseUri();
    }
    public void parseUri(){
        String[] data = uri.split("\\?");
        requestURI = data[0];
        if(data.length>1){
            queryString = data[1];
            String[] ara = queryString.split("&");
            for (String s : ara){
                String[] v = s.split("=",2);
                parameters.put(v[0],v[1]);
            }
            System.out.println(parameters);
        }
    }
        private void parseHeaders() throws IOException {
           String line;
            while (!(line = readLine()).isEmpty()) {

                System.out.println(line);
                String[] x = line.split(":\\s");
                headers.put(x[0], x[1]);
            }
            System.out.println(headers);
            //拆分消息头

        }
        private void parseContent(){

        }
    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        StringBuilder builder = new StringBuilder();
        int d;
        char pre ='a',cur='a';
        while ((d=in.read())!=-1){
            cur = (char) d;
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

    public Map<String, String> getHeaders() {
        return headers;
    }
}
