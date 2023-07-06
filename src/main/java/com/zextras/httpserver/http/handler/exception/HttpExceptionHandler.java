package src.main.java.com.zextras.httpserver.http.handler.exception;

import src.main.java.com.zextras.httpserver.http.HttpResponse;

public abstract class HttpExceptionHandler {
    private final Class<? extends Exception> exceptionClass;

    protected HttpExceptionHandler(Class<? extends Exception> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public boolean isHandlerSuitable(Class<? extends Exception> clazz) {
        return exceptionClass.isAssignableFrom(clazz);
    }

    public abstract HttpResponse handleException(Exception e);
}
