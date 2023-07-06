package src.main.java.com.zextras.httpserver.config;

public class ServerConfig {
    private final String serverRootPath;
    private final int port;
    private final int threadCount;

    public ServerConfig(String serverRootPath, int port, int threadCount) {
        this.serverRootPath = serverRootPath;
        this.port = port;
        this.threadCount = threadCount;
    }


    public int getPort() {
        return port;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public String getServerRootPath() {
        return serverRootPath;
    }
}
