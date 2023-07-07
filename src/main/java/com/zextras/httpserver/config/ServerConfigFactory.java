package src.main.java.com.zextras.httpserver.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;

public class ServerConfigFactory {
  private static final String DEFAULT_CONFIG_PATH = "src/main/resources/server.properties";
  private static final String DEFAULT_WEB_PATH = "root-path";
  private static final String SERVER_PORT = "port";
  private static final String THREAD_COUNT = "thread-count";
  private static ServerConfigFactory configManager;
  private static ServerConfig serverConfig;

  private ServerConfigFactory() {}

  public static ServerConfigFactory getInstance() {
    if (configManager == null) {
      configManager = new ServerConfigFactory();
      initConfiguration(DEFAULT_CONFIG_PATH);
    }
    return configManager;
  }

  public static ServerConfigFactory getInstance(String configPath) {
    if (configManager == null) {
      configManager = new ServerConfigFactory();
      initConfiguration(configPath);
    }
    return configManager;
  }

  private static void initConfiguration(String configPath) {
    Properties properties = new Properties();
    readConfiguration(configPath, properties);
    String rootPath = properties.getProperty(DEFAULT_WEB_PATH);
    int port = Integer.parseInt(properties.getProperty(SERVER_PORT));
    int threadCount = Integer.parseInt(properties.getProperty(THREAD_COUNT));
    serverConfig = new ServerConfig(rootPath, port, threadCount);
  }

  private static void readConfiguration(String configPath, Properties properties) {
    try (FileReader reader = new FileReader(configPath)) {
      properties.load(reader);
    } catch (IOException e) {
      throw new InternalServerException("Cannot load configuration", e);
    }
  }

  public ServerConfig getCurrentConfiguration() {
    if (serverConfig == null) {
      throw new InternalServerException("No Current Config Set");
    }
    return serverConfig;
  }
}
