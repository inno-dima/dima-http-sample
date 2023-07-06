package src.main.java.com.zextras.httpserver.http.handler.connection.compression;

import src.main.java.com.zextras.httpserver.http.HttpResponse;

public interface HttpContentCompressor {
    HttpResponse compressContent(HttpResponse response);


}
