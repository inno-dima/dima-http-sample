package src.test.java;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.BeforeClass;
import org.junit.Test;
import src.main.java.com.zextras.httpserver.config.ServerConfig;
import src.main.java.com.zextras.httpserver.config.ServerConfigFactory;
import src.main.java.com.zextras.httpserver.socket.ServerSocketListener;

public class ApplicationServerTest {

  private static final String SERVER_URL = "http://localhost:3000";

  @BeforeClass
  public static void setup() throws Exception {
    ServerConfig serverConfig =
        ServerConfigFactory.getInstance("src/test/resources/server.properties")
            .getCurrentConfiguration();
    ServerSocketListener server = new ServerSocketListener(serverConfig);
    server.startAsync();
  }

  @Test
  public void index_get_return_200_OK() throws Exception {
    URL url = new URL(SERVER_URL + "/");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
    url = new URL(SERVER_URL + "/index.html");
    connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
  }

  @Test
  public void index_get_return_404_NOT_FOUND() throws Exception {
    URL url = new URL(SERVER_URL + "/xxx");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
  }

  @Test
  public void index_content_length_equals() throws Exception {
    URL url = new URL(SERVER_URL + "/");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(106403, connection.getContentLength());
  }

  @Test
  public void check_index_content_same_as_file() throws Exception {
    URL url = new URL(SERVER_URL + "/index.html");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    byte[] expected =
        readAllBytes(
            Objects.requireNonNull(
                ApplicationServerTest.class.getResourceAsStream("../resources/files/index.html")));
    byte[] actual = readAllBytes(connection.getInputStream());
    assertArrayEquals(expected, actual);
  }

  @Test
  public void file_get_return_200_OK() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/parent/dir/file.txt");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
  }

  @Test
  public void file_content_same_as_file() throws Exception {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/parent/dir/file.txt");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    byte[] expected =
        readAllBytes(
            Objects.requireNonNull(
                ApplicationServerTest.class.getResourceAsStream(
                    "../resources/files/parent/dir/file.txt")));
    byte[] actual = readAllBytes(connection.getInputStream());
    assertArrayEquals(expected, actual);
  }

  @Test
  public void file_big_content_same_as_file() throws Exception {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/parent/dir/large.pdf");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    byte[] expected =
        readAllBytes(
            Objects.requireNonNull(
                ApplicationServerTest.class.getResourceAsStream(
                    "../resources/files/parent/dir/large.pdf")));
    byte[] actual = readAllBytes(connection.getInputStream());
    assertArrayEquals(expected, actual);
  }

  @Test
  public void file_get_return_404_NOT_FOUND() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=html/parent/dir/file1.txt");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
  }

  @Test
  public void file_get_return_400_BAD_REQUEST() throws IOException {
    URL url = new URL(SERVER_URL + "/file");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, connection.getResponseCode());
  }

  @Test
  public void file_get_outside_root_return_404_NOT_FOUND() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=html/index.html");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
  }

  @Test
  public void file_get_dir_contains_files_return_200_OK() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/parent/dir/");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    String content = new String(readAllBytes(connection.getInputStream()));
    assertTrue(
        content.contains(Paths.get("src/test/resources/files/parent/dir/file.txt").toString()));
    assertTrue(
        content.contains(Paths.get("src/test/resources/files/parent/dir/image.jpeg").toString()));
    assertTrue(
        content.contains(Paths.get("src/test/resources/files/parent/dir/large.pdf").toString()));
    assertTrue(content.contains("Parent Dir"));
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
  }

  @Test
  public void file_get_dir_not_contains_parent_return_200_OK() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    String content = new String(readAllBytes(connection.getInputStream()));
    assertTrue(content.contains(Paths.get("src/test/resources/files/parent").toString()));
    assertFalse(content.contains("Parent Dir"));
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
  }

  @Test
  public void file_test_compression_return_200_OK() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/parent/dir/large.pdf");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("Accept-Encoding", "gzip");
    connection.connect();
    byte[] compressed = readAllBytes(connection.getInputStream());
    byte[] actualSize =
        readAllBytes(
            Objects.requireNonNull(
                ApplicationServerTest.class.getResourceAsStream(
                    "../resources/files/parent/dir/large.pdf")));
    assertTrue(compressed.length < actualSize.length);
    assertTrue("gzip".equalsIgnoreCase(connection.getHeaderField("Content-Encoding")));
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
  }

  @Test
  public void file_test_wrong_compression_return_400_BAD_REQUEST() throws IOException {
    URL url = new URL(SERVER_URL + "/file?path=src/test/resources/files/parent/dir/large.pdf");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("Accept-Encoding", "xxx");
    connection.connect();
    assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, connection.getResponseCode());
  }

  private byte[] readAllBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[16384];

    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    return buffer.toByteArray();
  }
}
