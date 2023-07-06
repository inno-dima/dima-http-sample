package src.main.java.com.zextras.httpserver.http.handler.exception;

import src.main.java.com.zextras.httpserver.http.handler.exception.impl.BadRequestExceptionHandler;
import src.main.java.com.zextras.httpserver.http.handler.exception.impl.InternalServerExceptionHandler;
import src.main.java.com.zextras.httpserver.http.handler.exception.impl.NotFoundExceptionHandler;

import java.util.Arrays;
import java.util.List;

public class HttpExceptionHandlerFactory {
    private final List<HttpExceptionHandler> availableHandlers;

    public HttpExceptionHandlerFactory() {
        this.availableHandlers = Arrays.asList(new BadRequestExceptionHandler(), new InternalServerExceptionHandler(), new NotFoundExceptionHandler());
    }

    public HttpExceptionHandler getSuitableHandler(Exception e) {
        for (HttpExceptionHandler handler : availableHandlers) {
            if (handler.isHandlerSuitable(e.getClass())) {
                return handler;
            }
        }
        return new InternalServerExceptionHandler();
    }
}
