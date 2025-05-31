package server.model;

import java.util.HashMap;

public class Request {

  private final HttpMethod method;

  private final String path;

  HashMap<String, String> pathVariables = new HashMap<>();

  HashMap<String, String> headers;

  String body;

  public Request(HttpMethod method, String path, HashMap<String, String> headers, String body) {
    this.method = method;
    this.path = path;
    this.headers = headers;
    this.body = body;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public HashMap<String, String> getHeaders() {
    return headers;
  }

  public void putPathVariable(String key, String val) {
    this.pathVariables.put(key, val);
  }

  public HashMap<String, String> getPathVariables() {
    return this.pathVariables;
  }

  public String getBody() {
    return body;
  }

}
