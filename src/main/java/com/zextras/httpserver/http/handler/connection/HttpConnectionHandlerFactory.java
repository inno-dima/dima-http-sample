package src.main.java.com.zextras.httpserver.http.handler.connection;

import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandlerFactory;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandlerFactory;

public class HttpConnectionHandlerFactory {
    public static HttpConnectionHandler getHttpConnectionHandler() {
        return new HttpConnectionHandler(new HttpRequestHandlerFactory(), new HttpExceptionHandlerFactory());
    }
}
