package src.main.java.com.zextras.httpserver.http.handler.connection.compression.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;
import src.main.java.com.zextras.httpserver.http.HeaderConstants;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.handler.connection.compression.HttpContentCompressor;

public class GzipHttpContentCompressor implements HttpContentCompressor {
  @Override
  public HttpResponse compressContent(HttpResponse response) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(outputStream)) {
      gos.write(response.getContent(), 0, response.getContent().length);
      gos.finish();
      gos.flush();
      outputStream.flush();
      response.setContent(outputStream.toByteArray());
      response.getHeaders().put(HeaderConstants.CONTENT_ENCODING_HEADER, "gzip");
      response
          .getHeaders()
          .put(HeaderConstants.CONTENT_LENGTH_HEADER, String.valueOf(response.getContent().length));
      return response;
    } catch (IOException e) {
      throw new InternalServerException(e);
    }
  }
}
