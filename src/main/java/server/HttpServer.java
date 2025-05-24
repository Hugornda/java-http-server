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
  Socket clientSocket;
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
      clientSocket = serverSocket.accept();
      new Thread(this::handleClientRequest).start();
    }
  }

  private void handleClientRequest()  {
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
      in = new BufferedReader(inputStreamReader);
      String request = readRequestToString().toString();

      if (request.isEmpty()) {
        return;
      }

      Request parsed = RequestUtils.parseRequest(request);

      sendMessage(parsed);
      clientSocket.close();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void registerRoute(String method, String path, Function<Request,Response> requestHandler){
    router.addRoute(new Route(method, path), requestHandler);
  }


  private void sendMessage(Request request) throws IOException {
    Response response = router.handle(request);
    OutputStream outputStream = clientSocket.getOutputStream();
    outputStream.write(response.toString().getBytes());
    outputStream.close();

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

  private StringBuilder readRequestToString() throws IOException {
    String inputLine;
    StringBuilder request = new StringBuilder();
    while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
      request.append(inputLine).append("\r\n");
    }
    return request;
  }

  public void stop() throws IOException {
    in.close();
    clientSocket.close();
    serverSocket.close();
  }
}
