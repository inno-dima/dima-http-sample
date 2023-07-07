package src.main.java.com.zextras.httpserver.http.handler.exception.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandler;

public class NotFoundExceptionHandler implements HttpExceptionHandler {
  private static final Logger log =
      Logger.getLogger("com.zextras.httpserver.http.handler.exception.NotFoundExceptionHandler");

  @Override
  public HttpResponse handleException(HttpRequest request, Exception e) {
    log.log(Level.WARNING, "NotFoundException occurred while processing request", e);
    return new HttpResponse(HttpResponseStatus.NOT_FOUND);
  }
}
