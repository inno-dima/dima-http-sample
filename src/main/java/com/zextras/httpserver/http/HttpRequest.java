package src.main.java.com.zextras.httpserver.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import src.main.java.com.zextras.httpserver.config.ServerConfig;

public class HttpRequest {
  private static final Logger log =
      Logger.getLogger(".com.zextras.httpserver.SimpleHTTPServer");
  private String uri;

  public HttpRequest(String uri) {
    this.uri = uri;
  }

  public static HttpRequest parse(List<String> data) throws Exception {
    String uri = "";

    Pattern p = Pattern.compile("(\\A[A-Z]+) +(.+) +HTTP/1.\\d");
    Matcher m = p.matcher(data.remove(0));
    HttpRequest.parseHeaders(data);

    if (m.matches()) {
      uri = m.group(2);
    } else {
      throw new Exception("Malformed request.");
    }

    return new HttpRequest(uri);
  }

  private static HashMap<String, String> parseHeaders(List<String> headers) {
    HashMap<String, String> data = new HashMap<>();
    for (String h : headers) {
      String[] s = h.split(":", 2);
      data.put(s[0].trim(), s[1].trim());
    }
    return data;
  }

  public HttpResponse process() {
    HttpResponse res = new HttpResponse();
    Date now = new Date();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

    res.setHeader("Date", dateFormatter.format(now));
    res.setHeader("Server", ServerConfig.serverName);
    res.setHeader("Cache-Control", "no-cache");

    if (this.uri.equals("/")) {
      this.uri += "index.html";
    }

    String filePath = ServerConfig.sitePath + this.uri;

    byte[] buf = new byte[4096];
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    File file = new File(filePath);
    try (FileInputStream fin = new FileInputStream(file)) {
      while (fin.read(buf) != -1) {
        outputStream.write(buf);
      }
      log.info("Providing: " + file.getPath());

    } catch (IOException e) {
      e.printStackTrace();
    }

    res.setContent(outputStream.toByteArray());
    res.setHeader("Content-Length", outputStream.size());
    return res;
  }
}
