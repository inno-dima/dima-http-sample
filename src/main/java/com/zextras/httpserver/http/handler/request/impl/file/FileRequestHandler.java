package src.main.java.com.zextras.httpserver.http.handler.request.impl.file;

import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandler;

public class FileRequestHandler implements HttpRequestHandler {

  private final FileHandlingService fileHandlingService;

  public FileRequestHandler() {
    this.fileHandlingService = new FileHandlingService();
  }

  @Override
  public HttpResponse handleRequest(HttpRequest request) {
    return fileHandlingService.handleRequest(request.getUri());
  }

  @Override
  public boolean isRequestSupported(HttpRequest request) {
    return request.getUri().startsWith("/file");
  }
}
