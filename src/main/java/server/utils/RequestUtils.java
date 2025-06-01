package server.utils;

import server.model.HttpMethod;
import server.model.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

public final class RequestUtils {

  public static Request parseRequest(String request) {
    String[] requestParts = request.split("\r\n\r\n", 2);
    String[] lines = requestParts[0].split("\r\n");
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

    String body = null;
    if (requestParts.length > 1) {
      body = requestParts[1];
    }

    return new Request(method, path, headers, body);
  }

  public static String readRequestToString(BufferedReader in) throws IOException {
    String inputLine;
    StringBuilder headers = new StringBuilder();

    while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
      headers.append(inputLine).append("\r\n");
    }

    if (headers.isEmpty()) {
      return headers.toString();
    }

    int contentLength = 0;
    for (String header: headers.toString().split("\r\n")){
      if ( header.toLowerCase(Locale.ROOT).contains("content-length:")){
        contentLength = Integer.parseInt( header.split(":")[1].trim());
        break;
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

}
