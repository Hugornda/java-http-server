package server;

import utils.RequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

/**
 * HttpServer
 */
public class HttpServer {

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

      String request = readRequestToString(in).toString();

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
      e.printStackTrace();
    }
  }

  public void registerRoute(String method, String path, Function<Request,Response> requestHandler){
    router.addRoute(new Route(method, path), requestHandler);
  }

  private void registerDefaultErrorRoute() {
    this.router.registerRouteNotFound(( request )-> new Response(404, "Not Found"));
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      running = false;
      try {
        serverSocket.close();
      }catch (IOException e){
        e.printStackTrace();
      }
    }));
  }

  private StringBuilder readRequestToString(BufferedReader in) throws IOException {
    String inputLine;
    StringBuilder request = new StringBuilder();
    while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
      request.append(inputLine).append("\r\n");
    }
    return request;
  }

  public void stop() throws IOException {
    in.close();
    serverSocket.close();
  }
}
