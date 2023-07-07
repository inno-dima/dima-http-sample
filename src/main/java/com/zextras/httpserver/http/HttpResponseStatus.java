package src.main.java.com.zextras.httpserver.http;

public enum HttpResponseStatus {
  OK(200, "1.1", "OK"),
  NOT_FOUND(404, "1.1", "Not Found"),
  BAD_REQUEST(400, "1.1", "Bad Request"),
  INTERNAL_SERVER_ERROR(500, "1.1", "Internal Server Error"),
  MOVED_PERMANENTLY(301, "1.1", "Moved permanently");
  private final int code;
  private final String httpVersion;
  private final String msg;

  HttpResponseStatus(int code, String version, String msg) {
    this.code = code;
    this.httpVersion = version;
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "HTTP/" + this.httpVersion + " " + this.code + " " + this.msg + "\r\n";
  }
}
