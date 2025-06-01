package server;

import server.config.ServerConfiguration;
import server.handlers.HandlerFunction;
import server.handlers.NotFoundHandlerFunction;
import server.model.ContentEncoding;
import server.model.HttpMethod;
import server.model.Request;
import server.model.Response;
import server.router.Route;
import server.router.Router;
import server.utils.RequestUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * HttpServer
 */
public class HttpServer {

  private static final Logger logger = Logger.getLogger(HttpServer.class.getName());

  private final Router router = new Router();

  private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

  private final int port;

  private ServerSocket serverSocket;

  private volatile boolean running = true;


  public HttpServer(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    serverSocket = new ServerSocket(port);
    serverSocket.setReuseAddress(true);
    registerDefaultErrorRoute();
    addShutdownHook();

    while (running) {
      Socket clientSocket = serverSocket.accept();
      threadPool.submit(() -> handleClientRequest(clientSocket));
    }
  }

  public void stop() throws IOException {
    serverSocket.close();
  }

  public void registerRoute(HttpMethod method, String path, HandlerFunction requestHandler){
    router.addRoute(new Route(method, path), requestHandler);
  }

  private void handleClientRequest(Socket clientSocket) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         OutputStream outputStream = clientSocket.getOutputStream()) {

      String request = RequestUtils.readRequestToString(in);

      if (request.isEmpty()) {
        clientSocket.close();
        return;
      }

      Request parsed = RequestUtils.parseRequest(request);

      Response response = router.handle(parsed);

      if (parsed.getHeaders().containsKey("Accept-Encoding") && parsed.getHeaders().get("Accept-Encoding").contains("gzip")) {
        response.setEncoding(ContentEncoding.GZIP);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(os);
        byte[] bytes = response.getBody().getBytes();
        gzipOutputStream.write(bytes);
        gzipOutputStream.finish();
        gzipOutputStream.close();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
          sb.append( b);
        }

        response.setBody(sb.toString());
      }

      closeClientSocket(clientSocket, outputStream, response);

    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  private static void closeClientSocket(Socket clientSocket, OutputStream outputStream, Response response) throws IOException {
    outputStream.write(response.toString().getBytes());
    outputStream.flush();
    clientSocket.close();
  }

  private void registerDefaultErrorRoute() {
    this.router.registerRouteNotFound( new NotFoundHandlerFunction());
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      running = false;
      try {
        serverSocket.close();
      }catch (IOException e){
        logger.log(Level.SEVERE, e.getMessage(), e);
      }
    }));
  }
}
