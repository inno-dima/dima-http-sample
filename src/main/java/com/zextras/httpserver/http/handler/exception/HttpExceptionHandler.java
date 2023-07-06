package src.main.java.com.zextras.httpserver.http.handler.exception;

import src.main.java.com.zextras.httpserver.http.HttpResponse;

public interface HttpExceptionHandler { HttpResponse handleException(Exception e);
}
