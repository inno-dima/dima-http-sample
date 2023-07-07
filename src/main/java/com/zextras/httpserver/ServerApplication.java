package src.main.java.com.zextras.httpserver;

import java.util.logging.Logger;
import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.config.ServerConfigFactory;
import src.main.java.com.zextras.httpserver.socket.ServerSocketListener;

public class ServerApplication {
  private static final Logger log = Logger.getLogger("com.zextras.httpserver.ServerApplication");
  private static final int ZERO = 0;

  public static void main(String[] args) throws Exception {
    ServerConfig config = getServerConfig(args);
    log.info("**** Simple HTTP Server starts... ****");
    log.info("Server listening on port: " + config.getPort());
    new ServerSocketListener(config).start();
  }

  private static ServerConfig getServerConfig(String[] args) {
    if (args.length > ZERO) {
      return ServerConfigFactory.getInstance(args[ZERO]).getCurrentConfiguration();
    }
    return ServerConfigFactory.getInstance().getCurrentConfiguration();
  }
}
