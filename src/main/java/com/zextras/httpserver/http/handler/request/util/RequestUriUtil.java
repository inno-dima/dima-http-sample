package src.main.java.com.zextras.httpserver.http.handler.request.util;

import java.util.HashMap;
import java.util.Map;
import src.main.java.com.zextras.httpserver.exception.BadRequestException;

public class RequestUriUtil {
  private RequestUriUtil() {
    throw new UnsupportedOperationException();
  }

  public static Map<String, String> parseHttpParams(String uri) {
    try {
      return parseParams(uri);
    } catch (Exception e) {
      throw new BadRequestException();
    }
  }

  private static Map<String, String> parseParams(String uri) {
    Map<String, String> params = new HashMap<>();
    if (uri.lastIndexOf('?') < 0) {
      return params;
    }
    String paramsString = uri.split("\\?")[1];
    String[] parameters = paramsString.split("&");
    putParameters(params, parameters);
    return params;
  }

  private static void putParameters(Map<String, String> params, String[] parameters) {
    for (String param : parameters) {
      String[] paramKeyValue = param.split("=");
      params.put(paramKeyValue[0], paramKeyValue[1]);
    }
  }
}
