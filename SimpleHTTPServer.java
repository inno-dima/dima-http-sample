
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.util.logging.*;
import java.text.SimpleDateFormat;

class ServerConfig {
  public static String sitePath = "html";
  public static String serverName = "SimpleHTTPServer 0.0.1";
  public static int PORT = 3000;
}

public class SimpleHTTPServer extends Thread {
  private ServerSocket Server;
  private static Logger log = Logger.getLogger("SimpleHTTPServer");

  public static void main(String argv[]) throws Exception {
    log.info("**** Simple HTTP Server starts... ****");
    new SimpleHTTPServer();
  }

  public SimpleHTTPServer() throws Exception {
    Server = new ServerSocket(ServerConfig.PORT);
    log.info("Server listening on port: " + ServerConfig.PORT);
    this.start();
  }

  public void run() {
    log.info("Waiting for connections.");
    while(true) {
      try {
        Socket client = Server.accept();
        log.info("Connection from " + client.getInetAddress());
        new Connect(client);
      } catch(Exception e) {}
    }
  }
}

class ResponseStatus {
  private int code;
  private String HTTPversion;
  private String msg;

  public ResponseStatus(int code, String version, String msg) {
    this.code = code;
    this.HTTPversion = version;
    this.msg = msg;
  }

  public static ResponseStatus HTTPOk() {
    return new ResponseStatus(200, "1.1", "OK");
  }

  public static ResponseStatus HTTPNotFound() {
    return new ResponseStatus(404, "1.1", "Not Found");
  }

  public static ResponseStatus HTTPBadRequest() {
    return new ResponseStatus(400, "1.1", "Bad Request");
  }

  public String toString() {
    return "HTTP/" + this.HTTPversion + " " + this.code + " " + this.msg + "\r\n";
  }

}

class Response {
  private ResponseStatus HTTPstatus;
  private List<String> headers;
  private byte[] content;

  public Response() {
    this.HTTPstatus = ResponseStatus.HTTPOk();
    this.headers = new ArrayList<String>();
    this.content = new byte[0];
  }

  public void setStatus(ResponseStatus s) {
    this.HTTPstatus = s;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getContent() {
    String data = "";

    data += HTTPstatus.toString();

    Iterator i = this.headers.iterator();
    while(i.hasNext()) {
      String h = (String) i.next();
      data += h + "\r\n";
    }
    data += "\r\n" + new String(this.content);

    return data;
  }

  public void setHeader(String hName, String hValue) {
    this.headers.add(hName + ":" + hValue);
  }

  public void setHeader(String hName, int hValue) {
    this.headers.add(hName + ":" + hValue);
  }

}

class Request {
  private String command;
  private String URI;
  private Map<String, String> headers;
  private static Logger log = Logger.getLogger("SimpleHTTPServer");

  public Request(String method, String URI, Map<String, String> headers) {
    this.command = method;
    this.URI = URI;
    this.headers = headers;
  }

  public static Request parse(List<String> data) throws Exception {
    String method = "";
    String uri = "";
    HashMap<String, String> headers;

    Pattern p = Pattern.compile("(\\A[A-Z]+) +(.+) +HTTP/1.\\d");
    Matcher m = p.matcher(data.remove(0));
    headers = Request.parseHeaders(data);

    if(m.matches()) {
      method = m.group(1);
      uri = m.group(2);
    } else {
      throw new Exception("Malformed request.");
    }

    return new Request(method, uri, headers);
  }

  private static HashMap<String, String> parseHeaders(List<String> headers) {
    HashMap<String, String> data = new HashMap<String, String>();
    Iterator<String> i = headers.iterator();

    while(i.hasNext()) {
      String h = i.next();
      String[] s = h.split(":", 2);
      data.put(s[0].trim(), s[1].trim());
    }
    return data;
  }

  public Response process() {
    Response res = new Response();
    Date now = new Date();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

    res.setHeader("Date", dateFormatter.format(now));
    res.setHeader("Server", ServerConfig.serverName);
    res.setHeader("Cache-Control", "no-cache");

    if(this.URI.equals("/")) {
      this.URI += "index.html";
    }

    String filePath = ServerConfig.sitePath + this.URI;

    byte[] buf = new byte[4096];
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    File file = new File(filePath);
    FileInputStream fin;

    try {
      fin = new FileInputStream(file);
      while(fin.read(buf) != -1) {
        outputStream.write(buf);
      }
      fin.close();

      log.info("Providing: " + file.getPath());

    } catch(IOException e) {
      e.printStackTrace();
    }

    res.setContent(outputStream.toByteArray());
    res.setHeader("Content-Length", outputStream.size());
    return res;
  }
}

class Connect extends Thread {
  Socket client;
  BufferedReader in = null;
  PrintWriter out = null;
  private static Logger log = Logger.getLogger("SimpleHTTPServer");

  public Connect() {}

  public Connect(Socket clientSocket) {
    client = clientSocket;
    try {
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      out = new PrintWriter(client.getOutputStream());
    } catch(Exception e1) {
      try {
        client.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
      return;
    }
    this.start();
  }

  public void run() {
    try {
      String line;
      ArrayList<String> data = new ArrayList<String>();

      while((line = in.readLine()) != null && (!(line.equals("")))) {
        data.add(line);
      }

      Request req;
      Response res;
      try {
        req = Request.parse(data);
        res = req.process();
        out.write(res.getContent());
        out.flush();
      } catch(Exception e1) {
        client.close();
      }

    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      try {
        client.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
}
