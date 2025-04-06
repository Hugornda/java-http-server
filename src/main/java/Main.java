import java.io.IOException;

import server.HttpServer;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = new HttpServer(4221);

    try {
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } finally {
      server.stop();
    }

  }
}
