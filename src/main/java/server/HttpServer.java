package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HttpServer
 */
public class HttpServer {

  ServerSocket serverSocket;
  Socket clientSocket;
  BufferedReader in;

  int port;

  public HttpServer(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    serverSocket = new ServerSocket(port);
    serverSocket.setReuseAddress(true);

    while (true) {
      clientSocket = serverSocket.accept();
      InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
      in = new BufferedReader(inputStreamReader);

      String inputLine;
      StringBuilder request = new StringBuilder();
      while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
        request.append(inputLine).append("\r\n");
      }

      if (request.isEmpty()) {
        continue;
      }

      RequestParser requestParser = new RequestParser();
      Request parsed = requestParser.parseRequest(request.toString());

      sendMessage(parsed);

      clientSocket.close();

    }

  }

  public void sendMessage(Request request) throws IOException {
    String response = "HTTP/1.1 404 Not Found\r\n\r\n";

    if ( request.path.equals("/") ) {
       response = "HTTP/1.1 200 OK\r\n\r\n";
    }
    OutputStream outputStream = clientSocket.getOutputStream();
    outputStream.write(response.getBytes());
    outputStream.close();

  }

  public void stop() throws IOException {
    in.close();
    clientSocket.close();
    serverSocket.close();
  }
}
