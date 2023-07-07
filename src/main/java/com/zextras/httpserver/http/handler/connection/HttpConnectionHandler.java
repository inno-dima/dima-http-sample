package src.main.java.com.zextras.httpserver.http.handler.connection;

import java.io.IOException;
import java.util.Map;
import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.http.HeaderConstants;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.handler.connection.compression.HttpContentCompressorFactory;
import src.main.java.com.zextras.httpserver.http.handler.exception.HttpExceptionHandlerFactory;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandler;
import src.main.java.com.zextras.httpserver.http.handler.request.HttpRequestHandlerFactory;
import src.main.java.com.zextras.httpserver.socket.ConnectionRequest;

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

  public void processConnection(ConnectionRequest connectionRequest) {
    HttpRequest request = parseRequest(connectionRequest);
    try {
      HttpRequestHandler handler = requestHandlerFactory.getSuitableHandler(request);
      HttpResponse response = handler.handleRequest(request);
      HttpResponse compressedResponse = compressResponse(response, request.getHeaders());
      HttpHandlerUtils.write(compressedResponse, connectionRequest.getOut());
    } catch (Exception e) {
      HttpHandlerUtils.write(
          requestExceptionHandlerFactory.getSuitableHandler(e).handleException(request, e),
          connectionRequest.getOut());
    }
  }

  private HttpResponse compressResponse(HttpResponse response, Map<String, String> headers) {
    if (headers.containsKey(HeaderConstants.ACCEPT_ENCODING_HEADER)) {
      return contentCompressorFactory
          .getCompressor(headers.get(HeaderConstants.ACCEPT_ENCODING_HEADER))
          .compressContent(response);
    }
    return response;
  }

  private HttpRequest parseRequest(ConnectionRequest connectionRequest) {
    try {
      return HttpHandlerUtils.parseRequest(connectionRequest.getIn());
    } catch (IOException e) {
      throw new BadRequestException(e);
    }
  }
}
