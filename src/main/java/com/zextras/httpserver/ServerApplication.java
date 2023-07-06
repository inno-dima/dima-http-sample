package src.main.java.com.zextras.httpserver;

import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.config.ServerConfigManager;
import src.main.java.com.zextras.httpserver.socket.ServerSocketListener;

import java.util.logging.Logger;

public class ServerApplication {
    private static final Logger log = Logger.getLogger("com.zextras.httpserver.ServerApplication");

    public static void main(String[] args) throws Exception {
        ServerConfigManager.getInstance().initConfiguration("src/main/resources/server.properties");
        ServerConfig config = ServerConfigManager.getInstance().getCurrentConfiguration();
        log.info("**** Simple HTTP Server starts... ****");
        log.info("Server listening on port: " + config.getPort());
        new ServerSocketListener(config).run();
    }

}

