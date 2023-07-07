package src.main.java.com.zextras.httpserver.http;

import java.util.Map;

public class HttpRequest {
  private final String uri;
  private final HttpMethod method;
  private final Map<String, String> headers;

  public HttpRequest(String method, String uri, Map<String, String> headers) {
    this.method = HttpMethod.fromString(method);
    this.uri = uri;
    this.headers = headers;
  }

  public String getUri() {
    return uri;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public HttpMethod getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return "HttpRequest{"
        + "uri='"
        + uri
        + '\''
        + ", method="
        + method
        + ", headers="
        + headers
        + '}';
  }
}
