package src.main.java.com.zextras.httpserver.http.handler.request.impl.index;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.config.ServerConfigFactory;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;

public class IndexHandlerService {

  private final ServerConfig serverConfig;

  public IndexHandlerService() {
    this.serverConfig = ServerConfigFactory.getInstance().getCurrentConfiguration();
  }

  public byte[] readFileContent() {
    try {
      return Files.readAllBytes(Paths.get(serverConfig.getServerRootPath() + "/index.html"));
    } catch (IOException e) {
      throw new InternalServerException(e);
    }
  }
}
