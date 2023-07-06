package src.main.java.com.zextras.httpserver.http.handler.exception.impl;

import src.main.java.com.zextras.httpserver.exception.NotFoundException;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandler;

public class NotFoundExceptionHandler extends HttpExceptionHandler {
    public NotFoundExceptionHandler() {
        super(NotFoundException.class);
    }

    @Override
    public HttpResponse handleException(Exception e) {
        return new HttpResponse(HttpResponseStatus.NOT_FOUND);
    }
}
