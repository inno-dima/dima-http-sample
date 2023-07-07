package src.main.java.com.zextras.httpserver.http.handler.request;

import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;

public interface HttpRequestHandler {
  HttpResponse handleRequest(HttpRequest request);

  boolean isRequestSupported(HttpRequest request);
}
