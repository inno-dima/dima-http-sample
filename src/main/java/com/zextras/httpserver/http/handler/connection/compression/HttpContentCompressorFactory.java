package src.main.java.com.zextras.httpserver.http.handler.connection.compression;

import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.http.handler.connection.compression.impl.GzipHttpContentCompressor;

public class HttpContentCompressorFactory {
  private static final String COMPRESS_GZIP_HEADER = "gzip";
  public HttpContentCompressor getCompressor(String header) {
    if (header.toLowerCase().contains(COMPRESS_GZIP_HEADER)) {
      return new GzipHttpContentCompressor();
    }
    throw new BadRequestException("Compression type is not supported: " + header);
  }
}
