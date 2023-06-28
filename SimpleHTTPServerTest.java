import org.junit.BeforeClass;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

public class SimpleHTTPServerTest {

    static SimpleHTTPServer simpleHTTPServer;

    @BeforeClass
    public static void setup() throws Exception {
        simpleHTTPServer = new SimpleHTTPServer();
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
        assertEquals(106496, connection.getContentLength());
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