package server;

import server.config.ServerConfiguration;
import server.handlers.HandlerFunction;
import server.handlers.NotFoundHandlerFunction;
import server.model.HttpMethod;
import server.model.Request;
import server.model.Response;
import server.router.Route;
import server.router.Router;
import server.utils.RequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HttpServer
 */
public class HttpServer {

  private static final Logger logger = Logger.getLogger(HttpServer.class.getName());
  Router router = new Router();
  ServerSocket serverSocket;
  BufferedReader in;


  int port;
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
      new Thread(() -> handleClientRequest(clientSocket)).start();
    }
  }

  private void handleClientRequest(Socket clientSocket) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         OutputStream outputStream = clientSocket.getOutputStream()) {

      String request = readRequestToString(in);

      if (request.isEmpty()) {
        clientSocket.close();
        return;
      }

      Request parsed = RequestUtils.parseRequest(request);

      Response response = router.handle(parsed);
      outputStream.write(response.toString().getBytes());
      outputStream.flush();
      clientSocket.close();

    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void registerRoute(HttpMethod method, String path, HandlerFunction requestHandler){
    router.addRoute(new Route(method, path), requestHandler);
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

  private String readRequestToString(BufferedReader in) throws IOException {
    String inputLine;
    StringBuilder headers = new StringBuilder();
    while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
      headers.append(inputLine).append("\r\n");
    }
    //get Content type length
    int contentLength = 0;
    for (String header: headers.toString().split("\r\n")){
      if ( header.toLowerCase(Locale.ROOT).contains("content-length:")){
        contentLength = Integer.parseInt( header.split(":")[1].trim());
      }
    }

    //read the next remaining bytes
    char[] bodyBytes = new char[contentLength];
    int read = in.read(bodyBytes);
    String body = "";
    if (read == contentLength){
      body = new String(bodyBytes);
    }


    return headers +"\r\n" + body;
  }

  public void stop() throws IOException {
    in.close();
    serverSocket.close();
  }
}
