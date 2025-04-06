package server;

public class RequestParser {

  public Request parseRequest(String request) {
    System.out.println(request);
    String[] lines = request.split("\r\n");

    if (lines.length == 0) {
      return null;
    }
    System.out.println(lines);
    String[] requestInfo = lines[0].split(" ");
    System.out.println(requestInfo.toString());
    String method = requestInfo[0];
    String path = requestInfo[1];

    return new Request(method, path);
  }

}
