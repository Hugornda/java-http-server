import java.io.IOException;

import server.HttpServer;
import server.Response;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = new HttpServer(4221);
    server.registerRoute("GET", "/", (request) -> new Response(200, "OK"));
    server.registerRoute("GET",
            "/echo/{str}",
            (request) -> new Response(200, "OK", request.getPathVariables().get("str")));
    server.registerRoute("GET", "/user-agent", (request) -> new Response(200,"OK", request.getHeaders().get("User-Agent")));

    try {
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } finally {
      server.stop();
    }

  }
}
