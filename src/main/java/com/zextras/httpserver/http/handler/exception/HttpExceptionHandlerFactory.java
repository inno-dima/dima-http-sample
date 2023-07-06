package src.main.java.com.zextras.httpserver.http.handler.exception;

import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;
import src.main.java.com.zextras.httpserver.exception.NotFoundException;
import src.main.java.com.zextras.httpserver.http.handler.exception.impl.BadRequestExceptionHandler;
import src.main.java.com.zextras.httpserver.http.handler.exception.impl.InternalServerExceptionHandler;
import src.main.java.com.zextras.httpserver.http.handler.exception.impl.NotFoundExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class HttpExceptionHandlerFactory {
    private final Map<Class<?>, HttpExceptionHandler> availableHandlers;

    public HttpExceptionHandlerFactory() {
        this.availableHandlers = new HashMap<>();
        this.availableHandlers.put(BadRequestException.class, new BadRequestExceptionHandler());
        this.availableHandlers.put(NotFoundException.class, new NotFoundExceptionHandler());
        this.availableHandlers.put(InternalServerException.class, new InternalServerExceptionHandler());
    }

    public HttpExceptionHandler getSuitableHandler(Exception e) {
        if (availableHandlers.containsKey(e.getClass())) {
            return availableHandlers.get(e.getClass());
        }
        return new InternalServerExceptionHandler();
    }
}
