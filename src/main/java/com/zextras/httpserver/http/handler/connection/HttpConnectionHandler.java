package src.main.java.com.zextras.httpserver.http.handler.connection;

import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandlerFactory;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandler;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandlerFactory;
import src.main.java.com.zextras.httpserver.socket.Connect;

import java.io.IOException;

public class HttpConnectionHandler {

    private final HttpRequestHandlerFactory requestHandlerFactory;
    private final HttpExceptionHandlerFactory requestExceptionHandlerFactory;

    public HttpConnectionHandler(HttpRequestHandlerFactory requestHandlerFactory, HttpExceptionHandlerFactory requestExceptionHandlerFactory) {
        this.requestHandlerFactory = requestHandlerFactory;
        this.requestExceptionHandlerFactory = requestExceptionHandlerFactory;
    }


    public void handleConnection(Connect connect) {
        try  {
            HttpRequest request = parseRequest(connect);
            HttpRequestHandler handler = requestHandlerFactory.getSuitableHandler(request);
            HttpHandlerUtils.write(handler.handleRequest(request), connect.getOut());
        } catch (Exception e) {
            HttpHandlerUtils.write(requestExceptionHandlerFactory.getSuitableHandler(e).handleException(e), connect.getOut());
        }
    }

    private HttpRequest parseRequest(Connect connect) {
        try {
            return HttpHandlerUtils.parseRequest(connect.getIn());
        } catch (IOException e) {
            throw new BadRequestException(e);
        }
    }
}
