package src.main.java.com.zextras.httpserver.http;

public class HeaderConstants {
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String ACCEPT_ENCODING_HEADER = "accept-encoding";

    private HeaderConstants() {
        throw new UnsupportedOperationException();
    }
}
