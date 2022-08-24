package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
   private String method;
    private String uri;
    private String protocol;
    private Socket socket;
private String requestUri;
private String queryString;
private Map<String,String> parameters  = new HashMap<>();
    Map<String,String> headers = new HashMap<>();
    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();

    }
    private void parseRequestLine() throws IOException, EmptyRequestException {//请求
        String line = readLine();
        if (line.isEmpty()){
            throw new EmptyRequestException("空的");
        }
        System.out.println(line);

        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];
        parseUri();
        System.out.println(method + "======" + uri + "++++++++++++" + protocol);
    }
    private void parseUri(){
           /*
            uri有两种情况:
            1:不含有参数的
              例如: /index.html
              直接将uri的值赋值给requestURI即可.

            2:含有参数的
              例如:/regUser?username=fancq&password=&nickname=chuanqi&age=22
              将uri中"?"左侧的请求部分赋值给requestURI
              将uri中"?"右侧的参数部分赋值给queryString
              将参数部分首先按照"&"拆分出每一组参数，再将每一组参数按照"="拆分为参数名与参数值
              并将参数名作为key，参数值作为value存入到parameters中。
         */
            String[] line = uri.split("\\?");
            requestUri = line[0];
            if(line.length>1){
            queryString = line[1];
            parseParameter(queryString);


            System.out.println("requestURI:"+requestUri);
            System.out.println("queryString:"+queryString);
            System.out.println("parameters:"+parameters);
        }

    }
    private void parseHeaders() throws IOException {
        //消息头

        while (true) {
            String line = readLine();
            if (line.isEmpty()) {
                break;
            }
            System.out.println(line);
            String[] s = line.split(":\\s");
            headers.put(s[0] , s[1]);
        }
        System.out.println(headers);
    }
    private void parseParameter(String line){
        //转换中文的value
        try {
            line = URLDecoder.decode(line,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String[] k = line.split("\\&");
        for(String v :k){
            String[] o = v.split("\\=",2);
            parameters.put(o[0],o[1]);
        }
    }
    private void parseContent() throws IOException {
        if ("post".equalsIgnoreCase(method)){
            if(headers.containsKey("Content-Length")){
                int contentLength = Integer.parseInt(
                        headers.get("Content-Length")
                );
                System.out.println(contentLength);
                InputStream in = socket.getInputStream();
                byte[] data =new  byte[contentLength];
                in.read(data);
                String contentType = headers.get("Content-Type");
                if("application/x-www-form-urlencoded".equals(contentType));
                String line = new String(data, StandardCharsets.ISO_8859_1);
                parseParameter(line);
            }
        }
    }

    public String readLine () throws IOException {
        InputStream in  = socket.getInputStream();
        StringBuilder builder = new StringBuilder();
        int d ;
        char pre = 'a' , cur = 'a';
        while ((d = in.read())!=-1){
            cur = (char) d;
            if(pre == 13 && cur == 10){
                break;
            }
            builder.append(cur);
            pre = cur;
        } return builder.toString().trim();
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

    public String getHeaders(String name) {
        return headers.get(name);
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }

}
