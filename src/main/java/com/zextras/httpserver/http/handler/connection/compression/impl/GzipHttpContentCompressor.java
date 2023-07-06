package src.main.java.com.zextras.httpserver.http.handler.connection.compression.impl;

import src.main.java.com.zextras.httpserver.exception.InternalServerException;
import src.main.java.com.zextras.httpserver.http.HeaderConstants;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.handler.connection.compression.HttpContentCompressor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GzipHttpContentCompressor implements HttpContentCompressor {
    @Override
    public HttpResponse compressContent(HttpResponse response) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gos = new GZIPOutputStream(outputStream)) {
            gos.write(response.getContent(), 0, response.getContent().length);
            gos.finish();
            response.setContent(outputStream.toByteArray());
            response.getHeaders().put(HeaderConstants.CONTENT_ENCODING_HEADER, "gzip");
            return response;
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
