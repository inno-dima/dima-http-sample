package src.main.java.com.zextras.httpserver.http;

import src.main.java.com.zextras.httpserver.exception.NotFoundException;

import java.util.Arrays;

public enum HttpMethod {
    GET, PUT, PATCH, POST;

    public static HttpMethod fromString(String s) {
        return Arrays.
                stream(HttpMethod.values())
                .filter(method -> method.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
