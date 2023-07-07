package src.main.java.com.zextras.httpserver.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;
import src.main.java.com.zextras.httpserver.http.handler.connection.HttpConnectionHandler;
import src.main.java.com.zextras.httpserver.http.handler.connection.HttpConnectionHandlerFactory;

public class ServerSocketListener {
  private static final Logger log =
      Logger.getLogger("com.zextras.httpserver.socket.ServerSocketListener");

  private static final CountDownLatch latch = new CountDownLatch(1);

  private final ServerConfig serverConfig;

  private ExecutorService executor;

  public ServerSocketListener(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
  }

  private static void processNewConnection(Socket clientSocket) {
    HttpConnectionHandler handler =
        HttpConnectionHandlerFactory.getHttpConnectionHandler();
    ConnectionRequest connectionRequest = new ConnectionRequest(clientSocket);
    handler.processConnection(connectionRequest);
  }

  public void start() {
    startListening();
  }

  public void startAsync() throws InterruptedException {
    CompletableFuture.supplyAsync(
        () -> {
          startListening();
          return null;
        });
    latch.await();
  }

  private void startListening() {
    ServerSocket socket = createSocket();
    initShutdownHook(socket);
    initThreadPool();
    log.info("Waiting for connections.");
    latch.countDown();
    listen(socket);
  }

  private void listen(ServerSocket socket) {
    while (socket.isBound() && !socket.isClosed()) {
      try {
        Socket clientSocket = socket.accept();
        log.info("Connection from " + clientSocket.getInetAddress());
        executor.submit(
            () -> processNewConnection(clientSocket));
      } catch (IOException e) {
        executor.shutdown();
        throw new InternalServerException("Unexpected problem during Socket listening", e);
      }
    }
  }

  private ServerSocket createSocket() {
    try {
      log.info("Server listening on port: " + serverConfig.getPort());
      return new ServerSocket(serverConfig.getPort());
    } catch (IOException e) {
      throw new InternalServerException(e);
    }
  }

  private void initThreadPool() {
    this.executor = Executors.newFixedThreadPool(serverConfig.getThreadCount());
  }

  private void initShutdownHook(ServerSocket serverSocket) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> processShutdown(serverSocket)));
  }

  private void processShutdown(ServerSocket serverSocket) {
    try {
      log.info("Shutting down HTTP Server...");

      if (!serverSocket.isClosed()) {
        serverSocket.close();
      }
      this.executor.shutdown();
    } catch (IOException e) {
      log.warning("Error on shutting down.");
      System.exit(1);
    }
  }
}
