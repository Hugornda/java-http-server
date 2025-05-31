package server.utils;

import server.model.HttpMethod;
import server.model.Request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public final class RequestUtils {

  public static Request parseRequest(String request) {
    String[] lines = request.split("\r\n");
    String[] requestInfo = lines[0].split(" ");
    HttpMethod method = HttpMethod.valueOf( requestInfo[0]);
    String path =  URLDecoder.decode(requestInfo[1], StandardCharsets.UTF_8) ;

    HashMap<String, String> headers = new HashMap<>();
    for (int i = 1; i < lines.length; i++) {
      String line = lines[i];
      String[] split = line.split(": ");
      if (split.length == 2) {
        headers.put(split[0], split[1]);
      }
    }

    return new Request(method, path, headers);
  }

}
