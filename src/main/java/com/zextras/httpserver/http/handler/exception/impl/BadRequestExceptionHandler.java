package src.main.java.com.zextras.httpserver.http.handler.exception.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandler;

public class BadRequestExceptionHandler implements HttpExceptionHandler {
  private static final Logger log =
      Logger.getLogger("com.zextras.httpserver.http.handler.exception.BadRequestExceptionHandler");

  @Override
  public HttpResponse handleException(HttpRequest request, Exception e) {
    log.log(Level.WARNING, "BadRequestException occurred while processing request", e);
    return new HttpResponse(HttpResponseStatus.BAD_REQUEST);
  }
}
