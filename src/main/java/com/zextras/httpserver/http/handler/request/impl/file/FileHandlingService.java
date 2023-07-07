package src.main.java.com.zextras.httpserver.http.handler.request.impl.file;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import src.main.java.com.zextras.httpserver.config.ServerConfigFactory;
import src.main.java.com.zextras.httpserver.exception.BadRequestException;
import src.main.java.com.zextras.httpserver.exception.InternalServerException;
import src.main.java.com.zextras.httpserver.exception.NotFoundException;
import src.main.java.com.zextras.httpserver.http.HeaderConstants;
import src.main.java.com.zextras.httpserver.http.HttpResponse;
import src.main.java.com.zextras.httpserver.http.HttpResponseStatus;
import src.main.java.com.zextras.httpserver.http.handler.request.util.RequestUriUtil;

public class FileHandlingService {
  private static final UnaryOperator<String> MIME_TABLE =
      URLConnection.getFileNameMap()::getContentTypeFor;
  private static final String PATH_PARAMETER = "path";

  private final Path root;

  public FileHandlingService() {
    this.root =
        Paths.get(ServerConfigFactory.getInstance().getCurrentConfiguration().getServerRootPath());
  }

  public HttpResponse handleRequest(String uri) {
    String pathString = RequestUriUtil.parseHttpParams(uri).get(PATH_PARAMETER);
    if (pathString != null) {
      return prepareResponse(uri, pathString);
    }
    throw new BadRequestException();
  }

  private HttpResponse prepareResponse(String uri, String pathString) {
    Path path = getPath(pathString);
    if (path.toFile().isFile()) {
      return getFileContentResponse(path);
    }
    return getDirectoryResponse(uri, path);
  }

  private HttpResponse getFileContentResponse(Path path) {
    HttpResponse httpResponse = new HttpResponse();
    Map<String, String> headers = httpResponse.getHeaders();
    headers.put(HeaderConstants.CONTENT_TYPE_HEADER, mediaType(path.toString()));
    httpResponse.setStatus(HttpResponseStatus.OK);
    byte[] content = readFile(path);
    headers.put(HeaderConstants.CONTENT_LENGTH_HEADER, Integer.toString(content.length));
    httpResponse.setContent(content);
    return httpResponse;
  }

  private HttpResponse getDirectoryResponse(String uri, Path path) {
    HttpResponse httpResponse = new HttpResponse();
    Map<String, String> headers = httpResponse.getHeaders();
    headers.put(HeaderConstants.CONTENT_TYPE_HEADER, HeaderConstants.HTML_CONTENT_TYPE);
    byte[] bodyBytes = dirListing(uri, path).getBytes(StandardCharsets.UTF_8);
    httpResponse.setStatus(HttpResponseStatus.OK);
    headers.put(HeaderConstants.CONTENT_LENGTH_HEADER, Integer.toString(bodyBytes.length));
    httpResponse.setContent(bodyBytes);
    return httpResponse;
  }

  private Path getPath(String pathString) {
    Path path = Paths.get(pathString);
    if (!isFileReadable(path)) {
      throw new NotFoundException();
    }
    return path;
  }

  private boolean isFileReadable(Path path) {
    try {
      return (Files.isReadable(path) && (path.startsWith(root) || path.equals(root)))
          && !(Files.isHidden(path) || Files.isSymbolicLink(path));
    } catch (Exception e) {
      return false;
    }
  }

  private byte[] readFile(Path path) {
    try {
      return Files.readAllBytes(path);
    } catch (IOException e) {
      throw new InternalServerException();
    }
  }

  private String dirListing(String uri, Path path) {
    StringBuilder sb = new StringBuilder();
    addParentDir(path, sb);
    try (Stream<Path> paths = Files.list(path)) {
      addFileList(sb, paths);
    } catch (IOException e) {
      throw new NotFoundException();
    }
    return String.format(FileHttpPageContent.TEMPLATE, uri, sb);
  }

  private void addParentDir(Path path, StringBuilder sb) {
    Path parent = path.getParent();
    if (isFileReadable(parent)) {
      sb.append(String.format(FileHttpPageContent.HREF, parent.toString(), "Parent Dir"));
    }
  }

  private String mediaType(String file) {
    String type = MIME_TABLE.apply(file);
    return type != null ? type : HeaderConstants.DEFAULT_CONTENT_TYPE;
  }

  private void addFileList(StringBuilder sb, Stream<Path> paths) {
    paths.filter(this::isFileReadable).forEach(p -> sb.append(hrefListItemFor(p)));
  }

  private String hrefListItemFor(Path p) {
    return String.format(FileHttpPageContent.HREF, p.toString(), p.getFileName());
  }
}
