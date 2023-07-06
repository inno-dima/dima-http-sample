package src.main.java.com.zextras.httpserver.http.handler.exception.impl;

import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandler;

public class BadRequestExceptionHandler implements HttpExceptionHandler {
    @Override
    public HttpResponse handleException(Exception e) {
        return new HttpResponse(HttpResponseStatus.NOT_FOUND);
    }
}
