package src.main.java.com.zextras.httpserver.http.handler.connection.compression;

import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.http.handler.connection.compression.impl.GzipHttpContentCompressor;

public class HttpContentCompressorFactory {
  public HttpContentCompressor getCompressor(String header) {
    if (header.toLowerCase().contains("gzip")) {
      return new GzipHttpContentCompressor();
    }
    throw new BadRequestException("Compression type is not supported");
  }
}
