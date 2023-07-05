package src.main.java.com.zextras.httpserver.http;

import java.util.ArrayList;
import java.util.List;

public class HttpResponse {
    private HttpResponseStatus httpResponseStatus;
    private final List<String> headers;
    private byte[] content;

    public HttpResponse() {
        this.httpResponseStatus = HttpResponseStatus.OK;
        this.headers = new ArrayList<>();
        this.content = new byte[0];
    }

    public void setStatus(HttpResponseStatus s) {
        this.httpResponseStatus = s;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContent() {
        String data = "";

        data += httpResponseStatus.toString();

        for (String h : this.headers) {
            data += h + "\r\n";
        }
        data += "\r\n" + new String(this.content);

        return data;
    }

    public void setHeader(String hName, String hValue) {
        this.headers.add(hName + ":" + hValue);
    }

    public void setHeader(String hName, int hValue) {
        this.headers.add(hName + ":" + hValue);
    }

}
