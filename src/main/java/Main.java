import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import handlers.EchoHandler;
import handlers.FilesHandler;
import handlers.HealthCheckHandler;
import handlers.UserAgentHandler;
import server.HttpServer;
import server.model.Request;
import server.model.Response;

import static server.model.HttpMethod.*;

public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) throws IOException {
    logger.log(Level.INFO, Arrays.toString(args));
    HashMap<String, Object> executionParameters = ServerConfigUtils.getRunArguments(args);

    logger.log(Level.INFO, executionParameters.toString());
    String resourcePath = "";
    if ( executionParameters.containsKey("directory") ) {
      resourcePath = (String) executionParameters.get("directory");
    }

    HttpServer server = new HttpServer(4221 );
    server.registerRoute(GET, "/", new HealthCheckHandler());
    server.registerRoute(GET, "/echo/{str}", new EchoHandler());
    server.registerRoute(GET, "/user-agent",  new UserAgentHandler());
    server.registerRoute(GET,"/files/{fileName}", new FilesHandler( resourcePath));

    startServer(server);
  }

  private static void startServer(HttpServer server) throws IOException {
    try {
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } finally {
      server.stop();
    }
  }
}
