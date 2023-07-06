package src.main.java.com.zextras.httpserver.http.handler.request.impl.index;

import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IndexHandlerService {

    private final ServerConfig serverConfig;

    public IndexHandlerService() {
        this.serverConfig = new ServerConfig();
    }

    public byte[] readFileContent() {
        try {
            return Files.readAllBytes(Paths.get(serverConfig.getSitePath() + "/index.html"));
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
