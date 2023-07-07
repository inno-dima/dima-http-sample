package src.main.java.com.zextras.httpserver.http.handler.request.impl.file;

public class FileHttpPageContent {
  public static final String TEMPLATE =
      "<!DOCTYPE html>\n"
          + "<html>\n"
          + "<head>\n"
          + "<meta charset=\"utf-8\"/>\n"
          + "</head>\n"
          + "<body>"
          + "<h1> Directory listing for "
          + "%s"
          + "</h1>\n"
          + "<ul>\n"
          + "%s"
          + "</ul>"
          + "</body></html>";
  public static final String HREF = "<li><a href=\"/files?path=%s\">%s</a></li>";

  private FileHttpPageContent() {
    throw new UnsupportedOperationException();
  }
}
