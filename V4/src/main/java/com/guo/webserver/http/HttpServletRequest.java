package com.guo.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    private String method ;
    private String uri;
    private String protocol;
    private Socket socket;
    private Map<String ,String> headers = new HashMap<>();
    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;
        parseStatusLine();
        parseHeaders();
      parseContent();


    }
    private void parseStatusLine() throws IOException {
        String line =readLine();
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];
        System.out.println(method+","+uri+","+protocol);


    }
    private void parseHeaders() throws IOException {
        String line;
        while (!(line = readLine()).isEmpty()){
            System.out.println(line);
            String[] datas = line.split(":");
            headers.put(datas[0],datas[1]);
        }
        System.out.println(headers);

    }
    private void parseContent(){

    }


    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        int d;
        char pre = 'a',cur = 'a';
        StringBuilder builder = new StringBuilder();
        while ((d = in.read())!=-1){
            cur = (char) d;
            if(pre==13&&cur==10){
                break;
            }
            pre = cur;
            builder.append(cur);
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

    public String getHeaders(String name) {
        return headers.get(name);
    }
}
