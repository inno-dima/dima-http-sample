package src.main.java.com.zextras.httpserver.socket;

import src.main.java.com.zextras.httpserver.http.HttpRequest;
import src.main.java.com.zextras.httpserver.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Connect extends Thread {
    Socket client;
    BufferedReader in = null;
    PrintWriter out = null;
    private static final Logger log = Logger.getLogger("com.zextras.httpserver.SimpleHTTPServer");

    public Connect() {
    }

    public Connect(Socket clientSocket) {
        client = clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream());
        } catch (Exception e1) {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        this.start();
    }

    public void run() {
        try {
            String line;
      ArrayList<String> data = new ArrayList<>();

            while ((line = in.readLine()) != null && (!(line.equals("")))) {
                data.add(line);
            }

            HttpRequest req;
            HttpResponse res;
            try {
                req = HttpRequest.parse(data);
                res = req.process();
                out.write(res.getContent());
                out.flush();
            } catch (Exception e1) {
                client.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
