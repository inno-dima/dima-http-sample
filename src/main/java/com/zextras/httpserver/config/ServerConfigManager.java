package src.main.java.com.zextras.httpserver.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;

public class ServerConfigManager {
  private static ServerConfigManager configManager;
  private ServerConfig serverConfig;

  private ServerConfigManager() {}

  public static ServerConfigManager getInstance() {
    if (configManager == null) {
      configManager = new ServerConfigManager();
    }
    return configManager;
  }

  public ServerConfig getCurrentConfiguration() {
    if (serverConfig == null) {
      throw new InternalServerException("No Current Config Set");
    }
    return serverConfig;
  }

  public void initConfiguration(String configPath) throws IOException {
    Properties properties = new Properties();
    try (FileReader reader = new FileReader(configPath)) {
      properties.load(reader);
    }
    String rootPath = properties.getProperty("root-path");
    int port = Integer.parseInt(properties.getProperty("port"));
    int threadCount = Integer.parseInt(properties.getProperty("thread-count"));
    this.serverConfig = new ServerConfig(rootPath, port, threadCount);
  }
}
