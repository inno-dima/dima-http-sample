package src.main.java.com.zextras.httpserver.http.handler.exception.impl;

import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalServerExceptionHandler implements HttpExceptionHandler {
    private static final Logger log = Logger.getLogger("com.zextras.httpserver.http.handler.exception.InternalServerExceptionHandler");
    @Override
    public HttpResponse handleException(HttpRequest request, Exception e) {
        log.log(Level.WARNING, "InternalServerException occurred while processing request", e);
        return new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }
}
