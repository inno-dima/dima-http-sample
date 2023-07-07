package src.main.java.com.zextras.httpserver.http.handler.connection;

import java.io.IOException;
import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.http.HeaderConstants;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.handler.connection.compression.HttpContentCompressorFactory;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandlerFactory;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandler;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandlerFactory;
import src.main.java.com.zextras.httpserver.socket.Connect;

public class HttpConnectionHandler {

  private final HttpRequestHandlerFactory requestHandlerFactory;
  private final HttpExceptionHandlerFactory requestExceptionHandlerFactory;
  private final HttpContentCompressorFactory contentCompressorFactory;

  public HttpConnectionHandler(
      HttpRequestHandlerFactory requestHandlerFactory,
      HttpExceptionHandlerFactory requestExceptionHandlerFactory,
      HttpContentCompressorFactory contentCompressorFactory) {
    this.requestHandlerFactory = requestHandlerFactory;
    this.requestExceptionHandlerFactory = requestExceptionHandlerFactory;
    this.contentCompressorFactory = contentCompressorFactory;
  }

  public void handleConnection(Connect connect) {
    HttpRequest request = parseRequest(connect);
    try {
      HttpRequestHandler handler = requestHandlerFactory.getSuitableHandler(request);
      HttpResponse response = compressResponse(handler.handleRequest(request), request);
      HttpHandlerUtils.write(response, connect.getOut());
    } catch (Exception e) {
      HttpHandlerUtils.write(
          requestExceptionHandlerFactory.getSuitableHandler(e).handleException(request, e),
          connect.getOut());
    }
  }

  private HttpResponse compressResponse(HttpResponse response, HttpRequest request) {
    if (request.getHeaders().containsKey(HeaderConstants.ACCEPT_ENCODING_HEADER)) {
      return contentCompressorFactory
          .getCompressor(request.getHeaders().get(HeaderConstants.ACCEPT_ENCODING_HEADER))
          .compressContent(response);
    }
    return response;
  }

  private HttpRequest parseRequest(Connect connect) {
    try {
      return HttpHandlerUtils.parseRequest(connect.getIn());
    } catch (IOException e) {
      throw new BadRequestException(e);
    }
  }
}
