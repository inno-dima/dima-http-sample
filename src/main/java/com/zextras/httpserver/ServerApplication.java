package src.main.java.com.zextras.httpserver;

import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.socket.Connect;

import java.net.*;
import java.util.logging.*;

public class ServerApplication extends Thread {
  private final ServerSocket server;
  private static Logger log = Logger.getLogger("com.zextras.httpserver.SimpleHTTPServer");

  public static void main(String argv[]) throws Exception {
    log.info("**** Simple HTTP Server starts... ****");
    new ServerApplication();
  }

  public ServerApplication() throws Exception {
    server = new ServerSocket(ServerConfig.PORT);
    log.info("Server listening on port: " + ServerConfig.PORT);
    this.start();
  }

  @Override
  public void run() {
    log.info("Waiting for connections.");
    while(true) {
      try {
        Socket client = server.accept();
        log.info("Connection from " + client.getInetAddress());
        new Connect(client);
      } catch(Exception ignored) {}
    }
  }
}

