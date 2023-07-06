package src.main.java.com.zextras.httpserver.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private byte[] content;
    private HttpResponseStatus status;
    private Map<String, String> headers;

    public HttpResponse() {
        this.status = HttpResponseStatus.OK;
        this.headers = new LinkedHashMap<>();
        this.content = new byte[0];
    }

    public HttpResponse(HttpResponseStatus status) {
        this.status = status;
        this.headers = new LinkedHashMap<>();
        this.content = new byte[0];
    }

    public HttpResponse(HttpResponseStatus status, Map<String, String> headers, byte[] content) {
        this.status = status;
        this.headers = headers;
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus s) {
        this.status = s;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
