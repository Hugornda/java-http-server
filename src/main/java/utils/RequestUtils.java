package utils;

import server.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class RequestUtils {

  public static Request parseRequest(String request) throws UnsupportedEncodingException {
    String[] lines = request.split("\r\n");
    String[] requestInfo = lines[0].split(" ");
    String method = requestInfo[0];
    String path =  URLDecoder.decode(requestInfo[1], StandardCharsets.UTF_8) ;


    return new Request(method, path);
  }

}
