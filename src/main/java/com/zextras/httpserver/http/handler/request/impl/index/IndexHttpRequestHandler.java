package src.main.java.com.zextras.httpserver.http.handler.request.impl.index;

import java.util.HashMap;
import java.util.Map;
import src.main.java.com.zextras.httpserver.http.HeaderConstants;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandler;

public class IndexHttpRequestHandler implements HttpRequestHandler {
  private final IndexHandlerService indexHandlerService;

  public IndexHttpRequestHandler() {
    this.indexHandlerService = new IndexHandlerService();
  }

  @Override
  public HttpResponse handleRequest(HttpRequest request) {
    byte[] content = indexHandlerService.readFileContent();
    Map<String, String> headers = new HashMap<>();
    headers.put(HeaderConstants.CONTENT_LENGTH_HEADER, String.valueOf(content.length));
    return new HttpResponse(HttpResponseStatus.OK, headers, content);
  }

  @Override
  public boolean isRequestSupported(HttpRequest request) {
    String uri = request.getUri();
    return uri.equalsIgnoreCase("/") || uri.equalsIgnoreCase("/index.html");
  }
}
