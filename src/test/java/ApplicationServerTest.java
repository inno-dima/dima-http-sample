package src.test.java;

import org.junit.BeforeClass;
import org.junit.Test;
import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.config.ServerConfigManager;
import src.main.java.com.zextras.httpserver.socket.ServerSocketListener;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ApplicationServerTest {

    @BeforeClass
    public static void setup() throws Exception {
        ServerConfigManager.getInstance().initConfiguration("src/test/resources/server.properties");
        ServerConfig serverConfig = ServerConfigManager.getInstance().getCurrentConfiguration();
        ServerSocketListener server = new ServerSocketListener(serverConfig);
        server.runAsync();
    }

    @Test
    public void index_get_return_200_OK() throws Exception {
        URL url = new URL("http://localhost:3000/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
    }

    @Test
    public void index_content_length_header() throws Exception {
        URL url = new URL("http://localhost:3000/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(106403, connection.getContentLength());
    }

    @Test
    public void check_binary_content_is_same_as_file() throws Exception {
        //connection.getInputStream();
    }

    @Test
    public void check_text_content_is_same_as_file() throws Exception {
        //connection.getInputStream();
    }

    @Test
    public void check_big_content_is_same_as_file() throws Exception {
        //connection.getInputStream();
    }
}