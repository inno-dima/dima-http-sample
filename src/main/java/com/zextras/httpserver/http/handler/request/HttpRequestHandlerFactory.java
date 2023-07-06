package src.main.java.com.zextras.httpserver.http.handler.request;

import src.main.java.com.zextras.httpserver.exception.NotFoundException;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.handler.request.impl.index.IndexHttpRequestHandler;

import java.util.Collections;
import java.util.List;

public class HttpRequestHandlerFactory {
    private final List<HttpRequestHandler> handlersAvailable;

    public HttpRequestHandlerFactory() {
        this.handlersAvailable = Collections.singletonList(new IndexHttpRequestHandler());
    }

    public HttpRequestHandler getSuitableHandler(HttpRequest request) {
        for (HttpRequestHandler handler : handlersAvailable) {
            if (handler.isRequestSupported(request)) {
                return handler;
            }
        }
        throw new NotFoundException();
    }
}
