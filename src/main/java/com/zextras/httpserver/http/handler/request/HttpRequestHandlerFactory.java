package src.main.java.com.zextras.httpserver.http.handler.request;

import java.util.Arrays;
import java.util.List;
import src.main.java.com.zextras.httpserver.exception.NotFoundException;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.handler.request.impl.file.FileRequestHandler;
import src.main.java.com.zextras.httpserver.http.handler.request.impl.index.IndexHttpRequestHandler;

public class HttpRequestHandlerFactory {
  private final List<HttpRequestHandler> handlersAvailable;

  public HttpRequestHandlerFactory() {
    this.handlersAvailable = Arrays.asList(new IndexHttpRequestHandler(), new FileRequestHandler());
  }

  public HttpRequestHandler getSuitableHandler(HttpRequest request) {
    return handlersAvailable.stream()
        .filter(handler -> handler.isRequestSupported(request))
        .findFirst()
        .orElseThrow(() -> new NotFoundException("Cannot process request: " + request.toString()));
  }
}
