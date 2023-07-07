package src.main.java.com.zextras.httpserver.http.handler.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;
import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;

public class HttpHandlerUtils {

  private static final String HTTP_REGEX = "(\\A[A-Z]+) +(.+) +HTTP/1.\\d";
  private static final String COLON = ":";

  private HttpHandlerUtils() {
    throw new UnsupportedOperationException();
  }

  public static HttpRequest parseRequest(InputStream inputStream) throws IOException {
    List<String> data = readInputStream(inputStream);
    Pattern pattern = Pattern.compile(HTTP_REGEX);
    Matcher matcher = pattern.matcher(data.remove(0));
    Map<String, String> headers = parseHeaders(data);
    if (matcher.matches()) {
      return new HttpRequest(matcher.group(1), matcher.group(2), headers);
    } else {
      throw new BadRequestException();
    }
  }

  public static void write(HttpResponse response, OutputStream outputStream) {
    StringBuilder httpHeaders = new StringBuilder(response.getStatus().toString());
    writeHeaders(response, httpHeaders);
    httpHeaders.append("\r\n");
    writeHeadersAndContentToStream(response, outputStream, httpHeaders);
  }

  private static List<String> readInputStream(InputStream inputStream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    List<String> data = new ArrayList<>();
    for (String str = reader.readLine(); !str.equals(""); str = reader.readLine()) {
      data.add(str);
    }
    return data;
  }

  private static Map<String, String> parseHeaders(List<String> headers) {
    Map<String, String> data = new HashMap<>();
    for (String h : headers) {
      String[] s = h.split(COLON, 2);
      data.put(s[0].trim().toLowerCase(), s[1].trim().toLowerCase());
    }
    return data;
  }

  private static void writeHeaders(HttpResponse response, StringBuilder httpHeaders) {
    for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
      String h = header.getKey() + COLON + header.getValue();
      httpHeaders.append(h).append("\r\n");
    }
  }

  private static void writeHeadersAndContentToStream(
      HttpResponse response, OutputStream outputStream, StringBuilder httpHeaders) {
    try {
      outputStream.write(httpHeaders.toString().getBytes(StandardCharsets.UTF_8));
      outputStream.write(response.getContent());
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      throw new InternalServerException(e);
    }
  }
}
