package src.main.java.com.zextras.httpserver.socket;

import src.main.java.com.zextras.httpserver.exception.InternalServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connect implements AutoCloseable {
    private final InputStream in;
    private final OutputStream out;
    private final Socket socket;

    public Connect(Socket clientSocket) {
        try {
            this.in = clientSocket.getInputStream();
            this.out = clientSocket.getOutputStream();
            this.socket = clientSocket;
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
    }
}
